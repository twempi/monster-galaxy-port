package twitter4j.management;

import java.util.Map;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface APIStatisticsMBean extends InvocationStatistics {
    Iterable<? extends InvocationStatistics> getInvocationStatistics();

    Map<String, String> getMethodLevelSummariesAsString();

    String getMethodLevelSummary(String str);
}
