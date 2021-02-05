package com.ruinscraft.cinemadisplays;

import com.ruinscraft.cinemadisplays.block.PreviewScreenBlock;
import com.ruinscraft.cinemadisplays.block.PreviewScreenBlockEntity;
import com.ruinscraft.cinemadisplays.block.ScreenBlock;
import com.ruinscraft.cinemadisplays.block.ScreenBlockEntity;
import com.ruinscraft.cinemadisplays.block.render.PreviewScreenBlockEntityRenderer;
import com.ruinscraft.cinemadisplays.block.render.ScreenBlockEntityRenderer;
import com.ruinscraft.cinemadisplays.cef.CefLibInjector;
import com.ruinscraft.cinemadisplays.cef.CefUtil;
import com.ruinscraft.cinemadisplays.screen.PreviewScreenManager;
import com.ruinscraft.cinemadisplays.screen.ScreenManager;
import net.fabricmc.api.ModInitializer;

public class CinemaDisplaysMod implements ModInitializer {

    static {
        try {
            CefLibInjector.inject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static CinemaDisplaysMod instance;

    private ScreenManager screenManager;
    private PreviewScreenManager previewScreenManager;

    @Override
    public void onInitialize() {
        System.out.println("Loading Cinema Displays Mod...");

        instance = this;

        try {
            CefUtil.init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        CefUtil.registerCefGuiScreen();
        CefUtil.registerCefTick();

        ScreenBlock.register();
        ScreenBlockEntity.register();
        ScreenBlockEntityRenderer.register();

        PreviewScreenBlock.register();
        PreviewScreenBlockEntity.register();
        PreviewScreenBlockEntityRenderer.register();

        NetworkUtil.init();

        screenManager = new ScreenManager();
        previewScreenManager = new PreviewScreenManager();

        System.out.println("Cinema Displays Mod loaded!");
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    public PreviewScreenManager getPreviewScreenManager() {
        return previewScreenManager;
    }

    public static CinemaDisplaysMod getInstance() {
        return instance;
    }

}
