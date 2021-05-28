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
import net.minecraft.util.Util;
import org.cef.OS;

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

        // Temp hack for initializing CEF on macos
        if (OS.isMacintosh()) {
            Util.getBootstrapExecutor().execute(() -> {
                if (CefUtil.init()) {
                    System.out.println("Chromium Embedded Framework initialized");
                } else {
                    System.out.println("Could not initialize Chromium Embedded Framework");
                }
            });
        }

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
