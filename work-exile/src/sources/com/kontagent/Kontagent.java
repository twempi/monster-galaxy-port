package com.kontagent;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.kontagent.util.GUIDUtil;
import com.kontagent.util.ListUtil;
import com.kontagent.util.MapUtil;
import com.kontagent.util.NetworkUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Kontagent {
    protected static final String APPLICATION_ADDED = "apa";
    protected static final String CUSTOM_EVENT = "evt";
    private static final long DEFAULT_HEARTBEAT_TIMEOUT = 120000;
    protected static final String FAKETEST_URL_PREFIX = "http://74.86.105.35/api/v1/";
    public static final String FEEDPUB = "feedpub";
    private static final String GOAL_COUNT = "gci";
    private static final String INVITE_RESPONSE = "inr";
    private static final String INVITE_SENT = "ins";
    private static final String MESSAGE_RECEIVED = "mer";
    private static final String MESSAGE_SENT = "mes";
    private static final String NOTIFICATION_EMAIL_RESPONSE = "nei";
    private static final String NOTIFICATION_EMAIL_SENT = "nes";
    private static final String PAGE_REQUEST = "pgr";
    public static final String PRODUCTION_MODE = "production";
    private static final String REVENUE_TRACKING = "mtu";
    private static final String STREAM_POST = "pst";
    private static final String STREAM_POST_RESPONSE = "psr";
    public static final String TEST_MODE = "test";
    protected static final String TEST_URL_PREFIX = "http://test-server.kontagent.net/api/v1/";
    private static final String UNDIRECTED_COMMUNICATION_CLICK = "ucc";
    protected static final String UNIT_TEST_URL_PREFIX = "http://test.com/";
    private static final String URL_PREFIX = "http://api.geo.kontagent.net/api/v1/";
    private static final String USER_INFORMATION = "cpu";
    private static String apiKey;
    private static Context context;
    private static Handler handler;
    private static KontagentPrefs prefs;
    private static QueueManager queueManager;
    public static String API_VERSION = "1.0.2-RC1";
    private static String senderId = null;
    private static boolean test = true;
    private static boolean debug = false;
    private static boolean experimental = false;
    private static final Runnable heartbeatRunnable = new Runnable() { // from class: com.kontagent.Kontagent.1
        @Override // java.lang.Runnable
        public void run() {
            if (Kontagent.isRunning()) {
                Kontagent.heartbeat();
                Kontagent.handler.postDelayed(this, Kontagent.DEFAULT_HEARTBEAT_TIMEOUT);
            }
        }
    };
    private static final KontagentStateful stateful = new KontagentStateful();

    private static class KontagentStateful extends Stateful {
        private KontagentStateful() {
        }

        @Override // com.kontagent.Stateful
        protected void onStart() {
            Kontagent.queueManager.start();
        }

        @Override // com.kontagent.Stateful
        protected void onResume() {
            Kontagent.queueManager.resume();
            Kontagent.startHeartbeatTimer();
        }

        @Override // com.kontagent.Stateful
        protected void onPause() {
            Kontagent.stopHeartbeatTimer();
            Kontagent.queueManager.pause();
        }

        @Override // com.kontagent.Stateful
        protected void onStop() {
            Kontagent.queueManager.stop();
            Kontagent.deinit();
        }
    }

    private static void init(String apiKey2, Context context2, QueueManager queueManager2, String mode, Integer senderId2) {
        apiKey = apiKey2;
        handler = new Handler(Looper.getMainLooper());
        context = context2.getApplicationContext();
        queueManager = queueManager2;
        prefs = new KontagentPrefs(context);
        setSenderId(senderId2);
        if (PRODUCTION_MODE.equals(mode)) {
            setTest(false);
        } else {
            setTest(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void deinit() {
        apiKey = null;
        context = null;
        handler = null;
        queueManager = null;
        prefs = null;
    }

    protected static synchronized void startSession(String apiKey2, Context context2, QueueManager queueManager2, String mode, Integer senderId2, boolean sendApplicationAdded) {
        try {
            KontagentLog.d(String.format("STARTING NEW SESSION: key=%s, mode=%s, sender=%s", apiKey2, mode, senderId2));
            if (stateful.isStarted()) {
                KontagentLog.e("Kontagent session was already started. Force-stopping it before starting session again.", null);
                stateful.stop();
            }
            init(apiKey2, context2, queueManager2, mode, senderId2);
            stateful.start();
            if (sendApplicationAdded && isFirstRun()) {
                onFirstRun();
            }
        } catch (Exception e) {
            KontagentLog.e("Failed to start session.", e);
        }
    }

    public static synchronized void startSession(String apiKey2, Context context2, String mode, Integer senderId2, boolean sendApplicationAdded) {
        startSession(apiKey2, context2, new QueueManager(context2), mode, senderId2, sendApplicationAdded);
    }

    public static synchronized void startSession(String apiKey2, Context context2, String mode, Integer senderId2) {
        startSession(apiKey2, context2, mode, senderId2, true);
    }

    public static synchronized void startSession(String apiKey2, Context context2, String mode) {
        startSession(apiKey2, context2, mode, (Integer) null);
    }

    public static synchronized void startSession(String apiKey2, Context context2, String mode, boolean sendApplicationAdded) {
        startSession(apiKey2, context2, mode, null, sendApplicationAdded);
    }

    public static synchronized void resumeSession() {
        stateful.resume();
    }

    public static synchronized void pauseSession() {
        stateful.pause();
    }

    public static synchronized void stopSession() {
        stateful.stop();
    }

    public static synchronized boolean isStarted() {
        return stateful.isStarted();
    }

    public static synchronized boolean isRunning() {
        return stateful.isStarted() && !stateful.isPaused();
    }

    public static synchronized boolean isPaused() {
        return stateful.isPaused();
    }

    private static boolean assertStarted(String requestName) {
        if (isStarted()) {
            return true;
        }
        KontagentLog.e("Kontagent not started. Ignoring request: " + requestName, null);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void startHeartbeatTimer() {
        if (assertStarted("startHeartbeatTimer")) {
            handler.postDelayed(heartbeatRunnable, 0L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void stopHeartbeatTimer() {
        if (assertStarted("stopHeartbeatTimer")) {
            handler.removeCallbacks(heartbeatRunnable);
        }
    }

    public static void applicationAdded(Map<String, String> params) {
        if (assertStarted("applicationAdded")) {
            params.put("s", senderId);
            send(buildUrl(APPLICATION_ADDED, params));
        }
    }

    public static void applicationAdded() {
        if (assertStarted("applicationAdded")) {
            Map<String, String> params = new HashMap<>();
            applicationAdded(params);
        }
    }

    public static void customEvent(String name, Map<String, String> optionalParams) {
        if (assertStarted("customEvent")) {
            Map<String, String> params = processOptionalParams(optionalParams);
            params.put("s", senderId);
            params.put("n", name);
            send(buildUrl(CUSTOM_EVENT, params));
        }
    }

    public static void goalCount(Integer goalCountId, Integer value) {
        if (assertStarted("goalCount")) {
            Map<String, String> params = new HashMap<>();
            params.put("s", senderId);
            params.put("gc" + goalCountId.toString(), value.toString());
            send(buildUrl(GOAL_COUNT, params));
        }
    }

    public static void inviteSent(Integer recipientId, String trackingId, Map<String, String> optionalParams) {
        if (assertStarted("inviteSent")) {
            List<Integer> list = new ArrayList<>();
            list.add(recipientId);
            inviteSent(list, trackingId, optionalParams);
        }
    }

    protected static void inviteSent(List<Integer> recipientIds, String trackingId, Map<String, String> optionalParams) {
        if (assertStarted("inviteSent")) {
            Map<String, String> params = processOptionalParams(optionalParams);
            params.put("r", ListUtil.listToString(recipientIds));
            params.put("u", trackingId);
            params.put("s", senderId);
            send(buildUrl(INVITE_SENT, params));
        }
    }

    public static void inviteResponse(boolean applicationInstalled, String trackingId, Map<String, String> optionalParams) {
        if (assertStarted("inviteResponse")) {
            Map<String, String> params = processOptionalParams(optionalParams);
            params.put("i", applicationInstalled ? "1" : "0");
            params.put("u", trackingId);
            send(buildUrl(INVITE_RESPONSE, params));
        }
    }

    public static void messageSent(Integer recipientId, String trackingId, Map<String, String> optionalParams) {
        if (assertStarted("messageSent")) {
            messageSent(wrapAsList(recipientId), trackingId, optionalParams);
        }
    }

    protected static void messageSent(List<Integer> recipientIds, String trackingId, Map<String, String> optionalParams) {
        if (assertStarted("messageSent")) {
            Map<String, String> params = processOptionalParams(optionalParams);
            params.put("r", ListUtil.listToString(recipientIds));
            params.put("tu", "dashboard");
            params.put("u", trackingId);
            params.put("s", senderId);
            send(buildUrl(MESSAGE_SENT, params));
        }
    }

    public static void messageResponse(boolean applicationInstalled, String trackingId, Map<String, String> optionalParams) {
        if (assertStarted("messageResponse")) {
            Map<String, String> params = processOptionalParams(optionalParams);
            params.put("i", applicationInstalled ? "1" : "0");
            params.put("tu", "dashboard");
            params.put("u", trackingId);
            params.put("s", senderId);
            send(buildUrl(MESSAGE_RECEIVED, params));
        }
    }

    public static void pageRequest(Map<String, String> optionalParams) {
        if (assertStarted("pageRequest")) {
            Map<String, String> params = processOptionalParams(optionalParams);
            params.put("s", senderId);
            send(buildUrl(PAGE_REQUEST, params));
        }
    }

    public static void revenueTracking(Integer value, Map<String, String> optionalParams) {
        if (assertStarted("revenueTracking")) {
            Map<String, String> params = processOptionalParams(optionalParams);
            params.put("s", senderId);
            params.put("v", value.toString());
            send(buildUrl(REVENUE_TRACKING, params));
        }
    }

    public static void streamPost(String type, String trackingId, Map<String, String> optionalParams) {
        if (assertStarted("streamPost")) {
            Map<String, String> params = processOptionalParams(optionalParams);
            params.put("s", senderId);
            params.put("tu", type);
            params.put("u", trackingId);
            send(buildUrl(STREAM_POST, params));
        }
    }

    public static void streamPostResponse(boolean applicationInstalled, String type, String trackingId, Map<String, String> optionalParams) {
        if (assertStarted("streamPostResponse")) {
            Map<String, String> params = processOptionalParams(optionalParams);
            params.put("i", applicationInstalled ? "1" : "0");
            params.put("tu", type);
            params.put("u", trackingId);
            send(buildUrl(STREAM_POST_RESPONSE, params));
        }
    }

    public static void notificationEmailSent(Integer recipientId, String trackingId, Map<String, String> optionalParams) {
        if (assertStarted("notificationEmailSent")) {
            notificationEmailSent(wrapAsList(recipientId), trackingId, optionalParams);
        }
    }

    public static void notificationEmailSent(List<Integer> recipientIds, String trackingId, Map<String, String> optionalParams) {
        if (assertStarted("notificationEmailSent")) {
            Map<String, String> params = processOptionalParams(optionalParams);
            params.put("r", ListUtil.listToString(recipientIds));
            params.put("u", trackingId);
            params.put("s", senderId);
            send(buildUrl(NOTIFICATION_EMAIL_SENT, params));
        }
    }

    public static void notificationEmailResponse(boolean applicationInstalled, String trackingId, Map<String, String> optionalParams) {
        if (assertStarted("notificationEmailResponse")) {
            Map<String, String> params = processOptionalParams(optionalParams);
            params.put("i", applicationInstalled ? "1" : "0");
            params.put("u", trackingId);
            params.put("s", senderId);
            send(buildUrl(NOTIFICATION_EMAIL_RESPONSE, params));
        }
    }

    public static void undirectedCommunicationClick(boolean applicationInstalled, String type, Map<String, String> optionalParams) {
        if (assertStarted("undirectedCommunicationClick")) {
            Map<String, String> params = processOptionalParams(optionalParams);
            params.put("i", applicationInstalled ? "1" : "0");
            params.put("tu", type);
            send(buildUrl(UNDIRECTED_COMMUNICATION_CLICK, params));
        }
    }

    public static void userInformation(Map<String, String> optionalParams) {
        if (assertStarted("userInformation")) {
            Map<String, String> params = processOptionalParams(optionalParams);
            params.put("s", senderId);
            send(buildUrl(USER_INFORMATION, params));
        }
    }

    public static void sendDeviceInformation(Map<String, String> optionalParams) {
        if (assertStarted("sendDeviceInformation")) {
            Map<String, String> params = processOptionalParams(optionalParams);
            params.put("os", "android_" + Integer.toString(Build.VERSION.SDK_INT));
            params.put("m", Build.MANUFACTURER);
            params.put("d", Build.MODEL);
            if (isStarted() && NetworkUtil.isValidCarrierName(context)) {
                params.put("c", NetworkUtil.carrierName(context));
            }
            userInformation(params);
        }
    }

    public static void heartbeat() {
        if (assertStarted("heartbeat")) {
            KontagentLog.d("Heartbeat!");
            pageRequest(null);
        }
    }

    public static String generateUniqueTrackingTag() {
        return GUIDUtil.generateTrackingId();
    }

    public static String generateShortUniqueTrackingTag() {
        return GUIDUtil.generateShortTrackingId();
    }

    public static void enableDebug() {
        debug = true;
        KontagentLog.enable();
    }

    public static void enableExperimentalFeatures() {
        experimental = true;
    }

    public static void disableDebug() {
        debug = false;
    }

    public static Boolean debugEnabled() {
        return Boolean.valueOf(debug);
    }

    private static List<Integer> wrapAsList(Integer i) {
        List<Integer> list = new ArrayList<>();
        list.add(i);
        return list;
    }

    private static void setSenderId(Integer senderId2) {
        if (senderId2 != null) {
            senderId = senderId2.toString();
        }
        if (TextUtils.isEmpty(senderId)) {
            senderId = prefs.getSenderId();
            if (TextUtils.isEmpty(senderId)) {
                senderId = GUIDUtil.generateSenderId().toString();
                prefs.setSenderId(senderId);
                KontagentLog.d(" Generated Sender ID is " + senderId);
                return;
            }
            KontagentLog.d("Sender ID value: " + senderId);
        }
    }

    private static boolean isFirstRun() {
        boolean firstRun = prefs.isFirstRun();
        if (firstRun) {
            KontagentLog.d("First time application is being run.");
        }
        return firstRun;
    }

    private static void onFirstRun() {
        prefs.setFirstRun(false);
        KontagentLog.i("First sdk start detected.");
        String type = prefs.getReferrerEventType();
        String st1 = prefs.getReferrerEventSubtype1();
        String st2 = prefs.getReferrerEventSubtype2();
        String st3 = prefs.getReferrerEventSubtype3();
        KontagentLog.d("Kontagent referrer arguments: ");
        KontagentLog.d("\ttu=" + type);
        KontagentLog.d("\tst1=" + st1);
        KontagentLog.d("\tst2=" + st2);
        KontagentLog.d("\tst3==" + st3);
        if (type == null || st1 == null) {
            KontagentLog.i("No referrer arguments specified, sending \"apa\" event only.");
            applicationAdded();
            return;
        }
        KontagentLog.i("Referrer arguments were specified, sending \"apa\" and \"ucc\" events.");
        String trackingTag = generateShortUniqueTrackingTag();
        HashMap<String, String> uccParams = new HashMap<>();
        uccParams.put("su", trackingTag);
        if (st1 != null) {
            uccParams.put("st1", st1);
        }
        if (st1 != null && st2 != null) {
            uccParams.put("st2", st2);
        }
        if (st1 != null && st2 != null && st3 != null) {
            uccParams.put("st3", st3);
        }
        undirectedCommunicationClick(true, type, uccParams);
        HashMap<String, String> apaParams = new HashMap<>();
        apaParams.put("su", trackingTag);
        applicationAdded(apaParams);
    }

    private static synchronized void send(String url) {
        try {
            if (queueManager == null) {
                KontagentLog.e("Session not started. Dropping: " + url, null);
            } else {
                queueManager.enqueue(url);
            }
        } catch (Throwable e) {
            KontagentLog.e("Exception suppressed.", e);
        }
    }

    private static String buildUrl(String apiCall, Map<String, String> params) {
        Map<String, String> newParams = processOptionalParams(params);
        newParams.put("ts", Long.toString(System.currentTimeMillis()));
        StringBuffer params_string = new StringBuffer();
        if (newParams != null) {
            params_string.append(MapUtil.mapToString(newParams));
        }
        return urlPrefix() + apiKey + "/" + apiCall + "/?" + ((Object) params_string);
    }

    private static Map<String, String> processOptionalParams(Map<String, String> optionalParams) {
        return optionalParams != null ? new HashMap(optionalParams) : new HashMap();
    }

    private static String urlPrefix() {
        return isTest() ? TEST_URL_PREFIX : URL_PREFIX;
    }

    public static void setTest(boolean test2) {
        test = test2;
    }

    public static boolean isTest() {
        return test;
    }

    public static boolean isExperimental() {
        return experimental;
    }

    protected static QueueManager getQueueManager() {
        return queueManager;
    }

    protected static Stateful getStateful() {
        return stateful;
    }
}
