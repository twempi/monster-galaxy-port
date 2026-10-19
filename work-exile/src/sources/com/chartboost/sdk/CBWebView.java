package com.chartboost.sdk;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import org.json.JSONObject;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class CBWebView extends WebView {
    private JSONObject responseContext;
    private CBViewState state;
    private CBViewType type;

    public enum CBViewState {
        CBViewStateOther,
        CBViewStateWaitingForDisplay,
        CBViewStateDisplayedByDefaultController,
        CBViewStateWaitingForDismissal;

        /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
        public static CBViewState[] valuesCustom() {
            CBViewState[] cBViewStateArrValuesCustom = values();
            int length = cBViewStateArrValuesCustom.length;
            CBViewState[] cBViewStateArr = new CBViewState[length];
            System.arraycopy(cBViewStateArrValuesCustom, 0, cBViewStateArr, 0, length);
            return cBViewStateArr;
        }
    }

    public enum CBViewType {
        CBViewTypeInterstitial,
        CBViewTypeMoreApps;

        /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
        public static CBViewType[] valuesCustom() {
            CBViewType[] cBViewTypeArrValuesCustom = values();
            int length = cBViewTypeArrValuesCustom.length;
            CBViewType[] cBViewTypeArr = new CBViewType[length];
            System.arraycopy(cBViewTypeArrValuesCustom, 0, cBViewTypeArr, 0, length);
            return cBViewTypeArr;
        }
    }

    public CBWebView(Context context, CBViewType type) {
        super(context);
        setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        if (type == CBViewType.CBViewTypeMoreApps) {
            setBackgroundColor(-13421773);
        } else {
            setBackgroundColor(-1728053248);
        }
        getSettings().setJavaScriptEnabled(true);
        this.type = type;
        if (type == CBViewType.CBViewTypeInterstitial) {
            setOnTouchListener(new View.OnTouchListener() { // from class: com.chartboost.sdk.CBWebView.1
                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View v, MotionEvent event) {
                    return event.getAction() == 2;
                }
            });
        }
    }

    public JSONObject getResponseContext() {
        return this.responseContext;
    }

    public void setResponseContext(JSONObject responseContext) {
        this.responseContext = responseContext;
    }

    public CBViewState getState() {
        return this.state;
    }

    public void setState(CBViewState state) {
        this.state = state;
    }

    public CBViewType getType() {
        return this.type;
    }

    public void setType(CBViewType type) {
        this.type = type;
    }
}
