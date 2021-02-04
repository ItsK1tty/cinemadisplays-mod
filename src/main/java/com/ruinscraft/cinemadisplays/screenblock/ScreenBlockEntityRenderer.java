package com.ruinscraft.cinemadisplays.screenblock;

import com.mojang.blaze3d.systems.RenderSystem;
import com.ruinscraft.cinemadisplays.CinemaDisplaysMod;
import com.ruinscraft.cinemadisplays.cef.CefUtil;
import com.ruinscraft.cinemadisplays.screen.Screen;
import com.ruinscraft.cinemadisplays.screen.ScreenManager;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;

import static org.lwjgl.opengl.GL11.*;

public class ScreenBlockEntityRenderer extends BlockEntityRenderer<ScreenBlockEntity> {

    public ScreenBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(ScreenBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (!CefUtil.hasActiveBrowser()) {
            return;
        }

        ScreenManager screenManager = CinemaDisplaysMod.getInstance().getScreenManager();

        if (!screenManager.hasActive()) {
            return;
        }

        Screen active = screenManager.getActive();

        if (!entity.getPos().equals(active.getBlockPos())) {
            return;
        }

        matrices.push();

        glDisable(GL_LIGHTING);
        glDisable(GL_CULL_FACE);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);

        RenderSystem.enableDepthTest();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        final Quaternion rotation;

        switch (active.getFacing()) {
            case "NORTH":
                rotation = new Quaternion(0, 180, 0, true);
                break;
            case "WEST":
                rotation = new Quaternion(0, -90, 0, true);
                matrices.translate(-1, 0, 0);
                break;
            case "EAST":
                rotation = new Quaternion(0, 90, 0, true);
                break;
            default:
                rotation = new Quaternion(0, 0, 0, true);
                break;
        }

        matrices.translate(1, 1, 0);
        matrices.multiply(rotation);
        matrices.scale(active.getWidth(), active.getHeight(), 0);

        final int texId = CefUtil.getActiveBrowser().renderer_.texture_id_[0];
        RenderSystem.bindTexture(texId);

        Matrix4f matrix4f = matrices.peek().getModel();
        buffer.begin(GL_QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
        buffer.vertex(matrix4f, 0.0F, -1.0F, 0.0F).color(255, 255, 255, 255).texture(0.0f, 1.0f).next();
        buffer.vertex(matrix4f, 1.0F, -1.0F, 0.0F).color(255, 255, 255, 255).texture(1.0f, 1.0f).next();
        buffer.vertex(matrix4f, 1.0F, 0.0F, 0.0F).color(255, 255, 255, 255).texture(1.0f, 0.0f).next();
        buffer.vertex(matrix4f, 0.0F, 0.0F, 0.0F).color(255, 255, 255, 255).texture(0.0f, 0.0f).next();

        tessellator.draw();
        RenderSystem.bindTexture(0);

        glEnable(GL_LIGHTING);
        glEnable(GL_CULL_FACE);

        RenderSystem.disableDepthTest();

        matrices.pop();
    }

    public static void register() {
        BlockEntityRendererRegistry.INSTANCE
                .register(ScreenBlockEntity.SCREEN_BLOCK_ENTITY, ScreenBlockEntityRenderer::new);
    }

}
