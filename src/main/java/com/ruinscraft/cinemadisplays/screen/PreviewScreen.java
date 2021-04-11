package com.ruinscraft.cinemadisplays.screen;

import com.ruinscraft.cinemadisplays.block.PreviewScreenBlock;
import com.ruinscraft.cinemadisplays.video.Video;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class PreviewScreen {

    private static NativeImageBackedTexture noVideoPreviewScreenTexture;

    private UUID parentScreenId;
    private String world;
    private int x;
    private int y;
    private int z;
    private String facing;

    private transient NativeImageBackedTexture previewScreenTexture;
    private transient NativeImageBackedTexture thumbnailTexture;
    private transient Video video;
    private transient BlockPos blockPos; // used as a cache for performance
    private transient boolean unregistered;

    public PreviewScreen(UUID parentScreenId, String world, int x, int y, int z, String facing) {
        this.parentScreenId = parentScreenId;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.facing = facing;
        blockPos = new BlockPos(new Vec3d(x, y, z));
    }

    public UUID getParentScreenId() {
        return parentScreenId;
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

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;

        new Thread(() -> {
            if (video == null) {
                if (thumbnailTexture != null) {
                    thumbnailTexture.close();
                    thumbnailTexture = null;
                }

                // Download no video preview screen texture if not already downloaded
                try {
                    URL url = new URL("https://cdn.ruinscraft.com/media/images/flatscreen_bars.jpg");
                    URLConnection conn = url.openConnection();
                    conn.connect();

                    try (InputStream is = url.openStream()) {
                        if (previewScreenTexture != null) {
                            previewScreenTexture.close();
                        }

                        previewScreenTexture = new NativeImageBackedTexture(NativeImage.read(is));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return;
            }

            // Download preview screen
            try {
                URL url = new URL(video.getPreviewScreenTextureUrl());
                URLConnection conn = url.openConnection();
                conn.connect();

                try (InputStream is = url.openStream()) {
                    if (previewScreenTexture != null) {
                        previewScreenTexture.close();
                    }

                    previewScreenTexture = new NativeImageBackedTexture(NativeImage.read(is));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Download video thumbnail
            try {
                URL url = new URL(video.getThumbnailUrl());
                URLConnection conn = url.openConnection();
                conn.connect();

                try (InputStream is = url.openStream()) {
                    if (thumbnailTexture != null) {
                        thumbnailTexture.close();
                    }

                    thumbnailTexture = new NativeImageBackedTexture(NativeImage.read(is));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public NativeImageBackedTexture getPreviewScreenTexture() {
        return previewScreenTexture;
    }

    public NativeImageBackedTexture getThumbnailTexture() {
        return thumbnailTexture;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public void register() {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;

        if (MinecraftClient.getInstance().world.isChunkLoaded(chunkX, chunkZ)) {
            MinecraftClient.getInstance().world.setBlockState(getBlockPos(), PreviewScreenBlock.PREVIEW_SCREEN_BLOCK.getDefaultState());
        }

        ClientChunkEvents.CHUNK_LOAD.register((clientWorld, worldChunk) -> {
            if (unregistered) {
                return;
            }

            // If the loaded chunk has this screen block in it, place it in the world
            if (worldChunk.getPos().x == chunkX && worldChunk.getPos().z == chunkZ) {
                clientWorld.setBlockState(getBlockPos(), PreviewScreenBlock.PREVIEW_SCREEN_BLOCK.getDefaultState());
            }
        });
    }

    public void unregister() {
        unregistered = true;
        MinecraftClient.getInstance().world.setBlockState(getBlockPos(), Blocks.AIR.getDefaultState());
    }

}
