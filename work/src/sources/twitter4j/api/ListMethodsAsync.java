package twitter4j.api;

import twitter4j.Paging;

/* JADX INFO: loaded from: classes.dex */
public interface ListMethodsAsync {
    void createUserList(String str, boolean z, String str2);

    void destroyUserList(int i);

    void getAllSubscribingUserLists(long j);

    void getAllSubscribingUserLists(String str);

    void getUserListMemberships(String str, long j);

    void getUserListStatuses(int i, Paging paging);

    void getUserListStatuses(long j, int i, Paging paging);

    void getUserListStatuses(String str, int i, Paging paging);

    void getUserListSubscriptions(String str, long j);

    void getUserLists(long j, long j2);

    void getUserLists(String str, long j);

    void showUserList(int i);

    void showUserList(String str, int i);

    void updateUserList(int i, String str, boolean z, String str2);
}
