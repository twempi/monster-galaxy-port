package com.sessionm.net;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface RequestQueueListener {
    void onQueueConnected(RequestQueue requestQueue);

    void onQueueDisconnected(RequestQueue requestQueue);

    void onQueueStarted(RequestQueue requestQueue);

    void onQueueStopped(RequestQueue requestQueue);

    void onReplyReceived(RequestQueue requestQueue, Request request);

    void onRequestSent(RequestQueue requestQueue, Request request);
}
