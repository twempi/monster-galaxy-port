package com.sessionm.api;

import org.json.JSONObject;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface ActivityListener {
    void onCancelled(SessionM sessionM);

    void onDismissed(SessionM sessionM);

    void onFinishLoading(SessionM sessionM);

    void onPresented(SessionM sessionM);

    void onStartLoading(SessionM sessionM);

    void onUserInfoChanged(SessionM sessionM, JSONObject jSONObject);
}
