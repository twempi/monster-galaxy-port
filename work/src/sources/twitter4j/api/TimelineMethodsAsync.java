package twitter4j.api;

import twitter4j.Paging;

/* JADX INFO: loaded from: classes.dex */
public interface TimelineMethodsAsync {
    void getFriendsTimeline();

    void getFriendsTimeline(Paging paging);

    void getHomeTimeline();

    void getHomeTimeline(Paging paging);

    void getMentions();

    void getMentions(Paging paging);

    void getPublicTimeline();

    void getRetweetedByMe();

    void getRetweetedByMe(Paging paging);

    void getRetweetedByUser(long j, Paging paging);

    void getRetweetedByUser(String str, Paging paging);

    void getRetweetedToMe();

    void getRetweetedToMe(Paging paging);

    void getRetweetedToUser(long j, Paging paging);

    void getRetweetedToUser(String str, Paging paging);

    void getRetweetsOfMe();

    void getRetweetsOfMe(Paging paging);

    void getUserTimeline();

    void getUserTimeline(long j);

    void getUserTimeline(long j, Paging paging);

    void getUserTimeline(String str);

    void getUserTimeline(String str, Paging paging);

    void getUserTimeline(Paging paging);
}
