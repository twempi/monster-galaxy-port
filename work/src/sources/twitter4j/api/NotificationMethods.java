package twitter4j.api;

import twitter4j.TwitterException;
import twitter4j.User;

/* JADX INFO: loaded from: classes.dex */
public interface NotificationMethods {
    User disableNotification(long j) throws TwitterException;

    User disableNotification(String str) throws TwitterException;

    User enableNotification(long j) throws TwitterException;

    User enableNotification(String str) throws TwitterException;
}
