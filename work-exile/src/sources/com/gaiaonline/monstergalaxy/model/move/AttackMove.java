package com.gaiaonline.monstergalaxy.model.move;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.gaiaonline.monstergalaxy.app.Constants;
import com.gaiaonline.monstergalaxy.app.ZodiacHelper;
import com.gaiaonline.monstergalaxy.battle.anim.AnimationManager;
import com.gaiaonline.monstergalaxy.model.moga.Moga;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class AttackMove extends Move {
    private static final long serialVersionUID = 1108628652590869217L;
    private String animation;
    private String attackSound;
    private float bonusDamagePerHit;
    private String chargeAnimation;
    private String chargeEffect;
    private String hitSound;
    private String hitVisual;
    private int id;
    private int levelMultiplier;
    private float maxDamage;
    private float modifierP;
    private float modifierQ;
    private String name;
    private String power;
    private String type;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPower() {
        return this.power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getAttackSound() {
        return this.attackSound;
    }

    public void setAttackSound(String attackSound) {
        this.attackSound = attackSound;
    }

    public String getHitSound() {
        return this.hitSound;
    }

    public void setHitSound(String hitSound) {
        this.hitSound = hitSound;
    }

    public float getModifierQ() {
        return this.modifierQ;
    }

    public void setModifierQ(float modifierQ) {
        this.modifierQ = modifierQ;
    }

    public float getModifierP() {
        return this.modifierP;
    }

    public void setModifierP(float modifierP) {
        this.modifierP = modifierP;
    }

    public float getMaxDamage() {
        return this.maxDamage;
    }

    public void setMaxDamage(float maxDamage) {
        this.maxDamage = maxDamage;
    }

    public int getLevelMultiplier() {
        return this.levelMultiplier;
    }

    public void setLevelMultiplier(int levelMultiplier) {
        this.levelMultiplier = levelMultiplier;
    }

    public float getBonusDamagePerHit() {
        return this.bonusDamagePerHit;
    }

    public void setBonusDamagePerHit(float bonusDamagePerHit) {
        this.bonusDamagePerHit = bonusDamagePerHit;
    }

    public void setChargeAnimation(String chargeAnimation) {
        this.chargeAnimation = chargeAnimation;
    }

    public String getChargeEffect() {
        return this.chargeEffect;
    }

    public void setChargeEffect(String chargeEffect) {
        this.chargeEffect = chargeEffect;
    }

    public void setAnimation(String animation) {
        this.animation = animation;
    }

    public void setHitVisual(String hitVisual) {
        this.hitVisual = hitVisual;
    }

    public Action getChargeMove(boolean mirrored) {
        return AnimationManager.getInstance().getAnimation(this.chargeAnimation, mirrored);
    }

    public Action getStartMove(boolean mirrored) {
        return AnimationManager.getInstance().getAnimation(String.valueOf(this.animation) + "_start", mirrored);
    }

    public Action getEndMove(boolean mirrored) {
        return AnimationManager.getInstance().getAnimation(String.valueOf(this.animation) + "_end", mirrored);
    }

    public Action getHitMove(boolean mirrored) {
        return AnimationManager.getInstance().getAnimation(this.hitVisual, mirrored);
    }

    public int calculateDamage(float powerMeterResult, float noiseMultiplier, Moga attackerMoga, Moga targetMoga) {
        float physicalPerfectMultiplier = 1.0f;
        if (powerMeterResult > 1.0f) {
            physicalPerfectMultiplier = powerMeterResult;
            powerMeterResult = 1.0f;
        }
        float baseDamage = calculateBaseDamage(powerMeterResult, attackerMoga, targetMoga);
        float levelDiffEffect = Constants.levelDiffMultipliers.get(Integer.valueOf(attackerMoga.getLevel() - targetMoga.getLevel())).floatValue();
        float totalDamage = 0.5f + (noiseMultiplier * physicalPerfectMultiplier * levelDiffEffect * (baseDamage + 0.0f));
        return (int) Math.max(totalDamage, 0.0f);
    }

    private float calculateBaseDamage(float powerMeterResult, Moga attackerMoga, Moga targetMoga) {
        int attackerLevel = attackerMoga.getLevel();
        float dividend = (float) (1.0d - Math.exp((-(Constants.physicalAttackModifierP + getModifierQ())) * attackerLevel));
        float divider = (float) (((((double) getModifierQ()) * Math.exp((-(Constants.physicalAttackModifierP + getModifierQ())) * attackerLevel)) / ((double) Constants.physicalAttackModifierP)) + 1.0d);
        float baseDamage = ((getMaxDamage() * powerMeterResult) * dividend) / divider;
        if ("Zodiac".equals(this.name)) {
            float multiplier = ZodiacHelper.getInstance().getZodiacSignMultiplier(attackerMoga.getType().getZodiacSign(), targetMoga.getType().getZodiacSign());
            return baseDamage * multiplier;
        }
        return baseDamage;
    }
}
