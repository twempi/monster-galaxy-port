package com.gaiaonline.monstergalaxy.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.SoundManager;

/* JADX INFO: loaded from: classes.dex */
public class SettingsManager {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$settings$NotificationType = null;
    private static final String DAILY_PRIZES_ALERTS = "dailyPrizesAlerts";
    private static final String ISLAND_ALERTS = "islandAlerts";
    private static final String MOGA_RESTED_ALERTS = "mogaRestedAlerts";
    private static final String MUSIC_VOLUME = "musicVolume";
    public static final String SETTINGS_NAME = "moga-settings";
    private static final String SOUND_VOLUME = "soundVolume";
    private static final String TAMMER_ALERTS = "tammerAlerts";
    private static Preferences prefs;
    private static String TAG = "SettingsManager";
    private static float soundVolume = 0.5f;
    private static float musicVolume = 0.5f;
    private static boolean tammerAlerts = true;
    private static boolean islandAlerts = true;
    private static boolean dailyPrizesAlerts = true;
    private static boolean mogaRestedAlerts = true;

    static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$settings$NotificationType() {
        int[] iArr = $SWITCH_TABLE$com$gaiaonline$monstergalaxy$settings$NotificationType;
        if (iArr == null) {
            iArr = new int[NotificationType.valuesCustom().length];
            try {
                iArr[NotificationType.DAILY_PRIZE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[NotificationType.ISLAND.ordinal()] = 4;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[NotificationType.MOGAS_RESTED.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[NotificationType.TAMER_ENERGY_FULL.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            $SWITCH_TABLE$com$gaiaonline$monstergalaxy$settings$NotificationType = iArr;
        }
        return iArr;
    }

    public static void initialize() {
        if (prefs == null) {
            prefs = Gdx.app.getPreferences(SETTINGS_NAME);
        }
        if (prefs.contains(MUSIC_VOLUME)) {
            musicVolume = prefs.getFloat(MUSIC_VOLUME);
        }
        if (prefs.contains(SOUND_VOLUME)) {
            soundVolume = prefs.getFloat(SOUND_VOLUME);
        }
        if (prefs.contains(TAMMER_ALERTS)) {
            tammerAlerts = prefs.getBoolean(TAMMER_ALERTS);
        }
        if (prefs.contains(ISLAND_ALERTS)) {
            islandAlerts = prefs.getBoolean(ISLAND_ALERTS);
        }
        if (prefs.contains(DAILY_PRIZES_ALERTS)) {
            dailyPrizesAlerts = prefs.getBoolean(DAILY_PRIZES_ALERTS);
        }
        if (prefs.contains(MOGA_RESTED_ALERTS)) {
            mogaRestedAlerts = prefs.getBoolean(MOGA_RESTED_ALERTS);
        }
        Gdx.app.log(TAG, "Settings loaded");
        SoundManager.setMusicVolume(musicVolume);
        SoundManager.setSoundsVolume(soundVolume);
    }

    public static float getSoundVolume() {
        return soundVolume;
    }

    public static void setSoundVolume(float soundVolume2) {
        soundVolume = soundVolume2;
        if (prefs != null) {
            prefs.putFloat(SOUND_VOLUME, soundVolume2);
            prefs.flush();
            Gdx.app.log(TAG, "Settings saved, sound volume:" + soundVolume2);
        }
    }

    public static float getMusicVolume() {
        return musicVolume;
    }

    public static void setMusicVolume(float musicVolume2) {
        musicVolume = musicVolume2;
        if (prefs != null) {
            prefs.putFloat(MUSIC_VOLUME, musicVolume2);
            prefs.flush();
            Gdx.app.log(TAG, "Settings saved, music volume:" + musicVolume2);
        }
    }

    public static boolean isTammerAlerts() {
        return tammerAlerts;
    }

    public static boolean isIslandAlerts() {
        return islandAlerts;
    }

    public static void setIslandAlerts(boolean islandAlerts2) {
        islandAlerts = islandAlerts2;
        if (prefs != null) {
            prefs.putBoolean(ISLAND_ALERTS, islandAlerts2);
            prefs.flush();
            Gdx.app.log(TAG, "Settings saved, Island alerts:" + islandAlerts2);
            if (islandAlerts2) {
                Game.getAnalytics().trackEvent("retention", "alert_status", "island", "island_enable", "", new StringBuilder(String.valueOf(Game.getTrainer().getCurrentIsland().getId())).toString());
            } else {
                Game.getAnalytics().trackEvent("retention", "alert_status", "island", "island_disable", "", new StringBuilder(String.valueOf(Game.getTrainer().getCurrentIsland().getId())).toString());
            }
        }
    }

    public static void setTammerAlerts(boolean tammerAlerts2) {
        tammerAlerts = tammerAlerts2;
        if (prefs != null) {
            prefs.putBoolean(TAMMER_ALERTS, tammerAlerts2);
            prefs.flush();
            Gdx.app.log(TAG, "Settings saved, Tammer alerts:" + tammerAlerts2);
            if (tammerAlerts2) {
                Game.getAnalytics().trackEvent("retention", "alert_status", "tamer", "tamer_enable", "", new StringBuilder(String.valueOf(Game.getTrainer().getCurrentIsland().getId())).toString());
            } else {
                Game.getAnalytics().trackEvent("retention", "alert_status", "tamer", "tamer_disable", "", new StringBuilder(String.valueOf(Game.getTrainer().getCurrentIsland().getId())).toString());
            }
        }
    }

    public static boolean isDailyPrizesAlerts() {
        return dailyPrizesAlerts;
    }

    public static void setDailyPrizesAlerts(boolean dailyPrizesAlerts2) {
        dailyPrizesAlerts = dailyPrizesAlerts2;
        if (prefs != null) {
            prefs.putBoolean(DAILY_PRIZES_ALERTS, dailyPrizesAlerts2);
            prefs.flush();
            Gdx.app.log(TAG, "Settings saved, daily prizes alerts:" + dailyPrizesAlerts2);
            if (dailyPrizesAlerts2) {
                Game.getAnalytics().trackEvent("retention", "alert_status", "prize", "prize_enable", "", new StringBuilder(String.valueOf(Game.getTrainer().getCurrentIsland().getId())).toString());
            } else {
                Game.getAnalytics().trackEvent("retention", "alert_status", "prize", "prize_disable", "", new StringBuilder(String.valueOf(Game.getTrainer().getCurrentIsland().getId())).toString());
            }
        }
    }

    public static boolean isMogaRestedAlerts() {
        return mogaRestedAlerts;
    }

    public static void setMogaRestedAlerts(boolean mogaRestedAlerts2) {
        mogaRestedAlerts = mogaRestedAlerts2;
        if (prefs != null) {
            prefs.putBoolean(MOGA_RESTED_ALERTS, mogaRestedAlerts2);
            prefs.flush();
            Gdx.app.log(TAG, "Settings saved, moga rested alerts:" + dailyPrizesAlerts);
            if (mogaRestedAlerts2) {
                Game.getAnalytics().trackEvent("retention", "alert_status", "moga", "moga_enable", "", new StringBuilder(String.valueOf(Game.getTrainer().getCurrentIsland().getId())).toString());
            } else {
                Game.getAnalytics().trackEvent("retention", "alert_status", "moga", "moga_disable", "", new StringBuilder(String.valueOf(Game.getTrainer().getCurrentIsland().getId())).toString());
            }
        }
    }

    public static boolean isAlertEnabled(NotificationType type) {
        switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$settings$NotificationType()[type.ordinal()]) {
            case 1:
                return isDailyPrizesAlerts();
            case 2:
                return isMogaRestedAlerts();
            case 3:
                return isTammerAlerts();
            case 4:
                return isIslandAlerts();
            default:
                throw new RuntimeException("Unknown notification type " + type);
        }
    }

    public static void setValue(String key, boolean value) {
        prefs.putBoolean(key, value);
        prefs.flush();
    }

    public static void setValue(String key, String value) {
        prefs.putString(key, value);
        prefs.flush();
    }

    public static void setValue(String key, long value) {
        try {
            prefs.putLong(key, value);
            prefs.flush();
        } catch (Exception e) {
            Gdx.app.error("SettingsManager", "Storing Value", e);
        }
    }

    public static boolean getBoolean(String key) {
        return prefs.getBoolean(key);
    }

    public static long getLong(String key) {
        return prefs.getLong(key);
    }

    public static String getString(String key) {
        String value = prefs.getString(key);
        if (value == null) {
            return "";
        }
        return value;
    }
}
