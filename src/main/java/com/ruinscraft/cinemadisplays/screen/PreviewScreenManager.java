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
