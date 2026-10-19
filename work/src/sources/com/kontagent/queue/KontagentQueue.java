package com.kontagent.queue;

import java.util.Collection;

/* JADX INFO: loaded from: classes.dex */
public interface KontagentQueue {
    boolean enqueue(String str);

    boolean enqueue(Collection<String> collection);
}
