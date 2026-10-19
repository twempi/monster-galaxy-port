package com.millennialmedia.android;

import android.content.Context;
import android.util.AttributeSet;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class MMBannerAdView extends MMAdView {
    public MMBannerAdView(Context context) {
        super(context, MMAdViewSDK.DEFAULT_APID, MMAdView.BANNER_AD_BOTTOM, -1);
    }

    public MMBannerAdView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MMBannerAdView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void getAd(String apid, MMAdView.RequestListener listener) {
        MMAdView.Request request = new MMAdView.Request(apid, listener, false);
        if (!super.canRequestAd()) {
            MMAdViewSDK.Event.requestFailed(getContext(), this, request, new MMError(16));
        } else {
            super.requestAd(request);
        }
    }
}
