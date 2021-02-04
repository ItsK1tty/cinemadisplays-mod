package com.ruinscraft.cinemadisplays.screen;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class ScreenManager {

    private Screen active;
    private List<Screen> screens;

    public ScreenManager() {
        screens = new ArrayList<>();
    }

    public List<Screen> getScreens() {
        return screens;
    }

    public void setScreens(List<Screen> screens) {
        // Unregister any old screens
        for (Screen screen : this.screens) {
            screen.unregister();
        }

        // Register new screens
        for (Screen screen : screens) {
            screen.register();
        }

        System.out.println("Loaded " + screens.size() + " screens");

        this.screens = screens;
    }

    public Screen getActive() {
        return active;
    }

    public void setActive(Screen active) {
        this.active = active;
    }

    public boolean hasActive() {
        return active != null;
    }

    public Screen getScreen(BlockPos blockPos) {
        for (Screen screen : screens) {
            if (screen.getBlockPos().equals(blockPos)) {
                return screen;
            }
        }
        return null;
    }

}
