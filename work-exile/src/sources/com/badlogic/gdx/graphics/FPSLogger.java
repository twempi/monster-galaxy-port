package com.badlogic.gdx.graphics;

import com.badlogic.gdx.Gdx;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class FPSLogger {
    long startTime = System.nanoTime();

    public void log() {
        if (System.nanoTime() - this.startTime > 1000000000) {
            Gdx.app.log("FPSLogger", "fps: " + Gdx.graphics.getFramesPerSecond());
            this.startTime = System.nanoTime();
        }
    }
}
