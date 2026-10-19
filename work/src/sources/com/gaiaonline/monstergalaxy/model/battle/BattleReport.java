package com.gaiaonline.monstergalaxy.model.battle;

import com.gaiaonline.monstergalaxy.app.Constants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.model.map.Island;
import com.gaiaonline.monstergalaxy.model.map.Node;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class BattleReport {
    private Map<Integer, Integer> damageDoneByMoga;
    private boolean maxLevelAtStart;
    private Map<Integer, MogaChangesReport> mogaChanges;
    private boolean mogaMaxLevelReached;
    private int numberOfRewardedBlueCoffees;
    private int numberOfRewardedStarSeeds;
    private boolean scoredHighScore;
    private boolean scoredNormal;
    private boolean scoredPerfect;
    private boolean scoredVeryGood;
    private boolean wasCaptureEnabled;
    private boolean wasNextIslandLockedWhenBattleStarted;
    private Winner winner;
    private List<Moga> killedMogas = new ArrayList();
    private List<Moga> capturedMogas = new ArrayList();
    private Trainer trainer = Game.getTrainer();
    private boolean wasIslandCompletedWhenBattleStarted = this.trainer.isIslandCompleted(this.trainer.getCurrentIsland());
    private Set<Integer> completedNodeIdsBeforeBattle = new HashSet();

    public enum Winner {
        PLAYER,
        CPU;

        /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
        public static Winner[] valuesCustom() {
            Winner[] winnerArrValuesCustom = values();
            int length = winnerArrValuesCustom.length;
            Winner[] winnerArr = new Winner[length];
            System.arraycopy(winnerArrValuesCustom, 0, winnerArr, 0, length);
            return winnerArr;
        }
    }

    public BattleReport() {
        this.maxLevelAtStart = false;
        this.completedNodeIdsBeforeBattle.addAll(this.trainer.getCompletedNodesIds());
        Island nextIsland = this.trainer.getCurrentIsland().getNextIsland();
        if (nextIsland != null) {
            this.wasNextIslandLockedWhenBattleStarted = !this.trainer.canGoToIsland(nextIsland);
        } else {
            this.wasNextIslandLockedWhenBattleStarted = false;
        }
        this.damageDoneByMoga = new HashMap();
        this.mogaChanges = new HashMap();
        this.maxLevelAtStart = false;
        int maxLevel = this.trainer.getCurrentIsland().getMaxLevel();
        for (Moga moga : this.trainer.getTeam().getMogas()) {
            int mogaLevel = moga.getLevel();
            if (mogaLevel >= maxLevel) {
                this.maxLevelAtStart = true;
            }
        }
        this.wasCaptureEnabled = this.trainer.getCurrentNode().getEncounter().isCaptureEnabled();
    }

    public boolean isMaxLevelAtStart() {
        return this.maxLevelAtStart;
    }

    public void setMaxLevelAtStart(boolean maxLevelAtStart) {
        this.maxLevelAtStart = maxLevelAtStart;
    }

    public MogaChangesReport getMogaChangesReport(Integer mogaTypeId) {
        return this.mogaChanges.get(mogaTypeId);
    }

    public void setDamage(Moga moga, int damage) {
        Integer mogaTypeId = Integer.valueOf(moga.getType().getId());
        Integer damageDone = this.damageDoneByMoga.get(mogaTypeId);
        if (damageDone == null) {
            damageDone = 0;
        }
        this.damageDoneByMoga.put(mogaTypeId, Integer.valueOf(damageDone.intValue() + Math.min(damage, moga.getHitPoints())));
    }

    private int getDamage(int mogaTypeId) {
        Integer damage = this.damageDoneByMoga.get(Integer.valueOf(mogaTypeId));
        if (damage == null) {
            return 0;
        }
        return damage.intValue();
    }

    public void addKilledMoga(Moga computerMoga) {
        if (!this.killedMogas.contains(computerMoga)) {
            this.killedMogas.add(computerMoga);
        }
    }

    public List<Moga> getKilledMogas() {
        return this.killedMogas;
    }

    public void addCapturedMoga(Moga computerMoga) {
        if (!this.capturedMogas.contains(computerMoga)) {
            this.capturedMogas.add(computerMoga);
        }
    }

    public List<Moga> getCapturedMogas() {
        return this.capturedMogas;
    }

    public void setScoredNormal(boolean value) {
        this.scoredNormal = value;
    }

    public boolean hasScoredNormal() {
        return this.scoredNormal;
    }

    public void setScoredVeryGood(boolean value) {
        this.scoredVeryGood = value;
    }

    public boolean hasScoredVeryGood() {
        return this.scoredVeryGood;
    }

    public boolean hasScoredPerfect() {
        return this.scoredPerfect;
    }

    public void setScoredPerfect(boolean value) {
        this.scoredPerfect = value;
    }

    public boolean isHighScore() {
        return this.scoredHighScore;
    }

    public void setHighScore(boolean value) {
        this.scoredHighScore = value;
    }

    public int getNumberOfRewardedBlueCoffees() {
        return this.numberOfRewardedBlueCoffees;
    }

    public void setNumberOfRewardedBlueCoffees(int numberOfRewardedBlueCoffees) {
        this.numberOfRewardedBlueCoffees = numberOfRewardedBlueCoffees;
    }

    public int getNumberOfRewardedStarSeeds() {
        return this.numberOfRewardedStarSeeds;
    }

    public void setNumberOfRewardedStarSeeds(int numberOfRewardedStarSeeds) {
        this.numberOfRewardedStarSeeds = numberOfRewardedStarSeeds;
    }

    public void setWinner(Winner winner) {
        this.winner = winner;
    }

    public boolean playerHasWon() {
        return this.winner == Winner.PLAYER;
    }

    public boolean wasCaptureEnabled() {
        return this.wasCaptureEnabled;
    }

    public boolean areAllMogasKilledOrCaptured(Node currentNode) {
        boolean retValue = true;
        List<Moga> killedMogas = getKilledMogas();
        List<Moga> capturedMogas = getCapturedMogas();
        for (Moga moga : currentNode.getTeam().getMogas()) {
            if (!killedMogas.contains(moga) && !capturedMogas.contains(moga)) {
                retValue = false;
                break;
            }
        }
        if (this.trainer.hasActiveCaptureQuestOnCurrentNode() && capturedMogas.size() == 0) {
            return false;
        }
        return retValue;
    }

    public boolean hasCapturedMogas() {
        return this.capturedMogas.size() > 0;
    }

    public boolean hasCompletedIsland() {
        return !this.wasIslandCompletedWhenBattleStarted && this.trainer.isIslandCompleted(this.trainer.getCurrentIsland());
    }

    public boolean hasUnlockedNextIsland() {
        return this.wasNextIslandLockedWhenBattleStarted && this.trainer.canGoToIsland(this.trainer.getCurrentIsland().getNextIsland());
    }

    public boolean hasCompletedNode(int nodeId) {
        return !this.completedNodeIdsBeforeBattle.contains(Integer.valueOf(nodeId)) && this.trainer.getCompletedNodesIds().contains(Integer.valueOf(nodeId));
    }

    public Island getUnlockedIsland() {
        if (!hasUnlockedNextIsland()) {
            return null;
        }
        Island unlockedIsland = this.trainer.getCurrentIsland().getNextIsland();
        return unlockedIsland;
    }

    public void update() {
        updateTeamMogas();
        updateCapturedMogas();
    }

    public void commitCaptures() {
        if (getCapturedMogas().size() > 0) {
            for (Moga moga : getCapturedMogas()) {
                List<Moga> teamMogas = Game.getTrainer().getTeam().getMogas();
                if (teamMogas.size() < 3) {
                    teamMogas.add(moga);
                }
            }
        }
    }

    private void updateTeamMogas() {
        this.mogaMaxLevelReached = false;
        for (Moga moga : this.trainer.getTeam().getMogas()) {
            int damageDone = getDamage(moga.getType().getId());
            int variableXP = (int) (damageDone * Constants.variableXPMultiplierDamage * Constants.variableXPWeightDamage);
            int variableXP2 = Math.max(0, variableXP);
            int fixedXP = 0;
            for (Moga capturedMoga : getCapturedMogas()) {
                fixedXP += capturedMoga.getType().getFixedXP();
            }
            for (Moga killedMoga : getKilledMogas()) {
                fixedXP += killedMoga.getType().getFixedXP();
            }
            int variableXP3 = variableXP2 + fixedXP;
            int moga1TotalXP = variableXP3;
            if (hasCapturedMogas()) {
                float captureMultiplier = 0.0f;
                for (Moga capturedMoga2 : getCapturedMogas()) {
                    float captureBonus = capturedMoga2.getType().getCaptureBonus();
                    if (captureBonus > captureMultiplier) {
                        captureMultiplier = captureBonus;
                    }
                }
                float mult = 1.0f + captureMultiplier;
                moga1TotalXP = (int) (moga1TotalXP * mult);
            }
            if (moga.getHitPoints() == 0) {
                variableXP3 = 0;
                moga1TotalXP = 0;
            }
            int mogaOldLevel = moga.getLevel();
            int mogaOldLevelCompletion = moga.getLevelCompletion();
            moga.setExperience(moga.getExperience() + moga1TotalXP);
            int mogaNewLevel = moga.getLevel();
            int maxLevel = Game.getTrainer().getMaxUnlockedLevel();
            if (mogaNewLevel >= maxLevel) {
                moga.setExperience(Moga.getCumulativeExperienceForLevel(maxLevel));
                mogaNewLevel = maxLevel;
                this.mogaMaxLevelReached = true;
            }
            int mogaNewLevelCompletion = moga.getLevelCompletion();
            if (mogaOldLevel != mogaNewLevel) {
                moga.fullyRechargeHealth();
            }
            MogaChangesReport changesReport = new MogaChangesReport();
            changesReport.setVariableXP(variableXP3);
            changesReport.setTotalXP(moga1TotalXP);
            changesReport.setLevel(mogaOldLevel);
            changesReport.setNewLevel(mogaNewLevel);
            changesReport.setLevelCompletion(mogaOldLevelCompletion);
            changesReport.setNewLevelCompletion(mogaNewLevelCompletion);
            this.mogaChanges.put(Integer.valueOf(moga.getType().getId()), changesReport);
        }
    }

    public boolean isMogaMaxLevelReached() {
        return this.mogaMaxLevelReached;
    }

    private void updateCapturedMogas() {
        for (Moga moga : this.capturedMogas) {
            moga.fullyRechargeHealth();
            moga.fullyRechargeZodiac();
        }
    }
}
