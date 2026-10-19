package twitter4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import twitter4j.internal.http.HttpParameter;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class Paging implements Serializable {
    static final String COUNT = "count";
    static final String PER_PAGE = "per_page";
    private static final long serialVersionUID = -3285857427993796670L;
    private int count;
    private long maxId;
    private int page;
    private long sinceId;
    static char[] S = {'s'};
    static char[] SMCP = {'s', 'm', 'c', 'p'};
    private static HttpParameter[] NULL_PARAMETER_ARRAY = new HttpParameter[0];
    private static List<HttpParameter> NULL_PARAMETER_LIST = new ArrayList(0);

    List<HttpParameter> asPostParameterList() {
        return asPostParameterList(SMCP, COUNT);
    }

    HttpParameter[] asPostParameterArray() {
        List<HttpParameter> list = asPostParameterList(SMCP, COUNT);
        return list.size() == 0 ? NULL_PARAMETER_ARRAY : (HttpParameter[]) list.toArray(new HttpParameter[list.size()]);
    }

    List<HttpParameter> asPostParameterList(char[] supportedParams) {
        return asPostParameterList(supportedParams, COUNT);
    }

    List<HttpParameter> asPostParameterList(char[] supportedParams, String perPageParamName) {
        List<HttpParameter> pagingParams = new ArrayList<>(supportedParams.length);
        addPostParameter(supportedParams, 's', pagingParams, "since_id", getSinceId());
        addPostParameter(supportedParams, 'm', pagingParams, "max_id", getMaxId());
        addPostParameter(supportedParams, 'c', pagingParams, perPageParamName, getCount());
        addPostParameter(supportedParams, 'p', pagingParams, "page", getPage());
        if (pagingParams.size() == 0) {
            return NULL_PARAMETER_LIST;
        }
        return pagingParams;
    }

    HttpParameter[] asPostParameterArray(char[] supportedParams, String perPageParamName) {
        List<HttpParameter> pagingParams = new ArrayList<>(supportedParams.length);
        addPostParameter(supportedParams, 's', pagingParams, "since_id", getSinceId());
        addPostParameter(supportedParams, 'm', pagingParams, "max_id", getMaxId());
        addPostParameter(supportedParams, 'c', pagingParams, perPageParamName, getCount());
        addPostParameter(supportedParams, 'p', pagingParams, "page", getPage());
        return pagingParams.size() == 0 ? NULL_PARAMETER_ARRAY : (HttpParameter[]) pagingParams.toArray(new HttpParameter[pagingParams.size()]);
    }

    private void addPostParameter(char[] supportedParams, char paramKey, List<HttpParameter> pagingParams, String paramName, long paramValue) {
        boolean supported = false;
        for (char supportedParam : supportedParams) {
            if (supportedParam == paramKey) {
                supported = true;
                break;
            }
        }
        if (!supported && -1 != paramValue) {
            throw new IllegalStateException(new StringBuffer().append("Paging parameter [").append(paramName).append("] is not supported with this operation.").toString());
        }
        if (-1 != paramValue) {
            pagingParams.add(new HttpParameter(paramName, String.valueOf(paramValue)));
        }
    }

    public Paging() {
        this.page = -1;
        this.count = -1;
        this.sinceId = -1L;
        this.maxId = -1L;
    }

    public Paging(int page) {
        this.page = -1;
        this.count = -1;
        this.sinceId = -1L;
        this.maxId = -1L;
        setPage(page);
    }

    public Paging(long sinceId) {
        this.page = -1;
        this.count = -1;
        this.sinceId = -1L;
        this.maxId = -1L;
        setSinceId(sinceId);
    }

    public Paging(int page, int count) {
        this(page);
        setCount(count);
    }

    public Paging(int page, long sinceId) {
        this(page);
        setSinceId(sinceId);
    }

    public Paging(int page, int count, long sinceId) {
        this(page, count);
        setSinceId(sinceId);
    }

    public Paging(int page, int count, long sinceId, long maxId) {
        this(page, count, sinceId);
        setMaxId(maxId);
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        if (page < 1) {
            throw new IllegalArgumentException(new StringBuffer().append("page should be positive integer. passed:").append(page).toString());
        }
        this.page = page;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        if (count < 1) {
            throw new IllegalArgumentException(new StringBuffer().append("count should be positive integer. passed:").append(count).toString());
        }
        this.count = count;
    }

    public Paging count(int count) {
        setCount(count);
        return this;
    }

    public long getSinceId() {
        return this.sinceId;
    }

    public void setSinceId(long sinceId) {
        if (sinceId < 1) {
            throw new IllegalArgumentException(new StringBuffer().append("since_id should be positive integer. passed:").append(sinceId).toString());
        }
        this.sinceId = sinceId;
    }

    public Paging sinceId(long sinceId) {
        setSinceId(sinceId);
        return this;
    }

    public long getMaxId() {
        return this.maxId;
    }

    public void setMaxId(long maxId) {
        if (maxId < 1) {
            throw new IllegalArgumentException(new StringBuffer().append("max_id should be positive integer. passed:").append(maxId).toString());
        }
        this.maxId = maxId;
    }

    public Paging maxId(long maxId) {
        setMaxId(maxId);
        return this;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paging)) {
            return false;
        }
        Paging paging = (Paging) o;
        return this.count == paging.count && this.maxId == paging.maxId && this.page == paging.page && this.sinceId == paging.sinceId;
    }

    public int hashCode() {
        int result = this.page;
        return (((((result * 31) + this.count) * 31) + ((int) (this.sinceId ^ (this.sinceId >>> 32)))) * 31) + ((int) (this.maxId ^ (this.maxId >>> 32)));
    }

    public String toString() {
        return new StringBuffer().append("Paging{page=").append(this.page).append(", count=").append(this.count).append(", sinceId=").append(this.sinceId).append(", maxId=").append(this.maxId).append('}').toString();
    }
}
