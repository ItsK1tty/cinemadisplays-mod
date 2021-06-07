package com.ruinscraft.cinemadisplays.gui.widget;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.ruinscraft.cinemadisplays.CinemaDisplaysMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CheckBoxWhiteText extends CheckboxWidget {

    private static final Identifier TEXTURE = new Identifier("textures/gui/checkbox.png");

    public CheckBoxWhiteText(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message, CinemaDisplaysMod.getInstance().getVideoSettings().isMuteWhenAltTabbed());
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        minecraftClient.getTextureManager().bindTexture(TEXTURE);
        RenderSystem.enableDepthTest();
        TextRenderer textRenderer = minecraftClient.textRenderer;
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        drawTexture(matrices, this.x, this.y, this.isFocused() ? 20.0F : 0.0F, super.isChecked() ? 20.0F : 0.0F, 20, this.height, 64, 64);
        this.renderBg(matrices, minecraftClient, mouseX, mouseY);
        drawTextWithShadow(matrices, textRenderer, this.getMessage(), this.x + 24, this.y + (this.height - 8) / 2, 16777215);
    }

    @Override
    public void onPress() {
        CinemaDisplaysMod.getInstance().getVideoSettings().setMuteWhenAltTabbed(!isChecked());
        super.onPress();
    }

}
