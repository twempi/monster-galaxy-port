package twitter4j.api;

import java.util.Date;

/* JADX INFO: loaded from: classes.dex */
public interface TrendsMethodsAsync {
    void getCurrentTrends();

    void getCurrentTrends(boolean z);

    void getDailyTrends();

    void getDailyTrends(Date date, boolean z);

    void getTrends();

    void getWeeklyTrends();

    void getWeeklyTrends(Date date, boolean z);
}
