package com.badlogic.gdx.audio;

import com.badlogic.gdx.utils.Disposable;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface Sound extends Disposable {
    @Override // com.badlogic.gdx.utils.Disposable
    void dispose();

    void play();

    void play(float f);

    void stop();
}
