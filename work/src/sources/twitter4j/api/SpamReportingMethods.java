package twitter4j.api;

import twitter4j.TwitterException;
import twitter4j.User;

/* JADX INFO: loaded from: classes.dex */
public interface SpamReportingMethods {
    User reportSpam(long j) throws TwitterException;

    User reportSpam(String str) throws TwitterException;
}
