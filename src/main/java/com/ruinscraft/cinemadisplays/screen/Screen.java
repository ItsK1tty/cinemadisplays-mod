package com.ruinscraft.cinemadisplays.screen;

import com.ruinscraft.cinemadisplays.block.ScreenBlock;
import com.ruinscraft.cinemadisplays.cef.CefUtil;
import com.ruinscraft.cinemadisplays.video.*;
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

    private String world;
    private int x;
    private int y;
    private int z;
    private String facing;
    private int width;
    private int height;
    private boolean visible;
    private boolean muted;
    private UUID id;
    private List<PreviewScreen> previewScreens;

    private transient CefBrowserOsr browser;
    private transient Video video;
    private transient BlockPos blockPos; // used as a cache for performance
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

    public void playVideo(Video video) {
        this.video = video;

        closeBrowser();

        String startUrl;

        if (video instanceof YouTubeVideo) {
            startUrl = "https://cdn.ruinscraft.com/cinema/service/v1/youtube.html";
        } else if (video instanceof FileVideo) {
            FileVideo fileVideo = (FileVideo) video;

            if (fileVideo.isLoop()) {
                startUrl = "https://cdn.ruinscraft.com/cinema/service/v1/fileloop.html";
            } else {
                startUrl = "https://cdn.ruinscraft.com/cinema/service/v1/file.html";
            }
        } else if (video instanceof TwitchVideo) {
            startUrl = "https://cdn.ruinscraft.com/cinema/service/v1/twitch.html";
        } else if (video instanceof HLSVideo) {
            startUrl = "https://cdn.ruinscraft.com/cinema/service/v1/hls.html";
        } else {
            startUrl = null;
        }

        if (startUrl != null) {
            browser = CefUtil.createBrowser(startUrl, this);
        }
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

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public void register() {
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
        MinecraftClient.getInstance().world.setBlockState(getBlockPos(), Blocks.AIR.getDefaultState());
    }

}
