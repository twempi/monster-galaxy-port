package com.fiksu.asotracking;

import android.app.ActivityManager;
import android.app.Application;
import android.os.Process;
import android.util.Log;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
class ForegroundTester implements Runnable {
    private static boolean sStarted = false;
    private final Application mApplication;
    private final LaunchEventTracker mLaunchEventTracker;
    private boolean mWasInForeground = false;
    private boolean mPostedLaunch = false;

    ForegroundTester(Application application, LaunchEventTracker launchEventTracker) {
        this.mApplication = application;
        this.mLaunchEventTracker = launchEventTracker;
        synchronized (ForegroundTester.class) {
            if (sStarted) {
                Log.e("FiksuTracking", "Already initialized!. Only call FiksuTrackingManager.initialize() once.");
            } else {
                sStarted = true;
                new Thread(this).start();
            }
        }
    }

    private boolean inForeground() {
        ActivityManager activityManager = (ActivityManager) this.mApplication.getSystemService("activity");
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess != null && appProcess.importance == 100 && this.mApplication.getPackageName().equals(appProcess.processName)) {
                return true;
            }
        }
        return false;
    }

    protected void postEvent() {
        if (!this.mPostedLaunch) {
            this.mPostedLaunch = true;
            this.mLaunchEventTracker.uploadEvent();
        } else {
            new ResumeEventTracker(this.mApplication).uploadEvent();
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            Log.d("FiksuTracking", "ForegroundTester thread started, process: " + Process.myPid());
            Thread.sleep(6000L);
            while (true) {
                Thread.sleep(5000L);
                if (!this.mWasInForeground && inForeground()) {
                    postEvent();
                    this.mWasInForeground = true;
                } else if (this.mWasInForeground && !inForeground()) {
                    this.mWasInForeground = false;
                }
            }
        } catch (InterruptedException e) {
            Log.i("FiksuTracking", "Sleep interrupted");
        }
    }
}
