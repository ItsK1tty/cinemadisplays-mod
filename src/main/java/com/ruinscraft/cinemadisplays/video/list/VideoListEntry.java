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

package com.ruinscraft.cinemadisplays.video.list;

import com.ruinscraft.cinemadisplays.video.VideoInfo;
import org.jetbrains.annotations.NotNull;

public class VideoListEntry implements Comparable<VideoListEntry> {

    private final VideoInfo videoInfo;
    private final long lastRequested;
    private final int timesRequested;

    public VideoListEntry(VideoInfo videoInfo, long lastRequested, int timesRequested) {
        this.videoInfo = videoInfo;
        this.lastRequested = lastRequested;
        this.timesRequested = timesRequested;
    }

    public VideoInfo getVideoInfo() {
        return videoInfo;
    }

    public long getLastRequested() {
        return lastRequested;
    }

    public int getTimesRequested() {
        return Math.min(timesRequested, 999);
    }

    @Override
    public int compareTo(@NotNull VideoListEntry entry) {
        if (lastRequested == entry.lastRequested) {
            return 0;
        } else if (lastRequested < entry.lastRequested) {
            return 1;
        } else {
            return -1;
        }
    }

}
