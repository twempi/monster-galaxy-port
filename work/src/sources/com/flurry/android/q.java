package com.flurry.android;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import oauth.signpost.OAuth;

/* JADX INFO: loaded from: classes.dex */
final class q extends WebViewClient {
    private /* synthetic */ CatalogActivity a;

    q(CatalogActivity catalogActivity) {
        this.a = catalogActivity;
    }

    @Override // android.webkit.WebViewClient
    public final boolean shouldOverrideUrlLoading(WebView webView, String str) {
        if (str != null) {
            if (this.a.f != null) {
                this.a.f.a(new f((byte) 6, this.a.e.j()));
            }
            this.a.e.a(webView.getContext(), this.a.f, str);
            return true;
        }
        return false;
    }

    @Override // android.webkit.WebViewClient
    public final void onReceivedError(WebView webView, int i, String str, String str2) {
        ah.c("FlurryAgent", "Failed to load url: " + str2 + " with an errorCode of " + i);
        webView.loadData("Cannot find Android Market information. <p>Please check your network", "text/html", OAuth.ENCODING);
    }

    @Override // android.webkit.WebViewClient
    public final void onPageFinished(WebView webView, String str) {
        try {
            p pVar = this.a.f;
            f fVar = new f((byte) 5, this.a.e.j());
            long j = this.a.f.c;
            pVar.d.add(fVar);
            pVar.c = j;
        } catch (Exception e) {
        }
    }
}
