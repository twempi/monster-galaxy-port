package com.gaiaonline.monstergalaxy.model.trainer;

import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class Team implements Serializable {
    public static final byte MAX_TEAM_CAPACITY = 3;
    private static final long serialVersionUID = 6115325963464764687L;
    private Moga deployedMoga;
    private List<Moga> mogas = new ArrayList();

    public List<Moga> getMogas() {
        return this.mogas;
    }

    public Moga getDeployedMoga() {
        return this.deployedMoga;
    }

    public void setDeployedMoga(Moga newMoga) {
        this.deployedMoga = newMoga;
        SoundManager.loadMogaTypeSounds(this.deployedMoga.getType());
    }

    public boolean hasHealtyMogas() {
        int totalHealth = 0;
        for (Moga moga : this.mogas) {
            totalHealth += moga.getHitPoints();
        }
        return totalHealth > 0;
    }

    public void addMoga(Moga moga) {
        if (!this.mogas.contains(moga)) {
            this.mogas.add(moga);
        }
    }

    public Moga getFirstMoga() {
        return this.mogas.get(0);
    }

    public Moga getNextMoga() {
        int deployedMogaIndex = this.mogas.indexOf(this.deployedMoga);
        if (deployedMogaIndex >= this.mogas.size() - 1) {
            return null;
        }
        Moga nextMoga = this.mogas.get(deployedMogaIndex + 1);
        return nextMoga;
    }

    public boolean hasMoreMogasToDeploy() {
        return this.mogas.size() > 0;
    }

    public int getLevel() {
        return Moga.getMaxLevel(this.mogas);
    }
}
