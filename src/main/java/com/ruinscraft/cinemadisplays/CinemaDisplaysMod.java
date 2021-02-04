package com.ruinscraft.cinemadisplays;

import com.ruinscraft.cinemadisplays.cef.CefLibInjector;
import com.ruinscraft.cinemadisplays.cef.CefUtil;
import com.ruinscraft.cinemadisplays.screen.ScreenManager;
import com.ruinscraft.cinemadisplays.screenblock.ScreenBlock;
import com.ruinscraft.cinemadisplays.screenblock.ScreenBlockEntity;
import com.ruinscraft.cinemadisplays.screenblock.ScreenBlockEntityRenderer;
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

        NetworkUtil.init();

        screenManager = new ScreenManager();

        System.out.println("Cinema Displays Mod loaded!");
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    public static CinemaDisplaysMod getInstance() {
        return instance;
    }

}
