package com.ruinscraft.cinemadisplays.cef;

import com.ruinscraft.cinemadisplays.CinemaDisplaysMod;
import com.ruinscraft.cinemadisplays.screen.Screen;
import com.ruinscraft.cinemadisplays.video.Video;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefLoadHandler;
import org.cef.network.CefRequest;

public class LoadHandler implements CefLoadHandler {

    @Override
    public void onLoadingStateChange(CefBrowser browser, boolean isLoading, boolean canGoBack, boolean canGoForward) {
    }

    @Override
    public void onLoadStart(CefBrowser browser, CefFrame frame, CefRequest.TransitionType transitionType) {
    }

    @Override
    public void onLoadEnd(CefBrowser browser, CefFrame frame, int httpStatusCode) {
        if (CinemaDisplaysMod.getInstance().getScreenManager().hasActive()) {
            Screen screen = CinemaDisplaysMod.getInstance().getScreenManager().getActive();
            Video video = screen.getVideo();
            video.start(frame);
        }
    }

    @Override
    public void onLoadError(CefBrowser browser, CefFrame frame, ErrorCode errorCode, String errorText, String failedUrl) {
        System.out.println("Load error: " + errorText);
    }

}
