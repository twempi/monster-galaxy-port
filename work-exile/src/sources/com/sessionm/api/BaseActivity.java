package com.sessionm.api;

import android.app.Activity;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class BaseActivity extends Activity {
    @Override // android.app.Activity
    protected void onStart() {
        super.onStart();
        SessionM.getInstance().onActivityStart(this);
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        SessionM.getInstance().onActivityResume(this);
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        SessionM.getInstance().onActivityPause(this);
    }

    @Override // android.app.Activity
    protected void onStop() {
        super.onStop();
        SessionM.getInstance().onActivityStop(this);
    }
}
