package twitter4j.api;

import twitter4j.ResponseList;
import twitter4j.TwitterAPIConfiguration;
import twitter4j.TwitterException;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface HelpMethods {

    public interface Language {
        String getCode();

        String getName();

        String getStatus();
    }

    TwitterAPIConfiguration getAPIConfiguration() throws TwitterException;

    ResponseList<Language> getLanguages() throws TwitterException;

    boolean test() throws TwitterException;
}
