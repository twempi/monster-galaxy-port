package twitter4j.api;

import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;
import twitter4j.User;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface StatusMethods {
    Status destroyStatus(long j) throws TwitterException;

    ResponseList<User> getRetweetedBy(long j) throws TwitterException;

    ResponseList<User> getRetweetedBy(long j, Paging paging) throws TwitterException;

    IDs getRetweetedByIDs(long j) throws TwitterException;

    IDs getRetweetedByIDs(long j, Paging paging) throws TwitterException;

    ResponseList<Status> getRetweets(long j) throws TwitterException;

    Status retweetStatus(long j) throws TwitterException;

    Status showStatus(long j) throws TwitterException;

    Status updateStatus(String str) throws TwitterException;

    Status updateStatus(StatusUpdate statusUpdate) throws TwitterException;
}
