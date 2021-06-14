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

package com.ruinscraft.cinemadisplays.util;

import com.ruinscraft.cinemadisplays.CinemaDisplaysMod;
import com.ruinscraft.cinemadisplays.gui.VideoHistoryScreen;
import com.ruinscraft.cinemadisplays.gui.VideoSettingsScreen;
import com.ruinscraft.cinemadisplays.screen.PreviewScreen;
import com.ruinscraft.cinemadisplays.screen.PreviewScreenManager;
import com.ruinscraft.cinemadisplays.screen.Screen;
import com.ruinscraft.cinemadisplays.service.VideoService;
import com.ruinscraft.cinemadisplays.video.Video;
import com.ruinscraft.cinemadisplays.video.VideoInfo;
import com.ruinscraft.cinemadisplays.video.list.VideoList;
import com.ruinscraft.cinemadisplays.video.list.VideoListEntry;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public final class NetworkUtil {

    private static final CinemaDisplaysMod CD = CinemaDisplaysMod.getInstance();
    /* INCOMING */
    private static final Identifier CHANNEL_SERVICES = new Identifier(CinemaDisplaysMod.MODID, "services");
    private static final Identifier CHANNEL_SCREENS = new Identifier(CinemaDisplaysMod.MODID, "screens");
    private static final Identifier CHANNEL_LOAD_SCREEN = new Identifier(CinemaDisplaysMod.MODID, "load_screen");
    private static final Identifier CHANNEL_UNLOAD_SCREEN = new Identifier(CinemaDisplaysMod.MODID, "unload_screen");
    private static final Identifier CHANNEL_UPDATE_PREVIEW_SCREEN = new Identifier(CinemaDisplaysMod.MODID, "update_preview_screen");
    private static final Identifier CHANNEL_OPEN_SETTINGS_SCREEN = new Identifier(CinemaDisplaysMod.MODID, "open_settings_screen");
    private static final Identifier CHANNEL_OPEN_HISTORY_SCREEN = new Identifier(CinemaDisplaysMod.MODID, "open_history_screen");
    private static final Identifier CHANNEL_OPEN_PLAYLISTS_SCREEN = new Identifier(CinemaDisplaysMod.MODID, "open_playlists_screen");
    private static final Identifier CHANNEL_VIDEO_LIST_HISTORY_SPLIT = new Identifier(CinemaDisplaysMod.MODID, "video_list_history_split");
    private static final Identifier CHANNEL_VIDEO_LIST_PLAYLIST_SPLIT = new Identifier(CinemaDisplaysMod.MODID, "video_list_playlist_split");
    private static final Identifier CHANNEL_VIDEO_QUEUE_STATE = new Identifier(CinemaDisplaysMod.MODID, "video_queue_state");
    /* OUTGOING */
    private static final Identifier CHANNEL_VIDEO_REQUEST = new Identifier(CinemaDisplaysMod.MODID, "video_request");
    private static final Identifier CHANNEL_VIDEO_HISTORY_REMOVE = new Identifier(CinemaDisplaysMod.MODID, "video_history_remove");
    private static final Identifier CHANNEL_VIDEO_QUEUE_VOTE = new Identifier(CinemaDisplaysMod.MODID, "video_queue_vote");
    private static final Identifier CHANNEL_VIDEO_QUEUE_REMOVE = new Identifier(CinemaDisplaysMod.MODID, "video_queue_remove");

    public static void registerReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_SERVICES, (client, handler, buf, responseSender) -> {
            int length = buf.readInt();
            for (int i = 0; i < length; i++)
                CD.getVideoServiceManager().register(new VideoService().fromBytes(buf));
        });
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_SCREENS, (client, handler, buf, responseSender) -> {
            int length = buf.readInt();
            for (int i = 0; i < length; i++)
                CD.getScreenManager().registerScreen(new Screen().fromBytes(buf));
        });
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_LOAD_SCREEN, (client, handler, buf, responseSender) -> {
            BlockPos pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
            Screen screen = CD.getScreenManager().getScreen(pos);
            if (screen == null) return;
            Video video = new Video().fromBytes(buf);
            client.submit(() -> screen.loadVideo(video));
        });
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_UNLOAD_SCREEN, (client, handler, buf, responseSender) -> {
            BlockPos pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
            Screen screen = CD.getScreenManager().getScreen(pos);
            if (screen == null) return;
            client.submit(screen::closeBrowser);
        });
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_UPDATE_PREVIEW_SCREEN, (client, handler, buf, responseSender) -> {
            PreviewScreenManager manager = CD.getPreviewScreenManager();
            PreviewScreen previewScreen = new PreviewScreen().fromBytes(buf);
            VideoInfo videoInfo = buf.readBoolean() ? new VideoInfo().fromBytes(buf) : null;
            previewScreen.setVideoInfo(videoInfo);
            if (manager.getPreviewScreen(previewScreen.getBlockPos()) == null)
                manager.addPreviewScreen(previewScreen);
            else
                manager.getPreviewScreen(previewScreen.getBlockPos()).setVideoInfo(videoInfo);
        });
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_OPEN_SETTINGS_SCREEN, (client, handler, buf, responseSender) -> {
            client.submit(() -> client.openScreen(new VideoSettingsScreen()));
        });
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_OPEN_HISTORY_SCREEN, (client, handler, buf, responseSender) -> {
            client.submit(() -> client.openScreen(new VideoHistoryScreen()));
        });
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_VIDEO_LIST_HISTORY_SPLIT, (client, handler, buf, responseSender) -> {
            List<VideoListEntry> entries = new ArrayList<>();
            int length = buf.readInt();
            for (int i = 0; i < length; i++)
                entries.add(new VideoListEntry().fromBytes(buf));
            CD.getVideoListManager().getHistory().merge(new VideoList(entries));
        });
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_VIDEO_LIST_PLAYLIST_SPLIT, (client, handler, buf, responseSender) -> {
            // TODO:
        });
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_VIDEO_QUEUE_STATE, (client, handler, buf, responseSender) -> {
            CD.getVideoQueue().fromBytes(buf);
        });
    }

    public static void sendVideoRequestPacket(VideoInfo videoInfo) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        videoInfo.toBytes(buf);
        ClientPlayNetworking.send(CHANNEL_VIDEO_REQUEST, buf);
    }

    public static void sendDeleteHistoryPacket(VideoInfo videoInfo) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        videoInfo.toBytes(buf);
        ClientPlayNetworking.send(CHANNEL_VIDEO_HISTORY_REMOVE, buf);
    }

    public static void sendVideoQueueVotePacket(VideoInfo videoInfo, int voteType) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        videoInfo.toBytes(buf);
        buf.writeInt(voteType);
        ClientPlayNetworking.send(CHANNEL_VIDEO_QUEUE_VOTE, buf);
    }

}
