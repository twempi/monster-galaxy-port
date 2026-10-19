package com.sessionm.ui;

import android.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import com.chartboost.sdk.CBUtility;
import com.millennialmedia.android.MMAdView;
import com.millennialmedia.android.MMAdViewSDK;
import com.sessionm.a.b;
import com.sessionm.a.c;
import com.sessionm.a.e;
import com.sessionm.api.ActivityListener;
import com.sessionm.api.SessionM;
import com.sessionm.core.Session;
import com.sessionm.core.SessionMAndroidConfig;
import com.sessionm.cpi.ODIN;
import com.sessionm.cpi.PackageReceiver;
import com.sessionm.json.JSONObject;
import com.sessionm.logging.Logger;
import com.sessionm.net.Request;
import com.sessionm.net.RequestListener;
import com.tapjoy.TapjoyConstants;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.HttpResponseException;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import twitter4j.internal.http.HttpResponseCode;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public class ActivityController {
    private static final String TAG = "ActivityController";
    private static int controllerCounter;
    private static final Map<String, Integer> xPosFlagMap = new HashMap();
    private static final Map<String, Integer> yPosFlagMap = new HashMap();
    private final Activity activityContext;
    private JSONObject activityData;
    private SessionM.ActivityType activityType;
    private final int controllerId;
    private WindowManager.LayoutParams defaultLayoutParams;
    private FrameLayout frameLayout;
    private LoadWebViewListener loadWebViewListener;
    private Dialog loadingProgressDlg;
    private String[] orientations;
    private SessionMActivity presentedActivity;
    private Session session;
    private State state = State.DISMISSED;
    private WebView view;

    /* JADX INFO: compiled from: ProGuard */
    interface LoadContentListener {
        void onContentLoaded(String str, String str2);

        void onFailure(Throwable th);
    }

    /* JADX INFO: compiled from: ProGuard */
    interface LoadWebViewListener {
        void onFailure(Throwable th);

        void onWebViewLoaded();
    }

    /* JADX INFO: compiled from: ProGuard */
    interface PrepareListener {
        void onFailure(Throwable th);

        void onPrepared();
    }

    /* JADX INFO: compiled from: ProGuard */
    enum State {
        DISMISSED,
        LOADING_CONTENT,
        LOADED_CONTENT,
        LOADING_WEBVIEW,
        LOADED_WEBVIEW,
        PRESENTED
    }

    static {
        xPosFlagMap.put("left", 3);
        xPosFlagMap.put("right", 5);
        xPosFlagMap.put("center", 1);
        xPosFlagMap.put("middle", 1);
        yPosFlagMap.put("bottom", 80);
        yPosFlagMap.put("top", 48);
        yPosFlagMap.put("center", 16);
        yPosFlagMap.put("middle", 16);
        controllerCounter = 0;
    }

    public ActivityController(Activity activity) {
        int i = controllerCounter + 1;
        controllerCounter = i;
        this.controllerId = i;
        this.activityContext = activity;
        this.session = Session.getSession();
        this.defaultLayoutParams = new WindowManager.LayoutParams(256, Opcodes.ACC_DEPRECATED, 32, -3, 1000, 32, 1);
        initWebView();
    }

    private void initWebView() {
        this.view = new WebView(this.activityContext);
        this.frameLayout = this.view.getLayout();
        this.frameLayout.setBackgroundColor(0);
        this.activityContext.getWindow().setSoftInputMode(32);
        this.frameLayout.setLayoutParams(this.defaultLayoutParams);
        this.frameLayout.setScrollBarStyle(33554432);
        this.view.getSettings().setCacheMode(1);
        this.view.setWebViewClient(new ActivityWebViewClient());
        this.view.addJavascriptInterface(this, "Android");
        this.view.setFocusable(true);
        this.view.requestFocusFromTouch();
        this.frameLayout.setClickable(false);
        this.frameLayout.setFocusableInTouchMode(false);
        this.frameLayout.setAddStatesFromChildren(true);
        this.frameLayout.setFocusable(false);
        this.frameLayout.setEnabled(true);
    }

    int getControllerId() {
        return this.controllerId;
    }

    public Activity getActivityContext() {
        return this.activityContext;
    }

    public SessionM.ActivityType getActivityType() {
        return this.activityType;
    }

    State getState() {
        return this.state;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setState(State state) {
        if (this.state != State.PRESENTED || state == State.DISMISSED) {
            this.state = state;
        }
    }

    String[] getOrientations() {
        return this.session.isPortalOrientationLocked() ? new String[]{"portrait"} : this.orientations;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setOrientations(String[] strArr) {
        this.orientations = strArr;
    }

    private FrameLayout.LayoutParams getFullscreenLayoutParams() {
        Rect rect = new Rect();
        Window window = this.activityContext.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        int top = window.findViewById(R.id.content).getTop();
        int i = top > 0 ? top - rect.top : 0;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        layoutParams.gravity = 17;
        Logger.debug(TAG, "getFullscreenLayoutParams() r: " + rect + ", contentViewTop: " + top + ", titleBarOffset: " + i + ", height: " + (rect.height() - i));
        return layoutParams;
    }

    void setPresentedActivity(SessionMActivity sessionMActivity) {
        this.presentedActivity = sessionMActivity;
    }

    void loadContent(Request.Type type, String str, final LoadContentListener loadContentListener) {
        Logger.debug(TAG, "Loadding content, url: " + str);
        setState(State.LOADING_CONTENT);
        Request request = new Request(str, type, getActivityData());
        request.setListener(new RequestListener() { // from class: com.sessionm.ui.ActivityController.1
            @Override // com.sessionm.net.RequestListener
            public void onRequestSent(Request request2) {
            }

            @Override // com.sessionm.net.RequestListener
            public void onReplyReceived(final Request request2) {
                if (request2.getError() == null) {
                    ActivityController.this.activityContext.runOnUiThread(new Runnable() { // from class: com.sessionm.ui.ActivityController.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (ActivityController.this.getState() != State.DISMISSED) {
                                ActivityController.this.setState(State.LOADED_CONTENT);
                                if (loadContentListener == null) {
                                    ActivityController.this.loadWebView(request2.getUrl(), request2.getReply(), null);
                                } else {
                                    loadContentListener.onContentLoaded(request2.getUrl(), request2.getReply());
                                }
                            }
                        }
                    });
                    return;
                }
                Logger.error(ActivityController.TAG, "Failed to load activity content: " + request2.getError().getMessage());
                if (loadContentListener != null) {
                    loadContentListener.onFailure(request2.getError());
                } else {
                    ActivityController.this.dismiss();
                }
            }
        });
        request.send();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadWebView(final String str, final String str2, LoadWebViewListener loadWebViewListener) {
        this.loadWebViewListener = loadWebViewListener;
        this.activityContext.runOnUiThread(new Runnable() { // from class: com.sessionm.ui.ActivityController.2
            @Override // java.lang.Runnable
            public void run() {
                if (ActivityController.this.getState() != State.DISMISSED) {
                    ActivityController.this.setState(State.LOADING_WEBVIEW);
                    ActivityController.this.view.loadDataWithBaseURL(str, str2, "text/html", "utf-8", "http://about:blank");
                }
            }
        });
    }

    FrameLayout getFrameLayout() {
        return this.frameLayout;
    }

    android.webkit.WebView getWebView() {
        return this.view;
    }

    public void notifyApplicationInstalled(String str, String str2) {
        executeJavascript(String.format("GreyhoundEventDispatcher.dispatch('packageInstalled',{'name':'%s','referrer':'%s'});", str, str2));
    }

    void displayView() {
        executeJavascript(String.format("GreyhoundEventDispatcher.dispatch('load',%s);", getActivityData().toString()));
        this.view.setVisibility(0);
        boolean z = getState() != State.PRESENTED;
        setState(State.PRESENTED);
        if (z) {
            SessionM sessionM = SessionM.getInstance();
            ActivityListener activityListener = SessionM.getInstance().getActivityListener();
            if (activityListener != null) {
                try {
                    activityListener.onPresented(sessionM);
                } catch (Throwable th) {
                    Logger.error(TAG, "Exception calling ActivityListener.onPresented(): " + th.getMessage());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void launchActivity(final String str) {
        this.activityType = SessionM.ActivityType.PORTAL;
        this.activityContext.runOnUiThread(new Runnable() { // from class: com.sessionm.ui.ActivityController.3
            @Override // java.lang.Runnable
            public void run() {
                ActivityController.this.session.notifyActivityStarted(ActivityController.this);
                Intent intent = new Intent(ActivityController.this.activityContext, (Class<?>) SessionMActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(SessionMActivity.INTENT_PARAM_URL, str);
                bundle.putInt(SessionMActivity.INTENT_PARAM_CONTROLLER_ID, ActivityController.this.controllerId);
                intent.putExtras(bundle);
                intent.addFlags(268435456);
                ActivityController.this.activityContext.startActivity(intent);
            }
        });
    }

    public void presentPortal(String str) {
        launchActivity(String.format("%s/%s", SessionMAndroidConfig.getInstance().getConfigurationByKey(b.g), (str == null || str.trim().length() == 0) ? String.format("apps/%s/portal", this.session.getApiKey()) : str.trim()));
    }

    public void presentActivity(final JSONObject jSONObject) {
        this.activityType = SessionM.ActivityType.ACHIEVEMENT;
        this.activityContext.runOnUiThread(new Runnable() { // from class: com.sessionm.ui.ActivityController.4
            @Override // java.lang.Runnable
            public void run() {
                ActivityController.this.session.notifyActivityStarted(ActivityController.this);
                ActivityController.this.updateActivityData("data", jSONObject);
                ActivityController.this.updateActivityData("submitEvent", true);
                ActivityController.this.loadContent(Request.Type.CONTENT, jSONObject.getString("preload_url"), null);
            }
        });
    }

    public void presentActivity(final String str) {
        this.activityType = SessionM.ActivityType.ACHIEVEMENT;
        this.activityContext.runOnUiThread(new Runnable() { // from class: com.sessionm.ui.ActivityController.5
            @Override // java.lang.Runnable
            public void run() {
                ActivityController.this.session.notifyActivityStarted(ActivityController.this);
                if (str != null) {
                    ActivityController.this.updateActivityData("ad[event]", str);
                }
                ActivityController.this.loadContent(Request.Type.BUG, null, null);
            }
        });
    }

    public void presentIntroduction() {
        this.activityType = SessionM.ActivityType.PORTAL;
        this.activityContext.runOnUiThread(new Runnable() { // from class: com.sessionm.ui.ActivityController.6
            @Override // java.lang.Runnable
            public void run() {
                ActivityController.this.session.notifyActivityStarted(ActivityController.this);
                ActivityController.this.updateActivityData("ad[type]", "intro");
                ActivityController.this.loadContent(Request.Type.BUG, null, null);
            }
        });
    }

    public void dismiss() {
        this.activityContext.runOnUiThread(new Runnable() { // from class: com.sessionm.ui.ActivityController.7
            @Override // java.lang.Runnable
            public void run() {
                SessionM sessionM;
                ActivityListener activityListener;
                if (ActivityController.this.view != null) {
                    ActivityController.this.dismissLoader();
                    Session.getSession().getRequestQueue().getHttpClient().storeCookies();
                    if (ActivityController.this.presentedActivity != null) {
                        if (!ActivityController.this.presentedActivity.isFinishing()) {
                            ActivityController.this.presentedActivity.finish();
                        }
                    } else {
                        if (ActivityController.this.frameLayout != null) {
                            ViewGroup viewGroup = (ViewGroup) ActivityController.this.frameLayout.getParent();
                            if (viewGroup != null) {
                                viewGroup.removeView(ActivityController.this.frameLayout);
                            } else {
                                Logger.debug(ActivityController.TAG, "container has no parent?");
                            }
                        }
                        InputMethodManager inputMethodManager = (InputMethodManager) ActivityController.this.activityContext.getSystemService("input_method");
                        View currentFocus = ActivityController.this.activityContext.getCurrentFocus();
                        if (currentFocus != null) {
                            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 2);
                        }
                    }
                    ActivityController.this.view.destroy();
                    ActivityController.this.view = null;
                    ActivityController.this.frameLayout = null;
                    ActivityController.this.session.notifyActivityFinished(ActivityController.this);
                    boolean z = ActivityController.this.getState() == State.PRESENTED;
                    ActivityController.this.setState(State.DISMISSED);
                    if (z && (activityListener = (sessionM = SessionM.getInstance()).getActivityListener()) != null) {
                        try {
                            activityListener.onDismissed(sessionM);
                        } catch (Throwable th) {
                            Logger.error(ActivityController.TAG, "Exception calling ActivityListener.onDismissed(): " + th.getMessage());
                        }
                    }
                }
            }
        });
    }

    private void prepare(String str, final PrepareListener prepareListener) {
        loadContent(Request.Type.CONTENT, str, new LoadContentListener() { // from class: com.sessionm.ui.ActivityController.8
            @Override // com.sessionm.ui.ActivityController.LoadContentListener
            public void onContentLoaded(String str2, String str3) {
                ActivityController.this.loadWebView(str2, str3, new LoadWebViewListener() { // from class: com.sessionm.ui.ActivityController.8.1
                    @Override // com.sessionm.ui.ActivityController.LoadWebViewListener
                    public void onWebViewLoaded() {
                        prepareListener.onPrepared();
                    }

                    @Override // com.sessionm.ui.ActivityController.LoadWebViewListener
                    public void onFailure(Throwable th) {
                        prepareListener.onFailure(th);
                    }
                });
            }

            @Override // com.sessionm.ui.ActivityController.LoadContentListener
            public void onFailure(Throwable th) {
                prepareListener.onFailure(th);
            }
        });
    }

    void executeJavascript(final String str) {
        this.activityContext.runOnUiThread(new Runnable() { // from class: com.sessionm.ui.ActivityController.9
            @Override // java.lang.Runnable
            public void run() {
                if (ActivityController.this.getState() != State.DISMISSED) {
                    ActivityController.this.view.executeJavascript(str);
                }
            }
        });
    }

    private JSONObject getActivityData() {
        if (this.activityData == null) {
            this.activityData = JSONObject.create();
            if (this.session.getSessionData() != null) {
                this.activityData.put("global_data", this.session.getSessionData());
            }
            if (this.session.getMetaData() != null) {
                for (Map.Entry<String, String> entry : this.session.getMetaData().entrySet()) {
                    this.activityData.put("user[data][" + entry.getKey() + "]", entry.getValue());
                }
            }
            this.activityData.put("z", "" + System.currentTimeMillis());
            this.activityData.put(MMAdView.KEY_ORIENTATION, WebView.lastOrientationDegrees);
        }
        return this.activityData;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateActivityData(String str, Object obj) {
        getActivityData().put(str, obj);
    }

    void presentLoader() {
        this.activityContext.runOnUiThread(new Runnable() { // from class: com.sessionm.ui.ActivityController.10
            @Override // java.lang.Runnable
            public void run() {
                try {
                    if (ActivityController.this.loadingProgressDlg == null || !ActivityController.this.loadingProgressDlg.isShowing()) {
                        ActivityController.this.loadingProgressDlg = new Dialog(ActivityController.this.activityContext, R.style.Theme.Panel);
                        ProgressBar progressBar = new ProgressBar(ActivityController.this.activityContext);
                        progressBar.setInterpolator(new LinearInterpolator());
                        progressBar.setIndeterminate(true);
                        new FrameLayout(ActivityController.this.activityContext).setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
                        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(100, 100);
                        layoutParams.gravity = 17;
                        layoutParams.height = 100;
                        layoutParams.width = 100;
                        progressBar.setLayoutParams(layoutParams);
                        ActivityController.this.loadingProgressDlg.setContentView(progressBar);
                        ActivityController.this.loadingProgressDlg.getWindow().addFlags(1026);
                        ActivityController.this.loadingProgressDlg.getWindow().getAttributes().dimAmount = 0.6f;
                        ActivityController.this.loadingProgressDlg.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.sessionm.ui.ActivityController.10.1
                            @Override // android.content.DialogInterface.OnCancelListener
                            public void onCancel(DialogInterface dialogInterface) {
                                ActivityController.this.dismiss();
                            }
                        });
                        ActivityController.this.loadingProgressDlg.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void dismissLoader() {
        this.activityContext.runOnUiThread(new Runnable() { // from class: com.sessionm.ui.ActivityController.11
            @Override // java.lang.Runnable
            public void run() {
                try {
                    if (ActivityController.this.loadingProgressDlg != null && ActivityController.this.loadingProgressDlg.isShowing() && ActivityController.this.loadingProgressDlg.getWindow() != null) {
                        ActivityController.this.loadingProgressDlg.dismiss();
                    }
                    ActivityController.this.loadingProgressDlg = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void bridgeAction(final String str) {
        this.activityContext.runOnUiThread(new Runnable() { // from class: com.sessionm.ui.ActivityController.12
            @Override // java.lang.Runnable
            public void run() {
                Method jSHandlerMethod;
                boolean z = true;
                if (ActivityController.this.getState() != State.DISMISSED) {
                    try {
                        JSONObject jSONObjectCreate = JSONObject.create(str);
                        if (jSONObjectCreate == null) {
                            Logger.error(ActivityController.TAG, "Unable to parse JS JSON message: " + str);
                            return;
                        }
                        String string = jSONObjectCreate.getString("handler");
                        if (string == null) {
                            Logger.error(ActivityController.TAG, "JS command handler not specified");
                            return;
                        }
                        String str2 = "handle" + string.substring(0, 1).toUpperCase() + string.substring(1) + "Message";
                        Method jSHandlerMethod2 = ActivityController.this.getJSHandlerMethod(str2);
                        if (jSHandlerMethod2 == null) {
                            jSHandlerMethod = ActivityController.this.getJSHandlerMethod(str2 + "Async");
                        } else {
                            z = false;
                            jSHandlerMethod = jSHandlerMethod2;
                        }
                        if (jSHandlerMethod == null) {
                            Logger.error(ActivityController.TAG, "JS command handler method not found: " + string);
                            return;
                        }
                        jSHandlerMethod.setAccessible(true);
                        jSHandlerMethod.invoke(ActivityController.this, jSONObjectCreate);
                        if (!z) {
                            ActivityController.this.finishMessageHandling(jSONObjectCreate, null);
                        }
                    } catch (Throwable th) {
                        Logger.error(ActivityController.TAG, "Exception processing JS bridge message: " + th.getMessage());
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Method getJSHandlerMethod(String str) {
        for (Method method : ActivityController.class.getDeclaredMethods()) {
            if (method.getName().equals(str)) {
                return method;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishMessageHandling(JSONObject jSONObject, JSONObject jSONObject2) {
        JSONObject jSONObject3 = jSONObject.getJSONObject("callback");
        if (jSONObject3 != null) {
            if (jSONObject2 == null) {
                jSONObject2 = JSONObject.create();
            }
            jSONObject2.put("_id", jSONObject3.getString("_id"));
            executeJavascript(String.format("%s(%s);", jSONObject3.getString("name"), jSONObject2.toString()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUpdateForecastMessage(JSONObject jSONObject) {
        this.session.updateSessionData(jSONObject);
    }

    private void handleCloseMessage(JSONObject jSONObject) {
        dismiss();
    }

    private void handleOpenMessageAsync(final JSONObject jSONObject) {
        String string = jSONObject.getString(SessionMActivity.INTENT_PARAM_URL);
        if (string == null) {
            dismiss();
        } else {
            final ActivityController activityController = new ActivityController(this.activityContext);
            activityController.prepare(string, new PrepareListener() { // from class: com.sessionm.ui.ActivityController.13
                @Override // com.sessionm.ui.ActivityController.PrepareListener
                public void onPrepared() {
                    if (ActivityController.this.getState() != State.DISMISSED) {
                        ActivityController.this.finishMessageHandling(jSONObject, null);
                        if (!jSONObject.getBoolean("child")) {
                            ActivityController.this.dismiss();
                        }
                        String[] strArr = (String[]) jSONObject.getArray(MMAdView.KEY_ORIENTATION, String.class);
                        if (strArr != null) {
                            activityController.setOrientations(strArr);
                        }
                        activityController.launchActivity(null);
                    }
                }

                @Override // com.sessionm.ui.ActivityController.PrepareListener
                public void onFailure(Throwable th) {
                    ActivityController.this.dismiss();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleListenForInstallsMessage(JSONObject jSONObject) {
        this.activityContext.registerReceiver(new PackageReceiver(jSONObject), e.b());
    }

    private void handleBrowserMessage(JSONObject jSONObject) {
        String string = jSONObject.getString("action");
        String string2 = jSONObject.getString(SessionMActivity.INTENT_PARAM_URL);
        if (string.equalsIgnoreCase("open") && string2 != null && string2.length() > 0) {
            Intent intent = new Intent(this.activityContext, (Class<?>) SessionMActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(SessionMActivity.INTENT_PARAM_URL, string2);
            bundle.putString(SessionMActivity.INTENT_PARAM_TYPE, "inlineBrowser");
            intent.putExtras(bundle);
            intent.addFlags(268435456);
            this.activityContext.startActivity(intent);
        }
    }

    private void handleGetSystemIdentifierMessage(JSONObject jSONObject) {
        String str;
        NoSuchAlgorithmException noSuchAlgorithmException;
        String str2;
        String string;
        String string2 = "androidId";
        String string3 = "none";
        try {
            string2 = jSONObject.getString(SessionMActivity.INTENT_PARAM_TYPE);
            string3 = jSONObject.getString("algorithm");
            if (string2.equalsIgnoreCase("androidimei")) {
                string = ((TelephonyManager) this.activityContext.getSystemService("phone")).getDeviceId();
            } else if (string2.equalsIgnoreCase("hashedIdentifier")) {
                string = new c(this.activityContext).a().toString();
            } else if (string2.equalsIgnoreCase("odin1")) {
                string = ODIN.getODIN1(this.activityContext);
            } else if (!string2.equalsIgnoreCase("androiddeviceid") && string2.equalsIgnoreCase("hashedandroidimei")) {
                String deviceId = ((TelephonyManager) this.activityContext.getSystemService("phone")).getDeviceId();
                try {
                    string = String.valueOf(MessageDigest.getInstance(string3).digest(deviceId.getBytes()));
                } catch (SecurityException e) {
                    str = deviceId;
                    Logger.info(TAG, "the permission android.permission.READ_PHONE_STATE has not been defined in the manifest. Can't grab device identifier...");
                    str2 = "permissionDenied";
                } catch (NoSuchAlgorithmException e2) {
                    noSuchAlgorithmException = e2;
                    str = deviceId;
                    str2 = "noSuchAlgorithm";
                    noSuchAlgorithmException.printStackTrace();
                }
            } else {
                string = Settings.Secure.getString(this.activityContext.getContentResolver(), TapjoyConstants.TJC_ANDROID_ID);
            }
            if (string3.length() == 0 && !string3.equalsIgnoreCase("none")) {
                string = String.valueOf(MessageDigest.getInstance(string3).digest(string.getBytes()));
            }
            str = string;
            str2 = "";
        } catch (SecurityException e3) {
            str = "unavailable";
        } catch (NoSuchAlgorithmException e4) {
            str = "unavailable";
            noSuchAlgorithmException = e4;
        }
        JSONObject jSONObjectCreate = JSONObject.create();
        jSONObjectCreate.put("identifier", str);
        jSONObjectCreate.put(SessionMActivity.INTENT_PARAM_TYPE, string2);
        jSONObjectCreate.put("algorithm", string3);
        if (str2.length() > 0) {
            jSONObjectCreate.put("errors", str2);
        }
        executeJavascript(String.format("GreyhoundEventDispatcher.dispatch('deviceIdentifierCallback', %s);", jSONObjectCreate.toString()));
    }

    private void handleCheckInstalledMessage(JSONObject jSONObject) {
        if (e.b(this.activityContext, jSONObject.getString("packageName"))) {
            executeJavascript(String.format("GreyhoundEventDispatcher.dispatch('packageInstalled',{'installed':true});", new Object[0]));
        } else {
            executeJavascript(String.format("GreyhoundEventDispatcher.dispatch('packageInstalled',{'installed':false});", new Object[0]));
        }
    }

    private void handleMakeRequestMessageAsync(final JSONObject jSONObject) {
        Request request = new Request(jSONObject.getString(SessionMActivity.INTENT_PARAM_URL), jSONObject.getString("method"), jSONObject.getJSONObject("params"));
        request.setListener(new RequestListener() { // from class: com.sessionm.ui.ActivityController.14
            @Override // com.sessionm.net.RequestListener
            public void onRequestSent(Request request2) {
            }

            @Override // com.sessionm.net.RequestListener
            public void onReplyReceived(Request request2) {
                int statusCode;
                JSONObject jSONObjectCreate;
                if (request2.getError() == null) {
                    String reply = request2.getReply();
                    if (reply == null || reply.trim().length() == 0) {
                        jSONObjectCreate = JSONObject.create();
                    } else {
                        jSONObjectCreate = JSONObject.create(reply);
                        if (jSONObjectCreate == null) {
                            Logger.error(ActivityController.TAG, "Unable to parse JSON 'make request message' response: " + reply);
                            return;
                        }
                    }
                    if (jSONObjectCreate.has("ad_forecast")) {
                        ActivityController.this.handleUpdateForecastMessage(jSONObjectCreate);
                    }
                    if (jSONObjectCreate.has("cpc_info")) {
                        ActivityController.this.handleListenForInstallsMessage(jSONObjectCreate);
                    }
                    ActivityController.this.finishMessageHandling(jSONObject, jSONObjectCreate);
                    return;
                }
                Throwable error = request2.getError();
                if (!(error instanceof HttpResponseException)) {
                    statusCode = -1;
                } else {
                    statusCode = ((HttpResponseException) error).getStatusCode();
                }
                String str = CBUtility.AUID_STATIC_ERROR;
                switch (statusCode) {
                    case 204:
                        str = "no connection";
                        break;
                    case 408:
                        str = "no response";
                        break;
                    case HttpResponseCode.INTERNAL_SERVER_ERROR /* 500 */:
                        str = "server error";
                        break;
                }
                ActivityController.this.executeJavascript(String.format("GreyhoundEventDispatcher.dispatch('server.error', {message: '%s',status:%d});", str, Integer.valueOf(statusCode)));
            }
        });
        request.send();
    }

    private void handleDisplayMessage(JSONObject jSONObject) {
        FrameLayout.LayoutParams fullscreenLayoutParams;
        String string = jSONObject.getString("vertical_alignment");
        String string2 = jSONObject.getString("horizontal_alignment");
        if (string == null || string.equalsIgnoreCase("")) {
            string = "center";
        }
        if (string2 == null || string2.equalsIgnoreCase("")) {
            string2 = "center";
        }
        String string3 = jSONObject.getString("display_type");
        int i = jSONObject.getInt("horizontal_offset");
        int i2 = jSONObject.getInt("vertical_offset");
        int iIntValue = xPosFlagMap.get(string2).intValue();
        int iIntValue2 = yPosFlagMap.get(string).intValue();
        int iA = e.a(this.activityContext, jSONObject.getInt("width"));
        int iA2 = e.a(this.activityContext, jSONObject.getInt("height"));
        if (string3 != null && string3.equalsIgnoreCase("frame")) {
            fullscreenLayoutParams = new FrameLayout.LayoutParams(iA, iA2);
            if (iIntValue2 == 48) {
                fullscreenLayoutParams.topMargin = e.a(this.activityContext, i2);
            } else if (iIntValue2 == 80) {
                fullscreenLayoutParams.bottomMargin = e.a(this.activityContext, i2);
            }
            if (iIntValue == 3) {
                fullscreenLayoutParams.leftMargin = e.a(this.activityContext, i);
            } else if (iIntValue == 5) {
                fullscreenLayoutParams.rightMargin = e.a(this.activityContext, i);
            }
            fullscreenLayoutParams.gravity = iIntValue2 | iIntValue;
        } else {
            fullscreenLayoutParams = getFullscreenLayoutParams();
        }
        this.frameLayout.setLayoutParams(fullscreenLayoutParams);
        if (this.frameLayout.getParent() == null) {
            this.activityContext.addContentView(this.frameLayout, fullscreenLayoutParams);
        }
        this.activityContext.getWindow().setSoftInputMode(32);
        executeJavascript(String.format("GreyhoundEventDispatcher.dispatch('show', %s);", getActivityData().toString()));
    }

    private void handlePlayMessage(JSONObject jSONObject) {
        String string = jSONObject.getString(SessionMActivity.INTENT_PARAM_URL);
        Logger.info(TAG, "play video: " + string);
        Uri uri = Uri.parse(string);
        RelativeLayout relativeLayout = new RelativeLayout(this.activityContext);
        VideoView videoView = new VideoView(this.activityContext);
        videoView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        relativeLayout.setLayoutParams(new WindowManager.LayoutParams(-1));
        relativeLayout.setBackgroundColor(0);
        relativeLayout.addView(videoView, new WindowManager.LayoutParams(-1));
        final Dialog dialog = new Dialog(this.activityContext);
        dialog.setContentView(relativeLayout, new WindowManager.LayoutParams(-1));
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.setZOrderMediaOverlay(true);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.sessionm.ui.ActivityController.15
            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer mediaPlayer) {
                ActivityController.this.executeJavascript("GreyhoundEventDispatcher.dispatch('videoisdone',{});");
                dialog.dismiss();
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() { // from class: com.sessionm.ui.ActivityController.16
            @Override // android.media.MediaPlayer.OnErrorListener
            public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                ActivityController.this.executeJavascript("GreyhoundEventDispatcher.dispatch('videoisdone',{});");
                dialog.dismiss();
                return false;
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.sessionm.ui.ActivityController.17
            @Override // android.media.MediaPlayer.OnPreparedListener
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        dialog.show();
    }

    /* JADX INFO: compiled from: ProGuard */
    private class ActivityWebViewClient extends WebViewClient {
        private ActivityWebViewClient() {
        }

        @Override // android.webkit.WebViewClient
        public void onPageStarted(android.webkit.WebView webView, String str, Bitmap bitmap) {
            ActivityController.this.activityContext.runOnUiThread(new Runnable() { // from class: com.sessionm.ui.ActivityController.ActivityWebViewClient.1
                @Override // java.lang.Runnable
                public void run() {
                    SessionM sessionM = SessionM.getInstance();
                    ActivityListener activityListener = sessionM.getActivityListener();
                    if (activityListener != null) {
                        activityListener.onStartLoading(sessionM);
                    }
                }
            });
        }

        @Override // android.webkit.WebViewClient
        public void onPageFinished(android.webkit.WebView webView, String str) {
            ActivityController.this.activityContext.runOnUiThread(new Runnable() { // from class: com.sessionm.ui.ActivityController.ActivityWebViewClient.2
                @Override // java.lang.Runnable
                public void run() {
                    if (ActivityController.this.getState() != State.DISMISSED) {
                        ActivityController.this.setState(State.LOADED_WEBVIEW);
                        CookieSyncManager.getInstance().sync();
                        ActivityController.this.dismissLoader();
                        SessionM sessionM = SessionM.getInstance();
                        ActivityListener activityListener = sessionM.getActivityListener();
                        if (activityListener != null) {
                            activityListener.onFinishLoading(sessionM);
                        }
                        if (ActivityController.this.loadWebViewListener != null) {
                            ActivityController.this.loadWebViewListener.onWebViewLoaded();
                            ActivityController.this.loadWebViewListener = null;
                        } else {
                            ActivityController.this.displayView();
                        }
                    }
                }
            });
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedSslError(android.webkit.WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            sslErrorHandler.proceed();
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedError(android.webkit.WebView webView, int i, String str, String str2) {
            Logger.debug(ActivityController.TAG, "Failed to load activity content, url: " + str2 + ", description: " + str + ", code: " + i);
            if (ActivityController.this.loadWebViewListener != null) {
                ActivityController.this.loadWebViewListener.onFailure(new Throwable(str));
                ActivityController.this.loadWebViewListener = null;
            } else {
                ActivityController.this.dismiss();
            }
        }

        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(android.webkit.WebView webView, String str) {
            Uri uri = Uri.parse(str);
            String scheme = uri.getScheme();
            if (scheme.equalsIgnoreCase("external")) {
                String str2 = String.format("http:%s", uri.getEncodedSchemeSpecificPart());
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setFlags(33554432);
                intent.setData(Uri.parse(str2));
                webView.getContext().startActivity(intent);
                return true;
            }
            if (!scheme.equalsIgnoreCase("sip") && !scheme.equalsIgnoreCase(MMAdViewSDK.Event.INTENT_MAPS) && !scheme.equalsIgnoreCase("content") && !scheme.equalsIgnoreCase(MMAdViewSDK.Event.INTENT_PHONE_CALL) && !scheme.equalsIgnoreCase(MMAdViewSDK.Event.INTENT_TXT_MESSAGE) && !scheme.equalsIgnoreCase(MMAdViewSDK.Event.INTENT_MARKET) && !scheme.equalsIgnoreCase("mailto")) {
                return false;
            }
            Intent intent2 = new Intent("android.intent.action.VIEW");
            intent2.setFlags(33554432);
            intent2.setData(Uri.parse(str));
            webView.getContext().startActivity(intent2);
            return true;
        }
    }
}
