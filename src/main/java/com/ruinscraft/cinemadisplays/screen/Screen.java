package com.ruinscraft.cinemadisplays.screen;

import com.ruinscraft.cinemadisplays.block.ScreenBlock;
import com.ruinscraft.cinemadisplays.video.Video;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

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
    private UUID id;
    private List<PreviewScreen> previewScreens;

    private transient Video video;
    private transient BlockPos blockPos; // used as a cache for performance
    private transient boolean unregistered;

    public Screen(String world, int x, int y, int z, String facing, int width, int height, UUID id) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.facing = facing;
        this.width = width;
        this.height = height;
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

    public UUID getId() {
        return id;
    }

    public List<PreviewScreen> getPreviewScreens() {
        return previewScreens;
    }

    public void addPreviewScreen(PreviewScreen previewScreen) {
        previewScreens.add(previewScreen);
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
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
