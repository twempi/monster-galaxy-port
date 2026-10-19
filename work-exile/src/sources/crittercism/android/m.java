package crittercism.android;

import android.content.SharedPreferences;
import com.crittercism.app.Crittercism;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Map;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class m extends j {
    public String c = new String();
    public JSONArray d = new JSONArray();
    public JSONArray e = new JSONArray();
    public JSONObject f = new JSONObject();

    public m() {
        this.a = a.c.d;
        this.b = new Vector();
    }

    private static m a(JSONObject jSONObject) {
        JSONArray jSONArray;
        m mVar = new m();
        JSONObject jSONObject2 = new JSONObject();
        JSONArray jSONArray2 = new JSONArray();
        try {
            if (jSONObject.has("requestData")) {
                jSONObject2 = jSONObject.getJSONObject("requestData");
            }
        } catch (Exception e) {
            jSONObject2 = new JSONObject();
        }
        try {
            jSONArray = jSONObject2.has("crashes") ? jSONObject2.getJSONArray("crashes") : jSONArray2;
        } catch (Exception e2) {
            jSONArray = new JSONArray();
        }
        for (int i = 0; i < jSONArray.length(); i++) {
            try {
                mVar.a((Object) jSONArray.getJSONObject(i));
            } catch (Exception e3) {
                String str = "Exception in SdkCrashes.fromJSON: " + e3.getClass().getName();
            }
        }
        return mVar;
    }

    public static String a() {
        String str = new String();
        if (Crittercism.a() != null) {
            try {
                str = Crittercism.a().j();
            } catch (Exception e) {
                str = new String();
            }
        }
        return "critter_pendingcrashes_" + str;
    }

    public static m c() {
        JSONObject jSONObject;
        m mVar = new m();
        JSONObject jSONObject2 = new JSONObject();
        try {
            SharedPreferences sharedPreferences = Crittercism.a().k().getSharedPreferences("com.crittercism.crashes", 0);
            jSONObject = new JSONObject(sharedPreferences.getString(a(), new JSONObject().toString()));
            try {
                SharedPreferences.Editor editorEdit = sharedPreferences.edit();
                editorEdit.remove(a());
                if (!editorEdit.commit()) {
                    throw new Exception("failed to remove crashes from Shared Preferences");
                }
            } catch (Exception e) {
                e = e;
                String str = "Exception in SdkCrashes.readFromDisk(): " + e.getClass().getName();
            }
        } catch (Exception e2) {
            e = e2;
            jSONObject = jSONObject2;
        }
        try {
            return a(jSONObject);
        } catch (Exception e3) {
            return mVar;
        }
    }

    public final void a(Throwable th) {
        int iIndexOf = th.toString().indexOf(":");
        this.c = iIndexOf >= 0 ? th.toString().substring(0, iIndexOf) : th.getClass().getName();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        th.printStackTrace(printWriter);
        for (Throwable cause = th.getCause(); cause != null; cause = cause.getCause()) {
            this.c = cause.getClass().getName();
            cause.printStackTrace(printWriter);
        }
        String string = stringWriter.toString();
        this.d = new JSONArray();
        String[] strArrSplit = string.split("\n");
        for (String str : strArrSplit) {
            this.d.put(str);
        }
    }

    public final JSONObject b() {
        JSONArray jSONArray;
        JSONObject jSONObject;
        JSONObject jSONObject2 = new JSONObject();
        new JSONObject();
        new JSONArray();
        try {
            jSONArray = new JSONArray((Collection) this.b);
        } catch (Exception e) {
            jSONArray = new JSONArray();
        }
        try {
            jSONObject = Crittercism.a().l().c();
            jSONObject.put("crashes", jSONArray);
        } catch (Exception e2) {
            jSONObject = new JSONObject();
        }
        try {
            jSONObject2.put("requestUrl", this.a);
            jSONObject2.put("requestData", jSONObject);
            return jSONObject2;
        } catch (Exception e3) {
            return new JSONObject();
        }
    }

    public final void d() {
        for (Map.Entry<Thread, StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet()) {
            JSONObject jSONObject = new JSONObject();
            Thread key = entry.getKey();
            try {
                if (key.getId() != Thread.currentThread().getId()) {
                    jSONObject.put("name", key.getName());
                    jSONObject.put("id", key.getId());
                    jSONObject.put("state", key.getState().name());
                    JSONArray jSONArray = new JSONArray();
                    for (StackTraceElement stackTraceElement : entry.getValue()) {
                        String str = "  " + stackTraceElement;
                        jSONArray.put(stackTraceElement);
                    }
                    jSONObject.put("stacktrace", jSONArray);
                    this.e.put(jSONObject);
                }
            } catch (Exception e) {
                String str2 = "Problem with setBackgroundThreads(): " + e.getClass().getName();
            }
        }
    }
}
