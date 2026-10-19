package com.gaiaonline.monstergalaxy.app.notifications.types;

import com.gaiaonline.monstergalaxy.app.Clock;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.ScreenCode;
import com.gaiaonline.monstergalaxy.app.notifications.MogaNotification;
import com.gaiaonline.monstergalaxy.settings.NotificationType;
import com.tapjoy.TapjoyConstants;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class IslandNotification extends MogaNotification {
    private List<Long> otherFireTimes;
    private static long MIN = TapjoyConstants.THROTTLE_GET_TAP_POINTS_INTERVAL;
    private static long HOUR = 60 * MIN;
    private static long MIN_TIME_FROM_LAST = HOUR * 6;
    private static long MIN_TIME_TO_NEXT = HOUR * 6;
    private static long[] DISALLOWED_TIME_FRAMES = {0 * HOUR, 10 * HOUR, 22 * HOUR, 24 * HOUR};

    public IslandNotification(int id) {
        super(id);
    }

    public void setOtherFireTimes(List<Long> otherFireTimes) {
        this.otherFireTimes = otherFireTimes;
    }

    @Override // com.gaiaonline.monstergalaxy.app.notifications.MogaNotification
    public void setNextText() {
        Integer index;
        Integer index2 = Game.getTrainer().getLastNotificationTexts().get(Integer.valueOf(this.id));
        if (index2 == null) {
            index = 0;
        } else {
            index = Integer.valueOf((index2.intValue() + 1) % this.texts.size());
        }
        this.textIndex = index.intValue();
        this.text = this.texts.get(index.intValue());
    }

    @Override // com.gaiaonline.monstergalaxy.app.notifications.MogaNotification
    public void doRecalculate() {
        Integer index = Game.getTrainer().getLastNotificationTexts().get(Integer.valueOf(this.id));
        if (index == null || index.intValue() < this.texts.size() - 1) {
            this.willFire = true;
        } else {
            this.willFire = false;
        }
        if (this.willFire) {
            Collections.sort(this.otherFireTimes);
            Calendar time = Calendar.getInstance();
            time.setTimeInMillis(Clock.getCurrentTimeMillis() + MIN_TIME_FROM_LAST);
            Iterator<Long> it = this.otherFireTimes.iterator();
            while (it.hasNext()) {
                long otherTime = it.next().longValue();
                setNextAllowedTime(time);
                if (time.getTimeInMillis() > otherTime - MIN_TIME_TO_NEXT && time.getTimeInMillis() < MIN_TIME_FROM_LAST + otherTime) {
                    time.setTimeInMillis(MIN_TIME_FROM_LAST + otherTime + (1 * MIN));
                }
            }
            setNextAllowedTime(time);
            this.fireTime = time.getTimeInMillis();
        }
    }

    private static void setNextAllowedTime(Calendar time) {
        Calendar lastMidnight = Calendar.getInstance();
        for (int j = 0; j < 2; j++) {
            lastMidnight.set(time.get(1), time.get(2), time.get(5), 0, 0, 0);
            for (int i = 0; i < DISALLOWED_TIME_FRAMES.length; i += 2) {
                long startTime = lastMidnight.getTimeInMillis() + DISALLOWED_TIME_FRAMES[i];
                long endTime = lastMidnight.getTimeInMillis() + DISALLOWED_TIME_FRAMES[i + 1];
                if (time.getTimeInMillis() > startTime && time.getTimeInMillis() < endTime) {
                    time.setTimeInMillis((1 * MIN) + endTime);
                }
            }
        }
    }

    @Override // com.gaiaonline.monstergalaxy.app.notifications.MogaNotification
    public ScreenCode getTargetScreenName() {
        return ScreenCode.ISLAND;
    }

    @Override // com.gaiaonline.monstergalaxy.app.notifications.MogaNotification
    public NotificationType getType() {
        return NotificationType.ISLAND;
    }
}
