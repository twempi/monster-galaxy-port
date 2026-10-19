package twitter4j.api;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.TwitterException;

/* JADX INFO: loaded from: classes.dex */
public interface SearchMethods {
    QueryResult search(Query query) throws TwitterException;
}
