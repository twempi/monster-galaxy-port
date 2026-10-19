package com.kontagent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class InstallReceiver extends BroadcastReceiver {
    protected static final String ACTION_INSTALL_REFERRER = "com.android.vending.INSTALL_REFERRER";
    protected static final String DEFAULT_SUBTYPE1_KEY = "st1";
    protected static final String DEFAULT_SUBTYPE2_KEY = "st2";
    protected static final String DEFAULT_SUBTYPE3_KEY = "st3";
    protected static final String DEFAULT_TYPE_KEY = "tu";
    protected static final String EVENT_TYPE_AD = "ad";
    protected static final String EVENT_TYPE_PARTNER = "partner";
    protected static final String EXTRA_REFERRER = "referrer";

    protected enum ReferrerEventType {
        Ad,
        Partner
    }

    public static Map<String, String> parseReferrerArguments(String referrerArgs) {
        if (referrerArgs == null) {
            return new HashMap();
        }
        String decoded = URLDecoder.decode(referrerArgs).trim();
        if (decoded.length() == 0) {
            return new HashMap();
        }
        String[] args = decoded.split("&");
        HashMap<String, String> result = new HashMap<>();
        for (String arg : args) {
            String[] parts = arg.split("=");
            String key = parts[0];
            String value = null;
            if (parts.length > 1) {
                value = parts[1];
            }
            result.put(key, value);
        }
        return result;
    }

    public void setReferrerEventSubtype1(Context context, String value) {
        KontagentPrefs prefs = new KontagentPrefs(context);
        prefs.setReferrerEventSubtype1(value);
    }

    public void setReferrerEventSubtype2(Context context, String value) {
        KontagentPrefs prefs = new KontagentPrefs(context);
        prefs.setReferrerEventSubtype2(value);
    }

    public void setReferrerEventSubtype3(Context context, String value) {
        KontagentPrefs prefs = new KontagentPrefs(context);
        prefs.setReferrerEventSubtype3(value);
    }

    public void setReferrerEventType(Context context, ReferrerEventType eventType) {
        KontagentPrefs prefs = new KontagentPrefs(context);
        switch (eventType) {
            case Ad:
                prefs.setReferrerEventType(EVENT_TYPE_AD);
                break;
            case Partner:
                prefs.setReferrerEventType(EVENT_TYPE_PARTNER);
                break;
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (ACTION_INSTALL_REFERRER.equals(intent.getAction())) {
            String referrer = intent.getStringExtra(EXTRA_REFERRER);
            KontagentLog.i("Installation detected. Referrer: " + referrer);
            Map<String, String> referrerArgs = parseReferrerArguments(referrer);
            for (String key : referrerArgs.keySet()) {
                KontagentLog.i("\t" + key + " = " + referrerArgs.get(key));
            }
            onReferralEvent(context, intent, referrerArgs);
            dump(context);
        }
    }

    public void onReferralEvent(Context context, Intent intent, Map<String, String> eventArgs) {
        if (eventArgs.containsKey(DEFAULT_TYPE_KEY)) {
            String type = eventArgs.get(DEFAULT_TYPE_KEY);
            if (EVENT_TYPE_AD.equals(type) || EVENT_TYPE_PARTNER.equals(type)) {
                KontagentPrefs prefs = new KontagentPrefs(context);
                prefs.setReferrerEventType(type);
            }
        }
        if (eventArgs.containsKey(DEFAULT_SUBTYPE1_KEY)) {
            setReferrerEventSubtype1(context, eventArgs.get(DEFAULT_SUBTYPE1_KEY));
        }
        if (eventArgs.containsKey(DEFAULT_SUBTYPE2_KEY)) {
            setReferrerEventSubtype2(context, eventArgs.get(DEFAULT_SUBTYPE2_KEY));
        }
        if (eventArgs.containsKey(DEFAULT_SUBTYPE3_KEY)) {
            setReferrerEventSubtype3(context, eventArgs.get(DEFAULT_SUBTYPE3_KEY));
        }
    }

    protected void dump(Context context) {
        KontagentPrefs prefs = new KontagentPrefs(context);
        String type = prefs.getReferrerEventType();
        String st1 = prefs.getReferrerEventSubtype1();
        String st2 = prefs.getReferrerEventSubtype2();
        String st3 = prefs.getReferrerEventSubtype3();
        KontagentLog.d("Kontagent referrer arguments: ");
        KontagentLog.d("\ttu=" + type);
        KontagentLog.d("\tst1=" + st1);
        KontagentLog.d("\tst2=" + st2);
        KontagentLog.d("\tst2=" + st3);
    }
}
