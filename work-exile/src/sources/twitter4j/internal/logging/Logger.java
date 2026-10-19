package twitter4j.internal.logging;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class Logger {
    private static final LoggerFactory LOGGER_FACTORY;
    private static final String LOGGER_FACTORY_IMPLEMENTATION = "twitter4j.loggerFactory";
    static Class class$twitter4j$internal$logging$Logger;

    public abstract void debug(String str);

    public abstract void debug(String str, String str2);

    public abstract void error(String str);

    public abstract void error(String str, Throwable th);

    public abstract void info(String str);

    public abstract void info(String str, String str2);

    public abstract boolean isDebugEnabled();

    public abstract boolean isInfoEnabled();

    public abstract boolean isWarnEnabled();

    public abstract void warn(String str);

    public abstract void warn(String str, String str2);

    static {
        Class clsClass$;
        LoggerFactory loggerFactory = null;
        try {
            String loggerFactoryImpl = System.getProperty(LOGGER_FACTORY_IMPLEMENTATION);
            if (loggerFactoryImpl != null) {
                loggerFactory = (LoggerFactory) Class.forName(loggerFactoryImpl).newInstance();
            }
        } catch (ClassNotFoundException e) {
        } catch (IllegalAccessException e2) {
        } catch (InstantiationException e3) {
            throw new AssertionError(e3);
        } catch (NoClassDefFoundError e4) {
        } catch (SecurityException e5) {
        }
        if (loggerFactory == null) {
            try {
                Class.forName("org.slf4j.impl.StaticLoggerBinder");
                loggerFactory = getLoggerFactory("org.slf4j.Logger", "twitter4j.internal.logging.SLF4JLoggerFactory");
            } catch (ClassNotFoundException e6) {
            }
        }
        if (loggerFactory == null) {
            loggerFactory = getLoggerFactory("org.apache.commons.logging.Log", "twitter4j.internal.logging.CommonsLoggingLoggerFactory");
        }
        if (loggerFactory == null) {
            loggerFactory = getLoggerFactory("org.apache.log4j.Logger", "twitter4j.internal.logging.Log4JLoggerFactory");
        }
        if (loggerFactory == null) {
            loggerFactory = new StdOutLoggerFactory();
        }
        LOGGER_FACTORY = loggerFactory;
        if (class$twitter4j$internal$logging$Logger == null) {
            clsClass$ = class$("twitter4j.internal.logging.Logger");
            class$twitter4j$internal$logging$Logger = clsClass$;
        } else {
            clsClass$ = class$twitter4j$internal$logging$Logger;
        }
        loggerFactory.getLogger(clsClass$).debug(new StringBuffer().append("Will use ").append(loggerFactory.getClass()).append(" as logging factory.").toString());
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    private static LoggerFactory getLoggerFactory(String checkClassName, String implementationClass) {
        try {
            Class.forName(checkClassName);
            LoggerFactory logger = (LoggerFactory) Class.forName(implementationClass).newInstance();
            return logger;
        } catch (ClassNotFoundException e) {
            return null;
        } catch (IllegalAccessException e2) {
            throw new AssertionError(e2);
        } catch (InstantiationException e3) {
            throw new AssertionError(e3);
        }
    }

    public static Logger getLogger(Class clazz) {
        return LOGGER_FACTORY.getLogger(clazz);
    }
}
