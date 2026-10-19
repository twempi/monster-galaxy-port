package com.flurry.android;

import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class j implements Runnable {
    private /* synthetic */ List a;

    j(List list) {
        this.a = list;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Iterator it = this.a.iterator();
        while (it.hasNext()) {
            ((o) it.next()).a();
        }
    }
}
