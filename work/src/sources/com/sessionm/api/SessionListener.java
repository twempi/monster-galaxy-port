package com.sessionm.api;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public interface SessionListener {
    public static final int INTERNAL_ERROR = 1;
    public static final int NETWORK_ERROR = 0;
    public static final int SESSION_REFUSED = 2;
    public static final int USER_INELIGIBLE = 3;

    void onSessionFailed(SessionM sessionM, int i);

    void onSessionStarted(SessionM sessionM);

    void onUserChangedSubscriptionStatus(SessionM sessionM, SessionM.USER_SUBSCRIPTION_STATUS user_subscription_status);

    void onUserUpdated(SessionM sessionM, User user);
}
