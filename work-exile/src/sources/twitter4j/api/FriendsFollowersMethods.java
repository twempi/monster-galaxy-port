package twitter4j.api;

import twitter4j.IDs;
import twitter4j.TwitterException;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface FriendsFollowersMethods {
    IDs getFollowersIDs(long j) throws TwitterException;

    IDs getFollowersIDs(long j, long j2) throws TwitterException;

    IDs getFollowersIDs(String str, long j) throws TwitterException;

    IDs getFriendsIDs(long j) throws TwitterException;

    IDs getFriendsIDs(long j, long j2) throws TwitterException;

    IDs getFriendsIDs(String str, long j) throws TwitterException;
}
