package com.gaiaonline.monstergalaxy.app;

import com.gaiaonline.monstergalaxy.model.moga.MogaType;
import com.gaiaonline.monstergalaxy.storage.StorageHelper;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class Constants {
    public static final int MAX_DISPLAY_VALUE_BLUE_COFFEE = 999;
    public static final int MAX_DISPLAY_VALUE_MOGA_CASH = 9999;
    public static final int MAX_DISPLAY_VALUE_STARSEED = 999;
    public static float accuracyAllGreen;
    public static float captureBonusBeginner;
    public static float captureBonusCommon;
    public static float captureBonusEpic;
    public static float captureBonusLegendary;
    public static float captureBonusRare;
    public static float captureBonusSuperRare;
    public static float captureBonusUncommon;
    public static Map<MogaType.Rarity, Integer> captureCosts;
    public static float captureHPThreshold;
    public static Map<MogaType.Rarity, Float> captureRates;
    public static float computerZodiacProbability;
    public static float[] cpuAttackNoiseMultipliers;
    public static float[] cpuAttackNoiseProbabilities;
    public static int[] dailyPrizeIDs;
    public static float[] dailyPrizeProbabilities;
    public static int dailyPrizeTimeToWait;
    public static String defaultIslandAmbienceAudio;
    public static String defaultQuestAmbienceAudio;
    public static int fixedXPBeginner;
    public static int fixedXPCommon;
    public static int fixedXPEpic;
    public static int fixedXPLegendary;
    public static int fixedXPRare;
    public static int fixedXPSuperRare;
    public static int fixedXPUncommon;
    public static float healthModifierP;
    public static float healthModifierQ;
    public static int itemCapStarseed;
    public static int levelCutoff;
    public static Map<Integer, Float> levelDiffMultipliers;
    public static int levelThreshold;
    public static int maxNapTime;
    public static int maxTrainerEnergy;
    public static float napModifierP;
    public static float napModifierQ;
    public static int newPlayerLevelMoga1;
    public static int newPlayerLevelMoga2;
    public static int newPlayerLevelMoga3;
    public static int newPlayerMoga1;
    public static int newPlayerMoga2;
    public static int newPlayerMoga3;
    public static float newPlayerPriceMoga2;
    public static float newPlayerPriceMoga3;
    public static float physicalAttackModifierP;
    public static float physicalPerfectMultiplier;
    public static float physicalPerfectRange;
    public static float[] playerAttackNoiseMultipliers;
    public static float[] playerAttackNoiseProbabilities;
    public static float trainerEnergyRecoverPerMin;
    public static float variableXPMultiplierDamage;
    public static float variableXPWeightDamage;
    public static String worldAmbienceAudio;
    public static String worldStingAudio;
    public static int xpLevel50;
    public static float xpModifierP;
    public static float xpModifierQ;
    public static float zodiacAttackSuccessPercentage;
    public static float zodiacAttackTapPercentage;
    public static Map<Integer, Integer> zodiacRecharge;

    public static void load() {
        StorageHelper storageHelper = Game.getStorage();
        cpuAttackNoiseProbabilities = arrayOfFloatsFromList(storageHelper.getConstantValue("attackerAttackNoise_Probabilities"));
        cpuAttackNoiseMultipliers = arrayOfFloatsFromList(storageHelper.getConstantValue("attackerAttackNoise_Multipliers"));
        playerAttackNoiseProbabilities = arrayOfFloatsFromList(storageHelper.getConstantValue("playerAttackNoise_Probabilities"));
        playerAttackNoiseMultipliers = arrayOfFloatsFromList(storageHelper.getConstantValue("playerAttackNoise_Multipliers"));
        levelDiffMultipliers = storageHelper.getLevelDiffMultipliers();
        captureHPThreshold = Float.valueOf(storageHelper.getConstantValue("captureHPThreshold")).floatValue();
        physicalAttackModifierP = Float.valueOf(storageHelper.getConstantValue("physicalAttackModifier_P")).floatValue();
        xpModifierP = Float.valueOf(storageHelper.getConstantValue("xpModifier_P")).floatValue();
        xpModifierQ = Float.valueOf(storageHelper.getConstantValue("xpModifier_Q")).floatValue();
        xpLevel50 = Integer.valueOf(storageHelper.getConstantValue("xpLevel50")).intValue();
        fixedXPBeginner = Integer.valueOf(storageHelper.getConstantValue("fixedXP_Beginner")).intValue();
        fixedXPCommon = Integer.valueOf(storageHelper.getConstantValue("fixedXP_Common")).intValue();
        fixedXPUncommon = Integer.valueOf(storageHelper.getConstantValue("fixedXP_Uncommon")).intValue();
        fixedXPRare = Integer.valueOf(storageHelper.getConstantValue("fixedXP_Rare")).intValue();
        fixedXPSuperRare = Integer.valueOf(storageHelper.getConstantValue("fixedXP_SuperRare")).intValue();
        fixedXPEpic = Integer.valueOf(storageHelper.getConstantValue("fixedXP_Epic")).intValue();
        fixedXPLegendary = Integer.valueOf(storageHelper.getConstantValue("fixedXP_Legendary")).intValue();
        newPlayerLevelMoga1 = Integer.valueOf(storageHelper.getConstantValue("newPlayer_LevelMoga1")).intValue();
        newPlayerLevelMoga2 = Integer.valueOf(storageHelper.getConstantValue("newPlayer_LevelMoga2")).intValue();
        newPlayerLevelMoga3 = Integer.valueOf(storageHelper.getConstantValue("newPlayer_LevelMoga3")).intValue();
        newPlayerMoga1 = Integer.valueOf(storageHelper.getConstantValue("newPlayer_Moga1")).intValue();
        newPlayerMoga2 = Integer.valueOf(storageHelper.getConstantValue("newPlayer_Moga2")).intValue();
        newPlayerMoga3 = Integer.valueOf(storageHelper.getConstantValue("newPlayer_Moga3")).intValue();
        newPlayerPriceMoga2 = Float.valueOf(storageHelper.getConstantValue("newPlayer_PriceMoga2")).floatValue();
        newPlayerPriceMoga3 = Float.valueOf(storageHelper.getConstantValue("newPlayer_PriceMoga3")).floatValue();
        healthModifierP = Float.valueOf(storageHelper.getConstantValue("healthModifier_P")).floatValue();
        healthModifierQ = Float.valueOf(storageHelper.getConstantValue("healthModifier_Q")).floatValue();
        computerZodiacProbability = Float.valueOf(storageHelper.getConstantValue("computer_zodiac_probability")).floatValue();
        maxTrainerEnergy = Integer.valueOf(storageHelper.getConstantValue("trainerEnergy_Cap")).intValue();
        trainerEnergyRecoverPerMin = Float.valueOf(storageHelper.getConstantValue("trainerEnergy_Recharge")).floatValue();
        worldAmbienceAudio = storageHelper.getConstantValue("worldAmbienceAudio");
        worldStingAudio = storageHelper.getConstantValue("worldStingAudio");
        defaultIslandAmbienceAudio = storageHelper.getConstantValue("defaultIslandAmbienceAudio");
        defaultQuestAmbienceAudio = storageHelper.getConstantValue("defaultQuestAmbienceAudio");
        captureRates = new HashMap();
        captureCosts = new HashMap();
        for (MogaType.Rarity rarity : MogaType.Rarity.valuesCustom()) {
            captureRates.put(rarity, Float.valueOf(storageHelper.getConstantValue("captureRate_" + rarity.getCamelCaseName())));
            captureCosts.put(rarity, Integer.valueOf(storageHelper.getConstantValue("masterSeed_" + rarity.getCamelCaseName())));
        }
        physicalPerfectRange = Float.parseFloat(storageHelper.getConstantValue("physicalPerfect_Range"));
        physicalPerfectMultiplier = Float.parseFloat(storageHelper.getConstantValue("physicalPerfect_Multiplier"));
        accuracyAllGreen = Float.parseFloat(storageHelper.getConstantValue("accuracyAllGreen"));
        zodiacAttackTapPercentage = Float.parseFloat(storageHelper.getConstantValue("zodiacAttackTapPercentage"));
        zodiacAttackSuccessPercentage = Float.parseFloat(storageHelper.getConstantValue("zodiacAttackSuccessPercentage"));
        zodiacRecharge = new HashMap();
        for (int i = 1; i <= 5; i++) {
            zodiacRecharge.put(Integer.valueOf(i), Integer.valueOf(storageHelper.getConstantValue("zodiacRecharge_" + i)));
        }
        itemCapStarseed = Integer.parseInt(storageHelper.getConstantValue("itemCap_Starseed"));
        dailyPrizeTimeToWait = Integer.parseInt(storageHelper.getConstantValue("daily_prize_time_to_wait"));
        dailyPrizeIDs = arrayOfIntsFromList(storageHelper.getConstantValue("dailyPrize_IDs"));
        dailyPrizeProbabilities = arrayOfFloatsFromList(storageHelper.getConstantValue("dailyPrize_Probabilities"));
        levelCutoff = Integer.parseInt(storageHelper.getConstantValue("levelCutoff"));
        levelThreshold = Integer.parseInt(storageHelper.getConstantValue("levelThreshold"));
        napModifierQ = Float.parseFloat(storageHelper.getConstantValue("napModifier_Q"));
        napModifierP = Float.parseFloat(storageHelper.getConstantValue("napModifier_P"));
        maxNapTime = Integer.parseInt(storageHelper.getConstantValue("maxNapTime"));
        variableXPMultiplierDamage = Float.parseFloat(storageHelper.getConstantValue("variableXPMultiplier_Damage"));
        variableXPWeightDamage = Float.parseFloat(storageHelper.getConstantValue("variableXPWeight_Damage"));
        captureBonusBeginner = Float.parseFloat(storageHelper.getConstantValue("captureBonus_Beginner"));
        captureBonusCommon = Float.parseFloat(storageHelper.getConstantValue("captureBonus_Common"));
        captureBonusUncommon = Float.parseFloat(storageHelper.getConstantValue("captureBonus_Uncommon"));
        captureBonusRare = Float.parseFloat(storageHelper.getConstantValue("captureBonus_Rare"));
        captureBonusSuperRare = Float.parseFloat(storageHelper.getConstantValue("captureBonus_SuperRare"));
        captureBonusEpic = Float.parseFloat(storageHelper.getConstantValue("captureBonus_Epic"));
        captureBonusLegendary = Float.parseFloat(storageHelper.getConstantValue("captureBonus_Legendary"));
    }

    private static float[] arrayOfFloatsFromList(String list) {
        String[] stringValues = list.substring(1, list.length() - 1).split(",");
        float[] values = new float[stringValues.length];
        for (int i = 0; i < stringValues.length; i++) {
            values[i] = Float.parseFloat(stringValues[i].trim());
        }
        return values;
    }

    private static int[] arrayOfIntsFromList(String list) {
        String[] stringValues = list.substring(1, list.length() - 1).split(",");
        int[] values = new int[stringValues.length];
        for (int i = 0; i < stringValues.length; i++) {
            values[i] = Integer.parseInt(stringValues[i].trim());
        }
        return values;
    }

    public static int getXOffsetForNPCWithName(String name) {
        if (name.equals("npc.ceci")) {
            return -35;
        }
        if (name.equals("npc.otho")) {
            return -10;
        }
        if (name.equals("npc.poli")) {
            return -35;
        }
        if (name.equals("npc.probus")) {
            return 25;
        }
        if (name.equals("npc.nessus")) {
            return -20;
        }
        if (name.equals("npc.fishman")) {
            return 20;
        }
        if (name.equals("npc.ganymede")) {
            return -50;
        }
        if (name.equals("npc.parents")) {
            return -10;
        }
        if (name.equals("npc.seagoat")) {
            return -5;
        }
        if (name.equals("npc.lucille")) {
            return -7;
        }
        if (name.equals("npc.minotaur")) {
            return -20;
        }
        if (name.equals("npc.thejudge")) {
            return -10;
        }
        if (name.equals("npc.buffprobus")) {
            return -6;
        }
        if (name.equals("npc.ceciandpoli")) {
            return -60;
        }
        if (name.equals("npc.kingotho")) {
            return -65;
        }
        if (name.equals("npc.deb")) {
            return -85;
        }
        return 0;
    }

    public static int getYOffsetForNPCWithName(String name) {
        if (name.equals("npc.ganymede")) {
            return -25;
        }
        if (name.equals("npc.parents")) {
            return -2;
        }
        if (name.equals("npc.seagoat")) {
            return 15;
        }
        if (name.equals("npc.lucille")) {
            return 2;
        }
        if (name.equals("npc.minotaur")) {
            return 16;
        }
        if (name.equals("npc.thejudge")) {
            return 8;
        }
        if (name.equals("npc.buffprobus")) {
            return 7;
        }
        if (name.equals("npc.ceciandpoli")) {
            return -6;
        }
        if (name.equals("npc.broomstar") || name.equals("npc.hoodedbroomstar")) {
            return -10;
        }
        if (name.equals("npc.deb")) {
            return -55;
        }
        return 0;
    }

    public static int getMaxStarSeeds(MogaType.Rarity rarity) {
        return Integer.parseInt(Game.getStorage().getConstantValue("maxStarseed_" + rarity.getCamelCaseName()));
    }
}
