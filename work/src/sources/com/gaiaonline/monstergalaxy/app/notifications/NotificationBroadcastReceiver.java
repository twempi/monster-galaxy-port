package com.gaiaonline.monstergalaxy.app.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.gaiaonline.monstergalaxy.R;
import com.gaiaonline.monstergalaxy.app.Clock;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxyAndroid;
import com.gaiaonline.monstergalaxy.settings.NotificationType;
import com.gaiaonline.monstergalaxy.settings.SettingsManager;
import com.sessionm.ui.SessionMActivity;

/* JADX INFO: loaded from: classes.dex */
public class NotificationBroadcastReceiver extends BroadcastReceiver {
    private final long THRESHOLD_MILLIS = 5000;

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        try {
            int id = intent.getIntExtra("id", 0);
            long fireTime = intent.getLongExtra("fireTime", 0L);
            String title = intent.getStringExtra("title");
            String message = intent.getStringExtra("message");
            String targetScreen = intent.getStringExtra("targetScreen");
            String type = intent.getStringExtra(SessionMActivity.INTENT_PARAM_TYPE);
            NotificationType notificationType = NotificationType.valueOf(type);
            if (SettingsManager.isAlertEnabled(notificationType)) {
                long serverCurretTimeMillis = Clock.getCurrentTimeMillis();
                if (Math.abs(fireTime - serverCurretTimeMillis) <= 5000) {
                    showNotification(context, id, title, message, targetScreen, Clock.toLocalTimeMillis(fireTime), type);
                    Log.i(Game.TAG, "Notification type " + type + " fired.");
                } else {
                    Log.i(Game.TAG, "Notification type " + type + " fired " + ((fireTime - serverCurretTimeMillis) / 1000) + " seconds from its originally scheduled time. Ignoring notification.");
                }
            } else {
                Log.i(Game.TAG, "Notification type " + type + " is disabled. Skipping notification.");
            }
        } catch (Exception e) {
            Log.e(Game.TAG, "Exception in NotificationBroadcastReceiver.onReceive()");
        }
    }

    private void showNotification(Context context, int id, String title, String message, String targetScreen, long when, String type) {
        Notification notification = new Notification(R.drawable.ic_stat_starseed, title, when);
        Intent intent = new Intent(context, (Class<?>) MonsterGalaxyAndroid.class);
        intent.putExtra("screen", targetScreen);
        intent.putExtra("notificationType", type);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent, 134217728);
        notification.setLatestEventInfo(context, title, message, pendingIntent);
        notification.defaults |= 1;
        notification.flags |= 16;
        ((NotificationManager) context.getSystemService("notification")).notify(id, notification);
    }
}
