package com.millennialmedia.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StatFs;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import oauth.signpost.OAuth;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import twitter4j.conf.PropertyConfiguration;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class MMAdViewController implements AdCache.AdCacheTaskListener, HttpRedirection.Listener {
    private static final HashMap<Long, MMAdViewController> controllers = new HashMap<>();
    private static String overrideAdURL;
    String adUrl;
    WeakReference<MMAdView> adViewRef;
    private boolean appPaused;
    private Handler handler;
    String nextUrl;
    private boolean refreshTimerOn;
    boolean requestInProgress;
    private long timeRemaining;
    private long timeResumed;
    private String urlString;
    private String useragent;
    private WebView webView;
    private Handler cacheHandler = new Handler(Looper.getMainLooper());
    private boolean paused = true;
    OverlaySettings settings = new OverlaySettings();
    private Runnable runnable = new Runnable() { // from class: com.millennialmedia.android.MMAdViewController.5
        @Override // java.lang.Runnable
        public void run() {
            MMAdView adView = MMAdViewController.this.adViewRef.get();
            if (adView == null) {
                MMAdViewSDK.Log.e("The reference to the ad view was broken.");
            } else {
                MMAdViewController.this.requestAd(new MMAdView.Request(adView.apid, null, false));
                MMAdViewController.this.handler.postDelayed(this, adView.refreshInterval * 1000);
            }
        }
    };

    private MMAdViewController(MMAdView adView) {
        this.adViewRef = new WeakReference<>(adView);
        this.webView = new WebView(adView.getContext().getApplicationContext());
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setCacheMode(2);
        this.webView.setBackgroundColor(0);
        this.webView.setWillNotDraw(false);
        this.webView.addJavascriptInterface(new MMJSInterface(), "interface");
        this.webView.setId(15063);
        this.useragent = this.webView.getSettings().getUserAgentString() + Build.MODEL;
        this.settings.isBannerAd = true;
    }

    static synchronized void assignAdViewController(MMAdView adView) {
        MMAdViewController controller;
        boolean reassign = true;
        if (adView.controller == null) {
            if (adView.getId() == -1 && !adView.isInterstitial()) {
                MMAdViewSDK.Log.w("MMAdView created without a view id. Performance may be affected.");
            }
            if (adView.getWindowToken() != null) {
                controller = controllers.get(adView.adViewId);
                if (controller == null) {
                    controller = new MMAdViewController(adView);
                    controllers.put(adView.adViewId, controller);
                    reassign = false;
                }
            } else {
                controller = new MMAdViewController(adView);
                MMAdViewSDK.Log.w("MMAdView not attached to a window. Performance may be affected.");
            }
            controller.adViewRef = new WeakReference<>(adView);
            adView.controller = controller;
            if (controller.webView.getParent() != null) {
                ((ViewGroup) controller.webView.getParent()).removeView(controller.webView);
            }
            adView.addView(controller.webView, new ViewGroup.LayoutParams(-1, -1));
            if (adView.refreshInterval >= 0 && adView.refreshInterval < 15) {
                controller.refreshTimerOn = false;
                MMAdViewSDK.Log.d("Refresh interval is %d. Change to at least %s to refresh ads.", Integer.valueOf(adView.refreshInterval), 15);
            } else if (adView.refreshInterval < 0) {
                controller.refreshTimerOn = false;
                MMAdViewSDK.Log.d("Automatic ad fetching is off with %d. You must manually call for ads.", Integer.valueOf(adView.refreshInterval));
            } else {
                controller.refreshTimerOn = true;
                controller.resumeTimer(false);
            }
            if (adView.refreshInterval >= 0 && !reassign) {
                controller.requestAd(new MMAdView.Request(adView.apid, null, false));
            }
        }
    }

    static synchronized void removeAdViewController(MMAdView adView, boolean isFinishing) {
        MMAdViewController controller;
        if (adView.controller != null) {
            if (adView.getWindowToken() != null) {
                if (isFinishing) {
                    controller = controllers.put(adView.adViewId, null);
                } else {
                    controller = controllers.get(adView.adViewId);
                }
            } else {
                controller = adView.controller;
            }
            adView.controller = null;
            if (controller != null) {
                controller.pauseTimer(false);
                if (isFinishing) {
                    controller.handler = null;
                }
                adView.removeView(controller.webView);
            }
        }
    }

    static synchronized MMAdView getAdViewWithId(Long internalId) {
        MMAdViewController controller;
        controller = controllers.get(internalId);
        return controller != null ? controller.adViewRef.get() : null;
    }

    static synchronized void setAdURL(Context context, String url) {
        try {
            if (url != null) {
                overrideAdURL = url + "getAd.php5?";
            } else {
                overrideAdURL = "http://androidsdk.ads.mp.mydas.mobi/getAd.php5?";
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    void requestAd(final MMAdView.Request request) {
        this.requestInProgress = true;
        Thread thread = new Thread() { // from class: com.millennialmedia.android.MMAdViewController.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() throws Throwable {
                int endIndex;
                MMAdView adView = MMAdViewController.this.adViewRef.get();
                if (adView == null) {
                    MMAdViewSDK.Log.e("The reference to the ad view was broken.");
                    MMAdViewController.this.requestInProgress = false;
                    return;
                }
                if (request.apid == null) {
                    MMAdViewSDK.Event.requestFailed(adView.getContext(), adView, request, new MMError(1));
                    MMAdViewSDK.Log.e("MMAdView found with a null apid. New ad requests on this MMAdView are disabled until an apid has been assigned.");
                    MMAdViewController.this.requestInProgress = false;
                    return;
                }
                if (MMAdViewSDK.isConnected(adView.getContext())) {
                    MMAdViewController.this.adUrl = null;
                    try {
                        TreeMap<String, String> paramsMap = new TreeMap<>((Comparator<? super String>) String.CASE_INSENSITIVE_ORDER);
                        MMAdViewController.getUrlAdMetaValues(adView, paramsMap);
                        MMAdViewController.getUrlCommonValues(adView.getContext(), paramsMap);
                        MMAdViewController.this.getAdType(adView.adType, paramsMap);
                        if (MMAdViewController.this.useragent != null) {
                            paramsMap.put("ua", MMAdViewController.this.useragent);
                        } else {
                            paramsMap.put("ua", "UNKNOWN");
                        }
                        if (!request.fetch) {
                            paramsMap.put("cachedvideo", "false");
                        }
                        paramsMap.put("sdkapid", request.apid);
                        paramsMap.put("reqtype", request.fetch ? "fetch" : "getad");
                        StringBuilder adUrlBuilder = new StringBuilder(MMAdViewController.overrideAdURL == null ? "http://androidsdk.ads.mp.mydas.mobi/getAd.php5?" : MMAdViewController.overrideAdURL);
                        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                            adUrlBuilder.append(String.format("%s=%s&", entry.getKey(), URLEncoder.encode(entry.getValue(), OAuth.ENCODING)));
                        }
                        adUrlBuilder.delete(adUrlBuilder.length() - 1, adUrlBuilder.length());
                        MMAdViewController.this.adUrl = adUrlBuilder.toString();
                        MMAdViewSDK.Log.d("Calling for an advertisement: %s", MMAdViewController.this.adUrl);
                        try {
                            HttpGetRequest httpGetRequest = new HttpGetRequest();
                            HttpResponse httpResponse = httpGetRequest.get(MMAdViewController.this.adUrl);
                            if (httpResponse == null) {
                                MMAdViewSDK.Log.e("HTTP response is null");
                                MMAdViewController.this.requestInProgress = false;
                                MMAdViewSDK.Event.requestFailed(adView.getContext(), adView, request, new MMError(14));
                            } else {
                                HttpEntity httpEntity = httpResponse.getEntity();
                                if (httpEntity == null) {
                                    MMAdViewSDK.Log.i("Null HTTP entity");
                                    MMAdViewController.this.requestInProgress = false;
                                    MMAdViewSDK.Event.requestFailed(adView.getContext(), adView, request, new MMError(14));
                                } else if (httpEntity.getContentLength() == 0) {
                                    MMAdViewSDK.Log.i("Millennial ad return failed. Zero content length returned.");
                                    MMAdViewController.this.requestInProgress = false;
                                    MMAdViewSDK.Event.requestFailed(adView.getContext(), adView, request, new MMError(14));
                                } else {
                                    Header[] cookieHeaders = httpResponse.getHeaders("Set-Cookie");
                                    for (Header cookieHeader : cookieHeaders) {
                                        String value = cookieHeader.getValue();
                                        int index = value.indexOf("MAC-ID=");
                                        if (index >= 0 && (endIndex = value.indexOf(59, index)) > index) {
                                            MMAdViewSDK.macId = value.substring(index + 7, endIndex);
                                        }
                                    }
                                    Header httpHeader = httpEntity.getContentType();
                                    if (httpHeader != null) {
                                        if (httpHeader.getValue() != null) {
                                            if (httpHeader.getValue().equalsIgnoreCase("application/json")) {
                                                if (!request.fetch) {
                                                    MMAdViewSDK.Log.e("Millennial ad return unsupported format.");
                                                    MMAdViewController.this.requestInProgress = false;
                                                    MMAdViewSDK.Event.requestFailed(adView.getContext(), adView, request, new MMError(15));
                                                } else {
                                                    try {
                                                        String json = HttpGetRequest.convertStreamToString(httpEntity.getContent());
                                                        VideoAd videoAd = (VideoAd) CachedAd.parseJSON(json);
                                                        if (videoAd != null && videoAd.isValid()) {
                                                            MMAdViewSDK.Log.i("Cached video ad JSON received: " + videoAd.id);
                                                            if (videoAd.isExpired()) {
                                                                MMAdViewSDK.Log.i("New ad has expiration date in the past. Not downloading ad content.");
                                                                videoAd.delete(adView.getContext());
                                                                MMAdViewSDK.Event.requestFailed(adView.getContext(), adView, request, new MMError(15));
                                                            } else if (AdCache.loadNextCachedAd(adView.getContext(), adView.getCachedName()) != null) {
                                                                MMAdViewSDK.Log.i("Previously fetched ad exists in the playback queue. Not downloading ad content.");
                                                                videoAd.delete(adView.getContext());
                                                                MMAdViewSDK.Event.requestFailed(adView.getContext(), adView, request, new MMError(17));
                                                            } else {
                                                                AdCache.save(adView.getContext(), videoAd);
                                                                if (!videoAd.isOnDisk(adView.getContext())) {
                                                                    MMAdViewSDK.Log.d("Downloading ad...");
                                                                    MMAdViewSDK.Event.fetchStartedCaching(adView.getContext(), adView);
                                                                    videoAd.downloadPriority = 3;
                                                                    videoAd.request = request;
                                                                    AdCache.startDownloadTask(adView.getContext(), adView.getCachedName(), videoAd, MMAdViewController.this);
                                                                } else {
                                                                    MMAdViewSDK.Log.d("Cached ad is valid. Moving it to the front of the queue.");
                                                                    AdCache.setNextCachedAd(adView.getContext(), adView.getCachedName(), videoAd.id);
                                                                    MMAdViewSDK.Event.fetchStartedCaching(adView.getContext(), adView);
                                                                    MMAdViewSDK.Event.fetchFinishedCaching(adView.getContext(), adView, request);
                                                                }
                                                            }
                                                        }
                                                    } catch (IOException e1) {
                                                        e1.printStackTrace();
                                                        MMAdViewSDK.Log.i("Millennial ad return failed. %s", e1.getMessage());
                                                        MMAdViewController.this.requestInProgress = false;
                                                        MMAdViewSDK.Event.requestFailed(adView.getContext(), adView, request, new MMError(e1));
                                                    } catch (IllegalStateException e2) {
                                                        e2.printStackTrace();
                                                        MMAdViewSDK.Log.i("Millennial ad return failed. Invalid response data.");
                                                        MMAdViewController.this.requestInProgress = false;
                                                        MMAdViewSDK.Event.requestFailed(adView.getContext(), adView, request, new MMError(e2));
                                                    }
                                                }
                                            } else if (httpHeader.getValue().equalsIgnoreCase("text/html")) {
                                                Header xHeader = httpResponse.getFirstHeader("X-MM-Video");
                                                if (xHeader != null && xHeader.getValue().equalsIgnoreCase("true")) {
                                                    Context context = adView.getContext();
                                                    HandShake.sharedHandShake(context).updateLastVideoViewedTime(context, adView.adType);
                                                }
                                                try {
                                                    if (request.fetch) {
                                                        InterstitialAd interstitialAd = new InterstitialAd();
                                                        interstitialAd.content = HttpGetRequest.convertStreamToString(httpEntity.getContent());
                                                        interstitialAd.id = adView.adType;
                                                        interstitialAd.baseUrl = MMAdViewController.this.adUrl.substring(0, MMAdViewController.this.adUrl.lastIndexOf("/") + 1);
                                                        if (MMAdViewSDK.logLevel >= 4) {
                                                            MMAdViewSDK.Log.v("Received interstitial ad with base url %s.", interstitialAd.baseUrl);
                                                            MMAdViewSDK.Log.v(interstitialAd.content);
                                                        }
                                                        AdCache.save(adView.getContext(), interstitialAd);
                                                        AdCache.setNextCachedAd(adView.getContext(), adView.getCachedName(), interstitialAd.id);
                                                        MMAdViewSDK.Event.fetchStartedCaching(adView.getContext(), adView);
                                                        MMAdViewSDK.Event.fetchFinishedCaching(adView.getContext(), adView, request);
                                                    } else {
                                                        MMAdViewController.this.settings.adUrl = MMAdViewController.this.adUrl;
                                                        MMAdViewController.this.setWebViewContent(HttpGetRequest.convertStreamToString(httpEntity.getContent()), MMAdViewController.this.adUrl.substring(0, MMAdViewController.this.adUrl.lastIndexOf("/") + 1), adView, request, adView.transitionType != 0);
                                                    }
                                                } catch (IOException ioe) {
                                                    MMAdViewSDK.Log.e("Exception raised in HTTP stream: ", ioe);
                                                    MMAdViewSDK.Event.requestFailed(adView.getContext(), adView, request, new MMError(ioe));
                                                }
                                            } else {
                                                MMAdViewSDK.Log.i("Millennial ad return failed. Invalid mime type returned.");
                                                MMAdViewSDK.Event.requestFailed(adView.getContext(), adView, request, new MMError(15));
                                            }
                                        } else {
                                            MMAdViewSDK.Log.i("Millennial ad return failed. HTTP Header value null.");
                                            MMAdViewSDK.Event.requestFailed(adView.getContext(), adView, request, new MMError(15));
                                        }
                                    } else {
                                        MMAdViewSDK.Log.i("Millennial ad return failed. HTTP Header is null.");
                                        MMAdViewSDK.Event.requestFailed(adView.getContext(), adView, request, new MMError(15));
                                    }
                                }
                            }
                        } catch (Exception e) {
                            MMAdViewSDK.Log.e("Ad request HTTP error. %s", e.getMessage());
                            MMAdViewController.this.requestInProgress = false;
                            MMAdViewSDK.Event.requestFailed(adView.getContext(), adView, request, new MMError(e));
                            return;
                        }
                    } catch (Exception e3) {
                        MMAdViewSDK.Log.e(e3);
                        MMAdViewController.this.requestInProgress = false;
                        MMAdViewSDK.Event.requestFailed(adView.getContext(), adView, request, new MMError(e3));
                        return;
                    }
                }
                MMAdViewSDK.Log.i("No network available, can't call for ads.");
                MMAdViewSDK.Event.requestFailed(adView.getContext(), adView, request, new MMError(11));
                MMAdViewController.this.requestInProgress = false;
            }
        };
        thread.start();
    }

    void setWebViewContent(String webContent, final String baseUrl, final MMAdView adView, final MMAdView.Request request, final boolean animate) {
        final String content;
        if (webContent != null && baseUrl != null) {
            if (MMAdViewSDK.logLevel >= 4) {
                MMAdViewSDK.Log.v("Received banner ad with base url %s.", baseUrl);
                MMAdViewSDK.Log.v(webContent);
            }
            if (animate) {
                Runnable r = new Runnable() { // from class: com.millennialmedia.android.MMAdViewController.2
                    @Override // java.lang.Runnable
                    public synchronized void run() {
                        try {
                            MMAdViewController.this.webView.buildDrawingCache();
                            Bitmap cache = MMAdViewController.this.webView.getDrawingCache();
                            if (cache != null) {
                                Bitmap bitmap = Bitmap.createBitmap(cache);
                                adView.prepareTransition(bitmap);
                            }
                            MMAdViewController.this.webView.destroyDrawingCache();
                            notify();
                        } catch (Exception e) {
                            MMAdViewSDK.Log.d(e);
                        }
                    }
                };
                try {
                    synchronized (r) {
                        MMAdViewSDK.runOnUiThread(r);
                        r.wait();
                    }
                } catch (InterruptedException e) {
                }
            }
            final WebViewClient webViewClient = new MMWebViewClient(this.settings) { // from class: com.millennialmedia.android.MMAdViewController.3
                @Override // android.webkit.WebViewClient
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    MMAdViewController.this.nextUrl = null;
                    super.onPageStarted(view, url, favicon);
                }

                @Override // android.webkit.WebViewClient
                public void onPageFinished(WebView webView, String url) {
                    webView.loadUrl("javascript:window.interface.getUrl(document.links[0].href);");
                    if (!this.hasDoneMraidCalls) {
                        this.hasDoneMraidCalls = true;
                        if (webView.hasWindowFocus()) {
                            webView.loadUrl("javascript:MMSDK.mraid.viewableChange(true)");
                        }
                        webView.loadUrl("javascript:MMSDK.mraid.setPlacementType('inline');");
                        webView.loadUrl("javascript:MMSDK.mraid.stateChange('default');");
                        webView.loadUrl("javascript:MMSDK.mraid.ready();");
                    }
                    MMAdView webClientAdView = MMAdViewController.this.adViewRef.get();
                    if (webClientAdView != null) {
                        webClientAdView.setClickable(true);
                        if (animate) {
                            webClientAdView.animateTransition();
                        }
                    }
                    if (webView != null) {
                        webView.clearCache(true);
                    }
                    if (request != null) {
                        MMAdViewSDK.Event.getAdSuccess(adView.getContext(), adView, request);
                    }
                }

                @Override // com.millennialmedia.android.MMWebViewClient, android.webkit.WebViewClient
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    super.shouldOverrideUrlLoading(view, url);
                    return true;
                }

                @Override // android.webkit.WebViewClient
                public void onScaleChanged(WebView view, float oldScale, float newScale) {
                    MMAdViewSDK.Log.e("Scale Changed");
                }
            };
            if (adView.ignoreDensityScaling) {
                content = "<head><meta name=\"viewport\" content=\"target-densitydpi=device-dpi\" /></head>" + webContent;
            } else {
                content = webContent;
            }
            this.settings.reset();
            adView.setClickable(false);
            MMAdViewSDK.runOnUiThread(new Runnable() { // from class: com.millennialmedia.android.MMAdViewController.4
                @Override // java.lang.Runnable
                public void run() {
                    MMAdViewController.this.webView.setWebViewClient(webViewClient);
                    MMAdViewController.this.webView.loadDataWithBaseURL(baseUrl, content, "text/html", OAuth.ENCODING, null);
                }
            });
        }
    }

    @Override // com.millennialmedia.android.HttpRedirection.Listener
    public boolean shouldStartActivityForUri(Uri uri) {
        MMAdView adView;
        Context c;
        MMAdView adView2;
        MMAdViewSDK.Log.d("Starting activity for %s", uri);
        if (this.adViewRef != null && ((uri.getScheme().equalsIgnoreCase("http") || uri.getScheme().equalsIgnoreCase("https")) && (adView2 = this.adViewRef.get()) != null)) {
            MMAdViewSDK.Event.overlayOpened(adView2.getContext(), adView2);
        }
        if (this.adViewRef != null && (adView = this.adViewRef.get()) != null && (c = adView.getContext()) != null && (c instanceof Activity)) {
            Activity a = (Activity) c;
            if (a.isFinishing()) {
                return false;
            }
        }
        return true;
    }

    @Override // com.millennialmedia.android.HttpRedirection.Listener
    public void didFailToResolveUri(Uri uri) {
    }

    void loadUrl(String url) {
        this.webView.loadUrl(url);
    }

    void touchWebView(MotionEvent ev) {
        this.webView.onTouchEvent(ev);
    }

    void handleClick(String url) {
        MMAdViewSDK.Log.d("Touch occured, opening ad...");
        if (url != null) {
            MMAdView adView = this.adViewRef.get();
            if (adView == null) {
                MMAdViewSDK.Log.e("The reference to the ad view was broken.");
                return;
            }
            Context context = adView.getContext();
            if (context == null) {
                MMAdViewSDK.Log.e("The ad view does not have a parent activity.");
            } else {
                HttpRedirection.startActivityFromUri(context, url, this.settings, this, adView.adType);
            }
        }
    }

    void pauseTimer(boolean appRequested) {
        synchronized (this) {
            if (this.refreshTimerOn) {
                if (this.paused) {
                    if (appRequested) {
                        this.appPaused = true;
                    }
                } else {
                    this.handler.removeCallbacks(this.runnable);
                    this.timeRemaining = SystemClock.uptimeMillis() - this.timeResumed;
                    this.paused = true;
                    this.appPaused = appRequested;
                }
            }
        }
    }

    void resumeTimer(boolean appRequested) {
        synchronized (this) {
            if (this.refreshTimerOn) {
                if (this.paused) {
                    if (!this.appPaused || appRequested) {
                        MMAdView adView = this.adViewRef.get();
                        if (adView == null) {
                            MMAdViewSDK.Log.e("The reference to the ad view was broken.");
                            return;
                        }
                        if (this.handler == null) {
                            this.handler = new Handler(Looper.getMainLooper());
                        }
                        if (this.timeRemaining <= 0 || this.timeRemaining > adView.refreshInterval * 1000) {
                            this.timeRemaining = adView.refreshInterval * 1000;
                        }
                        this.handler.postDelayed(this.runnable, this.timeRemaining);
                        this.timeResumed = SystemClock.uptimeMillis();
                        this.appPaused = false;
                        this.paused = false;
                    }
                }
            }
        }
    }

    static void getUrlCommonValues(Context context, Map<String, String> paramsMap) {
        StatFs stat;
        paramsMap.put("accelerometer", MMAdViewSDK.hasAccelerometer(context));
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        paramsMap.put("density", Float.toString(metrics.density));
        paramsMap.put("hpx", Integer.toString(metrics.heightPixels));
        paramsMap.put("wpx", Integer.toString(metrics.widthPixels));
        if (context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == -1 || (Build.VERSION.SDK.equalsIgnoreCase("8") && !Environment.getExternalStorageState().equals("mounted"))) {
            paramsMap.put("cachedvideo", "false");
        } else {
            paramsMap.put("cachedvideo", "true");
        }
        String auid = MMAdViewSDK.getAuidOrHdid(context);
        if (auid != null) {
            if (auid.startsWith("mmh_")) {
                paramsMap.put("hdid", auid);
            } else {
                paramsMap.put("auid", auid);
            }
        }
        if (Build.MODEL != null) {
            paramsMap.put("dm", Build.MODEL);
        }
        if (Build.VERSION.RELEASE != null) {
            paramsMap.put("dv", "Android" + Build.VERSION.RELEASE);
        }
        String mmdid = MMAdViewSDK.getMMdid(context);
        if (mmdid != null) {
            paramsMap.put("mmdid", mmdid);
        }
        paramsMap.put("mmisdk", MMAdViewSDK.SDKVER);
        Locale locale = Locale.getDefault();
        if (locale != null) {
            paramsMap.put("language", locale.getLanguage());
            paramsMap.put("country", locale.getCountry());
        }
        try {
            paramsMap.put("pkid", context.getPackageName());
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(context.getPackageName(), 0);
            paramsMap.put("pknm", pm.getApplicationLabel(ai).toString());
        } catch (Exception e) {
        }
        if (MMAdViewSDK.debugMode) {
            paramsMap.put(PropertyConfiguration.DEBUG, "true");
        }
        if (MMAdViewSDK.demographic != null) {
            MMAdViewSDK.demographic.getUrlParams(paramsMap);
        }
        String schemes = HandShake.sharedHandShake(context).getSchemesList(context);
        if (schemes != null) {
            paramsMap.put("appsids", schemes);
        }
        String vid = AdCache.getCachedVideoList(context);
        if (vid != null) {
            paramsMap.put("vid", vid);
        }
        try {
            String connectionType = MMAdViewSDK.getConnectionType(context);
            if (Environment.getExternalStorageState().equals("mounted")) {
                stat = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
            } else {
                stat = new StatFs(context.getCacheDir().getPath());
            }
            String freeSpace = Long.toString(((long) stat.getAvailableBlocks()) * ((long) stat.getBlockSize()));
            String devicePluggedIn = null;
            String deviceBatteryLevel = null;
            Intent intent = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            if (intent != null) {
                devicePluggedIn = intent.getIntExtra("plugged", 0) == 0 ? "false" : "true";
                int scale = intent.getIntExtra("scale", 100);
                float ratio = 100.0f / scale;
                int level = (int) (intent.getIntExtra("level", 0) * ratio);
                deviceBatteryLevel = Integer.toString(level);
            }
            if (deviceBatteryLevel != null && deviceBatteryLevel.length() > 0) {
                paramsMap.put("bl", deviceBatteryLevel);
            }
            if (devicePluggedIn != null && devicePluggedIn.length() > 0) {
                paramsMap.put("plugged", devicePluggedIn);
            }
            if (freeSpace.length() > 0) {
                paramsMap.put("space", freeSpace);
            }
            if (connectionType != null) {
                paramsMap.put("conn", connectionType);
            }
        } catch (Exception e2) {
            MMAdViewSDK.Log.v(e2);
        }
        if (MMAdViewSDK.location != null) {
            paramsMap.put("lat", Double.toString(MMAdViewSDK.location.getLatitude()));
            paramsMap.put("long", Double.toString(MMAdViewSDK.location.getLongitude()));
            if (MMAdViewSDK.location.hasAccuracy()) {
                paramsMap.put("ha", Float.toString(MMAdViewSDK.location.getAccuracy()));
                paramsMap.put("va", Float.toString(MMAdViewSDK.location.getAccuracy()));
            }
            if (MMAdViewSDK.location.hasSpeed()) {
                paramsMap.put("spd", Float.toString(MMAdViewSDK.location.getSpeed()));
            }
            if (MMAdViewSDK.location.hasBearing()) {
                paramsMap.put("brg", Float.toString(MMAdViewSDK.location.getBearing()));
            }
            if (MMAdViewSDK.location.hasAltitude()) {
                paramsMap.put("alt", Double.toString(MMAdViewSDK.location.getAltitude()));
            }
            paramsMap.put("tslr", Long.toString(MMAdViewSDK.location.getTime()));
            paramsMap.put("loc", "true");
            return;
        }
        paramsMap.put("loc", "false");
    }

    static void getUrlAdMetaValues(MMAdView adView, Map<String, String> paramsMap) {
        Context context = adView.getContext();
        if (adView.acid != null) {
            paramsMap.put("acid", adView.acid);
        }
        if (adView.mxsdk != null) {
            paramsMap.put("mxsdk", adView.mxsdk);
        }
        if (adView.height != null) {
            paramsMap.put("hsht", adView.height);
        }
        if (adView.width != null) {
            paramsMap.put("hswd", adView.width);
        }
        if (adView.controller != null && adView.controller.refreshTimerOn) {
            paramsMap.put("ar", Integer.toString(adView.refreshInterval));
        } else {
            paramsMap.put("ar", "manual");
        }
        if (HandShake.sharedHandShake(context).canRequestVideo(context, adView.adType)) {
            paramsMap.put("video", "true");
        } else {
            paramsMap.put("video", "false");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getAdType(String adtype, TreeMap<String, String> paramsMap) {
        if (adtype != null && (adtype.equals(MMAdView.BANNER_AD_TOP) || adtype.equals(MMAdView.BANNER_AD_BOTTOM) || adtype.equals(MMAdView.BANNER_AD_RECTANGLE) || adtype.equals(MMAdView.FULLSCREEN_AD_LAUNCH) || adtype.equals(MMAdView.FULLSCREEN_AD_TRANSITION))) {
            paramsMap.put("adtype", adtype);
            return;
        }
        MMAdViewSDK.Log.e("******* ERROR: INCORRECT AD TYPE IN MMADVIEW OBJECT PARAMETERS (" + adtype + ") **********");
        MMAdViewSDK.Log.e("******* SDK DEFAULTED TO MMBannerAdBottom. THIS MAY AFFECT THE ADS YOU RECIEVE!!! **********");
        paramsMap.put("adtype", MMAdView.BANNER_AD_BOTTOM);
    }

    @Override // com.millennialmedia.android.AdCache.AdCacheTaskListener
    public void downloadCompleted(CachedAd ad, boolean success) {
        MMAdView adView = this.adViewRef.get();
        if (adView == null) {
            MMAdViewSDK.Log.e("The reference to the ad view was broken.");
            return;
        }
        if (success) {
            AdCache.setNextCachedAd(adView.getContext(), adView.getCachedName(), ad.id);
        }
        if (success) {
            MMAdViewSDK.Event.fetchFinishedCaching(adView.getContext(), adView, ad.request);
        } else {
            MMAdViewSDK.Event.fetchFailed(adView.getContext(), adView, ad.request, new MMError(15));
        }
    }

    synchronized void fetch(MMAdView.Request request) {
        MMAdView adView = this.adViewRef.get();
        if (adView == null) {
            MMAdViewSDK.Log.e("The reference to the ad view was broken.");
        } else {
            Context context = adView.getContext();
            if (HandShake.sharedHandShake(context).kill) {
                MMAdViewSDK.Log.i("The server is no longer allowing ads.");
                MMAdViewSDK.Event.requestFailed(context, adView, request, new MMError(16));
            } else if (this.requestInProgress) {
                MMAdViewSDK.Log.i("There is already an ad request in progress. Defering call for new ad");
                MMAdViewSDK.Event.requestFailed(context, adView, request, new MMError(12));
            } else if (HandShake.sharedHandShake(context).isAdTypeDownloading(adView.adType)) {
                MMAdViewSDK.Log.i("There is a download in progress. Defering call for new ad");
                MMAdViewSDK.Event.requestFailed(context, adView, request, new MMError(12));
            } else {
                MMAdViewSDK.Log.d("No download in progress.");
                CachedAd incompleteAd = AdCache.loadIncompleteDownload(adView.getContext(), adView.getCachedName());
                if (incompleteAd != null) {
                    MMAdViewSDK.Log.i("Last ad wasn't fully downloaded. Download again.");
                    MMAdViewSDK.Event.fetchStartedCaching(context, adView);
                    incompleteAd.request = request;
                    AdCache.startDownloadTask(adView.getContext(), adView.getCachedName(), incompleteAd, this);
                } else {
                    MMAdViewSDK.Log.i("No incomplete downloads.");
                    requestAd(request);
                }
            }
        }
    }

    int checkReason(MMAdView adView, CachedAd ad) {
        if (ad.isExpired()) {
            MMAdViewSDK.Log.d("%s is expired.", ad.id);
            return 21;
        }
        if (!ad.isOnDisk(adView.getContext())) {
            MMAdViewSDK.Log.d("%s is not on disk.", ad.id);
            return 22;
        }
        if (!HandShake.sharedHandShake(adView.getContext()).canDisplayCachedAd(adView.adType, ad.deferredViewStart)) {
            MMAdViewSDK.Log.d("%s cannot be shown at this time.", ad.id);
            return 24;
        }
        return 100;
    }

    int check(MMAdView adView) {
        CachedAd ad = AdCache.loadNextCachedAd(adView.getContext(), adView.getCachedName());
        if (ad != null) {
            if (ad.canShow(adView.getContext(), adView, true)) {
                return 0;
            }
            return checkReason(adView, ad);
        }
        MMAdViewSDK.Log.i("No next ad.");
        return 20;
    }

    int display(MMAdView adView) {
        CachedAd ad = AdCache.loadNextCachedAd(adView.getContext(), adView.getCachedName());
        if (ad != null) {
            if (ad.canShow(adView.getContext(), adView, true)) {
                MMAdViewSDK.Event.displayStarted(adView.getContext(), adView);
                AdCache.setNextCachedAd(adView.getContext(), adView.getCachedName(), null);
                ad.show(adView.getContext(), adView);
                HandShake.sharedHandShake(adView.getContext()).updateLastVideoViewedTime(adView.getContext(), adView.adType);
                return 0;
            }
            return checkReason(adView, ad);
        }
        return 20;
    }

    private class MMJSInterface {
        private MMJSInterface() {
        }

        public void getUrl(String url) {
            MMAdViewController.this.nextUrl = url;
            MMAdViewSDK.Log.v("nextUrl: %s", MMAdViewController.this.nextUrl);
            if (MMAdViewController.this.nextUrl.toLowerCase().startsWith("mmvideo")) {
                MMAdViewController.this.settings.shouldLaunchToOverlay = true;
            }
        }

        public void shouldOpen(String url) {
            final MMAdView adView = MMAdViewController.this.adViewRef.get();
            if (adView == null) {
                MMAdViewSDK.Log.e("The reference to the ad view was broken.");
                return;
            }
            MMAdViewController.this.settings.shouldLaunchToOverlay = true;
            MMAdViewController.this.handleClick(url);
            if (adView.listener != null) {
                MMAdViewSDK.runOnUiThread(new Runnable() { // from class: com.millennialmedia.android.MMAdViewController.MMJSInterface.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            adView.listener.MMAdOverlayLaunched(adView);
                        } catch (Exception e) {
                            MMAdViewSDK.Log.w("Exception raised in your MMAdListener: ", e);
                        }
                    }
                });
            }
        }

        public void shouldOverlay(boolean shouldOverlay) {
            MMAdViewController.this.settings.shouldLaunchToOverlay = shouldOverlay;
        }

        public void overlayTitle(String title) {
            MMAdViewController.this.settings.overlayTitle = title;
        }

        public void overlayTransition(String transition, long time) {
            MMAdViewController.this.settings.overlayTransition = transition;
            MMAdViewController.this.settings.transitionTime = time;
        }

        public void shouldAccelerate(boolean shouldAccelerate) {
            MMAdView adView = MMAdViewController.this.adViewRef.get();
            if (adView != null) {
                MMAdViewController.this.settings.canAccelerate = shouldAccelerate;
            }
        }

        public void shouldResizeOverlay(int padding) {
            MMAdViewController.this.settings.shouldResizeOverlay = padding;
        }

        public void shouldShowTitlebar(boolean showTitlebar) {
            MMAdViewController.this.settings.shouldShowTitlebar = showTitlebar;
        }

        public void shouldShowBottomBar(boolean showBottomBar) {
            MMAdViewController.this.settings.shouldShowBottomBar = showBottomBar;
        }

        public void shouldEnableBottomBar(boolean enableBottomBar) {
            MMAdViewController.this.settings.shouldEnableBottomBar = enableBottomBar;
        }

        public void shouldMakeOverlayTransparent(boolean isTransparent) {
            MMAdViewController.this.settings.shouldMakeOverlayTransparent = isTransparent;
        }

        public void log(String message) {
            MMAdViewSDK.Log.d(message);
        }

        public void vibrate(int time) {
            MMAdView adView = MMAdViewController.this.adViewRef.get();
            if (adView != null) {
                try {
                    Context context = adView.getContext();
                    Vibrator vibrator = (Vibrator) context.getSystemService("vibrator");
                    vibrator.vibrate(time);
                } catch (SecurityException e) {
                    MMAdViewSDK.Log.w("Advertisement is trying to use vibrator but permissions are missing.");
                } catch (Exception e2) {
                }
            }
        }
    }
}
