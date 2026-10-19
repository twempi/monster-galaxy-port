package com.fiksu.asotracking;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
class ConversionEventTracker extends EventTracker {
    ConversionEventTracker(Context context, String referrer) {
        super(context, "Conversion");
        addParameter("tvalue", referrer);
    }
}
