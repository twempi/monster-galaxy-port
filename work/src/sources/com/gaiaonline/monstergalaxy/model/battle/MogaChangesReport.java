package com.gaiaonline.monstergalaxy.model.battle;

/* JADX INFO: loaded from: classes.dex */
public class MogaChangesReport {
    private int level;
    private int levelCompletion;
    private int newLevel;
    private int newLevelCompletion;
    private int totalXP;
    private int variableXP;

    public int getVariableXP() {
        return this.variableXP;
    }

    public void setVariableXP(int variableXP) {
        this.variableXP = variableXP;
    }

    public int getTotalXP() {
        return this.totalXP;
    }

    public void setTotalXP(int totalXP) {
        this.totalXP = totalXP;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevelCompletion() {
        return this.levelCompletion;
    }

    public void setLevelCompletion(int levelCompletion) {
        this.levelCompletion = levelCompletion;
    }

    public int getNewLevel() {
        return this.newLevel;
    }

    public void setNewLevel(int newLevel) {
        this.newLevel = newLevel;
    }

    public int getNewLevelCompletion() {
        return this.newLevelCompletion;
    }

    public void setNewLevelCompletion(int newLevelCompletion) {
        this.newLevelCompletion = newLevelCompletion;
    }
}
