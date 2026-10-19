package com.gaiaonline.monstergalaxy.app.notifications;

import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.ScreenCode;
import com.gaiaonline.monstergalaxy.app.notifications.types.DailyPrizeNotification;
import com.gaiaonline.monstergalaxy.app.notifications.types.IslandNotification;
import com.gaiaonline.monstergalaxy.app.notifications.types.MogasRestedNotification;
import com.gaiaonline.monstergalaxy.app.notifications.types.TamerEnergyNotification;
import com.gaiaonline.monstergalaxy.settings.NotificationType;
import java.util.List;
import java.util.Random;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class MogaNotification {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$settings$NotificationType;
    protected long fireTime;
    protected int id;
    protected int islandId;
    protected NotificationText text;
    protected int textIndex;
    protected List<NotificationText> texts;
    protected boolean willFire;

    public abstract void doRecalculate();

    public abstract ScreenCode getTargetScreenName();

    public abstract NotificationType getType();

    static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$settings$NotificationType() {
        int[] iArr = $SWITCH_TABLE$com$gaiaonline$monstergalaxy$settings$NotificationType;
        if (iArr == null) {
            iArr = new int[NotificationType.valuesCustom().length];
            try {
                iArr[NotificationType.DAILY_PRIZE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[NotificationType.ISLAND.ordinal()] = 4;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[NotificationType.MOGAS_RESTED.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[NotificationType.TAMER_ENERGY_FULL.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            $SWITCH_TABLE$com$gaiaonline$monstergalaxy$settings$NotificationType = iArr;
        }
        return iArr;
    }

    public MogaNotification(int id) {
        this.id = id;
    }

    public final void recalculate() {
        if (Game.areTrainerAndMogaReady()) {
            doRecalculate();
        } else {
            this.willFire = false;
        }
    }

    public boolean willFire() {
        return this.willFire;
    }

    public long getFireTime() {
        return this.fireTime;
    }

    public int getId() {
        return this.id;
    }

    public void setTexts(List<NotificationText> texts) {
        this.texts = texts;
    }

    public void setIslandId(int islandId) {
        this.islandId = islandId;
    }

    public int getIslandId() {
        return this.islandId;
    }

    public void setNextText() {
        int index;
        if (this.texts.size() == 1) {
            index = 0;
        } else {
            Random random = new Random();
            Integer lastUsedIndex = Game.getTrainer().getLastNotificationTexts().get(Integer.valueOf(this.id));
            if (lastUsedIndex == null) {
                index = random.nextInt(this.texts.size());
            } else {
                index = random.nextInt(this.texts.size() - 1);
                if (index >= lastUsedIndex.intValue()) {
                    index++;
                }
            }
        }
        this.textIndex = index;
        this.text = this.texts.get(index);
    }

    public int getTextIndex() {
        return this.textIndex;
    }

    public NotificationText getText() {
        return this.text;
    }

    public static MogaNotification newInstance(NotificationType type, int id) {
        switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$settings$NotificationType()[type.ordinal()]) {
            case 1:
                return new DailyPrizeNotification(id);
            case 2:
                return new MogasRestedNotification(id);
            case 3:
                return new TamerEnergyNotification(id);
            case 4:
                return new IslandNotification(id);
            default:
                return null;
        }
    }
}
