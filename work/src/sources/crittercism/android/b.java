package crittercism.android;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Debug;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.WindowManager;
import com.crittercism.app.Crittercism;
import com.millennialmedia.android.MMAdView;
import com.tapjoy.TapjoyConstants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class b {
    private c d;
    private Context e;
    private String f;
    private String g;
    private String h;
    private String i;
    private int j;
    private final String c = d.b();
    final String a = "critter_did";
    final int b = 100;
    private boolean k = false;
    private String l = "";
    private JSONObject m = new JSONObject();
    private Object n = new Object();

    static class a implements Callable {
        private static boolean d = false;
        private static Object f;
        private String[] c;
        private Thread g;
        private C0015b h;
        private C0015b i;
        private StringBuilder a = new StringBuilder();
        private StringBuilder b = new StringBuilder();
        private Process e = null;

        public a(Object obj, Thread thread) {
            f = obj;
            this.g = thread;
            if (Build.VERSION.SDK_INT < 8) {
                this.c = new String[4];
                this.c[0] = "logcat";
                this.c[1] = "-d";
                this.c[2] = "-v";
                this.c[3] = "time";
                return;
            }
            this.c = new String[5];
            this.c[0] = "logcat";
            this.c[1] = "-t";
            this.c[2] = "100";
            this.c[3] = "-v";
            this.c[4] = "time";
        }

        public static boolean a() {
            return d;
        }

        public static void b() {
            d = true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        @Override // java.util.concurrent.Callable
        /* JADX INFO: renamed from: d, reason: merged with bridge method [inline-methods] */
        public StringBuilder call() {
            this.h = null;
            this.i = null;
            try {
                if (!d) {
                    this.e = Runtime.getRuntime().exec(this.c);
                    this.h = new C0015b(this.e.getInputStream());
                    this.i = new C0015b(this.e.getErrorStream());
                    this.h.start();
                    this.i.start();
                    this.e.waitFor();
                }
            } catch (Exception e) {
                d = true;
            } finally {
                if (this.h != null) {
                    this.a = this.h.a();
                }
                if (this.i != null) {
                    this.b = this.i.a();
                }
                if (this.e != null) {
                    try {
                        this.e.getInputStream().close();
                        this.e.getErrorStream().close();
                        this.e.getOutputStream().close();
                        this.e.destroy();
                    } catch (Exception e2) {
                    }
                }
            }
            return this.a;
        }

        public final void c() {
            synchronized (f) {
                try {
                    this.h.b();
                    this.i.b();
                    if (this.e != null) {
                        this.e.getInputStream().close();
                        this.e.getErrorStream().close();
                        this.e.getOutputStream().close();
                    }
                } catch (Exception e) {
                }
                try {
                    if (this.e != null) {
                        this.e.destroy();
                    }
                } catch (Exception e2) {
                }
            }
        }
    }

    /* JADX INFO: renamed from: crittercism.android.b$b, reason: collision with other inner class name */
    static class C0015b extends Thread {
        private InputStream a;
        private StringBuilder b = new StringBuilder();
        private BufferedReader c = null;

        public C0015b(InputStream inputStream) {
            this.a = inputStream;
        }

        public final StringBuilder a() {
            return this.b;
        }

        public final void b() {
            if (this.c != null) {
                try {
                    this.c.close();
                } catch (Exception e) {
                    this.c = null;
                }
            }
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public final void run() {
            new String();
            this.c = new BufferedReader(new InputStreamReader(this.a));
            while (true) {
                try {
                    String line = this.c.readLine();
                    if (line == null) {
                        try {
                            this.c.close();
                            return;
                        } catch (Exception e) {
                            String str = "CrittercismAPI.StreamThread$makeLogcatJsonArray: ERROR closing bufferedReader!!! " + e.getClass().getName();
                            return;
                        }
                    }
                    this.b.append(line);
                    this.b.append("\n");
                } catch (Exception e2) {
                    try {
                        this.c.close();
                        return;
                    } catch (Exception e3) {
                        String str2 = "CrittercismAPI.StreamThread$makeLogcatJsonArray: ERROR closing bufferedReader!!! " + e3.getClass().getName();
                        return;
                    }
                } catch (Throwable th) {
                    try {
                        this.c.close();
                    } catch (Exception e4) {
                        String str3 = "CrittercismAPI.StreamThread$makeLogcatJsonArray: ERROR closing bufferedReader!!! " + e4.getClass().getName();
                    }
                    throw th;
                }
            }
        }
    }

    public b(Context context, String str, String str2, String str3, int i) {
        this.d = null;
        this.e = null;
        this.f = "";
        this.g = "";
        this.h = "";
        this.i = "";
        this.j = 0;
        this.e = context;
        this.d = new c(this.c, this.e);
        this.f = str;
        this.g = null;
        this.h = str2;
        this.i = str3;
        this.j = i;
    }

    private static String b(String str) {
        if (str != null && !str.equals("")) {
            try {
                str = new String(new BigInteger(1, MessageDigest.getInstance("SHA-256").digest(str.getBytes())).toString(16));
            } catch (NoSuchAlgorithmException e) {
                str = null;
            }
        }
        if (str.equals("")) {
            return null;
        }
        return str;
    }

    private int c(String str) {
        try {
            return this.e.getPackageManager().checkPermission(str, this.e.getPackageName());
        } catch (Exception e) {
            return -1;
        }
    }

    private double j() {
        try {
            Intent intentRegisterReceiver = this.e.getApplicationContext().registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            int intExtra = intentRegisterReceiver.getIntExtra("level", -1);
            double intExtra2 = intentRegisterReceiver.getIntExtra("scale", -1);
            if (intExtra < 0 || intExtra2 <= 0.0d) {
                return 1.0d;
            }
            return ((double) intExtra) / intExtra2;
        } catch (Exception e) {
            return 1.0d;
        }
    }

    private static long k() {
        int i = -1;
        try {
            Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
            Debug.getMemoryInfo(memoryInfo);
            i = (memoryInfo.otherPss + memoryInfo.dalvikPss + memoryInfo.nativePss) * 1024;
        } catch (Exception e) {
        }
        return i;
    }

    private String l() {
        try {
            return ((TelephonyManager) this.e.getSystemService("phone")).getNetworkOperatorName();
        } catch (Exception e) {
            return Build.BRAND;
        }
    }

    private JSONObject m() {
        JSONObject jSONObject = new JSONObject();
        try {
            NetworkInfo networkInfo = ((ConnectivityManager) this.e.getSystemService("connectivity")).getNetworkInfo(1);
            jSONObject.put("available", networkInfo.isAvailable());
            jSONObject.put("connected", networkInfo.isConnected());
            if (!networkInfo.isConnected()) {
                jSONObject.put("connecting", networkInfo.isConnectedOrConnecting());
            }
            jSONObject.put("failover", networkInfo.isFailover());
        } catch (Exception e) {
            b.class.getCanonicalName();
            e.toString();
        }
        return jSONObject;
    }

    private JSONObject n() {
        JSONObject jSONObject = new JSONObject();
        try {
            NetworkInfo networkInfo = ((ConnectivityManager) this.e.getSystemService("connectivity")).getNetworkInfo(0);
            jSONObject.put("available", networkInfo.isAvailable());
            jSONObject.put("connected", networkInfo.isConnected());
            if (!networkInfo.isConnected()) {
                jSONObject.put("connecting", networkInfo.isConnectedOrConnecting());
            }
            jSONObject.put("failover", networkInfo.isFailover());
            jSONObject.put("roaming", networkInfo.isRoaming());
        } catch (Exception e) {
            b.class.getCanonicalName();
            String str = e.toString() + " in getMobileNetworkStatus";
        }
        return jSONObject;
    }

    private String o() {
        try {
            if (c("android.permission.GET_TASKS") == 0) {
                List<ActivityManager.RunningTaskInfo> runningTasks = ((ActivityManager) this.e.getSystemService("activity")).getRunningTasks(1);
                String str = "CURRENT Activity ::" + runningTasks.get(0).topActivity.getClassName();
                return runningTasks.get(0).topActivity.flattenToShortString().replace("/", "");
            }
        } catch (Exception e) {
        }
        return "";
    }

    private JSONArray p() {
        StringBuilder sb;
        JSONArray jSONArray = new JSONArray();
        StringBuilder sb2 = new StringBuilder();
        synchronized (this.n) {
            ExecutorService executorServiceNewCachedThreadPool = Executors.newCachedThreadPool();
            a aVar = new a(this.n, Thread.currentThread());
            Future futureSubmit = executorServiceNewCachedThreadPool.submit(aVar);
            try {
                try {
                    sb = !a.a() ? (StringBuilder) futureSubmit.get(5L, TimeUnit.SECONDS) : sb2;
                    futureSubmit.cancel(true);
                    executorServiceNewCachedThreadPool.shutdownNow();
                } catch (Exception e) {
                    a.b();
                    aVar.c();
                    try {
                        Thread.sleep(200L);
                    } catch (InterruptedException e2) {
                    }
                    futureSubmit.cancel(true);
                    executorServiceNewCachedThreadPool.shutdownNow();
                    sb = sb2;
                }
            } catch (Throwable th) {
                futureSubmit.cancel(true);
                executorServiceNewCachedThreadPool.shutdownNow();
                throw th;
            }
        }
        if (sb.toString().length() > 0) {
            String[] strArrSplit = sb.toString().split("\n");
            for (String str : strArrSplit) {
                jSONArray.put(str);
            }
        }
        int length = jSONArray.length();
        if (length <= 100) {
            return jSONArray;
        }
        JSONArray jSONArray2 = new JSONArray();
        for (int i = length - 100; i < length; i++) {
            try {
                jSONArray2.put(jSONArray.getString(i));
            } catch (JSONException e3) {
                String str2 = "Caught exception in second try-catch of getLogCat():" + e3.getClass().getName();
            }
        }
        return jSONArray2;
    }

    private float q() {
        try {
            return this.e.getResources().getDisplayMetrics().ydpi;
        } catch (Exception e) {
            return 0.0f;
        }
    }

    private boolean r() {
        int i = 30;
        while (!this.k && i > 0) {
            try {
                i--;
                Thread.sleep(1000L);
            } catch (Exception e) {
            }
        }
        return this.k;
    }

    public final String a() {
        return this.f;
    }

    public final JSONObject a(JSONObject jSONObject) throws JSONException, f, IOException {
        String str;
        JSONObject jSONObject2;
        String[] strArr;
        String string = null;
        String str2 = new String();
        JSONObject jSONObject3 = new JSONObject();
        JSONObject jSONObject4 = new JSONObject();
        new String();
        try {
            jSONObject4.put("success", 0);
            str2 = jSONObject.getString("requestUrl");
            str = str2;
            jSONObject2 = jSONObject.getJSONObject("requestData");
        } catch (JSONException e) {
            e.printStackTrace();
            str = str2;
            jSONObject2 = jSONObject3;
        } catch (Exception e2) {
            e2.printStackTrace();
            str = str2;
            jSONObject2 = jSONObject3;
        }
        try {
            if (jSONObject2.has("filenames") && jSONObject2.has("filename_prefix")) {
                new JSONArray();
                JSONArray jSONArray = jSONObject2.getJSONArray("filenames");
                strArr = new String[jSONArray.length()];
                for (int i = 0; i < jSONArray.length(); i++) {
                    strArr[i] = jSONArray.getString(i);
                }
                string = jSONObject2.getString("filename_prefix");
                jSONObject2.remove("filenames");
                jSONObject2.remove("filename_prefix");
            } else {
                strArr = null;
            }
            try {
                String strA = (strArr == null || string == null) ? this.d.a(str, jSONObject2) : this.d.a(str, jSONObject2, strArr, string);
                if (strA == null || strA.equals("")) {
                    return jSONObject4;
                }
                JSONObject jSONObject5 = new JSONObject(strA);
                String str3 = "POSTING JSON DATA: url = " + str + "\tdata = " + jSONObject2.toString();
                String str4 = "POSTING JSON DATA: response = " + jSONObject5.toString();
                return jSONObject5;
            } catch (f e3) {
                throw e3;
            } catch (IOException e4) {
                throw e4;
            } catch (Exception e5) {
                String str5 = "Exception in postJsonDataNew: " + e5.getClass().getName();
                JSONObject jSONObject6 = new JSONObject();
                try {
                    jSONObject6.put("success", 0);
                    return jSONObject6;
                } catch (Exception e6) {
                    return jSONObject6;
                }
            }
        } catch (Exception e7) {
            e7.printStackTrace();
            return jSONObject4;
        }
    }

    public final JSONObject a(boolean... zArr) {
        Exception exc;
        JSONObject jSONObject;
        int i = 1;
        boolean z = zArr.length > 0 ? zArr[0] : true;
        boolean z2 = zArr.length > 1 ? zArr[1] : false;
        JSONObject jSONObject2 = new JSONObject();
        try {
            JSONObject jSONObjectD = d();
            try {
                jSONObjectD.put("battery_level", j());
                jSONObjectD.put("memory_usage", k());
                ActivityManager activityManager = (ActivityManager) this.e.getSystemService("activity");
                ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
                activityManager.getMemoryInfo(memoryInfo);
                jSONObjectD.put("low_memory", memoryInfo.lowMemory ? 1 : 0);
                if (c("android.permission.ACCESS_NETWORK_STATE") == 0) {
                    jSONObjectD.put("wifi", m());
                    jSONObjectD.put("mobile_network", n());
                }
                jSONObjectD.put("disk_space_free", h.a().toString());
                jSONObjectD.put("disk_space_total", h.b().toString());
                jSONObjectD.put("sd_space_free", h.c().toString());
                jSONObjectD.put("sd_space_total", h.d().toString());
                int i2 = this.e.getResources().getConfiguration().orientation;
                if (i2 == 0) {
                    Display defaultDisplay = ((WindowManager) this.e.getSystemService("window")).getDefaultDisplay();
                    if (defaultDisplay.getWidth() == defaultDisplay.getHeight()) {
                        i = 3;
                    } else if (defaultDisplay.getWidth() > defaultDisplay.getHeight()) {
                        i = 2;
                    }
                } else {
                    i = i2;
                }
                jSONObjectD.put(MMAdView.KEY_ORIENTATION, i);
                jSONObjectD.put("activity", o());
                if (z2) {
                    jSONObjectD.put("metadata", this.m);
                }
                if (z) {
                    if (c("android.permission.READ_LOGS") == 0) {
                        JSONArray jSONArrayP = p();
                        if (jSONArrayP.length() > 0) {
                            try {
                                jSONObjectD.put("logcat", jSONArrayP);
                                return jSONObjectD;
                            } catch (Exception e) {
                                String str = "put logcat EXCEPTION: " + e.getClass().getName();
                            }
                        }
                        return jSONObjectD;
                    }
                    if (Build.VERSION.SDK_INT >= 16 && Crittercism.s()) {
                        JSONArray jSONArrayP2 = p();
                        if (jSONArrayP2.length() > 0) {
                            try {
                                jSONObjectD.put("logcat", jSONArrayP2);
                                return jSONObjectD;
                            } catch (Exception e2) {
                                String str2 = "put logcat EXCEPTION: " + e2.getClass().getName();
                            }
                        }
                    }
                    exc = e;
                    jSONObject = jSONObjectD;
                    String str3 = "Exception with getStateInfo(): " + exc.getClass().getName();
                    return jSONObject;
                }
                return jSONObjectD;
            } catch (Exception e3) {
                exc = e3;
                jSONObject = jSONObjectD;
            }
        } catch (Exception e4) {
            exc = e4;
            jSONObject = jSONObject2;
        }
    }

    public final void a(String str) {
        this.g = str;
    }

    public final String b() {
        String strB;
        String string = Settings.Secure.getString(this.e.getContentResolver(), TapjoyConstants.TJC_ANDROID_ID);
        if (string == null || string.equals("") || "9774d56d682e549c".equals(string)) {
            strB = null;
        } else {
            try {
                UUID uuidNameUUIDFromBytes = UUID.nameUUIDFromBytes(string.getBytes("utf8"));
                strB = uuidNameUUIDFromBytes != null ? uuidNameUUIDFromBytes.toString() : null;
            } catch (UnsupportedEncodingException e) {
            }
            if (strB != null && strB.equals("")) {
                strB = null;
            }
        }
        if (strB == null && this.e.getPackageManager().checkPermission("android.permission.READ_PHONE_STATE", Crittercism.a().q()) == 0) {
            strB = b(((TelephonyManager) this.e.getSystemService("phone")).getDeviceId());
        }
        return strB == null ? UUID.randomUUID().toString() : strB;
    }

    /* JADX WARN: Code duplicated, block: B:27:0x006e  */
    public final synchronized boolean b(JSONObject jSONObject) {
        boolean z;
        synchronized (this) {
            try {
                try {
                    if (jSONObject.has("username")) {
                        this.l = jSONObject.getString("username");
                    } else if (!this.l.equals("")) {
                        jSONObject.put("username", this.l);
                    }
                    if (r()) {
                        JSONObject jSONObject2 = new JSONObject();
                        new JSONObject();
                        JSONObject jSONObjectC = c();
                        jSONObjectC.put("metadata", jSONObject);
                        jSONObject2.put("requestUrl", crittercism.android.a.c.e);
                        jSONObject2.put("requestData", jSONObjectC);
                        JSONObject jSONObjectA = a(jSONObject2);
                        z = jSONObjectA.has("success") && jSONObjectA.getInt("success") == 1;
                    }
                } catch (Exception e) {
                    b.class.getCanonicalName();
                    e.toString();
                }
            } catch (f e2) {
                throw e2;
            }
        }
        return z;
    }

    public final JSONObject c() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(TapjoyConstants.TJC_APP_ID_NAME, this.f);
            if (this.g == null) {
                this.g = b();
            }
            jSONObject.put("hashed_device_id", this.g);
            jSONObject.put(TapjoyConstants.TJC_DEVICE_NAME, TapjoyConstants.TJC_DEVICE_PLATFORM_TYPE);
            jSONObject.put(TapjoyConstants.TJC_CONNECT_LIBRARY_VERSION_NAME, this.h);
        } catch (JSONException e) {
        } catch (Exception e2) {
            String str = "Exception in getRequiredParams(): " + e2.getClass().getName();
        }
        return jSONObject;
    }

    public final void c(JSONObject jSONObject) {
        this.m = jSONObject;
    }

    public final JSONObject d() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("arch", System.getProperty("os.arch"));
            jSONObject.put("locale", crittercism.android.a.b(this.e));
            jSONObject.put("dpi", e());
            jSONObject.put("xdpi", f());
            jSONObject.put("ydpi", q());
            jSONObject.put("name", "");
            jSONObject.put("system", TapjoyConstants.TJC_DEVICE_PLATFORM_TYPE);
            jSONObject.put("model", Build.MODEL);
            jSONObject.put("carrier", l());
            jSONObject.put(TapjoyConstants.TJC_APP_VERSION_NAME, this.i);
            jSONObject.put("system_version", Build.VERSION.RELEASE);
            jSONObject.put("app_version_code", this.j);
            return jSONObject;
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    public final float e() {
        try {
            return this.e.getResources().getDisplayMetrics().density;
        } catch (Exception e) {
            return 1.0f;
        }
    }

    public final float f() {
        try {
            return this.e.getResources().getDisplayMetrics().xdpi;
        } catch (Exception e) {
            return 0.0f;
        }
    }

    public final String g() {
        new JSONObject();
        new JSONObject();
        String str = new String();
        try {
            JSONObject jSONObjectC = c();
            jSONObjectC.put("pkg", this.e.getPackageName());
            str = this.d.a(crittercism.android.a.c.f, jSONObjectC);
            if (str != null && !str.equals("")) {
                JSONObject jSONObject = new JSONObject(str);
                if (!jSONObject.has("success")) {
                    String str2 = crittercism.android.a.c.f + " response: " + str;
                } else if (jSONObject.getInt("success") == 1) {
                    String str3 = "app_id: " + jSONObject.getString(TapjoyConstants.TJC_APP_ID_NAME);
                    String str4 = "package name: " + jSONObject.getString("pkg");
                    String str5 = "updated settings: " + jSONObject.getJSONObject("updated_settings");
                }
            }
        } catch (f e) {
        } catch (IOException e2) {
        } catch (JSONException e3) {
        } catch (Exception e4) {
            String str6 = "sendPackageName: Exception! " + e4.getClass().getName();
        }
        return str;
    }

    public final void h() {
        this.k = true;
    }

    public final JSONObject i() {
        return this.m;
    }
}
