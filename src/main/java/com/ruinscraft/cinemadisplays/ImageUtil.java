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

import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public final class ImageUtil {

    public static CompletableFuture<NativeImageBackedTexture> fetchImageTextureFromUrl(String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                try (InputStream stream = new URL(url).openStream()) {
                    return new NativeImageBackedTexture(NativeImage.read(stream));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        });
    }

}
