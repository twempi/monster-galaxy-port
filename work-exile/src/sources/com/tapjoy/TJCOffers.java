package com.tapjoy;

import android.content.Context;
import android.content.Intent;
import java.util.UUID;
import org.w3c.dom.Document;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class TJCOffers {
    public static final String TAPJOY_OFFERS = "TapjoyOffers";
    public static final String TAPJOY_POINTS = "TapjoyPoints";
    private static TapjoyAwardPointsNotifier tapjoyAwardPointsNotifier;
    private static TapjoyEarnedPointsNotifier tapjoyEarnedPointsNotifier;
    private static TapjoyNotifier tapjoyNotifier;
    private static TapjoySpendPointsNotifier tapjoySpendPointsNotifier;
    Context context;
    String spendTapPoints = null;
    int awardTapPoints = 0;
    private String multipleCurrencyID = "";
    private String multipleCurrencySelector = "";

    public TJCOffers(Context applicationContext) {
        this.context = applicationContext;
    }

    public void showOffers() {
        TapjoyLog.i(TAPJOY_OFFERS, "Showing offers with userID: " + TapjoyConnectCore.getUserID());
        Intent offersIntent = new Intent(this.context, (Class<?>) TJCOffersWebView.class);
        offersIntent.setFlags(268435456);
        offersIntent.putExtra(TapjoyConstants.EXTRA_USER_ID, TapjoyConnectCore.getUserID());
        offersIntent.putExtra(TapjoyConstants.EXTRA_URL_PARAMS, TapjoyConnectCore.getURLParams());
        this.context.startActivity(offersIntent);
    }

    public void showOffersWithCurrencyID(String currencyID, boolean enableCurrencySelector) {
        TapjoyLog.i(TAPJOY_OFFERS, "Showing offers with currencyID: " + currencyID + ", selector: " + enableCurrencySelector + " (userID = " + TapjoyConnectCore.getUserID() + ")");
        this.multipleCurrencyID = currencyID;
        this.multipleCurrencySelector = enableCurrencySelector ? "1" : "0";
        String offersURLParams = TapjoyConnectCore.getURLParams();
        String offersURLParams2 = String.valueOf(String.valueOf(offersURLParams) + "&currency_id=" + this.multipleCurrencyID) + "&currency_selector=" + this.multipleCurrencySelector;
        Intent offersIntent = new Intent(this.context, (Class<?>) TJCOffersWebView.class);
        offersIntent.setFlags(268435456);
        offersIntent.putExtra(TapjoyConstants.EXTRA_USER_ID, TapjoyConnectCore.getUserID());
        offersIntent.putExtra(TapjoyConstants.EXTRA_URL_PARAMS, offersURLParams2);
        this.context.startActivity(offersIntent);
    }

    public void getTapPoints(TapjoyNotifier notifier) {
        tapjoyNotifier = notifier;
        new Thread(new Runnable() { // from class: com.tapjoy.TJCOffers.1
            @Override // java.lang.Runnable
            public void run() {
                boolean returnValue = false;
                String url_params = TapjoyConnectCore.getURLParams();
                String result = new TapjoyURLConnection().connectToURL("https://ws.tapjoyads.com/get_vg_store_items/user_account?", String.valueOf(url_params) + "&publisher_user_id=" + TapjoyConnectCore.getUserID());
                if (result != null) {
                    returnValue = TJCOffers.this.handleGetPointsResponse(result);
                }
                if (!returnValue) {
                    TJCOffers.tapjoyNotifier.getUpdatePointsFailed("Failed to retrieve points from server");
                }
            }
        }).start();
    }

    public void spendTapPoints(int amount, TapjoySpendPointsNotifier notifier) {
        if (amount < 0) {
            TapjoyLog.e(TAPJOY_POINTS, "spendTapPoints error: amount must be a positive number");
            return;
        }
        this.spendTapPoints = new StringBuilder().append(amount).toString();
        tapjoySpendPointsNotifier = notifier;
        new Thread(new Runnable() { // from class: com.tapjoy.TJCOffers.2
            @Override // java.lang.Runnable
            public void run() {
                boolean returnValue = false;
                String url_params = TapjoyConnectCore.getURLParams();
                String result = new TapjoyURLConnection().connectToURL("https://ws.tapjoyads.com/points/spend?", String.valueOf(String.valueOf(url_params) + "&tap_points=" + TJCOffers.this.spendTapPoints) + "&publisher_user_id=" + TapjoyConnectCore.getUserID());
                if (result != null) {
                    returnValue = TJCOffers.this.handleSpendPointsResponse(result);
                }
                if (!returnValue) {
                    TJCOffers.tapjoySpendPointsNotifier.getSpendPointsResponseFailed("Failed to spend points.");
                }
            }
        }).start();
    }

    public void awardTapPoints(int amount, TapjoyAwardPointsNotifier notifier) {
        if (amount < 0) {
            TapjoyLog.e(TAPJOY_POINTS, "spendTapPoints error: amount must be a positive number");
            return;
        }
        this.awardTapPoints = amount;
        tapjoyAwardPointsNotifier = notifier;
        new Thread(new Runnable() { // from class: com.tapjoy.TJCOffers.3
            @Override // java.lang.Runnable
            public void run() {
                boolean returnValue = false;
                String guid = UUID.randomUUID().toString();
                long time = System.currentTimeMillis() / 1000;
                String url_params = TapjoyConnectCore.getURLParams();
                String result = new TapjoyURLConnection().connectToURL("https://ws.tapjoyads.com/points/award?", String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(url_params) + "&tap_points=" + TJCOffers.this.awardTapPoints) + "&publisher_user_id=" + TapjoyConnectCore.getUserID()) + "&guid=" + guid) + "&timestamp=" + time) + "&verifier=" + TapjoyConnectCore.getAwardPointsVerifier(time, TJCOffers.this.awardTapPoints, guid));
                if (result != null) {
                    returnValue = TJCOffers.this.handleAwardPointsResponse(result);
                }
                if (!returnValue) {
                    TJCOffers.tapjoyAwardPointsNotifier.getAwardPointsResponseFailed("Failed to award points.");
                }
            }
        }).start();
    }

    public void setEarnedPointsNotifier(TapjoyEarnedPointsNotifier notifier) {
        tapjoyEarnedPointsNotifier = notifier;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean handleGetPointsResponse(String response) {
        boolean z;
        Document document = TapjoyUtil.buildDocument(response);
        if (document != null) {
            String nodeValue = TapjoyUtil.getNodeTrimValue(document.getElementsByTagName("Success"));
            if (nodeValue != null && nodeValue.equals("true")) {
                String points = TapjoyUtil.getNodeTrimValue(document.getElementsByTagName("TapPoints"));
                String name = TapjoyUtil.getNodeTrimValue(document.getElementsByTagName("CurrencyName"));
                if (points != null && name != null) {
                    int pointTotal = Integer.parseInt(points);
                    int lastLocalPointTotal = TapjoyConnectCore.getLocalTapPointsTotal();
                    if (tapjoyEarnedPointsNotifier != null && lastLocalPointTotal != -9999 && pointTotal > lastLocalPointTotal) {
                        TapjoyLog.i(TAPJOY_POINTS, "earned: " + (pointTotal - lastLocalPointTotal));
                        tapjoyEarnedPointsNotifier.earnedTapPoints(pointTotal - lastLocalPointTotal);
                    }
                    TapjoyConnectCore.saveTapPointsTotal(Integer.parseInt(points));
                    tapjoyNotifier.getUpdatePoints(name, Integer.parseInt(points));
                    z = true;
                } else {
                    TapjoyLog.e(TAPJOY_POINTS, "Invalid XML: Missing tags.");
                }
            } else {
                TapjoyLog.e(TAPJOY_POINTS, "Invalid XML: Missing <Success> tag.");
            }
            z = false;
        } else {
            z = false;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean handleSpendPointsResponse(String response) {
        Document document = TapjoyUtil.buildDocument(response);
        if (document != null) {
            String nodeValue = TapjoyUtil.getNodeTrimValue(document.getElementsByTagName("Success"));
            if (nodeValue != null && nodeValue.equals("true")) {
                String pointsTotal = TapjoyUtil.getNodeTrimValue(document.getElementsByTagName("TapPoints"));
                String currencyName = TapjoyUtil.getNodeTrimValue(document.getElementsByTagName("CurrencyName"));
                if (pointsTotal != null && currencyName != null) {
                    TapjoyConnectCore.saveTapPointsTotal(Integer.parseInt(pointsTotal));
                    tapjoySpendPointsNotifier.getSpendPointsResponse(currencyName, Integer.parseInt(pointsTotal));
                    return true;
                }
                TapjoyLog.e(TAPJOY_POINTS, "Invalid XML: Missing tags.");
            } else {
                if (nodeValue != null && nodeValue.endsWith("false")) {
                    String message = TapjoyUtil.getNodeTrimValue(document.getElementsByTagName("Message"));
                    TapjoyLog.i(TAPJOY_POINTS, message);
                    tapjoySpendPointsNotifier.getSpendPointsResponseFailed(message);
                    return true;
                }
                TapjoyLog.e(TAPJOY_POINTS, "Invalid XML: Missing <Success> tag.");
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean handleAwardPointsResponse(String response) {
        Document document = TapjoyUtil.buildDocument(response);
        if (document != null) {
            String nodeValue = TapjoyUtil.getNodeTrimValue(document.getElementsByTagName("Success"));
            if (nodeValue != null && nodeValue.equals("true")) {
                String pointsTotal = TapjoyUtil.getNodeTrimValue(document.getElementsByTagName("TapPoints"));
                String currencyName = TapjoyUtil.getNodeTrimValue(document.getElementsByTagName("CurrencyName"));
                if (pointsTotal != null && currencyName != null) {
                    TapjoyConnectCore.saveTapPointsTotal(Integer.parseInt(pointsTotal));
                    tapjoyAwardPointsNotifier.getAwardPointsResponse(currencyName, Integer.parseInt(pointsTotal));
                    return true;
                }
                TapjoyLog.e(TAPJOY_POINTS, "Invalid XML: Missing tags.");
            } else {
                if (nodeValue != null && nodeValue.endsWith("false")) {
                    String message = TapjoyUtil.getNodeTrimValue(document.getElementsByTagName("Message"));
                    TapjoyLog.i(TAPJOY_POINTS, message);
                    tapjoyAwardPointsNotifier.getAwardPointsResponseFailed(message);
                    return true;
                }
                TapjoyLog.e(TAPJOY_POINTS, "Invalid XML: Missing <Success> tag.");
            }
        }
        return false;
    }
}
