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
        ScreenManager screenManager = CinemaDisplaysMod.getInstance().getScreenManager();
        Screen screen = screenManager.getScreen(entity.getPos());

        if (screen == null) {
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
        matrices.translate(1 - 0.05, 1, 0);
        fixRotation(matrices, screen.getFacing());
        matrices.scale(screen.getWidth(), screen.getHeight(), 0);

        // If this is the active screen, render the browser texture if available
        if (screenManager.hasActive()
                && screenManager.getActive().getBlockPos().equals(entity.getPos())
                && CefUtil.hasActiveBrowser()) {
            renderBrowserTex(matrices, tessellator, buffer);
        } else {
            renderBlack(matrices, tessellator, buffer);
        }

        glEnable(GL_LIGHTING);
        glEnable(GL_CULL_FACE);
        RenderSystem.disableDepthTest();
        matrices.pop();
    }

    @Override
    public boolean rendersOutsideBoundingBox(ScreenBlockEntity blockEntity) {
        return true;
    }

    private void renderBrowserTex(MatrixStack matrixStack, Tessellator tessellator, BufferBuilder buffer) {
        final int texId = CefUtil.getActiveBrowser().renderer_.texture_id_[0];
        RenderSystem.bindTexture(texId);
        Matrix4f matrix4f = matrixStack.peek().getModel();
        buffer.begin(GL_QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
        buffer.vertex(matrix4f, 0.0F, -1.0F, 1.0F).color(255, 255, 255, 255).texture(0.0f, 1.0f).next();
        buffer.vertex(matrix4f, 1.0F, -1.0F, 1.0F).color(255, 255, 255, 255).texture(1.0f, 1.0f).next();
        buffer.vertex(matrix4f, 1.0F, 0.0F, 0.0F).color(255, 255, 255, 255).texture(1.0f, 0.0f).next();
        buffer.vertex(matrix4f, 0, 0, 0).color(255, 255, 255, 255).texture(0.0f, 0.0f).next();
        tessellator.draw();
        RenderSystem.bindTexture(0);
    }

    private void renderBlack(MatrixStack matrixStack, Tessellator tessellator, BufferBuilder buffer) {
        Matrix4f matrix4f = matrixStack.peek().getModel();
        buffer.begin(GL_QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix4f, 0.0F, -1.0F, 1.0F).color(255, 255, 255, 255).next();
        buffer.vertex(matrix4f, 1.0F, -1.0F, 1.0F).color(255, 255, 255, 255).next();
        buffer.vertex(matrix4f, 1.0F, 0.0F, 0.0F).color(255, 255, 255, 255).next();
        buffer.vertex(matrix4f, 0, 0, 0).color(255, 255, 255, 255).next();
        tessellator.draw();
    }

    private void fixRotation(MatrixStack matrixStack, String facing) {
        final Quaternion rotation;

        switch (facing) {
            case "NORTH":
                rotation = new Quaternion(0, 180, 0, true);
                matrixStack.translate(0, 0, 1);
                break;
            case "WEST":
                rotation = new Quaternion(0, -90, 0, true);
                matrixStack.translate(0, 0, 0);
                break;
            case "EAST":
                rotation = new Quaternion(0, 90, 0, true);
                matrixStack.translate(-1, 0, 1);
                break;
            default:
                rotation = new Quaternion(0, 0, 0, true);
                matrixStack.translate(-1, 0, 0);
                break;
        }

        matrixStack.multiply(rotation);
    }

    public static void register() {
        BlockEntityRendererRegistry.INSTANCE
                .register(ScreenBlockEntity.SCREEN_BLOCK_ENTITY, ScreenBlockEntityRenderer::new);
    }

}
