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

package com.ruinscraft.cinemadisplays.video.queue;

import com.ruinscraft.cinemadisplays.video.VideoInfo;
import org.jetbrains.annotations.NotNull;

public class QueuedVideo implements Comparable<QueuedVideo> {

    private final VideoInfo videoInfo;
    private final int score;
    private final int clientState; // -1 = downvote, 0 = no vote, 1 = upvote

    public QueuedVideo(VideoInfo videoInfo, int score, int clientState) {
        this.videoInfo = videoInfo;
        this.score = score;
        this.clientState = clientState;
    }

    public VideoInfo getVideoInfo() {
        return videoInfo;
    }

    public int getScore() {
        return score;
    }

    public int getClientState() {
        return clientState;
    }

    public String getScoreString() {
        if (score > 0) {
            return "+" + score;
        } else {
            return String.valueOf(score);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof QueuedVideo)) {
            return false;
        }
        return videoInfo.equals(((QueuedVideo) o).videoInfo);
    }

    @Override
    public int compareTo(@NotNull QueuedVideo queuedVideo) {
        if (score == queuedVideo.score) {
            return 0;
        } else if (score < queuedVideo.score) {
            return 1;
        } else {
            return -1;
        }
    }

}
