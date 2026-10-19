package twitter4j.management;

/* JADX INFO: loaded from: classes.dex */
public class InvocationStatisticsCalculator implements InvocationStatistics {
    private long callCount;
    private long errorCount;
    private int index;
    private String name;
    private long[] times;
    private long totalTime;

    public InvocationStatisticsCalculator(String name, int historySize) {
        this.name = name;
        this.times = new long[historySize];
    }

    public void increment(long time, boolean success) {
        this.callCount++;
        this.errorCount = (success ? 0L : 1L) + this.errorCount;
        this.totalTime += time;
        this.times[this.index] = time;
        int i = this.index + 1;
        this.index = i;
        if (i >= this.times.length) {
            this.index = 0;
        }
    }

    @Override // twitter4j.management.InvocationStatistics
    public String getName() {
        return this.name;
    }

    @Override // twitter4j.management.InvocationStatistics
    public long getCallCount() {
        return this.callCount;
    }

    @Override // twitter4j.management.InvocationStatistics
    public long getErrorCount() {
        return this.errorCount;
    }

    @Override // twitter4j.management.InvocationStatistics
    public long getTotalTime() {
        return this.totalTime;
    }

    @Override // twitter4j.management.InvocationStatistics
    public synchronized long getAverageTime() {
        long j;
        int stopIndex = Math.min(Math.abs((int) this.callCount), this.times.length);
        if (stopIndex == 0) {
            j = 0;
        } else {
            long totalTime = 0;
            for (int i = 0; i < stopIndex; i++) {
                totalTime += this.times[i];
            }
            j = totalTime / ((long) stopIndex);
        }
        return j;
    }

    @Override // twitter4j.management.InvocationStatistics
    public synchronized void reset() {
        this.callCount = 0L;
        this.errorCount = 0L;
        this.totalTime = 0L;
        this.times = new long[this.times.length];
        this.index = 0;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("calls=").append(getCallCount()).append(",").append("errors=").append(getErrorCount()).append(",").append("totalTime=").append(getTotalTime()).append(",").append("avgTime=").append(getAverageTime());
        return sb.toString();
    }
}
