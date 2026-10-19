package com.flurry.android;

import android.os.Handler;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class ag {
    private Handler b;
    private int d;
    private List a = new ArrayList();
    private Handler c = new Handler();
    private Runnable e = new k(this);

    ag(Handler handler, int i) {
        this.b = handler;
        this.d = i;
        b();
    }

    final synchronized void a(o oVar) {
        oVar.a();
        this.a.add(new WeakReference(oVar));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void a() {
        ArrayList arrayList = new ArrayList();
        Iterator it = this.a.iterator();
        while (it.hasNext()) {
            o oVar = (o) ((WeakReference) it.next()).get();
            if (oVar != null) {
                arrayList.add(oVar);
            }
        }
        this.c.post(new j(arrayList));
        b();
    }

    private synchronized void b() {
        Iterator it = this.a.iterator();
        while (it.hasNext()) {
            if (((WeakReference) it.next()).get() == null) {
                it.remove();
            }
        }
        this.b.removeCallbacks(this.e);
        this.b.postDelayed(this.e, this.d);
    }
}
