package com.badlogic.gdx.graphics;

import com.badlogic.gdx.Gdx;

/* JADX INFO: loaded from: classes.dex */
public class FPSLogger {
    long startTime = System.currentTimeMillis();

    public void log() {
        if (System.currentTimeMillis() - this.startTime > 1000) {
            Gdx.app.log("FPSLogger", "fps: " + Gdx.graphics.getFramesPerSecond());
            this.startTime = System.currentTimeMillis();
        }
    }
}
