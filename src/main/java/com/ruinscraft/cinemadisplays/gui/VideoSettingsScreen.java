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
import com.ruinscraft.cinemadisplays.gui.widget.AdjustVolumeSliderWidget;
import com.ruinscraft.cinemadisplays.gui.widget.CheckBoxWhiteTextWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class VideoSettingsScreen extends Screen {

    private static final Identifier TEXTURE = new Identifier("cinemadisplays", "textures/gui/gui_generic_small.png");

    private int left;
    private int top;
    private int x;
    private int y;

    public VideoSettingsScreen() {
        super(Text.of("Video Settings"));
        this.x = 248;
        this.y = 226/2;
    }

    @Override
    protected void init() {
        super.init();

        addButton(new AdjustVolumeSliderWidget(left + 10, top + (95 / 2), x - 85, 20));
        addButton(new CheckBoxWhiteTextWidget(left + 10, top + (95 / 4), x, 20, Text.of("Mute video when alt-tabbed")));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.color4f(1F, 1F, 1F, 1F);
        client.getTextureManager().bindTexture(TEXTURE);
        drawTexture(matrices, left, top, 0, 0, x, y);
        drawCenteredText(matrices, client.textRenderer, Text.of("Video settings"), left + 45, top + 10, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        CinemaDisplaysMod.getInstance().getVideoSettings().saveAsync();
        super.onClose();
    }

}
