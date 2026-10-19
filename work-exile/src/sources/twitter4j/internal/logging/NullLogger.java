package twitter4j.internal.logging;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class NullLogger extends Logger {
    NullLogger() {
    }

    @Override // twitter4j.internal.logging.Logger
    public boolean isDebugEnabled() {
        return false;
    }

    @Override // twitter4j.internal.logging.Logger
    public boolean isInfoEnabled() {
        return false;
    }

    @Override // twitter4j.internal.logging.Logger
    public boolean isWarnEnabled() {
        return false;
    }

    @Override // twitter4j.internal.logging.Logger
    public void debug(String message) {
    }

    @Override // twitter4j.internal.logging.Logger
    public void debug(String message, String message2) {
    }

    @Override // twitter4j.internal.logging.Logger
    public void info(String message) {
    }

    @Override // twitter4j.internal.logging.Logger
    public void info(String message, String message2) {
    }

    @Override // twitter4j.internal.logging.Logger
    public void warn(String message) {
    }

    @Override // twitter4j.internal.logging.Logger
    public void warn(String message, String message2) {
    }

    @Override // twitter4j.internal.logging.Logger
    public void error(String message) {
    }

    @Override // twitter4j.internal.logging.Logger
    public void error(String message, Throwable th) {
    }
}
