package com.flurry.android;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class u implements View.OnClickListener {
    private static volatile long A;
    static String b;
    private static int g;
    private String h;
    private String i;
    private String j;
    private long k;
    private long l;
    private long m;
    private long n;
    private volatile boolean q;
    private String r;
    private Handler t;
    private boolean u;
    private ag w;
    private AppCircleCallback z;
    private static volatile String c = "market://";
    private static volatile String d = "market://details?id=";
    private static volatile String e = "https://market.android.com/details?id=";
    private static String f = "com.flurry.android.ACTION_CATALOG";
    static String a = "FlurryAgent";
    private boolean p = true;
    private Map s = new HashMap();
    private transient Map v = new HashMap();
    private List x = new ArrayList();
    private Map y = new HashMap();
    private z o = new z();

    static /* synthetic */ void a(u uVar, Context context, String str) {
        if (!str.startsWith(d)) {
            ah.d(a, "Unexpected android market url scheme: " + str);
            return;
        }
        String strSubstring = str.substring(d.length());
        if (!uVar.p) {
            ah.a(a, "Launching Android Market website for app " + strSubstring);
            context.startActivity(new Intent("android.intent.action.VIEW").setData(Uri.parse(e + strSubstring)));
        } else {
            try {
                ah.a(a, "Launching Android Market for app " + strSubstring);
                context.startActivity(new Intent("android.intent.action.VIEW").setData(Uri.parse(str)));
            } catch (Exception e2) {
                ah.c(a, "Cannot launch Marketplace url " + str, e2);
            }
        }
    }

    static {
        new Random(System.currentTimeMillis());
        g = 5000;
        b = "";
        A = 0L;
    }

    final synchronized void a(Context context, a aVar) {
        synchronized (this) {
            if (!this.q) {
                this.h = aVar.c;
                this.i = aVar.d;
                this.j = aVar.a;
                this.k = aVar.b;
                this.t = aVar.e;
                this.w = new ag(this.t, g);
                context.getResources().getDisplayMetrics();
                this.y.clear();
                this.v.clear();
                this.o.a(context, this, aVar);
                this.s.clear();
                PackageManager packageManager = context.getPackageManager();
                String str = d + context.getPackageName();
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(str));
                this.p = packageManager.queryIntentActivities(intent, 65536).size() > 0;
                this.q = true;
            }
        }
    }

    final synchronized void a(long j, long j2) {
        this.l = j;
        this.m = j2;
        this.n = 0L;
        this.x.clear();
    }

    final boolean a() {
        return this.q;
    }

    final void a(String str) {
        this.r = str;
    }

    final synchronized void b() {
        if (p()) {
            this.o.d();
        }
    }

    final synchronized void c() {
        if (p()) {
            this.o.e();
        }
    }

    final synchronized void a(Map map, Map map2, Map map3, Map map4, Map map5, Map map6) {
        if (p()) {
            this.o.a(map, map2, map3, map4, map5, map6);
            Log.i("FlurryAgent", this.o.toString());
        }
    }

    final synchronized long d() {
        return !p() ? 0L : this.o.c();
    }

    final synchronized Set e() {
        return !p() ? Collections.emptySet() : this.o.a();
    }

    final synchronized AdImage a(long j) {
        return !p() ? null : this.o.b(j);
    }

    private synchronized AdImage n() {
        return !p() ? null : this.o.a((short) 1);
    }

    final synchronized List f() {
        return this.x;
    }

    final synchronized p b(long j) {
        return (p) this.v.get(Long.valueOf(j));
    }

    final synchronized void g() {
        this.v.clear();
    }

    final synchronized void a(Context context, String str) {
        try {
            if (p()) {
                try {
                    List listA = a(Arrays.asList(str), (Long) null);
                    if (listA != null && !listA.isEmpty()) {
                        p pVar = new p(str, (byte) 2, j());
                        pVar.b = (v) listA.get(0);
                        c(pVar);
                        b(context, pVar, this.h + a(pVar));
                    } else {
                        Intent intent = new Intent(o());
                        intent.addCategory("android.intent.category.DEFAULT");
                        context.startActivity(intent);
                    }
                } catch (Exception e2) {
                    ah.d(a, "Failed to launch promotional canvas for hook: " + str, e2);
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    final void a(AppCircleCallback appCircleCallback) {
        this.z = appCircleCallback;
    }

    final void a(boolean z) {
        this.u = z;
    }

    final boolean h() {
        return this.u;
    }

    final String i() {
        return this.h;
    }

    final synchronized void a(Context context, p pVar, String str) {
        if (p()) {
            this.t.post(new ak(this, str, context, pVar));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String d(String str) {
        try {
            if (!str.startsWith(c)) {
                HttpResponse httpResponseExecute = new DefaultHttpClient().execute(new HttpGet(str));
                int statusCode = httpResponseExecute.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    str = EntityUtils.toString(httpResponseExecute.getEntity());
                    if (!str.startsWith(c)) {
                        str = d(str);
                    }
                } else {
                    ah.c(a, "Cannot process with responseCode " + statusCode);
                    e("Error when fetching application's android market ID, responseCode " + statusCode);
                }
            }
            return str;
        } catch (UnknownHostException e2) {
            ah.c(a, "Unknown host: " + e2.getMessage());
            if (this.z != null) {
                e("Unknown host: " + e2.getMessage());
            }
            return null;
        } catch (Exception e3) {
            ah.c(a, "Failed on url: " + str, e3);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e(String str) {
        a(new ae(this, str));
    }

    final synchronized Offer b(String str) {
        List listA;
        Offer offerA = null;
        synchronized (this) {
            if (p() && (listA = a(Arrays.asList(str), (Long) null)) != null && !listA.isEmpty()) {
                offerA = a(str, (v) listA.get(0));
                ah.a(a, "Impression for offer with ID " + offerA.getId());
            }
        }
        return offerA;
    }

    final synchronized void a(Context context, long j) {
        if (p()) {
            OfferInSdk offerInSdk = (OfferInSdk) this.y.get(Long.valueOf(j));
            if (offerInSdk == null) {
                ah.b(a, "Cannot find offer " + j);
            } else {
                p pVarB = b(offerInSdk.b);
                offerInSdk.b = pVarB;
                String str = FlurryAgent.c() + a(pVarB);
                ah.a(a, "Offer " + offerInSdk.a + " accepted. Sent with cookies: " + this.s);
                a(context, pVarB, str);
            }
        }
    }

    final synchronized List c(String str) {
        List arrayList;
        if (!p() || !this.o.b()) {
            arrayList = Collections.emptyList();
        } else {
            v[] vVarArrA = this.o.a(str);
            arrayList = new ArrayList();
            if (vVarArrA != null && vVarArrA.length > 0) {
                for (v vVar : vVarArrA) {
                    arrayList.add(a(str, vVar));
                }
            }
            ah.a(a, "Impressions for " + arrayList.size() + " offers.");
        }
        return arrayList;
    }

    final synchronized void a(List list) {
        if (p()) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                this.y.remove((Long) it.next());
            }
        }
    }

    private Offer a(String str, v vVar) {
        p pVar = new p(str, (byte) 3, j());
        c(pVar);
        pVar.a(new f((byte) 2, j()));
        pVar.b = vVar;
        al alVarA = this.o.a(vVar.a);
        String str2 = alVarA == null ? "" : alVarA.a;
        int i = alVarA == null ? 0 : alVarA.c;
        long j = A + 1;
        A = j;
        OfferInSdk offerInSdk = new OfferInSdk(j, pVar, vVar.h, vVar.d, str2, i);
        this.y.put(Long.valueOf(offerInSdk.a), offerInSdk);
        return new Offer(offerInSdk.a, offerInSdk.f, offerInSdk.c, offerInSdk.d, offerInSdk.e);
    }

    final synchronized List a(Context context, List list, Long l, int i, boolean z) {
        List listEmptyList;
        if (p() && this.o.b() && list != null) {
            List listA = a(list, l);
            int iMin = Math.min(list.size(), listA.size());
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < iMin; i2++) {
                String str = (String) list.get(i2);
                e eVarB = this.o.b(str);
                if (eVarB != null) {
                    p pVar = new p((String) list.get(i2), (byte) 1, j());
                    c(pVar);
                    if (i2 < listA.size()) {
                        pVar.b = (v) listA.get(i2);
                        pVar.a(new f((byte) 2, j()));
                        arrayList.add(new y(context, this, pVar, eVarB, i, z));
                    }
                } else {
                    ah.d(a, "Cannot find hook: " + str);
                }
            }
            listEmptyList = arrayList;
        } else {
            listEmptyList = Collections.emptyList();
        }
        return listEmptyList;
    }

    final synchronized View a(Context context, String str, int i) {
        o oVar;
        if (!p()) {
            oVar = null;
        } else {
            oVar = new o(this, context, str, i);
            this.w.a(oVar);
        }
        return oVar;
    }

    private void c(p pVar) {
        if (this.x.size() < 32767) {
            this.x.add(pVar);
            this.v.put(Long.valueOf(pVar.a()), pVar);
        }
    }

    private List a(List list, Long l) {
        if (list == null || list.isEmpty() || !this.o.b()) {
            return Collections.emptyList();
        }
        v[] vVarArrA = this.o.a((String) list.get(0));
        if (vVarArrA != null && vVarArrA.length > 0) {
            ArrayList arrayList = new ArrayList(Arrays.asList(vVarArrA));
            Collections.shuffle(arrayList);
            if (l != null) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    if (((v) it.next()).a == l.longValue()) {
                        it.remove();
                        break;
                    }
                }
            }
            return arrayList.subList(0, Math.min(arrayList.size(), list.size()));
        }
        return Collections.emptyList();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized long j() {
        long jElapsedRealtime = SystemClock.elapsedRealtime() - this.m;
        if (jElapsedRealtime <= this.n) {
            jElapsedRealtime = this.n + 1;
            this.n = jElapsedRealtime;
        }
        this.n = jElapsedRealtime;
        return this.n;
    }

    @Override // android.view.View.OnClickListener
    public final synchronized void onClick(View view) {
        y yVar = (y) view;
        p pVarB = b(yVar.a());
        yVar.a(pVarB);
        String strA = a(pVarB);
        if (this.u) {
            b(view.getContext(), pVarB, this.h + strA);
        } else {
            a(view.getContext(), pVarB, this.i + strA);
        }
    }

    final synchronized void a(String str, String str2) {
        this.s.put(str, str2);
    }

    final synchronized void k() {
        this.s.clear();
    }

    private void b(Context context, p pVar, String str) {
        Intent intent = new Intent(o());
        intent.addCategory("android.intent.category.DEFAULT");
        intent.putExtra("u", str);
        if (pVar != null) {
            intent.putExtra("o", pVar.a());
        }
        context.startActivity(intent);
    }

    private static String o() {
        return FlurryAgent.a != null ? FlurryAgent.a : f;
    }

    final synchronized String a(p pVar) {
        StringBuilder sbAppend;
        v vVar = pVar.b;
        sbAppend = new StringBuilder().append("?apik=").append(this.j).append("&cid=").append(vVar.e).append("&adid=").append(vVar.a).append("&pid=").append(this.r).append("&iid=").append(this.k).append("&sid=").append(this.l).append("&its=").append(pVar.a()).append("&hid=").append(r.a(pVar.a)).append("&ac=").append(a(vVar.g));
        if (this.s != null && !this.s.isEmpty()) {
            for (Map.Entry entry : this.s.entrySet()) {
                sbAppend.append("&").append("c_" + r.a((String) entry.getKey())).append("=").append(r.a((String) entry.getValue()));
            }
        }
        sbAppend.append("&ats=").append(System.currentTimeMillis());
        return sbAppend.toString();
    }

    private static String a(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bArr.length; i++) {
            int i2 = (bArr[i] >> 4) & 15;
            if (i2 < 10) {
                sb.append((char) (i2 + 48));
            } else {
                sb.append((char) ((i2 + 65) - 10));
            }
            int i3 = bArr[i] & 15;
            if (i3 < 10) {
                sb.append((char) (i3 + 48));
            } else {
                sb.append((char) ((i3 + 65) - 10));
            }
        }
        return sb.toString();
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[adLogs=").append(this.x).append("]");
        return sb.toString();
    }

    final synchronized AdImage l() {
        return !p() ? null : n();
    }

    final synchronized p b(p pVar) {
        if (!this.x.contains(pVar)) {
            p pVar2 = new p(pVar, j());
            this.x.add(pVar2);
            pVar = pVar2;
        }
        pVar.a(new f((byte) 4, j()));
        return pVar;
    }

    private static void a(Runnable runnable) {
        new Handler().post(runnable);
    }

    final synchronized void a(int i) {
        if (this.z != null) {
            a(new ad(this, i));
        }
    }

    final synchronized boolean m() {
        return !p() ? false : this.o.b();
    }

    private boolean p() {
        if (!this.q) {
            ah.d(a, "AppCircle is not initialized");
        }
        if (this.r == null) {
            ah.d(a, "Cannot identify UDID.");
        }
        return this.q;
    }
}
