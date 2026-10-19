package twitter4j.internal.logging;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class SLF4JLogger extends Logger {
    private final org.slf4j.Logger LOGGER;

    SLF4JLogger(org.slf4j.Logger logger) {
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
        return this.LOGGER.isWarnEnabled();
    }

    @Override // twitter4j.internal.logging.Logger
    public void debug(String message) {
        this.LOGGER.debug(message);
    }

    @Override // twitter4j.internal.logging.Logger
    public void debug(String message, String message2) {
        this.LOGGER.debug(new StringBuffer().append(message).append(message2).toString());
    }

    @Override // twitter4j.internal.logging.Logger
    public void info(String message) {
        this.LOGGER.info(message);
    }

    @Override // twitter4j.internal.logging.Logger
    public void info(String message, String message2) {
        this.LOGGER.info(new StringBuffer().append(message).append(message2).toString());
    }

    @Override // twitter4j.internal.logging.Logger
    public void warn(String message) {
        this.LOGGER.warn(message);
    }

    @Override // twitter4j.internal.logging.Logger
    public void warn(String message, String message2) {
        this.LOGGER.warn(new StringBuffer().append(message).append(message2).toString());
    }

    @Override // twitter4j.internal.logging.Logger
    public void error(String message) {
        this.LOGGER.error(message);
    }

    @Override // twitter4j.internal.logging.Logger
    public void error(String message, Throwable th) {
        this.LOGGER.error(message, th);
    }
}
