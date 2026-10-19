package twitter4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.DataObjectFactoryUtil;
import twitter4j.internal.util.ParseUtil;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class QueryResultJSONImpl implements QueryResult, Serializable {
    private static final long serialVersionUID = -9059136565234613286L;
    private double completedIn;
    private long maxId;
    private int page;
    private String query;
    private String refreshUrl;
    private int resultsPerPage;
    private long sinceId;
    private List<Tweet> tweets;
    private String warning;

    QueryResultJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        JSONObject json = res.asJSONObject();
        try {
            this.sinceId = ParseUtil.getLong("since_id", json);
            this.maxId = ParseUtil.getLong("max_id", json);
            this.refreshUrl = ParseUtil.getUnescapedString("refresh_url", json);
            this.resultsPerPage = ParseUtil.getInt("results_per_page", json);
            this.warning = ParseUtil.getRawString("warning", json);
            this.completedIn = ParseUtil.getDouble("completed_in", json);
            this.page = ParseUtil.getInt("page", json);
            this.query = ParseUtil.getURLDecodedString("query", json);
            JSONArray array = json.getJSONArray("results");
            this.tweets = new ArrayList(array.length());
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            for (int i = 0; i < array.length(); i++) {
                JSONObject tweet = array.getJSONObject(i);
                this.tweets.add(new TweetJSONImpl(tweet, conf));
            }
        } catch (JSONException jsone) {
            throw new TwitterException(new StringBuffer().append(jsone.getMessage()).append(":").append(json.toString()).toString(), jsone);
        }
    }

    QueryResultJSONImpl(Query query) {
        this.sinceId = query.getSinceId();
        this.resultsPerPage = query.getRpp();
        this.page = query.getPage();
        this.tweets = new ArrayList(0);
    }

    @Override // twitter4j.QueryResult
    public long getSinceId() {
        return this.sinceId;
    }

    @Override // twitter4j.QueryResult
    public long getMaxId() {
        return this.maxId;
    }

    @Override // twitter4j.QueryResult
    public String getRefreshUrl() {
        return this.refreshUrl;
    }

    @Override // twitter4j.QueryResult
    public int getResultsPerPage() {
        return this.resultsPerPage;
    }

    @Override // twitter4j.QueryResult
    public String getWarning() {
        return this.warning;
    }

    @Override // twitter4j.QueryResult
    public double getCompletedIn() {
        return this.completedIn;
    }

    @Override // twitter4j.QueryResult
    public int getPage() {
        return this.page;
    }

    @Override // twitter4j.QueryResult
    public String getQuery() {
        return this.query;
    }

    @Override // twitter4j.QueryResult
    public List<Tweet> getTweets() {
        return this.tweets;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QueryResult that = (QueryResult) o;
        if (Double.compare(that.getCompletedIn(), this.completedIn) == 0 && this.maxId == that.getMaxId() && this.page == that.getPage() && this.resultsPerPage == that.getResultsPerPage() && this.sinceId == that.getSinceId() && this.query.equals(that.getQuery())) {
            if (this.refreshUrl == null ? that.getRefreshUrl() != null : !this.refreshUrl.equals(that.getRefreshUrl())) {
                return false;
            }
            if (this.tweets == null ? that.getTweets() != null : !this.tweets.equals(that.getTweets())) {
                return false;
            }
            if (this.warning != null) {
                if (this.warning.equals(that.getWarning())) {
                    return true;
                }
            } else if (that.getWarning() == null) {
                return true;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int result = (int) (this.sinceId ^ (this.sinceId >>> 32));
        int result2 = (((((((result * 31) + ((int) (this.maxId ^ (this.maxId >>> 32)))) * 31) + (this.refreshUrl != null ? this.refreshUrl.hashCode() : 0)) * 31) + this.resultsPerPage) * 31) + (this.warning != null ? this.warning.hashCode() : 0);
        long temp = this.completedIn != 0.0d ? Double.doubleToLongBits(this.completedIn) : 0L;
        return (((((((result2 * 31) + ((int) ((temp >>> 32) ^ temp))) * 31) + this.page) * 31) + this.query.hashCode()) * 31) + (this.tweets != null ? this.tweets.hashCode() : 0);
    }

    public String toString() {
        return new StringBuffer().append("QueryResultJSONImpl{sinceId=").append(this.sinceId).append(", maxId=").append(this.maxId).append(", refreshUrl='").append(this.refreshUrl).append('\'').append(", resultsPerPage=").append(this.resultsPerPage).append(", warning='").append(this.warning).append('\'').append(", completedIn=").append(this.completedIn).append(", page=").append(this.page).append(", query='").append(this.query).append('\'').append(", tweets=").append(this.tweets).append('}').toString();
    }
}
