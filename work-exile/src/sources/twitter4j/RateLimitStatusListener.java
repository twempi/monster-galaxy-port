package twitter4j;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface RateLimitStatusListener {
    void onRateLimitReached(RateLimitStatusEvent rateLimitStatusEvent);

    void onRateLimitStatus(RateLimitStatusEvent rateLimitStatusEvent);
}
