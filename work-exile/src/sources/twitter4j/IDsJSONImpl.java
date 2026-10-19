package twitter4j;

import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.DataObjectFactoryUtil;
import twitter4j.internal.util.ParseUtil;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class IDsJSONImpl extends TwitterResponseImpl implements IDs {
    private static final long serialVersionUID = -6585026560164704953L;
    private long[] ids;
    private long nextCursor;
    private long previousCursor;

    IDsJSONImpl(HttpResponse res, Configuration conf) throws Throwable {
        super(res);
        this.previousCursor = -1L;
        this.nextCursor = -1L;
        String json = res.asString();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(this, json);
        }
    }

    IDsJSONImpl(String json) throws TwitterException {
        this.previousCursor = -1L;
        this.nextCursor = -1L;
        init(json);
    }

    private void init(String jsonStr) throws TwitterException {
        try {
            if (jsonStr.startsWith("{")) {
                JSONObject json = new JSONObject(jsonStr);
                JSONArray idList = json.getJSONArray("ids");
                this.ids = new long[idList.length()];
                for (int i = 0; i < idList.length(); i++) {
                    try {
                        this.ids[i] = Long.parseLong(idList.getString(i));
                    } catch (NumberFormatException nfe) {
                        throw new TwitterException(new StringBuffer().append("Twitter API returned malformed response: ").append(json).toString(), nfe);
                    }
                }
                this.previousCursor = ParseUtil.getLong("previous_cursor", json);
                this.nextCursor = ParseUtil.getLong("next_cursor", json);
                return;
            }
            JSONArray idList2 = new JSONArray(jsonStr);
            this.ids = new long[idList2.length()];
            for (int i2 = 0; i2 < idList2.length(); i2++) {
                try {
                    this.ids[i2] = Long.parseLong(idList2.getString(i2));
                } catch (NumberFormatException nfe2) {
                    throw new TwitterException(new StringBuffer().append("Twitter API returned malformed response: ").append(idList2).toString(), nfe2);
                }
            }
            return;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
        throw new TwitterException(jsone);
    }

    @Override // twitter4j.IDs
    public long[] getIDs() {
        return this.ids;
    }

    @Override // twitter4j.IDs, twitter4j.CursorSupport
    public boolean hasPrevious() {
        return 0 != this.previousCursor;
    }

    @Override // twitter4j.IDs, twitter4j.CursorSupport
    public long getPreviousCursor() {
        return this.previousCursor;
    }

    @Override // twitter4j.IDs, twitter4j.CursorSupport
    public boolean hasNext() {
        return 0 != this.nextCursor;
    }

    @Override // twitter4j.IDs, twitter4j.CursorSupport
    public long getNextCursor() {
        return this.nextCursor;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IDs)) {
            return false;
        }
        IDs iDs = (IDs) o;
        return Arrays.equals(this.ids, iDs.getIDs());
    }

    public int hashCode() {
        if (this.ids != null) {
            return Arrays.hashCode(this.ids);
        }
        return 0;
    }

    public String toString() {
        return new StringBuffer().append("IDsJSONImpl{ids=").append(Arrays.toString(this.ids)).append(", previousCursor=").append(this.previousCursor).append(", nextCursor=").append(this.nextCursor).append('}').toString();
    }
}
