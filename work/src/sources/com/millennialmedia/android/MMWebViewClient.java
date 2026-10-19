package com.millennialmedia.android;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/* JADX INFO: loaded from: classes.dex */
class MMWebViewClient extends WebViewClient {
    String adUrl;
    boolean hasDoneMraidCalls;
    OverlaySettings settings;

    private MMWebViewClient() {
    }

    MMWebViewClient(OverlaySettings settings) {
        this.settings = settings;
    }

    @Override // android.webkit.WebViewClient
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        if (!url.substring(0, 6).equalsIgnoreCase("mmsdk:")) {
            return false;
        }
        MMAdViewSDK.Log.v("Running JS bridge command: " + url);
        MMCommand command = new MMCommand(webView, url);
        command.settings = this.settings;
        Thread thread = new Thread(command);
        thread.start();
        return true;
    }
}
