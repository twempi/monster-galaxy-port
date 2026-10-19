package com.sessionm.net;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public interface RequestQueueListener {
    void onQueueConnected(RequestQueue requestQueue);

    void onQueueDisconnected(RequestQueue requestQueue);

    void onQueueStarted(RequestQueue requestQueue);

    void onQueueStopped(RequestQueue requestQueue);

    void onReplyReceived(RequestQueue requestQueue, Request request);

    void onRequestSent(RequestQueue requestQueue, Request request);
}
