package crittercism.android;

import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class e {
    public String a;
    public String b;
    private String c;
    private int d;

    public e() {
        this.c = "";
        this.a = "";
        this.d = 0;
        this.b = "";
    }

    private e(String str, int i, String str2) {
        this.c = "";
        this.a = "";
        this.d = 0;
        this.b = "";
        this.c = str;
        this.d = i;
        this.b = str2;
    }

    public static e a(JSONObject jSONObject) {
        try {
            e eVar = new e(jSONObject.getString("id"), jSONObject.has("votes_left") ? jSONObject.getInt("votes_left") : 0, jSONObject.has("username") ? jSONObject.getString("username") : "anonymous");
            try {
                if (!jSONObject.has("notify")) {
                    return eVar;
                }
                JSONObject jSONObject2 = jSONObject.getJSONObject("notify");
                if (!jSONObject2.has("text")) {
                    return eVar;
                }
                eVar.a = jSONObject2.getString("text");
                return eVar;
            } catch (JSONException e) {
                return eVar;
            }
        } catch (JSONException e2) {
            return null;
        }
    }
}
