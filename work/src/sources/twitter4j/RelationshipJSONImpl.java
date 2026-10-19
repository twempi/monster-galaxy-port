package twitter4j;

import com.millennialmedia.android.MMDemographic;
import java.io.Serializable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.DataObjectFactoryUtil;
import twitter4j.internal.util.ParseUtil;

/* JADX INFO: loaded from: classes.dex */
class RelationshipJSONImpl extends TwitterResponseImpl implements Relationship, Serializable {
    private static final long serialVersionUID = 7725021608907856360L;
    private final boolean sourceBlockingTarget;
    private final boolean sourceFollowedByTarget;
    private final boolean sourceFollowingTarget;
    private final boolean sourceNotificationsEnabled;
    private final long sourceUserId;
    private final String sourceUserScreenName;
    private final long targetUserId;
    private final String targetUserScreenName;

    RelationshipJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        this(res, res.asJSONObject());
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(this, res.asJSONObject());
        }
    }

    RelationshipJSONImpl(JSONObject json) throws TwitterException {
        this((HttpResponse) null, json);
    }

    RelationshipJSONImpl(HttpResponse res, JSONObject json) throws TwitterException {
        super(res);
        try {
            JSONObject relationship = json.getJSONObject(MMDemographic.MARITAL_RELATIONSHIP);
            JSONObject sourceJson = relationship.getJSONObject("source");
            JSONObject targetJson = relationship.getJSONObject("target");
            this.sourceUserId = ParseUtil.getLong("id", sourceJson);
            this.targetUserId = ParseUtil.getLong("id", targetJson);
            this.sourceUserScreenName = ParseUtil.getUnescapedString("screen_name", sourceJson);
            this.targetUserScreenName = ParseUtil.getUnescapedString("screen_name", targetJson);
            this.sourceBlockingTarget = ParseUtil.getBoolean("blocking", sourceJson);
            this.sourceFollowingTarget = ParseUtil.getBoolean("following", sourceJson);
            this.sourceFollowedByTarget = ParseUtil.getBoolean("followed_by", sourceJson);
            this.sourceNotificationsEnabled = ParseUtil.getBoolean("notifications_enabled", sourceJson);
        } catch (JSONException jsone) {
            throw new TwitterException(new StringBuffer().append(jsone.getMessage()).append(":").append(json.toString()).toString(), jsone);
        }
    }

    static ResponseList<Relationship> createRelationshipList(HttpResponse res, Configuration conf) throws Throwable {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            JSONArray list = res.asJSONArray();
            int size = list.length();
            ResponseList<Relationship> relationships = new ResponseListImpl<>(size, res);
            for (int i = 0; i < size; i++) {
                JSONObject json = list.getJSONObject(i);
                RelationshipJSONImpl relationshipJSONImpl = new RelationshipJSONImpl(json);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(relationshipJSONImpl, json);
                }
                relationships.add(relationshipJSONImpl);
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(relationships, list);
            }
            return relationships;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }

    @Override // twitter4j.Relationship
    public long getSourceUserId() {
        return this.sourceUserId;
    }

    @Override // twitter4j.Relationship
    public long getTargetUserId() {
        return this.targetUserId;
    }

    @Override // twitter4j.Relationship
    public boolean isSourceBlockingTarget() {
        return this.sourceBlockingTarget;
    }

    @Override // twitter4j.Relationship
    public String getSourceUserScreenName() {
        return this.sourceUserScreenName;
    }

    @Override // twitter4j.Relationship
    public String getTargetUserScreenName() {
        return this.targetUserScreenName;
    }

    @Override // twitter4j.Relationship
    public boolean isSourceFollowingTarget() {
        return this.sourceFollowingTarget;
    }

    @Override // twitter4j.Relationship
    public boolean isTargetFollowingSource() {
        return this.sourceFollowedByTarget;
    }

    @Override // twitter4j.Relationship
    public boolean isSourceFollowedByTarget() {
        return this.sourceFollowedByTarget;
    }

    @Override // twitter4j.Relationship
    public boolean isTargetFollowedBySource() {
        return this.sourceFollowingTarget;
    }

    @Override // twitter4j.Relationship
    public boolean isSourceNotificationsEnabled() {
        return this.sourceNotificationsEnabled;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Relationship)) {
            return false;
        }
        Relationship that = (Relationship) o;
        return this.sourceUserId == that.getSourceUserId() && this.targetUserId == that.getTargetUserId() && this.sourceUserScreenName.equals(that.getSourceUserScreenName()) && this.targetUserScreenName.equals(that.getTargetUserScreenName());
    }

    public int hashCode() {
        int result = (int) (this.targetUserId ^ (this.targetUserId >>> 32));
        return (((((((((((((result * 31) + (this.targetUserScreenName != null ? this.targetUserScreenName.hashCode() : 0)) * 31) + (this.sourceBlockingTarget ? 1 : 0)) * 31) + (this.sourceNotificationsEnabled ? 1 : 0)) * 31) + (this.sourceFollowingTarget ? 1 : 0)) * 31) + (this.sourceFollowedByTarget ? 1 : 0)) * 31) + ((int) (this.sourceUserId ^ (this.sourceUserId >>> 32)))) * 31) + (this.sourceUserScreenName != null ? this.sourceUserScreenName.hashCode() : 0);
    }

    public String toString() {
        return new StringBuffer().append("RelationshipJSONImpl{sourceUserId=").append(this.sourceUserId).append(", targetUserId=").append(this.targetUserId).append(", sourceUserScreenName='").append(this.sourceUserScreenName).append('\'').append(", targetUserScreenName='").append(this.targetUserScreenName).append('\'').append(", sourceFollowingTarget=").append(this.sourceFollowingTarget).append(", sourceFollowedByTarget=").append(this.sourceFollowedByTarget).append(", sourceNotificationsEnabled=").append(this.sourceNotificationsEnabled).append('}').toString();
    }
}
