package com.ruinscraft.cinemadisplays.video;

import org.cef.browser.CefFrame;

public abstract class Video {

    private String title;
    private String thumbnailUrl;
    private String previewScreenTextureUrl;
    private long durationSeconds;
    private long startedAt;

    public Video(String title, String thumbnailUrl, String previewScreenTextureUrl, long durationSeconds, long startedAt) {
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.previewScreenTextureUrl = previewScreenTextureUrl;
        this.durationSeconds = durationSeconds;
        this.startedAt = startedAt;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleShort() {
        if (title.length() > 25) {
            return title.substring(0, 22) + "...";
        } else {
            return title;
        }
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getPreviewScreenTextureUrl() {
        return previewScreenTextureUrl;
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

    public void setVolume(CefFrame frame, float volume) {
        String js = "th_volume('%d');";
        js = String.format(js, (int) (volume * 100));
        frame.executeJavaScript(js, frame.getURL(), 0);
    }

}
