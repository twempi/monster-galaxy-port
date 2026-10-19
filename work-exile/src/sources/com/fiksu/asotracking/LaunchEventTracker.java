package com.fiksu.asotracking;

import android.app.Application;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class LaunchEventTracker extends EventTracker {
    LaunchEventTracker(Application application) {
        this(application, false);
    }

    LaunchEventTracker(Application application, boolean notification) {
        super(application, notification ? "NotificationLaunch" : "Launch");
    }
}
