package com.chartboost.sdk;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.sessionm.ui.SessionMActivity;
import java.net.URI;
import java.net.URLDecoder;
import oauth.signpost.OAuth;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class CBWebViewClient extends WebViewClient {
    public static final String TAG = "ChartBoost AdView";
    private ChartBoost cb;
    private ViewGroup cbContainer;
    private Context context;
    public CBWebView webView = null;
    public CBDialogActivity dialogActivity = null;

    public CBWebViewClient(Context context, ViewGroup container) {
        this.context = null;
        this.cb = null;
        this.context = context;
        this.cbContainer = container;
        this.cb = ChartBoost.getSharedChartBoost(context);
    }

    @Override // android.webkit.WebViewClient
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override // android.webkit.WebViewClient
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        CBWebView chartBoostView = (CBWebView) view;
        if (chartBoostView.getState() == CBWebView.CBViewState.CBViewStateWaitingForDisplay) {
            chartBoostView.setState(CBWebView.CBViewState.CBViewStateOther);
            if (chartBoostView.getType() == CBWebView.CBViewType.CBViewTypeInterstitial) {
                if (this.cbContainer != null) {
                    Log.i(TAG, "Got an interstitial, adding to view!");
                    this.cbContainer.removeAllViews();
                    this.cbContainer.addView(chartBoostView);
                    this.cbContainer.invalidate();
                }
            } else if (chartBoostView.getType() == CBWebView.CBViewType.CBViewTypeMoreApps && this.cbContainer != null) {
                Log.i(TAG, "Got More Apps, adding to view!");
                this.cbContainer.removeAllViews();
                this.cbContainer.addView(chartBoostView);
                this.cbContainer.invalidate();
            }
            chartBoostView.setState(CBWebView.CBViewState.CBViewStateDisplayedByDefaultController);
        }
    }

    @Override // android.webkit.WebViewClient
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        try {
            URI uri = new URI(url);
            if (uri.getScheme().equals("file") || !uri.getScheme().equals("chartboost")) {
                return false;
            }
            handleChartBoostRequest(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected void handleChartBoostRequest(String url) {
        Log.i(TAG, "Handling chartboost:// request: " + url);
        if (this.webView.getType() == CBWebView.CBViewType.CBViewTypeInterstitial) {
            handleChartBoostRequestForInterstitial(url);
        } else if (this.webView.getType() == CBWebView.CBViewType.CBViewTypeMoreApps) {
            handleChartBoostRequestForMoreApps(url);
        }
    }

    private void handleChartBoostRequestForMoreApps(String url) {
        String[] items = url.split("/");
        String function = items[2];
        if (function.equals("close")) {
            if (this.cb.getDelegate() != null) {
                this.cb.getDelegate().didCloseMoreApps(this.webView);
            }
            closeView();
            return;
        }
        if (function.equals("link")) {
            if (this.cb.getDelegate() != null) {
                this.cb.getDelegate().didClickMoreApps(this.webView);
            }
            try {
                CBAPIRequest request = new CBAPIRequest(this.context, "api", "click");
                request.appendDeviceInfoParams();
                JSONObject embeddedData = new JSONObject(new JSONTokener(URLDecoder.decode(items[4], OAuth.ENCODING)));
                try {
                    request.appendBodyArgument("to", embeddedData.getString("to"));
                } catch (JSONException e) {
                }
                try {
                    request.appendBodyArgument("cgn", embeddedData.getString("cgn"));
                } catch (JSONException e2) {
                }
                try {
                    request.appendBodyArgument("creative", embeddedData.getString("creative"));
                } catch (JSONException e3) {
                }
                try {
                    request.appendBodyArgument(SessionMActivity.INTENT_PARAM_TYPE, embeddedData.getString(SessionMActivity.INTENT_PARAM_TYPE));
                } catch (JSONException e4) {
                }
                try {
                    request.appendBodyArgument("more_type", embeddedData.getString("more_type"));
                } catch (JSONException e5) {
                }
                request.sign(this.cb.getAppId(), this.cb.getAppSignature());
                RegisterClickConnection rcc = new RegisterClickConnection(this.context);
                rcc.shouldShowProgress = true;
                rcc.data = URLDecoder.decode(items[3], OAuth.ENCODING);
                rcc.execute(new CBAPIRequest[]{request});
            } catch (Exception e6) {
                e6.printStackTrace();
            }
        }
    }

    private void handleChartBoostRequestForInterstitial(String url) {
        Log.w("cb", "handleChartBoostRequestForInterstitial");
        String[] items = url.split("/");
        String function = items[2];
        Log.w("cb", "function: " + function);
        if (function.equals("close")) {
            if (this.cb.getDelegate() != null) {
                this.cb.getDelegate().didCloseInterstitial(this.webView);
            }
            closeView();
            return;
        }
        if (function.equals("link")) {
            if (this.cb.getDelegate() != null) {
                this.cb.getDelegate().didClickInterstitial(this.webView);
            }
            try {
                CBAPIRequest request = new CBAPIRequest(this.context, "api", "click");
                request.appendDeviceInfoParams();
                request.appendBodyArgument("to", this.webView.getResponseContext().getString("to"));
                request.appendBodyArgument("cgn", this.webView.getResponseContext().getString("cgn"));
                request.appendBodyArgument("creative", this.webView.getResponseContext().getString("creative"));
                request.sign(this.cb.getAppId(), this.cb.getAppSignature());
                Log.w("cb", "decoding: " + items[3]);
                RegisterClickConnection rcc = new RegisterClickConnection(this.context);
                rcc.shouldShowProgress = true;
                rcc.data = URLDecoder.decode(items[3], OAuth.ENCODING);
                rcc.execute(new CBAPIRequest[]{request});
                Log.w("cb", "sending request click request");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class RegisterClickConnection extends CBAPIConnection {
        public RegisterClickConnection(Context context) {
            super(context);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.chartboost.sdk.CBAPIConnection
        public void onPostExecute(JSONObject json) {
            try {
                validateJson(json);
                if (this.data != null && (this.data instanceof String)) {
                    CBWebViewClient.this.openUrl((String) this.data);
                }
            } catch (Exception e) {
            } finally {
                super.onPostExecute(json);
            }
            if (CBWebViewClient.this.webView.getState() == CBWebView.CBViewState.CBViewStateDisplayedByDefaultController) {
                Log.w("cb", "closing interstitial ad");
                CBWebViewClient.this.closeView();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openUrl(String url) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(url));
        Log.i(TAG, "Sideloading URL: " + url);
        this.context.startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeView() {
        if (this.cbContainer != null) {
            ViewManager view = (ViewManager) this.webView.getParent();
            if (view != null) {
                view.removeView(this.webView);
            }
            this.cbContainer = null;
            if (this.dialogActivity != null) {
                this.dialogActivity.close();
            }
        }
    }
}
