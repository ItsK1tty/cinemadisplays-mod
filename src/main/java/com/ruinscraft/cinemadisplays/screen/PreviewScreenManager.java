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

import java.util.ArrayList;
import java.util.List;

public class PreviewScreenManager {

    private final List<PreviewScreen> previewScreens;

    public PreviewScreenManager() {
        previewScreens = new ArrayList<>();
    }

    public void addPreviewScreen(PreviewScreen previewScreen) {
        PreviewScreen previous = getPreviewScreen(previewScreen.getBlockPos());

        if (previous != null) {
            previous.unregister();
        }

        previewScreen.register();

        previewScreens.add(previewScreen);
    }

    public PreviewScreen getPreviewScreen(BlockPos blockPos) {
        for (PreviewScreen previewScreen : previewScreens) {
            if (previewScreen.getBlockPos().equals(blockPos)) {
                return previewScreen;
            }
        }
        return null;
    }

    public void unloadAll() {
        for (PreviewScreen previewScreen : previewScreens) {
            previewScreen.unregister();
        }

        previewScreens.clear();
    }

}
