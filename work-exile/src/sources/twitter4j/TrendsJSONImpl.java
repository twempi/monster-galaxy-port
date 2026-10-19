package twitter4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.DataObjectFactoryUtil;
import twitter4j.internal.util.ParseUtil;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class TrendsJSONImpl implements Trends, Serializable {
    private static final long serialVersionUID = -7151479143843312309L;
    private Date asOf;
    private Location location;
    private Date trendAt;
    private Trend[] trends;

    @Override // java.lang.Comparable
    public int compareTo(Trends trends) {
        return compareTo2(trends);
    }

    /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
    public int compareTo2(Trends that) {
        return this.trendAt.compareTo(that.getTrendAt());
    }

    TrendsJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        this(res.asString(), conf.isJSONStoreEnabled());
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(this, res.asString());
        }
    }

    TrendsJSONImpl(String jsonStr) throws TwitterException {
        this(jsonStr, false);
    }

    TrendsJSONImpl(String jsonStr, boolean storeJSON) throws TwitterException {
        init(jsonStr, storeJSON);
    }

    void init(String jsonStr, boolean storeJSON) throws TwitterException {
        JSONObject json;
        try {
            if (jsonStr.startsWith("[")) {
                JSONArray array = new JSONArray(jsonStr);
                if (array.length() > 0) {
                    json = array.getJSONObject(0);
                } else {
                    throw new TwitterException("No trends found on the specified woeid");
                }
            } else {
                json = new JSONObject(jsonStr);
            }
            this.asOf = parseTrendsDate(json.getString("as_of"));
            this.location = extractLocation(json, storeJSON);
            JSONArray array2 = json.getJSONArray("trends");
            this.trendAt = this.asOf;
            this.trends = jsonArrayToTrendArray(array2, storeJSON);
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage(), jsone);
        }
    }

    TrendsJSONImpl(Date asOf, Location location, Date trendAt, Trend[] trends) {
        this.asOf = asOf;
        this.location = location;
        this.trendAt = trendAt;
        this.trends = trends;
    }

    static List<Trends> createTrendsList(HttpResponse res, boolean storeJSON) throws TwitterException {
        JSONObject json = res.asJSONObject();
        try {
            Date asOf = parseTrendsDate(json.getString("as_of"));
            JSONObject trendsJson = json.getJSONObject("trends");
            Location location = extractLocation(json, storeJSON);
            List<Trends> trends = new ArrayList<>(trendsJson.length());
            Iterator<String> itKeys = trendsJson.keys();
            while (itKeys.hasNext()) {
                String key = itKeys.next();
                JSONArray array = trendsJson.getJSONArray(key);
                Trend[] trendsArray = jsonArrayToTrendArray(array, storeJSON);
                if (key.length() == 19) {
                    trends.add(new TrendsJSONImpl(asOf, location, ParseUtil.getDate(key, "yyyy-MM-dd HH:mm:ss"), trendsArray));
                } else if (key.length() == 16) {
                    trends.add(new TrendsJSONImpl(asOf, location, ParseUtil.getDate(key, "yyyy-MM-dd HH:mm"), trendsArray));
                } else if (key.length() == 10) {
                    trends.add(new TrendsJSONImpl(asOf, location, ParseUtil.getDate(key, "yyyy-MM-dd"), trendsArray));
                }
            }
            Collections.sort(trends);
            return trends;
        } catch (JSONException jsone) {
            throw new TwitterException(new StringBuffer().append(jsone.getMessage()).append(":").append(res.asString()).toString(), jsone);
        }
    }

    private static Location extractLocation(JSONObject json, boolean storeJSON) throws TwitterException {
        if (json.isNull("locations")) {
            return null;
        }
        try {
            ResponseList<Location> locations = LocationJSONImpl.createLocationList(json.getJSONArray("locations"), storeJSON);
            if (locations.size() != 0) {
                return locations.get(0);
            }
            return null;
        } catch (JSONException e) {
            throw new AssertionError("locations can't be null");
        }
    }

    private static Date parseTrendsDate(String asOfStr) throws TwitterException {
        switch (asOfStr.length()) {
            case 10:
                Date parsed = new Date(Long.parseLong(asOfStr) * 1000);
                return parsed;
            case 20:
                Date parsed2 = ParseUtil.getDate(asOfStr, "yyyy-mm-dd'T'HH:mm:ss'Z'");
                return parsed2;
            default:
                Date parsed3 = ParseUtil.getDate(asOfStr, "EEE, d MMM yyyy HH:mm:ss z");
                return parsed3;
        }
    }

    private static Trend[] jsonArrayToTrendArray(JSONArray array, boolean storeJSON) throws JSONException {
        Trend[] trends = new Trend[array.length()];
        for (int i = 0; i < array.length(); i++) {
            JSONObject trend = array.getJSONObject(i);
            trends[i] = new TrendJSONImpl(trend, storeJSON);
        }
        return trends;
    }

    @Override // twitter4j.Trends
    public Trend[] getTrends() {
        return this.trends;
    }

    @Override // twitter4j.Trends
    public Location getLocation() {
        return this.location;
    }

    @Override // twitter4j.Trends
    public Date getAsOf() {
        return this.asOf;
    }

    @Override // twitter4j.Trends
    public Date getTrendAt() {
        return this.trendAt;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trends)) {
            return false;
        }
        Trends trends1 = (Trends) o;
        if (this.asOf == null ? trends1.getAsOf() != null : !this.asOf.equals(trends1.getAsOf())) {
            return false;
        }
        if (this.trendAt == null ? trends1.getTrendAt() != null : !this.trendAt.equals(trends1.getTrendAt())) {
            return false;
        }
        return Arrays.equals(this.trends, trends1.getTrends());
    }

    public int hashCode() {
        int result = this.asOf != null ? this.asOf.hashCode() : 0;
        return (((result * 31) + (this.trendAt != null ? this.trendAt.hashCode() : 0)) * 31) + (this.trends != null ? Arrays.hashCode(this.trends) : 0);
    }

    public String toString() {
        return new StringBuffer().append("TrendsJSONImpl{asOf=").append(this.asOf).append(", trendAt=").append(this.trendAt).append(", trends=").append(this.trends == null ? null : Arrays.asList(this.trends)).append('}').toString();
    }
}
