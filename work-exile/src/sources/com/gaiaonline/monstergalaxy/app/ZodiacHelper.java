package com.gaiaonline.monstergalaxy.app;

import com.gaiaonline.monstergalaxy.model.ZodiacPowerType;
import com.gaiaonline.monstergalaxy.model.ZodiacSign;
import com.gaiaonline.monstergalaxy.storage.StorageHelper;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class ZodiacHelper {
    private static ZodiacHelper instance;
    private StorageHelper storage;
    private float zodiacMultiplierBad;
    private float zodiacMultiplierGood;
    private Map<Integer, Float> zodiacMultipliers = new HashMap();

    private ZodiacHelper() {
    }

    public static ZodiacHelper getInstance() {
        if (instance == null) {
            instance = new ZodiacHelper();
        }
        return instance;
    }

    public void initialize() {
        this.storage = Game.getStorage();
        this.zodiacMultiplierGood = Float.valueOf(this.storage.getConstantValue("zodiacMultipler_Good")).floatValue();
        this.zodiacMultiplierBad = Float.valueOf(this.storage.getConstantValue("zodiacMultiplier_Bad")).floatValue();
    }

    public float getZodiacSignMultiplier(ZodiacSign playerZodiac, ZodiacSign computerZodiac) {
        Integer key = Integer.valueOf((playerZodiac.ordinal() * 12) + computerZodiac.ordinal());
        Float multiplier = this.zodiacMultipliers.get(key);
        if (multiplier == null) {
            multiplier = Float.valueOf(this.storage.getZodiacSignMultiplier(playerZodiac.getName(), computerZodiac.getName()));
            this.zodiacMultipliers.put(key, multiplier);
        }
        return multiplier.floatValue();
    }

    public ZodiacPowerType getPowerTypeAgainst(ZodiacSign playerZodiac, ZodiacSign computerZodiac) {
        return getPowerTypeFrom(getZodiacSignMultiplier(playerZodiac, computerZodiac));
    }

    public ZodiacPowerType getPowerTypeFrom(float zodiacMultiplier) {
        if (zodiacMultiplier >= this.zodiacMultiplierGood) {
            return ZodiacPowerType.GOOD;
        }
        if (zodiacMultiplier <= this.zodiacMultiplierBad) {
            return ZodiacPowerType.BAD;
        }
        return ZodiacPowerType.NORMAL;
    }
}
