package com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;
import oauth.signpost.OAuth;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class RequestHeaders {
    private String acceptEncoding;
    private String connection;
    private int contentLength;
    private String contentType;
    private boolean hasAuthorization;
    private final RawHeaders headers;
    private String host;
    private String ifModifiedSince;
    private String ifNoneMatch;
    private int maxAgeSeconds = -1;
    private int maxStaleSeconds = -1;
    private int minFreshSeconds = -1;
    private boolean noCache;
    private boolean onlyIfCached;
    private String proxyAuthorization;
    private String transferEncoding;
    private final URI uri;
    private String userAgent;

    public RequestHeaders(URI uri, RawHeaders rawHeaders) {
        this.contentLength = -1;
        this.uri = uri;
        this.headers = rawHeaders;
        HeaderParser.CacheControlHandler cacheControlHandler = new HeaderParser.CacheControlHandler() { // from class: com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http.RequestHeaders.1
            @Override // com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http.HeaderParser.CacheControlHandler
            public void handle(String str, String str2) {
                if (str.equalsIgnoreCase("no-cache")) {
                    RequestHeaders.this.noCache = true;
                    return;
                }
                if (str.equalsIgnoreCase("max-age")) {
                    RequestHeaders.this.maxAgeSeconds = HeaderParser.parseSeconds(str2);
                    return;
                }
                if (str.equalsIgnoreCase("max-stale")) {
                    RequestHeaders.this.maxStaleSeconds = HeaderParser.parseSeconds(str2);
                } else if (str.equalsIgnoreCase("min-fresh")) {
                    RequestHeaders.this.minFreshSeconds = HeaderParser.parseSeconds(str2);
                } else if (str.equalsIgnoreCase("only-if-cached")) {
                    RequestHeaders.this.onlyIfCached = true;
                }
            }
        };
        for (int i = 0; i < rawHeaders.length(); i++) {
            String fieldName = rawHeaders.getFieldName(i);
            String value = rawHeaders.getValue(i);
            if ("Cache-Control".equalsIgnoreCase(fieldName)) {
                HeaderParser.parseCacheControl(value, cacheControlHandler);
            } else if ("Pragma".equalsIgnoreCase(fieldName)) {
                if (value.equalsIgnoreCase("no-cache")) {
                    this.noCache = true;
                }
            } else if ("If-None-Match".equalsIgnoreCase(fieldName)) {
                this.ifNoneMatch = value;
            } else if ("If-Modified-Since".equalsIgnoreCase(fieldName)) {
                this.ifModifiedSince = value;
            } else if (OAuth.HTTP_AUTHORIZATION_HEADER.equalsIgnoreCase(fieldName)) {
                this.hasAuthorization = true;
            } else if ("Content-Length".equalsIgnoreCase(fieldName)) {
                try {
                    this.contentLength = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                }
            } else if ("Transfer-Encoding".equalsIgnoreCase(fieldName)) {
                this.transferEncoding = value;
            } else if ("User-Agent".equalsIgnoreCase(fieldName)) {
                this.userAgent = value;
            } else if ("Host".equalsIgnoreCase(fieldName)) {
                this.host = value;
            } else if ("Connection".equalsIgnoreCase(fieldName)) {
                this.connection = value;
            } else if ("Accept-Encoding".equalsIgnoreCase(fieldName)) {
                this.acceptEncoding = value;
            } else if ("Content-Type".equalsIgnoreCase(fieldName)) {
                this.contentType = value;
            } else if ("Proxy-Authorization".equalsIgnoreCase(fieldName)) {
                this.proxyAuthorization = value;
            }
        }
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

    public boolean isNoCache() {
        return this.noCache;
    }

    public int getMaxAgeSeconds() {
        return this.maxAgeSeconds;
    }

    public int getMaxStaleSeconds() {
        return this.maxStaleSeconds;
    }

    public int getMinFreshSeconds() {
        return this.minFreshSeconds;
    }

    public boolean isOnlyIfCached() {
        return this.onlyIfCached;
    }

    public boolean hasAuthorization() {
        return this.hasAuthorization;
    }

    public int getContentLength() {
        return this.contentLength;
    }

    public String getTransferEncoding() {
        return this.transferEncoding;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public String getHost() {
        return this.host;
    }

    public String getConnection() {
        return this.connection;
    }

    public String getAcceptEncoding() {
        return this.acceptEncoding;
    }

    public String getContentType() {
        return this.contentType;
    }

    public String getIfModifiedSince() {
        return this.ifModifiedSince;
    }

    public String getIfNoneMatch() {
        return this.ifNoneMatch;
    }

    public String getProxyAuthorization() {
        return this.proxyAuthorization;
    }

    public void setChunked() {
        if (this.transferEncoding != null) {
            this.headers.removeAll("Transfer-Encoding");
        }
        this.headers.add("Transfer-Encoding", "chunked");
        this.transferEncoding = "chunked";
    }

    public void setContentLength(int i) {
        if (this.contentLength != -1) {
            this.headers.removeAll("Content-Length");
        }
        this.headers.add("Content-Length", Integer.toString(i));
        this.contentLength = i;
    }

    public void setUserAgent(String str) {
        if (this.userAgent != null) {
            this.headers.removeAll("User-Agent");
        }
        this.headers.add("User-Agent", str);
        this.userAgent = str;
    }

    public void setHost(String str) {
        if (this.host != null) {
            this.headers.removeAll("Host");
        }
        this.headers.add("Host", str);
        this.host = str;
    }

    public void setConnection(String str) {
        if (this.connection != null) {
            this.headers.removeAll("Connection");
        }
        this.headers.add("Connection", str);
        this.connection = str;
    }

    public void setAcceptEncoding(String str) {
        if (this.acceptEncoding != null) {
            this.headers.removeAll("Accept-Encoding");
        }
        this.headers.add("Accept-Encoding", str);
        this.acceptEncoding = str;
    }

    public void setContentType(String str) {
        if (this.contentType != null) {
            this.headers.removeAll("Content-Type");
        }
        this.headers.add("Content-Type", str);
        this.contentType = str;
    }

    public void setIfModifiedSince(Date date) {
        if (this.ifModifiedSince != null) {
            this.headers.removeAll("If-Modified-Since");
        }
        String str = HttpDate.format(date);
        this.headers.add("If-Modified-Since", str);
        this.ifModifiedSince = str;
    }

    public void setIfNoneMatch(String str) {
        if (this.ifNoneMatch != null) {
            this.headers.removeAll("If-None-Match");
        }
        this.headers.add("If-None-Match", str);
        this.ifNoneMatch = str;
    }

    public boolean hasConditions() {
        return (this.ifModifiedSince == null && this.ifNoneMatch == null) ? false : true;
    }

    public void addCookies(Map<String, List<String>> map) {
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String key = entry.getKey();
            if ("Cookie".equalsIgnoreCase(key) || "Cookie2".equalsIgnoreCase(key)) {
                this.headers.addAll(key, entry.getValue());
            }
        }
    }
}
