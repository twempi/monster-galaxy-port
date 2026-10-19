package twitter4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.DataObjectFactoryUtil;
import twitter4j.internal.util.ParseUtil;

/* JADX INFO: loaded from: classes.dex */
class TwitterAPIConfigurationJSONImpl extends TwitterResponseImpl implements TwitterAPIConfiguration {
    private static final long serialVersionUID = 5786291660087491465L;
    private int charactersReservedPerMedia;
    private int maxMediaPerUpload;
    private String[] nonUsernamePaths;
    private int photoSizeLimit;
    private Map<Integer, MediaEntity.Size> photoSizes;
    private int shortURLLength;
    private int shortURLLengthHttps;

    TwitterAPIConfigurationJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        JSONObject medium;
        super(res);
        try {
            JSONObject json = res.asJSONObject();
            this.photoSizeLimit = ParseUtil.getInt("photo_size_limit", json);
            this.shortURLLength = ParseUtil.getInt("short_url_length", json);
            this.shortURLLengthHttps = ParseUtil.getInt("short_url_length_https", json);
            this.charactersReservedPerMedia = ParseUtil.getInt("characters_reserved_per_media", json);
            JSONObject sizes = json.getJSONObject("photo_sizes");
            this.photoSizes = new HashMap(4);
            this.photoSizes.put(MediaEntity.Size.LARGE, new MediaEntityJSONImpl.Size(sizes.getJSONObject("large")));
            if (sizes.isNull("med")) {
                medium = sizes.getJSONObject("medium");
            } else {
                medium = sizes.getJSONObject("med");
            }
            this.photoSizes.put(MediaEntity.Size.MEDIUM, new MediaEntityJSONImpl.Size(medium));
            this.photoSizes.put(MediaEntity.Size.SMALL, new MediaEntityJSONImpl.Size(sizes.getJSONObject("small")));
            this.photoSizes.put(MediaEntity.Size.THUMB, new MediaEntityJSONImpl.Size(sizes.getJSONObject("thumb")));
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
                DataObjectFactoryUtil.registerJSONObject(this, res.asJSONObject());
            }
            JSONArray nonUsernamePathsJSONArray = json.getJSONArray("non_username_paths");
            this.nonUsernamePaths = new String[nonUsernamePathsJSONArray.length()];
            for (int i = 0; i < nonUsernamePathsJSONArray.length(); i++) {
                this.nonUsernamePaths[i] = nonUsernamePathsJSONArray.getString(i);
            }
            this.maxMediaPerUpload = ParseUtil.getInt("max_media_per_upload", json);
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    @Override // twitter4j.TwitterAPIConfiguration
    public int getPhotoSizeLimit() {
        return this.photoSizeLimit;
    }

    @Override // twitter4j.TwitterAPIConfiguration
    public int getShortURLLength() {
        return this.shortURLLength;
    }

    @Override // twitter4j.TwitterAPIConfiguration
    public int getShortURLLengthHttps() {
        return this.shortURLLengthHttps;
    }

    @Override // twitter4j.TwitterAPIConfiguration
    public int getCharactersReservedPerMedia() {
        return this.charactersReservedPerMedia;
    }

    @Override // twitter4j.TwitterAPIConfiguration
    public Map<Integer, MediaEntity.Size> getPhotoSizes() {
        return this.photoSizes;
    }

    @Override // twitter4j.TwitterAPIConfiguration
    public String[] getNonUsernamePaths() {
        return this.nonUsernamePaths;
    }

    @Override // twitter4j.TwitterAPIConfiguration
    public int getMaxMediaPerUpload() {
        return this.maxMediaPerUpload;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TwitterAPIConfigurationJSONImpl)) {
            return false;
        }
        TwitterAPIConfigurationJSONImpl that = (TwitterAPIConfigurationJSONImpl) o;
        if (this.charactersReservedPerMedia == that.charactersReservedPerMedia && this.maxMediaPerUpload == that.maxMediaPerUpload && this.photoSizeLimit == that.photoSizeLimit && this.shortURLLength == that.shortURLLength && this.shortURLLengthHttps == that.shortURLLengthHttps && Arrays.equals(this.nonUsernamePaths, that.nonUsernamePaths)) {
            if (this.photoSizes != null) {
                if (this.photoSizes.equals(that.photoSizes)) {
                    return true;
                }
            } else if (that.photoSizes == null) {
                return true;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int result = this.photoSizeLimit;
        return (((((((((((result * 31) + this.shortURLLength) * 31) + this.shortURLLengthHttps) * 31) + this.charactersReservedPerMedia) * 31) + (this.photoSizes != null ? this.photoSizes.hashCode() : 0)) * 31) + (this.nonUsernamePaths != null ? Arrays.hashCode(this.nonUsernamePaths) : 0)) * 31) + this.maxMediaPerUpload;
    }

    public String toString() {
        return new StringBuffer().append("TwitterAPIConfigurationJSONImpl{photoSizeLimit=").append(this.photoSizeLimit).append(", shortURLLength=").append(this.shortURLLength).append(", shortURLLengthHttps=").append(this.shortURLLengthHttps).append(", charactersReservedPerMedia=").append(this.charactersReservedPerMedia).append(", photoSizes=").append(this.photoSizes).append(", nonUsernamePaths=").append(this.nonUsernamePaths == null ? null : Arrays.asList(this.nonUsernamePaths)).append(", maxMediaPerUpload=").append(this.maxMediaPerUpload).append('}').toString();
    }
}
