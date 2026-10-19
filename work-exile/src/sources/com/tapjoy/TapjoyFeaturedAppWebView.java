package com.tapjoy;

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class TapjoyFeaturedAppWebView extends Activity {
    private ProgressBar progressBar;
    private WebView webView = null;
    private String fullScreenAdURL = "";
    private String userID = "";
    private String urlParams = "";
    final String TAPJOY_FEATURED_APP = "Featured App";

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        this.userID = extras.getString(TapjoyConstants.EXTRA_USER_ID);
        this.urlParams = extras.getString(TapjoyConstants.EXTRA_URL_PARAMS);
        this.fullScreenAdURL = extras.getString(TapjoyConstants.EXTRA_FEATURED_APP_FULLSCREEN_AD_URL);
        this.fullScreenAdURL = this.fullScreenAdURL.replaceAll(" ", "%20");
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        RelativeLayout layout = new RelativeLayout(this);
        this.webView = new WebView(this);
        this.webView.setWebViewClient(new TapjoyWebViewClient(this, null));
        WebSettings webSettings = this.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        this.progressBar = new ProgressBar(this, null, R.attr.progressBarStyleLarge);
        this.progressBar.setVisibility(0);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(13);
        this.progressBar.setLayoutParams(layoutParams);
        layout.addView(this.webView, -1, -1);
        layout.addView(this.progressBar);
        setContentView(layout);
        this.webView.loadUrl(this.fullScreenAdURL);
        TapjoyLog.i("Featured App", "Opening Full Screen AD URL = [" + this.fullScreenAdURL + "]");
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.webView != null) {
            RefreshTask refreshTask = new RefreshTask(this, null);
            refreshTask.execute(new Void[0]);
        }
    }

    private class RefreshTask extends AsyncTask<Void, Void, Boolean> {
        private RefreshTask() {
        }

        /* synthetic */ RefreshTask(TapjoyFeaturedAppWebView tapjoyFeaturedAppWebView, RefreshTask refreshTask) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(200L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Boolean result) {
            if (TapjoyFeaturedAppWebView.this.webView != null) {
                TapjoyFeaturedAppWebView.this.webView.loadUrl("javascript:window.onorientationchange();");
            }
        }
    }

    private class TapjoyWebViewClient extends WebViewClient {
        private TapjoyWebViewClient() {
        }

        /* synthetic */ TapjoyWebViewClient(TapjoyFeaturedAppWebView tapjoyFeaturedAppWebView, TapjoyWebViewClient tapjoyWebViewClient) {
            this();
        }

        @Override // android.webkit.WebViewClient
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            TapjoyFeaturedAppWebView.this.progressBar.setVisibility(0);
            TapjoyFeaturedAppWebView.this.progressBar.bringToFront();
        }

        @Override // android.webkit.WebViewClient
        public void onPageFinished(WebView view, String url) {
            TapjoyFeaturedAppWebView.this.progressBar.setVisibility(8);
        }

        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            TapjoyLog.i("Featured App", "URL = [" + url + "]");
            if (url.contains("showOffers")) {
                TapjoyLog.i("Featured App", "show offers");
                TapjoyFeaturedAppWebView.this.showOffers();
                return true;
            }
            if (url.contains("dismiss")) {
                TapjoyLog.i("Featured App", "dismiss");
                TapjoyFeaturedAppWebView.this.finishActivity();
                return true;
            }
            if (url.contains(TapjoyConstants.TJC_BASE_REDIRECT_DOMAIN)) {
                TapjoyLog.i("Featured App", "Open redirecting URL = [" + url + "]");
                view.loadUrl(url);
                return true;
            }
            TapjoyLog.i("Featured App", "Opening URL in new browser = [" + url + "]");
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
            TapjoyFeaturedAppWebView.this.startActivity(intent);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showOffers() {
        TapjoyLog.i("Featured App", "Showing offers, userID: " + this.userID);
        TapjoyLog.i("Featured App", "Showing offers, URL PARAMS: " + this.urlParams);
        Intent offersIntent = new Intent(this, (Class<?>) TJCOffersWebView.class);
        offersIntent.putExtra(TapjoyConstants.EXTRA_USER_ID, this.userID);
        offersIntent.putExtra(TapjoyConstants.EXTRA_URL_PARAMS, this.urlParams);
        startActivity(offersIntent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishActivity() {
        finish();
    }
}
