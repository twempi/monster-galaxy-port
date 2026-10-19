package twitter4j;

import com.sessionm.ui.SessionMActivity;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.internal.util.ParseUtil;

/* JADX INFO: loaded from: classes.dex */
public class MediaEntityJSONImpl implements MediaEntity {
    private static final long serialVersionUID = -5196438049688518593L;
    private String displayURL;
    private int end;
    private URL expandedURL;
    private long id;
    private URL mediaURL;
    private URL mediaURLHttps;
    private Map<Integer, MediaEntity.Size> sizes;
    private int start;
    private URL url;

    public MediaEntityJSONImpl(JSONObject json) throws TwitterException {
        this.start = -1;
        this.end = -1;
        try {
            JSONArray indicesArray = json.getJSONArray("indices");
            this.start = indicesArray.getInt(0);
            this.end = indicesArray.getInt(1);
            this.id = ParseUtil.getLong("id", json);
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
            if (!json.isNull("media_url")) {
                try {
                    this.mediaURL = new URL(json.getString("media_url"));
                } catch (MalformedURLException e3) {
                }
            }
            if (!json.isNull("media_url_https")) {
                try {
                    this.mediaURLHttps = new URL(json.getString("media_url_https"));
                } catch (MalformedURLException e4) {
                }
            }
            if (!json.isNull("display_url")) {
                this.displayURL = json.getString("display_url");
            }
            JSONObject sizes = json.getJSONObject("sizes");
            this.sizes = new HashMap(4);
            this.sizes.put(MediaEntity.Size.LARGE, new Size(sizes.getJSONObject("large")));
            this.sizes.put(MediaEntity.Size.MEDIUM, new Size(sizes.getJSONObject("medium")));
            this.sizes.put(MediaEntity.Size.SMALL, new Size(sizes.getJSONObject("small")));
            this.sizes.put(MediaEntity.Size.THUMB, new Size(sizes.getJSONObject("thumb")));
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    @Override // twitter4j.MediaEntity
    public long getId() {
        return this.id;
    }

    @Override // twitter4j.MediaEntity
    public URL getMediaURL() {
        return this.mediaURL;
    }

    @Override // twitter4j.MediaEntity
    public URL getMediaURLHttps() {
        return this.mediaURLHttps;
    }

    @Override // twitter4j.URLEntity
    public URL getURL() {
        return this.url;
    }

    @Override // twitter4j.URLEntity
    public String getDisplayURL() {
        return this.displayURL;
    }

    @Override // twitter4j.URLEntity
    public URL getExpandedURL() {
        return this.expandedURL;
    }

    @Override // twitter4j.MediaEntity
    public Map<Integer, MediaEntity.Size> getSizes() {
        return this.sizes;
    }

    @Override // twitter4j.URLEntity
    public int getStart() {
        return this.start;
    }

    @Override // twitter4j.URLEntity
    public int getEnd() {
        return this.end;
    }

    static class Size implements MediaEntity.Size {
        private static final long serialVersionUID = 8681853416159361581L;
        int height;
        int resize;
        int width;

        Size(JSONObject json) throws JSONException {
            this.width = json.getInt("w");
            this.height = json.getInt("h");
            this.resize = "fit".equals(json.getString("resize")) ? 100 : 101;
        }

        @Override // twitter4j.MediaEntity.Size
        public int getWidth() {
            return this.width;
        }

        @Override // twitter4j.MediaEntity.Size
        public int getHeight() {
            return this.height;
        }

        @Override // twitter4j.MediaEntity.Size
        public int getResize() {
            return this.resize;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Size)) {
                return false;
            }
            Size size = (Size) o;
            return this.height == size.height && this.resize == size.resize && this.width == size.width;
        }

        public int hashCode() {
            int result = this.width;
            return (((result * 31) + this.height) * 31) + this.resize;
        }

        public String toString() {
            return new StringBuffer().append("Size{width=").append(this.width).append(", height=").append(this.height).append(", resize=").append(this.resize).append('}').toString();
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MediaEntityJSONImpl)) {
            return false;
        }
        MediaEntityJSONImpl that = (MediaEntityJSONImpl) o;
        return this.id == that.id;
    }

    public int hashCode() {
        return (int) (this.id ^ (this.id >>> 32));
    }

    public String toString() {
        return new StringBuffer().append("MediaEntityJSONImpl{id=").append(this.id).append(", start=").append(this.start).append(", end=").append(this.end).append(", url=").append(this.url).append(", mediaURL=").append(this.mediaURL).append(", mediaURLHttps=").append(this.mediaURLHttps).append(", expandedURL=").append(this.expandedURL).append(", displayURL='").append(this.displayURL).append('\'').append(", sizes=").append(this.sizes).append('}').toString();
    }
}
