package com.sessionm.json;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class JSONObject {
    private final org.json.JSONObject jsonObject;

    private JSONObject(org.json.JSONObject jSONObject) {
        this.jsonObject = jSONObject;
    }

    public static JSONObject create(String str) {
        JSONObject jSONObject;
        if (str == null) {
            return null;
        }
        try {
            jSONObject = new JSONObject(new org.json.JSONObject(str));
        } catch (JSONException e) {
            jSONObject = null;
        }
        return jSONObject;
    }

    public static JSONObject create() {
        return new JSONObject(new org.json.JSONObject());
    }

    public String getString(String str) {
        return this.jsonObject.optString(str);
    }

    public JSONObject getJSONObject(String str) {
        try {
            return new JSONObject(this.jsonObject.getJSONObject(str));
        } catch (JSONException e) {
            return null;
        }
    }

    public Integer getInteger(String str) {
        Object objOpt = this.jsonObject.opt(str);
        if (!(objOpt instanceof Integer)) {
            return null;
        }
        return (Integer) objOpt;
    }

    public int getInt(String str) {
        return this.jsonObject.optInt(str);
    }

    public <T> T[] getArray(String str, Class<T> cls) {
        try {
            return (T[]) toArray(this.jsonObject.getJSONArray(str), cls);
        } catch (JSONException e) {
            return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <T> T[] toArray(JSONArray jSONArray, Class<T> cls) {
        try {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < jSONArray.length(); i++) {
                Object jSONObject = jSONArray.get(i);
                if (cls == getClass() && (jSONObject instanceof org.json.JSONObject)) {
                    jSONObject = new JSONObject((org.json.JSONObject) jSONObject);
                }
                arrayList.add(jSONObject);
            }
            return (T[]) arrayList.toArray((Object[]) Array.newInstance((Class<?>) cls, 0));
        } catch (Throwable th) {
            return null;
        }
    }

    public boolean getBoolean(String str) {
        return this.jsonObject.optBoolean(str);
    }

    public Object getObject(String str) {
        return this.jsonObject.opt(str);
    }

    public boolean has(String str) {
        return this.jsonObject.has(str);
    }

    public void put(String str, String str2) {
        try {
            this.jsonObject.put(str, str2);
        } catch (JSONException e) {
        }
    }

    public void put(String str, Object obj) {
        try {
            if (obj instanceof JSONObject) {
                this.jsonObject.put(str, ((JSONObject) obj).toJSONObject());
            } else {
                this.jsonObject.put(str, obj);
            }
        } catch (JSONException e) {
        }
    }

    public void put(String str, int i) {
        try {
            this.jsonObject.put(str, i);
        } catch (JSONException e) {
        }
    }

    public void put(String str, boolean z) {
        try {
            this.jsonObject.put(str, z);
        } catch (JSONException e) {
        }
    }

    public <T> void put(Map<String, T> map) {
        if (map != null) {
            for (Map.Entry<String, T> entry : map.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    public String[] names() {
        return (String[]) toArray(this.jsonObject.names(), String.class);
    }

    public org.json.JSONObject toJSONObject() {
        return this.jsonObject;
    }

    public String toString() {
        return this.jsonObject.toString();
    }
}
