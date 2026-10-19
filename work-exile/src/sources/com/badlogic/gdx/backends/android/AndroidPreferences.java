package com.badlogic.gdx.backends.android;

import android.content.SharedPreferences;
import com.badlogic.gdx.Preferences;
import java.util.Map;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class AndroidPreferences implements Preferences {
    SharedPreferences sharedPrefs;

    public AndroidPreferences(SharedPreferences preferences) {
        this.sharedPrefs = preferences;
    }

    @Override // com.badlogic.gdx.Preferences
    public void putBoolean(String key, boolean val) {
        SharedPreferences.Editor edit = this.sharedPrefs.edit();
        edit.putBoolean(key, val);
        edit.commit();
    }

    @Override // com.badlogic.gdx.Preferences
    public void putInteger(String key, int val) {
        SharedPreferences.Editor edit = this.sharedPrefs.edit();
        edit.putInt(key, val);
        edit.commit();
    }

    @Override // com.badlogic.gdx.Preferences
    public void putLong(String key, long val) {
        SharedPreferences.Editor edit = this.sharedPrefs.edit();
        edit.putLong(key, val);
        edit.commit();
    }

    @Override // com.badlogic.gdx.Preferences
    public void putFloat(String key, float val) {
        SharedPreferences.Editor edit = this.sharedPrefs.edit();
        edit.putFloat(key, val);
        edit.commit();
    }

    @Override // com.badlogic.gdx.Preferences
    public void putString(String key, String val) {
        SharedPreferences.Editor edit = this.sharedPrefs.edit();
        edit.putString(key, val);
        edit.commit();
    }

    @Override // com.badlogic.gdx.Preferences
    public void put(Map<String, ?> vals) {
        SharedPreferences.Editor edit = this.sharedPrefs.edit();
        for (Map.Entry<String, ?> val : vals.entrySet()) {
            if (val.getValue() instanceof Boolean) {
                putBoolean(val.getKey(), ((Boolean) val.getValue()).booleanValue());
            }
            if (val.getValue() instanceof Integer) {
                putInteger(val.getKey(), ((Integer) val.getValue()).intValue());
            }
            if (val.getValue() instanceof Long) {
                putLong(val.getKey(), ((Long) val.getValue()).longValue());
            }
            if (val.getValue() instanceof String) {
                putString(val.getKey(), (String) val.getValue());
            }
            if (val.getValue() instanceof Float) {
                putFloat(val.getKey(), ((Float) val.getValue()).floatValue());
            }
        }
        edit.commit();
    }

    @Override // com.badlogic.gdx.Preferences
    public boolean getBoolean(String key) {
        return this.sharedPrefs.getBoolean(key, false);
    }

    @Override // com.badlogic.gdx.Preferences
    public int getInteger(String key) {
        return this.sharedPrefs.getInt(key, 0);
    }

    @Override // com.badlogic.gdx.Preferences
    public long getLong(String key) {
        return this.sharedPrefs.getLong(key, 0L);
    }

    @Override // com.badlogic.gdx.Preferences
    public float getFloat(String key) {
        return this.sharedPrefs.getFloat(key, 0.0f);
    }

    @Override // com.badlogic.gdx.Preferences
    public String getString(String key) {
        return this.sharedPrefs.getString(key, "");
    }

    @Override // com.badlogic.gdx.Preferences
    public boolean getBoolean(String key, boolean defValue) {
        return this.sharedPrefs.getBoolean(key, defValue);
    }

    @Override // com.badlogic.gdx.Preferences
    public int getInteger(String key, int defValue) {
        return this.sharedPrefs.getInt(key, defValue);
    }

    @Override // com.badlogic.gdx.Preferences
    public long getLong(String key, long defValue) {
        return this.sharedPrefs.getLong(key, defValue);
    }

    @Override // com.badlogic.gdx.Preferences
    public float getFloat(String key, float defValue) {
        return this.sharedPrefs.getFloat(key, defValue);
    }

    @Override // com.badlogic.gdx.Preferences
    public String getString(String key, String defValue) {
        return this.sharedPrefs.getString(key, defValue);
    }

    @Override // com.badlogic.gdx.Preferences
    public Map<String, ?> get() {
        return this.sharedPrefs.getAll();
    }

    @Override // com.badlogic.gdx.Preferences
    public boolean contains(String key) {
        return this.sharedPrefs.contains(key);
    }

    @Override // com.badlogic.gdx.Preferences
    public void clear() {
        SharedPreferences.Editor edit = this.sharedPrefs.edit();
        edit.clear();
        edit.commit();
    }

    @Override // com.badlogic.gdx.Preferences
    public void flush() {
    }
}
