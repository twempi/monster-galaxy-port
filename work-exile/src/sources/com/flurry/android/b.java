package com.flurry.android;

import android.content.Context;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class b implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ FlurryAgent b;
    private /* synthetic */ boolean c;

    b(FlurryAgent flurryAgent, boolean z, Context context) {
        this.b = flurryAgent;
        this.c = z;
        this.a = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.b.i();
        this.b.l();
        if (!this.c) {
            this.b.q.postDelayed(new l(this), FlurryAgent.i);
        }
        if (FlurryAgent.o) {
            this.b.Y.c();
        }
    }
}
