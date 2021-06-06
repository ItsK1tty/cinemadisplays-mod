package com.ruinscraft.cinemadisplays.mixins;

import com.ruinscraft.cinemadisplays.CinemaDisplaysMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class UnloadScreensMixin {

    @Shadow
    private ClientWorld world;

    @Inject(at = @At("HEAD"), method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V")
    private void disconnect(Screen screen, CallbackInfo info) {
        // Unload video screens
        CinemaDisplaysMod.getInstance().getScreenManager().unloadAll();
        // Unload preview screens
        CinemaDisplaysMod.getInstance().getPreviewScreenManager().unloadAll();
    }

}
