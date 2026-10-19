package com.google.gson;

/* JADX INFO: loaded from: classes.dex */
interface Cache<K, V> {
    void addElement(K k, V v);

    V getElement(K k);

    V removeElement(K k);
}
