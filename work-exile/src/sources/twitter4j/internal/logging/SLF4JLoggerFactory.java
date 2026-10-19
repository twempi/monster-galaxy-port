package twitter4j.internal.logging;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class SLF4JLoggerFactory extends LoggerFactory {
    SLF4JLoggerFactory() {
    }

    @Override // twitter4j.internal.logging.LoggerFactory
    public Logger getLogger(Class clazz) {
        return new SLF4JLogger(org.slf4j.LoggerFactory.getLogger(clazz));
    }
}
