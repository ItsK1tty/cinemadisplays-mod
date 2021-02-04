package com.ruinscraft.cinemadisplays.cef;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;

public class CefScreen extends Screen {

    private int width;
    private int height;

    public CefScreen() {
        super(Text.of("CEF Screen"));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (!CefUtil.hasActiveBrowser()) {
            return;
        }

        int currentWidth = MinecraftClient.getInstance().getWindow().getWidth();
        int currentHeight = MinecraftClient.getInstance().getWindow().getHeight();

        if (this.width != currentWidth || this.height != currentHeight) {
            this.width = currentWidth;
            this.height = currentHeight;

            CefUtil.getActiveBrowser().resize(this.width, this.height);
        }

        glEnable(GL_TEXTURE_2D);
        GlStateManager.disableDepthTest();
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        CefUtil.getActiveBrowser().renderer_.render(0, super.height, super.width, 0.0d);
        GlStateManager.enableDepthTest();
    }

}
