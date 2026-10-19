package com.millennialmedia.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.sessionm.ui.SessionMActivity;
import java.util.HashMap;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class MMInterstitial extends MMJSObject {
    MMInterstitial() {
    }

    public MMJSResponse show(HashMap<String, String> arguments) {
        String url = arguments.get(SessionMActivity.INTENT_PARAM_URL);
        Context context = this.contextRef.get();
        if (url == null || context == null) {
            return null;
        }
        Intent intent = new Intent(context, (Class<?>) MMActivity.class);
        intent.putExtra("class", "com.millennialmedia.android.AdViewOverlayActivity");
        intent.setData(Uri.parse(url));
        if (!(context instanceof MMActivity)) {
            intent.setFlags(603979776);
        }
        if (!(context instanceof Activity)) {
            intent.addFlags(268435456);
        }
        context.startActivity(intent);
        return MMJSResponse.responseWithSuccess();
    }

    public MMJSResponse close(HashMap<String, String> arguments) {
        Context context = this.contextRef.get();
        if (context == null || !(context instanceof Activity)) {
            return null;
        }
        Activity activity = (Activity) context;
        activity.finish();
        return MMJSResponse.responseWithSuccess();
    }

    public MMJSResponse open(HashMap<String, String> arguments) {
        String url = arguments.get(SessionMActivity.INTENT_PARAM_URL);
        Context context = this.contextRef.get();
        if (url == null || context == null) {
            return null;
        }
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        intent.setFlags(603979776);
        if (!(context instanceof Activity)) {
            intent.addFlags(268435456);
        }
        MMAdViewSDK.Event.intentStarted(context, null, MMAdViewSDK.Event.INTENT_EXTERNAL_BROWSER);
        context.startActivity(intent);
        return MMJSResponse.responseWithSuccess();
    }

    public MMJSResponse expandWithProperties(HashMap<String, String> arguments) {
        String isBanner = arguments.get(OverlaySettings.BANNER_TYPE);
        if (isBanner != null && !Boolean.parseBoolean(isBanner)) {
            return MMJSResponse.responseWithError("Cannot expand a non banner ad");
        }
        String url = arguments.get(SessionMActivity.INTENT_PARAM_URL);
        String transparent = arguments.get("transparent");
        String showTitlebar = arguments.get("showTitlebar");
        String showBottombar = arguments.get("showBottombar");
        String enableBottombar = arguments.get("enableBottombar");
        String enableNativeAccelerometer = arguments.get("enableNativeAccelerometer");
        String useCustomClose = arguments.get("useCustomClose");
        String title = arguments.get("title");
        String transition = arguments.get("transition");
        String orientation = arguments.get(MMAdView.KEY_ORIENTATION);
        String transitionDuration = arguments.get("transitionDuration");
        String delayEnableBottombar = arguments.get("delayEnableBottombar");
        String delayShowBottombar = arguments.get("delayShowBottombar");
        Context context = this.contextRef.get();
        if (context != null) {
            Intent intent = new Intent(context, (Class<?>) MMActivity.class);
            intent.putExtra("class", "com.millennialmedia.android.AdViewOverlayActivity");
            if (url == null && (url = arguments.get(OverlaySettings.ADURL)) == null) {
                return MMJSResponse.responseWithError("No url");
            }
            if (!(context instanceof MMActivity)) {
                intent.setFlags(603979776);
            }
            intent.setData(Uri.parse(url));
            OverlaySettings settings = new OverlaySettings();
            if (transparent != null) {
                settings.shouldMakeOverlayTransparent = Boolean.parseBoolean(transparent);
            }
            if (showTitlebar != null) {
                settings.shouldShowTitlebar = Boolean.parseBoolean(showTitlebar);
            }
            if (showBottombar != null) {
                settings.shouldShowBottomBar = Boolean.parseBoolean(showBottombar);
            }
            if (enableBottombar != null) {
                settings.shouldEnableBottomBar = Boolean.parseBoolean(enableBottombar);
            }
            if (enableNativeAccelerometer != null) {
                settings.canAccelerate = Boolean.parseBoolean(enableNativeAccelerometer);
            }
            if (useCustomClose != null) {
                settings.shouldShowCustomClose = Boolean.parseBoolean(useCustomClose);
            }
            if (title != null) {
                settings.overlayTitle = title;
            }
            if (transition != null) {
                settings.overlayTransition = transition;
            }
            if (orientation != null) {
                settings.orientation = orientation;
            }
            if (delayEnableBottombar != null) {
                try {
                    long tempDelayEnable = Long.parseLong(delayEnableBottombar);
                    settings.delayEnableBottombar = tempDelayEnable;
                } catch (Exception e) {
                }
            }
            if (delayShowBottombar != null) {
                try {
                    long tempDelayShow = Long.parseLong(delayShowBottombar);
                    settings.delayShowBottombar = tempDelayShow;
                } catch (Exception e2) {
                }
            }
            if (transitionDuration != null) {
                try {
                    long tempDuration = Long.parseLong(transitionDuration);
                    settings.transitionTime = tempDuration;
                } catch (Exception e3) {
                }
            }
            intent.putExtra("settings", settings);
            intent.setFlags(603979776);
            if (!(context instanceof Activity)) {
                intent.addFlags(268435456);
            }
            context.startActivity(intent);
            MMAdViewSDK.Event.overlayOpened(context, null);
            return MMJSResponse.responseWithSuccess();
        }
        return null;
    }
}
