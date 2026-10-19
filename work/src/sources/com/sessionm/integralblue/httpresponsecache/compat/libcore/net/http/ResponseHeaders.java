package com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http;

import com.sessionm.integralblue.httpresponsecache.compat.java.net.ResponseSource;
import com.sessionm.integralblue.httpresponsecache.compat.libcore.util.Objects;
import java.net.URI;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public final class ResponseHeaders {
    private static final String RECEIVED_MILLIS = "X-Android-Received-Millis";
    private static final String SENT_MILLIS = "X-Android-Sent-Millis";
    private int ageSeconds;
    private String connection;
    private String contentEncoding;
    private int contentLength;
    private String etag;
    private Date expires;
    private final RawHeaders headers;
    private boolean isPublic;
    private Date lastModified;
    private boolean mustRevalidate;
    private boolean noCache;
    private boolean noStore;
    private String proxyAuthenticate;
    private long receivedResponseMillis;
    private long sentRequestMillis;
    private Date servedDate;
    private String transferEncoding;
    private final URI uri;
    private Set<String> varyFields;
    private String wwwAuthenticate;
    private int maxAgeSeconds = -1;
    private int sMaxAgeSeconds = -1;

    public ResponseHeaders(URI uri, RawHeaders rawHeaders) {
        this.ageSeconds = -1;
        this.varyFields = Collections.emptySet();
        this.contentLength = -1;
        this.uri = uri;
        this.headers = rawHeaders;
        HeaderParser.CacheControlHandler cacheControlHandler = new HeaderParser.CacheControlHandler() { // from class: com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http.ResponseHeaders.1
            @Override // com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http.HeaderParser.CacheControlHandler
            public void handle(String str, String str2) {
                if (str.equalsIgnoreCase("no-cache")) {
                    ResponseHeaders.this.noCache = true;
                    return;
                }
                if (str.equalsIgnoreCase("no-store")) {
                    ResponseHeaders.this.noStore = true;
                    return;
                }
                if (str.equalsIgnoreCase("max-age")) {
                    ResponseHeaders.this.maxAgeSeconds = HeaderParser.parseSeconds(str2);
                } else if (str.equalsIgnoreCase("s-maxage")) {
                    ResponseHeaders.this.sMaxAgeSeconds = HeaderParser.parseSeconds(str2);
                } else if (str.equalsIgnoreCase("public")) {
                    ResponseHeaders.this.isPublic = true;
                } else if (str.equalsIgnoreCase("must-revalidate")) {
                    ResponseHeaders.this.mustRevalidate = true;
                }
            }
        };
        for (int i = 0; i < rawHeaders.length(); i++) {
            String fieldName = rawHeaders.getFieldName(i);
            String value = rawHeaders.getValue(i);
            if ("Cache-Control".equalsIgnoreCase(fieldName)) {
                HeaderParser.parseCacheControl(value, cacheControlHandler);
            } else if ("Date".equalsIgnoreCase(fieldName)) {
                this.servedDate = HttpDate.parse(value);
            } else if ("Expires".equalsIgnoreCase(fieldName)) {
                this.expires = HttpDate.parse(value);
            } else if ("Last-Modified".equalsIgnoreCase(fieldName)) {
                this.lastModified = HttpDate.parse(value);
            } else if ("ETag".equalsIgnoreCase(fieldName)) {
                this.etag = value;
            } else if ("Pragma".equalsIgnoreCase(fieldName)) {
                if (value.equalsIgnoreCase("no-cache")) {
                    this.noCache = true;
                }
            } else if ("Age".equalsIgnoreCase(fieldName)) {
                this.ageSeconds = HeaderParser.parseSeconds(value);
            } else if ("Vary".equalsIgnoreCase(fieldName)) {
                if (this.varyFields.isEmpty()) {
                    this.varyFields = new TreeSet(String.CASE_INSENSITIVE_ORDER);
                }
                for (String str : value.split(",")) {
                    this.varyFields.add(str.trim());
                }
            } else if ("Content-Encoding".equalsIgnoreCase(fieldName)) {
                this.contentEncoding = value;
            } else if ("Transfer-Encoding".equalsIgnoreCase(fieldName)) {
                this.transferEncoding = value;
            } else if ("Content-Length".equalsIgnoreCase(fieldName)) {
                try {
                    this.contentLength = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                }
            } else if ("Connection".equalsIgnoreCase(fieldName)) {
                this.connection = value;
            } else if ("Proxy-Authenticate".equalsIgnoreCase(fieldName)) {
                this.proxyAuthenticate = value;
            } else if ("WWW-Authenticate".equalsIgnoreCase(fieldName)) {
                this.wwwAuthenticate = value;
            } else if (SENT_MILLIS.equalsIgnoreCase(fieldName)) {
                this.sentRequestMillis = Long.parseLong(value);
            } else if (RECEIVED_MILLIS.equalsIgnoreCase(fieldName)) {
                this.receivedResponseMillis = Long.parseLong(value);
            }
        }
    }

    public boolean isContentEncodingGzip() {
        return "gzip".equalsIgnoreCase(this.contentEncoding);
    }

    public void stripContentEncoding() {
        this.contentEncoding = null;
        this.headers.removeAll("Content-Encoding");
    }

    public boolean isChunked() {
        return "chunked".equalsIgnoreCase(this.transferEncoding);
    }

    public boolean hasConnectionClose() {
        return "close".equalsIgnoreCase(this.connection);
    }

    public URI getUri() {
        return this.uri;
    }

    public RawHeaders getHeaders() {
        return this.headers;
    }

    public Date getServedDate() {
        return this.servedDate;
    }

    public Date getLastModified() {
        return this.lastModified;
    }

    public Date getExpires() {
        return this.expires;
    }

    public boolean isNoCache() {
        return this.noCache;
    }

    public boolean isNoStore() {
        return this.noStore;
    }

    public int getMaxAgeSeconds() {
        return this.maxAgeSeconds;
    }

    public int getSMaxAgeSeconds() {
        return this.sMaxAgeSeconds;
    }

    public boolean isPublic() {
        return this.isPublic;
    }

    public boolean isMustRevalidate() {
        return this.mustRevalidate;
    }

    public String getEtag() {
        return this.etag;
    }

    public Set<String> getVaryFields() {
        return this.varyFields;
    }

    public String getContentEncoding() {
        return this.contentEncoding;
    }

    public int getContentLength() {
        return this.contentLength;
    }

    public String getConnection() {
        return this.connection;
    }

    public String getProxyAuthenticate() {
        return this.proxyAuthenticate;
    }

    public String getWwwAuthenticate() {
        return this.wwwAuthenticate;
    }

    public void setLocalTimestamps(long j, long j2) {
        this.sentRequestMillis = j;
        this.headers.add(SENT_MILLIS, Long.toString(j));
        this.receivedResponseMillis = j2;
        this.headers.add(RECEIVED_MILLIS, Long.toString(j2));
    }

    private long computeAge(long j) {
        long jMax = this.servedDate != null ? Math.max(0L, this.receivedResponseMillis - this.servedDate.getTime()) : 0L;
        if (this.ageSeconds != -1) {
            jMax = Math.max(jMax, TimeUnit.SECONDS.toMillis(this.ageSeconds));
        }
        return jMax + (this.receivedResponseMillis - this.sentRequestMillis) + (j - this.receivedResponseMillis);
    }

    private long computeFreshnessLifetime() {
        if (this.maxAgeSeconds != -1) {
            return TimeUnit.SECONDS.toMillis(this.maxAgeSeconds);
        }
        if (this.expires != null) {
            long time = this.expires.getTime() - (this.servedDate != null ? this.servedDate.getTime() : this.receivedResponseMillis);
            if (time <= 0) {
                time = 0;
            }
            return time;
        }
        if (this.lastModified == null || this.uri.getRawQuery() != null) {
            return 0L;
        }
        long time2 = (this.servedDate != null ? this.servedDate.getTime() : this.sentRequestMillis) - this.lastModified.getTime();
        if (time2 > 0) {
            return time2 / 10;
        }
        return 0L;
    }

    private boolean isFreshnessLifetimeHeuristic() {
        return this.maxAgeSeconds == -1 && this.expires == null;
    }

    public boolean isCacheable(RequestHeaders requestHeaders) {
        int responseCode = this.headers.getResponseCode();
        if (responseCode == 200 || responseCode == 203 || responseCode == 300 || responseCode == 301 || responseCode == 410) {
            return (!requestHeaders.hasAuthorization() || this.isPublic || this.mustRevalidate || this.sMaxAgeSeconds != -1) && !this.noStore;
        }
        return false;
    }

    public boolean hasVaryAll() {
        return this.varyFields.contains("*");
    }

    public boolean varyMatches(Map<String, List<String>> map, Map<String, List<String>> map2) {
        for (String str : this.varyFields) {
            if (!Objects.equal(map.get(str), map2.get(str))) {
                return false;
            }
        }
        return true;
    }

    public ResponseSource chooseResponseSource(long j, RequestHeaders requestHeaders) {
        long millis = 0;
        if (!isCacheable(requestHeaders)) {
            return ResponseSource.NETWORK;
        }
        if (requestHeaders.isNoCache() || requestHeaders.hasConditions()) {
            return ResponseSource.NETWORK;
        }
        long jComputeAge = computeAge(j);
        long jComputeFreshnessLifetime = computeFreshnessLifetime();
        if (requestHeaders.getMaxAgeSeconds() != -1) {
            jComputeFreshnessLifetime = Math.min(jComputeFreshnessLifetime, TimeUnit.SECONDS.toMillis(requestHeaders.getMaxAgeSeconds()));
        }
        long millis2 = requestHeaders.getMinFreshSeconds() != -1 ? TimeUnit.SECONDS.toMillis(requestHeaders.getMinFreshSeconds()) : 0L;
        if (!this.mustRevalidate && requestHeaders.getMaxStaleSeconds() != -1) {
            millis = TimeUnit.SECONDS.toMillis(requestHeaders.getMaxStaleSeconds());
        }
        if (!this.noCache && jComputeAge + millis2 < millis + jComputeFreshnessLifetime) {
            if (millis2 + jComputeAge >= jComputeFreshnessLifetime) {
                this.headers.add("Warning", "110 HttpURLConnection \"Response is stale\"");
            }
            if (jComputeAge > com.sessionm.integralblue.httpresponsecache.compat.java.util.concurrent.TimeUnit.HOURS.toMillis(24L) && isFreshnessLifetimeHeuristic()) {
                this.headers.add("Warning", "113 HttpURLConnection \"Heuristic expiration\"");
            }
            return ResponseSource.CACHE;
        }
        if (this.lastModified != null) {
            requestHeaders.setIfModifiedSince(this.lastModified);
        } else if (this.servedDate != null) {
            requestHeaders.setIfModifiedSince(this.servedDate);
        }
        if (this.etag != null) {
            requestHeaders.setIfNoneMatch(this.etag);
        }
        return requestHeaders.hasConditions() ? ResponseSource.CONDITIONAL_CACHE : ResponseSource.NETWORK;
    }

    public boolean validate(ResponseHeaders responseHeaders) {
        if (responseHeaders.headers.getResponseCode() == 304) {
            return true;
        }
        return (this.lastModified == null || responseHeaders.lastModified == null || responseHeaders.lastModified.getTime() >= this.lastModified.getTime()) ? false : true;
    }

    public ResponseHeaders combine(ResponseHeaders responseHeaders) {
        RawHeaders rawHeaders = new RawHeaders();
        for (int i = 0; i < this.headers.length(); i++) {
            String fieldName = this.headers.getFieldName(i);
            String value = this.headers.getValue(i);
            if ((!fieldName.equals("Warning") || !value.startsWith("1")) && (!isEndToEnd(fieldName) || responseHeaders.headers.get(fieldName) == null)) {
                rawHeaders.add(fieldName, value);
            }
        }
        for (int i2 = 0; i2 < responseHeaders.headers.length(); i2++) {
            String fieldName2 = responseHeaders.headers.getFieldName(i2);
            if (isEndToEnd(fieldName2)) {
                rawHeaders.add(fieldName2, responseHeaders.headers.getValue(i2));
            }
        }
        return new ResponseHeaders(this.uri, rawHeaders);
    }

    private static boolean isEndToEnd(String str) {
        return (str.equalsIgnoreCase("Connection") || str.equalsIgnoreCase("Keep-Alive") || str.equalsIgnoreCase("Proxy-Authenticate") || str.equalsIgnoreCase("Proxy-Authorization") || str.equalsIgnoreCase("TE") || str.equalsIgnoreCase("Trailers") || str.equalsIgnoreCase("Transfer-Encoding") || str.equalsIgnoreCase("Upgrade")) ? false : true;
    }
}
