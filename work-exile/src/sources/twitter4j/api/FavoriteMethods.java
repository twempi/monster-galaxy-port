package twitter4j.api;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface FavoriteMethods {
    Status createFavorite(long j) throws TwitterException;

    Status destroyFavorite(long j) throws TwitterException;

    ResponseList<Status> getFavorites() throws TwitterException;

    ResponseList<Status> getFavorites(int i) throws TwitterException;

    ResponseList<Status> getFavorites(String str) throws TwitterException;

    ResponseList<Status> getFavorites(String str, int i) throws TwitterException;
}
