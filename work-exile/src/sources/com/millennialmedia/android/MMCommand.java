package com.millennialmedia.android;

import android.content.ActivityNotFoundException;
import android.net.Uri;
import android.webkit.WebView;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class MMCommand implements Runnable {
    private HashMap<String, String> arguments;
    private String callback;
    private Class cls;
    private Method method;
    OverlaySettings settings;
    private WeakReference<WebView> webViewRef;

    MMCommand(WebView webView, String uriString) {
        this.webViewRef = new WeakReference<>(webView);
        try {
            Uri uri = Uri.parse(uriString);
            String[] components = uri.getHost().split("\\.");
            if (components.length >= 2) {
                String className = components[components.length - 2];
                String methodName = components[components.length - 1];
                this.arguments = new HashMap<>();
                String queryString = uriString.substring(uriString.indexOf(63) + 1);
                for (String param : queryString.split("&")) {
                    String[] subComponents = param.split("=");
                    if (subComponents.length >= 2) {
                        this.arguments.put(Uri.decode(subComponents[0]), Uri.decode(subComponents[1]));
                        if (subComponents[0].equalsIgnoreCase("callback")) {
                            this.callback = Uri.decode(subComponents[1]);
                        }
                    }
                }
                this.cls = Class.forName("com.millennialmedia.android." + className);
                this.method = this.cls.getMethod(methodName, this.arguments.getClass());
            }
        } catch (Exception e) {
            MMAdViewSDK.Log.e("Exception while executing javascript call %s %s", uriString, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        MMJSResponse response;
        final WebView webViewCallback;
        if (this.cls != null && this.method != null) {
            try {
                WebView webView = this.webViewRef.get();
                if (webView != null) {
                    MMJSObject receiver = (MMJSObject) this.cls.newInstance();
                    receiver.setContext(webView.getContext());
                    if (this.settings != null) {
                        this.arguments.put(OverlaySettings.BANNER_TYPE, this.settings.isBannerAd ? "true" : "false");
                        this.arguments.put(OverlaySettings.STATE, this.settings.state);
                        this.arguments.put(OverlaySettings.ADURL, this.settings.adUrl);
                    }
                    try {
                        response = (MMJSResponse) this.method.invoke(receiver, this.arguments);
                    } catch (InvocationTargetException ite) {
                        Throwable t = ite.getCause();
                        if (t != null && t.getClass() == ActivityNotFoundException.class) {
                            response = MMJSResponse.responseWithError("Activity not found");
                        } else {
                            response = MMJSResponse.responseWithError();
                        }
                    }
                    if (this.callback != null && this.callback.length() > 0 && (webViewCallback = this.webViewRef.get()) != null) {
                        if (response == null) {
                            response = MMJSResponse.responseWithError(this.method.getName());
                        }
                        if (response.methodName == null) {
                            response.methodName = this.method.getName();
                        }
                        if (response.className == null) {
                            response.className = this.cls.getSimpleName();
                        }
                        final String call = String.format("javascript:%s(%s);", this.callback, response.toJSONString());
                        MMAdViewSDK.Log.v("Executing JS bridge callback: " + call);
                        MMAdViewSDK.runOnUiThread(new Runnable() { // from class: com.millennialmedia.android.MMCommand.1
                            @Override // java.lang.Runnable
                            public void run() {
                                webViewCallback.loadUrl(call);
                            }
                        });
                    }
                }
            } catch (Exception e) {
                MMAdViewSDK.Log.e("Exception while executing javascript call %s %s", this.method.toString(), e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
