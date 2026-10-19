package com.gaiaonline.monstergalaxy.model.quest;

import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.model.map.Island;
import com.gaiaonline.monstergalaxy.model.map.Node;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import java.io.Serializable;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class QuestProgress implements Serializable {
    private static final long serialVersionUID = 584078668218407154L;
    private Quest quest;
    private int requiredTasks = -1;
    private int completedTasks = -1;

    public QuestProgress(Quest quest) {
        this.quest = quest;
    }

    public Quest getQuest() {
        return this.quest;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    public int getCompletedTasks() {
        if (this.completedTasks == -1) {
            if (this.quest.getType() != Quest.Type.BEAT_NODE && this.quest.getType() == Quest.Type.STARS) {
                Island island = this.quest.getIslandToWinStars();
                this.completedTasks = Game.getTrainer().getNumberOfStarsWonInIsland(island);
            } else {
                this.completedTasks = 0;
            }
        }
        return this.completedTasks;
    }

    public int getRequiredTasks() {
        if (this.requiredTasks == -1) {
            if (this.quest.getType() == Quest.Type.BEAT_NODE) {
                this.requiredTasks = 1;
            } else if (this.quest.getType() == Quest.Type.STARS) {
                this.requiredTasks = this.quest.getNumberOfStarsToWinInIsland();
            } else {
                this.requiredTasks = this.quest.getNumberOfContenders();
            }
        }
        return this.requiredTasks;
    }

    public boolean isCompleted() {
        return this.completedTasks >= getRequiredTasks();
    }

    private int getContenderMogaTypeCount(List<Moga> mogas) {
        int count = 0;
        for (Moga moga : mogas) {
            if (this.quest.getContenderMogaTypeId() == moga.getType().getId()) {
                count++;
            }
        }
        return count;
    }

    public void update(Node capturedNode, List<Moga> killedMogas, List<Moga> capturedMogas) {
        if (this.quest.getType() == Quest.Type.BEAT_NODE && capturedNode != null && this.quest.getBeatNodeId() == capturedNode.getId()) {
            this.completedTasks = 1;
            return;
        }
        if (this.quest.getType() == Quest.Type.STARS) {
            Island island = this.quest.getIslandToWinStars();
            this.completedTasks = Game.getTrainer().getNumberOfStarsWonInIsland(island);
            return;
        }
        if (killedMogas.size() > 0 || capturedMogas.size() > 0) {
            int count = 0;
            if (this.quest.getType() == Quest.Type.KILL) {
                count = getContenderMogaTypeCount(killedMogas);
            } else if (this.quest.getType() == Quest.Type.CAPTURE) {
                count = getContenderMogaTypeCount(capturedMogas);
            }
            if (this.completedTasks < 0) {
                this.completedTasks = 0;
            }
            this.completedTasks += count;
        }
    }
}
