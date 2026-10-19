package twitter4j;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.internal.util.ParseUtil;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class UserMentionEntityJSONImpl implements UserMentionEntity {
    private static final long serialVersionUID = 6580431141350059702L;
    private long id;
    private String name;
    private String screenName;
    private int start = -1;
    private int end = -1;

    UserMentionEntityJSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        try {
            JSONArray indicesArray = json.getJSONArray("indices");
            this.start = indicesArray.getInt(0);
            this.end = indicesArray.getInt(1);
            if (!json.isNull("name")) {
                this.name = json.getString("name");
            }
            if (!json.isNull("screen_name")) {
                this.screenName = json.getString("screen_name");
            }
            this.id = ParseUtil.getLong("id", json);
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    @Override // twitter4j.UserMentionEntity
    public String getName() {
        return this.name;
    }

    @Override // twitter4j.UserMentionEntity
    public String getScreenName() {
        return this.screenName;
    }

    @Override // twitter4j.UserMentionEntity
    public long getId() {
        return this.id;
    }

    @Override // twitter4j.UserMentionEntity
    public int getStart() {
        return this.start;
    }

    @Override // twitter4j.UserMentionEntity
    public int getEnd() {
        return this.end;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserMentionEntityJSONImpl that = (UserMentionEntityJSONImpl) o;
        if (this.end == that.end && this.id == that.id && this.start == that.start) {
            if (this.name == null ? that.name != null : !this.name.equals(that.name)) {
                return false;
            }
            if (this.screenName != null) {
                if (this.screenName.equals(that.screenName)) {
                    return true;
                }
            } else if (that.screenName == null) {
                return true;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int result = this.start;
        return (((((((result * 31) + this.end) * 31) + (this.name != null ? this.name.hashCode() : 0)) * 31) + (this.screenName != null ? this.screenName.hashCode() : 0)) * 31) + ((int) (this.id ^ (this.id >>> 32)));
    }

    public String toString() {
        return new StringBuffer().append("UserMentionEntityJSONImpl{start=").append(this.start).append(", end=").append(this.end).append(", name='").append(this.name).append('\'').append(", screenName='").append(this.screenName).append('\'').append(", id=").append(this.id).append('}').toString();
    }
}
