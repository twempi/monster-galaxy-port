package com.gaiaonline.monstergalaxy.analytics;

/* JADX INFO: loaded from: classes.dex */
public interface Analytics {
    public static final String CAPTURE_MOGA_SESSIONM_EVENT = "Capture Moga";
    public static final String PERFECT_ATTAKS_SESSIONM_EVENT = "Perfect Attack";

    void sessionMLogEvent(String str);

    void sessionMPauseActivity();

    void sessionMPresentActivity();

    void sessionMPresentPortal();

    void sessionMResumeActivity();

    void sessionMStartActivity();

    void sessionMStopActivity();

    void trackEvent(String str, String str2, String str3, String str4, String str5, String str6);

    void trackRevenue(String str, String str2, int i);

    void uploadPurchaseEvent(String str, double d);

    void uploadRegistrationEvent(String str);
}
