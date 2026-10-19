package com.gaiaonline.monstergalaxy.model.battle;

import com.gaiaonline.monstergalaxy.app.Constants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.model.AttackType;
import com.gaiaonline.monstergalaxy.model.map.Node;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.model.moga.MogaType;
import com.gaiaonline.monstergalaxy.model.move.AttackMove;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Battle {
    public static final EnumSet<State> FINAL_STATES = EnumSet.of(State.CPU_KILLED, State.PLAYER_KILLED, State.PLAYER_RAN_AWAY, State.PLAYER_LOST);
    private static final State START_STATE = State.MOGA_CHOICE;
    private AttackType attackType;
    private boolean attackedWhileRunningAway;
    private int captureAttemps;
    private float captureRate;
    private boolean cpuAttackingBeforePlayerRunaway;
    private int mogaCaptureCount;
    private boolean runAwayRequested;
    private State state;
    private Trainer trainer = Game.getTrainer();
    private Node currentNode = this.trainer.getCurrentNode();
    private Map<EventType, Set<Listener>> listeners = new HashMap();

    public enum EventType {
        ATTACK_CHOICE,
        REGULAR_ATTACK_STRENGTH_METER,
        ZODIAC_ATTACK_STRENGTH_METER,
        PLAYER_KILLED,
        PLAYER_RAN_AWAY,
        PLAYER_ATTACK,
        CPU_ATTACK,
        CPU_KILLED,
        DEPLOY_MOGA,
        PLAYER_LOST,
        MOGA_CAPTURE_SUCCESS,
        MOGA_CAPTURE_FAILED,
        NOT_ENOUGH_STARSEEDS,
        NOT_ENOUGH_MATERSEEDS;

        /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
        public static EventType[] valuesCustom() {
            EventType[] eventTypeArrValuesCustom = values();
            int length = eventTypeArrValuesCustom.length;
            EventType[] eventTypeArr = new EventType[length];
            System.arraycopy(eventTypeArrValuesCustom, 0, eventTypeArr, 0, length);
            return eventTypeArr;
        }
    }

    public interface Listener {
        void onBattleEvent(EventType eventType);
    }

    public enum State {
        MOGA_CHOICE,
        ATTACK_CHOICE,
        REGULAR_ATTACK_STRENGTH_METER,
        ZODIAC_ATTACK_STRENGTH_METER,
        PLAYER_ATTACK,
        CPU_ATTACK,
        CPU_KILLED,
        PLAYER_KILLED,
        PLAYER_RAN_AWAY,
        MUST_RECHARGE_MOGA,
        PLAYER_LOST;

        /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
        public static State[] valuesCustom() {
            State[] stateArrValuesCustom = values();
            int length = stateArrValuesCustom.length;
            State[] stateArr = new State[length];
            System.arraycopy(stateArrValuesCustom, 0, stateArr, 0, length);
            return stateArr;
        }
    }

    public Battle() {
        this.trainer.setBattleReport(new BattleReport());
        for (EventType type : EventType.valuesCustom()) {
            this.listeners.put(type, new HashSet());
        }
    }

    public void init() {
        this.state = START_STATE;
        fullyRechargeMogasZodiac();
    }

    public Moga getPlayerMoga() {
        return this.trainer.getTeam().getDeployedMoga();
    }

    public Moga getComputerMoga() {
        return this.currentNode.getTeam().getDeployedMoga();
    }

    public AttackType getAttackType() {
        return this.attackType;
    }

    public void addEventListener(Listener listener) {
        for (EventType type : EventType.valuesCustom()) {
            this.listeners.get(type).add(listener);
        }
    }

    public void addEventListener(EventType type, Listener listener) {
        this.listeners.get(type).add(listener);
    }

    public void removeEventListener(EventType type, Listener listener) {
        this.listeners.get(type).remove(listener);
    }

    public void removeEventListeners(Listener listener) {
        for (Set<Listener> typeListeners : this.listeners.values()) {
            typeListeners.remove(listener);
        }
    }

    private void dispatchEvent(EventType type) {
        for (Listener listener : this.listeners.get(type)) {
            listener.onBattleEvent(type);
        }
    }

    private void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return this.state;
    }

    public void playerTimedOut() {
        setState(State.CPU_ATTACK);
        dispatchEvent(EventType.CPU_ATTACK);
    }

    public void playerQuits() {
        setState(State.PLAYER_LOST);
    }

    public void setAttackType(AttackType attackType) {
        if (attackType == null) {
            throw new NullPointerException();
        }
        this.attackType = attackType;
        if (attackType == AttackType.REGULAR) {
            setState(State.REGULAR_ATTACK_STRENGTH_METER);
            dispatchEvent(EventType.REGULAR_ATTACK_STRENGTH_METER);
        } else {
            setState(State.ZODIAC_ATTACK_STRENGTH_METER);
            dispatchEvent(EventType.ZODIAC_ATTACK_STRENGTH_METER);
        }
    }

    public void deployPlayerMoga(Moga playerMoga) {
        this.trainer.getTeam().setDeployedMoga(playerMoga);
        setPlayerTurn();
    }

    public void deployComputerMoga(Moga moga) {
        setComputerMoga(moga);
        setPlayerTurn();
    }

    public void playerMogaWasRecharged() {
        if (getPlayerMoga().getHitPoints() > 0) {
            setPlayerTurn();
            return;
        }
        throw new IllegalStateException("Chosen moga's health is zero");
    }

    public void setComputerMoga(Moga computerMoga) {
        this.currentNode.getTeam().setDeployedMoga(computerMoga);
    }

    public void requestRunaway() {
        this.runAwayRequested = true;
        if (getState() == State.ATTACK_CHOICE) {
            playerTurnCheckRunaway();
        } else {
            this.attackedWhileRunningAway = false;
        }
    }

    public float getCaptureRate() {
        Moga moga = getComputerMoga();
        int maxStarSeeds = Constants.getMaxStarSeeds(moga.getType().getRarity());
        if (this.captureAttemps == maxStarSeeds - 1) {
            this.captureRate = 1.0f;
        } else {
            this.captureRate = (((moga.getMaxHitPoints() * 3) - (moga.getHitPoints() * 2)) * getBaseCaptureRate(moga.getType())) / (moga.getMaxHitPoints() * 3);
        }
        return this.captureRate;
    }

    private float getBaseCaptureRate(MogaType mogaType) {
        return Constants.captureRates.get(mogaType.getRarity()).floatValue();
    }

    public void requestThrowSeed() {
        if (this.trainer.consumeStarseed()) {
            this.captureAttemps++;
            tryToCaptureMoga(this.captureRate, false, this.captureAttemps);
            String currentIslandId = String.valueOf(this.trainer.getCurrentIsland().getId());
            Game.getAnalytics().trackEvent("monetization", "use_item", "starseed", "starseed_battle", "", currentIslandId);
            return;
        }
        dispatchEvent(EventType.NOT_ENOUGH_STARSEEDS);
    }

    public void requestThrowMasterSeed() {
        int cost = getComputerMoga().getType().getCaptureCost();
        if (this.trainer.consumeCoins(cost)) {
            tryToCaptureMoga(1.0f, true, cost);
            String currentIslandId = String.valueOf(this.trainer.getCurrentIsland().getId());
            String coins = String.valueOf(getComputerMoga().getType().getCaptureCost());
            Game.getAnalytics().trackEvent("monetization", "use_item", "moga_cash", "moga_cash_master_seed", coins, currentIslandId);
            return;
        }
        dispatchEvent(EventType.NOT_ENOUGH_MATERSEEDS);
    }

    private void tryToCaptureMoga(float successRate, boolean wasMasterSeed, int cost) {
        int mogaTypeId = getComputerMoga().getType().getId();
        if (mogaTypeId == 266 || mogaTypeId == 60) {
            successRate = 1.0f;
        }
        if (Math.random() < successRate) {
            this.trainer.getBattleReport().setWinner(BattleReport.Winner.PLAYER);
            if (wasMasterSeed) {
                Game.getAnalytics().trackEvent("retention", "capture", new StringBuilder(String.valueOf(getComputerMoga().getType().getId())).toString(), "capture_masterseed", new StringBuilder(String.valueOf(cost)).toString(), new StringBuilder(String.valueOf(Game.getTrainer().getCurrentIsland().getId())).toString());
            } else {
                Game.getAnalytics().trackEvent("retention", "capture", new StringBuilder(String.valueOf(getComputerMoga().getType().getId())).toString(), "capture_starseed", new StringBuilder(String.valueOf(cost)).toString(), new StringBuilder(String.valueOf(Game.getTrainer().getCurrentIsland().getId())).toString());
            }
            this.captureAttemps = 0;
            dispatchEvent(EventType.MOGA_CAPTURE_SUCCESS);
            return;
        }
        this.trainer.getBattleReport().setWinner(BattleReport.Winner.CPU);
        dispatchEvent(EventType.MOGA_CAPTURE_FAILED);
    }

    public void mogaWasCaptured() {
        this.mogaCaptureCount++;
        cpuMogaCaptured();
    }

    private void cpuMogaCaptured() {
        this.trainer.getBattleReport().addCapturedMoga(getComputerMoga());
        this.currentNode.getTeam().getMogas().remove(getComputerMoga());
        this.trainer.capturedMoga(getComputerMoga());
        checkCpuLost();
    }

    private void cpuMogaKilled() {
        this.trainer.getBattleReport().addKilledMoga(getComputerMoga());
        this.currentNode.getTeam().getMogas().remove(getComputerMoga());
        checkCpuLost();
    }

    public int getMogaCaptureCount() {
        return this.mogaCaptureCount;
    }

    public boolean isRunningAway() {
        return this.runAwayRequested;
    }

    private void playerTurnCheckRunaway() {
        if (this.attackedWhileRunningAway) {
            this.attackedWhileRunningAway = false;
            setState(State.PLAYER_RAN_AWAY);
            dispatchEvent(EventType.PLAYER_RAN_AWAY);
        } else {
            this.cpuAttackingBeforePlayerRunaway = true;
            setComputerTurn();
        }
    }

    public void playerAttack(float strength) {
        if (this.attackType == AttackType.ZODIAC) {
            this.trainer.getTeam().getDeployedMoga().setZodiacCharge(0.0f);
        }
        attack(false, this.attackType, strength);
    }

    public void computerAttack(AttackType type, float strength) {
        if (type == AttackType.ZODIAC) {
            this.currentNode.getTeam().getDeployedMoga().setZodiacCharge(0.0f);
        }
        attack(true, type, strength);
    }

    public AttackType getRandomComputerAttackType() {
        return (getComputerMoga().getZodiacCharge() < 100.0f || Math.random() >= ((double) Constants.computerZodiacProbability)) ? AttackType.REGULAR : AttackType.ZODIAC;
    }

    private void setPlayerTurn() {
        if (this.runAwayRequested) {
            playerTurnCheckRunaway();
        } else {
            setState(State.ATTACK_CHOICE);
            dispatchEvent(EventType.ATTACK_CHOICE);
        }
    }

    public void setComputerTurn() {
        rechargeMogasZodiac();
        setState(State.CPU_ATTACK);
        dispatchEvent(EventType.CPU_ATTACK);
    }

    private void rechargeMogasZodiac() {
        for (Moga moga : this.trainer.getTeam().getMogas()) {
            moga.rechargeZodiac();
        }
        this.trainer.getCurrentNode().getTeam().getDeployedMoga().rechargeZodiac();
    }

    private void fullyRechargeMogasZodiac() {
        for (Moga moga : this.trainer.getTeam().getMogas()) {
            moga.fullyRechargeZodiac();
        }
        this.trainer.getCurrentNode().getTeam().getDeployedMoga().fullyRechargeZodiac();
    }

    private void nextTurn(boolean lastAttackerWasCPU) {
        if (this.cpuAttackingBeforePlayerRunaway) {
            this.cpuAttackingBeforePlayerRunaway = false;
            setState(State.PLAYER_RAN_AWAY);
            dispatchEvent(EventType.PLAYER_RAN_AWAY);
        } else if (lastAttackerWasCPU) {
            setPlayerTurn();
        } else {
            setComputerTurn();
        }
    }

    private void attack(boolean attackerIsCPU, AttackType type, float powerMeterResult) {
        AttackMove attack;
        Moga computerMoga = this.trainer.getCurrentNode().getTeam().getDeployedMoga();
        Moga defendingMoga = attackerIsCPU ? getPlayerMoga() : computerMoga;
        Moga attackingMoga = attackerIsCPU ? computerMoga : getPlayerMoga();
        float noiseMultiplier = attackerIsCPU ? getCPUAttackNoiseMultiplier() : getPlayerAttackNoiseMultiplier();
        if (type == AttackType.ZODIAC) {
            attack = attackingMoga.getType().getZodiacAttack();
        } else {
            attack = attackingMoga.getType().getBasicAttack();
        }
        int damage = attack.calculateDamage(powerMeterResult, noiseMultiplier, attackingMoga, defendingMoga);
        defendingMoga.applyDamage(damage);
        if (!attackerIsCPU) {
            this.trainer.getBattleReport().setDamage(attackingMoga, damage);
        }
        if (defendingMoga.getHitPoints() == 0) {
            if (attackerIsCPU) {
                if (this.trainer.getTeam().hasHealtyMogas()) {
                    setState(State.MOGA_CHOICE);
                } else {
                    setState(State.MUST_RECHARGE_MOGA);
                }
                this.trainer.getBattleReport().setWinner(BattleReport.Winner.CPU);
                dispatchEvent(EventType.PLAYER_KILLED);
                return;
            }
            cpuMogaKilled();
            return;
        }
        nextTurn(attackerIsCPU);
    }

    private void checkCpuLost() {
        if (this.currentNode.getTeam().hasMoreMogasToDeploy()) {
            setState(State.PLAYER_ATTACK);
            dispatchEvent(EventType.DEPLOY_MOGA);
        } else {
            this.trainer.getBattleReport().setWinner(BattleReport.Winner.PLAYER);
            setState(State.CPU_KILLED);
            dispatchEvent(EventType.CPU_KILLED);
        }
    }

    protected float getPlayerAttackNoiseMultiplier() {
        return getNoiseMultiplier(Constants.playerAttackNoiseProbabilities, Constants.playerAttackNoiseMultipliers);
    }

    protected float getCPUAttackNoiseMultiplier() {
        return getNoiseMultiplier(Constants.cpuAttackNoiseProbabilities, Constants.cpuAttackNoiseMultipliers);
    }

    protected float getNoiseMultiplier(float[] probabilities, float[] multipliers) {
        float randomValue = (float) Math.random();
        float accProbability = 0.0f;
        for (int i = 0; i < probabilities.length; i++) {
            accProbability += probabilities[i];
            if (accProbability >= randomValue) {
                return multipliers[i];
            }
        }
        return 1.0f;
    }
}
