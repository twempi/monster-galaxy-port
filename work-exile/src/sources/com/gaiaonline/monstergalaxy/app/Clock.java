package com.gaiaonline.monstergalaxy.app;

import com.gaiaonline.monstergalaxy.settings.SettingsManager;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Clock {
    private static long bootTimeAtStartMillis;
    private static long deviceDeltaMillis;
    private static boolean initialized;
    private static long serverTimeAtStartMillis;
    private static boolean timeReliable;

    public static synchronized void initialize() {
        deviceDeltaMillis = SettingsManager.getLong("deviceDeltaMillis") * 1000;
        bootTimeAtStartMillis = Game.getPlatformHelper().getBootElapsedTimeMillis();
        serverTimeAtStartMillis = System.currentTimeMillis() - deviceDeltaMillis;
        initialized = true;
    }

    public static synchronized boolean isInitialized() {
        return initialized;
    }

    public static synchronized long toLocalTimeMillis(long serverTimeMillis) {
        long delta;
        delta = System.currentTimeMillis() - (getCurrentTimeSec() * 1000);
        return serverTimeMillis + delta;
    }

    public static synchronized long toServerTimeMillis(long localTimeMillis) {
        long delta;
        delta = System.currentTimeMillis() - (getCurrentTimeSec() * 1000);
        return localTimeMillis - delta;
    }

    private static long timeSinceStartMillis() {
        return Game.getPlatformHelper().getBootElapsedTimeMillis() - bootTimeAtStartMillis;
    }

    public static synchronized long getCurrentTimeSec() {
        return getCurrentTimeMillis() / 1000;
    }

    public static synchronized long getCurrentTimeMillis() {
        return serverTimeAtStartMillis + timeSinceStartMillis();
    }

    public static synchronized boolean isTimeTrusted() {
        return timeReliable;
    }

    public static synchronized void saveLocalClockDelta() {
        if (timeReliable) {
            deviceDeltaMillis = System.currentTimeMillis() - (getCurrentTimeSec() * 1000);
            SettingsManager.setValue("deviceDeltaMillis", deviceDeltaMillis / 1000);
        }
    }

    public static synchronized void setServerTime(long serverTime) {
        serverTimeAtStartMillis = (serverTime * 1000) - timeSinceStartMillis();
        timeReliable = true;
        deviceDeltaMillis = System.currentTimeMillis() - (serverTime * 1000);
        SettingsManager.setValue("deviceDeltaMillis", deviceDeltaMillis / 1000);
        saveLocalClockDelta();
    }
}
