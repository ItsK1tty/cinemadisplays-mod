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
import com.ruinscraft.cinemadisplays.block.PreviewScreenBlockEntity;
import com.ruinscraft.cinemadisplays.screen.PreviewScreen;
import com.ruinscraft.cinemadisplays.screen.PreviewScreenManager;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Quaternion;

public class PreviewScreenBlockEntityRenderer extends BlockEntityRenderer<PreviewScreenBlockEntity> {

    public PreviewScreenBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(PreviewScreenBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        PreviewScreenManager previewScreenManager = CinemaDisplaysMod.getInstance().getPreviewScreenManager();
        PreviewScreen previewScreen = previewScreenManager.getPreviewScreen(entity.getPos());
        if (previewScreen == null) return;

        RenderSystem.enableDepthTest();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        // Screen texture start
        {
            NativeImageBackedTexture texture = previewScreen.hasVideoInfo()
                    ? previewScreen.getActiveTexture() : previewScreen.getStaticTexture();

            if (texture != null) {
                matrices.push();
                matrices.translate(1, 1, 0);
                RenderUtil.moveForward(matrices, previewScreen.getFacing(), 0.008f);
                RenderUtil.fixRotation(matrices, previewScreen.getFacing());
                matrices.scale(3, 2, 0);
                RenderUtil.renderTexture(matrices, tessellator, buffer, texture.getGlId());
                matrices.pop();
            }
        }
        // Screen texture end

        // Thumbnail image start
        {
            NativeImageBackedTexture texture = previewScreen.getThumbnailTexture();

            if (texture != null) {
                matrices.push();
                matrices.translate(1, 1, 0);
                RenderUtil.moveHorizontal(matrices, previewScreen.getFacing(), 0.5f);
                RenderUtil.moveVertical(matrices, -1 / 3f);
                RenderUtil.moveForward(matrices, previewScreen.getFacing(), 0.01f);
                RenderUtil.fixRotation(matrices, previewScreen.getFacing());
                matrices.scale(3 / 1.5f, 2 / 1.5f, 0);
                RenderUtil.renderTexture(matrices, tessellator, buffer, texture.getGlId());
                matrices.pop();
            }
        }
        // Thumbnail image end

        // Render text start
        {
            matrices.push();
            matrices.translate(1, 1, 0);
            RenderUtil.moveHorizontal(matrices, previewScreen.getFacing(), 0.1f);
            RenderUtil.moveVertical(matrices, -0.15f);
            RenderUtil.moveForward(matrices, previewScreen.getFacing(), 0.01f);
            RenderUtil.fixRotation(matrices, previewScreen.getFacing());
            matrices.multiply(new Quaternion(180, 0, 0, true));
            matrices.scale(0.02f, 0.02f, 0.02f);
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            final String topText;
            final String bottomText;
            if (previewScreen.hasVideoInfo()) {
                topText = previewScreen.getVideoInfo().getTitleShort();
                bottomText = previewScreen.getVideoInfo().getPoster();
            } else {
                topText = "NOTHING PLAYING";
                bottomText = "";
            }
            textRenderer.draw(matrices, topText, 0F, 0F, 16777215);
            RenderUtil.moveVertical(matrices, 78f);
            textRenderer.draw(matrices, bottomText, 0F, 0F, 16777215);
            matrices.pop();
        }
        // Render text end

        RenderSystem.disableDepthTest();
    }

    public static void register() {
        BlockEntityRendererRegistry.INSTANCE
                .register(PreviewScreenBlockEntity.PREVIEW_SCREEN_BLOCK_ENTITY, PreviewScreenBlockEntityRenderer::new);
    }

}
