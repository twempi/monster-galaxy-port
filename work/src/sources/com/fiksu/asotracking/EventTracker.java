package com.fiksu.asotracking;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Date;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class EventTracker {
    static final String SHARED_PREFERENCES_LOCK = "shared preferences lock";
    private static Context mCachedContext = null;
    protected Context mContext;
    private final HashMap<String, String> mParameters = new HashMap<>();

    public EventTracker(Context context, String event) {
        this.mContext = null;
        this.mParameters.put("event", event);
        if (context != null) {
            mCachedContext = context;
            this.mContext = context;
        } else {
            this.mContext = mCachedContext;
        }
    }

    protected void addParameter(String name, String value) {
        this.mParameters.put(name, value);
    }

    private HashMap<String, String> copyOfParams() {
        HashMap<String, String> newParams = new HashMap<>();
        for (String key : this.mParameters.keySet()) {
            newParams.put(key, this.mParameters.get(key));
        }
        return newParams;
    }

    protected void uploadEvent() {
        new Thread(new EventUploader(this.mContext, copyOfParams())).start();
    }

    protected void uploadEventSynchronously(long timeoutMs) {
        EventUploader uploader = new EventUploader(this.mContext, copyOfParams());
        synchronized (uploader) {
            new Thread(uploader).start();
            try {
                uploader.wait(timeoutMs);
            } catch (InterruptedException e) {
            }
        }
    }

    static SharedPreferences getOurSharedPreferences(Context context) {
        if (context == null) {
            return null;
        }
        return context.getSharedPreferences("FiksuSharedPreferences", 0);
    }

    private static final class C2DMessageTimeSaver implements Runnable {
        private final Context mContext;

        C2DMessageTimeSaver(Context context) {
            this.mContext = context;
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (EventTracker.SHARED_PREFERENCES_LOCK) {
                SharedPreferences preferences = EventTracker.getOurSharedPreferences(this.mContext);
                Date now = new Date();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putLong("Fiksu.cd2MessageTime", now.getTime());
                editor.commit();
            }
        }
    }

    static void c2dMessageReceived(Context context) {
        new Thread(new C2DMessageTimeSaver(context)).start();
    }
}
