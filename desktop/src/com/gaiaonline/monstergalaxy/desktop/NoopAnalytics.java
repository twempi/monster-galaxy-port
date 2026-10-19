package com.gaiaonline.monstergalaxy.desktop;

import com.gaiaonline.monstergalaxy.analytics.Analytics;

/** The analytics vendors (Flurry, Kontagent, SessionM) are dropped entirely. */
public class NoopAnalytics implements Analytics {
    public void sessionMLogEvent(String event) {
    }

    public void sessionMPauseActivity() {
    }

    public void sessionMPresentActivity() {
    }

    public void sessionMPresentPortal() {
    }

    public void sessionMResumeActivity() {
    }

    public void sessionMStartActivity() {
    }

    public void sessionMStopActivity() {
    }

    public void trackEvent(String a, String b, String c, String d, String e, String f) {
    }

    public void trackRevenue(String a, String b, int c) {
    }

    public void uploadPurchaseEvent(String a, double b) {
    }

    public void uploadRegistrationEvent(String a) {
    }
}
