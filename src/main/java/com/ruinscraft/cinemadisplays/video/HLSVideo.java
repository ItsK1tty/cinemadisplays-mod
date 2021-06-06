package com.ruinscraft.cinemadisplays.video;

import org.cef.browser.CefFrame;

public class HLSVideo extends Video {

    private final String url;

    public HLSVideo(String title, String thumbnailUrl, String previewScreenTextureUrl, long durationSeconds, long startedAt, String url) {
        super(title, thumbnailUrl, previewScreenTextureUrl, durationSeconds, startedAt);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public void start(CefFrame frame) {
        String js = "th_video('%s');";
        js = String.format(js, getUrl());
        frame.executeJavaScript(js, frame.getURL(), 0);
    }

}
