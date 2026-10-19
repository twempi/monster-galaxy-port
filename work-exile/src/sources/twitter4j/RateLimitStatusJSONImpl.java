package twitter4j;

import java.io.Serializable;
import java.util.Date;
import org.json.JSONObject;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.DataObjectFactoryUtil;
import twitter4j.internal.util.ParseUtil;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class RateLimitStatusJSONImpl implements RateLimitStatus, Serializable {
    private static final long serialVersionUID = 832355052293658614L;
    private int hourlyLimit;
    private int remainingHits;
    private Date resetTime;
    private int resetTimeInSeconds;
    private int secondsUntilReset;

    private RateLimitStatusJSONImpl(int hourlyLimit, int remainingHits, int resetTimeInSeconds, Date resetTime) {
        this.hourlyLimit = hourlyLimit;
        this.remainingHits = remainingHits;
        this.resetTime = resetTime;
        this.resetTimeInSeconds = resetTimeInSeconds;
        this.secondsUntilReset = (int) ((resetTime.getTime() - System.currentTimeMillis()) / 1000);
    }

    RateLimitStatusJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        JSONObject json = res.asJSONObject();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(this, json);
        }
    }

    RateLimitStatusJSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    void init(JSONObject json) throws TwitterException {
        this.hourlyLimit = ParseUtil.getInt("hourly_limit", json);
        this.remainingHits = ParseUtil.getInt("remaining_hits", json);
        this.resetTime = ParseUtil.getDate("reset_time", json, "EEE MMM d HH:mm:ss Z yyyy");
        this.resetTimeInSeconds = ParseUtil.getInt("reset_time_in_seconds", json);
        this.secondsUntilReset = (int) ((this.resetTime.getTime() - System.currentTimeMillis()) / 1000);
    }

    static RateLimitStatus createFromResponseHeader(HttpResponse res) {
        String limit;
        if (res == null || (limit = res.getResponseHeader("X-RateLimit-Limit")) == null) {
            return null;
        }
        int hourlyLimit = Integer.parseInt(limit);
        String remaining = res.getResponseHeader("X-RateLimit-Remaining");
        if (remaining == null) {
            return null;
        }
        int remainingHits = Integer.parseInt(remaining);
        String reset = res.getResponseHeader("X-RateLimit-Reset");
        if (reset == null) {
            return null;
        }
        long longReset = Long.parseLong(reset);
        int resetTimeInSeconds = (int) (longReset / 1000);
        Date resetTime = new Date(longReset * 1000);
        return new RateLimitStatusJSONImpl(hourlyLimit, remainingHits, resetTimeInSeconds, resetTime);
    }

    static RateLimitStatus createFeatureSpecificRateLimitStatusFromResponseHeader(HttpResponse res) {
        String limit;
        if (res == null || (limit = res.getResponseHeader("X-FeatureRateLimit-Limit")) == null) {
            return null;
        }
        int hourlyLimit = Integer.parseInt(limit);
        String remaining = res.getResponseHeader("X-FeatureRateLimit-Remaining");
        if (remaining == null) {
            return null;
        }
        int remainingHits = Integer.parseInt(remaining);
        String reset = res.getResponseHeader("X-FeatureRateLimit-Reset");
        if (reset == null) {
            return null;
        }
        long longReset = Long.parseLong(reset);
        int resetTimeInSeconds = (int) (longReset / 1000);
        Date resetTime = new Date(longReset * 1000);
        return new RateLimitStatusJSONImpl(hourlyLimit, remainingHits, resetTimeInSeconds, resetTime);
    }

    @Override // twitter4j.RateLimitStatus
    public int getRemainingHits() {
        return this.remainingHits;
    }

    @Override // twitter4j.RateLimitStatus
    public int getHourlyLimit() {
        return this.hourlyLimit;
    }

    @Override // twitter4j.RateLimitStatus
    public int getResetTimeInSeconds() {
        return this.resetTimeInSeconds;
    }

    @Override // twitter4j.RateLimitStatus
    public int getSecondsUntilReset() {
        return this.secondsUntilReset;
    }

    @Override // twitter4j.RateLimitStatus
    public Date getResetTime() {
        return this.resetTime;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RateLimitStatusJSONImpl)) {
            return false;
        }
        RateLimitStatusJSONImpl that = (RateLimitStatusJSONImpl) o;
        if (this.hourlyLimit == that.hourlyLimit && this.remainingHits == that.remainingHits && this.resetTimeInSeconds == that.resetTimeInSeconds && this.secondsUntilReset == that.secondsUntilReset) {
            if (this.resetTime != null) {
                if (this.resetTime.equals(that.resetTime)) {
                    return true;
                }
            } else if (that.resetTime == null) {
                return true;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int result = this.remainingHits;
        return (((((((result * 31) + this.hourlyLimit) * 31) + this.resetTimeInSeconds) * 31) + this.secondsUntilReset) * 31) + (this.resetTime != null ? this.resetTime.hashCode() : 0);
    }

    public String toString() {
        return new StringBuffer().append("RateLimitStatusJSONImpl{remainingHits=").append(this.remainingHits).append(", hourlyLimit=").append(this.hourlyLimit).append(", resetTimeInSeconds=").append(this.resetTimeInSeconds).append(", secondsUntilReset=").append(this.secondsUntilReset).append(", resetTime=").append(this.resetTime).append('}').toString();
    }
}
