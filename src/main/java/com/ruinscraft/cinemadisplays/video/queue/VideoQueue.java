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

package com.ruinscraft.cinemadisplays.video.queue;

import com.ruinscraft.cinemadisplays.buffer.PacketByteBufSerializable;
import com.ruinscraft.cinemadisplays.video.VideoInfo;
import net.minecraft.network.PacketByteBuf;
import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class VideoQueue implements PacketByteBufSerializable<VideoQueue> {

    private List<QueuedVideo> videos;

    public VideoQueue() {
        videos = new ArrayList<>();
    }

    public List<QueuedVideo> getVideos() {
        return videos;
    }

    public void clear() {
        videos.clear();
    }

//    public void setVideos(List<QueuedVideo> videos) {
//        this.videos = videos;
//        // Semi hack, need to improve
//        if (MinecraftClient.getInstance().currentScreen instanceof VideoQueueScreen) {
//            VideoQueueScreen videoQueueScreen = (VideoQueueScreen) MinecraftClient.getInstance().currentScreen;
//            videoQueueScreen.videoQueueWidget.update();
//        }
//    }

    @Override
    public VideoQueue fromBytes(PacketByteBuf buf) {
        List<QueuedVideo> videos = new ArrayList<>();
        int length = buf.readInt();
        for (int i = 0; i < length; i++) {
            VideoInfo videoInfo = new VideoInfo().fromBytes(buf);
            int score = buf.readInt();
            int clientState = buf.readInt();
            videos.add(new QueuedVideo(videoInfo, score, clientState));
        }
        this.videos = videos;
        return this;
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        throw new NotImplementedException("Not implemented on client");
    }

}
