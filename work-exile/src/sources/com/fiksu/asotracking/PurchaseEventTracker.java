package com.fiksu.asotracking;

import android.content.Context;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class PurchaseEventTracker extends EventTracker {
    PurchaseEventTracker(Context context, String username, Double price, String currency) {
        super(context, "Purchase");
        addParameter("username", username);
        addParameter("fvalue", price.toString());
        addParameter("tvalue", currency);
    }
}
