package com.sessionm.net;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public interface RequestStore {
    void add(Request request);

    void insert(Request request, int i);

    Request nextRequest();

    void remove(Request request);
}
