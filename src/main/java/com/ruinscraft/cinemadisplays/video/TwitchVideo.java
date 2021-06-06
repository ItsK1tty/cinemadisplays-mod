package com.ruinscraft.cinemadisplays.video;

import org.cef.browser.CefFrame;

public class TwitchVideo extends Video {

    private final String twitchUser;

    public TwitchVideo(String title, String thumbnailUrl, String previewScreenTextureUrl, long durationSeconds, long startedAt, String twitchUser) {
        super(title, thumbnailUrl, previewScreenTextureUrl, durationSeconds, startedAt);
        this.twitchUser = twitchUser;
    }

    public String getTwitchUser() {
        return twitchUser;
    }

    @Override
    public void start(CefFrame frame) {
        String js = "th_video('%s');";
        js = String.format(js, getTwitchUser());
        frame.executeJavaScript(js, frame.getURL(), 0);
    }

}
