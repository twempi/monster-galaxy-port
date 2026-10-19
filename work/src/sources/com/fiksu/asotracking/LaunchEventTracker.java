package com.fiksu.asotracking;

import android.app.Application;

/* JADX INFO: loaded from: classes.dex */
class LaunchEventTracker extends EventTracker {
    LaunchEventTracker(Application application) {
        this(application, false);
    }

    LaunchEventTracker(Application application, boolean notification) {
        super(application, notification ? "NotificationLaunch" : "Launch");
    }
}
