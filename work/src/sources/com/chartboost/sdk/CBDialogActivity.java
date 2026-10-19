package com.chartboost.sdk;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RelativeLayout;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class CBDialogActivity extends Activity {
    public static final String BUNDLE_KEY_CONFIGOBJECT = "bk_cfgo";
    public static final String BUNDLE_KEY_TYPE = "bk_type";
    public static final int OVERLAY_OPACITY = 75;
    public static final String TAG = "CBDialogActivity";
    protected JSONObject cbConfiguration;
    protected RelativeLayout cbContainer;
    protected CBWebView.CBViewType cbViewType;
    protected ChartBoost chartboost;
    protected Context ctx;
    private CBWebViewClient webViewClient = null;

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ctx = this;
        requestWindowFeature(1);
        if (Build.VERSION.SDK_INT > 8) {
            getWindow().setFlags(1024, 1024);
        }
        this.cbContainer = new RelativeLayout(this);
        this.cbContainer.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        this.cbContainer.setBackgroundColor(-16777216);
        this.cbContainer.getBackground().setAlpha(75);
        this.cbContainer.setGravity(49);
        if (!getIntent().hasExtra(BUNDLE_KEY_TYPE)) {
            finish();
        }
        int type = getIntent().getExtras().getInt(BUNDLE_KEY_TYPE);
        if (type == CBWebView.CBViewType.CBViewTypeInterstitial.ordinal()) {
            this.cbViewType = CBWebView.CBViewType.CBViewTypeInterstitial;
        } else if (type == CBWebView.CBViewType.CBViewTypeMoreApps.ordinal()) {
            this.cbViewType = CBWebView.CBViewType.CBViewTypeMoreApps;
        }
        this.chartboost = ChartBoost.getSharedChartBoost(this.ctx);
        try {
            this.cbConfiguration = new JSONObject(getIntent().getExtras().getString(BUNDLE_KEY_CONFIGOBJECT));
        } catch (JSONException e) {
            finish();
        }
        this.cbContainer.post(new Runnable() { // from class: com.chartboost.sdk.CBDialogActivity.1
            @Override // java.lang.Runnable
            public void run() {
                CBDialogActivity.this.display();
            }
        });
        setContentView(this.cbContainer);
    }

    public void close() {
        finish();
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        this.cbContainer.removeAllViews();
        this.cbContainer = null;
        super.onDestroy();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void display() {
        this.webViewClient = this.chartboost.loadIntoView(this.cbContainer, this.cbConfiguration, this.cbViewType);
        this.webViewClient.dialogActivity = this;
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && this.webViewClient != null) {
            this.webViewClient.handleChartBoostRequest("chartboost://close");
        }
        return super.onKeyDown(keyCode, event);
    }
}
