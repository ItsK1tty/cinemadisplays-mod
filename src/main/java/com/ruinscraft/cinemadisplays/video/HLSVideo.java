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
