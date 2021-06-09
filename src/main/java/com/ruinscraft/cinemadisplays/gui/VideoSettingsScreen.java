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

package com.ruinscraft.cinemadisplays.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.ruinscraft.cinemadisplays.CinemaDisplaysMod;
import com.ruinscraft.cinemadisplays.gui.widget.AdjustVolumeSlider;
import com.ruinscraft.cinemadisplays.gui.widget.CheckBoxWhiteText;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class VideoSettingsScreen extends Screen {

    private static final Identifier TEXTURE = new Identifier("cinemadisplays", "textures/gui/gui_generic_small.png");

    private static final int X_SIZE = 248;
    private static final int Y_SIZE = 226 / 2;

    private int guiLeft;
    private int guiTop;

    public VideoSettingsScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        this.guiLeft = (width - X_SIZE) / 2;
        this.guiTop = (height - Y_SIZE) / 2;
        addButton(new AdjustVolumeSlider(guiLeft + 10, guiTop + (95 / 2), X_SIZE - 85, 20));
        addButton(new CheckBoxWhiteText(guiLeft + 10, guiTop + (95 / 4), X_SIZE, 20, Text.of("Mute video when alt-tabbed")));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.color4f(1F, 1F, 1F, 1F);
        client.getTextureManager().bindTexture(TEXTURE);
        drawTexture(matrices, guiLeft, guiTop, 0, 0, X_SIZE, Y_SIZE);
        drawCenteredText(matrices, client.textRenderer, Text.of("Video settings"), guiLeft + 45, guiTop + 10, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        CinemaDisplaysMod.getInstance().getVideoSettings().saveAsync();
        super.onClose();
    }

}
