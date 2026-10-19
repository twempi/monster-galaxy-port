package com.sessionm.api.ext;

import com.sessionm.core.Session;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class SessionM extends com.sessionm.api.SessionM {
    private static SessionM instance;

    public static synchronized SessionM getInstance() {
        if (instance == null) {
            instance = new SessionM();
        }
        return instance;
    }

    public void setSkipBlacklist(boolean z) {
        Session.getSession().setSkipBlacklist(z);
    }

    public void stopSession() {
        Session.getSession().stopSession();
    }
}
