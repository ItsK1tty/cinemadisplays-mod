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
