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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VideoList {

    private final Map<VideoInfo, VideoListEntry> videos;

    public VideoList(List<VideoListEntry> videos) {
        this.videos = new HashMap<>();
        videos.forEach(video -> this.videos.put(video.getVideoInfo(), video));
    }

    public VideoList() {
        this(new ArrayList<>());
    }

    public List<VideoListEntry> getVideos() {
        return videos.values().stream().sorted().collect(Collectors.toList());
    }

    public void reset() {
        videos.clear();
    }

    public void merge(VideoList other) {
        other.videos.forEach(this.videos::put);
    }

    public void remove(VideoInfo videoInfo) {
        videos.remove(videoInfo);
    }

}
