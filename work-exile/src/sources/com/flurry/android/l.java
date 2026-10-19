package com.flurry.android;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class l implements Runnable {
    private /* synthetic */ b a;

    l(b bVar) {
        this.a = bVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        FlurryAgent.b(this.a.b, this.a.a);
    }
}
