package com.gaiaonline.monstergalaxy.model.map;

import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class EncounterAppearance implements Serializable {
    private static final long serialVersionUID = -7742396577858882847L;
    private int level;
    private int mogaTypeId;
    private int n;
    private float probability;

    public int getMogaTypeId() {
        return this.mogaTypeId;
    }

    public void setMogaTypeId(int mogaTypeId) {
        this.mogaTypeId = mogaTypeId;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public float getProbability() {
        return this.probability;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }

    public int getN() {
        return this.n;
    }

    public void setN(int n) {
        this.n = n;
    }
}
