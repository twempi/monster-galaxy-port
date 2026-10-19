package com.sessionm.net;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface RequestListener {
    void onReplyReceived(Request request);

    void onRequestSent(Request request);
}
