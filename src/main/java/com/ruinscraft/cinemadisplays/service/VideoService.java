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

import com.ruinscraft.cinemadisplays.buffer.PacketByteBufSerializable;
import net.minecraft.network.PacketByteBuf;
import org.apache.commons.lang3.NotImplementedException;

public class VideoService implements PacketByteBufSerializable<VideoService> {

    private String name;
    private String url;
    private String setVolumeJs;
    private String startJs;
    private String seekJs;

    public VideoService(String name, String url, String setVolumeJs, String startJs, String seekJs) {
        this.name = name;
        this.url = url;
        this.setVolumeJs = setVolumeJs;
        this.startJs = startJs;
        this.seekJs = seekJs;
    }

    public VideoService() {

    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getSetVolumeJs() {
        return setVolumeJs;
    }

    public String getStartJs() {
        return startJs;
    }

    public String getSeekJs() {
        return seekJs;
    }

    @Override
    public VideoService fromBytes(PacketByteBuf buf) {
        name = buf.readString();
        url = buf.readString();
        setVolumeJs = buf.readString();
        startJs = buf.readString();
        seekJs = buf.readString();
        return this;
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        throw new NotImplementedException("Not implemented on client");
    }

}
