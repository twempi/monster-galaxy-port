package twitter4j;

import java.io.Serializable;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.conf.Configuration;
import twitter4j.internal.json.DataObjectFactoryUtil;
import twitter4j.internal.util.ParseUtil;

/* JADX INFO: loaded from: classes.dex */
final class TweetJSONImpl implements Tweet, Serializable {
    private static final long serialVersionUID = 3019285230338056113L;
    private Annotations annotations;
    private Date createdAt;
    private String fromUser;
    private long fromUserId;
    private GeoLocation geoLocation;
    private long id;
    private String isoLanguageCode;
    private String location;
    private Place place;
    private String profileImageUrl;
    private String source;
    private String text;
    private String toUser;
    private long toUserId;

    @Override // java.lang.Comparable
    public int compareTo(Tweet tweet) {
        return compareTo2(tweet);
    }

    TweetJSONImpl(JSONObject tweet) throws TwitterException {
        this.toUserId = -1L;
        this.toUser = null;
        this.isoLanguageCode = null;
        this.geoLocation = null;
        this.annotations = null;
        this.text = ParseUtil.getUnescapedString("text", tweet);
        this.toUserId = ParseUtil.getLong("to_user_id", tweet);
        this.toUser = ParseUtil.getRawString("to_user", tweet);
        this.fromUser = ParseUtil.getRawString("from_user", tweet);
        this.id = ParseUtil.getLong("id", tweet);
        this.fromUserId = ParseUtil.getLong("from_user_id", tweet);
        this.isoLanguageCode = ParseUtil.getRawString("iso_language_code", tweet);
        this.source = ParseUtil.getUnescapedString("source", tweet);
        this.profileImageUrl = ParseUtil.getUnescapedString("profile_image_url", tweet);
        this.createdAt = ParseUtil.getDate("created_at", tweet, "EEE, dd MMM yyyy HH:mm:ss z");
        this.location = ParseUtil.getRawString("location", tweet);
        this.geoLocation = GeoLocation.getInstance(tweet);
        if (!tweet.isNull("annotations")) {
            try {
                JSONArray annotationsArray = tweet.getJSONArray("annotations");
                this.annotations = new Annotations(annotationsArray);
            } catch (JSONException e) {
            }
        }
        if (!tweet.isNull("place")) {
            try {
                this.place = new PlaceJSONImpl(tweet.getJSONObject("place"));
                return;
            } catch (JSONException jsone) {
                throw new TwitterException(jsone);
            }
        }
        this.place = null;
    }

    TweetJSONImpl(JSONObject tweet, Configuration conf) throws TwitterException {
        this(tweet);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.registerJSONObject(this, tweet);
        }
    }

    /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
    public int compareTo2(Tweet that) {
        long delta = this.id - that.getId();
        if (delta < -2147483648L) {
            return Integer.MIN_VALUE;
        }
        if (delta > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) delta;
    }

    @Override // twitter4j.Tweet
    public String getText() {
        return this.text;
    }

    @Override // twitter4j.Tweet
    public long getToUserId() {
        return this.toUserId;
    }

    @Override // twitter4j.Tweet
    public String getToUser() {
        return this.toUser;
    }

    @Override // twitter4j.Tweet
    public String getFromUser() {
        return this.fromUser;
    }

    @Override // twitter4j.Tweet
    public long getId() {
        return this.id;
    }

    @Override // twitter4j.Tweet
    public long getFromUserId() {
        return this.fromUserId;
    }

    @Override // twitter4j.Tweet
    public String getIsoLanguageCode() {
        return this.isoLanguageCode;
    }

    @Override // twitter4j.Tweet
    public String getSource() {
        return this.source;
    }

    @Override // twitter4j.Tweet
    public String getProfileImageUrl() {
        return this.profileImageUrl;
    }

    @Override // twitter4j.Tweet
    public Date getCreatedAt() {
        return this.createdAt;
    }

    @Override // twitter4j.Tweet
    public GeoLocation getGeoLocation() {
        return this.geoLocation;
    }

    @Override // twitter4j.Tweet
    public String getLocation() {
        return this.location;
    }

    @Override // twitter4j.Tweet
    public Place getPlace() {
        return this.place;
    }

    @Override // twitter4j.Tweet
    public Annotations getAnnotations() {
        return this.annotations;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tweet)) {
            return false;
        }
        Tweet tweet = (Tweet) o;
        return this.id == tweet.getId();
    }

    public int hashCode() {
        int result = this.text != null ? this.text.hashCode() : 0;
        return (((((((((((((((((((((((((result * 31) + ((int) (this.toUserId ^ (this.toUserId >>> 32)))) * 31) + (this.toUser != null ? this.toUser.hashCode() : 0)) * 31) + (this.fromUser != null ? this.fromUser.hashCode() : 0)) * 31) + ((int) (this.id ^ (this.id >>> 32)))) * 31) + ((int) (this.fromUserId ^ (this.fromUserId >>> 32)))) * 31) + (this.isoLanguageCode != null ? this.isoLanguageCode.hashCode() : 0)) * 31) + (this.source != null ? this.source.hashCode() : 0)) * 31) + (this.profileImageUrl != null ? this.profileImageUrl.hashCode() : 0)) * 31) + (this.createdAt != null ? this.createdAt.hashCode() : 0)) * 31) + (this.location != null ? this.location.hashCode() : 0)) * 31) + (this.place != null ? this.place.hashCode() : 0)) * 31) + (this.geoLocation != null ? this.geoLocation.hashCode() : 0)) * 31) + (this.annotations != null ? this.annotations.hashCode() : 0);
    }

    public String toString() {
        return new StringBuffer().append("TweetJSONImpl{text='").append(this.text).append('\'').append(", toUserId=").append(this.toUserId).append(", toUser='").append(this.toUser).append('\'').append(", fromUser='").append(this.fromUser).append('\'').append(", id=").append(this.id).append(", fromUserId=").append(this.fromUserId).append(", isoLanguageCode='").append(this.isoLanguageCode).append('\'').append(", source='").append(this.source).append('\'').append(", profileImageUrl='").append(this.profileImageUrl).append('\'').append(", createdAt=").append(this.createdAt).append(", location='").append(this.location).append('\'').append(", place=").append(this.place).append(", geoLocation=").append(this.geoLocation).append(", annotations=").append(this.annotations).append('}').toString();
    }
}
