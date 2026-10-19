package com.gaiaonline.monstergalaxy.app;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ScreenHistoryEntry {
    private Screen screen;
    private ScreenCode screenCode;

    public ScreenHistoryEntry(ScreenCode screenCode) {
        this.screenCode = screenCode;
    }

    public ScreenHistoryEntry(Screen screen) {
        this.screen = screen;
    }

    public Screen getScreen() {
        return this.screen == null ? ScreenFactory.createScreen(this.screenCode) : this.screen;
    }

    public ScreenCode getScreenCode() {
        return this.screenCode;
    }
}
