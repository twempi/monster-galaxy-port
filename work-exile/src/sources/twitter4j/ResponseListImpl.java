package twitter4j;

import java.util.ArrayList;
import twitter4j.internal.http.HttpResponse;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class ResponseListImpl<T> extends ArrayList<T> implements ResponseList<T> {
    private static final long serialVersionUID = 5646617841989265312L;
    private transient int accessLevel;
    private transient RateLimitStatus featureSpecificRateLimitStatus;
    private transient RateLimitStatus rateLimitStatus;

    ResponseListImpl(int size, HttpResponse res) {
        super(size);
        this.rateLimitStatus = null;
        this.featureSpecificRateLimitStatus = null;
        this.rateLimitStatus = RateLimitStatusJSONImpl.createFromResponseHeader(res);
        this.featureSpecificRateLimitStatus = RateLimitStatusJSONImpl.createFeatureSpecificRateLimitStatusFromResponseHeader(res);
        this.accessLevel = TwitterResponseImpl.toAccessLevel(res);
    }

    @Override // twitter4j.ResponseList, twitter4j.TwitterResponse
    public RateLimitStatus getRateLimitStatus() {
        return this.rateLimitStatus;
    }

    @Override // twitter4j.ResponseList
    public RateLimitStatus getFeatureSpecificRateLimitStatus() {
        return this.featureSpecificRateLimitStatus;
    }

    @Override // twitter4j.TwitterResponse
    public int getAccessLevel() {
        return this.accessLevel;
    }
}
