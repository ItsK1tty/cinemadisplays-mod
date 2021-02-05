package com.ruinscraft.cinemadisplays.video;

import org.cef.browser.CefFrame;

public class YouTubeVideo extends Video {

    private String videoId;
    private String channelName;

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
