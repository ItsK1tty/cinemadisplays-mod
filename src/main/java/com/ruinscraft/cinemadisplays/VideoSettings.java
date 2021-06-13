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

package com.ruinscraft.cinemadisplays;

import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class VideoSettings {

    private static final Path PATH = MinecraftClient.getInstance().runDirectory
            .toPath()
            .resolve("config")
            .resolve(CinemaDisplaysMod.MODID)
            .resolve(CinemaDisplaysMod.MODID + ".properties");

    private float volume;
    private boolean muteWhenAltTabbed;

    public VideoSettings(float volume, boolean muteWhenAltTabbed) {
        this.volume = volume;
        this.muteWhenAltTabbed = muteWhenAltTabbed;
    }

    public VideoSettings() {
        volume = 1.0f;
        muteWhenAltTabbed = true;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public boolean isMuteWhenAltTabbed() {
        return muteWhenAltTabbed;
    }

    public void setMuteWhenAltTabbed(boolean muteWhenAltTabbed) {
        this.muteWhenAltTabbed = muteWhenAltTabbed;
    }

    public void saveAsync() {
        CompletableFuture.runAsync(() -> {
            try {
                save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void save() throws IOException {
        File file = PATH.toFile();

        file.getParentFile().mkdirs();

        if (!file.exists()) {
            file.createNewFile();
        }

        Properties properties = new Properties();
        properties.setProperty("volume", String.valueOf(volume));
        properties.setProperty("mute-while-alt-tabbed", String.valueOf(muteWhenAltTabbed));

        try (FileOutputStream output = new FileOutputStream(file)) {
            properties.store(output, null);
        }
    }

    public void load() throws IOException {
        File file = PATH.toFile();

        if (!file.exists()) {
            save();
        }

        Properties properties = new Properties();

        try (FileInputStream input = new FileInputStream(file)) {
            properties.load(input);
        }

        try {
            volume = Float.parseFloat(properties.getProperty("volume"));
            muteWhenAltTabbed = properties.getProperty("mute-while-alt-tabbed").equalsIgnoreCase("true");
        } catch (Exception e) {
            file.delete();
            save();
        }
    }

}
