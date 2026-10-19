package com.kontagent;

import android.content.Context;
import android.content.SharedPreferences;

/* JADX INFO: loaded from: classes.dex */
class KontagentPrefs {
    private static final String FIRST_RUN_KEY = "firstRun";
    private static final String PREFS_NAME = "kontagent";
    private static final String REFERRER_SUBTYPE1_KEY = "referrer_st1";
    private static final String REFERRER_SUBTYPE2_KEY = "referrer_st2";
    private static final String REFERRER_SUBTYPE3_KEY = "referrer_st3";
    private static final String REFERRER_TYPE_KEY = "referrer_type";
    private static final String SENDER_ID_KEY = "senderId";
    private final Context mContext;

    public KontagentPrefs(Context context) {
        this.mContext = context;
    }

    public void resetPrefs() {
        getPreferences().edit().clear().commit();
    }

    public SharedPreferences getPreferences() {
        return this.mContext.getSharedPreferences(PREFS_NAME, 0);
    }

    public boolean isFirstRun() {
        SharedPreferences prefs = getPreferences();
        return prefs.getBoolean(FIRST_RUN_KEY, true);
    }

    public void setFirstRun(boolean firstRun) {
        SharedPreferences prefs = getPreferences();
        prefs.edit().putBoolean(FIRST_RUN_KEY, firstRun).commit();
    }

    public String getSenderId() {
        SharedPreferences prefs = getPreferences();
        return prefs.getString(SENDER_ID_KEY, null);
    }

    public void setSenderId(String id) {
        SharedPreferences prefs = getPreferences();
        prefs.edit().putString(SENDER_ID_KEY, id).commit();
    }

    public String getReferrerEventSubtype1() {
        SharedPreferences prefs = getPreferences();
        return prefs.getString(REFERRER_SUBTYPE1_KEY, null);
    }

    public String getReferrerEventSubtype2() {
        SharedPreferences prefs = getPreferences();
        return prefs.getString(REFERRER_SUBTYPE2_KEY, null);
    }

    public String getReferrerEventSubtype3() {
        SharedPreferences prefs = getPreferences();
        return prefs.getString(REFERRER_SUBTYPE3_KEY, null);
    }

    public void setReferrerEventSubtype1(String value) {
        SharedPreferences prefs = getPreferences();
        prefs.edit().putString(REFERRER_SUBTYPE1_KEY, value).commit();
    }

    public void setReferrerEventSubtype2(String value) {
        SharedPreferences prefs = getPreferences();
        prefs.edit().putString(REFERRER_SUBTYPE2_KEY, value).commit();
    }

    public void setReferrerEventSubtype3(String value) {
        SharedPreferences prefs = getPreferences();
        prefs.edit().putString(REFERRER_SUBTYPE3_KEY, value).commit();
    }

    public String getReferrerEventType() {
        SharedPreferences prefs = getPreferences();
        return prefs.getString(REFERRER_TYPE_KEY, null);
    }

    public void setReferrerEventType(String type) {
        SharedPreferences prefs = getPreferences();
        prefs.edit().putString(REFERRER_TYPE_KEY, type).commit();
    }
}
