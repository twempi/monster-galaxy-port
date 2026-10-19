package com.gaiaonline.monstergalaxy.service;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class AdsService {
    protected static String APP_ID = "4f764fe0f77659b310000043";
    protected static String APP_SIGNATURE = "e0e4be837dad4bccef8cf4021d10938d2ccecb36";
    private boolean afterBattleAdPending;

    public abstract void dismissInterstitial();

    public abstract void doCacheInterstitials();

    public abstract boolean isShowingInterstitial();

    public abstract void showInterstitial(Location location, Runnable runnable);

    public enum Location {
        APP_START("APP START"),
        BEFORE_BATTLE("BEFORE BATTLE"),
        AFTER_BATTLE("AFTER BATTLE"),
        ISLAND_CLICK("ISLAND CLICK");

        private String id;

        /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
        public static Location[] valuesCustom() {
            Location[] locationArrValuesCustom = values();
            int length = locationArrValuesCustom.length;
            Location[] locationArr = new Location[length];
            System.arraycopy(locationArrValuesCustom, 0, locationArr, 0, length);
            return locationArr;
        }

        Location(String id) {
            this.id = id;
        }

        public String getId() {
            return this.id;
        }
    }

    public boolean isAfterBattleAdPending() {
        return this.afterBattleAdPending;
    }

    public void setAfterBattleAdPending(boolean afterBattleAdPending) {
        this.afterBattleAdPending = afterBattleAdPending;
    }
}
