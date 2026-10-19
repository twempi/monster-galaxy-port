package com.chartboost.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ViewGroup;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class ChartBoost {
    public static final String DEFAULT_LOCATION = "Default";
    public static final String TAG = "ChartBoost";
    private static ChartBoost sharedChartBoost = null;
    private static int timeoutConnect = 30000;
    private static int timeoutRead = 30000;
    private String appId;
    private String appSignature;
    private Context context;
    private ChartBoostDelegate delegate;
    private ViewGroup interstitialDisplayContainer = null;
    private ViewGroup moreAppsDisplayContainer = null;
    public long preloadTime = 0;
    private Map<String, JSONObject> cacheMap = new HashMap();
    private Map<String, LoadDataConnection> connectionMap = new HashMap();

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSignature() {
        return this.appSignature;
    }

    public void setAppSignature(String appSignature) {
        this.appSignature = appSignature;
    }

    public ChartBoostDelegate getDelegate() {
        return this.delegate;
    }

    public void setDelegate(ChartBoostDelegate delegate) {
        this.delegate = delegate;
    }

    public void setInterstitialDisplayContainer(ViewGroup container) {
        this.interstitialDisplayContainer = container;
    }

    public void setMoreAppsDisplayContainer(ViewGroup container) {
        this.moreAppsDisplayContainer = container;
    }

    private ViewGroup displayContainerForType(CBWebView.CBViewType type) {
        return type == CBWebView.CBViewType.CBViewTypeInterstitial ? this.interstitialDisplayContainer : this.moreAppsDisplayContainer;
    }

    public static synchronized ChartBoost getSharedChartBoost(Context context) {
        if (sharedChartBoost == null) {
            sharedChartBoost = new ChartBoost();
        }
        if (!(context instanceof Activity)) {
            Log.e(TAG, "Chartboost context should be instance of activity");
        }
        sharedChartBoost.context = context;
        return sharedChartBoost;
    }

    private ChartBoost() {
    }

    public void install() {
        try {
            CBAPIRequest request = new CBAPIRequest(this.context, "api", "install");
            request.appendDeviceInfoParams();
            request.sign(this.appId, this.appSignature);
            GenericConnection gc = new GenericConnection(this.context);
            gc.execute(new CBAPIRequest[]{request});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class GenericConnection extends CBAPIConnection {
        public GenericConnection(Context context) {
            super(context);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.chartboost.sdk.CBAPIConnection
        public void onPostExecute(JSONObject json) {
            try {
                validateJson(json);
            } catch (Exception e) {
            } finally {
                super.onPostExecute(json);
            }
        }
    }

    public void showInterstitial() {
        showView(CBWebView.CBViewType.CBViewTypeInterstitial, DEFAULT_LOCATION, false);
    }

    public void showInterstitial(String location) {
        showView(CBWebView.CBViewType.CBViewTypeInterstitial, location, false);
    }

    public void cacheInterstitial() {
        showView(CBWebView.CBViewType.CBViewTypeInterstitial, DEFAULT_LOCATION, true);
    }

    public void cacheInterstitial(String location) {
        showView(CBWebView.CBViewType.CBViewTypeInterstitial, location, true);
    }

    public void showMoreApps() {
        showView(CBWebView.CBViewType.CBViewTypeMoreApps, DEFAULT_LOCATION, false);
    }

    public void cacheMoreApps() {
        showView(CBWebView.CBViewType.CBViewTypeMoreApps, DEFAULT_LOCATION, true);
    }

    public boolean hasCachedMoreApps() {
        return cacheExists(CBWebView.CBViewType.CBViewTypeMoreApps, DEFAULT_LOCATION);
    }

    public boolean hasCachedInterstitial() {
        return cacheExists(CBWebView.CBViewType.CBViewTypeInterstitial, DEFAULT_LOCATION);
    }

    public boolean hasCachedInterstitial(String location) {
        return cacheExists(CBWebView.CBViewType.CBViewTypeInterstitial, location);
    }

    private boolean cacheExists(CBWebView.CBViewType viewType, String location) {
        return getCache(viewType, location) != null;
    }

    private JSONObject getCache(CBWebView.CBViewType viewType, String location) {
        return this.cacheMap.get(String.valueOf(location) + viewType);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void putCache(CBWebView.CBViewType viewType, String location, JSONObject object) {
        String key = String.valueOf(location) + viewType;
        if (object != null) {
            this.cacheMap.put(key, object);
        } else {
            this.cacheMap.remove(key);
        }
    }

    private LoadDataConnection getConnection(CBWebView.CBViewType viewType, String location) {
        return this.connectionMap.get(String.valueOf(location) + viewType);
    }

    private void putConnection(CBWebView.CBViewType viewType, String location, LoadDataConnection object) {
        String key = String.valueOf(location) + viewType;
        if (object != null) {
            this.connectionMap.put(key, object);
        } else {
            this.connectionMap.remove(key);
        }
    }

    private class LoadDataConnection extends CBAPIConnection {
        public boolean hadError;
        public String location;
        public boolean shouldCache;
        public CBWebView.CBViewType viewType;

        public LoadDataConnection(Context context) {
            super(context);
            this.shouldCache = false;
            this.hadError = false;
            this.location = ChartBoost.DEFAULT_LOCATION;
            this.viewType = CBWebView.CBViewType.CBViewTypeInterstitial;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.chartboost.sdk.CBAPIConnection
        public void onPostExecute(JSONObject json) {
            try {
                validateJson(json);
            } catch (Exception e) {
                ChartBoost.this.invokeCallbackFailed(this.viewType, this.location);
                this.hadError = true;
            } finally {
                super.onPostExecute(json);
            }
            if (!this.hadError) {
                ChartBoost.this.putCache(this.viewType, this.location, json);
                if (this.shouldCache) {
                    return;
                }
                ChartBoost.this.showCache(this.viewType, this.location);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeCallbackFailed(CBWebView.CBViewType viewType, String location) {
        putCache(viewType, location, null);
        putConnection(viewType, location, null);
        if (this.delegate != null) {
            if (viewType == CBWebView.CBViewType.CBViewTypeInterstitial) {
                this.delegate.didFailToLoadInterstitial();
            }
            if (viewType == CBWebView.CBViewType.CBViewTypeMoreApps) {
                this.delegate.didFailToLoadMoreApps();
            }
        }
    }

    private void showView(CBWebView.CBViewType viewType, String location, Boolean shouldCache) {
        Boolean isInterstitial = Boolean.valueOf(viewType == CBWebView.CBViewType.CBViewTypeInterstitial);
        if (cacheExists(viewType, location)) {
            showCache(viewType, location);
            return;
        }
        if (this.delegate != null) {
            Boolean shouldShowView = Boolean.valueOf(isInterstitial.booleanValue() ? this.delegate.shouldRequestInterstitial() : this.delegate.shouldRequestMoreApps());
            if (!shouldShowView.booleanValue()) {
                return;
            }
        }
        LoadDataConnection ldc = getConnection(viewType, location);
        if (ldc == null) {
            CBAPIRequest request = new CBAPIRequest(this.context, "api", isInterstitial.booleanValue() ? "get" : "more");
            try {
                request.appendDeviceInfoParams(displayContainerForType(viewType));
                if (isInterstitial.booleanValue()) {
                    request.appendBodyArgument("location", location);
                }
                request.sign(this.appId, this.appSignature);
                LoadDataConnection ldc2 = new LoadDataConnection(this.context);
                try {
                    ldc2.shouldCache = shouldCache.booleanValue();
                    if (viewType == CBWebView.CBViewType.CBViewTypeMoreApps && !shouldCache.booleanValue()) {
                        if (this.delegate != null) {
                            ldc2.shouldShowProgress = this.delegate.shouldDisplayLoadingViewForMoreApps();
                        } else {
                            ldc2.shouldShowProgress = true;
                        }
                    }
                    ldc2.viewType = viewType;
                    ldc2.location = location;
                    putConnection(viewType, location, ldc2);
                    ldc2.execute(new CBAPIRequest[]{request});
                    return;
                } catch (Exception e) {
                    return;
                }
            } catch (Exception e2) {
                return;
            }
        }
        if (!shouldCache.booleanValue()) {
            ldc.shouldCache = shouldCache.booleanValue();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showCache(CBWebView.CBViewType viewType, String location) {
        JSONObject cache = getCache(viewType, location);
        putCache(viewType, location, null);
        putConnection(viewType, location, null);
        try {
            if (viewType == CBWebView.CBViewType.CBViewTypeInterstitial) {
                CBAPIRequest request = new CBAPIRequest(this.context, "api", "show");
                request.appendDeviceInfoParams();
                request.appendBodyArgument(CBAPIRequest.CB_PARAM_AD_ID, cache.optString(CBAPIRequest.CB_PARAM_AD_ID, ""));
                request.sign(this.appId, this.appSignature);
                GenericConnection gc = new GenericConnection(this.context);
                gc.execute(new CBAPIRequest[]{request});
            }
        } catch (Exception e) {
            Log.e(TAG, "error generating request!");
        }
        loadChartBoostView(cache, viewType);
    }

    private void loadChartBoostView(JSONObject response, CBWebView.CBViewType viewType) {
        Boolean shouldDisplay = true;
        ViewGroup displayContainer = displayContainerForType(viewType);
        if (this.delegate != null) {
            if (viewType == CBWebView.CBViewType.CBViewTypeInterstitial) {
                shouldDisplay = Boolean.valueOf(this.delegate.shouldDisplayInterstitial(displayContainer));
            } else if (viewType == CBWebView.CBViewType.CBViewTypeMoreApps) {
                shouldDisplay = Boolean.valueOf(this.delegate.shouldDisplayMoreApps(displayContainer));
            }
        }
        if (shouldDisplay.booleanValue()) {
            if (displayContainer != null) {
                loadIntoView(displayContainer, response, viewType);
                return;
            }
            Intent i = new Intent(this.context, (Class<?>) CBDialogActivity.class);
            i.putExtra(CBDialogActivity.BUNDLE_KEY_TYPE, viewType.ordinal());
            i.putExtra(CBDialogActivity.BUNDLE_KEY_CONFIGOBJECT, response.toString());
            this.context.startActivity(i);
        }
    }

    protected CBWebViewClient loadIntoView(ViewGroup view, JSONObject cbConfiguration, CBWebView.CBViewType type) {
        CBWebView webView = null;
        if (type == CBWebView.CBViewType.CBViewTypeInterstitial) {
            webView = new CBWebView(this.context, CBWebView.CBViewType.CBViewTypeInterstitial);
        } else if (type == CBWebView.CBViewType.CBViewTypeMoreApps) {
            webView = new CBWebView(this.context, CBWebView.CBViewType.CBViewTypeMoreApps);
        }
        CBWebViewClient webViewClient = new CBWebViewClient(this.context, view);
        webView.setState(CBWebView.CBViewState.CBViewStateWaitingForDisplay);
        webView.setResponseContext(cbConfiguration);
        webView.setWebViewClient(webViewClient);
        webViewClient.webView = webView;
        try {
            String cbHtmlContent = cbConfiguration.optString("html");
            if (cbHtmlContent == null || cbHtmlContent.length() == 0) {
                throw new Exception("html content is blank!");
            }
            webView.loadDataWithBaseURL("file:///android_asset/", cbHtmlContent, "text/html", "utf-8", null);
            return webViewClient;
        } catch (Exception e) {
        }
    }

    public Context getContext() {
        return this.context;
    }

    public void clearCache() {
        this.cacheMap = new HashMap();
        this.connectionMap = new HashMap();
        this.preloadTime = 0L;
    }

    public void setConnectionTimeout(int time_millis) {
        if (time_millis < 10000) {
            Log.w(TAG, "Timeout less than minimum of 10000 milliseconds");
        }
        timeoutConnect = Math.max(time_millis, CBAPIConnection.MIN_TIMEOUT);
    }

    public void setReadTimeout(int time_millis) {
        if (time_millis < 10000) {
            Log.w(TAG, "Timeout less than minimum of 10000 milliseconds");
        }
        timeoutRead = Math.max(time_millis, CBAPIConnection.MIN_TIMEOUT);
    }

    public int getTimeoutConnect() {
        return timeoutConnect;
    }

    public int getTimeoutRead() {
        return timeoutRead;
    }
}
