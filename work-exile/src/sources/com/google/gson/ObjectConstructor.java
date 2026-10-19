package com.google.gson;

import java.lang.reflect.Type;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
interface ObjectConstructor {
    <T> T construct(Type type);

    Object constructArray(Type type, int i);
}
