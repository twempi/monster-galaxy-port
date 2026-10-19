package com.gaiaonline.monstergalaxy.assets;

import java.util.concurrent.Callable;

/*
 * Overrides the game class. The originals fetched extra music from Gaia's CDN
 * (d.cdn.gaiaonline.com), which is gone; every other asset ships in the APK.
 * Report each download as failed straight away so the game uses its fallbacks
 * (MusicManager plays a bundled island/battle track instead).
 */
public class DownloadTask implements Callable<Boolean> {
    public static final int PRIORITY_NORMAL = 2;
    public static final int PRIORITY_NOW = 1;
    private final int priority;

    public DownloadTask(Asset asset, int priority) {
        this.priority = priority;
    }

    public Boolean call() {
        return Boolean.FALSE;
    }

    public int getPriority() {
        return this.priority;
    }
}
