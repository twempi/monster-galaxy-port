package twitter4j.api;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface TimelineMethods {
    ResponseList<Status> getFriendsTimeline() throws TwitterException;

    ResponseList<Status> getFriendsTimeline(Paging paging) throws TwitterException;

    ResponseList<Status> getHomeTimeline() throws TwitterException;

    ResponseList<Status> getHomeTimeline(Paging paging) throws TwitterException;

    ResponseList<Status> getMentions() throws TwitterException;

    ResponseList<Status> getMentions(Paging paging) throws TwitterException;

    ResponseList<Status> getPublicTimeline() throws TwitterException;

    ResponseList<Status> getRetweetedByMe() throws TwitterException;

    ResponseList<Status> getRetweetedByMe(Paging paging) throws TwitterException;

    ResponseList<Status> getRetweetedByUser(long j, Paging paging) throws TwitterException;

    ResponseList<Status> getRetweetedByUser(String str, Paging paging) throws TwitterException;

    ResponseList<Status> getRetweetedToMe() throws TwitterException;

    ResponseList<Status> getRetweetedToMe(Paging paging) throws TwitterException;

    ResponseList<Status> getRetweetedToUser(long j, Paging paging) throws TwitterException;

    ResponseList<Status> getRetweetedToUser(String str, Paging paging) throws TwitterException;

    ResponseList<Status> getRetweetsOfMe() throws TwitterException;

    ResponseList<Status> getRetweetsOfMe(Paging paging) throws TwitterException;

    ResponseList<Status> getUserTimeline() throws TwitterException;

    ResponseList<Status> getUserTimeline(long j) throws TwitterException;

    ResponseList<Status> getUserTimeline(long j, Paging paging) throws TwitterException;

    ResponseList<Status> getUserTimeline(String str) throws TwitterException;

    ResponseList<Status> getUserTimeline(String str, Paging paging) throws TwitterException;

    ResponseList<Status> getUserTimeline(Paging paging) throws TwitterException;
}
