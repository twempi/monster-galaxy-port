package com.millennialmedia.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

/* JADX INFO: loaded from: classes.dex */
public class MMAdView extends FrameLayout implements View.OnClickListener, Animation.AnimationListener {
    public static final String BANNER_AD_BOTTOM = "MMBannerAdBottom";
    public static final String BANNER_AD_RECTANGLE = "MMBannerAdRectangle";
    public static final String BANNER_AD_TOP = "MMBannerAdTop";
    public static final String FULLSCREEN_AD_LAUNCH = "MMFullScreenAdLaunch";
    public static final String FULLSCREEN_AD_TRANSITION = "MMFullScreenAdTransition";
    public static final String KEY_AGE = "age";
    public static final String KEY_CHILDREN = "children";
    public static final String KEY_EDUCATION = "education";
    public static final String KEY_ETHNICITY = "ethnicity";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_HEIGHT = "height";
    public static final String KEY_INCOME = "income";
    public static final String KEY_KEYWORDS = "keywords";
    public static final String KEY_MARITAL_STATUS = "marital";
    public static final String KEY_ORIENTATION = "orientation";
    public static final String KEY_POLITICS = "politics";
    public static final String KEY_VENDOR = "vendor";
    public static final String KEY_WIDTH = "width";
    public static final String KEY_ZIP_CODE = "zip";
    public static final int REFRESH_INTERVAL_OFF = -1;
    public static final int TRANSITION_DOWN = 3;
    public static final int TRANSITION_FADE = 1;
    public static final int TRANSITION_NONE = 0;
    public static final int TRANSITION_RANDOM = 4;
    public static final int TRANSITION_UP = 2;
    private static boolean appInit;
    private static long nextAdViewId = 1;
    String acid;
    String adType;
    Long adViewId;
    String apid;
    MMAdViewController controller;
    Request deferedRequest;
    private GestureDetector gestureDetector;
    String goalId;
    String height;
    boolean ignoreDensityScaling;
    ImageView imageView;
    long lastAdRequest;
    MMAdListener listener;
    String mxsdk;
    int refreshInterval;
    int transitionType;
    public String userData;
    String width;
    boolean xmlLayout;

    public interface MMAdListener {
        void MMAdCachingCompleted(MMAdView mMAdView, boolean z);

        void MMAdClickedToOverlay(MMAdView mMAdView);

        void MMAdFailed(MMAdView mMAdView);

        void MMAdOverlayLaunched(MMAdView mMAdView);

        void MMAdRequestIsCaching(MMAdView mMAdView);

        void MMAdReturned(MMAdView mMAdView);
    }

    public MMAdView(Context context) {
        super(context);
        this.refreshInterval = 60;
        this.apid = MMAdViewSDK.DEFAULT_APID;
        this.acid = null;
        this.mxsdk = null;
        this.height = null;
        this.width = null;
        this.xmlLayout = false;
        this.ignoreDensityScaling = false;
        this.transitionType = 4;
        MMAdViewSDK.Log.d("Creating new MMAdView for conversion tracking.");
        checkPermissions(context);
    }

    public MMAdView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /* JADX WARN: Code duplicated, block: B:32:0x01dd A[Catch: Exception -> 0x0215, TRY_LEAVE, TryCatch #4 {Exception -> 0x0215, blocks: (B:30:0x01d8, B:32:0x01dd), top: B:62:0x01d8 }] */
    /* JADX WARN: Code duplicated, block: B:42:0x01fe A[Catch: Exception -> 0x0202, TRY_LEAVE, TryCatch #6 {Exception -> 0x0202, blocks: (B:40:0x01f9, B:42:0x01fe), top: B:64:0x01f9 }] */
    /* JADX WARN: Code duplicated, block: B:50:0x020c A[Catch: Exception -> 0x0210, TRY_LEAVE, TryCatch #0 {Exception -> 0x0210, blocks: (B:48:0x0207, B:50:0x020c), top: B:60:0x0207 }] */
    public MMAdView(Context context, AttributeSet attrs, int defStyle) throws Throwable {
        super(context, attrs, defStyle);
        this.refreshInterval = 60;
        this.apid = MMAdViewSDK.DEFAULT_APID;
        this.acid = null;
        this.mxsdk = null;
        this.height = null;
        this.width = null;
        this.xmlLayout = false;
        this.ignoreDensityScaling = false;
        this.transitionType = 4;
        if (!isInEditMode()) {
            MMAdViewSDK.Log.d("Creating MMAdView from XML layout.");
            if (attrs != null) {
                this.apid = attrs.getAttributeValue("http://millennialmedia.com/android/schema", "apid");
                this.acid = attrs.getAttributeValue("http://millennialmedia.com/android/schema", "acid");
                this.adType = attrs.getAttributeValue("http://millennialmedia.com/android/schema", "adType");
                this.refreshInterval = attrs.getAttributeIntValue("http://millennialmedia.com/android/schema", "refreshInterval", 60);
                this.ignoreDensityScaling = attrs.getAttributeBooleanValue("http://millennialmedia.com/android/schema", "ignoreDensityScaling", false);
                this.height = attrs.getAttributeValue("http://millennialmedia.com/android/schema", "height");
                this.width = attrs.getAttributeValue("http://millennialmedia.com/android/schema", "width");
                if (MMAdViewSDK.demographic != null) {
                    MMAdViewSDK.demographic.gender = attrs.getAttributeValue("http://millennialmedia.com/android/schema", KEY_GENDER);
                    MMAdViewSDK.demographic.ethnicity = attrs.getAttributeValue("http://millennialmedia.com/android/schema", KEY_ETHNICITY);
                    MMAdViewSDK.demographic.orientation = attrs.getAttributeValue("http://millennialmedia.com/android/schema", KEY_ORIENTATION);
                    MMAdViewSDK.demographic.marital = attrs.getAttributeValue("http://millennialmedia.com/android/schema", KEY_MARITAL_STATUS);
                    MMAdViewSDK.demographic.education = attrs.getAttributeValue("http://millennialmedia.com/android/schema", KEY_EDUCATION);
                    MMAdViewSDK.demographic.put("age", attrs.getAttributeValue("http://millennialmedia.com/android/schema", "age"));
                    MMAdViewSDK.demographic.put("zip", attrs.getAttributeValue("http://millennialmedia.com/android/schema", "zip"));
                    MMAdViewSDK.demographic.put("income", attrs.getAttributeValue("http://millennialmedia.com/android/schema", "income"));
                    MMAdViewSDK.demographic.put("keywords", attrs.getAttributeValue("http://millennialmedia.com/android/schema", "keywords"));
                    MMAdViewSDK.demographic.put("children", attrs.getAttributeValue("http://millennialmedia.com/android/schema", "children"));
                    MMAdViewSDK.demographic.put("politics", attrs.getAttributeValue("http://millennialmedia.com/android/schema", "politics"));
                    MMAdViewSDK.demographic.put("vendor", attrs.getAttributeValue("http://millennialmedia.com/android/schema", "vendor"));
                }
                this.goalId = attrs.getAttributeValue("http://millennialmedia.com/android/schema", "goalId");
            }
            this.xmlLayout = true;
            init(context);
            return;
        }
        ImageView logoForXML = new ImageView(context);
        InputStream is = null;
        OutputStream out = null;
        try {
            String dir = System.getProperty("java.io.tmpdir");
            if (dir != null && !dir.endsWith(File.separator)) {
                dir = dir + File.separator;
            }
            File file = new File(dir + "millenial355jns6u3l1nmedia.png");
            if (!file.exists()) {
                HttpURLConnection conn = (HttpURLConnection) new URL("http://images.millennialmedia.com/9513/192134.gif").openConnection();
                conn.setDoOutput(true);
                conn.setConnectTimeout(3000);
                conn.connect();
                is = conn.getInputStream();
                OutputStream out2 = new FileOutputStream(file);
                try {
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int bytesRead = is.read(buffer);
                        if (bytesRead <= 0) {
                            break;
                        } else {
                            out2.write(buffer, 0, bytesRead);
                        }
                    }
                    out = out2;
                } catch (Exception e) {
                    out = out2;
                    if (is != null) {
                        try {
                            is.close();
                            if (out != null) {
                                out.close();
                            }
                        } catch (Exception e2) {
                        }
                    } else if (out != null) {
                        out.close();
                    }
                } catch (Throwable th) {
                    th = th;
                    out = out2;
                    if (is != null) {
                        try {
                            is.close();
                            if (out != null) {
                                out.close();
                            }
                        } catch (Exception e3) {
                            throw th;
                        }
                    } else if (out != null) {
                        out.close();
                    }
                    throw th;
                }
            }
            Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
            if (logoForXML != null && bmp != null) {
                logoForXML.setImageBitmap(bmp);
            }
            if (is != null) {
                try {
                    is.close();
                    if (out != null) {
                        out.close();
                    }
                } catch (Exception e4) {
                }
            } else if (out != null) {
                out.close();
            }
        } catch (Exception e5) {
        } catch (Throwable th2) {
            th = th2;
        }
        addView(logoForXML);
    }

    public MMAdView(Context context, String apid, String adType, int refreshInterval) throws Throwable {
        super(context);
        this.refreshInterval = 60;
        this.apid = MMAdViewSDK.DEFAULT_APID;
        this.acid = null;
        this.mxsdk = null;
        this.height = null;
        this.width = null;
        this.xmlLayout = false;
        this.ignoreDensityScaling = false;
        this.transitionType = 4;
        MMAdViewSDK.Log.d("Creating new MMAdView.");
        this.apid = apid;
        this.adType = adType;
        this.refreshInterval = refreshInterval;
        init(context);
    }

    public MMAdView(Context context, String apid, String adType, int refreshInterval, Hashtable<String, String> metaMap) throws Throwable {
        super(context);
        this.refreshInterval = 60;
        this.apid = MMAdViewSDK.DEFAULT_APID;
        this.acid = null;
        this.mxsdk = null;
        this.height = null;
        this.width = null;
        this.xmlLayout = false;
        this.ignoreDensityScaling = false;
        this.transitionType = 4;
        MMAdViewSDK.Log.d("Creating new MMAdView.");
        this.apid = apid;
        this.adType = adType;
        this.refreshInterval = refreshInterval;
        setMetaValues(metaMap);
        init(context);
    }

    public MMAdView(Context context, String apid, String adType, int refreshInterval, Hashtable<String, String> metaMap, boolean accelerate) throws Throwable {
        super(context);
        this.refreshInterval = 60;
        this.apid = MMAdViewSDK.DEFAULT_APID;
        this.acid = null;
        this.mxsdk = null;
        this.height = null;
        this.width = null;
        this.xmlLayout = false;
        this.ignoreDensityScaling = false;
        this.transitionType = 4;
        MMAdViewSDK.Log.d("Creating new MMAdView.");
        this.apid = apid;
        this.adType = adType;
        this.refreshInterval = refreshInterval;
        setMetaValues(metaMap);
        init(context);
    }

    public MMAdView(Context context, String apid, String adType, boolean accelerate, Hashtable<String, String> metaMap) throws Throwable {
        super(context);
        this.refreshInterval = 60;
        this.apid = MMAdViewSDK.DEFAULT_APID;
        this.acid = null;
        this.mxsdk = null;
        this.height = null;
        this.width = null;
        this.xmlLayout = false;
        this.ignoreDensityScaling = false;
        this.transitionType = 4;
        MMAdViewSDK.Log.d("Creating new MMAdView.");
        this.apid = apid;
        this.adType = adType;
        this.refreshInterval = -1;
        setMetaValues(metaMap);
        init(context);
    }

    protected void finalize() throws Throwable {
        if (getId() == -1 || isInterstitial()) {
            MMAdViewController.removeAdViewController(this, true);
        }
    }

    private void init(Context context) throws Throwable {
        try {
            MMAdViewSDK.Log.d("Initializing MMAdView.");
            synchronized (MMAdView.class) {
                this.adViewId = new Long(nextAdViewId);
                nextAdViewId++;
                MMAdViewSDK.Log.v("Assigning MMAdView internal id: %d", this.adViewId);
            }
            checkPermissions(context);
            checkActivity(context);
            setBackgroundColor(0);
            setOnClickListener(this);
            setFocusable(true);
            setDescendantFocusability(393216);
            if (this.apid == null) {
                MMAdViewSDK.Log.e("MMAdView initialized without an apid. New ad requests will be disabled.");
                HandShake.apid = MMAdViewSDK.DEFAULT_APID;
            } else {
                HandShake.apid = this.apid;
            }
            HandShake.sharedHandShake(context).overrideAdRefresh(this);
            if ((this.adType == FULLSCREEN_AD_TRANSITION || this.adType == FULLSCREEN_AD_LAUNCH) && this.refreshInterval != -1) {
                MMAdViewSDK.Log.w("Turning off interstitial refresh interval.");
                this.refreshInterval = -1;
            }
            this.imageView = new ImageView(context);
            this.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            this.imageView.setVisibility(8);
            addView(this.imageView, new FrameLayout.LayoutParams(-1, -1));
            setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
            this.gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() { // from class: com.millennialmedia.android.MMAdView.1
                @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) throws Throwable {
                    int dx = (int) (e2.getX() - e1.getX());
                    if (Math.abs(dx) <= 200 || Math.abs(velocityX) <= Math.abs(velocityY)) {
                        return false;
                    }
                    if (velocityX <= 0.0f) {
                        MMAdView.this.printDiagnostics();
                    } else if (MMAdViewSDK.logLevel == 0) {
                        MMAdViewSDK.Log.i("Enabling debug and verbose logging.");
                        MMAdViewSDK.logLevel = 2;
                    } else {
                        MMAdViewSDK.Log.i("Disabling debug and verbose logging.");
                        MMAdViewSDK.logLevel = 0;
                    }
                    return true;
                }
            });
        } catch (Exception e) {
            MMAdViewSDK.Log.e("There was an exception initializing the MMAdView. %s", e.getMessage());
            e.printStackTrace();
        }
        if (!appInit) {
            MMAdViewSDK.Log.d("********** Millennial Device Id *****************");
            MMAdViewSDK.Log.d(MMAdViewSDK.getHdid(context));
            MMAdViewSDK.Log.d("Use the above identifier to register this device and receive test ads. Test devices can be registered and administered through your account at http://mmedia.com.");
            MMAdViewSDK.Log.d("*************************************************");
            AdCache.cleanUpExpiredAds(context);
            MMFileManager.cleanupCache(context, false);
            appInit = true;
        }
    }

    static String[] getAdTypes() {
        return new String[]{BANNER_AD_TOP, BANNER_AD_BOTTOM, BANNER_AD_RECTANGLE, FULLSCREEN_AD_LAUNCH, FULLSCREEN_AD_TRANSITION};
    }

    @Override // android.view.View
    public void onWindowFocusChanged(boolean windowInFocus) {
        Activity activity;
        super.onWindowFocusChanged(windowInFocus);
        if (this.controller != null) {
            if (windowInFocus) {
                this.controller.resumeTimer(false);
                this.controller.settings.state = "default";
                this.controller.loadUrl("javascript:MMSDK.mraid.stateChange('default')");
                this.controller.loadUrl("javascript:MMSDK.mraid.viewableChange(true)");
                this.controller.loadUrl("javascript:MMSDK.mraid.ready()");
            } else {
                this.controller.pauseTimer(false);
                this.controller.settings.state = "hidden";
                this.controller.loadUrl("javascript:MMSDK.mraid.stateChange('hidden')");
                this.controller.loadUrl("javascript:MMSDK.mraid.viewableChange(false)");
            }
        }
        MMAdViewSDK.Log.d("Window Focus Changed. Window in focus?: %b", Boolean.valueOf(windowInFocus));
        if (!windowInFocus && (getContext() instanceof Activity) && ((activity = (Activity) getContext()) == null || activity.isFinishing())) {
            MMAdViewController.removeAdViewController(this, true);
        }
        MMMedia.Audio.sharedAudio(getContext()).stop();
    }

    @Override // android.view.View
    protected void onVisibilityChanged(View changedView, int visibility) {
        if (this.controller != null) {
            if (visibility == 0) {
                this.controller.resumeTimer(false);
            } else {
                this.controller.pauseTimer(false);
            }
        }
        Object[] objArr = new Object[1];
        objArr[0] = Boolean.valueOf(visibility == 0);
        MMAdViewSDK.Log.d("Window Visibility Changed. Window is visible?: %b", objArr);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.width == null) {
            this.width = Integer.toString((int) (getWidth() / getContext().getResources().getDisplayMetrics().density));
        }
        if (this.height == null) {
            this.height = Integer.toString((int) (getHeight() / getContext().getResources().getDisplayMetrics().density));
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            MMAdViewSDK.Log.d("onAttachedToWindow");
            MMAdViewController.assignAdViewController(this);
            if (this.deferedRequest != null) {
                requestAd(this.deferedRequest);
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        MMAdViewSDK.Log.d("onDetachedFromWindow");
        MMAdViewController.removeAdViewController(this, false);
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        MMAdViewSDK.Log.d("onSaveInstanceState");
        Bundle bundle = new Bundle();
        bundle.putParcelable("super", super.onSaveInstanceState());
        bundle.putLong("MMAdView", this.adViewId.longValue());
        return bundle;
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        MMAdViewSDK.Log.d("onRestoreInstanceState");
        long id = bundle.getLong("MMAdView");
        this.adViewId = new Long(id);
        super.onRestoreInstanceState(bundle.getParcelable("super"));
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        onTouchEvent(MotionEvent.obtain(0L, System.currentTimeMillis(), 1, 0.0f, 0.0f, 0));
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return true;
    }

    /* JADX WARN: Code duplicated, block: B:28:0x00a2  */
    /* JADX WARN: Code duplicated, block: B:33:0x00b1  */
    /* JADX WARN: Code duplicated, block: B:36:0x00c7  */
    /* JADX WARN: Code duplicated, block: B:37:0x00ce  */
    /* JADX WARN: Code duplicated, block: B:39:0x00d4  */
    /* JADX WARN: Code duplicated, block: B:41:0x00df  */
    /* JADX WARN: Code duplicated, block: B:44:0x00fe A[Catch: ActivityNotFoundException -> 0x0126, TryCatch #0 {ActivityNotFoundException -> 0x0126, blocks: (B:42:0x00e6, B:44:0x00fe, B:45:0x0103, B:47:0x010f, B:50:0x0121, B:49:0x011b), top: B:58:0x00e6 }] */
    /* JADX WARN: Code duplicated, block: B:49:0x011b A[Catch: ActivityNotFoundException -> 0x0126, TryCatch #0 {ActivityNotFoundException -> 0x0126, blocks: (B:42:0x00e6, B:44:0x00fe, B:45:0x0103, B:47:0x010f, B:50:0x0121, B:49:0x011b), top: B:58:0x00e6 }] */
    /* JADX WARN: Code duplicated, block: B:54:0x0136  */
    /* JADX WARN: Code duplicated, block: B:58:0x00e6 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:60:0x00a6 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent e) {
        Context context;
        Intent myIntent;
        if (!this.gestureDetector.onTouchEvent(e) && this.controller != null && isClickable()) {
            if (e.getAction() == 1) {
                MMAdViewSDK.Log.v("Ad clicked: action=%d x=%f y=%f", Integer.valueOf(e.getAction()), Float.valueOf(e.getX()), Float.valueOf(e.getY()));
                if (this.controller.nextUrl != null) {
                    if (this.controller.nextUrl.startsWith("javascript") || this.controller.nextUrl.startsWith("mmsdk")) {
                        this.controller.loadUrl(this.controller.nextUrl);
                    } else {
                        if (this.controller.nextUrl.startsWith("mmvideo") || this.controller.nextUrl.endsWith("content.once")) {
                            this.controller.settings.shouldLaunchToOverlay = true;
                        }
                        if (this.controller.nextUrl.startsWith("https")) {
                            this.controller.settings.shouldLaunchToOverlay = false;
                        }
                        if (this.controller.settings.shouldLaunchToOverlay) {
                            if (this.listener != null) {
                                this.listener.MMAdClickedToOverlay(this);
                            }
                            if (this.controller.nextUrl != null) {
                                this.controller.handleClick(this.controller.nextUrl);
                            } else {
                                this.controller.touchWebView(e);
                            }
                        } else if (this.controller.nextUrl != null) {
                            MMAdViewSDK.Log.d("Ad clicked, launching new browser");
                            context = getContext();
                            if (context == null) {
                                MMAdViewSDK.Log.e("The reference to the ad view was broken.");
                            } else {
                                myIntent = new Intent("android.intent.action.VIEW", Uri.parse(this.controller.nextUrl));
                                myIntent.setFlags(603979776);
                                if (!(context instanceof Activity)) {
                                    myIntent.addFlags(268435456);
                                }
                                if (!this.controller.nextUrl.startsWith("http")) {
                                    MMAdViewSDK.Event.intentStarted(context, null, MMAdViewSDK.Event.INTENT_EXTERNAL_BROWSER);
                                } else {
                                    MMAdViewSDK.Event.intentStarted(context, null, MMAdViewSDK.Event.INTENT_EXTERNAL_BROWSER);
                                }
                                context.startActivity(myIntent);
                            }
                        } else {
                            MMAdViewSDK.Log.e("No ad returned, no new browser launched");
                        }
                    }
                } else if (this.controller.settings.shouldLaunchToOverlay) {
                    if (this.listener != null) {
                        try {
                            this.listener.MMAdClickedToOverlay(this);
                        } catch (Exception exception) {
                            MMAdViewSDK.Log.w("Exception raised in your MMAdListener: ", exception);
                        }
                    }
                    if (this.controller.nextUrl != null) {
                        this.controller.handleClick(this.controller.nextUrl);
                    } else {
                        this.controller.touchWebView(e);
                    }
                } else if (this.controller.nextUrl != null) {
                    MMAdViewSDK.Log.d("Ad clicked, launching new browser");
                    context = getContext();
                    if (context == null) {
                        MMAdViewSDK.Log.e("The reference to the ad view was broken.");
                    } else {
                        try {
                            myIntent = new Intent("android.intent.action.VIEW", Uri.parse(this.controller.nextUrl));
                            myIntent.setFlags(603979776);
                            if (!(context instanceof Activity)) {
                                myIntent.addFlags(268435456);
                            }
                            if (!this.controller.nextUrl.startsWith("http") || this.controller.nextUrl.startsWith("https")) {
                                MMAdViewSDK.Event.intentStarted(context, null, MMAdViewSDK.Event.INTENT_EXTERNAL_BROWSER);
                            }
                            context.startActivity(myIntent);
                        } catch (ActivityNotFoundException e2) {
                            MMAdViewSDK.Log.d("Could not find activity for: %s", this.controller.nextUrl);
                        }
                    }
                } else {
                    MMAdViewSDK.Log.e("No ad returned, no new browser launched");
                }
            } else if (e.getAction() != 2) {
                this.controller.touchWebView(e);
            }
        }
        return true;
    }

    boolean isInterstitial() {
        return this.adType.equals(FULLSCREEN_AD_TRANSITION) || this.adType.equals(FULLSCREEN_AD_LAUNCH);
    }

    boolean canRequestAd() {
        if (MMAdViewSDK.disableAdMinRefresh) {
            return true;
        }
        if ((System.currentTimeMillis() - this.lastAdRequest) / 1000 >= 15) {
            this.lastAdRequest = System.currentTimeMillis();
            return true;
        }
        int lastRequest = (int) ((System.currentTimeMillis() - this.lastAdRequest) / 1000);
        MMAdViewSDK.Log.d("Cannot request ad. Last ad request was %d seconds ago. Next ad can be requested in %d seconds.", Integer.valueOf(lastRequest), Integer.valueOf(15 - lastRequest));
        return false;
    }

    void requestAd(Request request) {
        if (!MMAdViewSDK.isUiThread()) {
            MMAdViewSDK.Log.e("Only the main thread can access an MMAdView.");
            MMAdViewSDK.Event.requestFailed(getContext(), this, request, new MMError(3));
            return;
        }
        if (HandShake.sharedHandShake(getContext()).kill) {
            MMAdViewSDK.Log.i("The server is no longer allowing ads.");
            MMAdViewSDK.Event.requestFailed(getContext(), this, request, new MMError(16));
            return;
        }
        try {
            MMAdViewSDK.Log.d("callForAd");
            if (getWindowToken() == null && this.xmlLayout) {
                this.deferedRequest = request;
                return;
            }
            if (this.refreshInterval < 0) {
                MMAdViewController.assignAdViewController(this);
                if (this.controller != null) {
                    if (request.fetch) {
                        this.controller.fetch(request);
                    } else {
                        this.controller.requestAd(request);
                    }
                }
            }
            this.deferedRequest = null;
        } catch (Exception e) {
            MMAdViewSDK.Log.e("There was an exception with the ad request. %s", e.getMessage());
            e.printStackTrace();
        }
    }

    public void callForAd() {
        Request request = new Request(this.apid, null, false);
        if (!canRequestAd()) {
            MMAdViewSDK.Event.requestFailed(getContext(), this, request, new MMError(16));
        } else {
            requestAd(request);
        }
    }

    public void fetch() {
        if (check()) {
            MMAdViewSDK.Log.d("Ad already fetched and ready for display...");
            if (this.listener != null) {
                try {
                    this.listener.MMAdFailed(this);
                    return;
                } catch (Exception e) {
                    MMAdViewSDK.Log.w("Exception raised in your MMAdListener: " + e.getMessage());
                    return;
                }
            }
            return;
        }
        Request request = new Request(this.apid, null, true);
        if (!canRequestAd()) {
            MMAdViewSDK.Event.requestFailed(getContext(), this, request, new MMError(16));
        } else {
            MMAdViewSDK.Log.d("Fetching new ad...");
            requestAd(request);
        }
    }

    int checkInternal() {
        try {
            MMAdViewController.assignAdViewController(this);
            if (this.controller != null) {
                return this.controller.check(this);
            }
            return 100;
        } catch (Exception e) {
            MMAdViewSDK.Log.e("There was an exception checking for a cached ad. %s", e.getMessage());
            e.printStackTrace();
            return 100;
        }
    }

    int displayInternal() {
        try {
            MMAdViewController.assignAdViewController(this);
            if (this.controller != null) {
                return this.controller.display(this);
            }
            return 100;
        } catch (Exception e) {
            MMAdViewSDK.Log.e("There was an exception displaying a cached ad. %s", e.getMessage());
            e.printStackTrace();
            return 100;
        }
    }

    public boolean check() {
        if (MMAdViewSDK.isUiThread()) {
            return checkInternal() == 0;
        }
        MMAdViewSDK.Log.e("Only the main thread can access an MMAdView.");
        return false;
    }

    public boolean display() {
        if (MMAdViewSDK.isUiThread()) {
            return displayInternal() == 0;
        }
        MMAdViewSDK.Log.e("Only the main thread can access an MMAdView.");
        return false;
    }

    public void pause() {
        if (this.controller != null) {
            this.controller.pauseTimer(true);
        }
    }

    public void resume() {
        if (this.controller != null) {
            this.controller.resumeTimer(true);
        }
    }

    public void updateUserLocation(Location userLocation) {
        if (userLocation != null) {
            MMAdViewSDK.location = userLocation;
        }
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public void setAge(String age) {
        if (MMAdViewSDK.demographic != null) {
            MMAdViewSDK.demographic.put("age", age);
        }
    }

    public void setGender(String gender) {
        if (MMAdViewSDK.demographic != null) {
            MMAdViewSDK.demographic.gender = gender;
        }
    }

    public void setZip(String zip) {
        if (MMAdViewSDK.demographic != null) {
            MMAdViewSDK.demographic.put("zip", zip);
        }
    }

    public void setMarital(String marital) {
        if (MMAdViewSDK.demographic != null) {
            MMAdViewSDK.demographic.marital = marital;
        }
    }

    public void setIncome(String income) {
        if (MMAdViewSDK.demographic != null) {
            MMAdViewSDK.demographic.put("income", income);
        }
    }

    public void setEthnicity(String ethnicity) {
        if (MMAdViewSDK.demographic != null) {
            MMAdViewSDK.demographic.ethnicity = ethnicity;
        }
    }

    public void setOrientation(String orientation) {
        if (MMAdViewSDK.demographic != null) {
            MMAdViewSDK.demographic.orientation = orientation;
        }
    }

    public void setEducation(String education) {
        if (MMAdViewSDK.demographic != null) {
            MMAdViewSDK.demographic.education = education;
        }
    }

    public void setPolitics(String politics) {
        if (MMAdViewSDK.demographic != null) {
            MMAdViewSDK.demographic.put("politics", politics);
        }
    }

    public void setApid(String apid) {
        this.apid = apid;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public void setVendor(String vendor) {
        if (MMAdViewSDK.demographic != null) {
            MMAdViewSDK.demographic.put("vendor", vendor);
        }
    }

    public void setAcid(String acid) {
        this.acid = acid;
    }

    public void setMxsdk(String mxsdk) {
        this.mxsdk = mxsdk;
    }

    public void setMetaValues(Hashtable<String, String> metaHash) {
        if (metaHash != null) {
            if (metaHash.containsKey("mmacid")) {
                this.acid = metaHash.get("mmacid");
            }
            if (metaHash.containsKey("mxsdk")) {
                this.mxsdk = metaHash.get("mxsdk");
            }
            if (metaHash.containsKey("height")) {
                this.height = metaHash.get("height");
            }
            if (metaHash.containsKey("width")) {
                this.width = metaHash.get("width");
            }
            if (MMAdViewSDK.demographic != null) {
                for (Map.Entry<String, String> entry : metaHash.entrySet()) {
                    MMAdViewSDK.demographic.put(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    public void setListener(MMAdListener listener) {
        synchronized (this) {
            this.listener = listener;
        }
    }

    public MMAdListener getListener() {
        return this.listener;
    }

    public void setIgnoresDensityScaling(boolean ignoresDensityScaling) {
        synchronized (this) {
            this.ignoreDensityScaling = ignoresDensityScaling;
        }
    }

    public void setTransitionType(int type) {
        this.transitionType = type;
    }

    private static void checkPermissions(Context context) {
        if (context.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") == -1) {
            final AlertDialog phoneStateDialog = new AlertDialog.Builder(context).create();
            phoneStateDialog.setTitle("Whoops!");
            phoneStateDialog.setMessage("The developer has forgot to declare the READ_PHONE_STATE permission in the manifest file. Please reach out to the developer to remove this error.");
            phoneStateDialog.setButton(-3, "OK", new DialogInterface.OnClickListener() { // from class: com.millennialmedia.android.MMAdView.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    phoneStateDialog.cancel();
                }
            });
            phoneStateDialog.show();
        }
        if (context.checkCallingOrSelfPermission("android.permission.INTERNET") == -1) {
            final AlertDialog internetDialog = new AlertDialog.Builder(context).create();
            internetDialog.setTitle("Whoops!");
            internetDialog.setMessage("The developer has forgot to declare the INTERNET permission in the manifest file. Please reach out to the developer to remove this error.");
            internetDialog.setButton(-3, "OK", new DialogInterface.OnClickListener() { // from class: com.millennialmedia.android.MMAdView.3
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    internetDialog.cancel();
                }
            });
            internetDialog.show();
        }
        if (context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == -1) {
            final AlertDialog networkStateDialog = new AlertDialog.Builder(context).create();
            networkStateDialog.setTitle("Whoops!");
            networkStateDialog.setMessage("The developer has forgot to declare the ACCESS_NETWORK_STATE permission in the manifest file. Please reach out to the developer to remove this error.");
            networkStateDialog.setButton(-3, "OK", new DialogInterface.OnClickListener() { // from class: com.millennialmedia.android.MMAdView.4
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    networkStateDialog.cancel();
                }
            });
            networkStateDialog.show();
        }
    }

    private static void checkActivity(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            ComponentName cn = new ComponentName(context, "com.millennialmedia.android.MMActivity");
            pm.getActivityInfo(cn, 128);
        } catch (PackageManager.NameNotFoundException e) {
            MMAdViewSDK.Log.e("Activity MMActivity not declared in AndroidManifest.xml");
            e.printStackTrace();
            final AlertDialog activityDialog = new AlertDialog.Builder(context).create();
            activityDialog.setTitle("Whoops!");
            activityDialog.setMessage("The developer has forgot to declare the MMActivity in the manifest file. Please reach out to the developer to remove this error.");
            activityDialog.setButton(-3, "OK", new DialogInterface.OnClickListener() { // from class: com.millennialmedia.android.MMAdView.5
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    activityDialog.cancel();
                }
            });
            activityDialog.show();
        }
        try {
            ComponentName cn2 = new ComponentName(context, "com.millennialmedia.android.VideoPlayer");
            pm.getActivityInfo(cn2, 128);
        } catch (PackageManager.NameNotFoundException e2) {
            MMAdViewSDK.Log.e("Activity VideoPlayer not declared in AndroidManifest.xml");
            e2.printStackTrace();
            final AlertDialog activityDialog2 = new AlertDialog.Builder(context).create();
            activityDialog2.setTitle("Whoops!");
            activityDialog2.setMessage("The developer has forgot to declare the VideoPlayer in the manifest file. Please reach out to the developer to remove this error.");
            activityDialog2.setButton(-3, "OK", new DialogInterface.OnClickListener() { // from class: com.millennialmedia.android.MMAdView.6
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    activityDialog2.cancel();
                }
            });
            activityDialog2.show();
        }
    }

    public static class RequestListener {
        public void requestSucceeded(MMAdView adView) {
            MMAdViewSDK.Log.d("Ad request succeeded.");
        }

        public void requestFailed(MMAdView adView, MMError error) {
            MMAdViewSDK.Log.d("Ad request failed with error: %d %s.", Integer.valueOf(error.getCode()), error.getMessage());
        }
    }

    static class Request {
        String apid;
        boolean fetch;
        RequestListener requestListener;

        Request(String apid, RequestListener requestListener, boolean fetch) {
            this.apid = apid;
            this.requestListener = requestListener;
            this.fetch = fetch;
        }
    }

    public void startConversionTrackerWithGoalId(String goalId) {
        MMConversionTracker.trackConversion(getContext(), goalId);
    }

    public static void startConversionTrackerWithGoalId(Context context, String goalId) {
        checkPermissions(context);
        MMConversionTracker.trackConversion(context, goalId);
    }

    void prepareTransition(Bitmap bitmap) {
        this.imageView.setImageBitmap(bitmap);
        this.imageView.setVisibility(0);
        this.imageView.bringToFront();
    }

    void animateTransition() {
        if (this.imageView.getDrawable() != null) {
            MMAdViewSDK.runOnUiThread(new Runnable() { // from class: com.millennialmedia.android.MMAdView.7
                @Override // java.lang.Runnable
                public void run() {
                    Animation animation;
                    int type = MMAdView.this.transitionType;
                    if (type == 4) {
                        Random r = new Random();
                        type = r.nextInt(4);
                    }
                    switch (type) {
                        case 2:
                            float height = MMAdView.this.getHeight();
                            animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, -height);
                            break;
                        case 3:
                            float height2 = MMAdView.this.getHeight();
                            animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, height2);
                            break;
                        default:
                            animation = new AlphaAnimation(1.0f, 0.0f);
                            break;
                    }
                    animation.setDuration(1000L);
                    animation.setAnimationListener(MMAdView.this);
                    animation.setFillEnabled(true);
                    animation.setFillBefore(true);
                    animation.setFillAfter(true);
                    MMAdView.this.imageView.startAnimation(animation);
                }
            });
        }
    }

    @Override // android.view.animation.Animation.AnimationListener
    public void onAnimationStart(Animation animation) {
    }

    @Override // android.view.animation.Animation.AnimationListener
    public void onAnimationEnd(Animation animation) {
        this.imageView.setVisibility(8);
    }

    @Override // android.view.animation.Animation.AnimationListener
    public void onAnimationRepeat(Animation animation) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void printDiagnostics() throws Throwable {
        Context context = getContext();
        MMAdViewSDK.Log.i("MMAdView ID: %d", Integer.valueOf(getId()));
        MMAdViewSDK.Log.i("APID: %s", this.apid);
        Object[] objArr = new Object[1];
        objArr[0] = Environment.getExternalStorageState().equals("mounted") ? "" : "not ";
        MMAdViewSDK.Log.i("SD card is %savailable.", objArr);
        if (context != null) {
            MMAdViewSDK.Log.i("Package: %s", context.getPackageName());
            MMAdViewSDK.Log.i("HDID: %s", MMAdViewSDK.getHdid(context));
            MMAdViewSDK.Log.i("Permissions:");
            Object[] objArr2 = new Object[1];
            objArr2[0] = context.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") == -1 ? "not " : "";
            MMAdViewSDK.Log.i("android.permission.READ_PHONE_STATE is %spresent", objArr2);
            Object[] objArr3 = new Object[1];
            objArr3[0] = context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == -1 ? "not " : "";
            MMAdViewSDK.Log.i("android.permission.ACCESS_NETWORK_STATE is %spresent", objArr3);
            Object[] objArr4 = new Object[1];
            objArr4[0] = context.checkCallingOrSelfPermission("android.permission.INTERNET") == -1 ? "not " : "";
            MMAdViewSDK.Log.i("android.permission.INTERNET is %spresent", objArr4);
            Object[] objArr5 = new Object[1];
            objArr5[0] = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == -1 ? "not " : "";
            MMAdViewSDK.Log.i("android.permission.WRITE_EXTERNAL_STORAGE is %spresent", objArr5);
            Object[] objArr6 = new Object[1];
            objArr6[0] = context.checkCallingOrSelfPermission("android.permission.VIBRATE") == -1 ? "not " : "";
            MMAdViewSDK.Log.i("android.permission.VIBRATE is %spresent", objArr6);
            Object[] objArr7 = new Object[1];
            objArr7[0] = context.checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == -1 ? "not " : "";
            MMAdViewSDK.Log.i("android.permission.ACCESS_COARSE_LOCATION is %spresent", objArr7);
            Object[] objArr8 = new Object[1];
            objArr8[0] = context.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == -1 ? "not " : "";
            MMAdViewSDK.Log.i("android.permission.ACCESS_FINE_LOCATION is %spresent", objArr8);
            MMAdViewSDK.Log.i("Cached Ads:");
            AdCache.iterateCachedAds(context, 2, new AdCache.Iterator() { // from class: com.millennialmedia.android.MMAdView.8
                @Override // com.millennialmedia.android.AdCache.Iterator
                boolean callback(CachedAd ad) {
                    Object[] objArr9 = new Object[4];
                    objArr9[0] = ad.getTypeString();
                    objArr9[1] = ad.id;
                    objArr9[2] = ad.isOnDisk(MMAdView.this.getContext()) ? "" : "not ";
                    objArr9[3] = ad.isExpired() ? "" : "not ";
                    MMAdViewSDK.Log.i("%s %s is %son disk. Is %sexpired.", objArr9);
                    return true;
                }
            });
        }
    }

    String getCachedName() {
        if (this.adType == null || this.apid == null) {
            return null;
        }
        return this.adType + "_" + this.apid;
    }
}
