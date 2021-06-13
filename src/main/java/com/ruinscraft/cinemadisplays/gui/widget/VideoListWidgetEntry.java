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

import com.google.common.collect.ImmutableList;
import com.ruinscraft.cinemadisplays.video.list.VideoListEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.minecraft.client.gui.DrawableHelper.fill;
import static net.minecraft.client.gui.screen.multiplayer.SocialInteractionsPlayerListEntry.GRAY_COLOR;
import static net.minecraft.client.gui.screen.multiplayer.SocialInteractionsPlayerListEntry.WHITE_COLOR;

public class VideoListWidgetEntry extends ElementListWidget.Entry<VideoListWidgetEntry> implements Comparable<VideoListWidgetEntry> {

    private static final Identifier PLAY_TEXTURE = new Identifier("cinemadisplays", "textures/gui/play.png");
    private static final Identifier PLAY_SELECTED_TEXTURE = new Identifier("cinemadisplays", "textures/gui/play_selected.png");
    private static final Identifier TRASH_TEXTURE = new Identifier("cinemadisplays", "textures/gui/trash.png");
    private static final Identifier TRASH_SELECTED_TEXTURE = new Identifier("cinemadisplays", "textures/gui/trash_selected.png");

    private final VideoListEntry video;
    private final List<Element> children;
    protected final MinecraftClient client;
    private boolean requestButtonSelected;
    private boolean trashButtonSelected;

    public VideoListWidgetEntry(VideoListEntry video, MinecraftClient client) {
        this.video = video;
        children = ImmutableList.of();
        this.client = client;
    }

    public VideoListEntry getVideo() {
        return video;
    }

    @Override
    public List<? extends Element> children() {
        return children;
    }

    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        int i = x + 4;
        int j = y + (entryHeight - 24) / 2;
        int k = i;
        int m = y + (entryHeight - 9) / 2;
        fill(matrices, x, y, x + entryWidth, y + entryHeight, GRAY_COLOR);
        client.textRenderer.draw(matrices, video.getVideoInfo().getTitleShort(), (float) k, (float) m, WHITE_COLOR);
        client.textRenderer.draw(matrices, String.valueOf(video.getTimesRequested()), (float) k + 160, (float) m, WHITE_COLOR);
        renderRequestButton(matrices, mouseX, mouseY, i, j);
        renderTrashButton(matrices, mouseX, mouseY, i, j);
    }

    @Override
    public int compareTo(@NotNull VideoListWidgetEntry other) {
        return video.compareTo(other.video);
    }

    private void renderRequestButton(MatrixStack matrices, int mouseX, int mouseY, int i, int j) {
        int reqButtonPosX = i + 185;
        int reqButtonY = j + 5;

        if (mouseX > reqButtonPosX && mouseX < reqButtonPosX + 12 && mouseY > reqButtonY && mouseY < reqButtonY + 12) {
            requestButtonSelected = true;
        } else {
            requestButtonSelected = false;
        }

        if (requestButtonSelected) {
            client.getTextureManager().bindTexture(PLAY_SELECTED_TEXTURE);
        } else {
            client.getTextureManager().bindTexture(PLAY_TEXTURE);
        }

        DrawableHelper.drawTexture(matrices, reqButtonPosX, reqButtonY, 12, 12, 32F, 32F, 8, 8, 8, 8);
    }

    private void renderTrashButton(MatrixStack matrices, int mouseX, int mouseY, int i, int j) {
        int trashButtonPosX = i + 200;
        int trashButtonY = j + 5;

        if (mouseX > trashButtonPosX && mouseX < trashButtonPosX + 12 && mouseY > trashButtonY && mouseY < trashButtonY + 12) {
            trashButtonSelected = true;
        } else {
            trashButtonSelected = false;
        }

        if (trashButtonSelected) {
            client.getTextureManager().bindTexture(TRASH_SELECTED_TEXTURE);
        } else {
            client.getTextureManager().bindTexture(TRASH_TEXTURE);
        }

        DrawableHelper.drawTexture(matrices, trashButtonPosX, trashButtonY, 12, 12, 32F, 32F, 8, 8, 8, 8);
    }

}
