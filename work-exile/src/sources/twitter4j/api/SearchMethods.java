package twitter4j.api;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.TwitterException;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface SearchMethods {
    QueryResult search(Query query) throws TwitterException;
}
