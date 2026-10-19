package com.millennialmedia.android;

import android.content.Context;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class MMJSObject {
    protected WeakReference<Context> contextRef;

    MMJSObject() {
    }

    void setContext(Context context) {
        this.contextRef = new WeakReference<>(context);
    }
}
