package com.badlogic.gdx.utils;

import com.badlogic.gdx.Gdx;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Logger {
    private boolean enabled = true;
    private final String tag;

    public Logger(String tag) {
        this.tag = tag;
    }

    public void log(String message) {
        if (this.enabled) {
            Gdx.app.log(this.tag, message);
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
