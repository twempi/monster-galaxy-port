package twitter4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import twitter4j.internal.http.HttpParameter;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class GeoQuery implements Serializable {
    public static final String CITY = "city";
    public static final String NEIGHBORHOOD = "neighborhood";
    private static final long serialVersionUID = 927081526936169802L;
    private String accuracy;
    private String granularity;
    private String ip;
    private GeoLocation location;
    private int maxResults;

    public GeoQuery(GeoLocation location) {
        this.location = location;
        this.ip = null;
    }

    public GeoQuery(String ip) {
        this.ip = ip;
        this.location = null;
    }

    public GeoLocation getLocation() {
        return this.location;
    }

    public String getIp() {
        return this.ip;
    }

    public String getAccuracy() {
        return this.accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public GeoQuery accuracy(String accuracy) {
        setAccuracy(accuracy);
        return this;
    }

    public String getGranularity() {
        return this.granularity;
    }

    public void setGranularity(String granularity) {
        this.granularity = granularity;
    }

    public GeoQuery granularity(String granularity) {
        setGranularity(granularity);
        return this;
    }

    public int getMaxResults() {
        return this.maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public GeoQuery maxResults(int maxResults) {
        setMaxResults(maxResults);
        return this;
    }

    HttpParameter[] asHttpParameterArray() {
        ArrayList<HttpParameter> params = new ArrayList<>();
        if (this.location != null) {
            appendParameter("lat", this.location.getLatitude(), params);
            appendParameter("long", this.location.getLongitude(), params);
        }
        if (this.ip != null) {
            appendParameter("ip", this.ip, params);
        }
        appendParameter("accuracy", this.accuracy, params);
        appendParameter("granularity", this.granularity, params);
        appendParameter("max_results", this.maxResults, (List<HttpParameter>) params);
        HttpParameter[] paramArray = new HttpParameter[params.size()];
        return (HttpParameter[]) params.toArray(paramArray);
    }

    private void appendParameter(String name, String value, List<HttpParameter> params) {
        if (value != null) {
            params.add(new HttpParameter(name, value));
        }
    }

    private void appendParameter(String name, int value, List<HttpParameter> params) {
        if (value > 0) {
            params.add(new HttpParameter(name, String.valueOf(value)));
        }
    }

    private void appendParameter(String name, double value, List<HttpParameter> params) {
        params.add(new HttpParameter(name, String.valueOf(value)));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GeoQuery geoQuery = (GeoQuery) o;
        if (this.maxResults != geoQuery.maxResults) {
            return false;
        }
        if (this.accuracy == null ? geoQuery.accuracy != null : !this.accuracy.equals(geoQuery.accuracy)) {
            return false;
        }
        if (this.granularity == null ? geoQuery.granularity != null : !this.granularity.equals(geoQuery.granularity)) {
            return false;
        }
        if (this.ip == null ? geoQuery.ip != null : !this.ip.equals(geoQuery.ip)) {
            return false;
        }
        if (this.location != null) {
            if (this.location.equals(geoQuery.location)) {
                return true;
            }
        } else if (geoQuery.location == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.location != null ? this.location.hashCode() : 0;
        return (((((((result * 31) + (this.ip != null ? this.ip.hashCode() : 0)) * 31) + (this.accuracy != null ? this.accuracy.hashCode() : 0)) * 31) + (this.granularity != null ? this.granularity.hashCode() : 0)) * 31) + this.maxResults;
    }

    public String toString() {
        return new StringBuffer().append("GeoQuery{location=").append(this.location).append(", ip='").append(this.ip).append('\'').append(", accuracy='").append(this.accuracy).append('\'').append(", granularity='").append(this.granularity).append('\'').append(", maxResults=").append(this.maxResults).append('}').toString();
    }
}
