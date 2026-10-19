package com.gaiaonline.monstergalaxy.app.notifications.types;

import com.gaiaonline.monstergalaxy.app.Clock;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.ScreenCode;
import com.gaiaonline.monstergalaxy.app.notifications.MogaNotification;
import com.gaiaonline.monstergalaxy.settings.NotificationType;
import java.util.Calendar;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class DailyPrizeNotification extends MogaNotification {
    public DailyPrizeNotification(int id) {
        super(id);
    }

    @Override // com.gaiaonline.monstergalaxy.app.notifications.MogaNotification
    public void doRecalculate() {
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(Clock.getCurrentTimeMillis());
        Calendar prizeDate = Calendar.getInstance();
        if (Game.getTrainer().getClaimDailyPrizeAvaiableTime() == 0) {
            prizeDate.setTimeInMillis(now.getTimeInMillis());
            prizeDate.add(5, 1);
        } else {
            prizeDate.setTimeInMillis(Game.getGaiaClient().toDeviceTime(Game.getTrainer().getClaimDailyPrizeAvaiableTime()) * 1000);
            if (now.getTimeInMillis() > prizeDate.getTimeInMillis()) {
                int hour = prizeDate.get(11);
                int minute = prizeDate.get(12);
                prizeDate.setTimeInMillis(now.getTimeInMillis());
                prizeDate.set(11, hour);
                prizeDate.set(12, minute);
                if (now.getTimeInMillis() > prizeDate.getTimeInMillis()) {
                    prizeDate.add(5, 1);
                }
            }
        }
        this.fireTime = prizeDate.getTimeInMillis();
        this.willFire = true;
    }

    @Override // com.gaiaonline.monstergalaxy.app.notifications.MogaNotification
    public ScreenCode getTargetScreenName() {
        return ScreenCode.TAMER;
    }

    @Override // com.gaiaonline.monstergalaxy.app.notifications.MogaNotification
    public NotificationType getType() {
        return NotificationType.DAILY_PRIZE;
    }
}
