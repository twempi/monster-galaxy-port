package twitter4j;

import twitter4j.auth.Authorization;
import twitter4j.conf.Configuration;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface TwitterBase {
    void addRateLimitStatusListener(RateLimitStatusListener rateLimitStatusListener);

    Authorization getAuthorization();

    Configuration getConfiguration();

    long getId() throws IllegalStateException, TwitterException;

    String getScreenName() throws IllegalStateException, TwitterException;

    void shutdown();
}
