package com.gaiaonline.monstergalaxy.model.minigame;

import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/* JADX INFO: loaded from: classes.dex */
public class Slotmachine {
    public static final int AVAILABLE_SINCE_NODE_ID = 14;
    private SlotMachinePrize chosenPrize;
    private SlotMachineReel chosenReel;
    private int coolDownSeconds;
    private int cost;
    private long lastSpinTimestamp;
    private Random random = new Random();
    private List<SlotMachineReel> reels;

    public enum PrizeType {
        STAR_SEED,
        BLUE_COFFEE,
        MOGA_CASH,
        MOGA_TYPE;

        /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
        public static PrizeType[] valuesCustom() {
            PrizeType[] prizeTypeArrValuesCustom = values();
            int length = prizeTypeArrValuesCustom.length;
            PrizeType[] prizeTypeArr = new PrizeType[length];
            System.arraycopy(prizeTypeArrValuesCustom, 0, prizeTypeArr, 0, length);
            return prizeTypeArr;
        }
    }

    public void init() {
        Trainer trainer = Game.getTrainer();
        List<SlotMachinePrize> prizes = getReels().get(0).getPrizes();
        List<SlotMachinePrize> wonMogaTypes = new ArrayList<>();
        for (SlotMachinePrize prize : prizes) {
            if (trainer.hasMogasOfType(prize.getMogaTypeId())) {
                wonMogaTypes.add(prize);
            }
        }
        prizes.removeAll(wonMogaTypes);
        SlotMachineReel bestPrizesReel = this.reels.get(0);
        if (bestPrizesReel.getPrizes().size() == 0) {
            bestPrizesReel.getPrizes().addAll(this.reels.get(1).getPrizes());
        }
        spinReels();
    }

    public int getCost() {
        return this.cost;
    }

    public void setCost(float cost) {
        this.cost = (int) cost;
    }

    public int getCoolDownSeconds() {
        return this.coolDownSeconds;
    }

    public void setCoolDownSeconds(int coolDownSeconds) {
        this.coolDownSeconds = coolDownSeconds;
    }

    public List<SlotMachineReel> getReels() {
        return this.reels;
    }

    public void setReels(List<SlotMachineReel> reels) {
        this.reels = reels;
    }

    public long getLastSpinTimestamp() {
        return this.lastSpinTimestamp;
    }

    public SlotMachinePrize[] getPrizeForFirstReel() {
        SlotMachinePrize[] firstReelPrizes = {this.reels.get(0).getPrize(), this.reels.get(1).getPrize(), this.reels.get(2).getPrize()};
        return firstReelPrizes;
    }

    public void spin() {
        this.chosenReel = getRandomReel();
        this.chosenPrize = this.chosenReel.getPrize();
        reconfigureReels();
        spinReels();
    }

    private void spinReels() {
        long timestamp = System.currentTimeMillis();
        long elapsedTime = (timestamp - this.lastSpinTimestamp) / 1000;
        if (elapsedTime >= this.coolDownSeconds || this.chosenReel == this.reels.get(0)) {
            this.lastSpinTimestamp = timestamp;
            for (SlotMachineReel reel : this.reels) {
                reel.spin();
            }
        }
    }

    public SlotMachinePrize getChosenPrize() {
        return this.chosenPrize;
    }

    public SlotMachineReel getChosenReel() {
        return this.chosenReel;
    }

    private SlotMachineReel getRandomReel() {
        double randomValue = this.random.nextDouble();
        float sum = 0.0f;
        for (SlotMachineReel reel : this.reels) {
            sum = (float) (((double) sum) + reel.getProbability());
            if (randomValue <= sum) {
                return reel;
            }
        }
        return null;
    }

    private void reconfigureReels() {
        SlotMachineReel bestPrizesReel = this.reels.get(0);
        List<SlotMachinePrize> prizes = bestPrizesReel.getPrizes();
        if (prizes.size() > 0) {
            prizes.remove(this.chosenReel.getPrize());
            if (prizes.size() == 0) {
                SlotMachineReel betterPrizesReel = this.reels.get(1);
                bestPrizesReel.setPrizes(betterPrizesReel.getPrizes());
            }
        }
    }
}
