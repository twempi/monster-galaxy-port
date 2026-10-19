package com.kontagent.queue;

import java.util.Collection;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface KontagentQueue {
    boolean enqueue(String str);

    boolean enqueue(Collection<String> collection);
}
