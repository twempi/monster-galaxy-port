package twitter4j;

import com.sessionm.ui.SessionMActivity;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class URLEntityJSONImpl implements URLEntity {
    private static final long serialVersionUID = 1165188478018146676L;
    private String displayURL;
    private URL expandedURL;
    private URL url;
    private int start = -1;
    private int end = -1;

    URLEntityJSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        try {
            JSONArray indicesArray = json.getJSONArray("indices");
            this.start = indicesArray.getInt(0);
            this.end = indicesArray.getInt(1);
            try {
                this.url = new URL(json.getString(SessionMActivity.INTENT_PARAM_URL));
            } catch (MalformedURLException e) {
            }
            if (!json.isNull("expanded_url")) {
                try {
                    this.expandedURL = new URL(json.getString("expanded_url"));
                } catch (MalformedURLException e2) {
                }
            }
            if (!json.isNull("display_url")) {
                this.displayURL = json.getString("display_url");
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    @Override // twitter4j.URLEntity
    public URL getURL() {
        return this.url;
    }

    @Override // twitter4j.URLEntity
    public URL getExpandedURL() {
        return this.expandedURL;
    }

    @Override // twitter4j.URLEntity
    public String getDisplayURL() {
        return this.displayURL;
    }

    @Override // twitter4j.URLEntity
    public int getStart() {
        return this.start;
    }

    @Override // twitter4j.URLEntity
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
        URLEntityJSONImpl that = (URLEntityJSONImpl) o;
        if (this.end == that.end && this.start == that.start) {
            if (this.displayURL == null ? that.displayURL != null : !this.displayURL.equals(that.displayURL)) {
                return false;
            }
            if (this.expandedURL == null ? that.expandedURL != null : !this.expandedURL.equals(that.expandedURL)) {
                return false;
            }
            if (this.url != null) {
                if (this.url.equals(that.url)) {
                    return true;
                }
            } else if (that.url == null) {
                return true;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int result = this.start;
        return (((((((result * 31) + this.end) * 31) + (this.url != null ? this.url.hashCode() : 0)) * 31) + (this.expandedURL != null ? this.expandedURL.hashCode() : 0)) * 31) + (this.displayURL != null ? this.displayURL.hashCode() : 0);
    }

    public String toString() {
        return new StringBuffer().append("URLEntityJSONImpl{start=").append(this.start).append(", end=").append(this.end).append(", url=").append(this.url).append(", expandedURL=").append(this.expandedURL).append(", displayURL=").append(this.displayURL).append('}').toString();
    }
}
