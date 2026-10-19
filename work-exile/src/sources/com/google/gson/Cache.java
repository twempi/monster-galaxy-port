package com.google.gson;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
interface Cache<K, V> {
    void addElement(K k, V v);

    V getElement(K k);

    V removeElement(K k);
}
