package com.millennialmedia.android;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import java.io.File;

/* JADX INFO: loaded from: classes.dex */
class MMBaseActivity {
    MMActivity activity;

    MMBaseActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        this.activity.onCreateSuper(savedInstanceState);
    }

    protected void onDestroy() {
        this.activity.onDestroySuper();
    }

    protected void onStart() {
        this.activity.onStartSuper();
    }

    protected void onStop() {
        this.activity.onStopSuper();
    }

    protected void onRestart() {
        this.activity.onRestartSuper();
    }

    protected void onResume() {
        this.activity.onResumeSuper();
    }

    protected void onPause() {
        this.activity.onPauseSuper();
    }

    protected void onSaveInstanceState(Bundle outState) {
        this.activity.onSaveInstanceStateSuper(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        this.activity.onRestoreInstanceStateSuper(savedInstanceState);
    }

    public Object onRetainNonConfigurationInstance() {
        return this.activity.onRetainNonConfigurationInstanceSuper();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.activity.onActivityResultSuper(requestCode, resultCode, data);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return this.activity.onKeyDownSuper(keyCode, event);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        return this.activity.dispatchTouchEventSuper(ev);
    }

    public Intent getIntent() {
        return this.activity.getIntent();
    }

    public Object getLastNonConfigurationInstance() {
        return this.activity.getLastNonConfigurationInstance();
    }

    public Object getSystemService(String name) {
        return this.activity.getSystemService(name);
    }

    public Window getWindow() {
        return this.activity.getWindow();
    }

    public ContentResolver getContentResolver() {
        return this.activity.getContentResolver();
    }

    public void setContentView(View view) {
        this.activity.setContentView(view);
    }

    public void setRequestedOrientation(int requestedOrientation) {
        this.activity.setRequestedOrientation(requestedOrientation);
    }

    public final void setResult(int resultCode) {
        this.activity.setResult(resultCode);
    }

    public void setTheme(int resid) {
        this.activity.setTheme(resid);
    }

    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return this.activity.registerReceiver(receiver, filter);
    }

    public void unregisterReceiver(BroadcastReceiver receiver) {
        this.activity.unregisterReceiver(receiver);
    }

    public File getCacheDir() {
        return this.activity.getCacheDir();
    }

    public void startActivity(Intent intent) {
        this.activity.startActivity(intent);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        this.activity.startActivityForResult(intent, requestCode);
    }

    public void finish() {
        this.activity.finish();
    }

    public final void runOnUiThread(Runnable action) {
        this.activity.runOnUiThread(action);
    }

    public final boolean requestWindowFeature(int featureId) {
        return this.activity.requestWindowFeature(featureId);
    }
}
