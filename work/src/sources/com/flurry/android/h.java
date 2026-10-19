package com.flurry.android;

import java.util.LinkedHashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
final class h extends LinkedHashMap {
    private /* synthetic */ af a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    h(af afVar, int i, float f) {
        super(i, f, true);
        this.a = afVar;
    }

    @Override // java.util.LinkedHashMap
    protected final boolean removeEldestEntry(Map.Entry entry) {
        return size() > this.a.b;
    }
}
