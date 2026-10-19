package com.flurry.android;

/* JADX INFO: loaded from: classes.dex */
final class ad implements Runnable {
    private /* synthetic */ int a;
    private /* synthetic */ u b;

    ad(u uVar, int i) {
        this.b = uVar;
        this.a = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.b.z.onAdsUpdated(new CallbackEvent(this.a));
    }
}
