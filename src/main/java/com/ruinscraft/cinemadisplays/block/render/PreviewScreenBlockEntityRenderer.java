package com.ruinscraft.cinemadisplays.block.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.ruinscraft.cinemadisplays.CinemaDisplaysMod;
import com.ruinscraft.cinemadisplays.block.PreviewScreenBlockEntity;
import com.ruinscraft.cinemadisplays.screen.PreviewScreen;
import com.ruinscraft.cinemadisplays.screen.PreviewScreenManager;
import com.ruinscraft.cinemadisplays.video.YouTubeVideo;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Quaternion;

import static org.lwjgl.opengl.GL11.*;

public class PreviewScreenBlockEntityRenderer extends BlockEntityRenderer<PreviewScreenBlockEntity> {

    public PreviewScreenBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(PreviewScreenBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        PreviewScreenManager previewScreenManager = CinemaDisplaysMod.getInstance().getPreviewScreenManager();
        PreviewScreen previewScreen = previewScreenManager.getPreviewScreen(entity.getPos());

        if (previewScreen == null) {
            return;
        }

        // General setup
        glDisable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        RenderSystem.enableDepthTest();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        // Screen texture start
        if (previewScreen.getPreviewScreenTexture() != null) {
            matrices.push();
            matrices.translate(1, 1, 0);
            RenderUtil.moveForward(matrices, previewScreen.getFacing(), 0.008f);
            RenderUtil.fixRotation(matrices, previewScreen.getFacing());
            matrices.scale(3, 2, 0);
            RenderUtil.renderTexture(matrices, tessellator, buffer, previewScreen.getPreviewScreenTexture().getGlId());
            matrices.pop();
        }
        // Screen texture end

        // Thumbnail image start
        if (previewScreen.getThumbnailTexture() != null) {
            matrices.push();
            matrices.translate(1, 1, 0);
            RenderUtil.moveHorizontal(matrices, previewScreen.getFacing(), 0.5f);
            RenderUtil.moveVertical(matrices, -1 / 3f);
            RenderUtil.moveForward(matrices, previewScreen.getFacing(), 0.01f);
            RenderUtil.fixRotation(matrices, previewScreen.getFacing());
            matrices.scale(3 / 1.5f, 2 / 1.5f, 0);
            RenderUtil.renderTexture(matrices, tessellator, buffer, previewScreen.getThumbnailTexture().getGlId());
            matrices.pop();
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
            if (previewScreen.getVideo() == null) {
                topText = "NOTHING PLAYING";
                bottomText = "";
            } else {
                topText = previewScreen.getVideo().getTitleShort();
                if (previewScreen.getVideo() instanceof YouTubeVideo) {
                    bottomText = ((YouTubeVideo) previewScreen.getVideo()).getChannelNameShort();
                } else {
                    bottomText = "";
                }
            }
            textRenderer.draw(matrices, topText, 0F, 0F, 16777215);
            RenderUtil.moveVertical(matrices, 78f);
            textRenderer.draw(matrices, bottomText, 0F, 0F, 16777215);
            matrices.pop();
        }
        // Render text end

        glEnable(GL_LIGHTING);
        RenderSystem.disableDepthTest();
    }

    public static void register() {
        BlockEntityRendererRegistry.INSTANCE
                .register(PreviewScreenBlockEntity.PREVIEW_SCREEN_BLOCK_ENTITY, PreviewScreenBlockEntityRenderer::new);
    }

}
