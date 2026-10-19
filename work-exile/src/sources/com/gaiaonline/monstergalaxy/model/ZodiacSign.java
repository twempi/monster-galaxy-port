package com.gaiaonline.monstergalaxy.model;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public enum ZodiacSign {
    ARIES("ARIES", "Aries"),
    TAURUS("TAURUS", "Taurus"),
    GEMINI("GEMINI", "Gemini"),
    CANCER("CANCER", "Cancer"),
    LEO("LEO", "Leo"),
    VIRGO("VIRGO", "Virgo"),
    LIBRA("LIBRA", "Libra"),
    SCORPIO("SCORPIO", "Scorpio"),
    SAGITTARIUS("SAGITTARIUS", "Sagitarius"),
    CAPRICORN("CAPRICORN", "Capricorn"),
    AQUARIUS("AQUARIUS", "Aquarius"),
    PISCES("PISCES", "Pisces");

    private String camelCaseName;
    private String name;

    /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
    public static ZodiacSign[] valuesCustom() {
        ZodiacSign[] zodiacSignArrValuesCustom = values();
        int length = zodiacSignArrValuesCustom.length;
        ZodiacSign[] zodiacSignArr = new ZodiacSign[length];
        System.arraycopy(zodiacSignArrValuesCustom, 0, zodiacSignArr, 0, length);
        return zodiacSignArr;
    }

    ZodiacSign(String name, String camelCaseName) {
        this.name = name;
        this.camelCaseName = camelCaseName;
    }

    public String getName() {
        return this.name;
    }

    public String getCamelCaseName() {
        return this.camelCaseName;
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
