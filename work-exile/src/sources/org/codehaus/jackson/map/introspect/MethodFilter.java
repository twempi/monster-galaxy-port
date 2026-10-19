package org.codehaus.jackson.map.introspect;

import java.lang.reflect.Method;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface MethodFilter {
    boolean includeMethod(Method method);
}
