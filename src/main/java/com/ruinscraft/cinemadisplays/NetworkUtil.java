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
import com.ruinscraft.cinemadisplays.gui.CefSettingsScreen;
import com.ruinscraft.cinemadisplays.screen.PreviewScreen;
import com.ruinscraft.cinemadisplays.screen.PreviewScreenManager;
import com.ruinscraft.cinemadisplays.screen.Screen;
import com.ruinscraft.cinemadisplays.video.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class NetworkUtil {

    private static final JsonParser JSON_PARSER;
    private static final Identifier CHANNEL_SCREENS;
    private static final Identifier CHANNEL_LOAD_SCREEN;
    private static final Identifier CHANNEL_UNLOAD_SCREEN;
    private static final Identifier CHANNEL_UPDATE_PREVIEW_SCREEN;
    private static final Identifier CHANNEL_OPEN_SETTINGS_SCREEN;

    static {
        JSON_PARSER = new JsonParser();
        CHANNEL_SCREENS = new Identifier(CinemaDisplaysMod.MODID, "screens");
        CHANNEL_LOAD_SCREEN = new Identifier(CinemaDisplaysMod.MODID, "load_screen");
        CHANNEL_UNLOAD_SCREEN = new Identifier(CinemaDisplaysMod.MODID, "unload_screen");
        CHANNEL_UPDATE_PREVIEW_SCREEN = new Identifier(CinemaDisplaysMod.MODID, "update_preview_screen");
        CHANNEL_OPEN_SETTINGS_SCREEN = new Identifier(CinemaDisplaysMod.MODID, "open_settings_screen");
    }

    private static String readString(PacketByteBuf buf) {
        int len = buf.readShort();
        byte[] data = new byte[len];
        buf.readBytes(data, 0, len);
        return new String(data, StandardCharsets.UTF_8);
    }

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_SCREENS, (client, handler, buf, responseSender) -> {
            String screensJsonRaw = readString(buf);

            client.submit(() -> {
                try {
                    List<Screen> screens = new ArrayList<>();

                    JsonObject root = JSON_PARSER.parse(screensJsonRaw).getAsJsonObject();
                    JsonArray screensArray = root.getAsJsonArray("screens");

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
                    screen.playVideo(video);
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
                        previewScreenManager.getPreviewScreen(previewScreen.getBlockPos()).setVideo(previewScreen.getVideo());
                    } else {
                        previewScreenManager.addPreviewScreen(previewScreen);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_OPEN_SETTINGS_SCREEN, (client, handler, buf, responseSender) -> {
            client.submit(() -> {
                client.openScreen(new CefSettingsScreen(Text.of("Video Settings")));
            });
        });
    }

    private static PreviewScreen deserializePreviewScreen(JsonObject previewScreenJson) {
        UUID parentScreenId = UUID.fromString(previewScreenJson.get("parent_screen_id").getAsString());
        String world = previewScreenJson.get("world").getAsString();
        int x = previewScreenJson.get("x").getAsInt();
        int y = previewScreenJson.get("y").getAsInt();
        int z = previewScreenJson.get("z").getAsInt();
        String facing = previewScreenJson.get("facing").getAsString();
        PreviewScreen previewScreen = new PreviewScreen(parentScreenId, world, x, y, z, facing);
        if (previewScreenJson.has("video")) {
            Video video = deserializeVideo(previewScreenJson.getAsJsonObject("video"));
            previewScreen.setVideo(video);
        } else {
            previewScreen.setVideo(null);
        }
        return previewScreen;
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
        String service = videoJson.get("service").getAsString();
        String title = videoJson.get("title").getAsString();
        String thumbnailUrl = videoJson.get("thumbnail_url").getAsString();
        String previewScreenTextureUrl = videoJson.get("preview_screen_texture_url").getAsString();
        long durationSeconds = videoJson.get("duration_seconds").getAsLong();
        long startedAt = videoJson.get("started_at").getAsLong();

        switch (service) {
            case "youtube":
                String videoId = videoJson.get("video_id").getAsString();
                String channelName = videoJson.get("channel_name").getAsString();
                return new YouTubeVideo(title, thumbnailUrl, previewScreenTextureUrl, durationSeconds, startedAt, videoId, channelName);
            case "file":
                String fileUrl = videoJson.get("url").getAsString();
                boolean loop = videoJson.get("loop").getAsBoolean();
                return new FileVideo(title, thumbnailUrl, previewScreenTextureUrl, durationSeconds, startedAt, fileUrl, loop);
            case "twitch":
                String twitchUser = videoJson.get("twitch_user").getAsString();
                return new TwitchVideo(title, thumbnailUrl, previewScreenTextureUrl, durationSeconds, startedAt, twitchUser);
            case "hls":
                String hlsUrl = videoJson.get("url").getAsString();
                return new HLSVideo(title, thumbnailUrl, previewScreenTextureUrl, durationSeconds, startedAt, hlsUrl);
            default:
                return null;
        }
    }

}
