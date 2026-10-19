package com.sessionm.api;

import android.app.Activity;
import android.content.Context;
import com.sessionm.core.Session;
import com.sessionm.core.SessionMAndroidConfig;
import java.util.Map;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public class SessionM {
    protected static SessionM instance;
    public static USER_SUBSCRIPTION_STATUS userOptOutState = USER_SUBSCRIPTION_STATUS.UNKNOWN;
    private ActivityListener activityListener;
    private SessionListener sessionListener;

    /* JADX INFO: compiled from: ProGuard */
    public enum ActivityType {
        PORTAL,
        INTRODUCTION,
        ACHIEVEMENT
    }

    /* JADX INFO: compiled from: ProGuard */
    public enum USER_SUBSCRIPTION_STATUS {
        UNKNOWN,
        OPTED_OUT,
        OPTED_IN
    }

    protected SessionM() {
        instance = this;
    }

    public static synchronized SessionM getInstance() {
        if (instance == null) {
            instance = new SessionM();
        }
        return instance;
    }

    public Config getConfig() {
        return SessionMAndroidConfig.getInstance();
    }

    public SessionListener getSessionListener() {
        return this.sessionListener;
    }

    public void setSessionListener(SessionListener sessionListener) {
        this.sessionListener = sessionListener;
    }

    public ActivityListener getActivityListener() {
        return this.activityListener;
    }

    public void setActivityListener(ActivityListener activityListener) {
        this.activityListener = activityListener;
    }

    public synchronized void startSession(Context context, String str) {
        try {
            if (context == null) {
                throw new NullPointerException("Could not initialize SessionM SDK due to null application context");
            }
            if (str == null || str.length() == 0) {
                throw new NullPointerException("Could not initialize SessionM SDK due to null or empty API key");
            }
            SessionMAndroidConfig.getInstance();
            Session.getSession().startSession(context, str);
        } catch (Throwable th) {
            throw th;
        }
    }

    public void onActivityStart(Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Activity is null");
        }
        Session.getSession().onActivityStart(activity);
    }

    public void onActivityResume(Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Activity is null");
        }
        Session.getSession().startSession();
        Session.getSession().onActivityResume(activity);
    }

    public void onActivityPause(Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Activity is null");
        }
        Session.getSession().onActivityPause(activity);
    }

    public void onActivityStop(Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Activity is null");
        }
        Session.getSession().onActivityStop(activity);
    }

    public synchronized void presentActivity(ActivityType activityType, Object... objArr) {
        String str = null;
        synchronized (this) {
            Session session = Session.getSession();
            if (!session.isDisabled() && session.getSessionState() == Session.State.STARTED && session.getCurrentActivityController() == null) {
                if (session.getVisibleActivity() == null) {
                    throw new IllegalStateException("Visible activity is null. Ensure that activity extends com.sessionm.api.BaseActivity or calls SessionM.onActivity lifecycle methods.");
                }
                if (activityType == ActivityType.PORTAL) {
                    if (objArr != null && objArr.length > 0 && (objArr[0] instanceof String)) {
                        str = (String) objArr[0];
                    }
                    session.presentPortal(null, str);
                } else if (activityType == ActivityType.INTRODUCTION) {
                    session.presentIntroduction(null);
                } else if (activityType == ActivityType.ACHIEVEMENT) {
                    if (objArr != null && objArr.length > 0 && (objArr[0] instanceof String)) {
                        str = (String) objArr[0];
                    }
                    session.presentActivity(null, str);
                }
            }
        }
    }

    public synchronized void logAction(String str) {
        Session.getSession().logAction(str, null);
    }

    public synchronized void setMetaData(Map<String, String> map) {
        Session.getSession().setMetaData(map);
    }

    public User getUser() {
        return Session.getSession().getUser();
    }

    public synchronized void setUserOptOutState(Context context, USER_SUBSCRIPTION_STATUS user_subscription_status) {
        User user = getUser();
        if (user != null) {
            if (user_subscription_status == USER_SUBSCRIPTION_STATUS.OPTED_IN) {
                user.setOptedOut(false);
            } else {
                user.setOptedOut(true);
            }
        }
    }

    public synchronized USER_SUBSCRIPTION_STATUS getUserOptOutState(Context context) {
        USER_SUBSCRIPTION_STATUS user_subscription_status;
        User user = getUser();
        if (user == null) {
            user_subscription_status = USER_SUBSCRIPTION_STATUS.UNKNOWN;
        } else {
            userOptOutState = user.isOptedOut() ? USER_SUBSCRIPTION_STATUS.OPTED_OUT : USER_SUBSCRIPTION_STATUS.OPTED_IN;
            user_subscription_status = userOptOutState;
        }
        return user_subscription_status;
    }

    public synchronized void wakeUp(Context context) {
        Session.getSession().startSession();
    }

    public synchronized void presentIntroduction(Context context) {
        try {
            if (context == null) {
                throw new NullPointerException("Context is null");
            }
            if (!(context instanceof Activity)) {
                throw new IllegalArgumentException("Activity as context is expected");
            }
            Session.getSession().presentIntroduction((Activity) context);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void presentActivity(Context context, String str) {
        try {
            if (context == null) {
                throw new NullPointerException("Context is null");
            }
            if (!(context instanceof Activity)) {
                throw new IllegalArgumentException("Activity as context is expected");
            }
            Session.getSession().presentActivity((Activity) context, str);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void presentActivity(Context context) {
        presentActivity(context, (String) null);
    }

    public synchronized void presentPortal(Context context) {
        presentPortal(context, null);
    }

    public synchronized void presentPortal(Context context, String str) {
        try {
            if (context == null) {
                throw new NullPointerException("Context is null");
            }
            if (!(context instanceof Activity)) {
                throw new IllegalArgumentException("Activity as context is expected");
            }
            Session.getSession().presentPortal((Activity) context, str);
        } catch (Throwable th) {
            throw th;
        }
    }
}
