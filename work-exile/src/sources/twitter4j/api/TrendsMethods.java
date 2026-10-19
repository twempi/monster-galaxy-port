package twitter4j.api;

import java.util.Date;
import java.util.List;
import twitter4j.Trends;
import twitter4j.TwitterException;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface TrendsMethods {
    Trends getCurrentTrends() throws TwitterException;

    Trends getCurrentTrends(boolean z) throws TwitterException;

    List<Trends> getDailyTrends() throws TwitterException;

    List<Trends> getDailyTrends(Date date, boolean z) throws TwitterException;

    Trends getTrends() throws TwitterException;

    List<Trends> getWeeklyTrends() throws TwitterException;

    List<Trends> getWeeklyTrends(Date date, boolean z) throws TwitterException;
}
