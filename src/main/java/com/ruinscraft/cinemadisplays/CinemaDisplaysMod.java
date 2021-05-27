package com.ruinscraft.cinemadisplays;

import com.ruinscraft.cinemadisplays.block.PreviewScreenBlock;
import com.ruinscraft.cinemadisplays.block.PreviewScreenBlockEntity;
import com.ruinscraft.cinemadisplays.block.ScreenBlock;
import com.ruinscraft.cinemadisplays.block.ScreenBlockEntity;
import com.ruinscraft.cinemadisplays.block.render.PreviewScreenBlockEntityRenderer;
import com.ruinscraft.cinemadisplays.block.render.ScreenBlockEntityRenderer;
import com.ruinscraft.cinemadisplays.cef.CefUtil;
import com.ruinscraft.cinemadisplays.screen.PreviewScreenManager;
import com.ruinscraft.cinemadisplays.screen.ScreenManager;
import net.fabricmc.api.ModInitializer;

public class CinemaDisplaysMod implements ModInitializer {

    private ScreenManager screenManager;
    private PreviewScreenManager previewScreenManager;
    private static CinemaDisplaysMod instance;

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    public PreviewScreenManager getPreviewScreenManager() {
        return previewScreenManager;
    }

    public static CinemaDisplaysMod getInstance() {
        return instance;
    }

    @Override
    public void onInitialize() {
        instance = this;

        CefUtil.registerCefTick();

        // Register ScreenBlock
        ScreenBlock.register();
        ScreenBlockEntity.register();
        ScreenBlockEntityRenderer.register();

        // Register PreviewScreenBlock
        PreviewScreenBlock.register();
        PreviewScreenBlockEntity.register();
        PreviewScreenBlockEntityRenderer.register();

        NetworkUtil.init();

        screenManager = new ScreenManager();
        previewScreenManager = new PreviewScreenManager();
    }

}
