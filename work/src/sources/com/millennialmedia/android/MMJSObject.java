package com.millennialmedia.android;

import android.content.Context;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes.dex */
class MMJSObject {
    protected WeakReference<Context> contextRef;

    MMJSObject() {
    }

    void setContext(Context context) {
        this.contextRef = new WeakReference<>(context);
    }
}
