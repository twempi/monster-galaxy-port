package com.gaiaonline.monstergalaxy.service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxyAndroid;
import oauth.signpost.OAuth;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/* JADX INFO: loaded from: classes.dex */
public class AndroidTwitterService implements TwitterService {
    public static final String CALLBACK_URL = "http://www.gaiaonline.com/moga-android/twitter-callback/";
    private AccessToken accessToken;
    private MonsterGalaxyAndroid activity;
    private CommonsHttpOAuthConsumer consumer;
    private Runnable onAuthorizeCallback;
    private OAuthProvider provider;
    private Twitter twitter = new TwitterFactory().getInstance();
    private WebView webView;

    public AndroidTwitterService() {
        this.twitter.setOAuthConsumer(TwitterService.OAUTH_KEY, TwitterService.OAUTH_SECRET);
    }

    @Override // com.gaiaonline.monstergalaxy.service.TwitterService
    public boolean updateStatus(String status) {
        this.twitter.setOAuthAccessToken(getAccessToken());
        try {
            this.twitter.updateStatus(status);
            return true;
        } catch (TwitterException e) {
            e.printStackTrace();
            showError(e);
            return false;
        }
    }

    @Override // com.gaiaonline.monstergalaxy.service.TwitterService
    public boolean isAuthorized() {
        return getAccessToken() != null;
    }

    @Override // com.gaiaonline.monstergalaxy.service.TwitterService
    public boolean authorize(Runnable onAuthorizeCallback) {
        this.onAuthorizeCallback = onAuthorizeCallback;
        this.consumer = new CommonsHttpOAuthConsumer(TwitterService.OAUTH_KEY, TwitterService.OAUTH_SECRET);
        this.provider = new DefaultOAuthProvider("http://twitter.com/oauth/request_token", "http://twitter.com/oauth/access_token", "http://twitter.com/oauth/authorize");
        try {
            String authUrl = this.provider.retrieveRequestToken(this.consumer, CALLBACK_URL);
            loadPage(authUrl);
            return true;
        } catch (OAuthCommunicationException e) {
            showError(e);
            return false;
        } catch (OAuthExpectationFailedException e2) {
            showError(e2);
            return false;
        } catch (OAuthMessageSignerException e3) {
            showError(e3);
            return false;
        } catch (OAuthNotAuthorizedException e4) {
            showError(e4);
            return false;
        }
    }

    @Deprecated
    public void onNewIntent(Intent intent) {
        Uri uri;
        if (intent != null && intent.getData() != null && (uri = intent.getData()) != null && uri.toString().startsWith(CALLBACK_URL)) {
            authCompleted(uri);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void authCompleted(Uri uri) {
        String verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);
        try {
            this.provider.retrieveAccessToken(this.consumer, verifier);
            this.accessToken = new AccessToken(this.consumer.getToken(), this.consumer.getTokenSecret());
            storeAccessToken();
            this.twitter.setOAuthAccessToken(this.accessToken);
            if (this.onAuthorizeCallback != null) {
                this.onAuthorizeCallback.run();
            }
        } catch (Exception e) {
            showError(e);
        }
    }

    private void loadPage(final String url) {
        this.activity.runOnUiThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.service.AndroidTwitterService.1
            @Override // java.lang.Runnable
            public void run() {
                AndroidTwitterService.this.webView = new WebView(AndroidTwitterService.this.activity);
                ((FrameLayout) AndroidTwitterService.this.activity.getView().getParent()).addView(AndroidTwitterService.this.webView);
                AndroidTwitterService.this.webView.getSettings().setJavaScriptEnabled(true);
                AndroidTwitterService.this.webView.setVisibility(0);
                AndroidTwitterService.this.webView.requestFocus(130);
                AndroidTwitterService.this.webView.setOnTouchListener(new View.OnTouchListener() { // from class: com.gaiaonline.monstergalaxy.service.AndroidTwitterService.1.1
                    @Override // android.view.View.OnTouchListener
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case 0:
                            case 1:
                                if (!v.hasFocus()) {
                                    v.requestFocus();
                                }
                                break;
                        }
                        return false;
                    }
                });
                AndroidTwitterService.this.webView.setWebViewClient(new WebViewClient() { // from class: com.gaiaonline.monstergalaxy.service.AndroidTwitterService.1.2
                    @Override // android.webkit.WebViewClient
                    public void onPageStarted(WebView view, String url2, Bitmap favicon) {
                        if (url2.startsWith(AndroidTwitterService.CALLBACK_URL)) {
                            AndroidTwitterService.this.closeView();
                            AndroidTwitterService.this.authCompleted(Uri.parse(url2));
                        } else {
                            super.onPageStarted(view, url2, favicon);
                        }
                    }
                });
                AndroidTwitterService.this.webView.loadUrl(url);
            }
        });
    }

    public void setActivity(MonsterGalaxyAndroid activity) {
        this.activity = activity;
    }

    private void storeAccessToken() {
        SharedPreferences settings = getSharedPreferences();
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("twitter_access_token", this.accessToken.getToken());
        editor.putString("twitter_access_token_secret", this.accessToken.getTokenSecret());
        editor.commit();
    }

    private void showError(Throwable t) {
        if (t != null) {
            t.printStackTrace();
            showError(t.getMessage());
        } else {
            showError("Failed");
        }
    }

    private void showError(final String message) {
        this.activity.runOnUiThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.service.AndroidTwitterService.2
            @Override // java.lang.Runnable
            public void run() {
                Toast.makeText(AndroidTwitterService.this.activity, message, 1).show();
            }
        });
    }

    private AccessToken getAccessToken() {
        if (this.accessToken != null) {
            return this.accessToken;
        }
        SharedPreferences settings = getSharedPreferences();
        String token = settings.getString("twitter_access_token", null);
        String tokenSecret = settings.getString("twitter_access_token_secret", null);
        if (token == null || tokenSecret == null) {
            return null;
        }
        return new AccessToken(token, tokenSecret);
    }

    private SharedPreferences getSharedPreferences() {
        return this.activity.getPreferences(0);
    }

    @Override // com.gaiaonline.monstergalaxy.service.TwitterService
    public void closeView() {
        if (this.webView != null) {
            this.activity.runOnUiThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.service.AndroidTwitterService.3
                @Override // java.lang.Runnable
                public void run() {
                    ((FrameLayout) AndroidTwitterService.this.activity.getView().getParent()).removeView(AndroidTwitterService.this.webView);
                    AndroidTwitterService.this.webView.destroyDrawingCache();
                    AndroidTwitterService.this.webView.destroy();
                    AndroidTwitterService.this.webView = null;
                }
            });
        }
    }

    @Override // com.gaiaonline.monstergalaxy.service.TwitterService
    public boolean isViewOpen() {
        return this.webView != null;
    }

    public void dispose() {
        if (this.webView != null) {
            this.webView.destroyDrawingCache();
            this.webView.destroy();
        }
        this.webView = null;
        this.activity = null;
    }
}
