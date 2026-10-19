package com.millennialmedia.android;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/* JADX INFO: loaded from: classes.dex */
final class HttpRedirection {
    private static final String HEADER_LOCATION = "Location";
    private static final String LOG_URL_FORMAT = "Redirecting to: %s";
    private static final String METHOD_GET = "GET";

    interface Listener {
        void didFailToResolveUri(Uri uri);

        boolean shouldStartActivityForUri(Uri uri);
    }

    HttpRedirection() {
    }

    static final String navigateRedirects(String urlString) {
        if (urlString == null) {
            return null;
        }
        HttpURLConnection.setFollowRedirects(false);
        while (!urlString.startsWith("https")) {
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setRequestMethod("GET");
                conn.connect();
                int rc = conn.getResponseCode();
                if (rc >= 300 && rc < 400) {
                    String locationUrl = conn.getHeaderField(HEADER_LOCATION);
                    URI locationUri = new URI(locationUrl);
                    if (!locationUri.isAbsolute()) {
                        urlString = url.toURI().resolve(locationUri).toString();
                    } else if (locationUrl != null) {
                        urlString = locationUrl;
                    }
                    MMAdViewSDK.Log.v(LOG_URL_FORMAT, urlString);
                } else {
                    return urlString;
                }
            } catch (MalformedURLException e) {
                return urlString;
            } catch (SocketTimeoutException e2) {
                MMAdViewSDK.Log.d("Connection timeout.");
                return urlString;
            } catch (IOException e3) {
                return urlString;
            } catch (URISyntaxException e4) {
                MMAdViewSDK.Log.d("URI Syntax incorrect.");
                return urlString;
            }
        }
        return urlString;
    }

    static Intent getIntentFromUri(Context context, Uri destinationUri, OverlaySettings settings) {
        Intent intent = null;
        if (destinationUri != null && destinationUri.getScheme() != null) {
            if (destinationUri.getScheme().equalsIgnoreCase(MMAdViewSDK.Event.INTENT_MARKET)) {
                MMAdViewSDK.Log.v("Creating Android Market intent.");
                intent = new Intent("android.intent.action.VIEW", destinationUri);
                MMAdViewSDK.Event.intentStarted(context, null, MMAdViewSDK.Event.INTENT_MARKET);
            } else if (destinationUri.getScheme().equalsIgnoreCase("rtsp")) {
                MMAdViewSDK.Log.v("Creating streaming video player intent.");
                intent = new Intent(context, (Class<?>) MMActivity.class);
                intent.setData(destinationUri);
                intent.putExtra("class", "com.millennialmedia.android.VideoPlayerActivity");
            } else if (destinationUri.getScheme().equalsIgnoreCase(MMAdViewSDK.Event.INTENT_PHONE_CALL)) {
                MMAdViewSDK.Log.v("Creating telephone intent.");
                intent = new Intent("android.intent.action.DIAL", destinationUri);
                MMAdViewSDK.Event.intentStarted(context, null, MMAdViewSDK.Event.INTENT_PHONE_CALL);
            } else if (destinationUri.getScheme().equalsIgnoreCase(MMAdViewSDK.Event.INTENT_TXT_MESSAGE)) {
                MMAdViewSDK.Log.v("Creating txt message intent.");
                intent = new Intent("android.intent.action.VIEW");
                String address = destinationUri.getSchemeSpecificPart();
                int bodyIndex = address.indexOf("?body=");
                if (bodyIndex != -1) {
                    address = address.substring(0, bodyIndex);
                }
                intent.putExtra("address", address.replace(',', ';'));
                if (bodyIndex != -1) {
                    String body = destinationUri.getSchemeSpecificPart();
                    intent.putExtra("sms_body", body.substring(bodyIndex + 6));
                }
                intent.setType("vnd.android-dir/mms-sms");
                MMAdViewSDK.Event.intentStarted(context, null, MMAdViewSDK.Event.INTENT_TXT_MESSAGE);
            } else if (destinationUri.getScheme().equalsIgnoreCase("mailto")) {
                intent = new Intent("android.intent.action.VIEW", destinationUri);
                MMAdViewSDK.Event.intentStarted(context, null, MMAdViewSDK.Event.INTENT_EMAIL);
            } else if (destinationUri.getScheme().equalsIgnoreCase(MMAdViewSDK.Event.INTENT_MAPS)) {
                MMAdViewSDK.Log.v("Creating Google Maps intent.");
                intent = new Intent("android.intent.action.VIEW", destinationUri);
                MMAdViewSDK.Event.intentStarted(context, null, MMAdViewSDK.Event.INTENT_MAPS);
            } else if (destinationUri.getScheme().equalsIgnoreCase("http") && destinationUri.getLastPathSegment() != null && (destinationUri.getLastPathSegment().endsWith(".mp4") || destinationUri.getLastPathSegment().endsWith(".3gp"))) {
                MMAdViewSDK.Log.v("Creating video player intent.");
                intent = new Intent(context, (Class<?>) MMActivity.class);
                intent.setData(destinationUri);
                intent.putExtra("class", "com.millennialmedia.android.VideoPlayerActivity");
                MMAdViewSDK.Event.intentStarted(context, null, MMAdViewSDK.Event.INTENT_STREAMING_VIDEO);
            } else if (destinationUri.getScheme().equalsIgnoreCase("http")) {
                if (settings != null && settings.shouldLaunchToOverlay) {
                    MMAdViewSDK.Log.v("Creating launch overlay intent.");
                    intent = new Intent(context, (Class<?>) MMActivity.class);
                    intent.putExtra("settings", settings);
                    intent.putExtra("class", "com.millennialmedia.android.AdViewOverlayActivity");
                    intent.setData(destinationUri);
                } else {
                    MMAdViewSDK.Log.v("Creating launch browser intent.");
                    intent = new Intent("android.intent.action.VIEW", destinationUri);
                    MMAdViewSDK.Event.intentStarted(context, null, MMAdViewSDK.Event.INTENT_EXTERNAL_BROWSER);
                }
            } else if (destinationUri.getScheme().equalsIgnoreCase("https")) {
                MMAdViewSDK.Log.v("Creating launch browser intent.");
                intent = new Intent("android.intent.action.VIEW", destinationUri);
                MMAdViewSDK.Event.intentStarted(context, null, MMAdViewSDK.Event.INTENT_EXTERNAL_BROWSER);
            } else if (destinationUri.getScheme().equalsIgnoreCase("mmbrowser")) {
                String urlString = destinationUri.toString();
                String browserAction = urlString.substring(12);
                if (browserAction != null) {
                    if (!browserAction.contains("://")) {
                        browserAction = browserAction.replaceFirst("//", "://");
                    }
                    MMAdViewSDK.Log.v("Creating launch browser intent.");
                    intent = new Intent("android.intent.action.VIEW", Uri.parse(browserAction));
                    MMAdViewSDK.Event.intentStarted(context, null, MMAdViewSDK.Event.INTENT_EXTERNAL_BROWSER);
                }
            } else {
                MMAdViewSDK.Log.v("Creating intent for unrecognized URI. %s", destinationUri);
                intent = new Intent("android.intent.action.VIEW", destinationUri);
            }
        }
        if (intent != null) {
            String className = "";
            ComponentName component = intent.getComponent();
            if (component != null) {
                className = component.getClassName();
            }
            if (!className.equals("com.millennialmedia.android.MMActivity") || !(context instanceof MMActivity)) {
                intent.setFlags(603979776);
            }
            MMAdViewSDK.Log.v("%s resolved to Intent: %s", destinationUri, intent);
        } else {
            MMAdViewSDK.Log.v("%s", destinationUri);
        }
        return intent;
    }

    static void startActivityFromUri(Context context, final String uri, final OverlaySettings settings, Listener listener, final String adType) {
        final WeakReference<Context> contextReference = new WeakReference<>(context);
        final WeakReference<Listener> listenerReference = new WeakReference<>(listener);
        if (context != null && listener != null) {
            Thread thread = new Thread(new Runnable() { // from class: com.millennialmedia.android.HttpRedirection.1
                @Override // java.lang.Runnable
                public void run() {
                    String id;
                    String destination = HttpRedirection.navigateRedirects(uri);
                    Uri destinationUri = Uri.parse(destination);
                    Context context2 = (Context) contextReference.get();
                    Listener listener2 = (Listener) listenerReference.get();
                    if (destination != null && context2 != null && listener2 != null) {
                        if (destinationUri.getScheme() != null && destinationUri.getScheme().equalsIgnoreCase("mmvideo")) {
                            if (listener2.shouldStartActivityForUri(destinationUri) && (id = destinationUri.getHost()) != null) {
                                VideoAd videoAd = (VideoAd) AdCache.load(context2, id);
                                if (videoAd != null && videoAd.canShow(context2, null, false)) {
                                    if (adType != null) {
                                        HandShake.sharedHandShake(context2).updateLastVideoViewedTime(context2, adType);
                                    }
                                    MMAdViewSDK.Log.v("mmvideo: attempting to play video %s", id);
                                    videoAd.show(context2, null);
                                    return;
                                }
                                MMAdViewSDK.Log.v("mmvideo: Ad %s cannot be shown at this time.", id);
                                return;
                            }
                            return;
                        }
                        Intent intent = HttpRedirection.getIntentFromUri(context2, destinationUri, settings);
                        if (intent != null && listener2.shouldStartActivityForUri(destinationUri)) {
                            try {
                                if (!(context2 instanceof Activity)) {
                                    intent.addFlags(268435456);
                                }
                                context2.startActivity(intent);
                                return;
                            } catch (ActivityNotFoundException e) {
                                MMAdViewSDK.Log.e("No activity found for %s", destinationUri);
                                return;
                            }
                        }
                        MMAdViewSDK.Log.v("Could not start activity for %s", destinationUri);
                        listener2.didFailToResolveUri(destinationUri);
                        return;
                    }
                    if (listener2 != null) {
                        MMAdViewSDK.Log.v("Could not start activity for %s", destinationUri);
                        listener2.didFailToResolveUri(Uri.parse(uri));
                    }
                }
            });
            thread.start();
        }
    }
}
