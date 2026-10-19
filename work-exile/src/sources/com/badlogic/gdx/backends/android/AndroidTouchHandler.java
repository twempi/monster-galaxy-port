package com.badlogic.gdx.backends.android;

import android.view.MotionEvent;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface AndroidTouchHandler {
    void onTouch(MotionEvent motionEvent, AndroidInput androidInput);

    boolean supportsMultitouch(AndroidApplication androidApplication);
}
