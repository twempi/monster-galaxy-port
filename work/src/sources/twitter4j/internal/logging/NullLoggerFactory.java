package twitter4j.internal.logging;

/* JADX INFO: loaded from: classes.dex */
final class NullLoggerFactory extends LoggerFactory {
    private static final Logger SINGLETON = new NullLogger();

    NullLoggerFactory() {
    }

    @Override // twitter4j.internal.logging.LoggerFactory
    public Logger getLogger(Class clazz) {
        return SINGLETON;
    }
}
