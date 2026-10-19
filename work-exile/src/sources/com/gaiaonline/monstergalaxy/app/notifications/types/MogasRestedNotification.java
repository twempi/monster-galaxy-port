package com.gaiaonline.monstergalaxy.app.notifications.types;

import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.ScreenCode;
import com.gaiaonline.monstergalaxy.app.notifications.MogaNotification;
import com.gaiaonline.monstergalaxy.settings.NotificationType;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class MogasRestedNotification extends MogaNotification {
    public MogasRestedNotification(int id) {
        super(id);
    }

    @Override // com.gaiaonline.monstergalaxy.app.notifications.MogaNotification
    public void doRecalculate() {
        this.willFire = Game.getTrainer().hasSleepingMogas();
        if (this.willFire) {
            this.fireTime = Game.getTrainer().getFullTeamRestedTime() * 1000;
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.notifications.MogaNotification
    public ScreenCode getTargetScreenName() {
        return ScreenCode.ISLAND;
    }

    @Override // com.gaiaonline.monstergalaxy.app.notifications.MogaNotification
    public NotificationType getType() {
        return NotificationType.MOGAS_RESTED;
    }
}
