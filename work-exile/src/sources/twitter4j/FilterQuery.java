package twitter4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import twitter4j.internal.http.HttpParameter;
import twitter4j.internal.util.T4JInternalStringUtil;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class FilterQuery implements Serializable {
    private static final long serialVersionUID = 430966623248982833L;
    private int count;
    private long[] follow;
    private boolean includeEntities;
    private double[][] locations;
    private String[] track;

    public FilterQuery() {
        this.count = 0;
        this.follow = null;
        this.track = null;
        this.locations = (double[][]) null;
    }

    public FilterQuery(long[] follow) {
        this();
        this.count = 0;
        this.follow = follow;
    }

    public FilterQuery(int count, long[] follow) {
        this();
        this.count = count;
        this.follow = follow;
    }

    public FilterQuery(int count, long[] follow, String[] track) {
        this();
        this.count = count;
        this.follow = follow;
        this.track = track;
    }

    public FilterQuery(int count, long[] follow, String[] track, double[][] locations) {
        this.count = count;
        this.follow = follow;
        this.track = track;
        this.locations = locations;
    }

    public FilterQuery count(int count) {
        this.count = count;
        return this;
    }

    public FilterQuery follow(long[] follow) {
        this.follow = follow;
        return this;
    }

    public FilterQuery track(String[] track) {
        this.track = track;
        return this;
    }

    public FilterQuery locations(double[][] locations) {
        this.locations = locations;
        return this;
    }

    public FilterQuery setIncludeEntities(boolean include) {
        this.includeEntities = include;
        return this;
    }

    HttpParameter[] asHttpParameterArray() {
        ArrayList<HttpParameter> params = new ArrayList<>();
        params.add(new HttpParameter("count", this.count));
        if (this.follow != null && this.follow.length > 0) {
            params.add(new HttpParameter("follow", T4JInternalStringUtil.join(this.follow)));
        }
        if (this.track != null && this.track.length > 0) {
            params.add(new HttpParameter("track", T4JInternalStringUtil.join(this.track)));
        }
        if (this.locations != null && this.locations.length > 0) {
            params.add(new HttpParameter("locations", toLocationsString(this.locations)));
        }
        if (this.includeEntities) {
            params.add(new HttpParameter("include_entities", true));
        }
        HttpParameter[] paramArray = new HttpParameter[params.size()];
        return (HttpParameter[]) params.toArray(paramArray);
    }

    private String toLocationsString(double[][] keywords) {
        StringBuffer buf = new StringBuffer(keywords.length * 20 * 2);
        for (double[] keyword : keywords) {
            if (buf.length() != 0) {
                buf.append(",");
            }
            buf.append(keyword[0]);
            buf.append(",");
            buf.append(keyword[1]);
        }
        return buf.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FilterQuery that = (FilterQuery) o;
        return this.count == that.count && Arrays.equals(this.follow, that.follow) && Arrays.equals(this.track, that.track);
    }

    public int hashCode() {
        int result = this.count;
        return (((result * 31) + (this.follow != null ? Arrays.hashCode(this.follow) : 0)) * 31) + (this.track != null ? Arrays.hashCode(this.track) : 0);
    }

    public String toString() {
        return new StringBuffer().append("FilterQuery{count=").append(this.count).append(", follow=").append(Arrays.toString(this.follow)).append(", track=").append(this.track == null ? null : Arrays.asList(this.track)).append(", locations=").append(this.locations != null ? Arrays.asList(this.locations) : null).append(", includeEntities=").append(this.includeEntities).append('}').toString();
    }
}
