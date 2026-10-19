package com.gaiaonline.monstergalaxy.app.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.gaiaonline.monstergalaxy.app.Clock;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.notifications.types.IslandNotification;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;
import com.gaiaonline.monstergalaxy.settings.NotificationType;
import com.gaiaonline.monstergalaxy.storage.StorageHelper;
import com.sessionm.ui.SessionMActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class MogaNotificationManager {
    private Context context;
    private Set<MogaNotification> notifications;

    public MogaNotificationManager(Context context) {
        this.context = context;
    }

    public void loadAlarms(StorageHelper storage) {
        this.notifications = storage.getNotifications();
    }

    public void installAlarms() {
        List<Long> fireTimes = new ArrayList<>();
        if (MonsterGalaxy.isGameInitialized()) {
            for (MogaNotification notification : this.notifications) {
                if (notification.getType() != NotificationType.ISLAND) {
                    notification.recalculate();
                    if (notification.willFire()) {
                        installAlarm(notification);
                        fireTimes.add(Long.valueOf(notification.getFireTime()));
                    }
                }
            }
            for (MogaNotification notification2 : this.notifications) {
                if (notification2.getType() == NotificationType.ISLAND) {
                    IslandNotification islandNotification = (IslandNotification) notification2;
                    if (islandNotification.getIslandId() == Game.getTrainer().getCurrentIsland().getId()) {
                        islandNotification.setOtherFireTimes(fireTimes);
                        notification2.recalculate();
                        if (notification2.willFire()) {
                            installAlarm(notification2);
                        }
                    }
                }
            }
        }
    }

    private void installAlarm(MogaNotification notification) {
        Game.getTrainer().getScheduledAlarms().add(notification);
        Intent intent = new Intent(this.context, (Class<?>) NotificationBroadcastReceiver.class);
        notification.setNextText();
        intent.putExtra("title", notification.getText().getTitle());
        intent.putExtra("message", notification.getText().getMessage());
        intent.putExtra("targetScreen", notification.getTargetScreenName().toString());
        intent.putExtra("fireTime", notification.getFireTime());
        intent.putExtra("id", notification.getId());
        intent.putExtra(SessionMActivity.INTENT_PARAM_TYPE, notification.getType().toString());
        long localFireTime = Clock.toLocalTimeMillis(notification.getFireTime());
        PendingIntent operation = PendingIntent.getBroadcast(this.context, notification.getId(), intent, 134217728);
        ((AlarmManager) this.context.getSystemService("alarm")).set(1, localFireTime, operation);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        Log.i(Game.TAG, "Alarm set at " + sdf.format(new Date(localFireTime)) + " local time = " + sdf.format(new Date(notification.getFireTime())) + " server time / type: " + notification.getType());
    }

    public void uninstallAlarms() {
        if (MonsterGalaxy.isGameInitialized()) {
            AlarmManager alarmManager = (AlarmManager) this.context.getSystemService("alarm");
            for (MogaNotification alarm : this.notifications) {
                if (alarm.willFire()) {
                    Intent intent = new Intent(this.context, (Class<?>) NotificationBroadcastReceiver.class);
                    PendingIntent operation = PendingIntent.getBroadcast(this.context, alarm.getId(), intent, 134217728);
                    alarmManager.cancel(operation);
                }
            }
            Trainer trainer = Game.getTrainer();
            for (MogaNotification notification : trainer.getScheduledAlarms()) {
                if (notification.getFireTime() < System.currentTimeMillis() && notification.getType() == NotificationType.ISLAND) {
                    Integer textIndex = trainer.getLastNotificationTexts().get(Integer.valueOf(notification.getId()));
                    if (textIndex == null || textIndex.intValue() < notification.getTextIndex()) {
                        trainer.getLastNotificationTexts().put(Integer.valueOf(notification.getId()), Integer.valueOf(notification.getTextIndex()));
                    } else {
                        trainer.getLastNotificationTexts().put(Integer.valueOf(notification.getId()), Integer.valueOf(notification.getTextIndex()));
                    }
                }
            }
            trainer.getScheduledAlarms().clear();
        }
    }
}
