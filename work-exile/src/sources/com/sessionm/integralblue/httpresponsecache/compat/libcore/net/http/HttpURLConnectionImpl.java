package com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http;

import com.sessionm.integralblue.httpresponsecache.compat.Charsets;
import com.sessionm.integralblue.httpresponsecache.compat.Strings;
import com.sessionm.integralblue.httpresponsecache.compat.URLs;
import com.sessionm.integralblue.httpresponsecache.compat.libcore.io.Base64;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.SocketPermission;
import java.net.URL;
import java.security.Permission;
import java.util.List;
import java.util.Map;
import oauth.signpost.OAuth;
import twitter4j.internal.http.HttpResponseCode;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class HttpURLConnectionImpl extends HttpURLConnection {
    private final int defaultPort;
    protected HttpEngine httpEngine;
    protected IOException httpEngineFailure;
    private Proxy proxy;
    private final RawHeaders rawRequestHeaders;
    private int redirectionCount;

    /* JADX INFO: compiled from: ProGuard */
    enum Retry {
        NONE,
        SAME_CONNECTION,
        DIFFERENT_CONNECTION
    }

    protected HttpURLConnectionImpl(URL url, int i) {
        super(url);
        this.rawRequestHeaders = new RawHeaders();
        this.defaultPort = i;
    }

    protected HttpURLConnectionImpl(URL url, int i, Proxy proxy) {
        this(url, i);
        this.proxy = proxy;
    }

    @Override // java.net.URLConnection
    public final void connect() throws IOException {
        initHttpEngine();
        try {
            this.httpEngine.sendRequest();
        } catch (IOException e) {
            this.httpEngineFailure = e;
            throw e;
        }
    }

    @Override // java.net.HttpURLConnection
    public final void disconnect() {
        if (this.httpEngine != null) {
            this.httpEngine.release(false);
        }
    }

    @Override // java.net.HttpURLConnection
    public final InputStream getErrorStream() {
        try {
            HttpEngine response = getResponse();
            if (!response.hasResponseBody() || response.getResponseCode() < 400) {
                return null;
            }
            return response.getResponseBody();
        } catch (IOException e) {
            return null;
        }
    }

    @Override // java.net.HttpURLConnection, java.net.URLConnection
    public final String getHeaderField(int i) {
        try {
            return getResponse().getResponseHeaders().getHeaders().getValue(i);
        } catch (IOException e) {
            return null;
        }
    }

    @Override // java.net.URLConnection
    public final String getHeaderField(String str) {
        try {
            RawHeaders headers = getResponse().getResponseHeaders().getHeaders();
            return str == null ? headers.getStatusLine() : headers.get(str);
        } catch (IOException e) {
            return null;
        }
    }

    @Override // java.net.HttpURLConnection, java.net.URLConnection
    public final String getHeaderFieldKey(int i) {
        try {
            return getResponse().getResponseHeaders().getHeaders().getFieldName(i);
        } catch (IOException e) {
            return null;
        }
    }

    @Override // java.net.URLConnection
    public final Map<String, List<String>> getHeaderFields() {
        try {
            return getResponse().getResponseHeaders().getHeaders().toMultimap();
        } catch (IOException e) {
            return null;
        }
    }

    @Override // java.net.URLConnection
    public final Map<String, List<String>> getRequestProperties() {
        if (this.connected) {
            throw new IllegalStateException("Cannot access request header fields after connection is set");
        }
        return this.rawRequestHeaders.toMultimap();
    }

    @Override // java.net.URLConnection
    public final InputStream getInputStream() throws IOException {
        if (!this.doInput) {
            throw new ProtocolException("This protocol does not support input");
        }
        HttpEngine response = getResponse();
        if (getResponseCode() >= 400) {
            throw new FileNotFoundException(this.url.toString());
        }
        InputStream responseBody = response.getResponseBody();
        if (responseBody == null) {
            throw new IOException("No response body exists; responseCode=" + getResponseCode());
        }
        return responseBody;
    }

    @Override // java.net.URLConnection
    public final OutputStream getOutputStream() throws IOException {
        connect();
        OutputStream requestBody = this.httpEngine.getRequestBody();
        if (requestBody == null) {
            throw new ProtocolException("method does not support a request body: " + this.method);
        }
        if (this.httpEngine.hasResponse()) {
            throw new ProtocolException("cannot write request body after response has been read");
        }
        return requestBody;
    }

    @Override // java.net.HttpURLConnection, java.net.URLConnection
    public final Permission getPermission() throws IOException {
        return new SocketPermission(getConnectToHost() + ":" + getConnectToPort(), "connect, resolve");
    }

    private String getConnectToHost() {
        return usingProxy() ? ((InetSocketAddress) this.proxy.address()).getHostName() : getURL().getHost();
    }

    private int getConnectToPort() {
        int port = usingProxy() ? ((InetSocketAddress) this.proxy.address()).getPort() : getURL().getPort();
        return port < 0 ? getDefaultPort() : port;
    }

    @Override // java.net.URLConnection
    public final String getRequestProperty(String str) {
        if (str == null) {
            return null;
        }
        return this.rawRequestHeaders.get(str);
    }

    private void initHttpEngine() throws IOException {
        if (this.httpEngineFailure != null) {
            throw this.httpEngineFailure;
        }
        if (this.httpEngine == null) {
            this.connected = true;
            try {
                if (this.doOutput) {
                    if (this.method == HttpEngine.GET) {
                        this.method = HttpEngine.POST;
                    } else if (this.method != HttpEngine.POST && this.method != HttpEngine.PUT) {
                        throw new ProtocolException(this.method + " does not support writing");
                    }
                }
                this.httpEngine = newHttpEngine(this.method, this.rawRequestHeaders, null, null);
            } catch (IOException e) {
                this.httpEngineFailure = e;
                throw e;
            }
        }
    }

    protected HttpEngine newHttpEngine(String str, RawHeaders rawHeaders, HttpConnection httpConnection, RetryableOutputStream retryableOutputStream) throws IOException {
        return new HttpEngine(this, str, rawHeaders, httpConnection, retryableOutputStream);
    }

    private HttpEngine getResponse() throws IOException {
        initHttpEngine();
        if (this.httpEngine.hasResponse()) {
            return this.httpEngine;
        }
        while (true) {
            try {
                this.httpEngine.sendRequest();
                this.httpEngine.readResponse();
                Retry retryProcessResponseHeaders = processResponseHeaders();
                if (retryProcessResponseHeaders == Retry.NONE) {
                    this.httpEngine.automaticallyReleaseConnectionToPool();
                    return this.httpEngine;
                }
                String str = this.method;
                OutputStream requestBody = this.httpEngine.getRequestBody();
                int responseCode = getResponseCode();
                if (responseCode == 300 || responseCode == 301 || responseCode == 302 || responseCode == 303) {
                    str = HttpEngine.GET;
                    requestBody = null;
                }
                if (requestBody != null && !(requestBody instanceof RetryableOutputStream)) {
                    throw new HttpRetryException("Cannot retry streamed HTTP body", this.httpEngine.getResponseCode());
                }
                if (retryProcessResponseHeaders == Retry.DIFFERENT_CONNECTION) {
                    this.httpEngine.automaticallyReleaseConnectionToPool();
                }
                this.httpEngine.release(true);
                this.httpEngine = newHttpEngine(str, this.rawRequestHeaders, this.httpEngine.getConnection(), (RetryableOutputStream) requestBody);
            } catch (IOException e) {
                this.httpEngineFailure = e;
                throw e;
            }
        }
    }

    HttpEngine getHttpEngine() {
        return this.httpEngine;
    }

    private Retry processResponseHeaders() throws IOException {
        switch (getResponseCode()) {
            case HttpResponseCode.MULTIPLE_CHOICES /* 300 */:
            case 301:
            case HttpResponseCode.FOUND /* 302 */:
            case 303:
                if (!getInstanceFollowRedirects()) {
                    return Retry.NONE;
                }
                int i = this.redirectionCount + 1;
                this.redirectionCount = i;
                if (i > 5) {
                    throw new ProtocolException("Too many redirects");
                }
                String headerField = getHeaderField("Location");
                if (headerField == null) {
                    return Retry.NONE;
                }
                URL url = this.url;
                this.url = new URL(url, headerField);
                if (!url.getProtocol().equals(this.url.getProtocol())) {
                    return Retry.NONE;
                }
                if (url.getHost().equals(this.url.getHost()) && URLs.getEffectivePort(url) == URLs.getEffectivePort(this.url)) {
                    return Retry.SAME_CONNECTION;
                }
                return Retry.DIFFERENT_CONNECTION;
            case HttpResponseCode.UNAUTHORIZED /* 401 */:
                break;
            case 407:
                if (!usingProxy()) {
                    throw new IOException("Received HTTP_PROXY_AUTH (407) code while not using proxy");
                }
                break;
            default:
                return Retry.NONE;
        }
        return processAuthHeader(getResponseCode(), this.httpEngine.getResponseHeaders(), this.rawRequestHeaders) ? Retry.SAME_CONNECTION : Retry.NONE;
    }

    final boolean processAuthHeader(int i, ResponseHeaders responseHeaders, RawHeaders rawHeaders) throws IOException {
        if (i != 407 && i != 401) {
            throw new IllegalArgumentException();
        }
        String proxyAuthenticate = i == 407 ? responseHeaders.getProxyAuthenticate() : responseHeaders.getWwwAuthenticate();
        if (proxyAuthenticate == null) {
            throw new IOException("Received authentication challenge is null");
        }
        String authorizationCredentials = getAuthorizationCredentials(proxyAuthenticate);
        if (authorizationCredentials == null) {
            return false;
        }
        rawHeaders.set(i == 407 ? "Proxy-Authorization" : OAuth.HTTP_AUTHORIZATION_HEADER, authorizationCredentials);
        return true;
    }

    private String getAuthorizationCredentials(String str) throws IOException {
        int iIndexOf;
        int iIndexOf2 = str.indexOf(" ");
        if (iIndexOf2 == -1) {
            return null;
        }
        String strSubstring = str.substring(0, iIndexOf2);
        int iIndexOf3 = str.indexOf("realm=\"") + 7;
        PasswordAuthentication passwordAuthenticationRequestPasswordAuthentication = Authenticator.requestPasswordAuthentication(getConnectToInetAddress(), getConnectToPort(), this.url.getProtocol(), (iIndexOf3 == -1 || (iIndexOf = str.indexOf(34, iIndexOf3)) == -1) ? null : str.substring(iIndexOf3, iIndexOf), strSubstring);
        if (passwordAuthenticationRequestPasswordAuthentication != null) {
            return strSubstring + " " + Base64.encode(Strings.getBytes(passwordAuthenticationRequestPasswordAuthentication.getUserName() + ":" + new String(passwordAuthenticationRequestPasswordAuthentication.getPassword()), Charsets.ISO_8859_1));
        }
        return null;
    }

    private InetAddress getConnectToInetAddress() throws IOException {
        return usingProxy() ? ((InetSocketAddress) this.proxy.address()).getAddress() : InetAddress.getByName(getURL().getHost());
    }

    final int getDefaultPort() {
        return this.defaultPort;
    }

    final int getFixedContentLength() {
        return this.fixedContentLength;
    }

    final int getChunkLength() {
        return this.chunkLength;
    }

    final Proxy getProxy() {
        return this.proxy;
    }

    final void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    @Override // java.net.HttpURLConnection
    public final boolean usingProxy() {
        return (this.proxy == null || this.proxy.type() == Proxy.Type.DIRECT) ? false : true;
    }

    @Override // java.net.HttpURLConnection
    public String getResponseMessage() throws IOException {
        return getResponse().getResponseHeaders().getHeaders().getResponseMessage();
    }

    @Override // java.net.HttpURLConnection
    public final int getResponseCode() throws IOException {
        return getResponse().getResponseCode();
    }

    @Override // java.net.URLConnection
    public final void setRequestProperty(String str, String str2) {
        if (this.connected) {
            throw new IllegalStateException("Cannot set request property after connection is made");
        }
        if (str == null) {
            throw new NullPointerException("field == null");
        }
        this.rawRequestHeaders.set(str, str2);
    }

    @Override // java.net.URLConnection
    public final void addRequestProperty(String str, String str2) {
        if (this.connected) {
            throw new IllegalStateException("Cannot add request property after connection is made");
        }
        if (str == null) {
            throw new NullPointerException("field == null");
        }
        this.rawRequestHeaders.add(str, str2);
    }
}
