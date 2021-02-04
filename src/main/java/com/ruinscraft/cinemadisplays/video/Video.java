package com.ruinscraft.cinemadisplays.video;

import org.cef.browser.CefFrame;

public abstract class Video {

    private String title;
    private long durationSeconds;
    private long startedAt;

    public Video(String title, long durationSeconds, long startedAt) {
        this.title = title;
        this.durationSeconds = durationSeconds;
        this.startedAt = startedAt;
    }

    public String getTitle() {
        return title;
    }

    public long getDurationSeconds() {
        return durationSeconds;
    }

    public boolean isLivestream() {
        return durationSeconds == 0;
    }

    public long getStartedAt() {
        return startedAt;
    }

    public abstract void start(CefFrame frame);

}
