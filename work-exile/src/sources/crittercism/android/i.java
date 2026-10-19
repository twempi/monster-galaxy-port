package crittercism.android;

import android.content.SharedPreferences;
import com.crittercism.app.Crittercism;
import com.sessionm.ui.SessionMActivity;
import java.util.Collection;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class i extends j {
    public i() {
        this.a = a.c.b;
        this.b = new Vector();
    }

    private static i a(JSONObject jSONObject) {
        JSONArray jSONArray;
        i iVar = new i();
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
            jSONArray = jSONObject2.has("app_loads") ? jSONObject2.getJSONArray("app_loads") : jSONArray2;
        } catch (Exception e2) {
            jSONArray = new JSONArray();
        }
        for (int i = 0; i < jSONArray.length(); i++) {
            try {
                iVar.a((Object) jSONArray.getJSONObject(i));
            } catch (Exception e3) {
                String str = "Exception in AppLoads.fromJSON: " + e3.getClass().getName();
            }
        }
        return iVar;
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
        return "critter_pendingapploads_" + str;
    }

    public static i d() {
        JSONObject jSONObject;
        i iVar = new i();
        JSONObject jSONObject2 = new JSONObject();
        try {
            SharedPreferences sharedPreferences = Crittercism.a().k().getSharedPreferences("com.crittercism.loads", 0);
            jSONObject = new JSONObject(sharedPreferences.getString(a(), new JSONObject().toString()));
            try {
                SharedPreferences.Editor editorEdit = sharedPreferences.edit();
                editorEdit.remove(a());
                if (!editorEdit.commit()) {
                    throw new Exception("failed to remove app loads from Shared Preferences");
                }
            } catch (Exception e) {
                e = e;
                String str = "Exception in AppLoads.readFromDisk(): " + e.getClass().getName();
            }
        } catch (Exception e2) {
            e = e2;
            jSONObject = jSONObject2;
        }
        try {
            return a(jSONObject);
        } catch (Exception e3) {
            return iVar;
        }
    }

    public final void b() {
        JSONObject jSONObject;
        JSONObject jSONObject2 = new JSONObject();
        new JSONObject();
        try {
            jSONObject = Crittercism.a().l().a(false);
            jSONObject.put("ts", Crittercism.u());
        } catch (Exception e) {
            jSONObject = new JSONObject();
        }
        try {
            jSONObject2.put("app_state", jSONObject);
            jSONObject2.put(SessionMActivity.INTENT_PARAM_TYPE, a.C0011a.e);
        } catch (Exception e2) {
            jSONObject2 = null;
        }
        if (jSONObject2 != null) {
            try {
                this.b.add(jSONObject2);
            } catch (Exception e3) {
            }
        }
    }

    public final JSONObject c() {
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
            jSONObject.put("app_loads", jSONArray);
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
}
