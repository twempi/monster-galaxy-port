package twitter4j.management;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface InvocationStatistics {
    long getAverageTime();

    long getCallCount();

    long getErrorCount();

    String getName();

    long getTotalTime();

    void reset();
}
