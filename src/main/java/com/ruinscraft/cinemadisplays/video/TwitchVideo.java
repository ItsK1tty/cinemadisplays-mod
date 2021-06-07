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
