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

package com.ruinscraft.cinemadisplays.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.ruinscraft.cinemadisplays.CinemaDisplaysMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.util.math.MatrixStack;

import java.util.List;
import java.util.stream.Collectors;

public class VideoQueueWidget extends ElementListWidget<VideoQueueWidgetEntry> {

    public VideoQueueWidget(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
        super(client, width, height, top, bottom, itemHeight);
        this.method_31322(false); // Disables dirt texture
        this.method_31323(false); // Disables dirt texture
        update();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        double d = client.getWindow().getScaleFactor();
        RenderSystem.enableScissor((int) ((double) this.getRowLeft() * d), (int) ((double) (this.height - this.bottom) * d), (int) ((double) (this.getScrollbarPositionX() + 6) * d), (int) ((double) (this.height - (this.height - this.bottom) - this.top - 4) * d));
        super.render(matrices, mouseX, mouseY, delta);
        RenderSystem.disableScissor();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        children().forEach(child -> child.mouseClicked(mouseX, mouseY, button));
        return true;
    }

    public void update() {
        List<VideoQueueWidgetEntry> entries = CinemaDisplaysMod.getInstance().getVideoQueue().getVideos().stream()
                .map(entry -> new VideoQueueWidgetEntry(entry, client))
                .collect(Collectors.toList());
        replaceEntries(entries);
    }

}
