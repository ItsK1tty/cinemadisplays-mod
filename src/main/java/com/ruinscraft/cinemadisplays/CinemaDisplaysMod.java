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
import net.minecraft.util.Util;

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

        Runnable cefInitRunnable = () -> {
            try {
                CefUtil.init();
                System.out.println("Cinema Displays Mod loaded!");
            } catch (Exception e) {
                System.out.println("Could not initialize CEF.");
                System.out.println("Cinema Displays Mod will not display screens.");
                e.printStackTrace();
            }
        };

        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("mac")) {
            // Run CEF init on MC Bootstrap executor
            Util.getBootstrapExecutor().execute(cefInitRunnable);
        } else {
            cefInitRunnable.run();
        }
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
