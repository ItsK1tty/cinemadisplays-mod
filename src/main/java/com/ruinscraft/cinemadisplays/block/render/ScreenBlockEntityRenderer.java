package com.ruinscraft.cinemadisplays.block.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.ruinscraft.cinemadisplays.CinemaDisplaysMod;
import com.ruinscraft.cinemadisplays.block.ScreenBlockEntity;
import com.ruinscraft.cinemadisplays.cef.CefUtil;
import com.ruinscraft.cinemadisplays.screen.Screen;
import com.ruinscraft.cinemadisplays.screen.ScreenManager;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;

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

        RenderUtil.moveHorizontal(matrices, screen.getFacing(), 1);
        RenderUtil.moveVertical(matrices, 1);
        RenderUtil.moveForward(matrices, screen.getFacing(), 0.01f);
        RenderUtil.fixRotation(matrices, screen.getFacing());

        matrices.scale(screen.getWidth(), screen.getHeight(), 0);

        // If this is the active screen, render the browser texture if available
        if (screenManager.hasActive()
                && screenManager.getActive().getBlockPos().equals(entity.getPos())
                && CefUtil.hasActiveBrowser()) {
            int glId = CefUtil.getActiveBrowser().renderer_.texture_id_[0];
            RenderUtil.renderTexture(matrices, tessellator, buffer, glId);
        } else {
            RenderUtil.renderBlack(matrices, tessellator, buffer);
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

    public static void register() {
        BlockEntityRendererRegistry.INSTANCE
                .register(ScreenBlockEntity.SCREEN_BLOCK_ENTITY, ScreenBlockEntityRenderer::new);
    }

}
