package com.millennialmedia.android;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import java.io.File;

/* JADX INFO: loaded from: classes.dex */
public class AdViewOverlayActivity extends MMBaseActivity {
    private AdViewOverlayView mmOverlay;
    private SensorManager sensorManager;
    private OverlaySettings settings;
    private final float threshold = 0.2f;
    private final int interval = 1000;
    private SensorEventListener accelerometerEventListener = new SensorEventListener() { // from class: com.millennialmedia.android.AdViewOverlayActivity.1
        private float x = 0.0f;
        private float y = 0.0f;
        private float z = 0.0f;
        private float lastX = 0.0f;
        private float lastY = 0.0f;
        private float lastZ = 0.0f;
        private float force = 0.0f;
        private long currentTime = 0;
        private long prevTime = 0;
        private long timeDifference = 0;
        private long prevShakeTime = 0;

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent event) {
            this.currentTime = event.timestamp;
            this.x = event.values[0];
            this.y = event.values[1];
            this.z = event.values[2];
            this.timeDifference = this.currentTime - this.prevTime;
            if (this.timeDifference > 500) {
                AdViewOverlayActivity.this.didAccelerate(this.x, this.y, this.z);
                this.force = Math.abs(((((this.x + this.y) + this.z) - this.lastX) - this.lastY) - this.lastZ) / this.timeDifference;
                AdViewOverlayActivity.this.didAccelerate(this.x, this.y, this.z);
                if (this.force > 0.2f) {
                    if (this.currentTime - this.prevShakeTime >= 1000) {
                        AdViewOverlayActivity.this.didShake(this.force);
                    }
                    this.prevShakeTime = this.currentTime;
                }
                this.lastX = this.x;
                this.lastY = this.y;
                this.lastZ = this.z;
                this.prevTime = this.currentTime;
            }
        }

        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override // com.millennialmedia.android.MMBaseActivity
    public /* bridge */ /* synthetic */ boolean dispatchTouchEvent(MotionEvent x0) {
        return super.dispatchTouchEvent(x0);
    }

    @Override // com.millennialmedia.android.MMBaseActivity
    public /* bridge */ /* synthetic */ File getCacheDir() {
        return super.getCacheDir();
    }

    @Override // com.millennialmedia.android.MMBaseActivity
    public /* bridge */ /* synthetic */ ContentResolver getContentResolver() {
        return super.getContentResolver();
    }

    @Override // com.millennialmedia.android.MMBaseActivity
    public /* bridge */ /* synthetic */ Intent getIntent() {
        return super.getIntent();
    }

    @Override // com.millennialmedia.android.MMBaseActivity
    public /* bridge */ /* synthetic */ Object getLastNonConfigurationInstance() {
        return super.getLastNonConfigurationInstance();
    }

    @Override // com.millennialmedia.android.MMBaseActivity
    public /* bridge */ /* synthetic */ Object getSystemService(String x0) {
        return super.getSystemService(x0);
    }

    @Override // com.millennialmedia.android.MMBaseActivity
    public /* bridge */ /* synthetic */ Window getWindow() {
        return super.getWindow();
    }

    @Override // com.millennialmedia.android.MMBaseActivity
    public /* bridge */ /* synthetic */ Intent registerReceiver(BroadcastReceiver x0, IntentFilter x1) {
        return super.registerReceiver(x0, x1);
    }

    @Override // com.millennialmedia.android.MMBaseActivity
    public /* bridge */ /* synthetic */ void setContentView(View x0) {
        super.setContentView(x0);
    }

    @Override // com.millennialmedia.android.MMBaseActivity
    public /* bridge */ /* synthetic */ void setRequestedOrientation(int x0) {
        super.setRequestedOrientation(x0);
    }

    @Override // com.millennialmedia.android.MMBaseActivity
    public /* bridge */ /* synthetic */ void setTheme(int x0) {
        super.setTheme(x0);
    }

    @Override // com.millennialmedia.android.MMBaseActivity
    public /* bridge */ /* synthetic */ void startActivity(Intent x0) {
        super.startActivity(x0);
    }

    @Override // com.millennialmedia.android.MMBaseActivity
    public /* bridge */ /* synthetic */ void startActivityForResult(Intent x0, int x1) {
        super.startActivityForResult(x0, x1);
    }

    @Override // com.millennialmedia.android.MMBaseActivity
    public /* bridge */ /* synthetic */ void unregisterReceiver(BroadcastReceiver x0) {
        super.unregisterReceiver(x0);
    }

    @Override // com.millennialmedia.android.MMBaseActivity
    protected void onCreate(Bundle savedInstanceState) {
        Uri uri;
        setTheme(android.R.style.Theme.Translucent.NoTitleBar);
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().clearFlags(1024);
        getWindow().addFlags(2048);
        getWindow().addFlags(16777216);
        Intent intent = getIntent();
        this.settings = (OverlaySettings) intent.getParcelableExtra("settings");
        if (this.settings == null) {
            this.settings = new OverlaySettings();
        }
        this.settings.log();
        if (this.settings.orientation != null && this.settings.orientation.equalsIgnoreCase("landscape")) {
            setRequestedOrientation(0);
        } else if (this.settings.orientation != null && this.settings.orientation.equalsIgnoreCase("portrait")) {
            setRequestedOrientation(1);
        } else {
            setRequestedOrientation(2);
        }
        Intent i = getIntent();
        if (i != null && (uri = i.getData()) != null) {
            MMAdViewSDK.Log.v("Path: %s", uri.getLastPathSegment());
        }
        this.mmOverlay = new AdViewOverlayView(this.activity, this.settings);
        setContentView(this.mmOverlay);
        if (getLastNonConfigurationInstance() == null) {
            this.mmOverlay.loadWebContent(getIntent().getDataString());
        }
        this.settings.state = "expanded";
    }

    @Override // com.millennialmedia.android.MMBaseActivity
    public void finish() {
        MMAdViewSDK.Event.overlayClosed(this.activity);
        super.finish();
    }

    @Override // com.millennialmedia.android.MMBaseActivity
    protected void onResume() {
        MMAdViewSDK.Log.d("Overlay onResume");
        if (this.settings.canAccelerate) {
            startAccelerating();
        }
        this.settings.state = "expanded";
        super.onResume();
    }

    @Override // com.millennialmedia.android.MMBaseActivity
    protected void onDestroy() {
        super.onDestroy();
        MMAdViewSDK.Log.d("Overlay onDestroy");
    }

    @Override // com.millennialmedia.android.MMBaseActivity
    protected void onStop() {
        super.onStop();
    }

    @Override // com.millennialmedia.android.MMBaseActivity
    protected void onPause() {
        MMAdViewSDK.Log.d("Overlay onPause");
        if (this.settings.canAccelerate) {
            stopAccelerating();
        }
        MMMedia.Audio.sharedAudio(this.activity).stop();
        setResult(0);
        this.settings.state = "hidden";
        super.onPause();
    }

    @Override // com.millennialmedia.android.MMBaseActivity
    public Object onRetainNonConfigurationInstance() {
        return this.mmOverlay.getNonConfigurationInstance();
    }

    @Override // com.millennialmedia.android.MMBaseActivity
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || event.getRepeatCount() != 0) {
            return super.onKeyDown(keyCode, event);
        }
        if (this.mmOverlay.goBack()) {
            return true;
        }
        this.mmOverlay.dismiss(true);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void didAccelerate(float x, float y, float z) {
        MMAdViewSDK.Log.v("Accelerometer x:%fy:%fz:%f", Float.valueOf(x), Float.valueOf(y), Float.valueOf(z));
        this.mmOverlay.injectJS("javascript:didAccelerate(" + x + "," + y + "," + z + ")");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void didShake(float force) {
        MMAdViewSDK.Log.v("Phone shaken: %f", Float.valueOf(force));
        this.mmOverlay.injectJS("javascript:didShake(" + force + ")");
    }

    private void startAccelerating() {
        this.sensorManager = (SensorManager) getSystemService("sensor");
        Boolean isAccelerometerSupported = Boolean.valueOf(this.sensorManager.registerListener(this.accelerometerEventListener, this.sensorManager.getDefaultSensor(1), 1));
        if (!isAccelerometerSupported.booleanValue()) {
            MMAdViewSDK.Log.w("Accelerometer not supported by this device. Unregistering listener.");
            this.sensorManager.unregisterListener(this.accelerometerEventListener, this.sensorManager.getDefaultSensor(1));
            this.accelerometerEventListener = null;
            this.sensorManager = null;
        }
    }

    private void stopAccelerating() {
        try {
            if (this.sensorManager != null && this.accelerometerEventListener != null) {
                this.sensorManager.unregisterListener(this.accelerometerEventListener, this.sensorManager.getDefaultSensor(1));
            }
        } catch (Exception e) {
        }
    }
}
