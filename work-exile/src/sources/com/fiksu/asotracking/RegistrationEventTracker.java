package com.fiksu.asotracking;

import android.content.Context;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class RegistrationEventTracker extends EventTracker {
    RegistrationEventTracker(Context context, String username) {
        super(context, "Registration");
        addParameter("username", username);
    }
}
