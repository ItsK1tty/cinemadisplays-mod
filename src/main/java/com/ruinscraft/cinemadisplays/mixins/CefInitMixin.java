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

package com.ruinscraft.cinemadisplays.mixins;

import com.ruinscraft.cinemadisplays.CinemaDisplaysMod;
import com.ruinscraft.cinemadisplays.cef.CefUtil;
import net.minecraft.client.main.Main;
import org.cef.OS;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * A mixin is used here to load JCEF at the earliest point in the MC bootstrap process
 * See: net.minecraft.client.main.Main
 * This reduces issues with CEF initialization
 * Due to AWT issues on MacOS, we cannot initialize CEF here
 */
@Mixin(Main.class)
public class CefInitMixin {

    @Inject(at = @At("HEAD"), method = "main ([Ljava/lang/String;)V", remap = false)
    private static void cefInit(CallbackInfo info) {
        if (OS.isWindows() || OS.isLinux()) {
            if (CefUtil.init()) {
                CinemaDisplaysMod.LOGGER.info("Chromium Embedded Framework initialized");
            } else {
                CinemaDisplaysMod.LOGGER.warn("Could not initialize Chromium Embedded Framework");
            }
        }
    }

}
