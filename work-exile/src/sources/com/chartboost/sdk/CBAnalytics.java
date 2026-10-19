package com.chartboost.sdk;

import android.app.Activity;
import android.content.Context;
import com.tapjoy.TapjoyConstants;
import java.math.BigDecimal;
import java.util.Map;
import org.json.JSONObject;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class CBAnalytics {
    public static final String TAG = "ChartBoost Analytics";
    private static CBAnalytics sharedAnalytics = null;
    private Activity activity;
    private Context context;

    public static synchronized CBAnalytics getSharedAnalytics(Activity activity) {
        if (sharedAnalytics == null) {
            sharedAnalytics = new CBAnalytics();
        }
        sharedAnalytics.context = activity;
        sharedAnalytics.activity = activity;
        return sharedAnalytics;
    }

    private CBAnalytics() {
    }

    private class TransactionConnection extends CBAPIConnection {
        public TransactionConnection(Context context) {
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

    private String round(double unrounded, int precision, int roundingMode) {
        BigDecimal bd = new BigDecimal(unrounded);
        BigDecimal rounded = bd.setScale(precision, roundingMode);
        return new StringBuilder(String.valueOf(rounded.doubleValue())).toString();
    }

    public Boolean recordPaymentTransaction(String sku, String title, double price, String currency, int quantity, Map<String, Object> meta) {
        try {
            ChartBoost cb = ChartBoost.getSharedChartBoost(this.activity);
            if (cb.getAppId() == null || cb.getAppSignature() == null) {
                return false;
            }
            CBAPIRequest request = new CBAPIRequest(this.activity, "api", "purchase");
            request.appendDeviceInfoParams();
            request.appendBodyArgument("product_id", sku);
            request.appendBodyArgument("title", title);
            request.appendBodyArgument("price", round(price, 2, 4));
            request.appendBodyArgument("currency", currency);
            request.appendBodyArgument("quantity", new StringBuilder(String.valueOf(quantity)).toString());
            request.appendBodyArgument(TapjoyConstants.TJC_TIMESTAMP, new StringBuilder(String.valueOf(System.currentTimeMillis() / 1000.0d)).toString());
            if (meta != null) {
                JSONObject jsonObj = new JSONObject(meta);
                request.appendBodyArgument("meta", jsonObj.toString());
            }
            request.sign(cb.getAppId(), cb.getAppSignature());
            TransactionConnection tc = new TransactionConnection(this.context);
            tc.execute(new CBAPIRequest[]{request});
            return true;
        } catch (Exception e) {
        }
    }

    public Boolean trackEvent(String eventIdentifier) {
        trackEvent(eventIdentifier, 1.0d, null);
        return true;
    }

    public Boolean trackEvent(String eventIdentifier, double value) {
        trackEvent(eventIdentifier, value, null);
        return true;
    }

    public Boolean trackEvent(String eventIdentifier, double value, Map<String, Object> meta) {
        try {
            ChartBoost cb = ChartBoost.getSharedChartBoost(this.activity);
            if (cb.getAppId() == null || cb.getAppSignature() == null) {
                return false;
            }
            CBAPIRequest request = new CBAPIRequest(this.activity, "api", "event");
            request.appendDeviceInfoParams();
            request.appendBodyArgument("key", eventIdentifier);
            request.appendBodyArgument("value", new StringBuilder(String.valueOf(value)).toString());
            request.appendBodyArgument(TapjoyConstants.TJC_TIMESTAMP, new StringBuilder(String.valueOf(System.currentTimeMillis() / 1000.0d)).toString());
            if (meta != null) {
                JSONObject jsonObj = new JSONObject(meta);
                request.appendBodyArgument("meta", jsonObj.toString());
            }
            request.sign(cb.getAppId(), cb.getAppSignature());
            TransactionConnection tc = new TransactionConnection(this.context);
            tc.execute(new CBAPIRequest[]{request});
            return true;
        } catch (Exception e) {
        }
    }
}
