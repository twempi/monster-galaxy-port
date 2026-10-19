package com.chartboost.sdk;

import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public abstract class ChartBoostDelegate {
    public boolean shouldRequestInterstitial() {
        return true;
    }

    public boolean shouldDisplayInterstitial(View interstitialView) {
        return true;
    }

    public void didFailToLoadInterstitial() {
    }

    public void didDismissInterstitial(View interstitialView) {
    }

    public void didCloseInterstitial(View interstitialView) {
    }

    public void didClickInterstitial(View interstitialView) {
    }

    public boolean shouldDisplayLoadingViewForMoreApps() {
        return true;
    }

    public boolean shouldRequestMoreApps() {
        return true;
    }

    public boolean shouldDisplayMoreApps(View moreAppsView) {
        return true;
    }

    public void didFailToLoadMoreApps() {
    }

    public void didDismissMoreApps(View moreAppsView) {
    }

    public void didCloseMoreApps(View moreAppsView) {
    }

    public void didClickMoreApps(View moreAppsView) {
    }
}
