package com.millennialmedia.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class MMActivity extends Activity {
    private MMBaseActivity activity;

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        String className = null;
        try {
            className = getIntent().getStringExtra("class");
            this.activity = (MMBaseActivity) Class.forName(className).newInstance();
            this.activity.activity = this;
            this.activity.onCreate(savedInstanceState);
        } catch (Exception e) {
            MMAdViewSDK.Log.e("Could not start activity for %s. %s", className, e.getMessage());
            e.printStackTrace();
            super.onCreate(savedInstanceState);
            finish();
        }
    }

    void onCreateSuper(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        if (this.activity != null) {
            this.activity.onDestroy();
        } else {
            super.onDestroy();
        }
    }

    void onDestroySuper() {
        super.onDestroy();
    }

    @Override // android.app.Activity
    protected void onStart() {
        if (this.activity != null) {
            this.activity.onStart();
        } else {
            super.onStart();
        }
    }

    void onStartSuper() {
        super.onStart();
    }

    @Override // android.app.Activity
    protected void onStop() {
        if (this.activity != null) {
            this.activity.onStop();
        } else {
            super.onStop();
        }
    }

    void onStopSuper() {
        super.onStop();
    }

    @Override // android.app.Activity
    protected void onRestart() {
        if (this.activity != null) {
            this.activity.onRestart();
        } else {
            super.onRestart();
        }
    }

    void onRestartSuper() {
        super.onRestart();
    }

    @Override // android.app.Activity
    protected void onResume() {
        if (this.activity != null) {
            this.activity.onResume();
        } else {
            super.onResume();
        }
    }

    void onResumeSuper() {
        super.onResume();
    }

    @Override // android.app.Activity
    protected void onPause() {
        if (this.activity != null) {
            this.activity.onPause();
        } else {
            super.onPause();
        }
    }

    void onPauseSuper() {
        super.onPause();
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        if (this.activity != null) {
            this.activity.onSaveInstanceState(outState);
        } else {
            super.onSaveInstanceState(outState);
        }
    }

    void onSaveInstanceStateSuper(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override // android.app.Activity
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (this.activity != null) {
            this.activity.onRestoreInstanceState(savedInstanceState);
        } else {
            super.onRestoreInstanceState(savedInstanceState);
        }
    }

    void onRestoreInstanceStateSuper(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override // android.app.Activity
    public Object onRetainNonConfigurationInstance() {
        return this.activity != null ? this.activity.onRetainNonConfigurationInstance() : super.onRetainNonConfigurationInstance();
    }

    Object onRetainNonConfigurationInstanceSuper() {
        return super.onRetainNonConfigurationInstance();
    }

    @Override // android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.activity != null) {
            this.activity.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    void onActivityResultSuper(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return this.activity != null ? this.activity.onKeyDown(keyCode, event) : super.onKeyDown(keyCode, event);
    }

    boolean onKeyDownSuper(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return this.activity != null ? this.activity.dispatchTouchEvent(ev) : super.dispatchTouchEvent(ev);
    }

    boolean dispatchTouchEventSuper(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
