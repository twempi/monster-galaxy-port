package com.gaiaonline.monstergalaxy.tweak;

import com.gaiaonline.monstergalaxy.app.Game;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Tweak {
    private static boolean customCpuStartingEnergyEnabled;
    private static boolean enabled;
    private static Integer forcedPlayerMogaLevel;
    private static int customCpuStartingEnergy = 1;
    private static boolean showGameClock = false;
    private static boolean overrideNodeAndIslandLocks = false;

    public static Integer getForcedPlayerMogaLevel() {
        return forcedPlayerMogaLevel;
    }

    public static void setForcedPlayerMogaLevel(Integer forcedPlayerMogaLevel2) {
        forcedPlayerMogaLevel = forcedPlayerMogaLevel2;
    }

    public static boolean isOverrideNodeAndIslandLocks() {
        return overrideNodeAndIslandLocks;
    }

    public static void setOverrideNodeAndIslandLocks(boolean overrideNodeAndIslandLocks2) {
        overrideNodeAndIslandLocks = overrideNodeAndIslandLocks2;
    }

    public static int getMogaCash() {
        return Game.getTrainer().getCoins();
    }

    public static void setMogaCash(int coins) {
        Game.getTrainer().setCoins(coins);
    }

    public static int getBlueCoffees() {
        return Game.getTrainer().getBlueCoffeeCups();
    }

    public static void setBlueCoffees(int blueCoffees) {
        Game.getTrainer().setBlueCoffeeCups(blueCoffees);
    }

    public static int getStarSeeds() {
        return Game.getTrainer().getStarSeeds();
    }

    public static void setStarSeeds(int starSeeds) {
        Game.getTrainer().setStarSeeds(starSeeds);
    }

    public static void setCustomCpuStartingEnergy(int energy) {
        customCpuStartingEnergy = energy;
    }

    public static void setCustomCpuStartingEnergyEnabled(boolean enabled2) {
        customCpuStartingEnergyEnabled = enabled2;
    }

    public static int getCustomCpuStartingEnergy() {
        return customCpuStartingEnergy;
    }

    public static boolean isCustomCpuStartingEnergyEnabled() {
        return customCpuStartingEnergyEnabled;
    }

    public static boolean isShowGameClock() {
        return showGameClock;
    }

    public static void setShowGameClock(boolean showGameClock2) {
        showGameClock = showGameClock2;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean value) {
        enabled = value;
    }
}
