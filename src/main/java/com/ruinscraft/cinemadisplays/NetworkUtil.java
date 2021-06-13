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

package com.ruinscraft.cinemadisplays;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class NetworkUtil {

    private static final JsonParser JSON_PARSER = new JsonParser();
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

    private static String readString(PacketByteBuf buf) {
        int len = buf.readShort();
        byte[] data = new byte[len];
        buf.readBytes(data, 0, len);
        return new String(data, StandardCharsets.UTF_8);
    }

    public static void registerReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_SERVICES, (client, handler, buf, responseSender) -> {
            String videoServicesJsonRaw = readString(buf);

            client.submit(() -> {
                JsonObject root = JSON_PARSER.parse(videoServicesJsonRaw).getAsJsonObject();
                JsonArray videoServicesArray = root.getAsJsonArray("services");

                for (JsonElement jsonElement : videoServicesArray) {
                    JsonObject videoServiceJson = jsonElement.getAsJsonObject();
                    CinemaDisplaysMod.getInstance().getVideoServiceManager().register(deserializeService(videoServiceJson));
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_SCREENS, (client, handler, buf, responseSender) -> {
            String screensJsonRaw = readString(buf);

            client.submit(() -> {
                try {
                    JsonObject root = JSON_PARSER.parse(screensJsonRaw).getAsJsonObject();
                    JsonArray screensArray = root.getAsJsonArray("screens");
                    List<Screen> screens = new ArrayList<>();
                    for (JsonElement jsonElement : screensArray) {
                        JsonObject screenJson = jsonElement.getAsJsonObject();
                        screens.add(deserializeScreen(screenJson));
                    }
                    CinemaDisplaysMod.getInstance().getScreenManager().setScreens(screens);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_LOAD_SCREEN, (client, handler, buf, responseSender) -> {
            UUID screenId = UUID.fromString(readString(buf));
            String videoJsonRaw = readString(buf);

            client.submit(() -> {
                try {
                    Screen screen = CinemaDisplaysMod.getInstance().getScreenManager().getScreen(screenId);
                    if (screen == null) return;
                    JsonObject videoJson = JSON_PARSER.parse(videoJsonRaw).getAsJsonObject();
                    Video video = deserializeVideo(videoJson);
                    screen.loadVideo(video);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_UNLOAD_SCREEN, (client, handler, buf, responseSender) -> {
            UUID screenId = UUID.fromString(readString(buf));

            client.submit(() -> {
                try {
                    Screen screen = CinemaDisplaysMod.getInstance().getScreenManager().getScreen(screenId);
                    if (screen == null) return;
                    screen.closeBrowser();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_UPDATE_PREVIEW_SCREEN, (client, handler, buf, responseSender) -> {
            String previewScreenJsonRaw = readString(buf);

            client.submit(() -> {
                try {
                    JsonObject previewScreenJson = JSON_PARSER.parse(previewScreenJsonRaw).getAsJsonObject();
                    PreviewScreen previewScreen = deserializePreviewScreen(previewScreenJson);
                    PreviewScreenManager previewScreenManager = CinemaDisplaysMod.getInstance().getPreviewScreenManager();
                    if (previewScreenManager.getPreviewScreen(previewScreen.getBlockPos()) != null) {
                        previewScreenManager.getPreviewScreen(previewScreen.getBlockPos()).setVideoInfo(previewScreen.getVideoInfo());
                    } else {
                        previewScreenManager.addPreviewScreen(previewScreen);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_OPEN_SETTINGS_SCREEN, (client, handler, buf, responseSender) -> {
            client.submit(() -> client.openScreen(new VideoSettingsScreen()));
        });

        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_OPEN_HISTORY_SCREEN, (client, handler, buf, responseSender) -> {
            client.submit(() -> client.openScreen(new VideoHistoryScreen()));
        });

        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_VIDEO_LIST_HISTORY_SPLIT, (client, handler, buf, responseSender) -> {
            String videoHistoryJsonRaw = readString(buf);

            client.submit(() -> {
                JsonObject root = JSON_PARSER.parse(videoHistoryJsonRaw).getAsJsonObject();
                JsonArray historyArray = root.get("entries").getAsJsonArray();
                List<VideoListEntry> entries = new ArrayList<>();
                for (JsonElement jsonElement : historyArray) {
                    JsonObject videoListEntryJson = jsonElement.getAsJsonObject();
                    entries.add(deserializeVideoListEntry(videoListEntryJson));
                }
                VideoList videoList = new VideoList(entries);
                CinemaDisplaysMod.getInstance().getVideoListManager().getHistory().merge(videoList);
            });
        });
    }

    private static VideoService deserializeService(JsonObject videoServiceJson) {
        String name = videoServiceJson.get("name").getAsString();
        String url = videoServiceJson.get("url").getAsString();
        String setVolumeJs = videoServiceJson.get("set_volume_js").getAsString();
        String startJs = videoServiceJson.get("start_js").getAsString();
        String seekJs = videoServiceJson.get("seek_js").getAsString();
        return new VideoService(name, url, setVolumeJs, startJs, seekJs);
    }

    private static Screen deserializeScreen(JsonObject screenJson) {
        String world = screenJson.get("world").getAsString();
        int x = screenJson.get("x").getAsInt();
        int y = screenJson.get("y").getAsInt();
        int z = screenJson.get("z").getAsInt();
        String facing = screenJson.get("facing").getAsString();
        int width = screenJson.get("width").getAsInt();
        int height = screenJson.get("height").getAsInt();
        boolean visible = screenJson.get("visible").getAsBoolean();
        boolean muted = screenJson.get("muted").getAsBoolean();
        UUID id = UUID.fromString(screenJson.get("id").getAsString());
        Screen screen = new Screen(world, x, y, z, facing, width, height, visible, muted, id);
        if (screenJson.has("preview_screens")) {
            JsonArray previewScreensJson = screenJson.getAsJsonArray("preview_screens");
            for (JsonElement jsonElement : previewScreensJson) {
                PreviewScreen previewScreen = deserializePreviewScreen(jsonElement.getAsJsonObject());
                screen.addPreviewScreen(previewScreen);
            }
        }
        return screen;
    }

    private static Video deserializeVideo(JsonObject videoJson) {
        VideoInfo videoInfo = deserializeVideoInfo(videoJson.getAsJsonObject("video_info"));
        long startedAt = videoJson.get("started_at").getAsLong();
        return new Video(videoInfo, startedAt);
    }

    private static VideoInfo deserializeVideoInfo(JsonObject jsonObject) {
        String serviceType = jsonObject.get("service_type").getAsString();
        VideoService videoService = CinemaDisplaysMod.getInstance().getVideoServiceManager().getByName(serviceType);
        if (videoService == null) return null;
        String id = jsonObject.get("id").getAsString();
        String title = jsonObject.get("title").getAsString();
        String poster = jsonObject.get("poster").getAsString();
        final String thumbnailUrl;
        if (jsonObject.has("thumbnail_url")) {
            thumbnailUrl = jsonObject.get("thumbnail_url").getAsString();
        } else {
            thumbnailUrl = null;
        }
        long durationSeconds = jsonObject.get("duration_seconds").getAsLong();
        VideoInfo videoInfo = new VideoInfo(videoService, id);
        videoInfo.setTitle(title);
        videoInfo.setPoster(poster);
        videoInfo.setThumbnailUrl(thumbnailUrl);
        videoInfo.setDurationSeconds(durationSeconds);
        return videoInfo;
    }

    private static PreviewScreen deserializePreviewScreen(JsonObject previewScreenJson) {
        String world = previewScreenJson.get("world").getAsString();
        int x = previewScreenJson.get("x").getAsInt();
        int y = previewScreenJson.get("y").getAsInt();
        int z = previewScreenJson.get("z").getAsInt();
        String facing = previewScreenJson.get("facing").getAsString();
        String staticTexture = previewScreenJson.get("static_texture_url").getAsString();
        String activeTexture = previewScreenJson.get("active_texture_url").getAsString();
        PreviewScreen previewScreen = new PreviewScreen(world, x, y, z, facing, staticTexture, activeTexture);
        if (previewScreenJson.has("video_info")) {
            VideoInfo videoInfo = deserializeVideoInfo(previewScreenJson.getAsJsonObject("video_info"));
            previewScreen.setVideoInfo(videoInfo);
        }
        return previewScreen;
    }

    private static VideoListEntry deserializeVideoListEntry(JsonObject videoListEntryJson) {
        VideoInfo videoInfo = deserializeVideoInfo(videoListEntryJson.get("video_info").getAsJsonObject());
        if (videoInfo == null) return null;
        long lastRequested = videoListEntryJson.get("last_requested").getAsLong();
        int timesRequested = videoListEntryJson.get("times_requested").getAsInt();
        return new VideoListEntry(videoInfo, lastRequested, timesRequested);
    }

}
