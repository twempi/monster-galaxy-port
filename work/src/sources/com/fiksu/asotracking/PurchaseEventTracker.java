package com.fiksu.asotracking;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
class PurchaseEventTracker extends EventTracker {
    PurchaseEventTracker(Context context, String username, Double price, String currency) {
        super(context, "Purchase");
        addParameter("username", username);
        addParameter("fvalue", price.toString());
        addParameter("tvalue", currency);
    }
}
