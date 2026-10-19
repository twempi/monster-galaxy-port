package com.fiksu.asotracking;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
class ResumeEventTracker extends EventTracker {
    ResumeEventTracker(Context context) {
        this(context, false);
    }

    ResumeEventTracker(Context context, boolean notification) {
        super(context, notification ? "NotificationResume" : "Resume");
    }
}
