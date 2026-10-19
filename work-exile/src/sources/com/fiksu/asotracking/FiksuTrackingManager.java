package com.fiksu.asotracking;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class FiksuTrackingManager {
    static final String FIKSU_LOG_TAG = "FiksuTracking";

    public static void initialize(Application application) {
        new ForegroundTester(application, new LaunchEventTracker(application));
        InstallTracking.checkForFiksuReceiver(application);
    }

    public static void uploadPurchaseEvent(Context context, String username, double price, String currency) {
        new PurchaseEventTracker(context, username, Double.valueOf(price), currency).uploadEvent();
    }

    public static void uploadRegistrationEvent(Context context, String username) {
        new RegistrationEventTracker(context, username).uploadEvent();
    }

    public static void c2dMessageReceived(Context context) {
        EventTracker.c2dMessageReceived(context);
    }

    public static void promptForRating(Activity activity) {
        new RatingPrompter(activity).maybeShowPrompt();
    }
}
