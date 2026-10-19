package com.google.gson;

import java.lang.reflect.Type;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface InstanceCreator<T> {
    T createInstance(Type type);
}
