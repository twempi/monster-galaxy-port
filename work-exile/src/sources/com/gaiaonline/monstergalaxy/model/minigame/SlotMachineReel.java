package com.gaiaonline.monstergalaxy.model.minigame;

import java.util.List;
import java.util.Random;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class SlotMachineReel {
    private int currentPrizeIndex;
    private String name;
    private List<SlotMachinePrize> prizes;
    private double probability;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getProbability() {
        return this.probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public List<SlotMachinePrize> getPrizes() {
        return this.prizes;
    }

    public void setPrizes(List<SlotMachinePrize> prizes) {
        this.prizes = prizes;
    }

    public void spin() {
        Random random = new Random();
        this.currentPrizeIndex = random.nextInt(this.prizes.size());
    }

    public SlotMachinePrize getPrize() {
        if (this.prizes.size() <= 0) {
            return null;
        }
        if (this.currentPrizeIndex >= this.prizes.size()) {
            this.currentPrizeIndex = this.prizes.size() - 1;
        }
        SlotMachinePrize prize = this.prizes.get(this.currentPrizeIndex);
        return prize;
    }
}
