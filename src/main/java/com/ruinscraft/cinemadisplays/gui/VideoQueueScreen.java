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

package com.ruinscraft.cinemadisplays.gui;

import com.ruinscraft.cinemadisplays.CinemaDisplaysMod;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class VideoQueueScreen extends Screen {

    protected static final Identifier TEXTURE = new Identifier("textures/gui/social_interactions.png");

    public VideoQueueScreen() {
        super(Text.of("Video Queue"));
    }

    public void renderBackground(MatrixStack matrices) {
        int i = this.method_31362() + 3;
        super.renderBackground(matrices);
        this.client.getTextureManager().bindTexture(TEXTURE);
        this.drawTexture(matrices, i, 64, 1, 1, 236, 8);
        int j = this.method_31360();

        for (int k = 0; k < j; ++k) {
            this.drawTexture(matrices, i, 72 + 16 * k, 1, 10, 236, 16);
        }

        this.drawTexture(matrices, i, 72 + 16 * j, 1, 27, 236, 8);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }

    private int method_31359() {
        return Math.max(52, this.height - 128 - 16);
    }

    private int method_31360() {
        return this.method_31359() / 16;
    }

    private int method_31361() {
        return 80 + this.method_31360() * 16 - 8;
    }

    private int method_31362() {
        return (this.width - 238) / 2;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (client.options.keyDrop.matchesKey(keyCode, scanCode)) {
            onClose();
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    public static void registerKeyInput() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (!CinemaDisplaysMod.getInstance().getScreenManager().hasActiveScreen()) return;

            if (client.options.keyDrop.wasPressed()) {
                client.openScreen(new VideoQueueScreen());
            }
        });
    }

}
