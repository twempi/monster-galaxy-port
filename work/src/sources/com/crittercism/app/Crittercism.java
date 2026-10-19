package com.crittercism.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.crittercism.NotificationActivity;
import com.sessionm.ui.SessionMActivity;
import com.tapjoy.TapjoyConstants;
import crittercism.android.b;
import crittercism.android.d;
import crittercism.android.e;
import crittercism.android.i;
import crittercism.android.k;
import crittercism.android.l;
import crittercism.android.m;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import oauth.signpost.OAuth;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class Crittercism {
    private b d;
    private d e;
    private static Crittercism c = new Crittercism();
    public static final String a = d.b();
    private e f = null;
    private boolean g = false;
    private Context h = null;
    private float i = 1.0f;
    private i j = new i();
    private m k = new m();
    private k l = new k();
    private l m = new l();
    private boolean n = false;
    private boolean o = false;
    private boolean p = false;
    private boolean q = false;
    private String r = "";
    private int s = 0;
    private String t = "";
    private String u = "Developer Reply";
    private String v = "com.crittercism/dumps";
    private boolean w = false;
    final Handler b = new Handler() { // from class: com.crittercism.app.Crittercism.3
        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            super.handleMessage(message);
            Bundle data = message.getData();
            if (data.containsKey("notification")) {
                try {
                    Intent intent = new Intent(Crittercism.this.h, (Class<?>) NotificationActivity.class);
                    intent.setFlags(272629760);
                    intent.putExtra("com.crittercism.notification", data.getString("notification"));
                    Crittercism.this.h.startActivity(intent);
                } catch (Exception e) {
                }
            }
        }
    };

    class a implements Thread.UncaughtExceptionHandler {
        private Thread.UncaughtExceptionHandler b;

        public a(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
            this.b = uncaughtExceptionHandler;
        }

        @Override // java.lang.Thread.UncaughtExceptionHandler
        public final void uncaughtException(Thread thread, Throwable th) {
            try {
                StringWriter stringWriter = new StringWriter();
                th.printStackTrace(new PrintWriter(stringWriter));
                if (!Crittercism.getOptOutStatus()) {
                    Log.e("Crittercism", stringWriter.toString());
                }
            } catch (Exception e) {
            }
            try {
                try {
                    if (Crittercism.a() == null) {
                        throw new Exception("Failed to log error to Crittercism.");
                    }
                    if (!Crittercism.getOptOutStatus()) {
                        Crittercism.v();
                        Crittercism.a().a(th);
                        Crittercism.a().f();
                        if (Crittercism.a().m().e().size() > 0) {
                            i iVarD = i.d();
                            iVarD.a(Crittercism.a().m().e());
                            Crittercism.a().a(iVarD);
                            i iVarM = Crittercism.a().m();
                            try {
                                SharedPreferences.Editor editorEdit = Crittercism.a().k().getSharedPreferences("com.crittercism.loads", 0).edit();
                                editorEdit.remove(i.a());
                                editorEdit.putString(i.a(), iVarM.c().toString());
                                if (!editorEdit.commit()) {
                                    throw new Exception("commit failed");
                                }
                            } catch (Exception e2) {
                            }
                        }
                        if (Crittercism.a().n().e().size() > 0) {
                            m mVarN = Crittercism.a().n();
                            try {
                                SharedPreferences.Editor editorEdit2 = Crittercism.a().k().getSharedPreferences("com.crittercism.crashes", 0).edit();
                                editorEdit2.remove(m.a());
                                editorEdit2.putString(m.a(), mVarN.b().toString());
                                if (!editorEdit2.commit()) {
                                    throw new Exception("commit failed");
                                }
                            } catch (Exception e3) {
                            }
                        }
                        if (Crittercism.a().o().e().size() > 0) {
                            k kVarO = Crittercism.a().o();
                            try {
                                SharedPreferences.Editor editorEdit3 = Crittercism.a().k().getSharedPreferences("com.crittercism.exceptions", 0).edit();
                                editorEdit3.remove(k.a());
                                editorEdit3.putString(k.a(), kVarO.b().toString());
                                if (!editorEdit3.commit()) {
                                    throw new Exception("commit failed");
                                }
                            } catch (Exception e4) {
                            }
                        }
                    }
                    if (this.b == null || (this.b instanceof a)) {
                        return;
                    }
                    this.b.uncaughtException(thread, th);
                } catch (Throwable th2) {
                    if (this.b != null && !(this.b instanceof a)) {
                        this.b.uncaughtException(thread, th);
                    }
                    throw th2;
                }
            } catch (Exception e5) {
                Log.w("Crittercism", "Failed to log error with Crittercism.  Please contact us at support@crittercism.com.");
                String str = "Did not log error to Crittercism.  EXCEPTION: " + e5.getClass().getName();
                if (this.b == null || (this.b instanceof a)) {
                    return;
                }
                this.b.uncaughtException(thread, th);
            }
        }
    }

    private Crittercism() {
    }

    private static long a(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.s", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        long time = 0;
        try {
            time = simpleDateFormat.parse(simpleDateFormat.format(date)).getTime();
            String str = "longFormat = " + Long.toString(time) + "\tdate = " + date.toString();
            return time;
        } catch (Exception e) {
            return time;
        }
    }

    public static Crittercism a() {
        return c;
    }

    private static boolean a(Context context) {
        try {
            return context.getSharedPreferences("com.crittercism.prefs", 0).getBoolean("optOutStatus", false);
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean a(Context context, String str) {
        boolean z;
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("com.crittercism.prefs", 0);
            z = sharedPreferences.getBoolean("crashedOnLastAppLoad", false);
            SharedPreferences.Editor editorEdit = sharedPreferences.edit();
            editorEdit.remove("crashedOnLastAppLoad");
            if (!editorEdit.commit()) {
                throw new Exception();
            }
        } catch (Exception e) {
            String str2 = "Exception when trying to retrieve crashedOnLastAppLoad from SharedPreferences! " + e.getClass().getName();
            z = false;
        }
        if (!z) {
            String str3 = "";
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(Environment.getExternalStorageDirectory(), "critter_" + str + "_crashedonlastappload.txt"))));
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    str3 = str3 + line;
                }
                bufferedReader.close();
                if (str3.contains("true")) {
                    z = true;
                }
            } catch (Exception e2) {
                z = false;
            }
        }
        String str4 = "read crashedOnLastAppLoad as: " + (z ? "true" : "false");
        return z;
    }

    public static boolean didCrashOnLastAppLoad() {
        if (c == null) {
            Log.w("Crittercism", "Call to didCrashOnLastAppLoad() failed.  Please contact us at support@crittercism.com.");
            return false;
        }
        try {
            return c.o;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getNotificationTitle() {
        if (c != null) {
            return c.u;
        }
        Log.w("Crittercism", "Call to getNotificationTitle failed.  Please contact us at support@crittercism.com.");
        return "Developer Reply";
    }

    public static boolean getOptOutStatus() {
        if (c == null) {
            Log.w("Crittercism", "Call to getOptOutStatus() failed.  Please contact us at support@crittercism.com.");
            return false;
        }
        try {
            Crittercism crittercism2 = c;
            Context context = c.h;
            c.j();
            return a(context);
        } catch (Exception e) {
            return false;
        }
    }

    public static synchronized boolean init(Context context, String str, JSONObject... jSONObjectArr) throws JSONException {
        boolean zDidCrashOnLastAppLoad;
        boolean z;
        JSONObject jSONObject;
        JSONObject jSONObject2;
        JSONArray jSONArray;
        if (str.contains("CRITTERCISM_APP_ID")) {
            Log.e("Crittercism", "ERROR: Crittercism will not work unless you enter a valid Crittercism App ID. Check your settings page to find the ID.");
            zDidCrashOnLastAppLoad = false;
        } else {
            JSONObject jSONObject3 = jSONObjectArr.length > 0 ? jSONObjectArr[0] : new JSONObject();
            if (c == null || c.g) {
                zDidCrashOnLastAppLoad = false;
            } else {
                try {
                    Crittercism crittercism2 = c;
                    crittercism2.g = true;
                    crittercism2.h = context;
                    crittercism2.r = crittercism2.w();
                    crittercism2.s = crittercism2.x();
                    crittercism2.d = new b(context, str, a, crittercism2.r, crittercism2.s);
                    crittercism2.i = crittercism2.d.e();
                    if (crittercism2.e == null) {
                        crittercism2.e = new d();
                    }
                    crittercism.android.a.a(context);
                    if (jSONObject3.has("installNdk")) {
                        try {
                            z = jSONObject3.getBoolean("installNdk");
                        } catch (JSONException e) {
                            z = false;
                        } catch (Exception e2) {
                            z = false;
                        }
                    } else {
                        z = false;
                    }
                    if (jSONObject3.has("nativeDumpPath")) {
                        try {
                            crittercism2.v = jSONObject3.getString("nativeDumpPath");
                        } catch (Exception e3) {
                        }
                    }
                    if (jSONObject3.has("delaySendingAppLoad")) {
                        try {
                            crittercism2.w = jSONObject3.getBoolean("delaySendingAppLoad");
                        } catch (Exception e4) {
                            crittercism2.w = false;
                        }
                    } else {
                        crittercism2.w = false;
                    }
                    if (jSONObject3.has("shouldCollectLogcat")) {
                        try {
                            crittercism2.p = jSONObject3.getBoolean("shouldCollectLogcat");
                        } catch (Exception e5) {
                            crittercism2.p = false;
                        }
                    }
                    if (jSONObject3.has("notificationTitle")) {
                        try {
                            crittercism2.u = jSONObject3.getString("notificationTitle");
                        } catch (Exception e6) {
                            crittercism2.u = "Developer Reply";
                        }
                    }
                    if (jSONObject3.has("shouldUseAmazonMarket")) {
                        try {
                            crittercism2.q = jSONObject3.getBoolean("shouldUseAmazonMarket");
                        } catch (Exception e7) {
                            crittercism2.q = false;
                        }
                    }
                    crittercism2.n = a(context);
                    crittercism2.o = a(context, str);
                    if (!crittercism2.n) {
                        crittercism2.u = getNotificationTitle();
                        String str2 = "initialize: notification title is " + crittercism2.u;
                        d dVar = crittercism2.e;
                        String strA = d.a(crittercism2.h, "breadcrumbsFileString");
                        JSONArray jSONArray2 = new JSONArray();
                        jSONArray2.put("session_start");
                        jSONArray2.put(u());
                        JSONArray jSONArray3 = new JSONArray();
                        jSONArray3.put(jSONArray2);
                        JSONObject jSONObject4 = new JSONObject();
                        JSONArray jSONArray4 = new JSONArray();
                        if (strA != null) {
                            try {
                                jSONObject = new JSONObject(strA);
                            } catch (JSONException e8) {
                                jSONObject = new JSONObject();
                            }
                            if (jSONObject.has("current_session")) {
                                try {
                                    jSONObject2 = jSONObject;
                                    jSONArray = jSONObject.getJSONArray("current_session");
                                } catch (JSONException e9) {
                                    jSONObject2 = new JSONObject();
                                    jSONArray = new JSONArray();
                                }
                            } else {
                                jSONObject2 = jSONObject;
                                jSONArray = jSONArray4;
                            }
                        } else {
                            jSONObject2 = jSONObject4;
                            jSONArray = jSONArray4;
                        }
                        try {
                            jSONObject2.put("previous_session", jSONArray);
                            jSONObject2.put("current_session", jSONArray3);
                        } catch (JSONException e10) {
                            jSONObject2 = new JSONObject();
                        }
                        String str3 = "Breadcrumbs: " + jSONObject2.toString();
                        d dVar2 = crittercism2.e;
                        d.a(crittercism2.h, "breadcrumbsFileString", jSONObject2.toString());
                        Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
                        if (!(defaultUncaughtExceptionHandler instanceof a)) {
                            Thread.setDefaultUncaughtExceptionHandler(crittercism2.new a(defaultUncaughtExceptionHandler));
                        }
                        d dVar3 = crittercism2.e;
                        String strA2 = d.a(crittercism2.h, "com.crittercism.prefs.did");
                        if (strA2 == null) {
                            strA2 = crittercism2.d.b();
                            d dVar4 = crittercism2.e;
                            d.a(crittercism2.h, "com.crittercism.prefs.did", strA2);
                        } else {
                            String str4 = "deviceID is " + strA2;
                        }
                        crittercism2.d.a(strA2);
                        crittercism2.j.b();
                        try {
                            if (!crittercism2.w && !getOptOutStatus()) {
                                new Thread(new Runnable() { // from class: com.crittercism.app.Crittercism.4
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        Crittercism.a().b();
                                        Crittercism.a().d();
                                        Crittercism.a().f();
                                        Crittercism.a().h();
                                    }
                                }).start();
                            }
                        } catch (Exception e11) {
                        }
                        if (z) {
                            new Thread(new Runnable() { // from class: com.crittercism.app.Crittercism.1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    try {
                                        CrittercismNDK.installNdkLib(Crittercism.a().k(), Crittercism.this.v);
                                    } catch (Throwable th) {
                                        String str5 = "Exception installing ndk library: " + th.getClass().getName();
                                    }
                                }
                            }).start();
                        }
                        Log.i("Crittercism", "Crittercism Initialized.");
                    }
                    zDidCrashOnLastAppLoad = didCrashOnLastAppLoad();
                } catch (Exception e12) {
                    zDidCrashOnLastAppLoad = false;
                }
            }
        }
        return zDidCrashOnLastAppLoad;
    }

    public static synchronized void leaveBreadcrumb(String str) {
        JSONObject jSONObject;
        JSONObject jSONObject2;
        JSONArray jSONArray;
        if (c == null) {
            Log.w("Crittercism", "Call to leaveBreadcrumb() failed.  Please contact us at support@crittercism.com.");
        } else {
            try {
                Crittercism crittercism2 = c;
                if (!crittercism2.n) {
                    d dVar = crittercism2.e;
                    String strA = d.a(crittercism2.h, "breadcrumbsFileString");
                    JSONArray jSONArray2 = new JSONArray();
                    jSONArray2.put(str);
                    jSONArray2.put(u());
                    JSONObject jSONObject3 = new JSONObject();
                    JSONArray jSONArray3 = new JSONArray();
                    if (strA != null) {
                        try {
                            jSONObject = new JSONObject(strA);
                        } catch (JSONException e) {
                            jSONObject = new JSONObject();
                        }
                        if (jSONObject.has("current_session")) {
                            try {
                                jSONArray3 = jSONObject.getJSONArray("current_session");
                                jSONObject2 = jSONObject;
                            } catch (JSONException e2) {
                                jSONArray3 = new JSONArray();
                                jSONObject2 = jSONObject;
                            }
                        } else {
                            jSONObject2 = jSONObject;
                        }
                    } else {
                        jSONObject2 = jSONObject3;
                    }
                    jSONArray3.put(jSONArray2);
                    if (jSONArray3.length() > 50) {
                        jSONArray = new JSONArray();
                        try {
                            jSONArray.put(jSONArray3.getJSONArray(0));
                            for (int i = 2; i < jSONArray3.length(); i++) {
                                jSONArray.put(jSONArray3.getJSONArray(i));
                            }
                        } catch (JSONException e3) {
                            jSONArray = new JSONArray();
                        }
                    } else {
                        jSONArray = jSONArray3;
                    }
                    try {
                        jSONObject2.put("current_session", jSONArray);
                    } catch (JSONException e4) {
                    }
                    String str2 = "Breadcrumbs: " + jSONObject2.toString();
                    String str3 = "currentSessionJsonArray size: " + Integer.toString(jSONArray.length());
                    d dVar2 = crittercism2.e;
                    d.a(crittercism2.h, "breadcrumbsFileString", jSONObject2.toString());
                }
            } catch (Exception e5) {
                Log.w("Crittercism", "Failed to leave breadcrumb.  Please contact us at support@crittercism.com.");
            }
        }
    }

    public static synchronized void logHandledException(Throwable th) {
        String str;
        if (c == null) {
            Log.w("Crittercism", "Call to logHandledException() failed.  Please contact us at support@crittercism.com.");
        } else {
            try {
                if (!getOptOutStatus()) {
                    Crittercism crittercism2 = c;
                    if (crittercism2.l.h < 50 && crittercism2.l.e().size() < 5 && crittercism2.l.e().size() + crittercism2.l.h < 50) {
                        k kVar = crittercism2.l;
                        JSONObject jSONObject = new JSONObject();
                        new String();
                        try {
                            String message = th.getMessage() != null ? th.getMessage() : "";
                            kVar.a(th);
                            kVar.d();
                            try {
                                d dVar = c.e;
                                String strA = d.a(c.h, "breadcrumbsFileString");
                                if (strA != null) {
                                    kVar.f = new JSONObject(strA);
                                }
                            } catch (Exception e) {
                                kVar.f = new JSONObject();
                                String str2 = "Exception making breadcrumbs in HandledExceptions.setBreadcrumbs: " + e.getClass().getName();
                            }
                            if (kVar.f.has("current_session")) {
                                str = message;
                            } else {
                                try {
                                    kVar.f.put("current_session", new JSONArray());
                                    kVar.f.put("previous_session", new JSONArray());
                                    str = message;
                                } catch (JSONException e2) {
                                    kVar.f = new JSONObject();
                                    str = message;
                                }
                            }
                        } catch (Exception e3) {
                            String str3 = "Exception in addThrowableToVector: " + e3.getClass().getName();
                            str = new String();
                            kVar.c = new String();
                            kVar.d = new JSONArray();
                            kVar.e = new JSONArray();
                            kVar.f = new JSONObject();
                        }
                        try {
                            jSONObject.put("app_state", c.d.a(true, true));
                            jSONObject.put("breadcrumbs", kVar.f);
                            jSONObject.put("current_thread_id", Thread.currentThread().getId());
                            jSONObject.put("exception_name", kVar.c);
                            jSONObject.put("exception_reason", str);
                            jSONObject.put(TapjoyConstants.TJC_PLATFORM, TapjoyConstants.TJC_DEVICE_PLATFORM_TYPE);
                            jSONObject.put("threads", kVar.e);
                            jSONObject.put("ts", u());
                            if (Thread.currentThread().getId() == 1) {
                                jSONObject.put(SessionMActivity.INTENT_PARAM_TYPE, crittercism.android.a.C0014a.c);
                            } else {
                                jSONObject.put(SessionMActivity.INTENT_PARAM_TYPE, crittercism.android.a.C0014a.d);
                            }
                            jSONObject.put("unsymbolized_stacktrace", kVar.d);
                        } catch (JSONException e4) {
                            String str4 = "JSONException in addThrowableToVector: " + e4.getClass().getName();
                        } catch (Exception e5) {
                            String str5 = "Exception in addThrowableToVector: " + e5.getClass().getName();
                        }
                        kVar.a((Object) jSONObject);
                        try {
                            Date date = crittercism2.l.g;
                            if (date == null || (date != null && a(new Date()) - a(date) > TapjoyConstants.THROTTLE_GET_TAP_POINTS_INTERVAL)) {
                                new Thread(new Runnable() { // from class: com.crittercism.app.Crittercism.8
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        try {
                                            Crittercism.a().f();
                                        } catch (Exception e6) {
                                            String str6 = "Exception in logHandledExceptionInstanceMethod Thread.run: " + e6.getClass().getName();
                                        }
                                    }
                                }).start();
                            }
                        } catch (Exception e6) {
                            String str6 = "Exception in logHandledExceptionInstanceMethod: " + e6.getClass().getName();
                        }
                    }
                }
            } catch (Exception e7) {
            }
        }
    }

    public static boolean s() {
        if (c == null) {
            return false;
        }
        return c.p;
    }

    public static void sendAppLoadData() {
        if (c == null) {
            Log.w("Crittercism", "Failed to send app load data.  Please contact us at support@crittercism.com");
            return;
        }
        try {
            if (!c.w) {
                Log.i("Crittercism", "sendAppLoadData() will only send data to Crittercism if \"delaySendingAppLoad\" is set to true in the configuration settings you include in the init call.");
            } else if (!getOptOutStatus()) {
                new Thread(new Runnable() { // from class: com.crittercism.app.Crittercism.5
                    @Override // java.lang.Runnable
                    public final void run() {
                        try {
                            Crittercism.a().b();
                            Crittercism.a().d();
                            Crittercism.a().f();
                            Crittercism.a().h();
                        } catch (Exception e) {
                        }
                    }
                }).start();
            }
        } catch (Exception e) {
        }
    }

    public static void setMetadata(final JSONObject jSONObject) {
        if (c == null) {
            Log.w("Crittercism", "Call to setMetadata() failed.  Please contact us at support@crittercism.com.");
            return;
        }
        try {
            c.d.c(jSONObject);
            if (c.g) {
                new Thread(new Runnable() { // from class: com.crittercism.app.Crittercism.2
                    @Override // java.lang.Runnable
                    public final void run() {
                        try {
                            Crittercism.this.d.b(jSONObject);
                        } catch (Exception e) {
                        }
                    }
                }).start();
            } else {
                Log.e("Crittercism", "Initialize the Crittercism library before using its methods.");
            }
        } catch (Exception e) {
        }
    }

    public static void setOptOutStatus(boolean z) {
        if (c == null) {
            Log.w("Crittercism", "Call to setOptOutStatus() failed.  Please contact us at support@crittercism.com.");
            return;
        }
        try {
            SharedPreferences.Editor editorEdit = c.h.getSharedPreferences("com.crittercism.prefs", 0).edit();
            editorEdit.remove("optOutStatus");
            editorEdit.putBoolean("optOutStatus", z);
            if (!editorEdit.commit()) {
                throw new Exception();
            }
            String str = "Saved optOutStatus as " + (z ? "true" : "false") + " to SharedPreferences!!";
            c.n = z;
        } catch (Exception e) {
            try {
                File externalStorageDirectory = Environment.getExternalStorageDirectory();
                if (externalStorageDirectory.canWrite()) {
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(externalStorageDirectory, "critter_" + c.d.a() + "_optoutsetting.txt")));
                    bufferedWriter.write(z ? "true" : "false");
                    bufferedWriter.close();
                }
            } catch (IOException e2) {
                Log.w("Crittercism", "Failed to store opt out status!!");
            }
        }
    }

    public static void setUsername(String str) {
        if (c == null) {
            Log.w("Crittercism", "Call to setUsername() failed.  Please contact us at support@crittercism.com.");
            return;
        }
        try {
            if (!c.g) {
                Log.e("Crittercism", "Initialize the Crittercism library before using its methods.");
                return;
            }
            if (c.f == null) {
                c.f = new e();
            }
            c.f.b = str;
            JSONObject jSONObjectI = c.d.i();
            jSONObjectI.put("username", str);
            setMetadata(jSONObjectI);
        } catch (Exception e) {
        }
    }

    public static String u() {
        new String();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sZ", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat.format(new Date());
    }

    static /* synthetic */ void v() {
        if (c != null) {
            try {
                SharedPreferences.Editor editorEdit = c.h.getSharedPreferences("com.crittercism.prefs", 0).edit();
                editorEdit.remove("crashedOnLastAppLoad");
                editorEdit.putBoolean("crashedOnLastAppLoad", true);
                if (editorEdit.commit()) {
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
            }
        }
    }

    private String w() {
        try {
            return this.h.getPackageManager().getPackageInfo(this.h.getPackageName(), 0).versionName;
        } catch (Exception e) {
            return OAuth.VERSION_1_0;
        }
    }

    private int x() {
        try {
            return this.h.getPackageManager().getPackageInfo(this.h.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            return 0;
        }
    }

    public final void a(i iVar) {
        this.j = iVar;
    }

    public final void a(k kVar) {
        this.l = kVar;
    }

    public final void a(l lVar) {
        this.m = lVar;
    }

    public final void a(m mVar) {
        this.k = mVar;
    }

    /* JADX WARN: Code duplicated, block: B:17:0x00b1 A[Catch: all -> 0x0122, JSONException -> 0x0138, Exception -> 0x0151, TryCatch #2 {JSONException -> 0x0138, blocks: (B:15:0x0059, B:17:0x00b1, B:18:0x00b8, B:34:0x0130), top: B:45:0x0059, outer: #0 }] */
    /* JADX WARN: Code duplicated, block: B:34:0x0130 A[Catch: all -> 0x0122, JSONException -> 0x0138, Exception -> 0x0151, TRY_ENTER, TRY_LEAVE, TryCatch #2 {JSONException -> 0x0138, blocks: (B:15:0x0059, B:17:0x00b1, B:18:0x00b8, B:34:0x0130), top: B:45:0x0059, outer: #0 }] */
    public final synchronized boolean a(Throwable th) {
        String str;
        m mVar = this.k;
        JSONObject jSONObject = new JSONObject();
        new String();
        try {
            String message = th.getMessage() != null ? th.getMessage() : "";
            mVar.a(th);
            mVar.d();
            try {
                d dVar = c.e;
                String strA = d.a(c.h, "breadcrumbsFileString");
                if (strA != null) {
                    mVar.f = new JSONObject(strA);
                }
            } catch (Exception e) {
                mVar.f = new JSONObject();
                String str2 = "Exception making breadcrumbs in SdkCrashes.setBreadcrumbs: " + e.getClass().getName();
            }
            if (mVar.f.has("current_session")) {
                str = message;
                jSONObject.put("app_state", c.d.a(true, true));
                jSONObject.put("breadcrumbs", mVar.f);
                jSONObject.put("current_thread_id", Thread.currentThread().getId());
                jSONObject.put("exception_name", mVar.c);
                jSONObject.put("exception_reason", str);
                jSONObject.put(TapjoyConstants.TJC_PLATFORM, TapjoyConstants.TJC_DEVICE_PLATFORM_TYPE);
                jSONObject.put("threads", mVar.e);
                jSONObject.put("ts", u());
                if (Thread.currentThread().getId() == 1) {
                    jSONObject.put(SessionMActivity.INTENT_PARAM_TYPE, crittercism.android.a.C0014a.a);
                } else {
                    jSONObject.put(SessionMActivity.INTENT_PARAM_TYPE, crittercism.android.a.C0014a.b);
                }
                jSONObject.put("unsymbolized_stacktrace", mVar.d);
                mVar.a((Object) jSONObject);
            } else {
                try {
                    mVar.f.put("current_session", new JSONArray());
                    mVar.f.put("previous_session", new JSONArray());
                    str = message;
                } catch (JSONException e2) {
                    mVar.f = new JSONObject();
                    str = message;
                }
                try {
                    try {
                        jSONObject.put("app_state", c.d.a(true, true));
                        jSONObject.put("breadcrumbs", mVar.f);
                        jSONObject.put("current_thread_id", Thread.currentThread().getId());
                        jSONObject.put("exception_name", mVar.c);
                        jSONObject.put("exception_reason", str);
                        jSONObject.put(TapjoyConstants.TJC_PLATFORM, TapjoyConstants.TJC_DEVICE_PLATFORM_TYPE);
                        jSONObject.put("threads", mVar.e);
                        jSONObject.put("ts", u());
                        if (Thread.currentThread().getId() == 1) {
                            jSONObject.put(SessionMActivity.INTENT_PARAM_TYPE, crittercism.android.a.C0014a.a);
                        } else {
                            jSONObject.put(SessionMActivity.INTENT_PARAM_TYPE, crittercism.android.a.C0014a.b);
                        }
                        jSONObject.put("unsymbolized_stacktrace", mVar.d);
                    } catch (JSONException e3) {
                        String str3 = "JSONException in addThrowableToVector: " + e3.getClass().getName();
                    }
                } catch (Exception e4) {
                    String str4 = "Exception in addThrowableToVector: " + e4.getClass().getName();
                }
                mVar.a((Object) jSONObject);
            }
        } catch (Exception e5) {
            String str5 = "Exception in addThrowableToVector: " + e5.getClass().getName();
            str = new String();
            mVar.c = new String();
            mVar.d = new JSONArray();
            mVar.e = new JSONArray();
            mVar.f = new JSONObject();
        }
        throw th;
        return d();
    }

    public final boolean b() {
        boolean zBooleanValue;
        FutureTask futureTask = new FutureTask(new Callable() { // from class: com.crittercism.app.Crittercism.6
            private static Boolean a() {
                try {
                    Vector vectorE = Crittercism.a().m().e();
                    i iVarD = i.d();
                    iVarD.a(vectorE);
                    Crittercism.a().a(iVarD);
                    String str = "allAppLoads.getPendingDataVector().size() = " + iVarD.e().size();
                } catch (Exception e) {
                    String str2 = "Exception in startAppLoadsThreads boolean callable: " + e.getClass().getName();
                }
                try {
                    if (Crittercism.a().m().e().size() == 0) {
                        return true;
                    }
                } catch (Exception e2) {
                }
                return Boolean.valueOf(Crittercism.a().c());
            }

            @Override // java.util.concurrent.Callable
            public final /* synthetic */ Object call() {
                return a();
            }
        });
        try {
            Executors.newFixedThreadPool(10).execute(futureTask);
            zBooleanValue = false;
            while (!futureTask.isDone()) {
                zBooleanValue = ((Boolean) futureTask.get(2500L, TimeUnit.MILLISECONDS)).booleanValue();
            }
        } catch (Exception e) {
            zBooleanValue = false;
        }
        String str = "sentAppLoads = " + (zBooleanValue ? "TRUE" : "FALSE");
        return zBooleanValue;
    }

    /* JADX WARN: Code duplicated, block: B:27:0x0072 A[Catch: Exception -> 0x0159, TryCatch #4 {Exception -> 0x0159, blocks: (B:25:0x006a, B:27:0x0072, B:29:0x0080, B:31:0x008e, B:34:0x009f, B:36:0x00ae), top: B:77:0x006a, outer: #6 }] */
    /* JADX WARN: Code duplicated, block: B:29:0x0080 A[Catch: Exception -> 0x0159, TryCatch #4 {Exception -> 0x0159, blocks: (B:25:0x006a, B:27:0x0072, B:29:0x0080, B:31:0x008e, B:34:0x009f, B:36:0x00ae), top: B:77:0x006a, outer: #6 }] */
    /* JADX WARN: Code duplicated, block: B:31:0x008e A[Catch: Exception -> 0x0159, TryCatch #4 {Exception -> 0x0159, blocks: (B:25:0x006a, B:27:0x0072, B:29:0x0080, B:31:0x008e, B:34:0x009f, B:36:0x00ae), top: B:77:0x006a, outer: #6 }] */
    /* JADX WARN: Code duplicated, block: B:33:0x009d  */
    /* JADX WARN: Code duplicated, block: B:36:0x00ae A[Catch: Exception -> 0x0159, TRY_LEAVE, TryCatch #4 {Exception -> 0x0159, blocks: (B:25:0x006a, B:27:0x0072, B:29:0x0080, B:31:0x008e, B:34:0x009f, B:36:0x00ae), top: B:77:0x006a, outer: #6 }] */
    /* JADX WARN: Code duplicated, block: B:39:0x00b7 A[Catch: Exception -> 0x0172, TryCatch #7 {Exception -> 0x0172, blocks: (B:37:0x00b3, B:39:0x00b7, B:41:0x00e1, B:43:0x00e9), top: B:82:0x00b3, outer: #6 }] */
    /* JADX WARN: Code duplicated, block: B:41:0x00e1 A[Catch: Exception -> 0x0172, TryCatch #7 {Exception -> 0x0172, blocks: (B:37:0x00b3, B:39:0x00b7, B:41:0x00e1, B:43:0x00e9), top: B:82:0x00b3, outer: #6 }] */
    /* JADX WARN: Code duplicated, block: B:56:0x0155  */
    /* JADX WARN: Code duplicated, block: B:77:0x006a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:82:0x00b3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    public final boolean c() {
        JSONObject jSONObject;
        boolean z;
        Exception e;
        JSONObject jSONObjectA;
        boolean z2;
        e eVarA;
        String str;
        JSONObject jSONObject2;
        JSONObject jSONObject3;
        String str2;
        String str3 = null;
        boolean z3 = false;
        JSONObject jSONObject4 = new JSONObject();
        JSONObject jSONObject5 = new JSONObject();
        try {
            jSONObject4 = this.j.c();
        } catch (Exception e2) {
        }
        try {
            jSONObjectA = this.d.a(jSONObject4);
            try {
                if (jSONObjectA.has("success") && jSONObjectA.getInt("success") == 1) {
                    try {
                        this.d.h();
                        this.j.f();
                        z3 = true;
                    } catch (Exception e3) {
                        e = e3;
                        jSONObject = jSONObjectA;
                        z = true;
                        String str4 = "Exception obtaining or handling response object or clearing pending app loads vector in attemptToSendAppLoads " + e.getClass().getName();
                        JSONObject jSONObject6 = jSONObject;
                        z2 = z;
                        jSONObjectA = jSONObject6;
                    }
                }
                z2 = z3;
            } catch (Exception e4) {
                jSONObject = jSONObjectA;
                z = false;
                e = e4;
            }
        } catch (Exception e5) {
            jSONObject = jSONObject5;
            z = false;
            e = e5;
        }
        try {
            new JSONObject();
            try {
                eVarA = jSONObjectA.has("me") ? e.a(jSONObjectA.getJSONObject("me")) : null;
            } catch (Exception e6) {
                String str5 = "Exception getting user object in handleAppLoadResponse: " + e6.getClass().getName();
            }
            if (eVarA != null) {
                try {
                    if (this.f != null && !this.f.b.equals("")) {
                        str3 = this.f.b;
                    }
                    this.f = eVarA;
                    if (str3 != null) {
                        this.f.b = str3;
                        try {
                            if (jSONObjectA.has("app_settings")) {
                                jSONObject2 = jSONObjectA.getJSONObject("app_settings");
                                if (jSONObject2.has("settings")) {
                                    jSONObject3 = jSONObject2.getJSONObject("settings");
                                    if (jSONObject3.has("need_pkg")) {
                                        StringBuilder sb = new StringBuilder("settings need_pkg = ");
                                        if (jSONObject3.getInt("need_pkg") == 1) {
                                            str2 = "true";
                                        } else {
                                            str2 = "false";
                                        }
                                        sb.append(str2).toString();
                                        if (jSONObject3.getInt("need_pkg") == 1) {
                                            this.d.g();
                                            try {
                                                if (this.f != null) {
                                                    e eVar = this.f;
                                                    str = eVar.a;
                                                    eVar.a = "";
                                                    String str6 = "pop notification: " + str;
                                                    String str7 = "username: " + this.f.b;
                                                    if (str != null && !str.equals("")) {
                                                        Message messageObtain = Message.obtain(this.b);
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("notification", str);
                                                        messageObtain.setData(bundle);
                                                        messageObtain.sendToTarget();
                                                    }
                                                }
                                            } catch (Exception e7) {
                                                String str8 = "Exception with user pop notification! " + e7.getClass().getName();
                                            }
                                        } else if (this.f != null) {
                                            e eVar2 = this.f;
                                            str = eVar2.a;
                                            eVar2.a = "";
                                            String str9 = "pop notification: " + str;
                                            String str10 = "username: " + this.f.b;
                                            if (str != null) {
                                                Message messageObtain2 = Message.obtain(this.b);
                                                Bundle bundle2 = new Bundle();
                                                bundle2.putString("notification", str);
                                                messageObtain2.setData(bundle2);
                                                messageObtain2.sendToTarget();
                                            }
                                        }
                                    } else if (this.f != null) {
                                        e eVar3 = this.f;
                                        str = eVar3.a;
                                        eVar3.a = "";
                                        String str11 = "pop notification: " + str;
                                        String str12 = "username: " + this.f.b;
                                        if (str != null) {
                                            Message messageObtain3 = Message.obtain(this.b);
                                            Bundle bundle3 = new Bundle();
                                            bundle3.putString("notification", str);
                                            messageObtain3.setData(bundle3);
                                            messageObtain3.sendToTarget();
                                        }
                                    }
                                } else if (this.f != null) {
                                    e eVar4 = this.f;
                                    str = eVar4.a;
                                    eVar4.a = "";
                                    String str13 = "pop notification: " + str;
                                    String str14 = "username: " + this.f.b;
                                    if (str != null) {
                                        Message messageObtain4 = Message.obtain(this.b);
                                        Bundle bundle4 = new Bundle();
                                        bundle4.putString("notification", str);
                                        messageObtain4.setData(bundle4);
                                        messageObtain4.sendToTarget();
                                    }
                                }
                            } else if (this.f != null) {
                                e eVar5 = this.f;
                                str = eVar5.a;
                                eVar5.a = "";
                                String str15 = "pop notification: " + str;
                                String str16 = "username: " + this.f.b;
                                if (str != null) {
                                    Message messageObtain5 = Message.obtain(this.b);
                                    Bundle bundle5 = new Bundle();
                                    bundle5.putString("notification", str);
                                    messageObtain5.setData(bundle5);
                                    messageObtain5.sendToTarget();
                                }
                            }
                        } catch (Exception e8) {
                            String str17 = "Exception setting app settings in handleAppLoadResponse: " + e8.getClass().getName();
                        }
                    } else if (jSONObjectA.has("app_settings")) {
                        jSONObject2 = jSONObjectA.getJSONObject("app_settings");
                        if (jSONObject2.has("settings")) {
                            jSONObject3 = jSONObject2.getJSONObject("settings");
                            if (jSONObject3.has("need_pkg")) {
                                StringBuilder sb2 = new StringBuilder("settings need_pkg = ");
                                if (jSONObject3.getInt("need_pkg") == 1) {
                                    str2 = "true";
                                } else {
                                    str2 = "false";
                                }
                                sb2.append(str2).toString();
                                if (jSONObject3.getInt("need_pkg") == 1) {
                                    this.d.g();
                                    if (this.f != null) {
                                        e eVar6 = this.f;
                                        str = eVar6.a;
                                        eVar6.a = "";
                                        String str18 = "pop notification: " + str;
                                        String str19 = "username: " + this.f.b;
                                        if (str != null) {
                                            Message messageObtain6 = Message.obtain(this.b);
                                            Bundle bundle6 = new Bundle();
                                            bundle6.putString("notification", str);
                                            messageObtain6.setData(bundle6);
                                            messageObtain6.sendToTarget();
                                        }
                                    }
                                } else if (this.f != null) {
                                    e eVar7 = this.f;
                                    str = eVar7.a;
                                    eVar7.a = "";
                                    String str110 = "pop notification: " + str;
                                    String str111 = "username: " + this.f.b;
                                    if (str != null) {
                                        Message messageObtain7 = Message.obtain(this.b);
                                        Bundle bundle7 = new Bundle();
                                        bundle7.putString("notification", str);
                                        messageObtain7.setData(bundle7);
                                        messageObtain7.sendToTarget();
                                    }
                                }
                            } else if (this.f != null) {
                                e eVar8 = this.f;
                                str = eVar8.a;
                                eVar8.a = "";
                                String str112 = "pop notification: " + str;
                                String str113 = "username: " + this.f.b;
                                if (str != null) {
                                    Message messageObtain8 = Message.obtain(this.b);
                                    Bundle bundle8 = new Bundle();
                                    bundle8.putString("notification", str);
                                    messageObtain8.setData(bundle8);
                                    messageObtain8.sendToTarget();
                                }
                            }
                        } else if (this.f != null) {
                            e eVar9 = this.f;
                            str = eVar9.a;
                            eVar9.a = "";
                            String str114 = "pop notification: " + str;
                            String str115 = "username: " + this.f.b;
                            if (str != null) {
                                Message messageObtain9 = Message.obtain(this.b);
                                Bundle bundle9 = new Bundle();
                                bundle9.putString("notification", str);
                                messageObtain9.setData(bundle9);
                                messageObtain9.sendToTarget();
                            }
                        }
                    } else if (this.f != null) {
                        e eVar10 = this.f;
                        str = eVar10.a;
                        eVar10.a = "";
                        String str116 = "pop notification: " + str;
                        String str117 = "username: " + this.f.b;
                        if (str != null) {
                            Message messageObtain10 = Message.obtain(this.b);
                            Bundle bundle10 = new Bundle();
                            bundle10.putString("notification", str);
                            messageObtain10.setData(bundle10);
                            messageObtain10.sendToTarget();
                        }
                    }
                } catch (Exception e9) {
                    String str20 = "Exception setting user object in handleAppLoadResponse: " + e9.getClass().getName();
                }
            } else if (jSONObjectA.has("app_settings")) {
                jSONObject2 = jSONObjectA.getJSONObject("app_settings");
                if (jSONObject2.has("settings")) {
                    jSONObject3 = jSONObject2.getJSONObject("settings");
                    if (jSONObject3.has("need_pkg")) {
                        StringBuilder sb3 = new StringBuilder("settings need_pkg = ");
                        if (jSONObject3.getInt("need_pkg") == 1) {
                            str2 = "true";
                        } else {
                            str2 = "false";
                        }
                        sb3.append(str2).toString();
                        if (jSONObject3.getInt("need_pkg") == 1) {
                            this.d.g();
                            if (this.f != null) {
                                e eVar11 = this.f;
                                str = eVar11.a;
                                eVar11.a = "";
                                String str118 = "pop notification: " + str;
                                String str119 = "username: " + this.f.b;
                                if (str != null) {
                                    Message messageObtain11 = Message.obtain(this.b);
                                    Bundle bundle11 = new Bundle();
                                    bundle11.putString("notification", str);
                                    messageObtain11.setData(bundle11);
                                    messageObtain11.sendToTarget();
                                }
                            }
                        } else if (this.f != null) {
                            e eVar12 = this.f;
                            str = eVar12.a;
                            eVar12.a = "";
                            String str1110 = "pop notification: " + str;
                            String str1111 = "username: " + this.f.b;
                            if (str != null) {
                                Message messageObtain12 = Message.obtain(this.b);
                                Bundle bundle12 = new Bundle();
                                bundle12.putString("notification", str);
                                messageObtain12.setData(bundle12);
                                messageObtain12.sendToTarget();
                            }
                        }
                    } else if (this.f != null) {
                        e eVar13 = this.f;
                        str = eVar13.a;
                        eVar13.a = "";
                        String str1112 = "pop notification: " + str;
                        String str1113 = "username: " + this.f.b;
                        if (str != null) {
                            Message messageObtain13 = Message.obtain(this.b);
                            Bundle bundle13 = new Bundle();
                            bundle13.putString("notification", str);
                            messageObtain13.setData(bundle13);
                            messageObtain13.sendToTarget();
                        }
                    }
                } else if (this.f != null) {
                    e eVar14 = this.f;
                    str = eVar14.a;
                    eVar14.a = "";
                    String str1114 = "pop notification: " + str;
                    String str1115 = "username: " + this.f.b;
                    if (str != null) {
                        Message messageObtain14 = Message.obtain(this.b);
                        Bundle bundle14 = new Bundle();
                        bundle14.putString("notification", str);
                        messageObtain14.setData(bundle14);
                        messageObtain14.sendToTarget();
                    }
                }
            } else if (this.f != null) {
                e eVar15 = this.f;
                str = eVar15.a;
                eVar15.a = "";
                String str1116 = "pop notification: " + str;
                String str1117 = "username: " + this.f.b;
                if (str != null) {
                    Message messageObtain15 = Message.obtain(this.b);
                    Bundle bundle15 = new Bundle();
                    bundle15.putString("notification", str);
                    messageObtain15.setData(bundle15);
                    messageObtain15.sendToTarget();
                }
            }
        } catch (Exception e10) {
        }
        return z2;
    }

    public final boolean d() {
        boolean zBooleanValue;
        FutureTask futureTask = new FutureTask(new Callable() { // from class: com.crittercism.app.Crittercism.7
            private static Boolean a() {
                try {
                    Vector vectorE = Crittercism.a().n().e();
                    m mVarC = m.c();
                    mVarC.a(vectorE);
                    Crittercism.a().a(mVarC);
                } catch (Exception e) {
                    String str = "Exception in startCrashingSendingThreads boolean callable: " + e.getClass().getName();
                }
                try {
                    if (Crittercism.a().n().e().size() == 0) {
                        return true;
                    }
                } catch (Exception e2) {
                }
                return Boolean.valueOf(Crittercism.a().e());
            }

            @Override // java.util.concurrent.Callable
            public final /* synthetic */ Object call() {
                return a();
            }
        });
        try {
            Executors.newFixedThreadPool(10).execute(futureTask);
            zBooleanValue = false;
            while (!futureTask.isDone()) {
                zBooleanValue = ((Boolean) futureTask.get(2500L, TimeUnit.MILLISECONDS)).booleanValue();
            }
        } catch (Exception e) {
            zBooleanValue = false;
        }
        String str = "sentCrashes = " + (zBooleanValue ? "TRUE" : "FALSE");
        return zBooleanValue;
    }

    public final boolean e() {
        Exception e;
        boolean z = true;
        JSONObject jSONObject = new JSONObject();
        new JSONObject();
        try {
            jSONObject = this.k.b();
        } catch (Exception e2) {
        }
        try {
            JSONObject jSONObjectA = this.d.a(jSONObject);
            if (!jSONObjectA.has("success") || jSONObjectA.getInt("success") != 1) {
                return false;
            }
            try {
                this.k.f();
                return true;
            } catch (Exception e3) {
                e = e3;
                String str = "Exception obtaining or handling response object or clearing pending crashes vector in attemptToSendCrashes " + e.getClass().getName();
                return z;
            }
        } catch (Exception e4) {
            z = false;
            e = e4;
        }
    }

    public final boolean f() {
        boolean zBooleanValue;
        FutureTask futureTask = new FutureTask(new Callable() { // from class: com.crittercism.app.Crittercism.9
            private static Boolean a() {
                try {
                    Vector vectorE = Crittercism.a().o().e();
                    k kVarC = k.c();
                    kVarC.a(vectorE);
                    Crittercism.a().a(kVarC);
                } catch (Exception e) {
                    String str = "Exception in startExceptionSendingThreads boolean callable: " + e.getClass().getName();
                }
                try {
                    if (Crittercism.a().o().e().size() == 0) {
                        return true;
                    }
                } catch (Exception e2) {
                }
                return Boolean.valueOf(Crittercism.a().g());
            }

            @Override // java.util.concurrent.Callable
            public final /* synthetic */ Object call() {
                return a();
            }
        });
        try {
            Executors.newFixedThreadPool(10).execute(futureTask);
            zBooleanValue = false;
            while (!futureTask.isDone()) {
                zBooleanValue = ((Boolean) futureTask.get(2500L, TimeUnit.MILLISECONDS)).booleanValue();
            }
        } catch (Exception e) {
            zBooleanValue = false;
        }
        String str = "sentExceptions = " + (zBooleanValue ? "TRUE" : "FALSE");
        return zBooleanValue;
    }

    public final boolean g() {
        Exception e;
        boolean z;
        JSONObject jSONObject = new JSONObject();
        new JSONObject();
        try {
            jSONObject = this.l.b();
        } catch (Exception e2) {
        }
        try {
            JSONObject jSONObjectA = this.d.a(jSONObject);
            if (jSONObjectA.has("success") && jSONObjectA.getInt("success") == 1) {
                try {
                    k kVar = this.l;
                    kVar.h = this.l.e().size() + kVar.h;
                    this.l.f();
                    z = true;
                } catch (Exception e3) {
                    z = true;
                    e = e3;
                    String str = "Exception obtaining or handling response object or clearing pending exceptions vector in attemptToSendHandledExceptions " + e.getClass().getName();
                    return z;
                }
            } else {
                z = false;
            }
            try {
                this.l.g = new Date();
            } catch (Exception e4) {
                e = e4;
                String str2 = "Exception obtaining or handling response object or clearing pending exceptions vector in attemptToSendHandledExceptions " + e.getClass().getName();
            }
        } catch (Exception e5) {
            e = e5;
            z = false;
        }
        return z;
    }

    public final boolean h() {
        boolean zBooleanValue;
        FutureTask futureTask = new FutureTask(new Callable() { // from class: com.crittercism.app.Crittercism.10
            private static Boolean a() {
                try {
                    Crittercism.a().a(l.c());
                    String str = "pendingNdkCrashes.getPendingDataVector().size = " + Crittercism.a().p().e().size();
                } catch (Exception e) {
                    String str2 = "Exception in startNdkSendingThreads boolean callable: " + e.getClass().getName();
                    e.printStackTrace();
                }
                try {
                    if (Crittercism.a().p().e().size() == 0) {
                        return true;
                    }
                } catch (Exception e2) {
                }
                return Boolean.valueOf(Crittercism.a().i());
            }

            @Override // java.util.concurrent.Callable
            public final /* synthetic */ Object call() {
                return a();
            }
        });
        try {
            Executors.newFixedThreadPool(10).execute(futureTask);
            zBooleanValue = false;
            while (!futureTask.isDone()) {
                zBooleanValue = ((Boolean) futureTask.get(8000L, TimeUnit.MILLISECONDS)).booleanValue();
            }
        } catch (Exception e) {
            zBooleanValue = false;
        }
        String str = "sentNdkCrashes = " + (zBooleanValue ? "TRUE" : "FALSE");
        return zBooleanValue;
    }

    public final boolean i() {
        boolean z = false;
        JSONObject jSONObject = new JSONObject();
        new JSONObject();
        try {
            jSONObject = this.m.a();
        } catch (Exception e) {
        }
        try {
            JSONObject jSONObjectA = this.d.a(jSONObject);
            z = jSONObjectA.has("success") && jSONObjectA.getInt("success") == 1;
        } catch (Exception e2) {
            String str = "Exception obtaining or handling response object or clearing pending ndk filenames vector in attemptToSendNdkCrashes: " + e2.getClass().getName();
        }
        if (z) {
            try {
                this.m.b();
                this.m.f();
            } catch (Exception e3) {
                String str2 = "Exception removing ndk dump files from disk in attemptToSendNdkCrashes: " + e3.getClass().getName();
            }
        }
        return z;
    }

    public final String j() {
        if (this.d != null) {
            return this.d.a();
        }
        Log.w("Crittercism", "Failed to get app id.  Please contact us at support@crittercism.com.");
        return new String();
    }

    public final Context k() {
        return this.h;
    }

    public final b l() {
        return this.d;
    }

    public final i m() {
        return this.j;
    }

    public final m n() {
        return this.k;
    }

    public final k o() {
        return this.l;
    }

    public final l p() {
        return this.m;
    }

    public final String q() {
        try {
            if (this.t == null || this.t.equals("")) {
                this.t = this.h.getPackageName();
            }
        } catch (Exception e) {
            Log.w("Crittercism", "Call to getPackageName() failed.  Please contact us at support@crittercism.com.");
            this.t = new String();
        }
        return this.t;
    }

    public final String r() {
        return this.v;
    }

    public final int t() {
        try {
            return (int) ((this.d.f() * 10.0f) / 160.0f);
        } catch (Exception e) {
            return -1;
        }
    }
}
