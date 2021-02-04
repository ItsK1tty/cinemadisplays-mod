package com.ruinscraft.cinemadisplays.cef;

import com.google.common.collect.Lists;
import com.ruinscraft.cinemadisplays.CinemaDisplaysMod;
import com.ruinscraft.cinemadisplays.screen.Screen;
import com.ruinscraft.cinemadisplays.video.FileVideo;
import com.ruinscraft.cinemadisplays.video.Video;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowserOsr;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.util.ArrayList;

public final class CefUtil {

    private static boolean init;
    private static CefApp cefAppInstance;
    private static CefClient cefClientInstance;
    private static CefBrowserOsr cefBrowserOsr;

    public static void init() throws Exception {
        System.out.println("Initializing CEF...");

        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            System.loadLibrary("jawt");
            System.loadLibrary("chrome_elf");
            System.loadLibrary("libcef");
            System.loadLibrary("jcef");
        } else if (os.contains("mac")) {
            // TODO:
        } else if (os.contains("linux")) {
            // TODO:
        }

        CefApp.startup(new String[0]);

        CefSettings cefSettings = new CefSettings();
        cefSettings.windowless_rendering_enabled = true;
        cefSettings.background_color = cefSettings.new ColorType(0, 255, 255, 255);
        cefSettings.cache_path = new File("chromium", "cache").getAbsolutePath();
        cefSettings.ignore_certificate_errors = true;
        cefSettings.user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.146 Safari/537.36";

        ArrayList<String> cefSwitches = Lists.newArrayList("--autoplay-policy=no-user-gesture-required", "--disable-web-security");

        cefAppInstance = CefApp.getInstance(cefSwitches.toArray(new String[cefSwitches.size()]), cefSettings);
        cefClientInstance = cefAppInstance.createClient();
        cefClientInstance.addLoadHandler(new LoadHandler());

        System.out.println("CEF initialized!");
        init = true;
    }

    public static boolean isInit() {
        return init;
    }

    public static CefApp getCefApp() {
        return cefAppInstance;
    }

    public static CefClient getCefClient() {
        return cefClientInstance;
    }

    public static boolean hasActiveBrowser() {
        return cefBrowserOsr != null;
    }

    public static CefBrowserOsr createBrowser(String startUrl) {
        if (!init) {
            return null;
        }

        cefBrowserOsr = (CefBrowserOsr) cefClientInstance.createBrowser(startUrl, true, false);
        cefBrowserOsr.setCloseAllowed();
        cefBrowserOsr.createImmediately();

        if (CinemaDisplaysMod.getInstance().getScreenManager().hasActive()) {
            Screen active = CinemaDisplaysMod.getInstance().getScreenManager().getActive();
            int widthBlocks = active.getWidth();
            int heightBlocks = active.getHeight();
            double scale = (double) widthBlocks / (double) heightBlocks;
            int height = 720;
            int width = (int) Math.floor(height * scale);

            cefBrowserOsr.resize(width, height);
        }

        return cefBrowserOsr;
    }

    public static void closeBrowser() {
        if (hasActiveBrowser()) {
            cefBrowserOsr.close();
            cefBrowserOsr = null;
        }
    }

    public static void playVideo(Video video) {
        switch (video.getService()) {
            case "youtube":
                createBrowser("https://cdn.ruinscraft.com/cinema/service/v1/youtube.html");
                break;
            case "file":
                FileVideo fileVideo = (FileVideo) video;
                if (fileVideo.isLoop()) {
                    createBrowser("https://cdn.ruinscraft.com/cinema/service/v1/fileloop.html");
                } else {
                    createBrowser("https://cdn.ruinscraft.com/cinema/service/v1/file.html");
                }
                break;
            default:
                break;
        }
    }

    public static CefBrowserOsr getActiveBrowser() {
        return cefBrowserOsr;
    }

    public static void registerCefGuiScreen() {
        KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.cinemadisplays.cefscreen",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F10,
                "category.cinemadisplays.cefscreen"
        ));

//        ClientTickEvents.END_CLIENT_TICK.register(client -> {
//            while (keyBinding.wasPressed()) {
//                if (isInit() && !hasActiveBrowser()) {
//                    createBrowser("https://google.com");
//                }
//
//                client.openScreen(new CefScreen());
//            }
//        });
    }

    public static void registerCefTick() {
        ClientTickEvents.START_CLIENT_TICK.register(minecraftClient -> {
            if (minecraftClient.world == null) {
                if (CefUtil.isInit()) {
                    if (CefUtil.hasActiveBrowser()) {
                        CefUtil.closeBrowser();
                    }
                }
            }
        });

        ClientTickEvents.START_WORLD_TICK.register(client -> {
            if (CefUtil.isInit()) {
                CefUtil.getCefApp().N_DoMessageLoopWork();

                if (CefUtil.hasActiveBrowser()) {
                    CefUtil.getActiveBrowser().update();
                }
            }
        });
    }

}
