package twitter4j.api;

import twitter4j.PagableResponseList;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserList;

/* JADX INFO: loaded from: classes.dex */
public interface ListMembersMethods {
    UserList addUserListMember(int i, long j) throws TwitterException;

    UserList addUserListMembers(int i, long[] jArr) throws TwitterException;

    UserList addUserListMembers(int i, String[] strArr) throws TwitterException;

    User checkUserListMembership(String str, int i, long j) throws TwitterException;

    UserList deleteUserListMember(int i, long j) throws TwitterException;

    PagableResponseList<User> getUserListMembers(int i, long j) throws TwitterException;

    PagableResponseList<User> getUserListMembers(long j, int i, long j2) throws TwitterException;

    PagableResponseList<User> getUserListMembers(String str, int i, long j) throws TwitterException;

    User showUserListMembership(int i, long j) throws TwitterException;
}
