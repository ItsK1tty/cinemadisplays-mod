package com.ruinscraft.cinemadisplays;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ruinscraft.cinemadisplays.cef.CefUtil;
import com.ruinscraft.cinemadisplays.screen.Screen;
import com.ruinscraft.cinemadisplays.video.FileVideo;
import com.ruinscraft.cinemadisplays.video.Video;
import com.ruinscraft.cinemadisplays.video.YouTubeVideo;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
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

    static {
        JSON_PARSER = new JsonParser();
        CHANNEL_SCREENS = new Identifier("cinemadisplays", "screens");
        CHANNEL_LOAD_SCREEN = new Identifier("cinemadisplays", "load_screen");
        CHANNEL_UNLOAD_SCREEN = new Identifier("cinemadisplays", "unload_screen");
    }

    private static String readString(PacketByteBuf buf) {
        int len = buf.readShort();
        byte data[] = new byte[len];
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
            String screenJsonRaw = readString(buf);
            String videoJsonRaw = readString(buf);

            client.submit(() -> {
                try {
                    JsonObject screenJson = JSON_PARSER.parse(screenJsonRaw).getAsJsonObject();
                    JsonObject videoJson = JSON_PARSER.parse(videoJsonRaw).getAsJsonObject();
                    Screen screen = deserializeScreen(screenJson);
                    Video video = deserializeVideo(videoJson);

                    screen.setVideo(video);

                    CefUtil.closeBrowser();
                    CinemaDisplaysMod.getInstance().getScreenManager().setActive(screen);
                    CefUtil.playVideo(video);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_UNLOAD_SCREEN, (client, handler, buf, responseSender) -> {
            String screenJsonRaw = readString(buf);

            client.submit(() -> {
                try {
                    JsonObject screenJson = JSON_PARSER.parse(screenJsonRaw).getAsJsonObject();
                    Screen screen = deserializeScreen(screenJson);

                    if (CinemaDisplaysMod.getInstance().getScreenManager().hasActive()) {
                        UUID activeScreenId = CinemaDisplaysMod.getInstance().getScreenManager().getActive().getId();

                        if (screen.getId().equals(activeScreenId)) {
                            CefUtil.closeBrowser();
                            CinemaDisplaysMod.getInstance().getScreenManager().setActive(null);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }

    private static Screen deserializeScreen(JsonObject screenJson) {
        String world = screenJson.get("world").getAsString();
        int x = screenJson.get("x").getAsInt();
        int y = screenJson.get("y").getAsInt();
        int z = screenJson.get("z").getAsInt();
        String facing = screenJson.get("facing").getAsString();
        int width = screenJson.get("width").getAsInt();
        int height = screenJson.get("height").getAsInt();
        UUID id = UUID.fromString(screenJson.get("id").getAsString());
        return new Screen(world, x, y, z, facing, width, height, id);
    }

    private static Video deserializeVideo(JsonObject videoJson) {
        String service = videoJson.get("service").getAsString();
        String title = videoJson.get("title").getAsString();
        long durationSeconds = videoJson.get("duration_seconds").getAsLong();
        long startedAt = videoJson.get("started_at").getAsLong();

        switch (service) {
            case "youtube":
                String videoId = videoJson.get("video_id").getAsString();
                return new YouTubeVideo(title, durationSeconds, startedAt, videoId);
            case "file":
                String url = videoJson.get("url").getAsString();
                boolean loop = videoJson.get("loop").getAsBoolean();
                return new FileVideo(title, durationSeconds, startedAt, url, loop);
            default:
                return null;
        }
    }

}
