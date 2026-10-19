package com.sessionm.ui;

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.millennialmedia.android.MMAdViewSDK;
import com.sessionm.a.e;
import com.sessionm.core.Session;
import com.sessionm.json.JSONObject;
import com.sessionm.net.Request;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public class SessionMActivity extends Activity {
    private static final int ID_BACK_ImageButton = 1;
    private static final int ID_CLOSE_ImageButton = 4;
    private static final int ID_FORWARD_ImageButton = 2;
    private static final int ID_RELOAD_ImageButton = 3;
    public static final String INTENT_PARAM_CONTROLLER_ID = "controllerId";
    public static final String INTENT_PARAM_TYPE = "type";
    public static final String INTENT_PARAM_URL = "url";
    private ActivityController activityController;
    private ImageButton backBtn;
    private LinearLayout browserLayout;
    private android.webkit.WebView browserWebView;
    private ImageButton closeBtn;
    private RelativeLayout controlLayout;
    private ImageButton forwardBtn;
    private boolean isInlineBrowser;
    private ImageButton reloadBtn;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        runOnUiThread(new Runnable() { // from class: com.sessionm.ui.SessionMActivity.1
            @Override // java.lang.Runnable
            public void run() {
                int androidOrientationSdk10 = 0;
                Bundle extras = SessionMActivity.this.getIntent().getExtras();
                String string = extras.getString(SessionMActivity.INTENT_PARAM_TYPE);
                String string2 = extras.getString(SessionMActivity.INTENT_PARAM_URL);
                int i = extras.getInt(SessionMActivity.INTENT_PARAM_CONTROLLER_ID);
                SessionMActivity.this.activityController = Session.getSession().getCurrentActivityController();
                if (string == null || !string.equalsIgnoreCase("inlineBrowser")) {
                    if (SessionMActivity.this.activityController != null && SessionMActivity.this.activityController.getControllerId() == i) {
                        SessionMActivity.this.activityController.setPresentedActivity(SessionMActivity.this);
                        SessionMActivity.this.requestWindowFeature(1);
                        SessionMActivity.this.activityController.getWebView().setOnKeyListener(new View.OnKeyListener() { // from class: com.sessionm.ui.SessionMActivity.1.1
                            @Override // android.view.View.OnKeyListener
                            public boolean onKey(View view, int i2, KeyEvent keyEvent) {
                                switch (i2) {
                                    case 3:
                                    case 4:
                                        SessionMActivity.this.activityController.dismiss();
                                        break;
                                }
                                return true;
                            }
                        });
                        SessionMActivity.this.setContentView(SessionMActivity.this.activityController.getFrameLayout());
                        String[] orientations = SessionMActivity.this.activityController.getOrientations();
                        if (orientations != null) {
                            if (orientations.length == 0) {
                                androidOrientationSdk10 = -1;
                            } else {
                                for (String str : orientations) {
                                    androidOrientationSdk10 |= Build.VERSION.SDK_INT >= 10 ? SessionMActivity.this.getAndroidOrientationSdk10(str) : SessionMActivity.this.getAndroidOrientation(str);
                                }
                            }
                            SessionMActivity.this.setRequestedOrientation(androidOrientationSdk10);
                        }
                        if (string2 != null) {
                            SessionMActivity.this.activityController.presentLoader();
                            SessionMActivity.this.activityController.loadContent(Request.Type.CONTENT, string2, null);
                            return;
                        } else {
                            SessionMActivity.this.activityController.displayView();
                            return;
                        }
                    }
                    SessionMActivity.this.finish();
                    return;
                }
                SessionMActivity.this.isInlineBrowser = true;
                SessionMActivity.this.initializeInlineBrowser(string2);
            }
        });
    }

    @Override // android.app.Activity
    protected void onStop() {
        super.onStop();
        if (!this.isInlineBrowser) {
            this.activityController.setPresentedActivity(null);
            this.activityController.dismissLoader();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initializeInlineBrowser(String str) {
        getWindow().requestFeature(5);
        createBrowserMainLayout();
        createBrowserControlLayout();
        createBrowserWebview();
        startBrowserAnimation();
        if (str != null && str.length() > 0) {
            this.browserWebView.loadUrl(str);
        }
    }

    private void startBrowserAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.0f, 1, 0.0f);
        translateAnimation.setDuration(300L);
        translateAnimation.setInterpolator(this, R.anim.linear_interpolator);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.sessionm.ui.SessionMActivity.2
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                SessionMActivity.this.setTheme(R.style.Theme.Black);
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }
        });
        translateAnimation.setFillEnabled(true);
        translateAnimation.setFillAfter(true);
        this.browserLayout.startAnimation(translateAnimation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getAndroidOrientation(String str) {
        if (str != null) {
            if (str.equalsIgnoreCase("dynamic")) {
                return 2;
            }
            if (str.equalsIgnoreCase("portrait") || str.equalsIgnoreCase("portrait-upside-down")) {
                return 1;
            }
            if (str.equalsIgnoreCase("landscape") || str.equalsIgnoreCase("landscape-right") || str.equalsIgnoreCase("landscape-left")) {
                return 0;
            }
        }
        return 4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getAndroidOrientationSdk10(String str) {
        if (str != null) {
            if (str.equalsIgnoreCase("dynamic")) {
                return 2;
            }
            if (str.equalsIgnoreCase("portrait")) {
                return 1;
            }
            if (str.equalsIgnoreCase("landscape") || str.equalsIgnoreCase("landscape-right")) {
                return 0;
            }
            if (str.equalsIgnoreCase("landscape-left")) {
                return 8;
            }
            if (str.equalsIgnoreCase("portrait-upside-down")) {
                return 9;
            }
        }
        return 4;
    }

    /* JADX INFO: compiled from: ProGuard */
    private class BrowserWebViewClient extends WebViewClient {
        private BrowserWebViewClient() {
        }

        @Override // android.webkit.WebViewClient
        public void onPageStarted(android.webkit.WebView webView, String str, Bitmap bitmap) {
            ((Activity) webView.getContext()).setProgressBarIndeterminateVisibility(true);
        }

        @Override // android.webkit.WebViewClient
        public void onPageFinished(android.webkit.WebView webView, String str) {
            ((Activity) webView.getContext()).setProgressBarIndeterminateVisibility(false);
        }
    }

    private void createBrowserMainLayout() {
        this.browserLayout = new LinearLayout(this);
        this.browserLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.browserLayout.setOrientation(1);
        setContentView(this.browserLayout);
    }

    private void createBrowserControlLayout() {
        this.controlLayout = new RelativeLayout(this);
        this.controlLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        this.controlLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        this.browserLayout.setScrollBarStyle(33554432);
        this.browserLayout.addView(this.controlLayout);
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{-592138, -1973791, -1});
        gradientDrawable.setCornerRadius(0.0f);
        gradientDrawable.setStroke(1, -12574208);
        gradientDrawable.setCornerRadius(1.0f);
        this.controlLayout.setBackgroundDrawable(gradientDrawable);
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        Bitmap bitmapDecodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.ic_media_play);
        Bitmap bitmapDecodeResource2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_menu_close_clear_cancel);
        Bitmap bitmapDecodeResource3 = BitmapFactory.decodeResource(getResources(), R.drawable.stat_notify_sync);
        this.backBtn = getControlImageButton(50, 50, Bitmap.createBitmap(bitmapDecodeResource, 0, 0, bitmapDecodeResource.getWidth(), bitmapDecodeResource.getHeight(), matrix, true), 10, 10, 10, 10);
        this.backBtn.setId(1);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(this.backBtn.getLayoutParams());
        layoutParams.addRule(9);
        this.backBtn.setLayoutParams(layoutParams);
        this.controlLayout.addView(this.backBtn);
        this.forwardBtn = getControlImageButton(50, 50, bitmapDecodeResource, 10, 10, 10, 10);
        this.forwardBtn.setId(2);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(this.forwardBtn.getLayoutParams());
        layoutParams2.addRule(1, 1);
        this.forwardBtn.setLayoutParams(layoutParams2);
        this.controlLayout.addView(this.forwardBtn);
        this.reloadBtn = getControlImageButton(50, 50, bitmapDecodeResource3, 0, 10, 10, 10);
        this.reloadBtn.setId(3);
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(this.reloadBtn.getLayoutParams());
        layoutParams3.addRule(1, 2);
        this.reloadBtn.setLayoutParams(layoutParams3);
        this.controlLayout.addView(this.reloadBtn);
        this.closeBtn = getControlImageButton(50, 50, bitmapDecodeResource2, 10, 10, 10, 10);
        this.closeBtn.setId(4);
        RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(this.closeBtn.getLayoutParams());
        layoutParams4.addRule(11);
        this.closeBtn.setLayoutParams(layoutParams4);
        this.controlLayout.addView(this.closeBtn);
        layoutParams4.addRule(13);
        this.reloadBtn.setOnClickListener(new View.OnClickListener() { // from class: com.sessionm.ui.SessionMActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SessionMActivity.this.browserWebView.reload();
            }
        });
        this.closeBtn.setOnClickListener(new View.OnClickListener() { // from class: com.sessionm.ui.SessionMActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("returnedActivity", MMAdViewSDK.Event.INTENT_EXTERNAL_BROWSER);
                SessionMActivity.this.setResult(-1, intent);
                SessionMActivity.this.handleCloseCommand();
            }
        });
        this.backBtn.setOnClickListener(new View.OnClickListener() { // from class: com.sessionm.ui.SessionMActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SessionMActivity.this.browserWebView.canGoBack()) {
                    SessionMActivity.this.browserWebView.goBack();
                }
            }
        });
        this.forwardBtn.setOnClickListener(new View.OnClickListener() { // from class: com.sessionm.ui.SessionMActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SessionMActivity.this.browserWebView.canGoForward()) {
                    SessionMActivity.this.browserWebView.goForward();
                }
            }
        });
        this.controlLayout.setPadding(10, 10, 10, 10);
    }

    private void createBrowserWebview() {
        this.browserWebView = new android.webkit.WebView(this);
        this.browserWebView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.browserWebView.setScrollBarStyle(33554432);
        this.browserLayout.addView(this.browserWebView);
        this.browserWebView.addJavascriptInterface(this, "Android");
        this.browserWebView.getSettings().setJavaScriptEnabled(true);
        this.browserWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.browserWebView.getSettings().setLoadsImagesAutomatically(true);
        this.browserWebView.getSettings().setPluginsEnabled(true);
        this.browserWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        this.browserWebView.setWebViewClient(new WebViewClient());
        this.browserWebView.setFocusableInTouchMode(true);
        this.browserWebView.setClickable(true);
        this.browserWebView.setEnabled(true);
        this.browserWebView.getSettings().setGeolocationEnabled(true);
        this.browserWebView.setWebViewClient(new BrowserWebViewClient());
        this.browserWebView.setWebChromeClient(new WebChromeClient());
    }

    private ImageButton getControlImageButton(int i, int i2, Bitmap bitmap, int i3, int i4, int i5, int i6) {
        ImageButton imageButton = new ImageButton(this);
        imageButton.setLayoutParams(new RelativeLayout.LayoutParams(e.a(this, i), e.a(this, i2)));
        imageButton.setImageBitmap(bitmap);
        return imageButton;
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        switch (i) {
            case 3:
            case 4:
                finish();
                return true;
            default:
                return super.onKeyDown(i, keyEvent);
        }
    }

    public void bridgeAction(String str) {
        try {
            String string = JSONObject.create(str).getString("handler");
            if (string != null && string.length() != 0 && string.equalsIgnoreCase("close")) {
                handleCloseCommand();
            }
        } catch (Throwable th) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCloseCommand() {
        this.browserLayout.getHandler().post(new Runnable() { // from class: com.sessionm.ui.SessionMActivity.7
            @Override // java.lang.Runnable
            public void run() {
                TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, 1.0f);
                translateAnimation.setDuration(400L);
                translateAnimation.setFillAfter(true);
                translateAnimation.setInterpolator(SessionMActivity.this, R.anim.linear_interpolator);
                translateAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.sessionm.ui.SessionMActivity.7.1
                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationEnd(Animation animation) {
                        if (!SessionMActivity.this.isFinishing()) {
                            SessionMActivity.this.finish();
                        }
                    }

                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationStart(Animation animation) {
                        SessionMActivity.this.activityController.executeJavascript("GreyhoundEventDispatcher.dispatch('browserDidClose',{});");
                    }
                });
                SessionMActivity.this.browserLayout.startAnimation(translateAnimation);
            }
        });
    }
}
