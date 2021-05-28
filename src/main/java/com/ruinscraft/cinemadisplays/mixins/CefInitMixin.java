package com.ruinscraft.cinemadisplays.mixins;

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
                System.out.println("Chromium Embedded Framework initialized");
            } else {
                System.out.println("Could not initialize Chromium Embedded Framework");
            }
        }
    }

}
