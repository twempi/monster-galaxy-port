package com.flurry.android;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class af {
    private int b = 100;
    private LinkedHashMap a = new h(this, ((int) Math.ceil(100 / 0.75f)) + 1, 0.75f);

    af(int i) {
    }

    final synchronized Object a(Object obj) {
        return this.a.get(obj);
    }

    final synchronized void a(Object obj, Object obj2) {
        this.a.put(obj, obj2);
    }

    final synchronized int a() {
        return this.a.size();
    }

    final synchronized List b() {
        return new ArrayList(this.a.entrySet());
    }

    final synchronized Set c() {
        return this.a.keySet();
    }
}
