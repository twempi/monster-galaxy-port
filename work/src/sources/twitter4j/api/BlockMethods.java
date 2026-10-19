package twitter4j.api;

import twitter4j.IDs;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.User;

/* JADX INFO: loaded from: classes.dex */
public interface BlockMethods {
    User createBlock(long j) throws TwitterException;

    User createBlock(String str) throws TwitterException;

    User destroyBlock(long j) throws TwitterException;

    User destroyBlock(String str) throws TwitterException;

    boolean existsBlock(long j) throws TwitterException;

    boolean existsBlock(String str) throws TwitterException;

    ResponseList<User> getBlockingUsers() throws TwitterException;

    ResponseList<User> getBlockingUsers(int i) throws TwitterException;

    IDs getBlockingUsersIDs() throws TwitterException;
}
