package com.gaiaonline.monstergalaxy.battle.effects;

/* JADX INFO: loaded from: classes.dex */
public enum EffectType {
    BLUE_COFFEE("BlueCoffee"),
    BLUE_COFFEE_1("BlueCoffee1"),
    BLUE_COFFEE_2("BlueCoffee2"),
    BLUE_COFFEE_3("BlueCoffee3"),
    BLUE_COFFEE_4("BlueCoffee4"),
    BLUE_STARS("BlueStars"),
    INVENTORY("inventory"),
    ISLAND_UNLOCKED_PARTICLES("IslandUnlockedParticles"),
    AQUARIUS_1("aquarius1"),
    AQUARIUS_2("aquarius2"),
    ARIES_1("aries1"),
    ARIES_2("aries2"),
    CANCER_1("cancer1"),
    CANCER_2("cancer2"),
    CAPRICORN("capricorn"),
    CHARGE_A("chargeA"),
    CHARGE_B("chargeB"),
    EXPLOSION("explosion"),
    GEMINI("gemini"),
    HIT_1("hit1"),
    HIT_2("hit2"),
    HIT_3("hit3"),
    HIT_4("hit4"),
    HIT_5("hit5"),
    LEO("leo"),
    LIBRA("libra"),
    PISCES("pisces"),
    SAGITTARIUS("sagittarius"),
    SCORPIO("scorpio"),
    TAURUS("taurus"),
    VIRGO("virgo"),
    PUFF_SMOKE("PuffSmoke"),
    PUFF_SMOKE_GLOW("PuffSmokeGlow"),
    RING("Ring"),
    RUN_AWAY("RunAway"),
    RUN_AWAY_FAIL("RunAwayFail"),
    SEED_CAPTURE("SeedCapture"),
    SEED_CAPTURE_ENTRANCE("SeedCaptureEntrance"),
    STARS("Stars"),
    ZODIAC_NEUTRAL("zodiac_neutral"),
    ZODIAC_STRONG("zodiac_strong"),
    ZODIAC_WEAK("zodiac_weak"),
    ZODIAC_WHILE_TAPPING("zodiac_while_tapping"),
    ZODIAC_TAP("ZodiacTap");

    private String effectName;

    /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
    public static EffectType[] valuesCustom() {
        EffectType[] effectTypeArrValuesCustom = values();
        int length = effectTypeArrValuesCustom.length;
        EffectType[] effectTypeArr = new EffectType[length];
        System.arraycopy(effectTypeArrValuesCustom, 0, effectTypeArr, 0, length);
        return effectTypeArr;
    }

    EffectType(String effectName) {
        this.effectName = effectName;
    }

    public String getEffectName() {
        return this.effectName;
    }
}
