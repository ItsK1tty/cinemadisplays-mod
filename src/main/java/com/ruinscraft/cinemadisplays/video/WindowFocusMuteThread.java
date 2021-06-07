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

package com.ruinscraft.cinemadisplays.video;

import com.ruinscraft.cinemadisplays.CinemaDisplaysMod;
import com.ruinscraft.cinemadisplays.screen.Screen;
import net.minecraft.client.MinecraftClient;

public class WindowFocusMuteThread extends Thread {

    private boolean previousState;

    public WindowFocusMuteThread() {
        setDaemon(true);
        setName("window-focus-cef-mute-thread");
    }

    @Override
    public void run() {
        while (MinecraftClient.getInstance().isRunning()) {
            if (CinemaDisplaysMod.getInstance().getVideoSettings().isMuteWhenAltTabbed()) {
                if (MinecraftClient.getInstance().isWindowFocused() && !previousState) {
                    // if currently focused and was previously not focused
                    for (Screen screen : CinemaDisplaysMod.getInstance().getScreenManager().getScreens()) {
                        screen.setVolume(CinemaDisplaysMod.getInstance().getVideoSettings().getVolume());
                    }
                } else if (!MinecraftClient.getInstance().isWindowFocused() && previousState) {
                    // if not focused and was previous focused
                    for (Screen screen : CinemaDisplaysMod.getInstance().getScreenManager().getScreens()) {
                        screen.setVolume(0f);
                    }
                }

                previousState = MinecraftClient.getInstance().isWindowFocused();
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
