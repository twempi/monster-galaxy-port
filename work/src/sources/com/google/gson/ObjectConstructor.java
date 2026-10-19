package com.google.gson;

import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
interface ObjectConstructor {
    <T> T construct(Type type);

    Object constructArray(Type type, int i);
}
