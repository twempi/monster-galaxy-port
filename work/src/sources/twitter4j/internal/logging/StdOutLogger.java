package twitter4j.internal.logging;

import java.util.Date;
import twitter4j.conf.ConfigurationContext;

/* JADX INFO: loaded from: classes.dex */
final class StdOutLogger extends Logger {
    private static final boolean DEBUG = ConfigurationContext.getInstance().isDebugEnabled();

    StdOutLogger() {
    }

    @Override // twitter4j.internal.logging.Logger
    public boolean isDebugEnabled() {
        return DEBUG;
    }

    @Override // twitter4j.internal.logging.Logger
    public boolean isInfoEnabled() {
        return true;
    }

    @Override // twitter4j.internal.logging.Logger
    public boolean isWarnEnabled() {
        return true;
    }

    @Override // twitter4j.internal.logging.Logger
    public void debug(String message) {
        if (DEBUG) {
            System.out.println(new StringBuffer().append("[").append(new Date()).append("]").append(message).toString());
        }
    }

    @Override // twitter4j.internal.logging.Logger
    public void debug(String message, String message2) {
        if (DEBUG) {
            debug(new StringBuffer().append(message).append(message2).toString());
        }
    }

    @Override // twitter4j.internal.logging.Logger
    public void info(String message) {
        System.out.println(new StringBuffer().append("[").append(new Date()).append("]").append(message).toString());
    }

    @Override // twitter4j.internal.logging.Logger
    public void info(String message, String message2) {
        info(new StringBuffer().append(message).append(message2).toString());
    }

    @Override // twitter4j.internal.logging.Logger
    public void warn(String message) {
        System.out.println(new StringBuffer().append("[").append(new Date()).append("]").append(message).toString());
    }

    @Override // twitter4j.internal.logging.Logger
    public void warn(String message, String message2) {
        warn(new StringBuffer().append(message).append(message2).toString());
    }

    @Override // twitter4j.internal.logging.Logger
    public void error(String message) {
        System.out.println(new StringBuffer().append("[").append(new Date()).append("]").append(message).toString());
    }

    @Override // twitter4j.internal.logging.Logger
    public void error(String message, Throwable th) {
        System.out.println(message);
        th.printStackTrace(System.err);
    }
}
