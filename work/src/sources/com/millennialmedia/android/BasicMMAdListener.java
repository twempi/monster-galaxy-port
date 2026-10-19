package com.millennialmedia.android;

import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class BasicMMAdListener implements MMAdView.MMAdListener {
    @Override // com.millennialmedia.android.MMAdView.MMAdListener
    public void MMAdFailed(MMAdView adview) {
        Log.i(MMAdViewSDK.SDKLOG, "Millennial Media Ad View request failed");
    }

    @Override // com.millennialmedia.android.MMAdView.MMAdListener
    public void MMAdReturned(MMAdView adview) {
        Log.i(MMAdViewSDK.SDKLOG, "Millennial Media Ad View request succeeded");
    }

    public void MMAdClickedToNewBrowser(MMAdView adview) {
        Log.i(MMAdViewSDK.SDKLOG, "Millennial Media Ad View clicked and launched new browser");
    }

    @Override // com.millennialmedia.android.MMAdView.MMAdListener
    public void MMAdClickedToOverlay(MMAdView adview) {
        Log.i(MMAdViewSDK.SDKLOG, "Millennial Media Ad View clicked to overlay");
    }

    @Override // com.millennialmedia.android.MMAdView.MMAdListener
    public void MMAdOverlayLaunched(MMAdView adview) {
        Log.i(MMAdViewSDK.SDKLOG, "Millennial Media Ad View overlay launched");
    }

    @Override // com.millennialmedia.android.MMAdView.MMAdListener
    public void MMAdRequestIsCaching(MMAdView adview) {
        Log.i(MMAdViewSDK.SDKLOG, "Millennial Media Ad View caching request");
    }

    @Override // com.millennialmedia.android.MMAdView.MMAdListener
    public void MMAdCachingCompleted(MMAdView adview, boolean success) {
        Log.i(MMAdViewSDK.SDKLOG, "Millennial Media Ad View caching request completed successfully: " + success);
    }
}
