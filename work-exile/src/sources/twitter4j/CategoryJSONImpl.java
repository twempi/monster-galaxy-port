package twitter4j;

import com.tapjoy.TapjoyConstants;
import java.io.Serializable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.DataObjectFactoryUtil;
import twitter4j.internal.util.ParseUtil;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class CategoryJSONImpl implements Category, Serializable {
    private static final long serialVersionUID = -6703617743623288566L;
    private String name;
    private int size;
    private String slug;

    CategoryJSONImpl(JSONObject json) throws JSONException {
        init(json);
    }

    void init(JSONObject json) throws JSONException {
        this.name = json.getString("name");
        this.slug = json.getString("slug");
        this.size = ParseUtil.getInt(TapjoyConstants.TJC_DISPLAY_AD_SIZE, json);
    }

    static ResponseList<Category> createCategoriesList(HttpResponse res, Configuration conf) throws TwitterException {
        return createCategoriesList(res.asJSONArray(), res, conf);
    }

    static ResponseList<Category> createCategoriesList(JSONArray array, HttpResponse res, Configuration conf) throws TwitterException {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            ResponseList<Category> categories = new ResponseListImpl<>(array.length(), res);
            for (int i = 0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);
                CategoryJSONImpl categoryJSONImpl = new CategoryJSONImpl(json);
                categories.add(categoryJSONImpl);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(categoryJSONImpl, json);
                }
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(categories, array);
            }
            return categories;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    @Override // twitter4j.Category
    public String getName() {
        return this.name;
    }

    @Override // twitter4j.Category
    public String getSlug() {
        return this.slug;
    }

    @Override // twitter4j.Category
    public int getSize() {
        return this.size;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CategoryJSONImpl that = (CategoryJSONImpl) o;
        if (this.size != that.size) {
            return false;
        }
        if (this.name == null ? that.name != null : !this.name.equals(that.name)) {
            return false;
        }
        if (this.slug != null) {
            if (this.slug.equals(that.slug)) {
                return true;
            }
        } else if (that.slug == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.name != null ? this.name.hashCode() : 0;
        return (((result * 31) + (this.slug != null ? this.slug.hashCode() : 0)) * 31) + this.size;
    }

    public String toString() {
        return new StringBuffer().append("CategoryJSONImpl{name='").append(this.name).append('\'').append(", slug='").append(this.slug).append('\'').append(", size=").append(this.size).append('}').toString();
    }
}
