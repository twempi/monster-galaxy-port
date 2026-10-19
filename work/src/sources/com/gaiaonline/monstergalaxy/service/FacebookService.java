package com.gaiaonline.monstergalaxy.service;

/* JADX INFO: loaded from: classes.dex */
public interface FacebookService {

    public interface OnCompletedHandler {
        void onCompleted(boolean z);
    }

    void authorize(OnCompletedHandler onCompletedHandler);

    String getFacebookId();

    boolean isSessionValid();

    void logout(OnCompletedHandler onCompletedHandler);

    void postMessage(OnCompletedHandler onCompletedHandler, String str);
}
