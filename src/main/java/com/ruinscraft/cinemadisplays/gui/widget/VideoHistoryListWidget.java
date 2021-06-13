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

import com.ruinscraft.cinemadisplays.video.list.VideoList;
import com.ruinscraft.cinemadisplays.video.list.VideoListEntry;
import net.minecraft.client.MinecraftClient;

import java.util.List;
import java.util.stream.Collectors;

public class VideoHistoryListWidget extends VideoListWidget {

    public VideoHistoryListWidget(VideoList videoList, MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
        super(videoList, client, width, height, top, bottom, itemHeight);
    }

    @Override
    protected List<VideoListWidgetEntry> getWidgetEntries(List<VideoListEntry> entries) {
        return entries.stream()
                .map(entry -> new VideoHistoryListWidgetEntry(this, entry, client))
                .sorted()
                .collect(Collectors.toList());
    }

}
