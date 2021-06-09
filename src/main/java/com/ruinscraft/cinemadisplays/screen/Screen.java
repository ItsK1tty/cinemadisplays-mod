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

import com.ruinscraft.cinemadisplays.block.ScreenBlock;
import com.ruinscraft.cinemadisplays.cef.CefUtil;
import com.ruinscraft.cinemadisplays.video.Video;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.cef.browser.CefBrowserOsr;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Screen {

    private final String world;
    private final int x;
    private final int y;
    private final int z;
    private final String facing;
    private final int width;
    private final int height;
    private final boolean visible;
    private final boolean muted;
    private final UUID id;
    private final List<PreviewScreen> previewScreens;
    private final transient BlockPos blockPos; // used as a cache for performance
    private transient CefBrowserOsr browser;
    private transient Video video;
    private transient boolean unregistered;

    public Screen(String world, int x, int y, int z, String facing, int width, int height, boolean visible, boolean muted, UUID id) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.facing = facing;
        this.width = width;
        this.height = height;
        this.visible = visible;
        this.muted = muted;
        this.id = id;
        previewScreens = new ArrayList<>();
        blockPos = new BlockPos(new Vec3d(x, y, z));
    }

    public String getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public String getFacing() {
        return facing;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isMuted() {
        return muted;
    }

    public UUID getId() {
        return id;
    }

    public List<PreviewScreen> getPreviewScreens() {
        return previewScreens;
    }

    public void addPreviewScreen(PreviewScreen previewScreen) {
        previewScreens.add(previewScreen);
    }

    public CefBrowserOsr getBrowser() {
        return browser;
    }

    public boolean hasBrowser() {
        return browser != null;
    }

    public void loadVideo(Video video) {
        this.video = video;
        closeBrowser();
        browser = CefUtil.createBrowser(video.getVideoInfo().getVideoService().getUrl(), this);
    }

    public void closeBrowser() {
        if (browser != null) {
            browser.close();
            browser = null;
        }
    }

    public Video getVideo() {
        return video;
    }

    public void setVideoVolume(float volume) {
        if (browser != null && video != null) {
            String js = video.getVideoInfo().getVideoService().getSetVolumeJs();

            // 0-100 volume
            if (js.contains("%d")) {
                js = String.format(js, (int) (volume * 100));
            }

            // 0.00-1.00 volume
            else if (js.contains("%f")) {
                js = String.format(js, volume);
            }

            browser.getMainFrame().executeJavaScript(js, browser.getURL(), 0);
        }
    }

    public void startVideo() {
        if (browser != null && video != null) {
            String startJs = video.getVideoInfo().getVideoService().getStartJs();

            if (startJs.contains("%s") && startJs.contains("%b")) {
                startJs = String.format(startJs, video.getVideoInfo().getId(), video.getVideoInfo().isLivestream());
            } else if (startJs.contains("%s")) {
                startJs = String.format(startJs, video.getVideoInfo().getId());
            }

            browser.getMainFrame().executeJavaScript(startJs, browser.getURL(), 0);

            // Seek to current time
            if (!video.getVideoInfo().isLivestream()) {
                long millisSinceStart = System.currentTimeMillis() - video.getStartedAt();
                long secondsSinceStart = millisSinceStart / 1000;
                if (secondsSinceStart < video.getVideoInfo().getDurationSeconds()) {
                    String seekJs = video.getVideoInfo().getVideoService().getSeekJs();

                    if (seekJs.contains("%d")) {
                        seekJs = String.format(seekJs, secondsSinceStart);
                    }

                    browser.getMainFrame().executeJavaScript(seekJs, browser.getURL(), 0);
                }
            }
        }
    }

    public void seekVideo(int seconds) {
        // TODO:
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public void register() {
        if (MinecraftClient.getInstance().world == null) {
            return;
        }

        int chunkX = x >> 4;
        int chunkZ = z >> 4;

        if (MinecraftClient.getInstance().world.isChunkLoaded(chunkX, chunkZ)) {
            MinecraftClient.getInstance().world.setBlockState(getBlockPos(), ScreenBlock.SCREEN_BLOCK.getDefaultState());
        }

        ClientChunkEvents.CHUNK_LOAD.register((clientWorld, worldChunk) -> {
            if (unregistered) {
                return;
            }

            // If the loaded chunk has this screen block in it, place it in the world
            if (worldChunk.getPos().x == chunkX && worldChunk.getPos().z == chunkZ) {
                clientWorld.setBlockState(getBlockPos(), ScreenBlock.SCREEN_BLOCK.getDefaultState());
            }
        });
    }

    public void unregister() {
        unregistered = true;

        if (MinecraftClient.getInstance().world != null) {
            MinecraftClient.getInstance().world.setBlockState(getBlockPos(), Blocks.AIR.getDefaultState());
        }
    }

}
