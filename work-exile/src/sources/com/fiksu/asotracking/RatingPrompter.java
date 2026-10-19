package com.fiksu.asotracking;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import java.util.Date;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class RatingPrompter {
    private static final int NUMBER_OF_DAYS_BEFORE_RATING_IN_MILLIS = 432000000;
    private static final int NUMBER_OF_LAUNCHES_BEFORE_RATING = 5;
    private static final String PREFERENCES_ALREADY_RATED_KEY = "Fiksu.alreadyRated";
    private static final String PREFERENCES_FIRST_LAUNCHED_KEY = "Fiksu.firstLaunchedAt";
    private static final String PREFERENCES_NAME_KEY = "Fiksu.ratingsDictionary";
    private static final String PREFERENCES_NUMBER_OF_LAUNCHES_KEY = "Fiksu.numberOfLaunches";
    private final Activity mActivity;
    private final String mAppName;
    private final RatingClickListener mNoRatingButtonListener;
    private final RatingClickListener mPostponeRatingButtonListener;
    private final RatingClickListener mRatingButtonListener;

    public RatingPrompter(Activity activity) {
        this.mRatingButtonListener = new RatingClickListener(PromptResult.USER_RATED, activity);
        this.mNoRatingButtonListener = new RatingClickListener(PromptResult.USER_DID_NOT_RATE, activity);
        this.mPostponeRatingButtonListener = new RatingClickListener(PromptResult.USER_POSTPONED_RATING, activity);
        this.mActivity = activity;
        PackageManager packageManager = this.mActivity.getPackageManager();
        String packageName = this.mActivity.getPackageName();
        String appName = null;
        try {
            appName = packageManager.getApplicationInfo(packageName, 0).loadLabel(packageManager).toString();
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("FiksuTracking", "Could not access package: " + packageName);
        }
        this.mAppName = appName;
    }

    private enum PromptResult {
        USER_RATED(1),
        USER_DID_NOT_RATE(2),
        USER_POSTPONED_RATING(3);

        /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
        public static PromptResult[] valuesCustom() {
            PromptResult[] promptResultArrValuesCustom = values();
            int length = promptResultArrValuesCustom.length;
            PromptResult[] promptResultArr = new PromptResult[length];
            System.arraycopy(promptResultArrValuesCustom, 0, promptResultArr, 0, length);
            return promptResultArr;
        }

        PromptResult(int value) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setUserRated() {
        SharedPreferences preferences = this.mActivity.getSharedPreferences(PREFERENCES_NAME_KEY, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREFERENCES_ALREADY_RATED_KEY, true);
        editor.commit();
    }

    private class RatingClickListener implements DialogInterface.OnClickListener {
        private static /* synthetic */ int[] $SWITCH_TABLE$com$fiksu$asotracking$RatingPrompter$PromptResult;
        private PromptResult mPromptResult;

        static /* synthetic */ int[] $SWITCH_TABLE$com$fiksu$asotracking$RatingPrompter$PromptResult() {
            int[] iArr = $SWITCH_TABLE$com$fiksu$asotracking$RatingPrompter$PromptResult;
            if (iArr == null) {
                iArr = new int[PromptResult.valuesCustom().length];
                try {
                    iArr[PromptResult.USER_DID_NOT_RATE.ordinal()] = 2;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[PromptResult.USER_POSTPONED_RATING.ordinal()] = 3;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[PromptResult.USER_RATED.ordinal()] = 1;
                } catch (NoSuchFieldError e3) {
                }
                $SWITCH_TABLE$com$fiksu$asotracking$RatingPrompter$PromptResult = iArr;
            }
            return iArr;
        }

        RatingClickListener(PromptResult userRated, Activity activity) {
            this.mPromptResult = userRated;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            switch ($SWITCH_TABLE$com$fiksu$asotracking$RatingPrompter$PromptResult()[this.mPromptResult.ordinal()]) {
                case 1:
                    new RatingEventTracker(RatingPrompter.this.mActivity, "rated", 5).uploadEvent();
                    Log.e("FiksuTracking", RatingPrompter.this.mActivity.getPackageName());
                    RatingPrompter.this.setUserRated();
                    RatingPrompter.this.mActivity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + RatingPrompter.this.mActivity.getPackageName())));
                    break;
                case 2:
                    new RatingEventTracker(RatingPrompter.this.mActivity, "did_not_rate", 5).uploadEvent();
                    RatingPrompter.this.setUserRated();
                    break;
                case 3:
                    new RatingEventTracker(RatingPrompter.this.mActivity, "deferred_rating", 5).uploadEvent();
                    break;
            }
        }
    }

    private boolean connectedToNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.mActivity.getSystemService("connectivity");
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    private int getNumberOfLaunches(SharedPreferences preferences, SharedPreferences.Editor editor) {
        int numberOfLaunches = preferences.getInt(PREFERENCES_NUMBER_OF_LAUNCHES_KEY, 0) + 1;
        editor.putInt(PREFERENCES_NUMBER_OF_LAUNCHES_KEY, numberOfLaunches);
        return numberOfLaunches;
    }

    private boolean enoughTimeSinceFirstLaunch(SharedPreferences preferences, SharedPreferences.Editor editor) {
        Date now = new Date();
        long firstLaunchedAtMillis = preferences.getLong(PREFERENCES_FIRST_LAUNCHED_KEY, now.getTime());
        boolean enoughTimeHasPassed = now.getTime() - firstLaunchedAtMillis > 432000000;
        if (now.getTime() == firstLaunchedAtMillis) {
            editor.putLong(PREFERENCES_FIRST_LAUNCHED_KEY, now.getTime());
        }
        return enoughTimeHasPassed;
    }

    private boolean shouldPrompt() {
        if (this.mAppName == null || !connectedToNetwork()) {
            return false;
        }
        SharedPreferences preferences = this.mActivity.getSharedPreferences(PREFERENCES_NAME_KEY, 0);
        if (preferences.getBoolean(PREFERENCES_ALREADY_RATED_KEY, false)) {
            return false;
        }
        SharedPreferences.Editor editor = preferences.edit();
        int numberOfLaunches = getNumberOfLaunches(preferences, editor);
        boolean enoughTimeHasPassed = enoughTimeSinceFirstLaunch(preferences, editor);
        editor.commit();
        return numberOfLaunches >= 5 && enoughTimeHasPassed;
    }

    public void maybeShowPrompt() {
        if (shouldPrompt()) {
            AlertDialog.Builder ratingDialogBuilder = new AlertDialog.Builder(this.mActivity);
            ratingDialogBuilder.setTitle("Enjoying " + this.mAppName + "?");
            ratingDialogBuilder.setMessage("If so, please rate it in the Android Marketplace.  It takes less than a minute and we appreciate your support!");
            ratingDialogBuilder.setPositiveButton("Rate " + this.mAppName, this.mRatingButtonListener);
            ratingDialogBuilder.setNegativeButton("No thanks", this.mNoRatingButtonListener);
            ratingDialogBuilder.setNeutralButton("Remind me later", this.mPostponeRatingButtonListener);
            ratingDialogBuilder.show();
        }
    }
}
