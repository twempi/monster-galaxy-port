package com.flurry.android;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class ae implements Runnable {
    private /* synthetic */ String a;
    private /* synthetic */ u b;

    ae(u uVar, String str) {
        this.b = uVar;
        this.a = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        CallbackEvent callbackEvent = new CallbackEvent(101);
        callbackEvent.setMessage(this.a);
        if (this.b.z != null) {
            this.b.z.onMarketAppLaunchError(callbackEvent);
        }
    }
}
