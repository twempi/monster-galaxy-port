package com.gaiaonline.monstergalaxy.app.notifications.types;

import com.gaiaonline.monstergalaxy.app.Clock;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.ScreenCode;
import com.gaiaonline.monstergalaxy.app.notifications.MogaNotification;
import com.gaiaonline.monstergalaxy.settings.NotificationType;

/* JADX INFO: loaded from: classes.dex */
public class TamerEnergyNotification extends MogaNotification {
    public TamerEnergyNotification(int id) {
        super(id);
    }

    @Override // com.gaiaonline.monstergalaxy.app.notifications.MogaNotification
    public void doRecalculate() {
        this.willFire = !Game.getTrainer().isEnergyFullyCharged();
        if (this.willFire) {
            this.fireTime = Clock.getCurrentTimeMillis() + ((long) (Game.getTrainer().getTimeUntilEnergyFullyCharged() * 1000));
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.notifications.MogaNotification
    public ScreenCode getTargetScreenName() {
        return ScreenCode.TAMER;
    }

    @Override // com.gaiaonline.monstergalaxy.app.notifications.MogaNotification
    public NotificationType getType() {
        return NotificationType.TAMER_ENERGY_FULL;
    }
}
