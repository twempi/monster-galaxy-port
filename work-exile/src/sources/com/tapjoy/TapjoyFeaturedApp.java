package com.tapjoy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import oauth.signpost.OAuth;
import org.w3c.dom.Document;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class TapjoyFeaturedApp {
    private static TapjoyFeaturedAppNotifier featuredAppNotifier;
    public static String featuredAppURLParams;
    private static TapjoyURLConnection tapjoyURLConnection = null;
    private Context context;
    private String currencyID;
    private TapjoyFeaturedAppObject featuredAppObject = null;
    private int displayCount = 5;
    final String TAPJOY_FEATURED_APP = "Featured App";

    public TapjoyFeaturedApp(Context ctx) {
        this.context = ctx;
        tapjoyURLConnection = new TapjoyURLConnection();
    }

    public void getFeaturedApp(TapjoyFeaturedAppNotifier notifier) {
        TapjoyLog.i("Featured App", "Getting Featured App");
        getFeaturedApp(null, notifier);
    }

    public void getFeaturedApp(String theCurrencyID, TapjoyFeaturedAppNotifier notifier) {
        this.currencyID = theCurrencyID;
        TapjoyLog.i("Featured App", "Getting Featured App userID: " + TapjoyConnectCore.getUserID() + ", currencyID: " + this.currencyID);
        featuredAppNotifier = notifier;
        this.featuredAppObject = new TapjoyFeaturedAppObject();
        featuredAppURLParams = TapjoyConnectCore.getURLParams();
        featuredAppURLParams = String.valueOf(featuredAppURLParams) + "&publisher_user_id=" + TapjoyConnectCore.getUserID();
        if (this.currencyID != null) {
            featuredAppURLParams = String.valueOf(featuredAppURLParams) + "&currency_id=" + this.currencyID;
        }
        new Thread(new Runnable() { // from class: com.tapjoy.TapjoyFeaturedApp.1
            @Override // java.lang.Runnable
            public void run() {
                boolean returnValue = false;
                String response = TapjoyFeaturedApp.tapjoyURLConnection.connectToURL("https://ws.tapjoyads.com/get_offers/featured?", TapjoyFeaturedApp.featuredAppURLParams);
                if (response != null) {
                    returnValue = TapjoyFeaturedApp.this.buildResponse(response);
                }
                if (!returnValue) {
                    TapjoyFeaturedApp.featuredAppNotifier.getFeaturedAppResponseFailed("Error retrieving featured app data from the server.");
                }
            }
        }).start();
    }

    public void showFeaturedAppFullScreenAd() {
        String fullscreenURL = "";
        if (this.featuredAppObject != null) {
            fullscreenURL = this.featuredAppObject.fullScreenAdURL;
        }
        TapjoyLog.i("Featured App", "Displaying Full Screen AD with URL: " + fullscreenURL);
        if (fullscreenURL.length() != 0) {
            String urlParams = TapjoyConnectCore.getURLParams();
            if (this.currencyID != null && this.currencyID.length() > 0) {
                urlParams = String.valueOf(urlParams) + "&currency_id=" + this.currencyID;
            }
            Intent featuredAppIntent = new Intent(this.context, (Class<?>) TapjoyFeaturedAppWebView.class);
            featuredAppIntent.setFlags(268435456);
            featuredAppIntent.putExtra(TapjoyConstants.EXTRA_USER_ID, TapjoyConnectCore.getUserID());
            featuredAppIntent.putExtra(TapjoyConstants.EXTRA_URL_PARAMS, urlParams);
            featuredAppIntent.putExtra(TapjoyConstants.EXTRA_FEATURED_APP_FULLSCREEN_AD_URL, fullscreenURL);
            this.context.startActivity(featuredAppIntent);
        }
    }

    public void setDisplayCount(int count) {
        this.displayCount = count;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean buildResponse(String response) {
        boolean retValue = false;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            InputStream is = new ByteArrayInputStream(response.getBytes(OAuth.ENCODING));
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(is);
            this.featuredAppObject.cost = TapjoyUtil.getNodeTrimValue(document.getElementsByTagName("Cost"));
            String amount = TapjoyUtil.getNodeTrimValue(document.getElementsByTagName("Amount"));
            if (amount != null) {
                this.featuredAppObject.amount = Integer.parseInt(amount);
            }
            this.featuredAppObject.description = TapjoyUtil.getNodeTrimValue(document.getElementsByTagName("Description"));
            this.featuredAppObject.iconURL = TapjoyUtil.getNodeTrimValue(document.getElementsByTagName("IconURL"));
            this.featuredAppObject.name = TapjoyUtil.getNodeTrimValue(document.getElementsByTagName("Name"));
            this.featuredAppObject.redirectURL = TapjoyUtil.getNodeTrimValue(document.getElementsByTagName("RedirectURL"));
            this.featuredAppObject.storeID = TapjoyUtil.getNodeTrimValue(document.getElementsByTagName("StoreID"));
            this.featuredAppObject.fullScreenAdURL = TapjoyUtil.getNodeTrimValue(document.getElementsByTagName("FullScreenAdURL"));
            TapjoyLog.i("Featured App", "cost: " + this.featuredAppObject.cost);
            TapjoyLog.i("Featured App", "amount: " + this.featuredAppObject.amount);
            TapjoyLog.i("Featured App", "description: " + this.featuredAppObject.description);
            TapjoyLog.i("Featured App", "iconURL: " + this.featuredAppObject.iconURL);
            TapjoyLog.i("Featured App", "name: " + this.featuredAppObject.name);
            TapjoyLog.i("Featured App", "redirectURL: " + this.featuredAppObject.redirectURL);
            TapjoyLog.i("Featured App", "storeID: " + this.featuredAppObject.storeID);
            TapjoyLog.i("Featured App", "fullScreenAdURL: " + this.featuredAppObject.fullScreenAdURL);
            if (this.featuredAppObject.fullScreenAdURL == null || this.featuredAppObject.fullScreenAdURL.length() == 0) {
                retValue = false;
            } else {
                retValue = true;
            }
        } catch (Exception e) {
            TapjoyLog.e("Featured App", "Error parsing XML: " + e.toString());
        }
        if (retValue) {
            if (getDisplayCountForStoreID(this.featuredAppObject.storeID) < this.displayCount) {
                featuredAppNotifier.getFeaturedAppResponse(this.featuredAppObject);
                if (!TapjoyConnectCore.getAppID().equals(this.featuredAppObject.storeID)) {
                    incrementDisplayCountForStoreID(this.featuredAppObject.storeID);
                    return retValue;
                }
                return retValue;
            }
            featuredAppNotifier.getFeaturedAppResponseFailed("Featured App to display has exceeded display count");
            return retValue;
        }
        featuredAppNotifier.getFeaturedAppResponseFailed("Failed to parse XML file from response");
        return true;
    }

    private int getDisplayCountForStoreID(String storeID) {
        SharedPreferences settings = this.context.getSharedPreferences(TapjoyConstants.TJC_FEATURED_APP_PREFERENCE, 0);
        int count = settings.getInt(storeID, 0);
        TapjoyLog.i("Featured App", "getDisplayCount: " + count + ", storeID: " + storeID);
        return count;
    }

    private void incrementDisplayCountForStoreID(String storeID) {
        SharedPreferences settings = this.context.getSharedPreferences(TapjoyConstants.TJC_FEATURED_APP_PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        int count = settings.getInt(storeID, 0) + 1;
        TapjoyLog.i("Featured App", "incrementDisplayCount: " + count + ", storeID: " + storeID);
        editor.putInt(storeID, count);
        editor.commit();
    }

    public TapjoyFeaturedAppObject getFeaturedAppObject() {
        return this.featuredAppObject;
    }
}
