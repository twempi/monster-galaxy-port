package twitter4j.api;

import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.UserList;

/* JADX INFO: loaded from: classes.dex */
public interface ListMethods {
    UserList createUserList(String str, boolean z, String str2) throws TwitterException;

    UserList destroyUserList(int i) throws TwitterException;

    ResponseList<UserList> getAllUserLists(long j) throws TwitterException;

    ResponseList<UserList> getAllUserLists(String str) throws TwitterException;

    PagableResponseList<UserList> getUserListMemberships(String str, long j) throws TwitterException;

    ResponseList<Status> getUserListStatuses(int i, Paging paging) throws TwitterException;

    ResponseList<Status> getUserListStatuses(long j, int i, Paging paging) throws TwitterException;

    ResponseList<Status> getUserListStatuses(String str, int i, Paging paging) throws TwitterException;

    PagableResponseList<UserList> getUserListSubscriptions(String str, long j) throws TwitterException;

    PagableResponseList<UserList> getUserLists(long j, long j2) throws TwitterException;

    PagableResponseList<UserList> getUserLists(String str, long j) throws TwitterException;

    UserList showUserList(int i) throws TwitterException;

    UserList showUserList(String str, int i) throws TwitterException;

    UserList updateUserList(int i, String str, boolean z, String str2) throws TwitterException;
}
