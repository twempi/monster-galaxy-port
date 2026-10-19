package com.millennialmedia.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/* JADX INFO: loaded from: classes.dex */
public class MMBroadcastReceiver extends BroadcastReceiver {
    public static IntentFilter createIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addCategory(MMAdViewSDK.Event.CATEGORY_SDK);
        filter.addAction(MMAdViewSDK.Event.ACTION_DISPLAY_STARTED);
        filter.addAction(MMAdViewSDK.Event.ACTION_FETCH_FAILED);
        filter.addAction(MMAdViewSDK.Event.ACTION_FETCH_FINISHED_CACHING);
        filter.addAction(MMAdViewSDK.Event.ACTION_FETCH_STARTED_CACHING);
        filter.addAction(MMAdViewSDK.Event.ACTION_GETAD_FAILED);
        filter.addAction(MMAdViewSDK.Event.ACTION_GETAD_SUCCEEDED);
        filter.addAction(MMAdViewSDK.Event.ACTION_INTENT_STARTED);
        filter.addAction(MMAdViewSDK.Event.ACTION_OVERLAY_CLOSED);
        filter.addAction(MMAdViewSDK.Event.ACTION_OVERLAY_OPENED);
        filter.addAction(MMAdViewSDK.Event.ACTION_OVERLAY_TAP);
        return filter;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String packageName = intent.getStringExtra("packageName");
        long internalId = intent.getLongExtra("internalId", 0L);
        MMAdView adView = null;
        if (context.getPackageName().equals(packageName)) {
            if (internalId != 0) {
                Long tempId = new Long(internalId);
                adView = MMAdViewController.getAdViewWithId(tempId);
            }
            if (action.equals(MMAdViewSDK.Event.ACTION_OVERLAY_OPENED)) {
                overlayOpened(adView);
                return;
            }
            if (action.equals(MMAdViewSDK.Event.ACTION_OVERLAY_CLOSED)) {
                overlayClosed(adView);
                return;
            }
            if (action.equals(MMAdViewSDK.Event.ACTION_OVERLAY_TAP)) {
                overlayTap(adView);
                return;
            }
            if (action.equals(MMAdViewSDK.Event.ACTION_DISPLAY_STARTED)) {
                displayStarted(adView);
                return;
            }
            if (action.equals(MMAdViewSDK.Event.ACTION_FETCH_FAILED)) {
                fetchFailure(adView);
                return;
            }
            if (action.equals(MMAdViewSDK.Event.ACTION_FETCH_FINISHED_CACHING)) {
                fetchFinishedCaching(adView);
                return;
            }
            if (action.equals(MMAdViewSDK.Event.ACTION_FETCH_STARTED_CACHING)) {
                fetchStartedCaching(adView);
                return;
            }
            if (action.equals(MMAdViewSDK.Event.ACTION_GETAD_FAILED)) {
                getAdFailure(adView);
                return;
            }
            if (action.equals(MMAdViewSDK.Event.ACTION_GETAD_SUCCEEDED)) {
                getAdSuccess(adView);
            } else if (action.equals(MMAdViewSDK.Event.ACTION_INTENT_STARTED)) {
                String info = intent.getStringExtra("intentType");
                intentStarted(adView, info);
            }
        }
    }

    public void getAdSuccess(MMAdView adview) {
        MMAdViewSDK.Log.i("Millennial Media ad Success.");
    }

    public void getAdFailure(MMAdView adview) {
        MMAdViewSDK.Log.i("Millennial Media ad Failure.");
    }

    public void overlayOpened(MMAdView adview) {
        MMAdViewSDK.Log.i("Millennial Media overlay opened.");
    }

    public void overlayClosed(MMAdView adview) {
        MMAdViewSDK.Log.i("Millennial Media overlay closed.");
    }

    public void intentStarted(MMAdView adview, String intent) {
        if (intent != null) {
            MMAdViewSDK.Log.i("Millennial Media started intent: " + intent);
        }
    }

    public void fetchStartedCaching(MMAdView adview) {
        MMAdViewSDK.Log.i("Millennial Media fetch started caching.");
    }

    public void fetchFinishedCaching(MMAdView adview) {
        MMAdViewSDK.Log.i("Millennial Media fetch finished caching.");
    }

    public void fetchFailure(MMAdView adview) {
        MMAdViewSDK.Log.i("Millennial Media fetch failed.");
    }

    public void displayStarted(MMAdView adview) {
        MMAdViewSDK.Log.i("Millennial Media display started.");
    }

    public void overlayTap(MMAdView adview) {
        MMAdViewSDK.Log.i("Millennial Media overlay Tap.");
    }
}
