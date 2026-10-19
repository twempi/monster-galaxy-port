package twitter4j.internal.logging;

/* JADX INFO: loaded from: classes.dex */
final class Log4JLoggerFactory extends LoggerFactory {
    Log4JLoggerFactory() {
    }

    @Override // twitter4j.internal.logging.LoggerFactory
    public Logger getLogger(Class clazz) {
        return new Log4JLogger(org.apache.log4j.Logger.getLogger(clazz));
    }
}
