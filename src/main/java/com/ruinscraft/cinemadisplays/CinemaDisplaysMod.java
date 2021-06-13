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

package com.ruinscraft.cinemadisplays;

import com.ruinscraft.cinemadisplays.block.PreviewScreenBlock;
import com.ruinscraft.cinemadisplays.block.PreviewScreenBlockEntity;
import com.ruinscraft.cinemadisplays.block.ScreenBlock;
import com.ruinscraft.cinemadisplays.block.ScreenBlockEntity;
import com.ruinscraft.cinemadisplays.block.render.PreviewScreenBlockEntityRenderer;
import com.ruinscraft.cinemadisplays.block.render.ScreenBlockEntityRenderer;
import com.ruinscraft.cinemadisplays.cef.CefUtil;
import com.ruinscraft.cinemadisplays.gui.VideoQueueScreen;
import com.ruinscraft.cinemadisplays.screen.PreviewScreenManager;
import com.ruinscraft.cinemadisplays.screen.ScreenManager;
import com.ruinscraft.cinemadisplays.service.VideoServiceManager;
import com.ruinscraft.cinemadisplays.video.list.VideoListManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cef.OS;

import java.io.IOException;

public class CinemaDisplaysMod implements ModInitializer {

    public static final String MODID = "cinemadisplays";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    private static CinemaDisplaysMod instance;

    public static CinemaDisplaysMod getInstance() {
        return instance;
    }

    private VideoServiceManager videoServiceManager;
    private ScreenManager screenManager;
    private PreviewScreenManager previewScreenManager;
    private VideoSettings videoSettings;
    private VideoListManager videoListManager;

    public VideoServiceManager getVideoServiceManager() {
        return videoServiceManager;
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    public PreviewScreenManager getPreviewScreenManager() {
        return previewScreenManager;
    }

    public VideoSettings getVideoSettings() {
        return videoSettings;
    }

    public VideoListManager getVideoListManager() {
        return videoListManager;
    }

    private static void initCefMac() {
        if (OS.isMacintosh()) {
            Util.getBootstrapExecutor().execute(() -> {
                if (CefUtil.init()) {
                    LOGGER.info("Chromium Embedded Framework initialized for macOS");
                } else {
                    LOGGER.warn("Could not initialize Chromium Embedded Framework for macOS");
                }
            });
        }
    }

    @Override
    public void onInitialize() {
        instance = this;

        // Hack for initializing CEF on macos
        initCefMac();

        CefUtil.registerCefTick();

        // Register ScreenBlock
        ScreenBlock.register();
        ScreenBlockEntity.register();
        ScreenBlockEntityRenderer.register();

        // Register PreviewScreenBlock
        PreviewScreenBlock.register();
        PreviewScreenBlockEntity.register();
        PreviewScreenBlockEntityRenderer.register();

        NetworkUtil.registerReceivers();

        videoServiceManager = new VideoServiceManager();
        screenManager = new ScreenManager();
        previewScreenManager = new PreviewScreenManager();
        videoSettings = new VideoSettings();
        videoListManager = new VideoListManager();

        try {
            videoSettings.load();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.warn("Could not load video settings.");
        }

        new WindowFocusMuteThread().start();

        VideoQueueScreen.registerKeyInput();
    }

}
