package twitter4j.internal.logging;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class StdOutLoggerFactory extends LoggerFactory {
    private static final Logger SINGLETON = new StdOutLogger();

    StdOutLoggerFactory() {
    }

    @Override // twitter4j.internal.logging.LoggerFactory
    public Logger getLogger(Class clazz) {
        return SINGLETON;
    }
}
