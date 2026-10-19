package com.sessionm.net;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public interface RequestListener {
    void onReplyReceived(Request request);

    void onRequestSent(Request request);
}
