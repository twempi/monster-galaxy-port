package com.gaiaonline.monstergalaxy.model;

import com.gaiaonline.monstergalaxy.i18n.I18nManager;

/* JADX INFO: loaded from: classes.dex */
public enum ZodiacSign {
    ARIES(I18nManager.I18nKey.ARIES, "ARIES", "Aries"),
    TAURUS(I18nManager.I18nKey.TAURUS, "TAURUS", "Taurus"),
    GEMINI(I18nManager.I18nKey.GEMINI, "GEMINI", "Gemini"),
    CANCER(I18nManager.I18nKey.CANCER, "CANCER", "Cancer"),
    LEO(I18nManager.I18nKey.LEO, "LEO", "Leo"),
    VIRGO(I18nManager.I18nKey.VIRGO, "VIRGO", "Virgo"),
    LIBRA(I18nManager.I18nKey.LIBRA, "LIBRA", "Libra"),
    SCORPIO(I18nManager.I18nKey.SCORPIO, "SCORPIO", "Scorpio"),
    SAGITTARIUS(I18nManager.I18nKey.SAGITTARIUS, "SAGITTARIUS", "Sagitarius"),
    CAPRICORN(I18nManager.I18nKey.CAPRICORN, "CAPRICORN", "Capricorn"),
    AQUARIUS(I18nManager.I18nKey.AQUARIUS, "AQUARIUS", "Aquarius"),
    PISCES(I18nManager.I18nKey.PISCES, "PISCES", "Pisces");

    private String camelCaseName;
    private I18nManager.I18nKey i18nKey;
    private String name;

    /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
    public static ZodiacSign[] valuesCustom() {
        ZodiacSign[] zodiacSignArrValuesCustom = values();
        int length = zodiacSignArrValuesCustom.length;
        ZodiacSign[] zodiacSignArr = new ZodiacSign[length];
        System.arraycopy(zodiacSignArrValuesCustom, 0, zodiacSignArr, 0, length);
        return zodiacSignArr;
    }

    ZodiacSign(I18nManager.I18nKey i18nKey, String name, String camelCaseName) {
        this.name = name;
        this.camelCaseName = camelCaseName;
        this.i18nKey = i18nKey;
    }

    public String getName() {
        return this.name;
    }

    public String getCamelCaseName() {
        return this.camelCaseName;
    }

    public String getLocalizedName() {
        return I18nManager.getText(this.i18nKey).toUpperCase();
    }

    public String getLocalizedCamelCaseName() {
        return I18nManager.getText(this.i18nKey);
    }

    public static ZodiacSign getValue(String name) {
        for (ZodiacSign zodiacSign : valuesCustom()) {
            if (zodiacSign.getName().equalsIgnoreCase(name)) {
                return zodiacSign;
            }
        }
        return null;
    }
}
