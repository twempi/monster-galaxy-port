package com.sessionm.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.FrameLayout;
import com.sessionm.core.Session;
import com.sessionm.logging.Logger;
import java.util.List;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public class WebView extends android.webkit.WebView {
    protected static final String TAG = "WebView";
    private Activity mActivity;
    private HTML5InlineVideoJSInterface mInlineVideoInterface;
    private FrameLayout mLayout;
    private SensorEventListener mOrientationListener;
    private MyWebChromeClient mWebChromeClient;
    static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(-1, -1, 17);
    static int lastOrientationDegrees = 0;

    public WebView(Context context) {
        super(context);
        init(context);
    }

    public WebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public WebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        this.mActivity = (Activity) context;
        this.mLayout = new FrameLayout(context);
        this.mWebChromeClient = new MyWebChromeClient();
        setWebChromeClient(this.mWebChromeClient);
        setScrollBarStyle(33554432);
        WebSettings settings = getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setSavePassword(false);
        settings.setSaveFormData(false);
        settings.setJavaScriptEnabled(true);
        settings.setLightTouchEnabled(false);
        setId(20000);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setGeolocationEnabled(true);
        settings.setGeolocationDatabasePath("/data/data/com.sessionm.sdk/databases/");
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(0);
        settings.setAllowFileAccess(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setPluginsEnabled(true);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setSupportZoom(false);
        settings.setSupportMultipleWindows(false);
        settings.setUseWideViewPort(false);
        requestDisallowInterceptTouchEvent(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setClickable(true);
        setEnabled(true);
        setBackgroundColor(0);
        CookieManager.getInstance().setAcceptCookie(true);
        requestFocus(130);
        setOnTouchListener(new View.OnTouchListener() { // from class: com.sessionm.ui.WebView.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case 0:
                    case 1:
                        if (!view.hasFocus()) {
                            view.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });
        this.mInlineVideoInterface = new HTML5InlineVideoJSInterface((Activity) context, this);
        addJavascriptInterface(this.mInlineVideoInterface, "AndroidVideo");
        if (Build.VERSION.SDK_INT >= 11) {
            try {
                View.class.getMethod("setLayerType", Integer.TYPE, Paint.class).invoke(this, 1, new Paint());
            } catch (Exception e) {
                Logger.info(TAG, e.getStackTrace().toString());
            }
        }
        this.mLayout.addView(this);
        requestFocus();
    }

    public FrameLayout getLayout() {
        return this.mLayout;
    }

    @Override // android.webkit.WebView
    public void destroy() {
        if (this.mInlineVideoInterface != null) {
            this.mInlineVideoInterface.destroy();
            this.mInlineVideoInterface = null;
        }
        removeOrientationListener();
        super.destroy();
    }

    @Override // android.webkit.WebView, android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        switch (i) {
            case 3:
            case 4:
                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService("input_method");
                requestFocus();
                if (getVisibility() == 0) {
                    if (inputMethodManager.isActive(this) && inputMethodManager.isAcceptingText()) {
                        inputMethodManager.hideSoftInputFromWindow(this.mActivity.getCurrentFocus().getWindowToken(), 2);
                    } else {
                        closeActivityView();
                    }
                }
                return true;
            default:
                super.onKeyDown(i, keyEvent);
                return true;
        }
    }

    private void closeActivityView() {
        ActivityController currentActivityController = Session.getSession().getCurrentActivityController();
        if (currentActivityController != null) {
            currentActivityController.dismiss();
        }
    }

    /* JADX INFO: compiled from: ProGuard */
    private class MyWebChromeClient extends WebChromeClient {
        private MyWebChromeClient() {
        }

        @Override // android.webkit.WebChromeClient
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Logger.debug(WebView.TAG, String.format("line : %s  message %s", Integer.valueOf(consoleMessage.lineNumber()), consoleMessage.message()));
            return true;
        }

        @Override // android.webkit.WebChromeClient
        public void onProgressChanged(android.webkit.WebView webView, int i) {
            WebView.this.mActivity.getWindow().setFeatureInt(2, i * 100);
        }

        @Override // android.webkit.WebChromeClient
        public void onGeolocationPermissionsShowPrompt(String str, GeolocationPermissions.Callback callback) {
            callback.invoke(str, true, false);
        }
    }

    @Override // android.webkit.WebView, android.view.View
    protected void onVisibilityChanged(View view, int i) {
        if (i != 4 && i != 8) {
            if (i == 0) {
                requestLayout();
                requestFocus();
                addOrientationListener();
                return;
            }
            return;
        }
        removeOrientationListener();
    }

    public void executeJavascript(final String str) {
        this.mActivity.runOnUiThread(new Runnable() { // from class: com.sessionm.ui.WebView.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    WebView.this.loadUrl("javascript:" + str);
                } catch (NullPointerException e) {
                    Logger.error(WebView.TAG, "failed to execute javascript: " + str);
                }
            }
        });
    }

    private void removeOrientationListener() {
        if (this.mOrientationListener != null) {
            ((SensorManager) getContext().getSystemService("sensor")).unregisterListener(this.mOrientationListener);
            this.mOrientationListener = null;
            Logger.debug(TAG, "removed orientation listener");
        }
    }

    private void addOrientationListener() {
        if (this.mOrientationListener == null) {
            SensorManager sensorManager = (SensorManager) getContext().getSystemService("sensor");
            List<Sensor> sensorList = sensorManager.getSensorList(3);
            if (sensorList.size() == 0) {
                Logger.debug(TAG, "no orientation sensors");
                return;
            }
            Sensor sensor = sensorList.get(0);
            this.mOrientationListener = new SensorEventListener() { // from class: com.sessionm.ui.WebView.3
                @Override // android.hardware.SensorEventListener
                public void onAccuracyChanged(Sensor sensor2, int i) {
                }

                @Override // android.hardware.SensorEventListener
                public void onSensorChanged(SensorEvent sensorEvent) {
                    int i;
                    if (sensorEvent.sensor.getType() == 3) {
                        float f = sensorEvent.values[1];
                        float f2 = sensorEvent.values[2];
                        if (f < -45.0f && f > -135.0f) {
                            i = 0;
                        } else if (f > 45.0f && f < 135.0f) {
                            i = Opcodes.GETFIELD;
                        } else if (f2 > 45.0f) {
                            i = -90;
                        } else {
                            i = f2 < -45.0f ? 90 : 0;
                        }
                        if (WebView.lastOrientationDegrees != i) {
                            WebView.lastOrientationDegrees = i;
                            if (WebView.this.isShown()) {
                                Logger.info(WebView.TAG, String.format("orientation changed to %d", Integer.valueOf(i)));
                                WebView.this.executeJavascript(String.format("if(GreyhoundEventDispatcher){GreyhoundEventDispatcher.dispatch('orientationChanged',{'orientation':'%d'});};", Integer.valueOf(i)));
                            }
                        }
                    }
                }
            };
            sensorManager.registerListener(this.mOrientationListener, sensor, 3);
            Logger.info(TAG, "added orientation listener");
        }
    }
}
