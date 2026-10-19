package twitter4j.api;

import twitter4j.PagableResponseList;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserList;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface ListSubscribersMethods {
    User checkUserListSubscription(String str, int i, long j) throws TwitterException;

    UserList createUserListSubscription(int i) throws TwitterException;

    UserList destroyUserListSubscription(int i) throws TwitterException;

    PagableResponseList<User> getUserListSubscribers(int i, long j) throws TwitterException;

    PagableResponseList<User> getUserListSubscribers(String str, int i, long j) throws TwitterException;

    User showUserListSubscription(int i, long j) throws TwitterException;

    UserList subscribeUserList(String str, int i) throws TwitterException;

    UserList unsubscribeUserList(String str, int i) throws TwitterException;
}
