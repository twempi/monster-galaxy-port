package twitter4j;

import java.io.Serializable;
import java.util.Date;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface RateLimitStatus extends Serializable {
    int getHourlyLimit();

    int getRemainingHits();

    Date getResetTime();

    int getResetTimeInSeconds();

    int getSecondsUntilReset();
}
