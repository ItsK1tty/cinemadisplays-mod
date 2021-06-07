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

package com.ruinscraft.cinemadisplays.screen;

import net.minecraft.util.math.BlockPos;

import java.util.*;

public class ScreenManager {

    private final Map<UUID, Screen> screens;

    public ScreenManager() {
        screens = new HashMap<>();
    }

    public Collection<Screen> getScreens() {
        return screens.values();
    }

    public void setScreens(List<Screen> screens) {
        // Unregister any old screens
        for (Screen screen : this.screens.values()) {
            screen.unregister();
            screen.closeBrowser();
        }

        this.screens.clear();

        // Register new screens
        for (Screen screen : screens) {
            screen.register();
            this.screens.put(screen.getId(), screen);
        }
    }

    public Screen getScreen(UUID screenId) {
        return screens.get(screenId);
    }

    // Used for CefClient LoadHandler
    public Screen getScreen(int browserId) {
        for (Screen screen : screens.values()) {
            if (screen.hasBrowser()) {
                if (screen.getBrowser().getIdentifier() == browserId) {
                    return screen;
                }
            }
        }

        return null;
    }

    public void unloadAll() {
        for (Screen screen : screens.values()) {
            screen.closeBrowser();
            screen.unregister();
        }

        screens.clear();
    }

    public void updateAll() {
        for (Screen screen : screens.values()) {
            if (screen.hasBrowser()) {
                screen.getBrowser().update();
            }
        }
    }

    public Screen getScreen(BlockPos blockPos) {
        for (Screen screen : screens.values()) {
            if (screen.getBlockPos().equals(blockPos)) {
                return screen;
            }
        }

        return null;
    }

}
