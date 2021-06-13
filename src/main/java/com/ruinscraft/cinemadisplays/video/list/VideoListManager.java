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

package com.ruinscraft.cinemadisplays.video.list;

import java.util.HashMap;
import java.util.Map;

public class VideoListManager {

    private VideoList history;
    private Map<String, VideoList> playlists;

    public VideoListManager() {
        history = new VideoList();
        playlists = new HashMap<>();
    }

    public VideoList getHistory() {
        return history;
    }

    public VideoList getPlaylist(String name) {
        return playlists.get(name);
    }

}
