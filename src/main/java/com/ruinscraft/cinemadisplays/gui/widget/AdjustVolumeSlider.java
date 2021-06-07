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

package com.ruinscraft.cinemadisplays.gui.widget;

import com.ruinscraft.cinemadisplays.CinemaDisplaysMod;
import com.ruinscraft.cinemadisplays.screen.Screen;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class AdjustVolumeSlider extends SliderWidget {

    public AdjustVolumeSlider(int x, int y, int width, int height) {
        super(x, y, width, height, Text.of("Volume"), CinemaDisplaysMod.getInstance().getVideoSettings().getVolume());
    }

    @Override
    protected void updateMessage() {
    }

    @Override
    protected void applyValue() {
        for (Screen screen : CinemaDisplaysMod.getInstance().getScreenManager().getScreens())
            screen.setVolume((float) value);
        CinemaDisplaysMod.getInstance().getVideoSettings().setVolume((float) value);
    }

}
