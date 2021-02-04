package com.ruinscraft.cinemadisplays.video;

import org.cef.browser.CefFrame;

public class YouTubeVideo extends Video {

    private String videoId;

    public YouTubeVideo(String title, long durationSeconds, long startedAt, String videoId) {
        super("youtube", title, durationSeconds, startedAt);
        this.videoId = videoId;
    }

    public String getVideoId() {
        return videoId;
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
