package twitter4j.internal.http;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import twitter4j.internal.logging.Logger;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class HttpClientFactory {
    private static final Constructor HTTP_CLIENT_CONSTRUCTOR;
    static Class class$twitter4j$internal$http$HttpClientConfiguration;
    static Class class$twitter4j$internal$http$HttpClientFactory;
    private static final Logger logger;

    static {
        Class clsClass$;
        Class<?> clsClass$2;
        if (class$twitter4j$internal$http$HttpClientFactory == null) {
            clsClass$ = class$("twitter4j.internal.http.HttpClientFactory");
            class$twitter4j$internal$http$HttpClientFactory = clsClass$;
        } else {
            clsClass$ = class$twitter4j$internal$http$HttpClientFactory;
        }
        logger = Logger.getLogger(clsClass$);
        Class<?> cls = null;
        try {
            cls = Class.forName("twitter4j.internal.http.alternative.HttpClientImpl");
        } catch (ClassNotFoundException e) {
        }
        if (cls == null) {
            try {
                cls = Class.forName("twitter4j.internal.http.HttpClientImpl");
            } catch (ClassNotFoundException cnfe) {
                throw new AssertionError(cnfe);
            }
        }
        logger.debug(new StringBuffer().append("Will use ").append(cls.getName()).append(" as HttpClient implementation.").toString());
        try {
            Class<?>[] clsArr = new Class[1];
            if (class$twitter4j$internal$http$HttpClientConfiguration == null) {
                clsClass$2 = class$("twitter4j.internal.http.HttpClientConfiguration");
                class$twitter4j$internal$http$HttpClientConfiguration = clsClass$2;
            } else {
                clsClass$2 = class$twitter4j$internal$http$HttpClientConfiguration;
            }
            clsArr[0] = clsClass$2;
            HTTP_CLIENT_CONSTRUCTOR = cls.getConstructor(clsArr);
        } catch (NoSuchMethodException nsme) {
            throw new AssertionError(nsme);
        }
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public static HttpClient getInstance(HttpClientConfiguration conf) {
        try {
            return (HttpClient) HTTP_CLIENT_CONSTRUCTOR.newInstance(conf);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InstantiationException e2) {
            throw new AssertionError(e2);
        } catch (InvocationTargetException e3) {
            throw new AssertionError(e3);
        }
    }
}
