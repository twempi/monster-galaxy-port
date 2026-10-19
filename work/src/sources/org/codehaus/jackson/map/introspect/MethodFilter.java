package org.codehaus.jackson.map.introspect;

import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public interface MethodFilter {
    boolean includeMethod(Method method);
}
