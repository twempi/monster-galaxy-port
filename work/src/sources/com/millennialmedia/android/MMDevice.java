package com.millennialmedia.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import com.sessionm.ui.SessionMActivity;
import com.tapjoy.TapjoyConstants;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
class MMDevice extends MMJSObject {
    MMDevice() {
    }

    public MMJSResponse setMMDID(HashMap<String, String> arguments) {
        String mmdid = arguments.get("mmdid");
        Context context = this.contextRef.get();
        if (context == null) {
            return null;
        }
        HandShake handShake = HandShake.sharedHandShake(context);
        handShake.setMMdid(context, mmdid);
        return MMJSResponse.responseWithSuccess();
    }

    public MMJSResponse getAvailableSchemes(HashMap<String, String> arguments) {
        Context context = this.contextRef.get();
        if (context == null) {
            return null;
        }
        HandShake handShake = HandShake.sharedHandShake(context);
        MMJSResponse response = new MMJSResponse();
        response.result = 1;
        response.response = handShake.getSchemesJSONArray(context);
        return response;
    }

    public MMJSResponse isSchemeAvailable(HashMap<String, String> arguments) {
        String scheme = arguments.get("scheme");
        if (!scheme.contains(":")) {
            scheme = scheme + ":";
        }
        Context context = this.contextRef.get();
        if (scheme != null && context != null) {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(scheme));
            List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, 65536);
            if (list.size() > 0) {
                return MMJSResponse.responseWithSuccess(scheme);
            }
        }
        return MMJSResponse.responseWithError(scheme);
    }

    public MMJSResponse getOrientation(HashMap<String, String> arguments) {
        Context context = this.contextRef.get();
        if (context != null) {
            int orientation = context.getResources().getConfiguration().orientation;
            if (orientation == 0) {
                Display display = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
                orientation = display.getOrientation();
            }
            MMJSResponse response = new MMJSResponse();
            response.result = 1;
            switch (orientation) {
                case 2:
                    response.response = "landscape";
                    return response;
                default:
                    response.response = "portrait";
                    return response;
            }
        }
        return null;
    }

    public MMJSResponse getInfo(HashMap<String, String> arguments) {
        JSONObject jsonObject = null;
        Context context = this.contextRef.get();
        if (context != null) {
            try {
                JSONObject jsonObject2 = new JSONObject();
                try {
                    jsonObject2.put("sdkVersion", MMAdViewSDK.SDKVER);
                    jsonObject2.put("connection", MMAdViewSDK.getConnectionType(context));
                    jsonObject2.put(TapjoyConstants.TJC_PLATFORM, "Android");
                    if (Build.VERSION.RELEASE != null) {
                        jsonObject2.put("version", Build.VERSION.RELEASE);
                    }
                    if (Build.MODEL != null) {
                        jsonObject2.put("device", Build.MODEL);
                    }
                    if (HandShake.sharedHandShake(context).hdid) {
                        jsonObject2.put("hdid", MMAdViewSDK.getHdid(context));
                    } else {
                        jsonObject2.put("auid", MMAdViewSDK.getAuid(context));
                    }
                    jsonObject2.put("mmdid", MMAdViewSDK.getMMdid(context));
                    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                    jsonObject2.put("density", new Float(metrics.density));
                    jsonObject2.put("height", new Integer(metrics.heightPixels));
                    jsonObject2.put("width", new Integer(metrics.widthPixels));
                    Locale locale = Locale.getDefault();
                    if (locale != null) {
                        jsonObject2.put("language", locale.getLanguage());
                        jsonObject2.put("country", locale.getCountry());
                    }
                    JSONObject jsonCookieObject = new JSONObject();
                    try {
                        jsonCookieObject.put("name", "MAC-ID");
                        jsonCookieObject.put("path", "/");
                        jsonCookieObject.put("value", MMAdViewSDK.macId);
                        JSONArray jsonCookieArray = new JSONArray();
                        try {
                            jsonCookieArray.put(jsonCookieObject);
                            jsonObject2.put("cookies", jsonCookieArray);
                            jsonObject = jsonObject2;
                        } catch (JSONException e) {
                            jsonObject = jsonObject2;
                        }
                    } catch (JSONException e2) {
                        jsonObject = jsonObject2;
                    }
                } catch (JSONException e3) {
                    jsonObject = jsonObject2;
                }
            } catch (JSONException e4) {
            }
            MMJSResponse response = new MMJSResponse();
            response.result = 1;
            response.response = jsonObject;
            return response;
        }
        return null;
    }

    public MMJSResponse getLocation(HashMap<String, String> arguments) {
        if (MMAdViewSDK.location == null) {
            return null;
        }
        JSONObject jsonObject = null;
        try {
            JSONObject jsonObject2 = new JSONObject();
            try {
                jsonObject2.put("lat", Double.toString(MMAdViewSDK.location.getLatitude()));
                jsonObject2.put("long", Double.toString(MMAdViewSDK.location.getLongitude()));
                if (MMAdViewSDK.location.hasAccuracy()) {
                    jsonObject2.put("ha", Float.toString(MMAdViewSDK.location.getAccuracy()));
                    jsonObject2.put("va", Float.toString(MMAdViewSDK.location.getAccuracy()));
                }
                if (MMAdViewSDK.location.hasSpeed()) {
                    jsonObject2.put("spd", Float.toString(MMAdViewSDK.location.getSpeed()));
                }
                if (MMAdViewSDK.location.hasBearing()) {
                    jsonObject2.put("brg", Float.toString(MMAdViewSDK.location.getBearing()));
                }
                if (MMAdViewSDK.location.hasAltitude()) {
                    jsonObject2.put("alt", Double.toString(MMAdViewSDK.location.getAltitude()));
                }
                jsonObject2.put("tslr", Long.toString(MMAdViewSDK.location.getTime()));
                jsonObject = jsonObject2;
            } catch (JSONException e) {
                jsonObject = jsonObject2;
            }
        } catch (JSONException e2) {
        }
        MMJSResponse response = new MMJSResponse();
        response.result = 1;
        response.response = jsonObject;
        return response;
    }

    public MMJSResponse getState(HashMap<String, String> arguments) {
        String state = arguments.get(OverlaySettings.STATE);
        if (state == null) {
            return null;
        }
        MMJSResponse response = new MMJSResponse();
        response.result = 1;
        response.response = state;
        return response;
    }

    public MMJSResponse getPlacementType(HashMap<String, String> arguments) {
        String isBannerAd = arguments.get(OverlaySettings.BANNER_TYPE);
        if (isBannerAd != null) {
            MMJSResponse response = new MMJSResponse();
            response.result = 1;
            response.response = isBannerAd.equals("true") ? "inline" : "interstitial";
            return response;
        }
        return null;
    }

    public MMJSResponse showMap(HashMap<String, String> arguments) {
        Context context = this.contextRef.get();
        String location = arguments.get("location");
        if (context == null || location == null) {
            return null;
        }
        MMAdViewSDK.Log.d("Launching Google Maps: %s", location);
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("geo:" + location));
        intent.setFlags(603979776);
        if (!(context instanceof Activity)) {
            intent.addFlags(268435456);
        }
        context.startActivity(intent);
        MMAdViewSDK.Event.intentStarted(context, null, MMAdViewSDK.Event.INTENT_MAPS);
        return MMJSResponse.responseWithSuccess();
    }

    public MMJSResponse call(HashMap<String, String> arguments) {
        Intent intent;
        Context context = this.contextRef.get();
        String number = arguments.get("number");
        if (context == null || number == null) {
            return null;
        }
        MMAdViewSDK.Log.d("Dialing Phone: %s", number);
        context.checkCallingOrSelfPermission("android.permission.CALL_PHONE");
        if (Boolean.parseBoolean(arguments.get("dial")) && context.checkCallingOrSelfPermission("android.permission.CALL_PHONE") == 0) {
            intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + number));
        } else {
            intent = new Intent("android.intent.action.VIEW", Uri.parse("tel:" + number));
        }
        intent.setFlags(603979776);
        if (!(context instanceof Activity)) {
            intent.addFlags(268435456);
        }
        context.startActivity(intent);
        MMAdViewSDK.Event.intentStarted(context, null, MMAdViewSDK.Event.INTENT_PHONE_CALL);
        return MMJSResponse.responseWithSuccess();
    }

    public MMJSResponse composeSms(HashMap<String, String> arguments) {
        Context context = this.contextRef.get();
        String number = arguments.get("number");
        String message = arguments.get("message");
        if (context == null || number == null) {
            return null;
        }
        MMAdViewSDK.Log.d("Creating sms: %s", number);
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("sms:" + number));
        if (message != null) {
            intent.putExtra("sms_body", message);
        }
        intent.setFlags(603979776);
        if (!(context instanceof Activity)) {
            intent.addFlags(268435456);
        }
        context.startActivity(intent);
        MMAdViewSDK.Event.intentStarted(context, null, MMAdViewSDK.Event.INTENT_TXT_MESSAGE);
        return MMJSResponse.responseWithSuccess();
    }

    public MMJSResponse composeEmail(HashMap<String, String> arguments) {
        Context context = this.contextRef.get();
        String recipients = arguments.get("recipients");
        String subject = arguments.get("subject");
        String message = arguments.get("message");
        if (context == null) {
            return null;
        }
        MMAdViewSDK.Log.d("Creating email");
        Intent emailIntent = new Intent("android.intent.action.SEND");
        emailIntent.setType("plain/text");
        if (recipients != null) {
            emailIntent.putExtra("android.intent.extra.EMAIL", recipients.split(","));
        }
        if (subject != null) {
            emailIntent.putExtra("android.intent.extra.SUBJECT", subject);
        }
        if (message != null) {
            emailIntent.putExtra("android.intent.extra.TEXT", message);
        }
        emailIntent.setFlags(603979776);
        if (!(context instanceof Activity)) {
            emailIntent.addFlags(268435456);
        }
        context.startActivity(emailIntent);
        return MMJSResponse.responseWithSuccess();
    }

    public MMJSResponse openUrl(HashMap<String, String> arguments) {
        Context context = this.contextRef.get();
        String url = arguments.get(SessionMActivity.INTENT_PARAM_URL);
        if (context == null || url == null) {
            return null;
        }
        MMAdViewSDK.Log.d("Opening: %s", url);
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        intent.setFlags(603979776);
        if (!(context instanceof Activity)) {
            intent.addFlags(268435456);
        }
        if (intent.getScheme().startsWith("http") || intent.getScheme().startsWith("https")) {
            MMAdViewSDK.Event.intentStarted(context, null, MMAdViewSDK.Event.INTENT_EXTERNAL_BROWSER);
        }
        context.startActivity(intent);
        return MMJSResponse.responseWithSuccess();
    }

    public MMJSResponse openAppStore(HashMap<String, String> arguments) {
        Context context = this.contextRef.get();
        String id = arguments.get("appId");
        String referrer = arguments.get("referrer");
        if (context == null || id == null) {
            return null;
        }
        MMAdViewSDK.Log.d("Opening marketplace: %s", id);
        Intent intent = new Intent("android.intent.action.VIEW");
        if (referrer != null) {
            intent.setData(Uri.parse(String.format("market://details?id=%s&referrer=%s", id, URLEncoder.encode(referrer))));
        } else {
            intent.setData(Uri.parse("market://details?id=" + id));
        }
        intent.setFlags(603979776);
        if (!(context instanceof Activity)) {
            intent.addFlags(268435456);
        }
        MMAdViewSDK.Event.intentStarted(context, null, MMAdViewSDK.Event.INTENT_MARKET);
        context.startActivity(intent);
        return MMJSResponse.responseWithSuccess();
    }

    public MMJSResponse tweet(HashMap<String, String> arguments) {
        return null;
    }
}
