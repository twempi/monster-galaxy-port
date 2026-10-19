package com.flurry.android;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.utils.compression.lzma.Base;
import com.chartboost.sdk.CBAPIConnection;
import com.tapjoy.TapjoyConstants;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.WeakHashMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

/* JADX INFO: loaded from: classes.dex */
public final class FlurryAgent implements LocationListener {
    static String a;
    private List B;
    private LocationManager C;
    private String D;
    private boolean E;
    private long F;
    private long H;
    private long I;
    private long J;
    private Long P;
    private int Q;
    private Location R;
    private boolean U;
    private int V;
    private int X;
    private final Handler q;
    private File r;
    private long v;
    private String x;
    private String y;
    private String z;
    private static final String[] b = {"9774d56d682e549c", "dead00beef"};
    private static volatile String c = null;
    private static volatile String kInsecureReportUrl = "http://data.flurry.com/aap.do";
    private static volatile String kSecureReportUrl = "https://data.flurry.com/aap.do";
    private static volatile String d = null;
    private static volatile String e = "http://ad.flurry.com/getCanvas.do";
    private static volatile String f = null;
    private static volatile String g = "http://ad.flurry.com/getAndroidApp.do";
    private static final FlurryAgent h = new FlurryAgent();
    private static long i = TapjoyConstants.TIMER_INCREMENT;
    private static boolean j = true;
    private static boolean k = false;
    private static boolean l = false;
    private static boolean m = true;
    private static Criteria n = null;
    private static boolean o = false;
    private static AppCircle p = new AppCircle();
    private File s = null;
    private volatile boolean t = false;
    private volatile boolean u = false;
    private Map w = new WeakHashMap();
    private boolean A = true;
    private List G = new ArrayList();
    private String K = "";
    private String L = "";
    private byte M = -1;
    private String N = "";
    private byte O = -1;
    private Map S = new HashMap();
    private List T = new ArrayList();
    private List W = new ArrayList();
    private u Y = new u();

    static /* synthetic */ void a(FlurryAgent flurryAgent, Context context, boolean z) {
        Location locationD = null;
        if (z) {
            try {
                locationD = flurryAgent.d(context);
            } catch (Throwable th) {
                ah.b("FlurryAgent", "", th);
                return;
            }
        }
        synchronized (flurryAgent) {
            flurryAgent.R = locationD;
        }
        if (o) {
            flurryAgent.Y.b();
        }
        flurryAgent.c(true);
    }

    static /* synthetic */ void b(FlurryAgent flurryAgent, Context context) {
        boolean z = false;
        try {
            synchronized (flurryAgent) {
                long jElapsedRealtime = SystemClock.elapsedRealtime() - flurryAgent.v;
                if (!flurryAgent.t && jElapsedRealtime > i && flurryAgent.G.size() > 0) {
                    z = true;
                }
            }
            if (z) {
                flurryAgent.c(false);
            }
        } catch (Throwable th) {
            ah.b("FlurryAgent", "", th);
        }
    }

    public class FlurryDefaultExceptionHandler implements Thread.UncaughtExceptionHandler {
        private Thread.UncaughtExceptionHandler a = Thread.getDefaultUncaughtExceptionHandler();

        FlurryDefaultExceptionHandler() {
        }

        @Override // java.lang.Thread.UncaughtExceptionHandler
        public void uncaughtException(Thread thread, Throwable th) {
            try {
                FlurryAgent.h.a(th);
            } catch (Throwable th2) {
                ah.b("FlurryAgent", "", th2);
            }
            if (this.a != null) {
                this.a.uncaughtException(thread, th);
            }
        }
    }

    final void a(Throwable th) {
        th.printStackTrace();
        String message = "";
        StackTraceElement[] stackTrace = th.getStackTrace();
        if (stackTrace != null && stackTrace.length > 0) {
            StackTraceElement stackTraceElement = stackTrace[0];
            StringBuilder sb = new StringBuilder();
            sb.append(stackTraceElement.getClassName()).append(".").append(stackTraceElement.getMethodName()).append(":").append(stackTraceElement.getLineNumber());
            if (th.getMessage() != null) {
                sb.append(" (" + th.getMessage() + ")");
            }
            message = sb.toString();
        } else if (th.getMessage() != null) {
            message = th.getMessage();
        }
        onError("uncaught", message, th.getClass().toString());
        this.w.clear();
        a((Context) null, true);
    }

    private FlurryAgent() {
        HandlerThread handlerThread = new HandlerThread("FlurryAgent");
        handlerThread.start();
        this.q = new Handler(handlerThread.getLooper());
    }

    public static void setCatalogIntentName(String str) {
        a = str;
    }

    public static void enableAppCircle() {
        o = true;
    }

    public static AppCircle getAppCircle() {
        return p;
    }

    static View a(Context context, String str, int i2) {
        if (!o) {
            return null;
        }
        try {
            return h.Y.a(context, str, i2);
        } catch (Throwable th) {
            ah.b("FlurryAgent", "", th);
            return null;
        }
    }

    static void a(Context context, String str) {
        if (o) {
            h.Y.a(context, str);
        }
    }

    static Offer a(String str) {
        if (o) {
            return h.Y.b(str);
        }
        return null;
    }

    static List b(String str) {
        if (o) {
            return h.Y.c(str);
        }
        return null;
    }

    static void a(Context context, long j2) {
        if (!o) {
            ah.d("FlurryAgent", "Cannot accept Offer. AppCircle is not enabled");
        }
        h.Y.a(context, j2);
    }

    static void a(List list) {
        if (o) {
            h.Y.a(list);
        }
    }

    static void a(boolean z) {
        if (o) {
            h.Y.a(z);
        }
    }

    static boolean a() {
        return h.Y.h();
    }

    public static void setDefaultNoAdsMessage(String str) {
        if (o) {
            if (str == null) {
                str = "";
            }
            u.b = str;
        }
    }

    static void a(AppCircleCallback appCircleCallback) {
        h.Y.a(appCircleCallback);
    }

    public static void addUserCookie(String str, String str2) {
        if (o) {
            h.Y.a(str, str2);
        }
    }

    public static void clearUserCookies() {
        if (o) {
            h.Y.k();
        }
    }

    public static void setVersionName(String str) {
        synchronized (h) {
            h.z = str;
        }
    }

    public static int getAgentVersion() {
        return Opcodes.LSHL;
    }

    public static void setReportLocation(boolean z) {
        synchronized (h) {
            h.A = z;
        }
    }

    public static void setLogEnabled(boolean z) {
        synchronized (h) {
            try {
                if (z) {
                    ah.b();
                } else {
                    ah.a();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static void setLogLevel(int i2) {
        synchronized (h) {
            ah.a(i2);
        }
    }

    public static void setContinueSessionMillis(long j2) {
        if (j2 < 5000) {
            ah.b("FlurryAgent", "Invalid time set for session resumption: " + j2);
            return;
        }
        synchronized (h) {
            i = j2;
        }
    }

    public static void setLogEvents(boolean z) {
        synchronized (h) {
            j = z;
        }
    }

    public static void setUseHttps(boolean z) {
        k = z;
    }

    public static void setCaptureUncaughtExceptions(boolean z) {
        synchronized (h) {
            if (h.t) {
                ah.b("FlurryAgent", "Cannot setCaptureUncaughtExceptions after onSessionStart");
            } else {
                m = z;
            }
        }
    }

    public static void onStartSession(Context context, String str) {
        if (context == null) {
            throw new NullPointerException("Null context");
        }
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("Api key not specified");
        }
        try {
            h.b(context, str);
        } catch (Throwable th) {
            ah.b("FlurryAgent", "", th);
        }
    }

    public static void onEndSession(Context context) {
        if (context == null) {
            throw new NullPointerException("Null context");
        }
        try {
            h.a(context, false);
        } catch (Throwable th) {
            ah.b("FlurryAgent", "", th);
        }
    }

    public static void logEvent(String str) {
        try {
            h.a(str, (Map) null, false);
        } catch (Throwable th) {
            ah.b("FlurryAgent", "Failed to log event: " + str, th);
        }
    }

    public static void logEvent(String str, Map map) {
        try {
            h.a(str, map, false);
        } catch (Throwable th) {
            ah.b("FlurryAgent", "Failed to log event: " + str, th);
        }
    }

    public static void logEvent(String str, boolean z) {
        try {
            h.a(str, (Map) null, z);
        } catch (Throwable th) {
            ah.b("FlurryAgent", "Failed to log event: " + str, th);
        }
    }

    public static void logEvent(String str, Map map, boolean z) {
        try {
            h.a(str, map, z);
        } catch (Throwable th) {
            ah.b("FlurryAgent", "Failed to log event: " + str, th);
        }
    }

    public static void endTimedEvent(String str) {
        try {
            h.c(str);
        } catch (Throwable th) {
            ah.b("FlurryAgent", "Failed to signify the end of event: " + str, th);
        }
    }

    public static void onError(String str, String str2, String str3) {
        try {
            h.a(str, str2, str3);
        } catch (Throwable th) {
            ah.b("FlurryAgent", "", th);
        }
    }

    public static void onEvent(String str) {
        try {
            h.a(str, (Map) null, false);
        } catch (Throwable th) {
            ah.b("FlurryAgent", "", th);
        }
    }

    public static void onEvent(String str, Map map) {
        try {
            h.a(str, map, false);
        } catch (Throwable th) {
            ah.b("FlurryAgent", "", th);
        }
    }

    public static void onPageView() {
        try {
            h.j();
        } catch (Throwable th) {
            ah.b("FlurryAgent", "", th);
        }
    }

    public static void setReportUrl(String str) {
        c = str;
    }

    public static void setCanvasUrl(String str) {
        d = str;
    }

    public static void setGetAppUrl(String str) {
        f = str;
    }

    public static void setLocationCriteria(Criteria criteria) {
        synchronized (h) {
            n = criteria;
        }
    }

    public static void setAge(int i2) {
        if (i2 > 0 && i2 < 110) {
            Date date = new Date(new Date(System.currentTimeMillis() - (((long) i2) * 31449600000L)).getYear(), 1, 1);
            h.P = Long.valueOf(date.getTime());
        }
    }

    public static void setGender(byte b2) {
        switch (b2) {
            case 0:
            case 1:
                h.O = b2;
                break;
            default:
                h.O = (byte) -1;
                break;
        }
    }

    public static void setUserId(String str) {
        synchronized (h) {
            h.N = r.a(str, 255);
        }
    }

    public static boolean getForbidPlaintextFallback() {
        return false;
    }

    protected static boolean isCaptureUncaughtExceptions() {
        return m;
    }

    static u b() {
        return h.Y;
    }

    private synchronized void b(Context context, String str) {
        if (this.x != null && !this.x.equals(str)) {
            ah.b("FlurryAgent", "onStartSession called with different api keys: " + this.x + " and " + str);
        }
        if (((Context) this.w.put(context, context)) != null) {
            ah.d("FlurryAgent", "onStartSession called with duplicate context, use a specific Activity or Service as context instead of using a global context");
        }
        if (!this.t) {
            ah.a("FlurryAgent", "Initializing Flurry session");
            this.x = str;
            this.s = context.getFileStreamPath(".flurryagent." + Integer.toString(this.x.hashCode(), 16));
            this.r = context.getFileStreamPath(".flurryb.");
            if (m) {
                Thread.setDefaultUncaughtExceptionHandler(new FlurryDefaultExceptionHandler());
            }
            Context applicationContext = context.getApplicationContext();
            if (this.z == null) {
                this.z = c(applicationContext);
            }
            String packageName = applicationContext.getPackageName();
            if (this.y != null && !this.y.equals(packageName)) {
                ah.b("FlurryAgent", "onStartSession called from different application packages: " + this.y + " and " + packageName);
            }
            this.y = packageName;
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            if (jElapsedRealtime - this.v > i) {
                ah.a("FlurryAgent", "New session");
                this.H = System.currentTimeMillis();
                this.I = jElapsedRealtime;
                this.J = -1L;
                this.N = "";
                this.Q = 0;
                this.R = null;
                this.L = TimeZone.getDefault().getID();
                this.K = Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();
                this.S = new HashMap();
                this.T = new ArrayList();
                this.U = true;
                this.W = new ArrayList();
                this.V = 0;
                this.X = 0;
                if (o) {
                    if (!this.Y.a()) {
                        ah.a("FlurryAgent", "Initializing AppCircle");
                        a aVar = new a();
                        aVar.a = this.x;
                        aVar.b = this.F;
                        aVar.c = d != null ? d : e;
                        aVar.d = c();
                        aVar.e = this.q;
                        this.Y.a(context, aVar);
                        ah.a("FlurryAgent", "AppCircle initialized");
                    }
                    this.Y.a(this.H, this.I);
                }
                a(new d(this, applicationContext, this.A));
            } else {
                ah.a("FlurryAgent", "Continuing previous session");
                if (!this.G.isEmpty()) {
                    this.G.remove(this.G.size() - 1);
                }
            }
            this.t = true;
        }
    }

    /* JADX WARN: Code duplicated, block: B:13:0x002c  */
    /* JADX WARN: Code duplicated, block: B:15:0x002f A[Catch: all -> 0x0087, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x000d, B:7:0x0014, B:9:0x0018, B:11:0x0020, B:15:0x002f, B:17:0x003b, B:18:0x005f, B:20:0x006e, B:21:0x0075, B:24:0x0082), top: B:29:0x0003 }] */
    /* JADX WARN: Code duplicated, block: B:17:0x003b A[Catch: all -> 0x0087, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x000d, B:7:0x0014, B:9:0x0018, B:11:0x0020, B:15:0x002f, B:17:0x003b, B:18:0x005f, B:20:0x006e, B:21:0x0075, B:24:0x0082), top: B:29:0x0003 }] */
    /* JADX WARN: Code duplicated, block: B:20:0x006e A[Catch: all -> 0x0087, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x000d, B:7:0x0014, B:9:0x0018, B:11:0x0020, B:15:0x002f, B:17:0x003b, B:18:0x005f, B:20:0x006e, B:21:0x0075, B:24:0x0082), top: B:29:0x0003 }] */
    /* JADX WARN: Code duplicated, block: B:24:0x0082 A[Catch: all -> 0x0087, TRY_ENTER, TRY_LEAVE, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x000d, B:7:0x0014, B:9:0x0018, B:11:0x0020, B:15:0x002f, B:17:0x003b, B:18:0x005f, B:20:0x006e, B:21:0x0075, B:24:0x0082), top: B:29:0x0003 }] */
    private synchronized void a(Context context, boolean z) {
        Context applicationContext;
        String packageName;
        if (context != null) {
            if (((Context) this.w.remove(context)) == null) {
                ah.d("FlurryAgent", "onEndSession called without context from corresponding onStartSession");
            }
            if (this.t && this.w.isEmpty()) {
                ah.a("FlurryAgent", "Ending session");
                m();
                if (context == null) {
                    applicationContext = null;
                } else {
                    applicationContext = context.getApplicationContext();
                }
                if (context != null) {
                    packageName = applicationContext.getPackageName();
                    if (!this.y.equals(packageName)) {
                        ah.b("FlurryAgent", "onEndSession called from different application package, expected: " + this.y + " actual: " + packageName);
                    }
                }
                long jElapsedRealtime = SystemClock.elapsedRealtime();
                this.v = jElapsedRealtime;
                this.J = jElapsedRealtime - this.I;
                if (this.D == null) {
                    ah.b("FlurryAgent", "Not creating report because of bad Android ID or generated ID is null");
                }
                a(new b(this, z, applicationContext));
                this.t = false;
            }
        } else if (this.t) {
            ah.a("FlurryAgent", "Ending session");
            m();
            if (context == null) {
                applicationContext = null;
            } else {
                applicationContext = context.getApplicationContext();
            }
            if (context != null) {
                packageName = applicationContext.getPackageName();
                if (!this.y.equals(packageName)) {
                    ah.b("FlurryAgent", "onEndSession called from different application package, expected: " + this.y + " actual: " + packageName);
                }
            }
            long jElapsedRealtime2 = SystemClock.elapsedRealtime();
            this.v = jElapsedRealtime2;
            this.J = jElapsedRealtime2 - this.I;
            if (this.D == null) {
                ah.b("FlurryAgent", "Not creating report because of bad Android ID or generated ID is null");
            }
            a(new b(this, z, applicationContext));
            this.t = false;
        }
        throw th;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void i() {
        DataOutputStream dataOutputStream;
        DataOutputStream dataOutputStream2 = null;
        try {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                try {
                    dataOutputStream.writeShort(1);
                    dataOutputStream.writeUTF(this.z);
                    dataOutputStream.writeLong(this.H);
                    dataOutputStream.writeLong(this.J);
                    dataOutputStream.writeLong(0L);
                    dataOutputStream.writeUTF(this.K);
                    dataOutputStream.writeUTF(this.L);
                    dataOutputStream.writeByte(this.M);
                    dataOutputStream.writeUTF(this.N == null ? "" : this.N);
                    if (this.R == null) {
                        dataOutputStream.writeBoolean(false);
                    } else {
                        dataOutputStream.writeBoolean(true);
                        dataOutputStream.writeDouble(a(this.R.getLatitude()));
                        dataOutputStream.writeDouble(a(this.R.getLongitude()));
                        dataOutputStream.writeFloat(this.R.getAccuracy());
                    }
                    dataOutputStream.writeInt(this.X);
                    dataOutputStream.writeByte(-1);
                    dataOutputStream.writeByte(-1);
                    dataOutputStream.writeByte(this.O);
                    if (this.P == null) {
                        dataOutputStream.writeBoolean(false);
                    } else {
                        dataOutputStream.writeBoolean(true);
                        dataOutputStream.writeLong(this.P.longValue());
                    }
                    dataOutputStream.writeShort(this.S.size());
                    for (Map.Entry entry : this.S.entrySet()) {
                        dataOutputStream.writeUTF((String) entry.getKey());
                        dataOutputStream.writeInt(((g) entry.getValue()).a);
                    }
                    dataOutputStream.writeShort(this.T.size());
                    Iterator it = this.T.iterator();
                    while (it.hasNext()) {
                        dataOutputStream.write(((i) it.next()).a());
                    }
                    dataOutputStream.writeBoolean(this.U);
                    dataOutputStream.writeInt(this.Q);
                    dataOutputStream.writeShort(this.W.size());
                    for (aa aaVar : this.W) {
                        dataOutputStream.writeLong(aaVar.a);
                        dataOutputStream.writeUTF(aaVar.b);
                        dataOutputStream.writeUTF(aaVar.c);
                        dataOutputStream.writeUTF(aaVar.d);
                    }
                    if (o) {
                        List listF = this.Y.f();
                        dataOutputStream.writeShort(listF.size());
                        Iterator it2 = listF.iterator();
                        while (it2.hasNext()) {
                            ((p) it2.next()).a(dataOutputStream);
                        }
                    } else {
                        dataOutputStream.writeShort(0);
                    }
                    this.G.add(byteArrayOutputStream.toByteArray());
                    r.a(dataOutputStream);
                } catch (IOException e2) {
                    e = e2;
                    dataOutputStream2 = dataOutputStream;
                    try {
                        ah.b("FlurryAgent", "", e);
                        r.a(dataOutputStream2);
                    } catch (Throwable th) {
                        th = th;
                        dataOutputStream = dataOutputStream2;
                        r.a(dataOutputStream);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    r.a(dataOutputStream);
                    throw th;
                }
            } catch (Throwable th3) {
                throw th3;
            }
        } catch (IOException e3) {
            e = e3;
        } catch (Throwable th4) {
            th = th4;
            dataOutputStream = null;
        }
    }

    private static double a(double d2) {
        return Math.round(d2 * 1000.0d) / 1000.0d;
    }

    private void a(Runnable runnable) {
        this.q.post(runnable);
    }

    private synchronized void j() {
        this.X++;
    }

    private synchronized void a(String str, Map map, boolean z) {
        if (this.T == null) {
            ah.b("FlurryAgent", "onEvent called before onStartSession.  Event: " + str);
        } else {
            long jElapsedRealtime = SystemClock.elapsedRealtime() - this.I;
            String strA = r.a(str, 255);
            if (strA.length() != 0) {
                g gVar = (g) this.S.get(strA);
                if (gVar == null) {
                    if (this.S.size() < 100) {
                        g gVar2 = new g();
                        gVar2.a = 1;
                        this.S.put(strA, gVar2);
                        ah.a("FlurryAgent", "Event count incremented: " + strA);
                    } else if (ah.a("FlurryAgent")) {
                        ah.a("FlurryAgent", "Too many different events. Event not counted: " + strA);
                    }
                } else {
                    gVar.a++;
                    ah.a("FlurryAgent", "Event count incremented: " + strA);
                }
                if (j && this.T.size() < 200 && this.V < 16000) {
                    Map mapEmptyMap = map == null ? Collections.emptyMap() : map;
                    if (mapEmptyMap.size() > 10) {
                        if (ah.a("FlurryAgent")) {
                            ah.a("FlurryAgent", "MaxEventParams exceeded: " + mapEmptyMap.size());
                        }
                    } else {
                        i iVar = new i(strA, mapEmptyMap, jElapsedRealtime, z);
                        if (iVar.a().length + this.V <= 16000) {
                            this.T.add(iVar);
                            this.V = iVar.a().length + this.V;
                            ah.a("FlurryAgent", "Logged event: " + strA);
                        } else {
                            this.V = 16000;
                            this.U = false;
                            ah.a("FlurryAgent", "Event Log size exceeded. No more event details logged.");
                        }
                    }
                } else {
                    this.U = false;
                }
            }
        }
    }

    private synchronized void c(String str) {
        for (i iVar : this.T) {
            if (iVar.a(str)) {
                iVar.a(SystemClock.elapsedRealtime() - this.I);
                break;
            }
        }
    }

    private synchronized void a(String str, String str2, String str3) {
        if (this.W == null) {
            ah.b("FlurryAgent", "onError called before onStartSession.  Error: " + str);
        } else {
            this.Q++;
            if (this.W.size() < 10) {
                aa aaVar = new aa();
                aaVar.a = System.currentTimeMillis();
                aaVar.b = r.a(str, 255);
                aaVar.c = r.a(str2, 512);
                aaVar.d = r.a(str3, 255);
                this.W.add(aaVar);
                ah.a("FlurryAgent", "Error logged: " + aaVar.b);
            } else {
                ah.a("FlurryAgent", "Max errors logged. No more errors logged.");
            }
        }
    }

    private synchronized byte[] b(boolean z) {
        DataOutputStream dataOutputStream;
        byte[] byteArray;
        synchronized (this) {
            try {
                try {
                    try {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                        try {
                            dataOutputStream.writeShort(15);
                            if (o && z) {
                                dataOutputStream.writeShort(1);
                            } else {
                                dataOutputStream.writeShort(0);
                            }
                            if (o) {
                                dataOutputStream.writeLong(this.Y.d());
                                Set setE = this.Y.e();
                                dataOutputStream.writeShort(setE.size());
                                Iterator it = setE.iterator();
                                while (it.hasNext()) {
                                    long jLongValue = ((Long) it.next()).longValue();
                                    dataOutputStream.writeByte(1);
                                    dataOutputStream.writeLong(jLongValue);
                                }
                            } else {
                                dataOutputStream.writeLong(0L);
                                dataOutputStream.writeShort(0);
                            }
                            dataOutputStream.writeShort(3);
                            dataOutputStream.writeShort(Opcodes.LSHL);
                            dataOutputStream.writeLong(System.currentTimeMillis());
                            dataOutputStream.writeUTF(this.x);
                            dataOutputStream.writeUTF(this.z);
                            dataOutputStream.writeShort(0);
                            dataOutputStream.writeUTF(this.D);
                            dataOutputStream.writeLong(this.F);
                            dataOutputStream.writeLong(this.H);
                            dataOutputStream.writeShort(6);
                            dataOutputStream.writeUTF("device.model");
                            dataOutputStream.writeUTF(Build.MODEL);
                            dataOutputStream.writeUTF("build.brand");
                            dataOutputStream.writeUTF(Build.BRAND);
                            dataOutputStream.writeUTF("build.id");
                            dataOutputStream.writeUTF(Build.ID);
                            dataOutputStream.writeUTF("version.release");
                            dataOutputStream.writeUTF(Build.VERSION.RELEASE);
                            dataOutputStream.writeUTF("build.device");
                            dataOutputStream.writeUTF(Build.DEVICE);
                            dataOutputStream.writeUTF("build.product");
                            dataOutputStream.writeUTF(Build.PRODUCT);
                            int size = this.G.size();
                            dataOutputStream.writeShort(size);
                            for (int i2 = 0; i2 < size; i2++) {
                                dataOutputStream.write((byte[]) this.G.get(i2));
                            }
                            this.B = new ArrayList(this.G);
                            dataOutputStream.close();
                            byteArray = byteArrayOutputStream.toByteArray();
                            r.a(dataOutputStream);
                        } catch (Throwable th) {
                            th = th;
                            ah.b("FlurryAgent", "Error when generating report", th);
                            r.a(dataOutputStream);
                            byteArray = null;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        r.a((Closeable) null);
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    dataOutputStream = null;
                }
            } catch (Throwable th4) {
                throw th4;
            }
        }
        return byteArray;
    }

    private static String k() {
        if (c != null) {
            return c;
        }
        if (l) {
            return kInsecureReportUrl;
        }
        if (k) {
            return kSecureReportUrl;
        }
        return kInsecureReportUrl;
    }

    static String c() {
        return f != null ? f : g;
    }

    static boolean d() {
        if (o) {
            return h.Y.m();
        }
        return false;
    }

    private boolean a(byte[] bArr) {
        boolean zA;
        String strK = k();
        if (strK == null) {
            return false;
        }
        try {
            zA = a(bArr, strK);
        } catch (Exception e2) {
            ah.a("FlurryAgent", "Sending report exception: " + e2.getMessage());
            zA = false;
        }
        if (!zA && c == null && k && !l) {
            synchronized (h) {
                l = true;
                String strK2 = k();
                if (strK2 == null) {
                    zA = false;
                } else {
                    try {
                        zA = a(bArr, strK2);
                    } catch (Exception e3) {
                    }
                }
            }
            return zA;
        }
        return zA;
    }

    private boolean a(byte[] bArr, String str) throws IOException {
        boolean z = true;
        if (!"local".equals(str)) {
            ah.a("FlurryAgent", "Sending report to: " + str);
            ByteArrayEntity byteArrayEntity = new ByteArrayEntity(bArr);
            byteArrayEntity.setContentType("application/octet-stream");
            HttpPost httpPost = new HttpPost(str);
            httpPost.setEntity(byteArrayEntity);
            BasicHttpParams basicHttpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(basicHttpParams, CBAPIConnection.MIN_TIMEOUT);
            HttpConnectionParams.setSoTimeout(basicHttpParams, 15000);
            httpPost.getParams().setBooleanParameter("http.protocol.expect-continue", false);
            HttpResponse httpResponseExecute = a(basicHttpParams).execute(httpPost);
            int statusCode = httpResponseExecute.getStatusLine().getStatusCode();
            synchronized (this) {
                if (statusCode == 200) {
                    ah.a("FlurryAgent", "Report successful");
                    this.E = true;
                    this.G.removeAll(this.B);
                    HttpEntity entity = httpResponseExecute.getEntity();
                    ah.a("FlurryAgent", "Processing report response");
                    if (entity != null && entity.getContentLength() != 0) {
                        try {
                            a(new DataInputStream(entity.getContent()));
                            entity.consumeContent();
                        } catch (Throwable th) {
                            entity.consumeContent();
                            throw th;
                        }
                    }
                } else {
                    ah.a("FlurryAgent", "Report failed. HTTP response: " + statusCode);
                    z = false;
                }
                this.B = null;
            }
        }
        return z;
    }

    private void a(DataInputStream dataInputStream) throws IOException {
        int unsignedShort;
        HashMap map = new HashMap();
        HashMap map2 = new HashMap();
        HashMap map3 = new HashMap();
        HashMap map4 = new HashMap();
        HashMap map5 = new HashMap();
        HashMap map6 = new HashMap();
        do {
            unsignedShort = dataInputStream.readUnsignedShort();
            int i2 = dataInputStream.readInt();
            switch (unsignedShort) {
                case 258:
                    dataInputStream.readInt();
                    break;
                case 259:
                    byte b2 = dataInputStream.readByte();
                    int unsignedShort2 = dataInputStream.readUnsignedShort();
                    v[] vVarArr = new v[unsignedShort2];
                    for (int i3 = 0; i3 < unsignedShort2; i3++) {
                        vVarArr[i3] = new v(dataInputStream);
                    }
                    map.put(Byte.valueOf(b2), vVarArr);
                    break;
                case GL10.GL_ADD /* 260 */:
                case 261:
                case 265:
                case 267:
                default:
                    ah.a("FlurryAgent", "Unknown chunkType: " + unsignedShort);
                    dataInputStream.skipBytes(i2);
                    break;
                case 262:
                    int unsignedShort3 = dataInputStream.readUnsignedShort();
                    for (int i4 = 0; i4 < unsignedShort3; i4++) {
                        AdImage adImage = new AdImage(dataInputStream);
                        map2.put(Long.valueOf(adImage.a), adImage);
                    }
                    break;
                case 263:
                    int i5 = dataInputStream.readInt();
                    for (int i6 = 0; i6 < i5; i6++) {
                        e eVar = new e(dataInputStream);
                        map4.put(eVar.a, eVar);
                        ah.a("FlurryAgent", "Parsed hook: " + eVar);
                    }
                    break;
                case 264:
                    break;
                case 266:
                    byte b3 = dataInputStream.readByte();
                    for (int i7 = 0; i7 < b3; i7++) {
                        c cVar = new c(dataInputStream);
                        map5.put(Byte.valueOf(cVar.a), cVar);
                    }
                    break;
                case 268:
                    int i8 = dataInputStream.readInt();
                    for (int i9 = 0; i9 < i8; i9++) {
                        map6.put(Short.valueOf(dataInputStream.readShort()), Long.valueOf(dataInputStream.readLong()));
                    }
                    break;
                case 269:
                    dataInputStream.skipBytes(i2);
                    break;
                case 270:
                    dataInputStream.skipBytes(i2);
                    break;
                case 271:
                    byte b4 = dataInputStream.readByte();
                    for (int i10 = 0; i10 < b4; i10++) {
                        c cVar2 = (c) map5.get(Byte.valueOf(dataInputStream.readByte()));
                        if (cVar2 != null) {
                            cVar2.a(dataInputStream);
                        }
                    }
                    break;
                case Base.kNumLenSymbols /* 272 */:
                    long j2 = dataInputStream.readLong();
                    al alVar = (al) map3.get(Long.valueOf(j2));
                    if (alVar == null) {
                        alVar = new al();
                    }
                    alVar.a = dataInputStream.readUTF();
                    alVar.c = dataInputStream.readInt();
                    map3.put(Long.valueOf(j2), alVar);
                    break;
                case Base.kMatchMaxLen /* 273 */:
                    dataInputStream.skipBytes(i2);
                    break;
            }
        } while (unsignedShort != 264);
        if (o) {
            if (map.isEmpty()) {
                ah.a("FlurryAgent", "No ads from server");
            }
            this.Y.a(map, map4, map5, map2, map3, map6);
        }
    }

    private void c(boolean z) {
        try {
            ah.a("FlurryAgent", "generating report");
            byte[] bArrB = b(z);
            if (bArrB != null) {
                if (a(bArrB)) {
                    ah.a("FlurryAgent", "Done sending " + (this.t ? "initial " : "") + "agent report");
                    l();
                }
            } else {
                ah.a("FlurryAgent", "Error generating report");
            }
        } catch (IOException e2) {
            ah.a("FlurryAgent", "", e2);
        } catch (Throwable th) {
            ah.b("FlurryAgent", "", th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v10, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r1v7, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v9 */
    public synchronized void a(Context context) {
        DataInputStream dataInputStream;
        this.D = b(context);
        if (this.s.exists()) {
            ?? r1 = "loading persistent data: " + this.s.getAbsolutePath();
            ah.c("FlurryAgent", r1);
            try {
                try {
                    dataInputStream = new DataInputStream(new FileInputStream(this.s));
                    try {
                        if (dataInputStream.readUnsignedShort() == 46586) {
                            b(dataInputStream);
                        } else {
                            ah.a("FlurryAgent", "Unexpected file type");
                        }
                        r.a(dataInputStream);
                    } catch (Throwable th) {
                        th = th;
                        ah.b("FlurryAgent", "Error when loading persistent file", th);
                        r.a(dataInputStream);
                    }
                } catch (Throwable th2) {
                    th = th2;
                    r.a((Closeable) r1);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                dataInputStream = null;
            }
            try {
                if (!this.u) {
                    if (this.s.delete()) {
                        ah.a("FlurryAgent", "Deleted persistence file");
                    } else {
                        ah.b("FlurryAgent", "Cannot delete persistence file");
                    }
                }
            } catch (Throwable th4) {
                ah.b("FlurryAgent", "", th4);
            }
        } else {
            ah.c("FlurryAgent", "Agent cache file doesn't exist.");
        }
        if (!this.u) {
            this.E = false;
            this.F = this.H;
            this.u = true;
        }
        if (this.D == null) {
            this.D = "ID" + Long.toString(Double.doubleToLongBits(Math.random()) + (37 * (System.nanoTime() + ((long) (this.x.hashCode() * 37)))), 16);
            ah.c("FlurryAgent", "Generated id");
        }
        this.Y.a(this.D);
        if (!this.D.startsWith("AND") && !this.r.exists()) {
            c(context, this.D);
        }
    }

    private synchronized void b(DataInputStream dataInputStream) {
        int i2 = 0;
        synchronized (this) {
            int unsignedShort = dataInputStream.readUnsignedShort();
            if (unsignedShort > 2) {
                ah.b("FlurryAgent", "Unknown agent file version: " + unsignedShort);
                throw new IOException("Unknown agent file version: " + unsignedShort);
            }
            if (unsignedShort >= 2) {
                String utf = dataInputStream.readUTF();
                ah.a("FlurryAgent", "Loading API key: " + d(this.x));
                if (utf.equals(this.x)) {
                    String utf2 = dataInputStream.readUTF();
                    if (this.D == null) {
                        ah.a("FlurryAgent", "Loading phoneId: " + utf2);
                    }
                    this.D = utf2;
                    this.E = dataInputStream.readBoolean();
                    this.F = dataInputStream.readLong();
                    ah.a("FlurryAgent", "Loading session reports");
                    while (true) {
                        int unsignedShort2 = dataInputStream.readUnsignedShort();
                        if (unsignedShort2 == 0) {
                            break;
                        }
                        byte[] bArr = new byte[unsignedShort2];
                        dataInputStream.readFully(bArr);
                        this.G.add(0, bArr);
                        i2++;
                        ah.a("FlurryAgent", "Session report added: " + i2);
                    }
                    ah.a("FlurryAgent", "Persistent file loaded");
                    this.u = true;
                } else {
                    ah.a("FlurryAgent", "Api keys do not match, old: " + d(utf) + ", new: " + d(this.x));
                }
            } else {
                ah.d("FlurryAgent", "Deleting old file version: " + unsignedShort);
            }
        }
    }

    private static String d(String str) {
        if (str != null && str.length() > 4) {
            StringBuilder sb = new StringBuilder();
            for (int i2 = 0; i2 < str.length() - 4; i2++) {
                sb.append('*');
            }
            sb.append(str.substring(str.length() - 4));
            return sb.toString();
        }
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void l() {
        DataOutputStream dataOutputStream;
        try {
            try {
                if (a(this.s)) {
                    dataOutputStream = new DataOutputStream(new FileOutputStream(this.s));
                    try {
                        try {
                            dataOutputStream.writeShort(46586);
                            dataOutputStream.writeShort(2);
                            dataOutputStream.writeUTF(this.x);
                            dataOutputStream.writeUTF(this.D);
                            dataOutputStream.writeBoolean(this.E);
                            dataOutputStream.writeLong(this.F);
                            for (int size = this.G.size() - 1; size >= 0; size--) {
                                byte[] bArr = (byte[]) this.G.get(size);
                                int length = bArr.length;
                                if (length + 2 + dataOutputStream.size() > 50000) {
                                    ah.a("FlurryAgent", "discarded sessions: " + size);
                                    break;
                                } else {
                                    dataOutputStream.writeShort(length);
                                    dataOutputStream.write(bArr);
                                }
                            }
                            dataOutputStream.writeShort(0);
                            r.a(dataOutputStream);
                        } catch (Throwable th) {
                            th = th;
                            ah.b("FlurryAgent", "", th);
                            r.a(dataOutputStream);
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        r.a(dataOutputStream);
                        throw th;
                    }
                } else {
                    r.a((Closeable) null);
                }
            } catch (Throwable th3) {
                throw th3;
            }
        } catch (Throwable th4) {
            th = th4;
            dataOutputStream = null;
        }
    }

    private static boolean a(File file) {
        File parentFile = file.getParentFile();
        if (parentFile.mkdirs() || parentFile.exists()) {
            return true;
        }
        ah.b("FlurryAgent", "Unable to create persistent dir: " + parentFile);
        return false;
    }

    private synchronized void c(Context context, String str) {
        DataOutputStream dataOutputStream;
        this.r = context.getFileStreamPath(".flurryb.");
        if (a(this.r)) {
            try {
                try {
                    dataOutputStream = new DataOutputStream(new FileOutputStream(this.r));
                    try {
                        dataOutputStream.writeInt(1);
                        dataOutputStream.writeUTF(str);
                        r.a(dataOutputStream);
                    } catch (Throwable th) {
                        th = th;
                        ah.b("FlurryAgent", "Error when saving b file", th);
                        r.a(dataOutputStream);
                    }
                } catch (Throwable th2) {
                    th = th2;
                    r.a(dataOutputStream);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                dataOutputStream = null;
                r.a(dataOutputStream);
                throw th;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v16 */
    /* JADX WARN: Type inference failed for: r2v17 */
    /* JADX WARN: Type inference failed for: r2v2, types: [boolean] */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r2v4, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r2v5 */
    /* JADX WARN: Type inference failed for: r2v8 */
    private String b(Context context) throws Throwable {
        DataInputStream dataInputStream;
        String utf = null;
        boolean z = false;
        if (this.D != null) {
            return this.D;
        }
        String string = Settings.System.getString(context.getContentResolver(), TapjoyConstants.TJC_ANDROID_ID);
        if (string != null && string.length() > 0 && !string.equals("null")) {
            String[] strArr = b;
            int length = strArr.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    z = true;
                    break;
                }
                if (string.equals(strArr[i2])) {
                    break;
                }
                i2++;
            }
        }
        if (z) {
            return "AND" + string;
        }
        File fileStreamPath = context.getFileStreamPath(".flurryb.");
        ?? Exists = fileStreamPath.exists();
        try {
            if (Exists == 0) {
                return null;
            }
            try {
                dataInputStream = new DataInputStream(new FileInputStream(fileStreamPath));
                try {
                    dataInputStream.readInt();
                    utf = dataInputStream.readUTF();
                    r.a(dataInputStream);
                    Exists = dataInputStream;
                } catch (Throwable th) {
                    th = th;
                    ah.b("FlurryAgent", "Error when loading b file", th);
                    r.a(dataInputStream);
                    Exists = dataInputStream;
                }
            } catch (Throwable th2) {
                Exists = 0;
                th = th2;
            }
            return utf;
        } catch (Throwable th3) {
            th = th3;
        }
        r.a((Closeable) Exists);
        throw th;
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0013, code lost:
    
        r0 = "Unknown";
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static String c(Context context) {
        String string;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (packageInfo.versionName != null) {
                string = packageInfo.versionName;
            } else {
                string = packageInfo.versionCode != 0 ? Integer.toString(packageInfo.versionCode) : "Unknown";
            }
        } catch (Throwable th) {
            ah.b("FlurryAgent", "", th);
        }
        return string;
    }

    private Location d(Context context) {
        if (context.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == 0 || context.checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == 0) {
            LocationManager locationManager = (LocationManager) context.getSystemService("location");
            synchronized (this) {
                if (this.C == null) {
                    this.C = locationManager;
                } else {
                    locationManager = this.C;
                }
            }
            Criteria criteria = n;
            if (criteria == null) {
                criteria = new Criteria();
            }
            String bestProvider = locationManager.getBestProvider(criteria, true);
            if (bestProvider != null) {
                locationManager.requestLocationUpdates(bestProvider, 0L, 0.0f, this, Looper.getMainLooper());
                return locationManager.getLastKnownLocation(bestProvider);
            }
        }
        return null;
    }

    private synchronized void m() {
        if (this.C != null) {
            this.C.removeUpdates(this);
        }
    }

    static String e() {
        return h.x;
    }

    private synchronized String n() {
        return this.D;
    }

    public static String getPhoneId() {
        return h.n();
    }

    @Override // android.location.LocationListener
    public final synchronized void onLocationChanged(Location location) {
        try {
            this.R = location;
            m();
        } catch (Throwable th) {
            ah.b("FlurryAgent", "", th);
        }
    }

    @Override // android.location.LocationListener
    public final void onProviderDisabled(String str) {
    }

    @Override // android.location.LocationListener
    public final void onProviderEnabled(String str) {
    }

    @Override // android.location.LocationListener
    public final void onStatusChanged(String str, int i2, Bundle bundle) {
    }

    private HttpClient a(HttpParams httpParams) {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            ai aiVar = new ai(this, keyStore);
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schemeRegistry.register(new Scheme("https", aiVar, 443));
            return new DefaultHttpClient(new ThreadSafeClientConnManager(httpParams, schemeRegistry), httpParams);
        } catch (Exception e2) {
            return new DefaultHttpClient(httpParams);
        }
    }
}
