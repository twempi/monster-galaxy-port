package com.gaiaonline.monstergalaxy.app.sound;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface MusicElement {
    void destroy();

    boolean isPlaying();

    void play();

    void setVolume(float f);

    void stop();

    void update();
}
