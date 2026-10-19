package twitter4j;

import com.sessionm.ui.SessionMActivity;
import java.io.Serializable;
import org.json.JSONObject;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.util.ParseUtil;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class TrendJSONImpl extends TwitterResponseImpl implements Trend, Serializable {
    private static final long serialVersionUID = 1925956704460743946L;
    private String name;
    private String query;
    private String url;

    TrendJSONImpl(HttpResponse res, JSONObject json, boolean storeJSON) {
        super(res);
        this.url = null;
        this.query = null;
        this.name = ParseUtil.getRawString("name", json);
        this.url = ParseUtil.getRawString(SessionMActivity.INTENT_PARAM_URL, json);
        this.query = ParseUtil.getRawString("query", json);
    }

    TrendJSONImpl(JSONObject json, Configuration conf) {
        this(null, json, conf.isJSONStoreEnabled());
    }

    TrendJSONImpl(JSONObject json, boolean storeJSON) {
        this(null, json, storeJSON);
    }

    TrendJSONImpl(JSONObject json) {
        this(null, json, false);
    }

    @Override // twitter4j.Trend
    public String getName() {
        return this.name;
    }

    @Override // twitter4j.Trend
    public String getUrl() {
        return this.url;
    }

    @Override // twitter4j.Trend
    public String getQuery() {
        return this.query;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trend)) {
            return false;
        }
        Trend trend = (Trend) o;
        if (!this.name.equals(trend.getName())) {
            return false;
        }
        if (this.query == null ? trend.getQuery() != null : !this.query.equals(trend.getQuery())) {
            return false;
        }
        if (this.url != null) {
            if (this.url.equals(trend.getUrl())) {
                return true;
            }
        } else if (trend.getUrl() == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.name.hashCode();
        return (((result * 31) + (this.url != null ? this.url.hashCode() : 0)) * 31) + (this.query != null ? this.query.hashCode() : 0);
    }

    public String toString() {
        return new StringBuffer().append("TrendJSONImpl{name='").append(this.name).append('\'').append(", url='").append(this.url).append('\'').append(", query='").append(this.query).append('\'').append('}').toString();
    }
}
