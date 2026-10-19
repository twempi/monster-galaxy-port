package com.gaiaonline.monstergalaxy.app.notifications;

import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import java.util.Iterator;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class NotificationText {
    private String buttonText;
    private int id;
    private String messageTemplate;
    private String title;

    public NotificationText(int id, String messageTemplate, String title, String buttonText) {
        this.id = id;
        this.messageTemplate = messageTemplate;
        this.title = title;
        this.buttonText = buttonText;
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return "Island".equals(this.title) ? "Island story" : this.title;
    }

    public String getMessage() {
        String message = this.messageTemplate.replace("${TAMER_NAME}", Game.getTrainer().getName());
        if (message.contains("${MOGA_NAME}")) {
            return message.replace("${MOGA_NAME}", getHighestLevelMoga().getName());
        }
        return message;
    }

    private Moga getHighestLevelMoga() {
        int highestLevel = 1;
        if (Game.getTrainer().getTeam().getMogas().size() > 0) {
            Iterator<Moga> it = Game.getTrainer().getTeam().getMogas().iterator();
            while (it.hasNext()) {
                highestLevel = Math.max(highestLevel, it.next().getLevel());
            }
            if (Game.getTrainer().getTeam().getDeployedMoga().getLevel() == highestLevel) {
                return Game.getTrainer().getTeam().getDeployedMoga();
            }
            for (Moga moga : Game.getTrainer().getTeam().getMogas()) {
                if (moga.getLevel() == highestLevel) {
                    return moga;
                }
            }
        }
        Iterator<Moga> it2 = Game.getTrainer().getMogas().iterator();
        while (it2.hasNext()) {
            highestLevel = Math.max(highestLevel, it2.next().getLevel());
        }
        for (Moga moga2 : Game.getTrainer().getMogas()) {
            if (moga2.getLevel() == highestLevel) {
                return moga2;
            }
        }
        return null;
    }

    public String getMessageTemplate() {
        return this.messageTemplate;
    }

    public String getButtonText() {
        return this.buttonText;
    }
}
