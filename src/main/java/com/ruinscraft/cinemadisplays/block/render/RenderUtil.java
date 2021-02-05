package com.ruinscraft.cinemadisplays.block.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;

import static org.lwjgl.opengl.GL11.GL_QUADS;

public final class RenderUtil {

    public static void fixRotation(MatrixStack matrixStack, String facing) {
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

    public static void moveForward(MatrixStack matrixStack, String facing, float amount) {
        switch (facing) {
            case "NORTH":
                matrixStack.translate(0, 0, -amount);
                break;
            case "WEST":
                matrixStack.translate(-amount, 0, 0);
                break;
            case "EAST":
                matrixStack.translate(amount, 0, 0);
                break;
            default:
                matrixStack.translate(0, 0, amount);
                break;
        }
    }

    public static void moveHorizontal(MatrixStack matrixStack, String facing, float amount) {
        switch (facing) {
            case "NORTH":
                matrixStack.translate(-amount, 0, 0);
                break;
            case "WEST":
                matrixStack.translate(0, 0, amount);
                break;
            case "EAST":
                matrixStack.translate(0, 0, -amount);
                break;
            default:
                matrixStack.translate(amount, 0, 0);
                break;
        }
    }

    public static void moveVertical(MatrixStack matrixStack, float amount) {
        matrixStack.translate(0, amount, 0);
    }

    public static void renderTexture(MatrixStack matrixStack, Tessellator tessellator, BufferBuilder buffer, int glId) {
        RenderSystem.bindTexture(glId);
        Matrix4f matrix4f = matrixStack.peek().getModel();
        buffer.begin(GL_QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
        buffer.vertex(matrix4f, 0.0F, -1.0F, 1.0F).color(255, 255, 255, 255).texture(0.0f, 1.0f).next();
        buffer.vertex(matrix4f, 1.0F, -1.0F, 1.0F).color(255, 255, 255, 255).texture(1.0f, 1.0f).next();
        buffer.vertex(matrix4f, 1.0F, 0.0F, 0.0F).color(255, 255, 255, 255).texture(1.0f, 0.0f).next();
        buffer.vertex(matrix4f, 0, 0, 0).color(255, 255, 255, 255).texture(0.0f, 0.0f).next();
        tessellator.draw();
        RenderSystem.bindTexture(0);
    }

    public static void renderColor(MatrixStack matrixStack, Tessellator tessellator, BufferBuilder buffer, int r, int g, int b) {
        Matrix4f matrix4f = matrixStack.peek().getModel();
        buffer.begin(GL_QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix4f, 0.0F, -1.0F, 1.0F).color(r, g, b, 255).next();
        buffer.vertex(matrix4f, 1.0F, -1.0F, 1.0F).color(r, g, b, 255).next();
        buffer.vertex(matrix4f, 1.0F, 0.0F, 0.0F).color(r, g, b, 255).next();
        buffer.vertex(matrix4f, 0, 0, 0).color(r, g, b, 255).next();
        tessellator.draw();
    }

    public static void renderBlack(MatrixStack matrixStack, Tessellator tessellator, BufferBuilder buffer) {
        renderColor(matrixStack, tessellator, buffer, 0, 0, 0);
    }

}
