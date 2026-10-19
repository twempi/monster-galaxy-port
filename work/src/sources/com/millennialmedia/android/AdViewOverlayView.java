package com.millennialmedia.android;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.lang.reflect.Method;
import oauth.signpost.OAuth;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import twitter4j.internal.http.HttpResponseCode;

/* JADX INFO: loaded from: classes.dex */
class AdViewOverlayView extends FrameLayout {
    private static final int TITLE_MARGIN_X = 8;
    private static final int TITLE_MARGIN_Y = 9;
    private RelativeLayout content;
    private Button mraidCloseButton;
    private RelativeLayout navBar;
    private Button navCloseButton;
    private String overlayUrl;
    private ProgressBar progressBar;
    private boolean progressDone;
    private OverlaySettings settings;
    private TextView title;
    Handler viewHandler;
    protected WebView webView;

    private static final class NonConfigurationInstance {
        boolean bottomBarEnabled;
        boolean bottomBarVisible;
        boolean progressDone;
        WebView webView;

        private NonConfigurationInstance() {
        }
    }

    AdViewOverlayView(final Activity activity, OverlaySettings settings) {
        super(activity);
        this.viewHandler = new Handler() { // from class: com.millennialmedia.android.AdViewOverlayView.12
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 2:
                        AdViewOverlayView.this.dismiss(true);
                        break;
                }
            }
        };
        setId(15062);
        if (activity != null) {
            this.settings = settings;
            this.settings.isBannerAd = false;
            NonConfigurationInstance nonConfigurationInstance = (NonConfigurationInstance) activity.getLastNonConfigurationInstance();
            if (nonConfigurationInstance != null) {
                this.settings.shouldShowBottomBar = nonConfigurationInstance.bottomBarVisible;
                this.settings.shouldEnableBottomBar = nonConfigurationInstance.bottomBarEnabled;
                this.progressDone = nonConfigurationInstance.progressDone;
                this.webView = nonConfigurationInstance.webView;
            }
            setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
            float scale = activity.getResources().getDisplayMetrics().density;
            Integer scaledPadding = Integer.valueOf((int) (0.0625f * scale * this.settings.shouldResizeOverlay));
            setPadding(scaledPadding.intValue(), scaledPadding.intValue(), scaledPadding.intValue(), scaledPadding.intValue());
            this.content = new RelativeLayout(activity);
            this.content.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
            addView(this.content);
            RelativeLayout titleBar = null;
            if (this.settings.shouldShowCustomClose && this.settings.shouldShowTitlebar) {
                titleBar = new RelativeLayout(activity);
                RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(-1, -2);
                titleParams.addRule(10);
                titleBar.setLayoutParams(titleParams);
                titleBar.setBackgroundColor(-16777216);
                titleBar.setId(100);
                this.title = new TextView(activity);
                this.title.setText(this.settings.overlayTitle);
                this.title.setTextColor(-1);
                this.title.setBackgroundColor(-16777216);
                this.title.setTypeface(Typeface.DEFAULT_BOLD);
                this.title.setPadding(8, 9, 8, 9);
                this.title.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
                titleBar.addView(this.title);
                Button closeButton = new Button(activity);
                closeButton.setBackgroundColor(-16777216);
                closeButton.setText("Close");
                closeButton.setTextColor(-1);
                closeButton.setOnTouchListener(new View.OnTouchListener() { // from class: com.millennialmedia.android.AdViewOverlayView.1
                    @Override // android.view.View.OnTouchListener
                    public boolean onTouch(View view, MotionEvent event) {
                        int action = event.getAction();
                        switch (action) {
                            case 0:
                                return true;
                            case 1:
                                AdViewOverlayView.this.title.setBackgroundColor(-7829368);
                                AdViewOverlayView.this.dismiss(true);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-2, -2);
                lp.addRule(11);
                titleBar.addView(closeButton, lp);
                this.content.addView(titleBar);
            }
            if (settings.shouldShowCustomClose) {
                this.navBar = new RelativeLayout(activity);
                RelativeLayout.LayoutParams navLayoutParams = new RelativeLayout.LayoutParams(-1, -2);
                navLayoutParams.addRule(12);
                this.navBar.setLayoutParams(navLayoutParams);
                this.navBar.setBackgroundDrawable(new BottomBarDrawable());
                this.navBar.setId(HttpResponseCode.MULTIPLE_CHOICES);
                this.navCloseButton = new Button(activity);
                this.navCloseButton.setBackgroundColor(-16777216);
                setCloseButtonListener(this.settings.shouldEnableBottomBar);
                int closeHeight = (int) ((25.0f * scale) + 0.5f);
                RelativeLayout.LayoutParams closeLP = new RelativeLayout.LayoutParams(closeHeight, closeHeight);
                int i = (int) ((12.0f * scale) + 0.5f);
                closeLP.rightMargin = i;
                closeLP.bottomMargin = i;
                closeLP.topMargin = (int) ((15.0f * scale) + 0.5f);
                closeLP.addRule(11);
                closeLP.addRule(15);
                this.navBar.addView(this.navCloseButton, closeLP);
                this.navBar.post(new SetCloseButtonTouchDelegateRunnable(this.navCloseButton, closeLP.topMargin, closeLP.leftMargin, closeLP.bottomMargin, closeLP.rightMargin));
                this.content.addView(this.navBar);
                if (this.settings.shouldShowBottomBar) {
                    this.navBar.setVisibility(0);
                } else {
                    this.navBar.setVisibility(8);
                }
            } else {
                this.mraidCloseButton = new Button(activity);
                this.mraidCloseButton.setId(301);
                this.mraidCloseButton.setBackgroundDrawable(new CloseTopDrawable(true, scale));
                this.mraidCloseButton.setOnClickListener(new View.OnClickListener() { // from class: com.millennialmedia.android.AdViewOverlayView.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        MMAdViewSDK.Log.v("Close button clicked.");
                        AdViewOverlayView.this.dismiss(true);
                    }
                });
                int closeHeight2 = (int) ((50.0f * scale) + 0.5f);
                RelativeLayout.LayoutParams closeButParams = new RelativeLayout.LayoutParams(closeHeight2, closeHeight2);
                closeButParams.addRule(11);
                closeButParams.addRule(10);
                this.mraidCloseButton.setLayoutParams(closeButParams);
                this.mraidCloseButton.post(new SetCloseButtonTouchDelegateRunnable(this.mraidCloseButton, closeButParams.topMargin, closeButParams.leftMargin, closeButParams.bottomMargin, closeButParams.rightMargin));
            }
            if (this.webView == null) {
                this.webView = new WebView(activity);
            }
            this.webView.setId(HttpResponseCode.OK);
            RelativeLayout.LayoutParams webviewLp = new RelativeLayout.LayoutParams(-1, -1);
            if (titleBar != null) {
                webviewLp.addRule(3, titleBar.getId());
            } else {
                webviewLp.addRule(10);
            }
            if (this.navBar != null) {
                webviewLp.addRule(2, this.navBar.getId());
            } else {
                webviewLp.addRule(12);
            }
            this.webView.setLayoutParams(webviewLp);
            this.webView.setWebViewClient(new OverlayWebViewClient(settings));
            this.webView.addJavascriptInterface(new OverlayJSInterface(), "interface");
            WebSettings webSettings = this.webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDefaultTextEncodingName(OAuth.ENCODING);
            try {
                Method method = webSettings.getClass().getMethod("setLoadWithOverviewMode", Boolean.TYPE);
                method.invoke(webSettings, true);
            } catch (Exception e) {
            }
            try {
                Method method2 = webSettings.getClass().getMethod("setGeolocationEnabled", Boolean.TYPE);
                method2.invoke(webSettings, true);
            } catch (Exception e2) {
            }
            final GestureDetector gestDetector = new GestureDetector(activity, new GestureDetector.SimpleOnGestureListener() { // from class: com.millennialmedia.android.AdViewOverlayView.3
                @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
                public boolean onSingleTapConfirmed(MotionEvent e3) {
                    MMAdViewSDK.Event.overlayTap(activity);
                    return false;
                }
            });
            this.webView.setOnTouchListener(new View.OnTouchListener() { // from class: com.millennialmedia.android.AdViewOverlayView.4
                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View v, MotionEvent event) {
                    gestDetector.onTouchEvent(event);
                    return false;
                }
            });
            if (this.settings.shouldMakeOverlayTransparent) {
                this.webView.setBackgroundColor(0);
                try {
                    Method method3 = WebView.class.getMethod("setLayerType", Integer.TYPE, Paint.class);
                    method3.invoke(this.webView, 1, null);
                } catch (Exception e3) {
                }
                this.content.setBackgroundColor(0);
            } else {
                this.webView.setBackgroundColor(-1);
                this.content.setBackgroundColor(-1);
            }
            this.content.addView(this.webView);
            if (this.mraidCloseButton != null) {
                this.content.addView(this.mraidCloseButton);
            }
            if (!this.progressDone) {
                this.progressBar = new ProgressBar(activity);
                this.progressBar.setIndeterminate(true);
                this.progressBar.setVisibility(0);
                RelativeLayout.LayoutParams progParams = new RelativeLayout.LayoutParams(-2, -2);
                progParams.addRule(13);
                this.content.addView(this.progressBar, progParams);
            }
            if (nonConfigurationInstance == null) {
                animateView();
            }
            if (settings.delayShowBottombar > 0) {
                this.viewHandler.postDelayed(new Runnable() { // from class: com.millennialmedia.android.AdViewOverlayView.5
                    @Override // java.lang.Runnable
                    public void run() {
                        AdViewOverlayView.this.settings.shouldShowBottomBar = true;
                        if (AdViewOverlayView.this.navBar != null) {
                            AdViewOverlayView.this.navBar.setVisibility(0);
                        }
                    }
                }, settings.delayShowBottombar);
            }
            if (settings.delayEnableBottombar > 0) {
                this.viewHandler.postDelayed(new Runnable() { // from class: com.millennialmedia.android.AdViewOverlayView.6
                    @Override // java.lang.Runnable
                    public void run() {
                        AdViewOverlayView.this.settings.shouldEnableBottomBar = true;
                        AdViewOverlayView.this.setCloseButtonListener(true);
                    }
                }, settings.delayEnableBottombar);
            }
        }
    }

    void stopProgress() {
        if (!this.progressDone) {
            this.progressDone = true;
            this.progressBar.setVisibility(8);
            this.content.removeView(this.progressBar);
            this.progressBar = null;
        }
    }

    private class SetCloseButtonTouchDelegateRunnable implements Runnable {
        int bottom;
        private final Button closeButton;
        int left;
        int right;
        int top;

        SetCloseButtonTouchDelegateRunnable(Button closeButton, int topMargin, int leftMargin, int bottomMargin, int rightMargin) {
            this.closeButton = closeButton;
            this.top = topMargin;
            this.left = leftMargin;
            this.bottom = bottomMargin;
            this.right = rightMargin;
        }

        @Override // java.lang.Runnable
        public void run() {
            Rect delegateArea = new Rect();
            this.closeButton.getHitRect(delegateArea);
            delegateArea.top += this.top;
            delegateArea.right += this.right;
            delegateArea.bottom += this.bottom;
            delegateArea.left += this.left;
            TouchDelegate expandedArea = new TouchDelegate(delegateArea, this.closeButton);
            if (View.class.isInstance(this.closeButton.getParent())) {
                ((View) this.closeButton.getParent()).setTouchDelegate(expandedArea);
            }
        }
    }

    private class BottomBarDrawable extends Drawable {
        protected final Paint paint = new Paint();

        public BottomBarDrawable() {
            this.paint.setStrokeWidth(1.0f);
            this.paint.setStyle(Paint.Style.STROKE);
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            Rect bounds = copyBounds();
            int startEndColor = Color.argb(255, 8, 8, 8);
            int middleColor = Color.argb(255, 50, 50, 50);
            int borderColor = Color.argb(255, Opcodes.ISHL, Opcodes.ISHL, Opcodes.ISHL);
            GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{middleColor, startEndColor});
            gradient.setBounds(bounds);
            gradient.draw(canvas);
            this.paint.setARGB(255, 0, 0, 0);
            canvas.drawLine(bounds.left, 0.0f, bounds.right, 0.0f, this.paint);
            this.paint.setColor(borderColor);
            canvas.drawLine(bounds.left, 1.0f, bounds.right, 1.0f, this.paint);
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -3;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int alpha) {
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter cf) {
        }
    }

    private class CloseDrawable extends Drawable {
        protected boolean enabled;
        protected final Paint paint = new Paint();

        CloseDrawable(boolean enabled) {
            this.enabled = true;
            this.enabled = enabled;
            this.paint.setAntiAlias(true);
            this.paint.setStyle(Paint.Style.STROKE);
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            Rect bounds = copyBounds();
            int width = bounds.right - bounds.left;
            int height = bounds.bottom - bounds.top;
            float strokeWidth = width / 6.0f;
            this.paint.setStrokeWidth(strokeWidth);
            int grayScale = this.enabled ? 255 : 80;
            this.paint.setARGB(255, grayScale, grayScale, grayScale);
            canvas.drawLine(strokeWidth / 2.0f, strokeWidth / 2.0f, width - (strokeWidth / 2.0f), height - (strokeWidth / 2.0f), this.paint);
            canvas.drawLine(width - (strokeWidth / 2.0f), strokeWidth / 2.0f, strokeWidth / 2.0f, height - (strokeWidth / 2.0f), this.paint);
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -3;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int alpha) {
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter cf) {
        }
    }

    private class CloseTopDrawable extends CloseDrawable {
        final float dist;
        final float scale;

        CloseTopDrawable(boolean enabled, float scale) {
            super(enabled);
            this.scale = scale;
            this.dist = 4.0f * scale;
            this.paint.setColor(-16777216);
        }

        @Override // com.millennialmedia.android.AdViewOverlayView.CloseDrawable, android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            Rect bounds = copyBounds();
            int width = bounds.right - bounds.left;
            float strokeWidth = width / 10.0f;
            float cx = bounds.right - (this.scale * 20.0f);
            float cy = bounds.top + (this.scale * 20.0f);
            this.paint.setStrokeWidth(strokeWidth);
            this.paint.setColor(-16777216);
            this.paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(cx, cy, 12.0f * this.scale, this.paint);
            this.paint.setColor(-1);
            this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawCircle(cx, cy, this.scale * 10.0f, this.paint);
            this.paint.setColor(-16777216);
            canvas.drawCircle(cx, cy, 7.0f * this.scale, this.paint);
            this.paint.setColor(-1);
            this.paint.setStrokeWidth(strokeWidth / 2.0f);
            this.paint.setStyle(Paint.Style.STROKE);
            canvas.drawLine(cx - this.dist, cy - this.dist, cx + this.dist, cy + this.dist, this.paint);
            canvas.drawLine(cx + this.dist, cy - this.dist, cx - this.dist, cy + this.dist, this.paint);
        }
    }

    private void animateView() {
        if (this.settings.overlayTransition.equals("toptobottom")) {
            TranslateAnimation translateDown = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, -1.0f, 1, 0.0f);
            translateDown.setDuration(this.settings.transitionTime);
            translateDown.setAnimationListener(new Animation.AnimationListener() { // from class: com.millennialmedia.android.AdViewOverlayView.7
                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationEnd(Animation arg0) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationRepeat(Animation arg0) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationStart(Animation arg0) {
                }
            });
            MMAdViewSDK.Log.v("Translate down");
            startAnimation(translateDown);
            return;
        }
        if (this.settings.overlayTransition.equals("explode")) {
            ScaleAnimation scale = new ScaleAnimation(1.1f, 0.9f, 0.1f, 0.9f, 1, 0.5f, 1, 0.5f);
            scale.setDuration(this.settings.transitionTime);
            scale.setAnimationListener(new Animation.AnimationListener() { // from class: com.millennialmedia.android.AdViewOverlayView.8
                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationEnd(Animation arg0) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationRepeat(Animation arg0) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationStart(Animation arg0) {
                }
            });
            MMAdViewSDK.Log.v("Explode");
            startAnimation(scale);
            return;
        }
        if (!this.settings.overlayTransition.equals("none")) {
            TranslateAnimation translateUp = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.0f, 1, 0.0f);
            translateUp.setDuration(this.settings.transitionTime);
            translateUp.setAnimationListener(new Animation.AnimationListener() { // from class: com.millennialmedia.android.AdViewOverlayView.9
                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationEnd(Animation arg0) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationRepeat(Animation arg0) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationStart(Animation arg0) {
                }
            });
            MMAdViewSDK.Log.v("Translate up");
            startAnimation(translateUp);
        }
    }

    boolean goBack() {
        if (!this.webView.canGoBack()) {
            return false;
        }
        this.webView.goBack();
        return true;
    }

    void loadWebContent(String url) {
        this.overlayUrl = url;
        if (MMAdViewSDK.isConnected(getContext())) {
            this.webView.loadUrl(this.overlayUrl);
        } else {
            MMAdViewSDK.Log.e("No network available, can't load overlay.");
        }
    }

    void injectJS(String jsString) {
        this.webView.loadUrl(jsString);
    }

    Object getNonConfigurationInstance() {
        NonConfigurationInstance nonConfigurationInstance = new NonConfigurationInstance();
        this.webView.setWebViewClient(null);
        ((ViewGroup) this.webView.getParent()).removeView(this.webView);
        if (this.navBar != null) {
            nonConfigurationInstance.bottomBarVisible = this.navBar.getVisibility() == 0;
            nonConfigurationInstance.bottomBarEnabled = this.navBar.isEnabled();
        }
        nonConfigurationInstance.progressDone = this.progressDone;
        nonConfigurationInstance.webView = this.webView;
        return nonConfigurationInstance;
    }

    protected void setCloseButtonListener(boolean enabled) {
        if (this.navCloseButton != null) {
            if (enabled) {
                this.navCloseButton.setBackgroundDrawable(new CloseDrawable(true));
                this.navCloseButton.setOnClickListener(new View.OnClickListener() { // from class: com.millennialmedia.android.AdViewOverlayView.10
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        MMAdViewSDK.Log.v("Close button clicked.");
                        AdViewOverlayView.this.dismiss(true);
                    }
                });
                this.navCloseButton.setEnabled(true);
            } else {
                this.navCloseButton.setBackgroundDrawable(new CloseDrawable(false));
                this.navCloseButton.setEnabled(false);
            }
        }
    }

    void dismiss(boolean animated) {
        MMAdViewSDK.Log.d("Ad overlay closed");
        Activity activity = (Activity) getContext();
        if (activity != null) {
            if (animated) {
                AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
                animation.setAnimationListener(new Animation.AnimationListener() { // from class: com.millennialmedia.android.AdViewOverlayView.11
                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationStart(Animation animation2) {
                        if (AdViewOverlayView.this.mraidCloseButton != null) {
                            AdViewOverlayView.this.mraidCloseButton.setVisibility(8);
                        }
                    }

                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationEnd(Animation animation2) {
                        Activity activity2 = (Activity) AdViewOverlayView.this.getContext();
                        activity2.finish();
                    }

                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationRepeat(Animation animation2) {
                    }
                });
                animation.setFillEnabled(true);
                animation.setFillBefore(true);
                animation.setFillAfter(true);
                animation.setDuration(400L);
                startAnimation(animation);
                return;
            }
            activity.finish();
        }
    }

    class OverlayJSInterface {
        OverlayJSInterface() {
        }

        public void shouldCloseOverlay() {
            AdViewOverlayView.this.viewHandler.sendEmptyMessage(2);
        }

        public void shouldVibrate(long milliseconds) {
            if (AdViewOverlayView.this.getContext().checkCallingOrSelfPermission("android.permission.VIBRATE") == 0) {
                Vibrator v = (Vibrator) AdViewOverlayView.this.getContext().getSystemService("vibrator");
                v.vibrate(milliseconds);
            }
        }

        public void shouldEnableBottomBar(final boolean enabled) {
            if (AdViewOverlayView.this.settings.shouldEnableBottomBar) {
                MMAdViewSDK.Log.d("Should Enable Bottom Bar: %b", Boolean.valueOf(enabled));
                AdViewOverlayView.this.viewHandler.post(new Runnable() { // from class: com.millennialmedia.android.AdViewOverlayView.OverlayJSInterface.1
                    @Override // java.lang.Runnable
                    public void run() {
                        AdViewOverlayView.this.setCloseButtonListener(enabled);
                    }
                });
            }
        }

        public void shouldShowBottomBar(final boolean show) {
            if (AdViewOverlayView.this.settings.shouldShowBottomBar) {
                MMAdViewSDK.Log.d("Should show Bottom Bar: %b", Boolean.valueOf(show));
                AdViewOverlayView.this.viewHandler.post(new Runnable() { // from class: com.millennialmedia.android.AdViewOverlayView.OverlayJSInterface.2
                    @Override // java.lang.Runnable
                    public void run() {
                        if (AdViewOverlayView.this.navBar != null) {
                            if (show) {
                                AdViewOverlayView.this.navBar.setVisibility(0);
                            } else {
                                AdViewOverlayView.this.navBar.setVisibility(8);
                            }
                        }
                    }
                });
            }
        }
    }

    final class OverlayWebViewClient extends MMWebViewClient {
        OverlayWebViewClient(OverlaySettings settings) {
            super(settings);
        }

        @Override // android.webkit.WebViewClient
        public void onPageStarted(WebView view, String urlString, Bitmap favicon) {
            VideoAd ad;
            MMAdViewSDK.Log.d("onPageStarted: %s", urlString);
            if (urlString != null) {
                Activity activity = (Activity) AdViewOverlayView.this.getContext();
                if (activity == null) {
                    MMAdViewSDK.Log.d("Activity is null. Returning from click");
                    return;
                }
                Uri destinationUri = Uri.parse(urlString);
                if (destinationUri != null && destinationUri.getScheme() != null && destinationUri.getScheme().equalsIgnoreCase("mmvideo")) {
                    String id = destinationUri.getHost();
                    if (id != null && (ad = (VideoAd) AdCache.load(activity, id)) != null && ad.canShow(activity, null, false)) {
                        ad.show(activity, null);
                    }
                    AdViewOverlayView.this.webView.goBack();
                    return;
                }
                if (destinationUri.getScheme() != null && destinationUri.getScheme().equalsIgnoreCase("http")) {
                    if (destinationUri.getLastPathSegment() != null && (destinationUri.getLastPathSegment().endsWith(".mp4") || destinationUri.getLastPathSegment().endsWith(".3gp"))) {
                        MMAdViewSDK.Log.v("Creating video player intent.");
                        Intent intent = new Intent(activity, (Class<?>) MMActivity.class);
                        intent.setData(destinationUri);
                        intent.putExtra("class", "com.millennialmedia.android.VideoPlayerActivity");
                        activity.startActivity(intent);
                        MMAdViewSDK.Event.intentStarted(AdViewOverlayView.this.getContext(), null, MMAdViewSDK.Event.INTENT_STREAMING_VIDEO);
                        return;
                    }
                    shouldShowAndEnableBottomBar();
                    return;
                }
                try {
                    Intent intent2 = HttpRedirection.getIntentFromUri(activity, destinationUri, null);
                    if (intent2 != null) {
                        activity.startActivity(intent2);
                        AdViewOverlayView.this.webView.stopLoading();
                        AdViewOverlayView.this.webView.goBack();
                    } else {
                        MMAdViewSDK.Log.d("Uncertain about content. Stay in the overlay");
                        shouldShowAndEnableBottomBar();
                    }
                } catch (ActivityNotFoundException e) {
                    MMAdViewSDK.Log.e(e.getMessage());
                }
            }
        }

        @Override // com.millennialmedia.android.MMWebViewClient, android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView view, String urlString) {
            MMAdViewSDK.Log.d("shouldOverrideUrlLoading: %s", urlString);
            return super.shouldOverrideUrlLoading(view, urlString);
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            MMAdViewSDK.Log.e("Error: %s %s %s", Integer.valueOf(errorCode), description, failingUrl);
        }

        public void shouldShowAndEnableBottomBar() {
            if (this.settings.shouldShowBottomBar) {
                MMAdViewSDK.Log.v("Showing bottom bar");
                if (AdViewOverlayView.this.navBar != null) {
                    AdViewOverlayView.this.navBar.setVisibility(0);
                    if (this.settings.shouldEnableBottomBar) {
                        MMAdViewSDK.Log.v("Enabling bottom bar");
                        AdViewOverlayView.this.setCloseButtonListener(true);
                    }
                }
            }
        }

        @Override // android.webkit.WebViewClient
        public void onPageFinished(WebView view, String url) {
            if (!this.hasDoneMraidCalls) {
                this.hasDoneMraidCalls = true;
                AdViewOverlayView.this.webView.loadUrl("javascript:MMSDK.mraid.setPlacementType('interstitial');");
                if (AdViewOverlayView.this.hasWindowFocus()) {
                    AdViewOverlayView.this.webView.loadUrl("javascript:MMSDK.mraid.stateChange('expanded');");
                    AdViewOverlayView.this.webView.loadUrl("javascript:MMSDK.mraid.viewableChange(true)");
                } else {
                    AdViewOverlayView.this.webView.loadUrl("javascript:MMSDK.mraid.stateChange('hidden');");
                    AdViewOverlayView.this.webView.loadUrl("javascript:MMSDK.mraid.viewableChange(false)");
                }
                AdViewOverlayView.this.webView.loadUrl("javascript:MMSDK.mraid.ready();");
            }
            AdViewOverlayView.this.stopProgress();
            super.onPageFinished(view, url);
        }
    }

    @Override // android.view.View
    public void onWindowFocusChanged(boolean windowInFocus) {
        super.onWindowFocusChanged(windowInFocus);
        if (windowInFocus) {
            this.webView.loadUrl("javascript:MMSDK.mraid.stateChange('expanded');");
            this.webView.loadUrl("javascript:MMSDK.mraid.viewableChange(true)");
        } else {
            this.webView.loadUrl("javascript:MMSDK.mraid.stateChange('hidden');");
            this.webView.loadUrl("javascript:MMSDK.mraid.viewableChange(false)");
        }
    }
}
