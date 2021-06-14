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
import com.ruinscraft.cinemadisplays.CinemaDisplaysMod;
import com.ruinscraft.cinemadisplays.util.NetworkUtil;
import com.ruinscraft.cinemadisplays.video.queue.QueuedVideo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.List;

import static net.minecraft.client.gui.DrawableHelper.drawTexture;
import static net.minecraft.client.gui.DrawableHelper.fill;
import static net.minecraft.client.gui.screen.multiplayer.SocialInteractionsPlayerListEntry.GRAY_COLOR;
import static net.minecraft.client.gui.screen.multiplayer.SocialInteractionsPlayerListEntry.WHITE_COLOR;

public class VideoQueueWidgetEntry extends ElementListWidget.Entry<VideoQueueWidgetEntry> {

    private static final Identifier UPVOTE_TEXTURE = new Identifier(CinemaDisplaysMod.MODID, "textures/gui/upvote.png");
    private static final Identifier UPVOTE_SELECTED_TEXTURE = new Identifier(CinemaDisplaysMod.MODID, "textures/gui/upvote_selected.png");
    private static final Identifier UPVOTE_ACTIVE_TEXTURE = new Identifier(CinemaDisplaysMod.MODID, "textures/gui/upvote_active.png");
    private static final Identifier DOWNVOTE_TEXTURE = new Identifier(CinemaDisplaysMod.MODID, "textures/gui/downvote.png");
    private static final Identifier DOWNVOTE_SELECTED_TEXTURE = new Identifier(CinemaDisplaysMod.MODID, "textures/gui/downvote_selected.png");
    private static final Identifier DOWNVOTE_ACTIVE_TEXTURE = new Identifier(CinemaDisplaysMod.MODID, "textures/gui/downvote_active.png");

    private final QueuedVideo queuedVideo;
    private final List<Element> children;
    protected MinecraftClient client;
    private boolean downVoteButtonSelected;
    private boolean upVoteButtonSelected;

    public VideoQueueWidgetEntry(QueuedVideo queuedVideo, MinecraftClient client) {
        this.queuedVideo = queuedVideo;
        children = ImmutableList.of();
        this.client = client;
    }

    @Override
    public List<? extends Element> children() {
        return children;
    }

    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        int i = x + 4;
        int j = y + (entryHeight - 24) / 2;
        int m = y + (entryHeight - 7) / 2;
        fill(matrices, x, y, x + entryWidth, y + entryHeight, GRAY_COLOR);
        client.textRenderer.draw(matrices, queuedVideo.getVideoInfo().getTitleShort(), (float) i, (float) m, WHITE_COLOR);
        client.textRenderer.draw(matrices, queuedVideo.getScoreString(), (float) i + 160, (float) m, WHITE_COLOR);
        renderDownVoteButton(matrices, mouseX, mouseY, i, j);
        renderUpVoteButton(matrices, mouseX, mouseY, i, j);
    }

    private void renderDownVoteButton(MatrixStack matrices, int mouseX, int mouseY, int i, int j) {
        int downVoteButtonPosX = i + 185;
        int downVoteButtonPosY = j + 7;

        if (mouseX > downVoteButtonPosX && mouseX < downVoteButtonPosX + 12 && mouseY > downVoteButtonPosY && mouseY < downVoteButtonPosY + 12) {
            downVoteButtonSelected = true;
        } else {
            downVoteButtonSelected = false;
        }

        if (queuedVideo.getClientState() == -1) {
            client.getTextureManager().bindTexture(DOWNVOTE_ACTIVE_TEXTURE);
        } else if (downVoteButtonSelected) {
            client.getTextureManager().bindTexture(DOWNVOTE_SELECTED_TEXTURE);
        } else {
            client.getTextureManager().bindTexture(DOWNVOTE_TEXTURE);
        }

        drawTexture(matrices, downVoteButtonPosX, downVoteButtonPosY, 12, 12, 32F, 32F, 8, 8, 8, 8);
    }

    private void renderUpVoteButton(MatrixStack matrices, int mouseX, int mouseY, int i, int j) {
        int upVoteButtonPosX = i + 200;
        int upVoteButtonPosY = j + 3;

        if (mouseX > upVoteButtonPosX && mouseX < upVoteButtonPosX + 12 && mouseY > upVoteButtonPosY && mouseY < upVoteButtonPosY + 12) {
            upVoteButtonSelected = true;
        } else {
            upVoteButtonSelected = false;
        }

        if (queuedVideo.getClientState() == 1) {
            client.getTextureManager().bindTexture(UPVOTE_ACTIVE_TEXTURE);
        } else if (upVoteButtonSelected) {
            client.getTextureManager().bindTexture(UPVOTE_SELECTED_TEXTURE);
        } else {
            client.getTextureManager().bindTexture(UPVOTE_TEXTURE);
        }

        drawTexture(matrices, upVoteButtonPosX, upVoteButtonPosY, 12, 12, 32F, 32F, 8, 8, 8, 8);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (downVoteButtonSelected) {
            NetworkUtil.sendVideoQueueVotePacket(queuedVideo.getVideoInfo(), -1);
        } else if (upVoteButtonSelected) {
            NetworkUtil.sendVideoQueueVotePacket(queuedVideo.getVideoInfo(), 1);
        }

        return true;
    }

}
