package twitter4j.management;

/* JADX INFO: loaded from: classes.dex */
public interface InvocationStatistics {
    long getAverageTime();

    long getCallCount();

    long getErrorCount();

    String getName();

    long getTotalTime();

    void reset();
}
