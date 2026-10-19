package com.gaiaonline.monstergalaxy.service;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface FacebookService {

    public interface OnCompletedHandler {
        void onCompleted(boolean z);
    }

    void authorize(OnCompletedHandler onCompletedHandler);

    boolean isSessionValid();

    void logout(OnCompletedHandler onCompletedHandler);

    void postMessage(OnCompletedHandler onCompletedHandler, String str);
}
