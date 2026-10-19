package com.badlogic.gdx.audio;

import com.badlogic.gdx.utils.Disposable;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface Music extends Disposable {
    @Override // com.badlogic.gdx.utils.Disposable
    void dispose();

    float getPosition();

    boolean isLooping();

    boolean isPlaying();

    void pause();

    void play();

    void setLooping(boolean z);

    void setVolume(float f);

    void stop();
}
