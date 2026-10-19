package twitter4j.internal.logging;

import org.apache.log4j.Level;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class Log4JLogger extends Logger {
    private final org.apache.log4j.Logger LOGGER;

    Log4JLogger(org.apache.log4j.Logger logger) {
        this.LOGGER = logger;
    }

    @Override // twitter4j.internal.logging.Logger
    public boolean isDebugEnabled() {
        return this.LOGGER.isDebugEnabled();
    }

    @Override // twitter4j.internal.logging.Logger
    public boolean isInfoEnabled() {
        return this.LOGGER.isInfoEnabled();
    }

    @Override // twitter4j.internal.logging.Logger
    public boolean isWarnEnabled() {
        return this.LOGGER.isEnabledFor(Level.WARN);
    }

    @Override // twitter4j.internal.logging.Logger
    public void debug(String message) {
        this.LOGGER.debug(message);
    }

    @Override // twitter4j.internal.logging.Logger
    public void debug(String message, String message2) {
        debug(new StringBuffer().append(message).append(message2).toString());
    }

    @Override // twitter4j.internal.logging.Logger
    public void info(String message) {
        this.LOGGER.info(message);
    }

    @Override // twitter4j.internal.logging.Logger
    public void info(String message, String message2) {
        info(new StringBuffer().append(message).append(message2).toString());
    }

    @Override // twitter4j.internal.logging.Logger
    public void warn(String message) {
        this.LOGGER.warn(message);
    }

    @Override // twitter4j.internal.logging.Logger
    public void warn(String message, String message2) {
        warn(new StringBuffer().append(message).append(message2).toString());
    }

    @Override // twitter4j.internal.logging.Logger
    public void error(String message) {
        this.LOGGER.error(message);
    }

    @Override // twitter4j.internal.logging.Logger
    public void error(String message, Throwable th) {
        error(message, th);
    }
}
