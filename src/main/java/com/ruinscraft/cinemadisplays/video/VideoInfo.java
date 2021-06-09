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

public class VideoInfo {

    private VideoService videoService;
    private String id;
    private String title;
    private String poster;
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
        if (title.length() > 23) {
            return title.substring(0, 20) + "...";
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

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
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

}
