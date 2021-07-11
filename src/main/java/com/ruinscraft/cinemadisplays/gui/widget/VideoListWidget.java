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
import com.ruinscraft.cinemadisplays.video.list.VideoList;
import com.ruinscraft.cinemadisplays.video.list.VideoListEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public abstract class VideoListWidget extends ElementListWidget<VideoListWidgetEntry> {

    protected final VideoList videoList;
    @Nullable
    private String search;

    public VideoListWidget(VideoList videoList, MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
        super(client, width, height, top, bottom, itemHeight);
        this.videoList = videoList;
        this.setRenderBackground(false);
        this.setRenderHorizontalShadows(false);
        update();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        double d = client.getWindow().getScaleFactor();
        RenderSystem.enableScissor((int) ((double) this.getRowLeft() * d), (int) ((double) (this.height - this.bottom) * d), (int) ((double) (this.getScrollbarPositionX() + 6) * d), (int) ((double) (this.height - (this.height - this.bottom) - this.top - 4) * d));
        super.render(matrices, mouseX, mouseY, delta);
        RenderSystem.disableScissor();
    }

    public void update() {
        List<VideoListEntry> entries = videoList.getVideos();
        if (search != null)
            entries.removeIf(entry -> !entry.getVideoInfo().getTitle().toLowerCase(Locale.ROOT).contains(search));
        replaceEntries(getWidgetEntries(entries));
    }

    public void setSearch(@Nullable String search) {
        this.search = search;
        update();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        children().forEach(widgetEntry -> widgetEntry.mouseClicked(mouseX, mouseY, button));
        update();
        return true;
    }

    protected abstract List<VideoListWidgetEntry> getWidgetEntries(List<VideoListEntry> entries);

}
