package twitter4j;

import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.DataObjectFactoryUtil;
import twitter4j.internal.util.ParseUtil;

/* JADX INFO: loaded from: classes.dex */
final class SavedSearchJSONImpl extends TwitterResponseImpl implements SavedSearch {
    private static final long serialVersionUID = 3083819860391598212L;
    private Date createdAt;
    private int id;
    private String name;
    private int position;
    private String query;

    @Override // java.lang.Comparable
    public int compareTo(SavedSearch savedSearch) {
        return compareTo2(savedSearch);
    }

    SavedSearchJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
        }
        JSONObject json = res.asJSONObject();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.registerJSONObject(this, json);
        }
    }

    SavedSearchJSONImpl(JSONObject savedSearch) throws TwitterException {
        init(savedSearch);
    }

    static ResponseList<SavedSearch> createSavedSearchList(HttpResponse res, Configuration conf) throws Throwable {
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
        }
        JSONArray json = res.asJSONArray();
        try {
            ResponseList<SavedSearch> savedSearches = new ResponseListImpl<>(json.length(), res);
            for (int i = 0; i < json.length(); i++) {
                JSONObject savedSearchesJSON = json.getJSONObject(i);
                SavedSearchJSONImpl savedSearchJSONImpl = new SavedSearchJSONImpl(savedSearchesJSON);
                savedSearches.add(savedSearchJSONImpl);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(savedSearchJSONImpl, savedSearchesJSON);
                }
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(savedSearches, json);
            }
            return savedSearches;
        } catch (JSONException jsone) {
            throw new TwitterException(new StringBuffer().append(jsone.getMessage()).append(":").append(res.asString()).toString(), jsone);
        }
    }

    private void init(JSONObject savedSearch) throws TwitterException {
        this.createdAt = ParseUtil.getDate("created_at", savedSearch, "EEE MMM dd HH:mm:ss z yyyy");
        this.query = ParseUtil.getUnescapedString("query", savedSearch);
        this.position = ParseUtil.getInt("position", savedSearch);
        this.name = ParseUtil.getUnescapedString("name", savedSearch);
        this.id = ParseUtil.getInt("id", savedSearch);
    }

    /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
    public int compareTo2(SavedSearch that) {
        return this.id - that.getId();
    }

    @Override // twitter4j.SavedSearch
    public Date getCreatedAt() {
        return this.createdAt;
    }

    @Override // twitter4j.SavedSearch
    public String getQuery() {
        return this.query;
    }

    @Override // twitter4j.SavedSearch
    public int getPosition() {
        return this.position;
    }

    @Override // twitter4j.SavedSearch
    public String getName() {
        return this.name;
    }

    @Override // twitter4j.SavedSearch
    public int getId() {
        return this.id;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SavedSearch)) {
            return false;
        }
        SavedSearch that = (SavedSearch) o;
        return this.id == that.getId();
    }

    public int hashCode() {
        int result = this.createdAt.hashCode();
        return (((((((result * 31) + this.query.hashCode()) * 31) + this.position) * 31) + this.name.hashCode()) * 31) + this.id;
    }

    public String toString() {
        return new StringBuffer().append("SavedSearchJSONImpl{createdAt=").append(this.createdAt).append(", query='").append(this.query).append('\'').append(", position=").append(this.position).append(", name='").append(this.name).append('\'').append(", id=").append(this.id).append('}').toString();
    }
}
