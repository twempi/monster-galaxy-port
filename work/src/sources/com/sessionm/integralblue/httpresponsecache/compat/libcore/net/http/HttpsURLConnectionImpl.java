package com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http;

import com.sessionm.integralblue.httpresponsecache.compat.URLs;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CacheResponse;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.SecureCacheResponse;
import java.net.URL;
import java.security.Permission;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import twitter4j.internal.http.HttpResponseCode;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
final class HttpsURLConnectionImpl extends HttpsURLConnection {
    private final HttpUrlConnectionDelegate delegate;

    protected HttpsURLConnectionImpl(URL url, int i) {
        super(url);
        this.delegate = new HttpUrlConnectionDelegate(url, i);
    }

    protected HttpsURLConnectionImpl(URL url, int i, Proxy proxy) {
        super(url);
        this.delegate = new HttpUrlConnectionDelegate(url, i, proxy);
    }

    private void checkConnected() {
        if (this.delegate.getSSLSocket() == null) {
            throw new IllegalStateException("Connection has not yet been established");
        }
    }

    HttpEngine getHttpEngine() {
        return this.delegate.getHttpEngine();
    }

    @Override // javax.net.ssl.HttpsURLConnection
    public String getCipherSuite() {
        SecureCacheResponse cacheResponse = this.delegate.getCacheResponse();
        if (cacheResponse != null) {
            return cacheResponse.getCipherSuite();
        }
        checkConnected();
        return this.delegate.getSSLSocket().getSession().getCipherSuite();
    }

    @Override // javax.net.ssl.HttpsURLConnection
    public Certificate[] getLocalCertificates() {
        SecureCacheResponse cacheResponse = this.delegate.getCacheResponse();
        if (cacheResponse != null) {
            List<Certificate> localCertificateChain = cacheResponse.getLocalCertificateChain();
            if (localCertificateChain != null) {
                return (Certificate[]) localCertificateChain.toArray(new Certificate[localCertificateChain.size()]);
            }
            return null;
        }
        checkConnected();
        return this.delegate.getSSLSocket().getSession().getLocalCertificates();
    }

    @Override // javax.net.ssl.HttpsURLConnection
    public Certificate[] getServerCertificates() throws SSLPeerUnverifiedException {
        SecureCacheResponse cacheResponse = this.delegate.getCacheResponse();
        if (cacheResponse != null) {
            List<Certificate> serverCertificateChain = cacheResponse.getServerCertificateChain();
            if (serverCertificateChain != null) {
                return (Certificate[]) serverCertificateChain.toArray(new Certificate[serverCertificateChain.size()]);
            }
            return null;
        }
        checkConnected();
        return this.delegate.getSSLSocket().getSession().getPeerCertificates();
    }

    @Override // javax.net.ssl.HttpsURLConnection
    public Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
        SecureCacheResponse cacheResponse = this.delegate.getCacheResponse();
        if (cacheResponse != null) {
            return cacheResponse.getPeerPrincipal();
        }
        checkConnected();
        return this.delegate.getSSLSocket().getSession().getPeerPrincipal();
    }

    @Override // javax.net.ssl.HttpsURLConnection
    public Principal getLocalPrincipal() {
        SecureCacheResponse cacheResponse = this.delegate.getCacheResponse();
        if (cacheResponse != null) {
            return cacheResponse.getLocalPrincipal();
        }
        checkConnected();
        return this.delegate.getSSLSocket().getSession().getLocalPrincipal();
    }

    @Override // java.net.HttpURLConnection
    public void disconnect() {
        this.delegate.disconnect();
    }

    @Override // java.net.HttpURLConnection
    public InputStream getErrorStream() {
        return this.delegate.getErrorStream();
    }

    @Override // java.net.HttpURLConnection
    public String getRequestMethod() {
        return this.delegate.getRequestMethod();
    }

    @Override // java.net.HttpURLConnection
    public int getResponseCode() throws IOException {
        return this.delegate.getResponseCode();
    }

    @Override // java.net.HttpURLConnection
    public String getResponseMessage() throws IOException {
        return this.delegate.getResponseMessage();
    }

    @Override // java.net.HttpURLConnection
    public void setRequestMethod(String str) throws ProtocolException {
        this.delegate.setRequestMethod(str);
    }

    @Override // java.net.HttpURLConnection
    public boolean usingProxy() {
        return this.delegate.usingProxy();
    }

    @Override // java.net.HttpURLConnection
    public boolean getInstanceFollowRedirects() {
        return this.delegate.getInstanceFollowRedirects();
    }

    @Override // java.net.HttpURLConnection
    public void setInstanceFollowRedirects(boolean z) {
        this.delegate.setInstanceFollowRedirects(z);
    }

    @Override // java.net.URLConnection
    public void connect() throws IOException {
        this.connected = true;
        this.delegate.connect();
    }

    @Override // java.net.URLConnection
    public boolean getAllowUserInteraction() {
        return this.delegate.getAllowUserInteraction();
    }

    @Override // java.net.URLConnection
    public Object getContent() throws IOException {
        return this.delegate.getContent();
    }

    @Override // java.net.URLConnection
    public Object getContent(Class[] clsArr) throws IOException {
        return this.delegate.getContent(clsArr);
    }

    @Override // java.net.URLConnection
    public String getContentEncoding() {
        return this.delegate.getContentEncoding();
    }

    @Override // java.net.URLConnection
    public int getContentLength() {
        return this.delegate.getContentLength();
    }

    @Override // java.net.URLConnection
    public String getContentType() {
        return this.delegate.getContentType();
    }

    @Override // java.net.URLConnection
    public long getDate() {
        return this.delegate.getDate();
    }

    @Override // java.net.URLConnection
    public boolean getDefaultUseCaches() {
        return this.delegate.getDefaultUseCaches();
    }

    @Override // java.net.URLConnection
    public boolean getDoInput() {
        return this.delegate.getDoInput();
    }

    @Override // java.net.URLConnection
    public boolean getDoOutput() {
        return this.delegate.getDoOutput();
    }

    @Override // java.net.URLConnection
    public long getExpiration() {
        return this.delegate.getExpiration();
    }

    @Override // java.net.HttpURLConnection, java.net.URLConnection
    public String getHeaderField(int i) {
        return this.delegate.getHeaderField(i);
    }

    @Override // java.net.URLConnection
    public Map<String, List<String>> getHeaderFields() {
        return this.delegate.getHeaderFields();
    }

    @Override // java.net.URLConnection
    public Map<String, List<String>> getRequestProperties() {
        return this.delegate.getRequestProperties();
    }

    @Override // java.net.URLConnection
    public void addRequestProperty(String str, String str2) {
        this.delegate.addRequestProperty(str, str2);
    }

    @Override // java.net.URLConnection
    public String getHeaderField(String str) {
        return this.delegate.getHeaderField(str);
    }

    @Override // java.net.HttpURLConnection, java.net.URLConnection
    public long getHeaderFieldDate(String str, long j) {
        return this.delegate.getHeaderFieldDate(str, j);
    }

    @Override // java.net.URLConnection
    public int getHeaderFieldInt(String str, int i) {
        return this.delegate.getHeaderFieldInt(str, i);
    }

    @Override // java.net.HttpURLConnection, java.net.URLConnection
    public String getHeaderFieldKey(int i) {
        return this.delegate.getHeaderFieldKey(i);
    }

    @Override // java.net.URLConnection
    public long getIfModifiedSince() {
        return this.delegate.getIfModifiedSince();
    }

    @Override // java.net.URLConnection
    public InputStream getInputStream() throws IOException {
        return this.delegate.getInputStream();
    }

    @Override // java.net.URLConnection
    public long getLastModified() {
        return this.delegate.getLastModified();
    }

    @Override // java.net.URLConnection
    public OutputStream getOutputStream() throws IOException {
        return this.delegate.getOutputStream();
    }

    @Override // java.net.HttpURLConnection, java.net.URLConnection
    public Permission getPermission() throws IOException {
        return this.delegate.getPermission();
    }

    @Override // java.net.URLConnection
    public String getRequestProperty(String str) {
        return this.delegate.getRequestProperty(str);
    }

    @Override // java.net.URLConnection
    public URL getURL() {
        return this.delegate.getURL();
    }

    @Override // java.net.URLConnection
    public boolean getUseCaches() {
        return this.delegate.getUseCaches();
    }

    @Override // java.net.URLConnection
    public void setAllowUserInteraction(boolean z) {
        this.delegate.setAllowUserInteraction(z);
    }

    @Override // java.net.URLConnection
    public void setDefaultUseCaches(boolean z) {
        this.delegate.setDefaultUseCaches(z);
    }

    @Override // java.net.URLConnection
    public void setDoInput(boolean z) {
        this.delegate.setDoInput(z);
    }

    @Override // java.net.URLConnection
    public void setDoOutput(boolean z) {
        this.delegate.setDoOutput(z);
    }

    @Override // java.net.URLConnection
    public void setIfModifiedSince(long j) {
        this.delegate.setIfModifiedSince(j);
    }

    @Override // java.net.URLConnection
    public void setRequestProperty(String str, String str2) {
        this.delegate.setRequestProperty(str, str2);
    }

    @Override // java.net.URLConnection
    public void setUseCaches(boolean z) {
        this.delegate.setUseCaches(z);
    }

    @Override // java.net.URLConnection
    public void setConnectTimeout(int i) {
        this.delegate.setConnectTimeout(i);
    }

    @Override // java.net.URLConnection
    public int getConnectTimeout() {
        return this.delegate.getConnectTimeout();
    }

    @Override // java.net.URLConnection
    public void setReadTimeout(int i) {
        this.delegate.setReadTimeout(i);
    }

    @Override // java.net.URLConnection
    public int getReadTimeout() {
        return this.delegate.getReadTimeout();
    }

    @Override // java.net.URLConnection
    public String toString() {
        return this.delegate.toString();
    }

    @Override // java.net.HttpURLConnection
    public void setFixedLengthStreamingMode(int i) {
        this.delegate.setFixedLengthStreamingMode(i);
    }

    @Override // java.net.HttpURLConnection
    public void setChunkedStreamingMode(int i) {
        this.delegate.setChunkedStreamingMode(i);
    }

    /* JADX INFO: compiled from: ProGuard */
    private final class HttpUrlConnectionDelegate extends HttpURLConnectionImpl {
        private HttpUrlConnectionDelegate(URL url, int i) {
            super(url, i);
        }

        private HttpUrlConnectionDelegate(URL url, int i, Proxy proxy) {
            super(url, i, proxy);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http.HttpURLConnectionImpl
        protected HttpEngine newHttpEngine(String str, RawHeaders rawHeaders, HttpConnection httpConnection, RetryableOutputStream retryableOutputStream) throws IOException {
            return new HttpsEngine(this, str, rawHeaders, httpConnection, retryableOutputStream, HttpsURLConnectionImpl.this);
        }

        public SecureCacheResponse getCacheResponse() {
            HttpsEngine httpsEngine = (HttpsEngine) this.httpEngine;
            if (httpsEngine != null) {
                return (SecureCacheResponse) httpsEngine.getCacheResponse();
            }
            return null;
        }

        public SSLSocket getSSLSocket() {
            HttpsEngine httpsEngine = (HttpsEngine) this.httpEngine;
            if (httpsEngine != null) {
                return httpsEngine.sslSocket;
            }
            return null;
        }
    }

    /* JADX INFO: compiled from: ProGuard */
    private static class HttpsEngine extends HttpEngine {
        private final HttpsURLConnectionImpl enclosing;
        private SSLSocket sslSocket;

        private HttpsEngine(HttpURLConnectionImpl httpURLConnectionImpl, String str, RawHeaders rawHeaders, HttpConnection httpConnection, RetryableOutputStream retryableOutputStream, HttpsURLConnectionImpl httpsURLConnectionImpl) throws IOException {
            super(httpURLConnectionImpl, str, rawHeaders, httpConnection, retryableOutputStream);
            this.sslSocket = httpConnection != null ? httpConnection.getSecureSocketIfConnected() : null;
            this.enclosing = httpsURLConnectionImpl;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http.HttpEngine
        protected void connect() throws IOException {
            boolean zMakeSslConnection;
            try {
                zMakeSslConnection = makeSslConnection(true);
            } catch (IOException e) {
                if ((e instanceof SSLHandshakeException) && (e.getCause() instanceof CertificateException)) {
                    throw e;
                }
                release(false);
                zMakeSslConnection = makeSslConnection(false);
            }
            if (!zMakeSslConnection) {
                this.sslSocket = this.connection.verifySecureSocketHostname(this.enclosing.getHostnameVerifier());
            }
        }

        private boolean makeSslConnection(boolean z) throws IOException {
            if (this.connection == null) {
                this.connection = openSocketConnection();
                if (this.connection.getAddress().getProxy() != null) {
                    makeTunnel(this.policy, this.connection, getRequestHeaders());
                }
            }
            this.sslSocket = this.connection.getSecureSocketIfConnected();
            if (this.sslSocket != null) {
                return true;
            }
            this.connection.setupSecureSocket(this.enclosing.getSSLSocketFactory(), z);
            return false;
        }

        /* JADX WARN: Switch 'out' block B:3:0x0004 for B:4:0x0017 already processed. Defaulting to fallback option. */
        private void makeTunnel(HttpURLConnectionImpl httpURLConnectionImpl, HttpConnection httpConnection, RequestHeaders requestHeaders) throws IOException {
            RawHeaders headers = requestHeaders.getHeaders();
            while (true) {
                ProxyConnectEngine proxyConnectEngine = new ProxyConnectEngine(httpURLConnectionImpl, headers, httpConnection);
                proxyConnectEngine.sendRequest();
                proxyConnectEngine.readResponse();
                int responseCode = proxyConnectEngine.getResponseCode();
                switch (proxyConnectEngine.getResponseCode()) {
                    case HttpResponseCode.OK /* 200 */:
                        return;
                    case 407:
                        RawHeaders rawHeaders = new RawHeaders(headers);
                        if (!httpURLConnectionImpl.processAuthHeader(407, proxyConnectEngine.getResponseHeaders(), rawHeaders)) {
                            throw new IOException("Failed to authenticate with proxy");
                        }
                        headers = rawHeaders;
                        break;
                        break;
                    default:
                        throw new IOException("Unexpected response code for CONNECT: " + responseCode);
                }
            }
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http.HttpEngine
        protected boolean acceptCacheResponseType(CacheResponse cacheResponse) {
            return cacheResponse instanceof SecureCacheResponse;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http.HttpEngine
        protected boolean includeAuthorityInRequestLine() {
            return false;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http.HttpEngine
        protected HttpURLConnection getHttpConnectionToCache() {
            return this.enclosing;
        }
    }

    /* JADX INFO: compiled from: ProGuard */
    private static class ProxyConnectEngine extends HttpEngine {
        public ProxyConnectEngine(HttpURLConnectionImpl httpURLConnectionImpl, RawHeaders rawHeaders, HttpConnection httpConnection) throws IOException {
            super(httpURLConnectionImpl, HttpEngine.CONNECT, rawHeaders, httpConnection, null);
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http.HttpEngine
        protected RawHeaders getNetworkRequestHeaders() throws IOException {
            RequestHeaders requestHeaders = getRequestHeaders();
            URL url = this.policy.getURL();
            RawHeaders rawHeaders = new RawHeaders();
            rawHeaders.setStatusLine("CONNECT " + url.getHost() + ":" + URLs.getEffectivePort(url) + " HTTP/1.1");
            String host = requestHeaders.getHost();
            if (host == null) {
                host = getOriginAddress(url);
            }
            rawHeaders.set("Host", host);
            String userAgent = requestHeaders.getUserAgent();
            if (userAgent == null) {
                userAgent = getDefaultUserAgent();
            }
            rawHeaders.set("User-Agent", userAgent);
            String proxyAuthorization = requestHeaders.getProxyAuthorization();
            if (proxyAuthorization != null) {
                rawHeaders.set("Proxy-Authorization", proxyAuthorization);
            }
            rawHeaders.set("Proxy-Connection", "Keep-Alive");
            return rawHeaders;
        }

        @Override // com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http.HttpEngine
        protected boolean requiresTunnel() {
            return true;
        }
    }
}
