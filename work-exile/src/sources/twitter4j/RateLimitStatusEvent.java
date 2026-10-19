package twitter4j;

import java.util.EventObject;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class RateLimitStatusEvent extends EventObject {
    private static final long serialVersionUID = -2332507741769177298L;
    private boolean isAccountRateLimitStatus;
    private RateLimitStatus rateLimitStatus;

    RateLimitStatusEvent(Object source, RateLimitStatus rateLimitStatus, boolean isAccountRateLimitStatus) {
        super(source);
        this.rateLimitStatus = rateLimitStatus;
        this.isAccountRateLimitStatus = isAccountRateLimitStatus;
    }

    public RateLimitStatus getRateLimitStatus() {
        return this.rateLimitStatus;
    }

    public boolean isAccountRateLimitStatus() {
        return this.isAccountRateLimitStatus;
    }

    public boolean isIPRateLimitStatus() {
        return !this.isAccountRateLimitStatus;
    }
}
