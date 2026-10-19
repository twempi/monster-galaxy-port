package com.gaiaonline.monstergalaxy.app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.gaiaonline.monstergalaxy.R;
import com.gaiaonline.monstergalaxy.app.notifications.MogaNotificationManager;
import com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelperImpl;
import com.gaiaonline.monstergalaxy.service.AndroidAdsService;
import com.gaiaonline.monstergalaxy.service.GaiaClient;
import com.gaiaonline.monstergalaxy.settings.NotificationType;
import com.gaiaonline.monstergalaxy.tweak.AutoPlayerScreen;
import com.nextive.android.billing.market.BillingResponseHandler;
import com.nextive.android.billing.market.BillingService;
import org.json.JSONException;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class MonsterGalaxyAndroid extends AndroidApplication {
    private MonsterGalaxy game;
    private MogaNotificationManager notificationManager;

    @Override // android.app.Activity
    @SuppressLint({"NewApi"})
    public void onCreate(Bundle savedInstanceState) throws JSONException {
        super.onCreate(savedInstanceState);
        this.game = MonsterGalaxy.getInstance();
        AndroidPlatformHelperImpl platformHelper = new AndroidPlatformHelperImpl(this);
        if (this.game == null) {
            String storageFolder = getFilesDir().getAbsolutePath();
            BluetoothAdapter.getDefaultAdapter();
            Analytics analytics = new Analytics();
            this.game = new MonsterGalaxy(storageFolder, platformHelper, new GaiaClient(storageFolder, this), null, null, analytics);
            analytics.setActivity(this);
        } else {
            this.game.setPlatformHelper(new AndroidPlatformHelperImpl(this));
            ((Analytics) Game.getAnalytics()).setActivity(this);
        }
        initialize((ApplicationListener) this.game, false);
        this.notificationManager = new MogaNotificationManager(getApplicationContext());
        this.notificationManager.loadAlarms(Game.getStorage());
        MonsterGalaxyApp.BILLING_SERVICE = new BillingService();
        MonsterGalaxyApp.BILLING_SERVICE.setContext(this);
        platformHelper.getDealRewards();
    }

    @Override // android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onLowMemory() {
        super.onLowMemory();
        Gdx.app.log("Warning", "LOW MEMORY");
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent intent) {
        String screenCode = intent.getStringExtra("screen");
        if (screenCode != null) {
            MonsterGalaxy.showScreenWhenPossible(ScreenCode.valueOf(screenCode));
        }
        String notification = intent.getStringExtra("notificationType");
        if (notification != null) {
            if (notification.equals(NotificationType.DAILY_PRIZE.toString())) {
                Game.getAnalytics().trackEvent("retention", "alert_activity", "prize", "tap", "", new StringBuilder(String.valueOf(Game.getTrainer().getCurrentIsland().getId())).toString());
            }
            if (notification.equals(NotificationType.TAMER_ENERGY_FULL.toString())) {
                Game.getAnalytics().trackEvent("retention", "alert_activity", "tamer", "tap", "", new StringBuilder(String.valueOf(Game.getTrainer().getCurrentIsland().getId())).toString());
            }
            if (notification.equals(NotificationType.MOGAS_RESTED.toString())) {
                Game.getAnalytics().trackEvent("retention", "alert_activity", "moga", "tap", "", new StringBuilder(String.valueOf(Game.getTrainer().getCurrentIsland().getId())).toString());
            }
            if (notification.equals(NotificationType.ISLAND.toString())) {
                Game.getAnalytics().trackEvent("retention", "alert_activity", "island", "tap", "", new StringBuilder(String.valueOf(Game.getTrainer().getCurrentIsland().getId())).toString());
            }
        }
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        if (Game.getTrainer() != null && Game.getTrainer().getName() != null && Game.getTrainer().getName().toLowerCase().equals("neckmode")) {
            if (!TweaksScreen.isShowing() && MonsterGalaxy.canShowTweaksMenu()) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu, menu);
                return true;
            }
            return false;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        Game.menuKeyPressed();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Complete islands upto:");
                builder.setItems(AutoPlayerScreen.getIslandNames(), new DialogInterface.OnClickListener() { // from class: com.gaiaonline.monstergalaxy.app.MonsterGalaxyAndroid.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, final int which) {
                        Game.runOnGameThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.app.MonsterGalaxyAndroid.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                TweaksScreen.autoPlay(which);
                            }
                        });
                    }
                });
                return builder.create();
            case 2:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("Pick a snapshot:");
                final String[] snapshots = TweaksScreen.getSnapshots();
                if (snapshots == null) {
                    Toast.makeText(this, "Can't read snapshots", 0).show();
                } else {
                    builder2.setItems(snapshots, new DialogInterface.OnClickListener() { // from class: com.gaiaonline.monstergalaxy.app.MonsterGalaxyAndroid.2
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            TweaksScreen.snapshotPicked(snapshots[which]);
                        }
                    });
                    return builder2.create();
                }
                break;
        }
        return super.onCreateDialog(id);
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.tweaks) {
            return super.onOptionsItemSelected(item);
        }
        TweaksScreen.show(this);
        return true;
    }

    @Override // com.badlogic.gdx.backends.android.AndroidApplication, android.app.Activity
    protected void onPause() {
        super.onPause();
        this.notificationManager.installAlarms();
        TweaksScreen.hide();
        MonsterGalaxy.getAnalytics().sessionMPauseActivity();
    }

    @Override // com.badlogic.gdx.backends.android.AndroidApplication, android.app.Activity
    protected void onResume() {
        super.onResume();
        Game.getAndroidPlatformHelper().showNativeProgressDialog();
        ((AndroidAdsService) Game.getPlatformHelper().getAdsService()).initialize(this);
        this.notificationManager.uninstallAlarms();
        MonsterGalaxy.getAnalytics().sessionMResumeActivity();
    }

    @Override // android.app.Activity
    public void onStart() {
        super.onStart();
        ((Analytics) Game.getAnalytics()).startSession(this);
        BillingResponseHandler.registerActivity(this);
        MonsterGalaxy.getAnalytics().sessionMStartActivity();
    }

    @Override // android.app.Activity
    public void onStop() {
        ((Analytics) Game.getAnalytics()).endSession(this);
        BillingResponseHandler.unregisterActivity();
        MonsterGalaxy.getAnalytics().sessionMStopActivity();
        super.onStop();
    }

    @Override // com.badlogic.gdx.backends.android.AndroidApplication, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        MonsterGalaxyApp.BILLING_SERVICE.unbind();
    }

    public void nativeOnBackPressed() {
        runOnUiThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.app.MonsterGalaxyAndroid.3
            @Override // java.lang.Runnable
            public void run() {
                MonsterGalaxyAndroid.super.onBackPressed();
            }
        });
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        Game.backKeyPressed();
    }

    public View getView() {
        return this.graphics.getView();
    }
}
