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

package com.ruinscraft.cinemadisplays.video;

import com.ruinscraft.cinemadisplays.buffer.PacketByteBufSerializable;
import net.minecraft.network.PacketByteBuf;
import org.apache.commons.lang3.NotImplementedException;

public class Video implements PacketByteBufSerializable<Video> {

    private VideoInfo videoInfo;
    private long startedAt;

    public Video(VideoInfo videoInfo, long startedAt) {
        this.videoInfo = videoInfo;
        this.startedAt = startedAt;
    }

    public Video() {

    }

    public VideoInfo getVideoInfo() {
        return videoInfo;
    }

    public long getStartedAt() {
        return startedAt;
    }

    @Override
    public Video fromBytes(PacketByteBuf buf) {
        videoInfo = new VideoInfo().fromBytes(buf);
        if (videoInfo == null) return null;
        startedAt = buf.readLong();
        return this;
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        throw new NotImplementedException("Not implemented on client");
    }

}
