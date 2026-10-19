package com.sessionm.core;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.webkit.CookieSyncManager;
import com.chartboost.sdk.CBUtility;
import com.sessionm.a.b;
import com.sessionm.a.e;
import com.sessionm.api.ActivityListener;
import com.sessionm.api.SessionListener;
import com.sessionm.api.SessionM;
import com.sessionm.api.User;
import com.sessionm.json.JSONObject;
import com.sessionm.logging.Logger;
import com.sessionm.net.Request;
import com.sessionm.net.RequestListener;
import com.sessionm.net.RequestQueue;
import com.sessionm.ui.ActivityController;
import com.tapjoy.TapjoyConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public class Session {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final int SESSION_TIMEOUT_INTERVAL = 600;
    private static final String TAG = "Session";
    private static Session sessionSingleton;
    private ActivityContentManager activityContentManager;
    private String apiKey;
    private Context appContext;
    private boolean isDisabled;
    private Map<String, String> metaData;
    private ExecutorService pendingActionExecutor;
    private RequestQueue requestQueue;
    private JSONObject sessionData;
    private String sessionId;
    private Timer sessionTimer;
    private boolean skipBlacklist;
    private SharedPreferences uuidFile;
    private Activity visibleActivity;
    private final List<ActivityController> activityControllers = new ArrayList();
    private String savedDeviceUUID = "";
    private boolean portalOrientationLocked = false;
    private State sessionState = State.STOPPED;
    private final ArrayList<Runnable> pendingActionRunnables = new ArrayList<>();
    private final Map<String, JSONObject> activityDataMap = new HashMap();
    private final ArrayList<Activity> startedActivities = new ArrayList<>();
    private User user = new UserImpl();

    /* JADX INFO: compiled from: ProGuard */
    public enum State {
        STOPPED,
        STARTING,
        STARTED
    }

    static {
        $assertionsDisabled = !Session.class.desiredAssertionStatus();
    }

    private Session() {
        this.isDisabled = false;
        if (Build.VERSION.SDK_INT < 8) {
            this.isDisabled = true;
        } else {
            this.requestQueue = new RequestQueue();
            this.activityContentManager = new ActivityContentManager();
        }
    }

    public static synchronized Session getSession() {
        Session session;
        if (sessionSingleton != null) {
            session = sessionSingleton;
        } else {
            sessionSingleton = new Session();
            session = sessionSingleton;
        }
        return session;
    }

    public boolean isDisabled() {
        return this.isDisabled;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public Context getApplicationContext() {
        return this.appContext;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setSessionState(State state) {
        this.sessionState = state;
    }

    public synchronized State getSessionState() {
        return this.sessionState;
    }

    public synchronized String getSessionId() {
        return this.sessionId;
    }

    public User getUser() {
        return this.user;
    }

    public synchronized boolean startSession(Context context, String str) {
        boolean zStartSession;
        if (getSessionState() != State.STOPPED || isDisabled()) {
            zStartSession = false;
        } else {
            this.appContext = context;
            this.apiKey = str;
            zStartSession = startSession();
        }
        return zStartSession;
    }

    public synchronized boolean startSession() {
        boolean z;
        if (getSessionState() != State.STOPPED || isDisabled() || this.appContext == null || this.apiKey == null) {
            z = false;
        } else {
            Logger.info(TAG, "Session starting");
            setSessionState(State.STARTING);
            e.b(this.appContext);
            e.a(this.appContext);
            CookieSyncManager.createInstance(this.appContext);
            this.requestQueue.getHttpClient().getCookieStore().clear();
            Request request = new Request(Request.Type.SESSION_START, new HashMap(getDeviceInfo()));
            this.requestQueue.addRequestListener(request, new InitSessionResponseHandler());
            this.requestQueue.enqueRequest(request);
            rescheduleSessionTimer();
            z = true;
        }
        return z;
    }

    void setPortalOrientationLocked(boolean z) {
        this.portalOrientationLocked = z;
    }

    public void setSkipBlacklist(boolean z) {
        this.skipBlacklist = z;
    }

    public boolean isPortalOrientationLocked() {
        return this.portalOrientationLocked;
    }

    public Map<String, String> getMetaData() {
        return this.metaData;
    }

    public void setMetaData(Map<String, String> map) {
        if (map != null) {
            this.metaData = new HashMap(map);
        } else {
            this.metaData = null;
        }
    }

    public JSONObject getSessionData() {
        return this.sessionData;
    }

    /* JADX INFO: compiled from: ProGuard */
    class SessionEndTimerTask extends TimerTask {
        SessionEndTimerTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            Session.this.stopSession();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void rescheduleSessionTimer() {
        if (getCurrentActivityController() == null && this.startedActivities.size() <= 0 && getSessionState() == State.STARTED) {
            cancelSessionTimer();
            this.sessionTimer = new Timer();
            this.sessionTimer.schedule(new SessionEndTimerTask(), 600000L);
            Logger.debug(TAG, "Started session timer, 600 seconds");
        }
    }

    private synchronized void cancelSessionTimer() {
        if (this.sessionTimer != null) {
            this.sessionTimer.cancel();
            this.sessionTimer = null;
        }
        Logger.debug(TAG, "Cancelled session timer");
    }

    public synchronized void stopSession() {
        if (getSessionState() == State.STARTED) {
            cancelSessionTimer();
            this.activityContentManager.cancelLoading();
            ActivityController currentActivityController = getCurrentActivityController();
            if (currentActivityController != null) {
                currentActivityController.dismiss();
            }
            HashMap map = new HashMap();
            map.put("_method", "delete");
            Request request = new Request(Request.Type.SESSION_END, map);
            this.requestQueue.addRequestListener(request, new RequestListener() { // from class: com.sessionm.core.Session.1
                @Override // com.sessionm.net.RequestListener
                public void onRequestSent(Request request2) {
                }

                @Override // com.sessionm.net.RequestListener
                public void onReplyReceived(Request request2) {
                    if (request2.getError() == null) {
                        Logger.debug(Session.TAG, "Session end reply received");
                    } else {
                        Logger.error(Session.TAG, request2.getError().getMessage());
                    }
                }
            });
            this.requestQueue.enqueRequest(request);
            this.sessionId = null;
            setSessionState(State.STOPPED);
            Logger.info(TAG, "Session stopped");
        }
    }

    public synchronized void onActivityStart(Activity activity) {
        if (!$assertionsDisabled && activity == null) {
            throw new AssertionError("Started activity is null");
        }
        this.startedActivities.add(activity);
        cancelSessionTimer();
    }

    public synchronized void onActivityResume(Activity activity) {
        if (!$assertionsDisabled && activity == null) {
            throw new AssertionError("Resumed activity is null");
        }
        ActivityController currentActivityController = getCurrentActivityController();
        if (currentActivityController != null && currentActivityController.getActivityContext() != activity) {
            currentActivityController.dismiss();
        }
        this.visibleActivity = activity;
    }

    public synchronized void onActivityPause(Activity activity) {
        if (!$assertionsDisabled && activity == null) {
            throw new AssertionError("Paused activity is null");
        }
        if (!$assertionsDisabled && activity != this.visibleActivity) {
            throw new AssertionError("Activity being paused is not visble");
        }
        ActivityController currentActivityController = getCurrentActivityController();
        if (currentActivityController != null && currentActivityController.getActivityContext() == activity && currentActivityController.getActivityType() != SessionM.ActivityType.PORTAL) {
            currentActivityController.dismiss();
        }
        if (activity == this.visibleActivity) {
            this.visibleActivity = null;
        }
    }

    public synchronized void onActivityStop(Activity activity) {
        if (!$assertionsDisabled && activity == null) {
            throw new AssertionError("Stopped activity is null");
        }
        this.startedActivities.remove(activity);
        rescheduleSessionTimer();
    }

    public synchronized Activity getVisibleActivity() {
        return this.visibleActivity;
    }

    public synchronized void presentActivity(Activity activity, String str) {
        if (getSessionState() == State.STARTED && getCurrentActivityController() == null && !this.user.isOptedOut()) {
            if (!$assertionsDisabled && activity == null && this.visibleActivity == null) {
                throw new AssertionError("Visible activity is unknown");
            }
            if (activity == null) {
                activity = this.visibleActivity;
            }
            if (getCurrentActivityController() == null) {
                JSONObject jSONObject = null;
                if (str != null) {
                    jSONObject = this.activityDataMap.get(str);
                }
                if (jSONObject != null) {
                    if (decrementActivityDistance(str) == 0) {
                        this.activityDataMap.remove(str);
                        new ActivityController(activity).presentActivity(jSONObject);
                    }
                } else {
                    new ActivityController(activity).presentActivity(str);
                }
            } else if (str != null) {
                logAction(str);
            }
        }
    }

    public synchronized void presentIntroduction(Activity activity) {
        if (getSessionState() == State.STARTED && getCurrentActivityController() == null && !this.user.isOptedOut()) {
            if (activity == null) {
                activity = this.visibleActivity;
            }
            new ActivityController(activity).presentIntroduction();
        }
    }

    public synchronized void presentPortal(Activity activity, String str) {
        if (getSessionState() == State.STARTED && !this.user.isOptedOut()) {
            if (!$assertionsDisabled && activity == null && this.visibleActivity == null) {
                throw new AssertionError("Visible activity is unknown");
            }
            if (activity == null) {
                activity = this.visibleActivity;
            }
            ActivityController currentActivityController = getCurrentActivityController();
            if (currentActivityController != null) {
                currentActivityController.dismiss();
            }
            new ActivityController(activity).presentPortal(str);
        }
    }

    public synchronized void dismissActivity() {
        ActivityController currentActivityController;
        if (getSessionState() == State.STARTED && (currentActivityController = getCurrentActivityController()) != null) {
            currentActivityController.dismiss();
        }
    }

    public synchronized void notifyActivityStarted(ActivityController activityController) {
        cancelSessionTimer();
        this.activityControllers.add(activityController);
    }

    public synchronized void notifyActivityFinished(ActivityController activityController) {
        this.activityControllers.remove(activityController);
        rescheduleSessionTimer();
    }

    public synchronized ActivityController getCurrentActivityController() {
        return this.activityControllers.size() > 0 ? this.activityControllers.get(this.activityControllers.size() - 1) : null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendActionCall(Map<String, String> map) {
        Request request = new Request(Request.Type.ACTION, map);
        this.requestQueue.addRequestListener(request, new RequestListener() { // from class: com.sessionm.core.Session.2
            @Override // com.sessionm.net.RequestListener
            public void onRequestSent(Request request2) {
            }

            @Override // com.sessionm.net.RequestListener
            public void onReplyReceived(Request request2) {
                String reply = request2.getReply();
                JSONObject jSONObjectCreate = JSONObject.create(reply);
                if (jSONObjectCreate != null) {
                    Session.this.updateSessionData(jSONObjectCreate);
                } else {
                    Logger.error(Session.TAG, "Unable to parse JSON response: " + reply);
                }
            }
        });
        this.requestQueue.enqueRequest(request);
    }

    public synchronized void logAction(String str) {
        logAction(str, null);
    }

    public synchronized void logAction(final String str, final Map<String, String> map) {
        if (!isDisabled() && str != null && str.length() != 0) {
            Runnable runnable = new Runnable() { // from class: com.sessionm.core.Session.3
                @Override // java.lang.Runnable
                public void run() {
                    Map map2 = map;
                    if (map2 == null) {
                        map2 = new HashMap();
                    }
                    Session.this.rescheduleSessionTimer();
                    Session.this.decrementActivityDistance(str);
                    map2.put("event", str);
                    Session.this.sendActionCall(map2);
                }
            };
            if (getSessionState() != State.STARTED) {
                if (this.pendingActionRunnables.size() < 10) {
                    this.pendingActionRunnables.add(runnable);
                }
            } else {
                runnable.run();
            }
        }
    }

    private synchronized void clearPendingActions() {
        this.pendingActionRunnables.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void sendPendingActions() {
        if (this.pendingActionRunnables.size() != 0) {
            if (this.pendingActionExecutor == null) {
                this.pendingActionExecutor = Executors.newCachedThreadPool();
            }
            ArrayList arrayList = new ArrayList(this.pendingActionRunnables);
            this.pendingActionRunnables.clear();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                this.pendingActionExecutor.execute((Runnable) it.next());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processInitializationResponse(JSONObject jSONObject) {
        String string = jSONObject.getString(b.K);
        if (string == null) {
            string = "error";
        }
        if (!string.equalsIgnoreCase(b.N)) {
            processSessionFailure(2);
            return;
        }
        JSONObject jSONObjectUpdateSessionData = updateSessionData(jSONObject);
        if (jSONObjectUpdateSessionData.has("eligible") && !jSONObjectUpdateSessionData.getBoolean("eligible")) {
            processSessionFailure(3);
            return;
        }
        this.sessionId = jSONObject.getString("id");
        this.portalOrientationLocked = jSONObject.getBoolean("portal_orientation_locked");
        if (this.savedDeviceUUID.length() == 0) {
            String string2 = jSONObject.getString(b.O);
            if (string2 != null) {
                this.savedDeviceUUID = string2;
            } else {
                this.savedDeviceUUID = "";
            }
            if (this.savedDeviceUUID.length() > 0) {
                saveDeviceUUID();
            }
        }
    }

    /* JADX INFO: compiled from: ProGuard */
    private class InitSessionResponseHandler implements RequestListener {
        private InitSessionResponseHandler() {
        }

        @Override // com.sessionm.net.RequestListener
        public void onReplyReceived(Request request) {
            Logger.debug(Session.TAG, "Session start success response");
            String reply = request.getReply();
            JSONObject jSONObjectCreate = JSONObject.create(reply);
            Throwable error = request.getError();
            if (error == null && jSONObjectCreate != null) {
                Session.this.processInitializationResponse(jSONObjectCreate);
                if (!Session.this.isDisabled) {
                    Session.this.requestQueue.start();
                    Session.this.setSessionState(State.STARTED);
                    SessionM sessionM = SessionM.getInstance();
                    SessionListener sessionListener = sessionM.getSessionListener();
                    if (sessionListener != null) {
                        try {
                            sessionListener.onSessionStarted(sessionM);
                        } catch (Throwable th) {
                            Logger.error(Session.TAG, "Exception calling SessionListener.onSessionStarted(): " + th.getMessage());
                        }
                    }
                    Session.this.sendPendingActions();
                    return;
                }
                return;
            }
            Logger.debug(Session.TAG, "Session start failure");
            if (jSONObjectCreate == null && error == null) {
                Logger.error(Session.TAG, "Unable to parse JSON response: " + reply);
                Session.this.processSessionFailure(1);
                return;
            }
            Logger.debug(Session.TAG, "Session start error, response: " + reply + ", error: " + error);
            if (request.statusCode() == 406) {
                Session.this.processSessionFailure(2);
            } else {
                Session.this.processSessionFailure(0);
            }
        }

        @Override // com.sessionm.net.RequestListener
        public void onRequestSent(Request request) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processSessionFailure(int i) {
        try {
            setSessionState(State.STOPPED);
            if (i == 3 || i == 2 || i == 1) {
                this.isDisabled = true;
                clearPendingActions();
            }
            SessionM sessionM = SessionM.getInstance();
            SessionListener sessionListener = sessionM.getSessionListener();
            if (sessionListener != null) {
                try {
                    sessionListener.onSessionFailed(sessionM, i);
                } catch (Throwable th) {
                    Logger.error(TAG, "Exception calling SessionListener.onSessionFailed(): " + th.getMessage());
                }
            }
        } catch (Throwable th2) {
            throw th2;
        }
    }

    private boolean deviceSupportsFlash() {
        try {
            return this.appContext.getPackageManager().getApplicationInfo("com.adobe.flashplayer", 0) != null;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private Map<String, String> getDeviceInfo() {
        HashMap map = new HashMap();
        retrieveSavedDeviceUUID();
        try {
            TelephonyManager telephonyManager = (TelephonyManager) this.appContext.getSystemService("phone");
            map.put("device[packagename]", this.appContext.getApplicationInfo().packageName);
            map.put("device[countrycode]", String.valueOf(this.appContext.getResources().getConfiguration().mcc));
            map.put("device[isocountrycode]", telephonyManager.getNetworkCountryIso());
            map.put("device[carriername]", telephonyManager.getNetworkOperatorName() == null ? CBUtility.AUID_STATIC_ERROR : telephonyManager.getNetworkOperatorName());
            map.put("device[networkoperator]", telephonyManager.getNetworkOperator() == null ? CBUtility.AUID_STATIC_ERROR : telephonyManager.getNetworkOperator());
        } catch (SecurityException e) {
            Logger.info(TAG, "the permission android.permission.READ_PHONE_STATE has not been defined in the manifest. Skipping network info...");
            map.put("device[packagename]", "permission_denied");
            map.put("device[countrycode]", "permission_denied");
            map.put("device[carriername]", "permission_denied");
            map.put("device[networkoperator]", "permission_denied");
        }
        map.put("device[connection]", getConnectionType());
        WindowManager windowManager = (WindowManager) this.appContext.getSystemService("window");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        String str = String.format("%sx%s", String.valueOf(displayMetrics.widthPixels), String.valueOf(displayMetrics.heightPixels));
        String str2 = (String) SessionMAndroidConfig.getInstance().getConfigurationByKey(b.J);
        if (this.savedDeviceUUID.length() > 0) {
            map.put("device[login]", this.savedDeviceUUID);
        }
        String displayName = TimeZone.getDefault().getDisplayName(true, 1);
        map.put("device[supportsflash]", String.valueOf(deviceSupportsFlash()));
        map.put("device[screen]", str);
        map.put("device[density]", String.valueOf(this.appContext.getResources().getDisplayMetrics().density));
        map.put("device[brand]", Build.BRAND);
        map.put("device[platform]", TapjoyConstants.TJC_DEVICE_PLATFORM_TYPE);
        map.put("device[model]", Build.MODEL);
        map.put("device[version]", Build.VERSION.INCREMENTAL);
        map.put("device[manufacturer]", Build.MANUFACTURER);
        map.put("device[buildId]", Build.DISPLAY);
        map.put("device[display_orientation]", getAppOrientation());
        map.put("device[platform_version]", "" + Build.VERSION.SDK_INT);
        map.put("device[sdkVersion]", str2);
        map.put("device[locale]", Locale.getDefault().toString());
        map.put("device[timezone]", displayName);
        if (this.skipBlacklist) {
            map.put("device[skip_blacklist]", "true");
        }
        String[] formattedLocationString = getFormattedLocationString();
        if (formattedLocationString != null && formattedLocationString.length > 1) {
            map.put("device[latitude]", formattedLocationString[0]);
            map.put("device[longitude]", formattedLocationString[1]);
        }
        return map;
    }

    private String getConnectionType() {
        String subtypeName;
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.appContext.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()) {
                if (activeNetworkInfo.getType() == 1) {
                    subtypeName = "wifi";
                } else if (activeNetworkInfo.getType() == 0) {
                    subtypeName = activeNetworkInfo.getSubtypeName();
                } else {
                    subtypeName = "connection_type_" + activeNetworkInfo.getType();
                }
            } else {
                subtypeName = CBUtility.AUID_STATIC_ERROR;
            }
            return subtypeName;
        } catch (SecurityException e) {
            Logger.info(TAG, "the permission android.permission.ACCESS_NETWORK_STATE has not been defined in the manifest. Skipping network type info...");
            return "";
        }
    }

    private String getAppOrientation() {
        String str = (String) SessionMAndroidConfig.getInstance().getConfigurationByKey(b.H);
        if (str == null) {
            switch (this.appContext.getResources().getConfiguration().orientation) {
                case 0:
                    return "dynamic";
                case 1:
                    return "portrait";
                case 2:
                    return "landscape";
                case 3:
                    return "square";
                default:
                    return CBUtility.AUID_STATIC_ERROR;
            }
        }
        return str;
    }

    private String[] getFormattedLocationString() {
        LocationManager locationManager = (LocationManager) this.appContext.getSystemService("location");
        List<String> allProviders = locationManager.getAllProviders();
        if (allProviders == null) {
            return null;
        }
        Iterator<String> it = allProviders.iterator();
        while (it.hasNext()) {
            String string = it.next().toString();
            try {
                Location lastKnownLocation = locationManager.getLastKnownLocation(string);
                if (lastKnownLocation != null) {
                    return new String[]{String.valueOf(lastKnownLocation.getLatitude()), String.valueOf(lastKnownLocation.getLongitude())};
                }
                continue;
            } catch (SecurityException e) {
                Logger.info(TAG, String.format("location settings for %s are not defined in the manifest...ignoring location", string));
            }
        }
        return null;
    }

    private void retrieveSavedDeviceUUID() {
        this.uuidFile = this.appContext.getSharedPreferences("deviceIDFile", 3);
        this.savedDeviceUUID = this.uuidFile.getString(b.O, "");
    }

    private void saveDeviceUUID() {
        if (this.savedDeviceUUID.length() > 0) {
            try {
                SharedPreferences.Editor editorEdit = this.uuidFile.edit();
                editorEdit.putString(b.O, this.savedDeviceUUID);
                editorEdit.commit();
            } catch (Exception e) {
                Logger.error(TAG, e.getMessage());
            }
        }
    }

    public synchronized JSONObject updateSessionData(JSONObject jSONObject) {
        JSONObject jSONObject2;
        boolean z = false;
        synchronized (this) {
            Logger.debug(TAG, jSONObject.toString());
            jSONObject2 = jSONObject.getJSONObject("player");
            if (jSONObject2 != null) {
                if (!jSONObject2.has("eligible") || jSONObject2.getBoolean("eligible")) {
                    this.user = new UserImpl(jSONObject2);
                    SessionM sessionM = SessionM.getInstance();
                    ActivityListener activityListener = sessionM.getActivityListener();
                    if (activityListener != null) {
                        try {
                            activityListener.onUserInfoChanged(sessionM, jSONObject2.toJSONObject());
                        } catch (Throwable th) {
                            Logger.error(TAG, "Exception calling ActivityListener.onUserInfoChanged(), message: " + th.getMessage());
                        }
                    }
                    SessionListener sessionListener = sessionM.getSessionListener();
                    if (sessionListener != null) {
                        try {
                            sessionListener.onUserUpdated(sessionM, this.user);
                        } catch (Throwable th2) {
                            Logger.error(TAG, "Exception calling SessionListener.onUserUpdated(), message: " + th2.getMessage());
                        }
                    }
                }
            }
            this.sessionData = jSONObject.getJSONObject("ad_forecast_global");
            JSONObject[] jSONObjectArr = (JSONObject[]) jSONObject.getArray("ad_forecast", JSONObject.class);
            if (jSONObjectArr != null) {
                String string = jSONObject.getString("update_strategy");
                if (string != null && !string.equals("update")) {
                    this.activityDataMap.clear();
                }
                for (JSONObject jSONObject3 : jSONObjectArr) {
                    String string2 = jSONObject3.getString("event_name") != null ? jSONObject3.getString("event_name") : jSONObject3.getString("ad_type");
                    this.activityDataMap.put(string2, jSONObject3);
                    Integer integer = jSONObject3.getInteger("distance");
                    if (integer != null && integer.intValue() <= 0 && !z) {
                        if (this.visibleActivity != null) {
                            presentActivity(this.visibleActivity, string2);
                        }
                        z = true;
                    }
                }
            }
            this.activityContentManager.loadContent(jSONObject);
        }
        return jSONObject2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int decrementActivityDistance(String str) {
        Integer num;
        JSONObject jSONObject = this.activityDataMap.get(str);
        if (jSONObject == null) {
            num = null;
        } else {
            Integer integer = jSONObject.getInteger("distance");
            if (integer != null) {
                integer = Integer.valueOf(integer.intValue() - 1);
                if (integer.intValue() >= 0) {
                    jSONObject.put("distance", integer);
                }
            }
            num = integer;
        }
        if (num == null) {
            return -1;
        }
        return num.intValue();
    }

    public RequestQueue getRequestQueue() {
        return this.requestQueue;
    }
}
