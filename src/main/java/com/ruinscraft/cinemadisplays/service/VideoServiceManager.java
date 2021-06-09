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

package com.ruinscraft.cinemadisplays.service;

import java.util.HashMap;
import java.util.Map;

public class VideoServiceManager {

    private Map<String, VideoService> registry;

    public VideoServiceManager() {
        registry = new HashMap<>();
    }

    public void register(VideoService videoService) {
        registry.put(videoService.getName(), videoService);
    }

    public void unregisterAll() {
        registry.clear();
    }

    public VideoService getByName(String name) {
        return registry.get(name);
    }

}
