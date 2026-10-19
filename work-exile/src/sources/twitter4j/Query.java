package twitter4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import twitter4j.internal.http.HttpParameter;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class Query implements Serializable {
    public static final String KILOMETERS = "km";
    public static final String MILES = "mi";
    public static final String MIXED = "mixed";
    public static final String POPULAR = "popular";
    public static final String RECENT = "recent";
    private static final long serialVersionUID = -8108425822233599808L;
    private String geocode;
    private String lang;
    private String locale;
    private long maxId;
    private int page;
    private String query;
    private String resultType;
    private int rpp;
    private String since;
    private long sinceId;
    private String until;

    public Query() {
        this.query = null;
        this.lang = null;
        this.locale = null;
        this.maxId = -1L;
        this.rpp = -1;
        this.page = -1;
        this.since = null;
        this.sinceId = -1L;
        this.geocode = null;
        this.until = null;
        this.resultType = null;
    }

    public Query(String query) {
        this.query = null;
        this.lang = null;
        this.locale = null;
        this.maxId = -1L;
        this.rpp = -1;
        this.page = -1;
        this.since = null;
        this.sinceId = -1L;
        this.geocode = null;
        this.until = null;
        this.resultType = null;
        this.query = query;
    }

    public String getQuery() {
        return this.query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Query query(String query) {
        setQuery(query);
        return this;
    }

    public String getLang() {
        return this.lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Query lang(String lang) {
        setLang(lang);
        return this;
    }

    public String getLocale() {
        return this.locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Query locale(String locale) {
        setLocale(locale);
        return this;
    }

    public long getMaxId() {
        return this.maxId;
    }

    public void setMaxId(long maxId) {
        this.maxId = maxId;
    }

    public Query maxId(long maxId) {
        setMaxId(maxId);
        return this;
    }

    public int getRpp() {
        return this.rpp;
    }

    public void setRpp(int rpp) {
        this.rpp = rpp;
    }

    public Query rpp(int rpp) {
        setRpp(rpp);
        return this;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Query page(int page) {
        setPage(page);
        return this;
    }

    public String getSince() {
        return this.since;
    }

    public void setSince(String since) {
        this.since = since;
    }

    public Query since(String since) {
        setSince(since);
        return this;
    }

    public long getSinceId() {
        return this.sinceId;
    }

    public void setSinceId(long sinceId) {
        this.sinceId = sinceId;
    }

    public Query sinceId(long sinceId) {
        setSinceId(sinceId);
        return this;
    }

    public String getGeocode() {
        return this.geocode;
    }

    public void setGeoCode(GeoLocation location, double radius, String unit) {
        this.geocode = new StringBuffer().append(location.getLatitude()).append(",").append(location.getLongitude()).append(",").append(radius).append(unit).toString();
    }

    public Query geoCode(GeoLocation location, double radius, String unit) {
        setGeoCode(location, radius, unit);
        return this;
    }

    public String getUntil() {
        return this.until;
    }

    public void setUntil(String until) {
        this.until = until;
    }

    public Query until(String until) {
        setUntil(until);
        return this;
    }

    public String getResultType() {
        return this.resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public Query resultType(String resultType) {
        setResultType(resultType);
        return this;
    }

    HttpParameter[] asHttpParameterArray() {
        ArrayList<HttpParameter> params = new ArrayList<>();
        appendParameter("q", this.query, params);
        appendParameter("lang", this.lang, params);
        appendParameter("locale", this.locale, params);
        appendParameter("max_id", this.maxId, params);
        appendParameter("rpp", this.rpp, params);
        appendParameter("page", this.page, params);
        appendParameter("since", this.since, params);
        appendParameter("since_id", this.sinceId, params);
        appendParameter("geocode", this.geocode, params);
        appendParameter("until", this.until, params);
        appendParameter("result_type", this.resultType, params);
        HttpParameter[] paramArray = new HttpParameter[params.size()];
        return (HttpParameter[]) params.toArray(paramArray);
    }

    private void appendParameter(String name, String value, List<HttpParameter> params) {
        if (value != null) {
            params.add(new HttpParameter(name, value));
        }
    }

    private void appendParameter(String name, long value, List<HttpParameter> params) {
        if (0 <= value) {
            params.add(new HttpParameter(name, String.valueOf(value)));
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Query query1 = (Query) o;
        if (this.maxId == query1.maxId && this.page == query1.page && this.rpp == query1.rpp && this.sinceId == query1.sinceId) {
            if (this.geocode == null ? query1.geocode != null : !this.geocode.equals(query1.geocode)) {
                return false;
            }
            if (this.lang == null ? query1.lang != null : !this.lang.equals(query1.lang)) {
                return false;
            }
            if (this.locale == null ? query1.locale != null : !this.locale.equals(query1.locale)) {
                return false;
            }
            if (this.query == null ? query1.query != null : !this.query.equals(query1.query)) {
                return false;
            }
            if (this.since == null ? query1.since != null : !this.since.equals(query1.since)) {
                return false;
            }
            if (this.until == null ? query1.until != null : !this.until.equals(query1.until)) {
                return false;
            }
            if (this.resultType != null) {
                if (this.resultType.equals(query1.resultType)) {
                    return true;
                }
            } else if (query1.resultType == null) {
                return true;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int result = this.query != null ? this.query.hashCode() : 0;
        return (((((((((((((((((((result * 31) + (this.lang != null ? this.lang.hashCode() : 0)) * 31) + (this.locale != null ? this.locale.hashCode() : 0)) * 31) + ((int) (this.maxId ^ (this.maxId >>> 32)))) * 31) + this.rpp) * 31) + this.page) * 31) + (this.since != null ? this.since.hashCode() : 0)) * 31) + ((int) (this.sinceId ^ (this.sinceId >>> 32)))) * 31) + (this.geocode != null ? this.geocode.hashCode() : 0)) * 31) + (this.until != null ? this.until.hashCode() : 0)) * 31) + (this.resultType != null ? this.resultType.hashCode() : 0);
    }

    public String toString() {
        return new StringBuffer().append("Query{query='").append(this.query).append('\'').append(", lang='").append(this.lang).append('\'').append(", locale='").append(this.locale).append('\'').append(", maxId=").append(this.maxId).append(", rpp=").append(this.rpp).append(", page=").append(this.page).append(", since='").append(this.since).append('\'').append(", sinceId=").append(this.sinceId).append(", geocode='").append(this.geocode).append('\'').append(", until='").append(this.until).append('\'').append(", resultType='").append(this.resultType).append('\'').append('}').toString();
    }
}
