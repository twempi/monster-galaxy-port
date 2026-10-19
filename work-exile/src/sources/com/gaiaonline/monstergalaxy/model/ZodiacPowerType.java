package com.gaiaonline.monstergalaxy.model;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public enum ZodiacPowerType {
    GOOD("data/green.circle.png"),
    NORMAL("data/white.circle.png"),
    BAD("data/red.circle.png");

    private String assetName;

    /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
    public static ZodiacPowerType[] valuesCustom() {
        ZodiacPowerType[] zodiacPowerTypeArrValuesCustom = values();
        int length = zodiacPowerTypeArrValuesCustom.length;
        ZodiacPowerType[] zodiacPowerTypeArr = new ZodiacPowerType[length];
        System.arraycopy(zodiacPowerTypeArrValuesCustom, 0, zodiacPowerTypeArr, 0, length);
        return zodiacPowerTypeArr;
    }

    ZodiacPowerType(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetName() {
        return this.assetName;
    }
}
