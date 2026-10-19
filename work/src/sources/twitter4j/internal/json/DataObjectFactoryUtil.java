package twitter4j.internal.json;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public class DataObjectFactoryUtil {
    private static final Method CLEAR_THREAD_LOCAL_MAP;
    private static final Method REGISTER_JSON_OBJECT;
    static Class class$twitter4j$json$DataObjectFactory;

    private DataObjectFactoryUtil() {
        throw new AssertionError("not intended to be instantiated.");
    }

    static {
        Class clsClass$;
        if (class$twitter4j$json$DataObjectFactory == null) {
            clsClass$ = class$("twitter4j.json.DataObjectFactory");
            class$twitter4j$json$DataObjectFactory = clsClass$;
        } else {
            clsClass$ = class$twitter4j$json$DataObjectFactory;
        }
        Method[] methods = clsClass$.getDeclaredMethods();
        Method clearThreadLocalMap = null;
        Method registerJSONObject = null;
        for (Method method : methods) {
            if (method.getName().equals("clearThreadLocalMap")) {
                clearThreadLocalMap = method;
                clearThreadLocalMap.setAccessible(true);
            } else if (method.getName().equals("registerJSONObject")) {
                registerJSONObject = method;
                registerJSONObject.setAccessible(true);
            }
        }
        if (clearThreadLocalMap == null || registerJSONObject == null) {
            throw new AssertionError();
        }
        CLEAR_THREAD_LOCAL_MAP = clearThreadLocalMap;
        REGISTER_JSON_OBJECT = registerJSONObject;
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public static void clearThreadLocalMap() {
        try {
            CLEAR_THREAD_LOCAL_MAP.invoke(null, new Object[0]);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e2) {
            throw new AssertionError(e2);
        }
    }

    public static <T> T registerJSONObject(T t, Object obj) {
        try {
            return (T) REGISTER_JSON_OBJECT.invoke(null, t, obj);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e2) {
            throw new AssertionError(e2);
        }
    }
}
