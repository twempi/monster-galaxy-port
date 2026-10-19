package twitter4j.internal.async;

import java.lang.reflect.InvocationTargetException;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;

/* JADX INFO: loaded from: classes.dex */
public final class DispatcherFactory {
    static Class class$twitter4j$conf$Configuration;
    private Configuration conf;
    private String dispatcherImpl;

    public DispatcherFactory(Configuration conf) {
        this.dispatcherImpl = conf.getDispatcherImpl();
        this.conf = conf;
    }

    public DispatcherFactory() {
        this(ConfigurationContext.getInstance());
    }

    public Dispatcher getInstance() throws Throwable {
        Class<?> clsClass$;
        try {
            Class<?> cls = Class.forName(this.dispatcherImpl);
            Class<?>[] clsArr = new Class[1];
            if (class$twitter4j$conf$Configuration == null) {
                clsClass$ = class$("twitter4j.conf.Configuration");
                class$twitter4j$conf$Configuration = clsClass$;
            } else {
                clsClass$ = class$twitter4j$conf$Configuration;
            }
            clsArr[0] = clsClass$;
            return (Dispatcher) cls.getConstructor(clsArr).newInstance(this.conf);
        } catch (ClassCastException e) {
            throw new AssertionError(e);
        } catch (ClassNotFoundException e2) {
            throw new AssertionError(e2);
        } catch (IllegalAccessException e3) {
            throw new AssertionError(e3);
        } catch (InstantiationException e4) {
            throw new AssertionError(e4);
        } catch (NoSuchMethodException e5) {
            throw new AssertionError(e5);
        } catch (InvocationTargetException e6) {
            throw new AssertionError(e6);
        }
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }
}
