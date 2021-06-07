/*
 * Cinema Displays Mod for Minecraft License
 *
 * Copyright (c) 2021 Ruinscraft, LLC
 *
 * This software is intellectual property of Ruinscraft, LLC and may not
 * be modified, distributed, or used for commercial purposes without
 * explicit written permission from the author.
 *
 * You may use this software for personal or testing purposes as long as
 * you do not modify it, distribute it, or claim to be the original
 * author.
 *
 * If you would like to license this software for commercial use, please
 * email: andersond@ruinscraft.com
 */

package com.ruinscraft.cinemadisplays.block.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.ruinscraft.cinemadisplays.CinemaDisplaysMod;
import com.ruinscraft.cinemadisplays.block.ScreenBlockEntity;
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

        if (screen == null || !screen.isVisible()) {
            return;
        }

        // General setup
        glDisable(GL_LIGHTING);
        glDisable(GL_CULL_FACE);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        RenderSystem.enableDepthTest();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        // Texture rendering
        {
            matrices.push();
            matrices.translate(1, 1, 0);
            RenderUtil.moveForward(matrices, screen.getFacing(), 0.008f);
            RenderUtil.fixRotation(matrices, screen.getFacing());
            matrices.scale(screen.getWidth(), screen.getHeight(), 0);
            if (screen.hasBrowser()) {
                int glId = screen.getBrowser().renderer_.texture_id_[0];
                RenderUtil.renderTexture(matrices, tessellator, buffer, glId);
            } else {
                RenderUtil.renderBlack(matrices, tessellator, buffer);
            }
            matrices.pop();
        }

        // Cleanup
        glEnable(GL_LIGHTING);
        glEnable(GL_CULL_FACE);
        RenderSystem.disableDepthTest();
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
