package twitter4j;

import java.io.Serializable;
import java.util.List;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface QueryResult extends Serializable {
    double getCompletedIn();

    long getMaxId();

    int getPage();

    String getQuery();

    String getRefreshUrl();

    int getResultsPerPage();

    long getSinceId();

    List<Tweet> getTweets();

    String getWarning();
}
