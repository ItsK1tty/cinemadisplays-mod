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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class FileVideo extends Video {

    private final String url;
    private final boolean loop;

    public FileVideo(String title, String thumbnailUrl, String previewScreenTextureUrl, long durationSeconds, long startedAt, String url, boolean loop) {
        super(title, thumbnailUrl, previewScreenTextureUrl, durationSeconds, startedAt);
        this.url = url;
        this.loop = loop;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlEncoded() {
        try {
            return URLEncoder.encode(getUrl(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }

    public boolean isLoop() {
        return loop;
    }

    @Override
    public void start(CefFrame frame) {
        String js = "th_video('%s');";
        js = String.format(js, getUrlEncoded());
        frame.executeJavaScript(js, frame.getURL(), 0);

        long millisSinceStarted = System.currentTimeMillis() - getStartedAt();
        long secondsSinceStarted = millisSinceStarted / 1000;
        if (secondsSinceStarted < getDurationSeconds()) {
            frame.executeJavaScript(String.format("th_seek(%d);", secondsSinceStarted), frame.getURL(), 0);
        }
    }

}
