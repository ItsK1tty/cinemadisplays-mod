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

import java.util.HashMap;
import java.util.Map;

public class PreviewScreenManager {

    private Map<BlockPos, PreviewScreen> previewScreens;

    public PreviewScreenManager() {
        previewScreens = new HashMap<>();
    }

    public void addPreviewScreen(PreviewScreen previewScreen) {
        if (previewScreens.containsKey(previewScreen.getBlockPos())) {
            previewScreens.remove(previewScreen.getBlockPos()).unregister();
        }

        previewScreen.register();

        previewScreens.put(previewScreen.getBlockPos(), previewScreen);
    }

    public PreviewScreen getPreviewScreen(BlockPos blockPos) {
        return previewScreens.get(blockPos);
    }

    public void unloadAll() {
        previewScreens.values().forEach(PreviewScreen::unregister);
        previewScreens.clear();
    }

}
