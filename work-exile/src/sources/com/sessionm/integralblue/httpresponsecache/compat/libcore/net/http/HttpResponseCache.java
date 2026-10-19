package com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http;

import com.sessionm.integralblue.httpresponsecache.compat.Charsets;
import com.sessionm.integralblue.httpresponsecache.compat.Strings;
import com.sessionm.integralblue.httpresponsecache.compat.java.net.ExtendedResponseCache;
import com.sessionm.integralblue.httpresponsecache.compat.java.net.ResponseSource;
import com.sessionm.integralblue.httpresponsecache.compat.libcore.io.Base64;
import com.sessionm.integralblue.httpresponsecache.compat.libcore.io.IoUtils;
import com.sessionm.integralblue.httpresponsecache.compat.libcore.io.Streams;
import com.sessionm.jakewharton.DiskLruCache;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.CacheRequest;
import java.net.CacheResponse;
import java.net.HttpURLConnection;
import java.net.ResponseCache;
import java.net.SecureCacheResponse;
import java.net.URI;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class HttpResponseCache extends ResponseCache implements ExtendedResponseCache {
    private static final int ENTRY_BODY = 1;
    private static final int ENTRY_COUNT = 2;
    private static final int ENTRY_METADATA = 0;
    private static final int VERSION = 201105;
    private final DiskLruCache cache;
    private int hitCount;
    private int networkCount;
    private int requestCount;
    private int writeAbortCount;
    private int writeSuccessCount;

    static /* synthetic */ int access$208(HttpResponseCache httpResponseCache) {
        int i = httpResponseCache.writeSuccessCount;
        httpResponseCache.writeSuccessCount = i + 1;
        return i;
    }

    static /* synthetic */ int access$308(HttpResponseCache httpResponseCache) {
        int i = httpResponseCache.writeAbortCount;
        httpResponseCache.writeAbortCount = i + 1;
        return i;
    }

    public HttpResponseCache(File file, long j) throws IOException {
        this.cache = DiskLruCache.open(file, VERSION, 2, j);
    }

    private String uriToKey(URI uri) {
        try {
            return Strings.bytesToHexString(MessageDigest.getInstance("MD5").digest(Strings.getBytes(uri.toString(), Charsets.UTF_8)), false);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }

    @Override // java.net.ResponseCache
    public CacheResponse get(URI uri, String str, Map<String, List<String>> map) {
        try {
            DiskLruCache.Snapshot snapshot = this.cache.get(uriToKey(uri));
            if (snapshot == null) {
                return null;
            }
            Entry entry = new Entry(new BufferedInputStream(snapshot.getInputStream(0)));
            if (!entry.matches(uri, str, map)) {
                snapshot.close();
                return null;
            }
            InputStream inputStreamNewBodyInputStream = newBodyInputStream(snapshot);
            return entry.isHttps() ? entry.newSecureCacheResponse(inputStreamNewBodyInputStream) : entry.newCacheResponse(inputStreamNewBodyInputStream);
        } catch (IOException e) {
            return null;
        }
    }

    private InputStream newBodyInputStream(final DiskLruCache.Snapshot snapshot) {
        return new FilterInputStream(snapshot.getInputStream(1)) { // from class: com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http.HttpResponseCache.1
            @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                snapshot.close();
                super.close();
            }
        };
    }

    @Override // java.net.ResponseCache
    public CacheRequest put(URI uri, URLConnection uRLConnection) throws IOException {
        HttpEngine httpEngine;
        DiskLruCache.Editor editor;
        if (!(uRLConnection instanceof HttpURLConnection)) {
            return null;
        }
        HttpURLConnection httpURLConnection = (HttpURLConnection) uRLConnection;
        String requestMethod = httpURLConnection.getRequestMethod();
        String strUriToKey = uriToKey(uri);
        if (requestMethod.equals(HttpEngine.POST) || requestMethod.equals(HttpEngine.PUT) || requestMethod.equals(HttpEngine.DELETE)) {
            try {
                this.cache.remove(strUriToKey);
                return null;
            } catch (IOException e) {
                return null;
            }
        }
        if (!requestMethod.equals(HttpEngine.GET) || (httpEngine = getHttpEngine(httpURLConnection)) == null) {
            return null;
        }
        ResponseHeaders responseHeaders = httpEngine.getResponseHeaders();
        if (responseHeaders.hasVaryAll()) {
            return null;
        }
        Entry entry = new Entry(uri, httpEngine.getRequestHeaders().getHeaders().getAll(responseHeaders.getVaryFields()), httpURLConnection);
        try {
            DiskLruCache.Editor editorEdit = this.cache.edit(strUriToKey);
            if (editorEdit == null) {
                return null;
            }
            try {
                entry.writeTo(editorEdit);
                return new CacheRequestImpl(editorEdit);
            } catch (IOException e2) {
                editor = editorEdit;
                if (editor == null) {
                    return null;
                }
                try {
                    editor.abort();
                    return null;
                } catch (IOException e3) {
                    return null;
                }
            }
        } catch (IOException e4) {
            editor = null;
        }
    }

    private HttpEngine getHttpEngine(HttpURLConnection httpURLConnection) {
        if (httpURLConnection instanceof HttpURLConnectionImpl) {
            return ((HttpURLConnectionImpl) httpURLConnection).getHttpEngine();
        }
        if (httpURLConnection instanceof HttpsURLConnectionImpl) {
            return ((HttpsURLConnectionImpl) httpURLConnection).getHttpEngine();
        }
        return null;
    }

    public DiskLruCache getCache() {
        return this.cache;
    }

    public synchronized int getWriteAbortCount() {
        return this.writeAbortCount;
    }

    public synchronized int getWriteSuccessCount() {
        return this.writeSuccessCount;
    }

    @Override // com.sessionm.integralblue.httpresponsecache.compat.java.net.ExtendedResponseCache
    public synchronized void trackResponse(ResponseSource responseSource) {
        this.requestCount++;
        switch (responseSource) {
            case CACHE:
                this.hitCount++;
                break;
            case CONDITIONAL_CACHE:
            case NETWORK:
                this.networkCount++;
                break;
        }
    }

    @Override // com.sessionm.integralblue.httpresponsecache.compat.java.net.ExtendedResponseCache
    public synchronized void trackConditionalCacheHit() {
        this.hitCount++;
    }

    public synchronized int getNetworkCount() {
        return this.networkCount;
    }

    public synchronized int getHitCount() {
        return this.hitCount;
    }

    public synchronized int getRequestCount() {
        return this.requestCount;
    }

    /* JADX INFO: compiled from: ProGuard */
    private final class CacheRequestImpl extends CacheRequest {
        private OutputStream body;
        private OutputStream cacheOut;
        private boolean done;
        private final DiskLruCache.Editor editor;

        public CacheRequestImpl(final DiskLruCache.Editor editor) throws IOException {
            this.editor = editor;
            this.cacheOut = editor.newOutputStream(1);
            this.body = new FilterOutputStream(this.cacheOut) { // from class: com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http.HttpResponseCache.CacheRequestImpl.1
                @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
                public void close() throws IOException {
                    synchronized (HttpResponseCache.this) {
                        if (!CacheRequestImpl.this.done) {
                            CacheRequestImpl.this.done = true;
                            HttpResponseCache.access$208(HttpResponseCache.this);
                            super.close();
                            editor.commit();
                        }
                    }
                }
            };
        }

        @Override // java.net.CacheRequest
        public void abort() {
            synchronized (HttpResponseCache.this) {
                if (!this.done) {
                    this.done = true;
                    HttpResponseCache.access$308(HttpResponseCache.this);
                    IoUtils.closeQuietly(this.cacheOut);
                    try {
                        this.editor.abort();
                    } catch (IOException e) {
                    }
                }
            }
        }

        @Override // java.net.CacheRequest
        public OutputStream getBody() throws IOException {
            return this.body;
        }
    }

    /* JADX INFO: compiled from: ProGuard */
    private static final class Entry {
        private final String cipherSuite;
        private final Certificate[] localCertificates;
        private final Certificate[] peerCertificates;
        private final String requestMethod;
        private final RawHeaders responseHeaders;
        private final String uri;
        private final RawHeaders varyHeaders;

        public Entry(InputStream inputStream) throws IOException {
            try {
                this.uri = Streams.readAsciiLine(inputStream);
                this.requestMethod = Streams.readAsciiLine(inputStream);
                this.varyHeaders = new RawHeaders();
                int i = readInt(inputStream);
                for (int i2 = 0; i2 < i; i2++) {
                    this.varyHeaders.addLine(Streams.readAsciiLine(inputStream));
                }
                this.responseHeaders = new RawHeaders();
                this.responseHeaders.setStatusLine(Streams.readAsciiLine(inputStream));
                int i3 = readInt(inputStream);
                for (int i4 = 0; i4 < i3; i4++) {
                    this.responseHeaders.addLine(Streams.readAsciiLine(inputStream));
                }
                if (isHttps()) {
                    String asciiLine = Streams.readAsciiLine(inputStream);
                    if (!Strings.isEmpty(asciiLine)) {
                        throw new IOException("expected \"\" but was \"" + asciiLine + "\"");
                    }
                    this.cipherSuite = Streams.readAsciiLine(inputStream);
                    this.peerCertificates = readCertArray(inputStream);
                    this.localCertificates = readCertArray(inputStream);
                } else {
                    this.cipherSuite = null;
                    this.peerCertificates = null;
                    this.localCertificates = null;
                }
                inputStream.close();
            } catch (Throwable th) {
                inputStream.close();
                throw th;
            }
        }

        public Entry(URI uri, RawHeaders rawHeaders, HttpURLConnection httpURLConnection) {
            Certificate[] serverCertificates = null;
            this.uri = uri.toString();
            this.varyHeaders = rawHeaders;
            this.requestMethod = httpURLConnection.getRequestMethod();
            this.responseHeaders = RawHeaders.fromMultimap(httpURLConnection.getHeaderFields());
            if (isHttps()) {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) httpURLConnection;
                this.cipherSuite = httpsURLConnection.getCipherSuite();
                try {
                    serverCertificates = httpsURLConnection.getServerCertificates();
                } catch (SSLPeerUnverifiedException e) {
                }
                this.peerCertificates = serverCertificates;
                this.localCertificates = httpsURLConnection.getLocalCertificates();
                return;
            }
            this.cipherSuite = null;
            this.peerCertificates = null;
            this.localCertificates = null;
        }

        public void writeTo(DiskLruCache.Editor editor) throws IOException {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(editor.newOutputStream(0), Charsets.UTF_8));
            bufferedWriter.write(this.uri + '\n');
            bufferedWriter.write(this.requestMethod + '\n');
            bufferedWriter.write(Integer.toString(this.varyHeaders.length()) + '\n');
            for (int i = 0; i < this.varyHeaders.length(); i++) {
                bufferedWriter.write(this.varyHeaders.getFieldName(i) + ": " + this.varyHeaders.getValue(i) + '\n');
            }
            bufferedWriter.write(this.responseHeaders.getStatusLine() + '\n');
            bufferedWriter.write(Integer.toString(this.responseHeaders.length()) + '\n');
            for (int i2 = 0; i2 < this.responseHeaders.length(); i2++) {
                bufferedWriter.write(this.responseHeaders.getFieldName(i2) + ": " + this.responseHeaders.getValue(i2) + '\n');
            }
            if (isHttps()) {
                bufferedWriter.write(10);
                bufferedWriter.write(this.cipherSuite + '\n');
                writeCertArray(bufferedWriter, this.peerCertificates);
                writeCertArray(bufferedWriter, this.localCertificates);
            }
            bufferedWriter.close();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isHttps() {
            return this.uri.startsWith("https://");
        }

        private int readInt(InputStream inputStream) throws IOException {
            String asciiLine = Streams.readAsciiLine(inputStream);
            try {
                return Integer.parseInt(asciiLine);
            } catch (NumberFormatException e) {
                throw new IOException("expected an int but was \"" + asciiLine + "\"");
            }
        }

        private Certificate[] readCertArray(InputStream inputStream) throws IOException {
            int i = readInt(inputStream);
            if (i == -1) {
                return null;
            }
            try {
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                Certificate[] certificateArr = new Certificate[i];
                for (int i2 = 0; i2 < certificateArr.length; i2++) {
                    certificateArr[i2] = certificateFactory.generateCertificate(new ByteArrayInputStream(Base64.decode(Strings.getBytes(Streams.readAsciiLine(inputStream), Charsets.US_ASCII))));
                }
                return certificateArr;
            } catch (CertificateException e) {
                throw new com.sessionm.integralblue.httpresponsecache.compat.java.io.IOException(e);
            }
        }

        private void writeCertArray(Writer writer, Certificate[] certificateArr) throws IOException {
            if (certificateArr == null) {
                writer.write("-1\n");
                return;
            }
            try {
                writer.write(Integer.toString(certificateArr.length) + '\n');
                for (Certificate certificate : certificateArr) {
                    writer.write(Base64.encode(certificate.getEncoded()) + '\n');
                }
            } catch (CertificateEncodingException e) {
                throw new com.sessionm.integralblue.httpresponsecache.compat.java.io.IOException(e);
            }
        }

        public boolean matches(URI uri, String str, Map<String, List<String>> map) {
            return this.uri.equals(uri.toString()) && this.requestMethod.equals(str) && new ResponseHeaders(uri, this.responseHeaders).varyMatches(this.varyHeaders.toMultimap(), map);
        }

        public CacheResponse newCacheResponse(final InputStream inputStream) {
            return new CacheResponse() { // from class: com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http.HttpResponseCache.Entry.1
                @Override // java.net.CacheResponse
                public Map<String, List<String>> getHeaders() {
                    return Entry.this.responseHeaders.toMultimap();
                }

                @Override // java.net.CacheResponse
                public InputStream getBody() {
                    return inputStream;
                }
            };
        }

        public SecureCacheResponse newSecureCacheResponse(final InputStream inputStream) {
            return new SecureCacheResponse() { // from class: com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http.HttpResponseCache.Entry.2
                @Override // java.net.CacheResponse
                public Map<String, List<String>> getHeaders() {
                    return Entry.this.responseHeaders.toMultimap();
                }

                @Override // java.net.CacheResponse
                public InputStream getBody() {
                    return inputStream;
                }

                @Override // java.net.SecureCacheResponse
                public String getCipherSuite() {
                    return Entry.this.cipherSuite;
                }

                @Override // java.net.SecureCacheResponse
                public List<Certificate> getServerCertificateChain() throws SSLPeerUnverifiedException {
                    if (Entry.this.peerCertificates != null && Entry.this.peerCertificates.length != 0) {
                        return Arrays.asList((Object[]) Entry.this.peerCertificates.clone());
                    }
                    throw new SSLPeerUnverifiedException(null);
                }

                @Override // java.net.SecureCacheResponse
                public Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
                    if (Entry.this.peerCertificates != null && Entry.this.peerCertificates.length != 0) {
                        return ((X509Certificate) Entry.this.peerCertificates[0]).getSubjectX500Principal();
                    }
                    throw new SSLPeerUnverifiedException(null);
                }

                @Override // java.net.SecureCacheResponse
                public List<Certificate> getLocalCertificateChain() {
                    if (Entry.this.localCertificates != null && Entry.this.localCertificates.length != 0) {
                        return Arrays.asList((Object[]) Entry.this.localCertificates.clone());
                    }
                    return null;
                }

                @Override // java.net.SecureCacheResponse
                public Principal getLocalPrincipal() {
                    if (Entry.this.localCertificates != null && Entry.this.localCertificates.length != 0) {
                        return ((X509Certificate) Entry.this.localCertificates[0]).getSubjectX500Principal();
                    }
                    return null;
                }
            };
        }
    }
}
