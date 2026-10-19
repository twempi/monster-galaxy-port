package com.fiksu.asotracking;

import android.content.Context;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class RatingEventTracker extends EventTracker {
    RatingEventTracker(Context context, String outcome, int launches) {
        super(context, "Rating");
        addParameter("tvalue", outcome);
        addParameter("ivalue", new Integer(launches).toString());
    }

    @Override // com.fiksu.asotracking.EventTracker
    public void uploadEvent() {
        super.uploadEvent();
    }
}
