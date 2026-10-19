package twitter4j.internal.logging;

import org.apache.commons.logging.LogFactory;

/* JADX INFO: loaded from: classes.dex */
final class CommonsLoggingLoggerFactory extends LoggerFactory {
    CommonsLoggingLoggerFactory() {
    }

    @Override // twitter4j.internal.logging.LoggerFactory
    public Logger getLogger(Class clazz) {
        return new CommonsLoggingLogger(LogFactory.getLog(clazz));
    }
}
