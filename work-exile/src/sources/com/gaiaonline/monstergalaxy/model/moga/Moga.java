package com.gaiaonline.monstergalaxy.model.moga;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Logger;
import com.gaiaonline.monstergalaxy.app.Clock;
import com.gaiaonline.monstergalaxy.app.Constants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.model.map.EncounterAppearance;
import com.gaiaonline.monstergalaxy.tweak.Tweak;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Moga implements Serializable {
    public static final float MAX_ZODIAC_CHARGE = 100.0f;
    public static final String NAME_DISALLOWED_CHARS = " ";
    private static final long serialVersionUID = 7792587842698511778L;
    private Vector2 combatPosition;
    private int experience;
    private int hitPoints;
    private String islandOfOriginId;
    private int lastDamage;
    private String name;
    private long napEnd;
    private boolean playingAnimation;
    private int sleepingTimeout;
    private MogaType type;
    private float zodiacCharge;
    public static final Integer NAME_MAX_CHARS = 15;
    private static final Logger logger = new Logger("Moga");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

    public static Moga newMogaFromType(MogaType type) {
        Moga moga = new Moga();
        moga.type = type;
        int level = Constants.newPlayerLevelMoga1;
        if (Tweak.getForcedPlayerMogaLevel() != null) {
            level = Tweak.getForcedPlayerMogaLevel().intValue();
        }
        moga.experience = getCumulativeExperienceForLevel(level);
        moga.name = type.getName();
        moga.fullyRechargeHealth();
        moga.fullyRechargeZodiac();
        return moga;
    }

    public static Moga newMogaFromAppearance(EncounterAppearance appearance) {
        Moga moga = new Moga();
        moga.type = Game.getStorage().getMogaType(appearance.getMogaTypeId());
        int teamLevel = Game.getTrainer().getTeam().getLevel();
        int level = appearance.getLevel();
        if (teamLevel > Constants.levelCutoff) {
            level = Constants.levelCutoff;
        } else if (teamLevel - appearance.getLevel() >= Constants.levelThreshold) {
            level = teamLevel - Constants.levelThreshold;
        }
        moga.name = moga.type.getName();
        moga.experience = getCumulativeExperienceForLevel(level);
        moga.fullyRechargeHealth();
        moga.fullyRechargeZodiac();
        if (Tweak.isEnabled() && Tweak.isCustomCpuStartingEnergyEnabled() && Tweak.getCustomCpuStartingEnergy() < moga.getMaxHitPoints()) {
            moga.hitPoints = Tweak.getCustomCpuStartingEnergy();
        }
        return moga;
    }

    public MogaType getType() {
        return this.type;
    }

    public void setType(MogaType type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExperience() {
        return this.experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setTeamLevel() {
        setExperience(getCumulativeExperienceForLevel(Game.getTrainer().getTeam().getLevel()));
    }

    public void setTrainerMogasMaxLevel() {
        setExperience(getCumulativeExperienceForLevel(getMaxLevel(Game.getTrainer().getMogas())));
    }

    public static int getMaxLevel(List<Moga> mogas) {
        int level = 1;
        for (Moga moga : mogas) {
            level = Math.max(level, moga.getLevel());
        }
        return level;
    }

    public int getNeededXPForNextLevel() {
        int level = getLevel();
        int cumXPforCurrentLvl = getCumulativeExperienceForLevel(level);
        int neededXP = getCumulativeExperienceForLevel(level + 1) - cumXPforCurrentLvl;
        return neededXP;
    }

    public int getCurrentXPForNextLevel() {
        int cumXPforCurrentLvl = getCumulativeExperienceForLevel(getLevel());
        return this.experience - cumXPforCurrentLvl;
    }

    public int getSleepingTimeout() {
        return this.sleepingTimeout;
    }

    public void setSleepingTimeout(int sleepingTimeout) {
        this.sleepingTimeout = sleepingTimeout;
    }

    public float getZodiacCharge() {
        return this.zodiacCharge;
    }

    public void setZodiacCharge(float zodiacCharge) {
        this.zodiacCharge = Math.min(100.0f, zodiacCharge);
    }

    public void rechargeZodiac() {
        setZodiacCharge(getZodiacCharge() + getType().getZodiacRecharge());
    }

    public void fullyRechargeZodiac() {
        setZodiacCharge(100.0f);
    }

    public void fullyRechargeHealth() {
        setHitPoints(getMaxHitPoints());
    }

    public boolean isPlayingAnimation() {
        return this.playingAnimation;
    }

    public void setPlayingAnimation(boolean playingAnimation) {
        this.playingAnimation = playingAnimation;
    }

    public Vector2 getCombatPosition() {
        return this.combatPosition;
    }

    public void setCombatPosition(Vector2 combatPosition) {
        this.combatPosition = combatPosition;
    }

    private void setNapEnd(long napEnd) {
        this.napEnd = napEnd;
    }

    public long getNapEnd() {
        updateNapProgress();
        return this.napEnd;
    }

    private void updateNapProgress() {
        if (this.napEnd > 0 && Clock.getCurrentTimeSec() >= this.napEnd) {
            napCompleted();
        }
    }

    private void napCompleted() {
        setNapEnd(0L);
        fullyRechargeHealth();
    }

    public void interruptNap() {
        setNapEnd(0L);
    }

    public void startNap() {
        Game.getTrainer().getTeam().getMogas().remove(this);
        setNapEnd(Clock.getCurrentTimeSec() + ((long) getRecoveryTime()));
        logger.log("Nap started. Nap ends at " + dateFormat.format(new Date(this.napEnd * 1000)));
    }

    public int getLevel() {
        float peak = Constants.xpLevel50;
        if (getExperience() >= peak) {
            return 50;
        }
        float dividend = (getExperience() / peak) - 1.0f;
        float divisor = (-1.0f) - (((Constants.xpModifierQ / Constants.xpModifierP) * getExperience()) / peak);
        return (int) Math.floor((Math.log(dividend / divisor) / ((double) ((-Constants.xpModifierP) - Constants.xpModifierQ))) + 0.009999999776482582d);
    }

    public String getIslandOfOriginId() {
        return this.islandOfOriginId;
    }

    public void setIslandOfOriginId(String islandOfOriginId) {
        this.islandOfOriginId = islandOfOriginId;
    }

    public int getHitPoints() {
        updateNapProgress();
        return this.hitPoints;
    }

    public int getMaxHitPoints() {
        float p = Constants.healthModifierP;
        float q = Constants.healthModifierQ;
        float dividend = 1.0f - ((float) Math.exp((-(p + q)) * getLevel()));
        float divisor = 1.0f + ((((float) Math.exp((-(p + q)) * getLevel())) * q) / p);
        return (int) Math.floor(this.type.getMaxHealth() * (dividend / divisor));
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
        if (this.hitPoints < 0) {
            this.hitPoints = 0;
        }
    }

    public void applyDamage(int damage) {
        this.hitPoints = Math.max(0, this.hitPoints - damage);
        this.lastDamage = damage;
    }

    public int getLastDamage() {
        return this.lastDamage;
    }

    public boolean canBeCaptured() {
        return ((float) this.hitPoints) <= ((float) getMaxHitPoints()) * Constants.captureHPThreshold;
    }

    public static int getCumulativeExperienceForLevel(int level) {
        float fLevel = level;
        float peak = Constants.xpLevel50;
        float dividend = (float) (1.0d - Math.exp(((-fLevel) * Constants.xpModifierP) - (Constants.xpModifierQ * fLevel)));
        float divisor = (float) ((((double) (Constants.xpModifierQ / Constants.xpModifierP)) * Math.exp(((-fLevel) * Constants.xpModifierP) - (Constants.xpModifierQ * fLevel))) + 1.0d);
        return (int) ((peak * dividend) / divisor);
    }

    public int getLevelCompletion() {
        int level = getLevel();
        int cumXPforCurrentLevel = getCumulativeExperienceForLevel(level);
        int neededXP = getCumulativeExperienceForLevel(level + 1) - cumXPforCurrentLevel;
        if (this.experience < cumXPforCurrentLevel) {
            this.experience = cumXPforCurrentLevel;
        }
        int retValue = ((this.experience - cumXPforCurrentLevel) * 100) / neededXP;
        return Math.min(100, retValue);
    }

    public int getRecoveryTime() {
        int level = getLevel();
        float q = Constants.napModifierQ;
        float p = Constants.napModifierP;
        float maxTime = Constants.maxNapTime;
        float dividend = maxTime * (1.0f - ((float) Math.exp(((-level) * p) - (level * q))));
        float divisor = 1.0f + ((q / p) * ((float) Math.exp(((-level) * p) - (level * q))));
        int time = (int) Math.floor(dividend / divisor);
        return time * 60;
    }

    public boolean isSleeping() {
        updateNapProgress();
        return this.napEnd > 0;
    }

    public boolean isAlive() {
        return getHitPoints() > 0;
    }

    public boolean isWounded() {
        return getHitPoints() < getMaxHitPoints();
    }

    public boolean isDead() {
        return getHitPoints() == 0;
    }

    public boolean canGoToSleep() {
        return isWounded() && !isSleeping();
    }

    public boolean equals(Object other) {
        if (!(other instanceof Moga)) {
            return false;
        }
        Moga otherMoga = (Moga) other;
        if (getType().getId() != otherMoga.getType().getId()) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return getType().getId();
    }
}
