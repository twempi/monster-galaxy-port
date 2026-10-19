package com.gaiaonline.monstergalaxy.model.battle;

import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.battle.BattleReportScreen;
import com.gaiaonline.monstergalaxy.model.moga.Moga;

/* JADX INFO: loaded from: classes.dex */
public class MockBattleReport extends BattleReport {
    public static void showMockBattleReportScreen() {
        BattleReport battleReport = new MockBattleReport();
        Game.getTrainer().setBattleReport(battleReport);
        MonsterGalaxy.showScreen(new BattleReportScreen());
    }

    private MockBattleReport() {
        setHighScore(true);
        setMaxLevelAtStart(false);
        setNumberOfRewardedBlueCoffees(2);
        setNumberOfRewardedStarSeeds(0);
        setScoredNormal(true);
        setScoredVeryGood(true);
        setScoredPerfect(true);
        addCapturedMoga(Moga.newMogaFromType(Game.getStorage().getMogaType(28)));
        update();
    }

    @Override // com.gaiaonline.monstergalaxy.model.battle.BattleReport
    public boolean isMogaMaxLevelReached() {
        return false;
    }

    @Override // com.gaiaonline.monstergalaxy.model.battle.BattleReport
    public MogaChangesReport getMogaChangesReport(Integer mogaTypeId) {
        MogaChangesReport mogarep = new MogaChangesReport();
        if (Game.getTrainer().getTeam().getMogas().size() > 2) {
            if (mogaTypeId.intValue() == Game.getTrainer().getTeam().getMogas().get(0).getType().getId()) {
                mogarep.setLevel(2);
                mogarep.setNewLevel(50);
                mogarep.setLevelCompletion(20);
                mogarep.setTotalXP(50);
                mogarep.setVariableXP(20);
            } else if (mogaTypeId.intValue() == Game.getTrainer().getTeam().getMogas().get(1).getType().getId() || mogaTypeId.intValue() == Game.getTrainer().getTeam().getMogas().get(2).getType().getId()) {
                mogarep.setLevel(2);
                mogarep.setNewLevel(3);
                mogarep.setLevelCompletion(20);
                mogarep.setTotalXP(50);
                mogarep.setVariableXP(20);
            }
        }
        return mogarep;
    }
}
