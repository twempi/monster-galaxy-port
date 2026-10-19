package twitter4j.api;

import twitter4j.Paging;
import twitter4j.StatusUpdate;

/* JADX INFO: loaded from: classes.dex */
public interface StatusMethodsAsync {
    void destroyStatus(long j);

    void getRetweetedBy(long j);

    void getRetweetedBy(long j, Paging paging);

    void getRetweetedByIDs(long j);

    void getRetweetedByIDs(long j, Paging paging);

    void getRetweets(long j);

    void retweetStatus(long j);

    void showStatus(long j);

    void updateStatus(String str);

    void updateStatus(StatusUpdate statusUpdate);
}
