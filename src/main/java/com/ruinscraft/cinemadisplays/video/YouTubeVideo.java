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

import org.cef.browser.CefFrame;

public class YouTubeVideo extends Video {

    private final String videoId;
    private final String channelName;

    public YouTubeVideo(String title, String thumbnailUrl, String previewScreenTextureUrl, long durationSeconds, long startedAt, String videoId, String channelName) {
        super(title, thumbnailUrl, previewScreenTextureUrl, durationSeconds, startedAt);
        this.videoId = videoId;
        this.channelName = channelName;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getChannelNameShort() {
        if (channelName.length() > 25) {
            return channelName.substring(0, 22) + "...";
        } else {
            return channelName;
        }
    }

    @Override
    public void start(CefFrame frame) {
        String js;
        if (isLivestream()) {
            js = "th_video('%s', true);";
        } else {
            js = "th_video('%s');";
        }
        js = String.format(js, videoId);
        frame.executeJavaScript(js, frame.getURL(), 0);

        if (!isLivestream()) {
            long millisSinceStarted = System.currentTimeMillis() - getStartedAt();
            long secondsSinceStarted = millisSinceStarted / 1000;
            if (secondsSinceStarted < getDurationSeconds()) {
                frame.executeJavaScript(String.format("th_seek(%d);", secondsSinceStarted), frame.getURL(), 0);
            }
        }
    }

}
