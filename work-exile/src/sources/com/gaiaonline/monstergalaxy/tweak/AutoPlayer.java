package com.gaiaonline.monstergalaxy.tweak;

import com.badlogic.gdx.utils.Logger;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.ScreenCode;
import com.gaiaonline.monstergalaxy.map.WorldScreen;
import com.gaiaonline.monstergalaxy.model.AttackType;
import com.gaiaonline.monstergalaxy.model.battle.Battle;
import com.gaiaonline.monstergalaxy.model.battle.BattleReport;
import com.gaiaonline.monstergalaxy.model.map.Island;
import com.gaiaonline.monstergalaxy.model.map.Node;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.model.quest.Quest;
import com.gaiaonline.monstergalaxy.model.trainer.Team;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class AutoPlayer {
    private int islandIdToComplete;
    private Node node;
    private int lastNodePlayCount = 0;
    private Node previousPlayedNode = null;
    private Logger logger = new Logger(AutoPlayer.class.getName());
    private Trainer trainer = Game.getTrainer();

    public void setIslandToComplete(int islandIndex) {
        Tweak.setForcedPlayerMogaLevel(50);
        this.trainer.getTeam().getMogas().get(0).setExperience(Moga.getCumulativeExperienceForLevel(Tweak.getForcedPlayerMogaLevel().intValue()));
        this.islandIdToComplete = Game.getStorage().getAllIslands().get(islandIndex).getId();
    }

    public Node playNextNode() {
        this.node = getNextNode(this.islandIdToComplete);
        if (this.node == null) {
            Game.save();
            return this.node;
        }
        if (this.node == this.previousPlayedNode) {
            this.lastNodePlayCount++;
            if (this.lastNodePlayCount > 10) {
                throw new RuntimeException("Played node " + this.node.getName() + " " + this.lastNodePlayCount + " times and still not completed");
            }
        } else {
            this.lastNodePlayCount = 0;
        }
        playNode(this.node);
        this.previousPlayedNode = this.node;
        return this.node;
    }

    private Node getNextNode(int lastIsland) {
        for (Island island : Game.getStorage().getAllIslands()) {
            if (island.getId() > lastIsland) {
                return null;
            }
            if (this.trainer.isIslandCompleted(island.getPreviousIsland())) {
                if (this.trainer.hasCapturedEnoughMogas(island)) {
                    for (Node node : island.getNodes()) {
                        if (this.trainer.canGoToNode(node) && this.trainer.isNodeSuggested(node)) {
                            return node;
                        }
                    }
                } else {
                    return getFirstCaptureNode();
                }
            }
        }
        return null;
    }

    private Node getFirstCaptureNode() {
        for (Island island : Game.getStorage().getAllIslands()) {
            if (this.trainer.canGoToIsland(island)) {
                this.trainer.setCurrentIsland(island);
                for (Node node : island.getNodes()) {
                    if (this.trainer.canGoToNode(node)) {
                        this.trainer.setCurrentNode(node);
                        node.createTeam();
                        Team team = node.getTeam();
                        for (Moga cpuMoga = team.getFirstMoga(); cpuMoga != null; cpuMoga = team.getNextMoga()) {
                            team.setDeployedMoga(cpuMoga);
                            cpuMoga.setHitPoints(1);
                            if (this.trainer.canCaptureMoga(cpuMoga)) {
                                return node;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private void playNode(Node node) {
        if (!node.getIsland().equals(this.trainer.getCurrentIsland())) {
            this.trainer.setCurrentIsland(node.getIsland());
        }
        this.logger.log("Playing node " + node.getId() + " - " + node.getName());
        this.trainer.setCurrentNode(node);
        Quest currentQuest = null;
        if (this.trainer.getCompletedUnpaidQuests().size() > 0) {
            Quest currentQuest2 = this.trainer.getCompletedUnpaidQuests().get(0);
            Quest currentQuest3 = currentQuest2;
            this.trainer.setPaidQuest(currentQuest3);
            currentQuest = this.trainer.getNextQuest(currentQuest3.getId());
        }
        if (currentQuest == null) {
            currentQuest = this.trainer.getAvailableQuestForNode(node);
        }
        if (currentQuest == null) {
            playBattle();
        } else {
            acceptQuest(currentQuest);
        }
    }

    private void acceptQuest(Quest quest) {
        if (this.trainer.isQuestCompleted(quest.getId())) {
            this.logger.log("Quest completed " + quest.getId() + " - " + quest.getName());
            this.trainer.setPaidQuest(quest);
            Quest nextQuest = this.trainer.getNextQuest(quest.getId());
            if (nextQuest != null) {
                acceptQuest(nextQuest);
                return;
            }
            BattleReport report = this.trainer.getBattleReport();
            if (report != null && report.hasUnlockedNextIsland()) {
                MonsterGalaxy.showScreen(new WorldScreen(this.trainer.getCurrentIsland().getNextIsland().getId()));
                return;
            } else {
                MonsterGalaxy.showScreen(ScreenCode.ISLAND);
                return;
            }
        }
        this.logger.log("Quest started " + quest.getId() + " - " + quest.getName());
        this.trainer.startQuest(quest);
        BattleReport report2 = this.trainer.getBattleReport();
        if ((report2 == null || !report2.hasUnlockedNextIsland()) && 0 == 0) {
            playBattle();
        }
    }

    private void playBattle() {
        this.logger.log("Playing battle " + this.trainer.getCurrentNode().getId() + " - " + this.trainer.getCurrentNode().getName());
        Battle battle = MonsterGalaxy.staticCreateBattle();
        battle.init();
        Moga playerMoga = this.trainer.getTeam().getMogas().get(0);
        battle.deployPlayerMoga(playerMoga);
        this.logger.log("Deployed player moga " + playerMoga.getName());
        Team cpuTeam = this.trainer.getCurrentNode().getTeam();
        for (Moga cpuMoga = cpuTeam.getFirstMoga(); cpuMoga != null; cpuMoga = cpuTeam.getNextMoga()) {
            battle.deployComputerMoga(cpuMoga);
            this.logger.log("Deployed cpu moga " + cpuMoga.getName());
            cpuMoga.setHitPoints(1);
            if (this.trainer.canCaptureMoga(cpuMoga)) {
                this.trainer.addCoins(cpuMoga.getType().getCaptureCost());
                battle.requestThrowMasterSeed();
                battle.mogaWasCaptured();
                this.logger.log("Captured moga " + cpuMoga.getName());
            } else {
                cpuMoga.fullyRechargeHealth();
                playerAttack(battle);
                while (cpuMoga.getHitPoints() > 0) {
                    computerHarmlessAttack(battle);
                    playerAttack(battle);
                }
                this.logger.log("Killed moga " + cpuMoga.getName());
            }
        }
        this.trainer.battleOver();
        this.logger.log("Battle won " + this.trainer.getCurrentNode().getId() + " - " + this.trainer.getCurrentNode().getName());
    }

    private void playerAttack(Battle battle) {
        this.logger.log("Player attack");
        battle.setAttackType(AttackType.REGULAR);
        battle.playerAttack(1.0f);
    }

    private void computerHarmlessAttack(Battle battle) {
        this.logger.log("Computer attack");
        battle.computerAttack(AttackType.REGULAR, 0.0f);
    }
}
