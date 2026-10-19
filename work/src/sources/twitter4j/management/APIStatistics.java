package twitter4j.management;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class APIStatistics implements APIStatisticsMBean {
    private final InvocationStatisticsCalculator API_STATS_CALCULATOR;
    private final int HISTORY_SIZE;
    private final Map<String, InvocationStatisticsCalculator> METHOD_STATS_MAP = new HashMap(100);

    public APIStatistics(int historySize) {
        this.API_STATS_CALCULATOR = new InvocationStatisticsCalculator("API", historySize);
        this.HISTORY_SIZE = historySize;
    }

    public synchronized void methodCalled(String method, long time, boolean success) {
        getMethodStatistics(method).increment(time, success);
        this.API_STATS_CALCULATOR.increment(time, success);
    }

    private synchronized InvocationStatisticsCalculator getMethodStatistics(String method) {
        InvocationStatisticsCalculator methodStats;
        methodStats = this.METHOD_STATS_MAP.get(method);
        if (methodStats == null) {
            methodStats = new InvocationStatisticsCalculator(method, this.HISTORY_SIZE);
            this.METHOD_STATS_MAP.put(method, methodStats);
        }
        return methodStats;
    }

    @Override // twitter4j.management.APIStatisticsMBean
    public synchronized Iterable<? extends InvocationStatistics> getInvocationStatistics() {
        return this.METHOD_STATS_MAP.values();
    }

    @Override // twitter4j.management.InvocationStatistics
    public synchronized void reset() {
        this.API_STATS_CALCULATOR.reset();
        this.METHOD_STATS_MAP.clear();
    }

    @Override // twitter4j.management.InvocationStatistics
    public String getName() {
        return this.API_STATS_CALCULATOR.getName();
    }

    @Override // twitter4j.management.InvocationStatistics
    public long getCallCount() {
        return this.API_STATS_CALCULATOR.getCallCount();
    }

    @Override // twitter4j.management.InvocationStatistics
    public long getErrorCount() {
        return this.API_STATS_CALCULATOR.getErrorCount();
    }

    @Override // twitter4j.management.InvocationStatistics
    public long getTotalTime() {
        return this.API_STATS_CALCULATOR.getTotalTime();
    }

    @Override // twitter4j.management.InvocationStatistics
    public long getAverageTime() {
        return this.API_STATS_CALCULATOR.getAverageTime();
    }

    @Override // twitter4j.management.APIStatisticsMBean
    public synchronized Map<String, String> getMethodLevelSummariesAsString() {
        Map<String, String> summariesMap;
        summariesMap = new HashMap<>();
        Collection<InvocationStatisticsCalculator> allMethodStats = this.METHOD_STATS_MAP.values();
        for (InvocationStatisticsCalculator methodStats : allMethodStats) {
            summariesMap.put(methodStats.getName(), methodStats.toString());
        }
        return summariesMap;
    }

    @Override // twitter4j.management.APIStatisticsMBean
    public synchronized String getMethodLevelSummary(String methodName) {
        return this.METHOD_STATS_MAP.get(methodName).toString();
    }
}
