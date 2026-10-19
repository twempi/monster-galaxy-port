package twitter4j;

import java.util.List;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface ResponseList<T> extends TwitterResponse, List<T> {
    RateLimitStatus getFeatureSpecificRateLimitStatus();

    @Override // twitter4j.TwitterResponse
    RateLimitStatus getRateLimitStatus();
}
