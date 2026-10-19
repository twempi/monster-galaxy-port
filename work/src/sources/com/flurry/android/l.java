package com.flurry.android;

/* JADX INFO: loaded from: classes.dex */
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
