package com.flurry.android;

import android.content.Context;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class d implements Runnable {
    private /* synthetic */ Context a;
    private /* synthetic */ boolean b;
    private /* synthetic */ FlurryAgent c;

    d(FlurryAgent flurryAgent, Context context, boolean z) {
        this.c = flurryAgent;
        this.a = context;
        this.b = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (!this.c.u) {
            this.c.a(this.a);
        }
        FlurryAgent.a(this.c, this.a, this.b);
    }
}
