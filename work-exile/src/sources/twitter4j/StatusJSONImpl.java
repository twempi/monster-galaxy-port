package twitter4j;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.conf.Configuration;
import twitter4j.conf.PropertyConfiguration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.DataObjectFactoryUtil;
import twitter4j.internal.logging.Logger;
import twitter4j.internal.util.ParseUtil;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class StatusJSONImpl extends TwitterResponseImpl implements Status, Serializable {
    static Class class$twitter4j$StatusJSONImpl = null;
    private static final Logger logger;
    private static final long serialVersionUID = 7548618898682727465L;
    private Annotations annotations;
    private String[] contributors;
    private long[] contributorsIDs;
    private Date createdAt;
    private GeoLocation geoLocation;
    private HashtagEntity[] hashtagEntities;
    private long id;
    private String inReplyToScreenName;
    private long inReplyToStatusId;
    private long inReplyToUserId;
    private boolean isFavorited;
    private boolean isTruncated;
    private MediaEntity[] mediaEntities;
    private Place place;
    private long retweetCount;
    private Status retweetedStatus;
    private String source;
    private String text;
    private URLEntity[] urlEntities;
    private User user;
    private UserMentionEntity[] userMentionEntities;
    private boolean wasRetweetedByMe;

    @Override // java.lang.Comparable
    public int compareTo(Status status) {
        return compareTo2(status);
    }

    static {
        Class clsClass$;
        if (class$twitter4j$StatusJSONImpl == null) {
            clsClass$ = class$("twitter4j.StatusJSONImpl");
            class$twitter4j$StatusJSONImpl = clsClass$;
        } else {
            clsClass$ = class$twitter4j$StatusJSONImpl;
        }
        logger = Logger.getLogger(clsClass$);
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    StatusJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        this.geoLocation = null;
        this.place = null;
        this.contributors = null;
        this.annotations = null;
        this.user = null;
        JSONObject json = res.asJSONObject();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(this, json);
        }
    }

    StatusJSONImpl(JSONObject json) throws TwitterException {
        this.geoLocation = null;
        this.place = null;
        this.contributors = null;
        this.annotations = null;
        this.user = null;
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        this.id = ParseUtil.getLong("id", json);
        this.text = ParseUtil.getUnescapedString("text", json);
        this.source = ParseUtil.getUnescapedString("source", json);
        this.createdAt = ParseUtil.getDate("created_at", json);
        this.isTruncated = ParseUtil.getBoolean("truncated", json);
        this.inReplyToStatusId = ParseUtil.getLong("in_reply_to_status_id", json);
        this.inReplyToUserId = ParseUtil.getLong("in_reply_to_user_id", json);
        this.isFavorited = ParseUtil.getBoolean("favorited", json);
        this.inReplyToScreenName = ParseUtil.getUnescapedString("in_reply_to_screen_name", json);
        this.retweetCount = ParseUtil.getLong("retweet_count", json);
        this.wasRetweetedByMe = ParseUtil.getBoolean("retweeted", json);
        try {
            if (!json.isNull(PropertyConfiguration.USER)) {
                this.user = new UserJSONImpl(json.getJSONObject(PropertyConfiguration.USER));
            }
            this.geoLocation = GeoLocation.getInstance(json);
            if (!json.isNull("place")) {
                try {
                    this.place = new PlaceJSONImpl(json.getJSONObject("place"));
                } catch (JSONException ignore) {
                    ignore.printStackTrace();
                    logger.warn(new StringBuffer().append("failed to parse place:").append(json).toString());
                }
            }
            if (!json.isNull("retweeted_status")) {
                try {
                    this.retweetedStatus = new StatusJSONImpl(json.getJSONObject("retweeted_status"));
                } catch (JSONException ignore2) {
                    ignore2.printStackTrace();
                    logger.warn(new StringBuffer().append("failed to parse retweeted_status:").append(json).toString());
                }
            }
            if (!json.isNull("contributors")) {
                try {
                    JSONArray contributorsArray = json.getJSONArray("contributors");
                    this.contributorsIDs = new long[contributorsArray.length()];
                    for (int i = 0; i < contributorsArray.length(); i++) {
                        this.contributorsIDs[i] = Long.parseLong(contributorsArray.getString(i));
                    }
                } catch (NumberFormatException ignore3) {
                    ignore3.printStackTrace();
                    logger.warn(new StringBuffer().append("failed to parse contributors:").append(json).toString());
                } catch (JSONException ignore4) {
                    ignore4.printStackTrace();
                    logger.warn(new StringBuffer().append("failed to parse contributors:").append(json).toString());
                }
            } else {
                this.contributors = null;
            }
            if (!json.isNull("entities")) {
                try {
                    JSONObject entities = json.getJSONObject("entities");
                    JSONArray userMentionsArray = entities.getJSONArray("user_mentions");
                    int len = userMentionsArray.length();
                    this.userMentionEntities = new UserMentionEntity[len];
                    for (int i2 = 0; i2 < len; i2++) {
                        this.userMentionEntities[i2] = new UserMentionEntityJSONImpl(userMentionsArray.getJSONObject(i2));
                    }
                    JSONArray urlsArray = entities.getJSONArray("urls");
                    int len2 = urlsArray.length();
                    this.urlEntities = new URLEntity[len2];
                    for (int i3 = 0; i3 < len2; i3++) {
                        this.urlEntities[i3] = new URLEntityJSONImpl(urlsArray.getJSONObject(i3));
                    }
                    JSONArray hashtagsArray = entities.getJSONArray("hashtags");
                    int len3 = hashtagsArray.length();
                    this.hashtagEntities = new HashtagEntity[len3];
                    for (int i4 = 0; i4 < len3; i4++) {
                        this.hashtagEntities[i4] = new HashtagEntityJSONImpl(hashtagsArray.getJSONObject(i4));
                    }
                    JSONArray mediaArray = entities.getJSONArray("media");
                    int len4 = mediaArray.length();
                    this.mediaEntities = new MediaEntity[len4];
                    for (int i5 = 0; i5 < len4; i5++) {
                        this.mediaEntities[i5] = new MediaEntityJSONImpl(mediaArray.getJSONObject(i5));
                    }
                } catch (JSONException e) {
                }
            }
            if (!json.isNull("annotations")) {
                try {
                    JSONArray annotationsArray = json.getJSONArray("annotations");
                    this.annotations = new Annotations(annotationsArray);
                } catch (JSONException e2) {
                }
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
    public int compareTo2(Status that) {
        long delta = this.id - that.getId();
        if (delta < -2147483648L) {
            return Integer.MIN_VALUE;
        }
        if (delta > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) delta;
    }

    @Override // twitter4j.Status
    public Date getCreatedAt() {
        return this.createdAt;
    }

    @Override // twitter4j.Status
    public long getId() {
        return this.id;
    }

    @Override // twitter4j.Status
    public String getText() {
        return this.text;
    }

    @Override // twitter4j.Status
    public String getSource() {
        return this.source;
    }

    @Override // twitter4j.Status
    public boolean isTruncated() {
        return this.isTruncated;
    }

    @Override // twitter4j.Status
    public long getInReplyToStatusId() {
        return this.inReplyToStatusId;
    }

    @Override // twitter4j.Status
    public long getInReplyToUserId() {
        return this.inReplyToUserId;
    }

    @Override // twitter4j.Status
    public String getInReplyToScreenName() {
        return this.inReplyToScreenName;
    }

    @Override // twitter4j.Status
    public GeoLocation getGeoLocation() {
        return this.geoLocation;
    }

    @Override // twitter4j.Status
    public Place getPlace() {
        return this.place;
    }

    @Override // twitter4j.Status
    public long[] getContributors() {
        if (this.contributors != null) {
            this.contributorsIDs = new long[this.contributors.length];
            for (int i = 0; i < this.contributors.length; i++) {
                try {
                    this.contributorsIDs[i] = Long.parseLong(this.contributors[i]);
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                    logger.warn(new StringBuffer().append("failed to parse contributors:").append(nfe).toString());
                }
            }
            this.contributors = null;
        }
        return this.contributorsIDs;
    }

    @Override // twitter4j.Status
    public Annotations getAnnotations() {
        return this.annotations;
    }

    @Override // twitter4j.Status
    public boolean isFavorited() {
        return this.isFavorited;
    }

    @Override // twitter4j.Status
    public User getUser() {
        return this.user;
    }

    @Override // twitter4j.Status
    public boolean isRetweet() {
        return this.retweetedStatus != null;
    }

    @Override // twitter4j.Status
    public Status getRetweetedStatus() {
        return this.retweetedStatus;
    }

    @Override // twitter4j.Status
    public long getRetweetCount() {
        return this.retweetCount;
    }

    @Override // twitter4j.Status
    public boolean isRetweetedByMe() {
        return this.wasRetweetedByMe;
    }

    @Override // twitter4j.Status
    public UserMentionEntity[] getUserMentionEntities() {
        return this.userMentionEntities;
    }

    @Override // twitter4j.Status
    public URLEntity[] getURLEntities() {
        return this.urlEntities;
    }

    @Override // twitter4j.Status
    public HashtagEntity[] getHashtagEntities() {
        return this.hashtagEntities;
    }

    @Override // twitter4j.Status
    public MediaEntity[] getMediaEntities() {
        return this.mediaEntities;
    }

    static ResponseList<Status> createStatusList(HttpResponse res, Configuration conf) throws Throwable {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            JSONArray list = res.asJSONArray();
            int size = list.length();
            ResponseList<Status> statuses = new ResponseListImpl<>(size, res);
            for (int i = 0; i < size; i++) {
                JSONObject json = list.getJSONObject(i);
                StatusJSONImpl statusJSONImpl = new StatusJSONImpl(json);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(statusJSONImpl, json);
                }
                statuses.add(statusJSONImpl);
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(statuses, list);
            }
            return statuses;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }

    public int hashCode() {
        return (int) this.id;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this != obj) {
            return (obj instanceof Status) && ((Status) obj).getId() == this.id;
        }
        return true;
    }

    public String toString() {
        return new StringBuffer().append("StatusJSONImpl{createdAt=").append(this.createdAt).append(", id=").append(this.id).append(", text='").append(this.text).append('\'').append(", source='").append(this.source).append('\'').append(", isTruncated=").append(this.isTruncated).append(", inReplyToStatusId=").append(this.inReplyToStatusId).append(", inReplyToUserId=").append(this.inReplyToUserId).append(", isFavorited=").append(this.isFavorited).append(", inReplyToScreenName='").append(this.inReplyToScreenName).append('\'').append(", geoLocation=").append(this.geoLocation).append(", place=").append(this.place).append(", retweetCount=").append(this.retweetCount).append(", wasRetweetedByMe=").append(this.wasRetweetedByMe).append(", contributors=").append(this.contributorsIDs == null ? null : Arrays.asList(this.contributorsIDs)).append(", annotations=").append(this.annotations).append(", retweetedStatus=").append(this.retweetedStatus).append(", userMentionEntities=").append(this.userMentionEntities == null ? null : Arrays.asList(this.userMentionEntities)).append(", urlEntities=").append(this.urlEntities == null ? null : Arrays.asList(this.urlEntities)).append(", hashtagEntities=").append(this.hashtagEntities != null ? Arrays.asList(this.hashtagEntities) : null).append(", user=").append(this.user).append('}').toString();
    }
}
