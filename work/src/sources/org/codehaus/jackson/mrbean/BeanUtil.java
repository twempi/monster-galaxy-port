package org.codehaus.jackson.mrbean;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class BeanUtil {
    protected static boolean isConcrete(Member member) {
        int mod = member.getModifiers();
        return (mod & 1536) == 0;
    }

    public static List<Class<?>> findSuperTypes(Class<?> cls, Class<?> endBefore) {
        return findSuperTypes(cls, endBefore, new ArrayList());
    }

    public static List<Class<?>> findSuperTypes(Class<?> cls, Class<?> endBefore, List<Class<?>> result) {
        _addSuperTypes(cls, endBefore, result, false);
        return result;
    }

    private static void _addSuperTypes(Class<?> cls, Class<?> endBefore, Collection<Class<?>> result, boolean addClassItself) {
        if (cls != endBefore && cls != null && cls != Object.class) {
            if (addClassItself) {
                if (!result.contains(cls)) {
                    result.add(cls);
                } else {
                    return;
                }
            }
            for (Class<?> intCls : cls.getInterfaces()) {
                _addSuperTypes(intCls, endBefore, result, true);
            }
            _addSuperTypes(cls.getSuperclass(), endBefore, result, true);
        }
    }
}
