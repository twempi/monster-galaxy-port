package com.sessionm.net;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface RequestStore {
    void add(Request request);

    void insert(Request request, int i);

    Request nextRequest();

    void remove(Request request);
}
