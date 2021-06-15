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
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class VideoSettingsScreen extends Screen {

    protected static final Identifier TEXTURE = new Identifier("textures/gui/social_interactions.png");
    private boolean shouldReloadScreen;

    public VideoSettingsScreen() {
        super(Text.of("Video Settings"));
    }

    @Override
    protected void init() {
        addButton(new SliderWidget(method_31362() + 23, 78, 196, 20, Text.of("Volume"),
                CinemaDisplaysMod.getInstance().getVideoSettings().getVolume()) {
            @Override
            protected void updateMessage() {
            }

            @Override
            protected void applyValue() {
                for (com.ruinscraft.cinemadisplays.screen.Screen screen : CinemaDisplaysMod.getInstance().getScreenManager().getScreens())
                    screen.setVideoVolume((float) value);
                CinemaDisplaysMod.getInstance().getVideoSettings().setVolume((float) value);
            }
        });
        addButton(new CheckboxWidget(method_31362() + 23, 110, 196, 20, Text.of("Mute video while alt-tabbed"),
                CinemaDisplaysMod.getInstance().getVideoSettings().isMuteWhenAltTabbed()) {
            @Override
            public void onPress() {
                super.onPress();
                CinemaDisplaysMod.getInstance().getVideoSettings().setMuteWhenAltTabbed(isChecked());
            }
        });
        addButton(new CheckboxWidget(method_31362() + 23, 142, 196, 20, Text.of("Hide crosshair while video playing"),
                CinemaDisplaysMod.getInstance().getVideoSettings().isHideCrosshair()) {
            @Override
            public void onPress() {
                super.onPress();
                CinemaDisplaysMod.getInstance().getVideoSettings().setHideCrosshair(isChecked());
            }
        });
        addButton(new ButtonWidget(method_31362() + 23, 142 + 32, 196, 20,
                Text.of("Screen resolution: " + CinemaDisplaysMod.getInstance().getVideoSettings().getBrowserResolution() + "p"), button -> {
            CinemaDisplaysMod.getInstance().getVideoSettings().setNextBrowserResolution();
            button.setMessage(Text.of("Screen resolution: " + CinemaDisplaysMod.getInstance().getVideoSettings().getBrowserResolution() + "p"));
            shouldReloadScreen = true;
        }, (button, matrices, mouseX, mouseY) -> {
            renderTooltip(matrices, Text.of("A higher resolution may decrease FPS"), mouseX, mouseY);
        }));
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

    public void renderBackground(MatrixStack matrices) {
        int i = this.method_31362() + 3;
        super.renderBackground(matrices);
        this.client.getTextureManager().bindTexture(TEXTURE);
        this.drawTexture(matrices, i, 64, 1, 1, 236, 8);
        int j = this.method_31360();
        for (int k = 0; k < j; ++k)
            this.drawTexture(matrices, i, 72 + 16 * k, 1, 10, 236, 16);
        this.drawTexture(matrices, i, 72 + 16 * j, 1, 27, 236, 8);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.client.textRenderer, Text.of("Video Settings"), this.width / 2, 64 - 10, -1);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        super.onClose();
        CinemaDisplaysMod.getInstance().getVideoSettings().saveAsync();
        if (shouldReloadScreen) {
            for (com.ruinscraft.cinemadisplays.screen.Screen screen : CinemaDisplaysMod.getInstance().getScreenManager().getScreens()) {
                if (screen.hasBrowser()) {
                    screen.reload();
                }
            }
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.client.options.keyInventory.matchesKey(keyCode, scanCode)) {
            onClose();
            return true;
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

}
