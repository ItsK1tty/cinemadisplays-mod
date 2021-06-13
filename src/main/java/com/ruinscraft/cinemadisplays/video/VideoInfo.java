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

import com.ruinscraft.cinemadisplays.service.VideoService;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class VideoInfo {

    private final VideoService videoService;
    private final String id;
    private String title;
    private String poster;
    @Nullable
    private String thumbnailUrl;
    private long durationSeconds;

    public VideoInfo(VideoService videoService, String id) {
        this.videoService = videoService;
        this.id = id;
    }

    public VideoService getVideoService() {
        return videoService;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleShort() {
        if (title.length() > 30) {
            return title.substring(0, 27) + "...";
        } else {
            return title;
        }
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    @Nullable
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(@Nullable String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public long getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(long durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public boolean isLivestream() {
        return durationSeconds == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof VideoInfo)) {
            return false;
        }
        VideoInfo videoInfo = (VideoInfo) o;
        return videoService == videoInfo.videoService && Objects.equals(id, videoInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoService, id);
    }

}
