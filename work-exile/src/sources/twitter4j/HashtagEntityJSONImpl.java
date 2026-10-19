package twitter4j;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class HashtagEntityJSONImpl implements HashtagEntity {
    private static final long serialVersionUID = 4068992372784813200L;
    private String text;
    private int start = -1;
    private int end = -1;

    HashtagEntityJSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        try {
            JSONArray indicesArray = json.getJSONArray("indices");
            this.start = indicesArray.getInt(0);
            this.end = indicesArray.getInt(1);
            if (!json.isNull("text")) {
                this.text = json.getString("text");
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    @Override // twitter4j.HashtagEntity
    public String getText() {
        return this.text;
    }

    @Override // twitter4j.HashtagEntity
    public int getStart() {
        return this.start;
    }

    @Override // twitter4j.HashtagEntity
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
        HashtagEntityJSONImpl that = (HashtagEntityJSONImpl) o;
        if (this.end == that.end && this.start == that.start) {
            if (this.text != null) {
                if (this.text.equals(that.text)) {
                    return true;
                }
            } else if (that.text == null) {
                return true;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int result = this.start;
        return (((result * 31) + this.end) * 31) + (this.text != null ? this.text.hashCode() : 0);
    }

    public String toString() {
        return new StringBuffer().append("HashtagEntityJSONImpl{start=").append(this.start).append(", end=").append(this.end).append(", text='").append(this.text).append('\'').append('}').toString();
    }
}
