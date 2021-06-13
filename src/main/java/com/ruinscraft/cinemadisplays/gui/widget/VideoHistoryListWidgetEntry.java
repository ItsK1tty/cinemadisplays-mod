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

package com.ruinscraft.cinemadisplays.gui.widget;

import com.ruinscraft.cinemadisplays.NetworkUtil;
import com.ruinscraft.cinemadisplays.video.list.VideoListEntry;
import net.minecraft.client.MinecraftClient;

public class VideoHistoryListWidgetEntry extends VideoListWidgetEntry {

    public VideoHistoryListWidgetEntry(VideoListWidget parent, VideoListEntry video, MinecraftClient client) {
        super(parent, video, client);
    }

    @Override
    protected void trashButtonAction(VideoListEntry video) {
        NetworkUtil.sendDeleteHistoryPacket(video.getVideoInfo());
    }

}
