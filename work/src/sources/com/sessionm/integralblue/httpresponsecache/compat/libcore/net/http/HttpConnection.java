package com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http;

import com.sessionm.integralblue.httpresponsecache.compat.URIs;
import com.sessionm.integralblue.httpresponsecache.compat.libcore.io.IoUtils;
import com.sessionm.integralblue.httpresponsecache.compat.libcore.util.Objects;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
final class HttpConnection {
    private final Address address;
    private InputStream inputStream;
    private OutputStream outputStream;
    private final Socket socket;
    private InputStream sslInputStream;
    private OutputStream sslOutputStream;
    private SSLSocket sslSocket;
    private SSLSocket unverifiedSocket;

    private HttpConnection(Address address, int i) throws IOException {
        this.address = address;
        Socket socket = null;
        InetAddress[] allByName = InetAddress.getAllByName(address.socketHost);
        for (int i2 = 0; i2 < allByName.length; i2++) {
            if (address.proxy == null || address.proxy.type() == Proxy.Type.HTTP) {
                socket = new Socket();
            } else {
                socket = new Socket(address.proxy);
            }
            try {
                socket.connect(new InetSocketAddress(allByName[i2], address.socketPort), i);
                break;
            } catch (IOException e) {
                if (i2 == allByName.length - 1) {
                    throw e;
                }
            }
        }
        this.socket = socket;
    }

    public static HttpConnection connect(URI uri, Proxy proxy, boolean z, int i) throws IOException {
        if (proxy != null) {
            return HttpConnectionPool.INSTANCE.get(proxy.type() == Proxy.Type.DIRECT ? new Address(uri) : new Address(uri, proxy, z), i);
        }
        ProxySelector proxySelector = ProxySelector.getDefault();
        List<Proxy> listSelect = proxySelector.select(uri);
        if (listSelect != null) {
            for (Proxy proxy2 : listSelect) {
                if (proxy2.type() != Proxy.Type.DIRECT) {
                    try {
                        return HttpConnectionPool.INSTANCE.get(new Address(uri, proxy2, z), i);
                    } catch (IOException e) {
                        proxySelector.connectFailed(uri, proxy2.address(), e);
                    }
                }
            }
        }
        return HttpConnectionPool.INSTANCE.get(new Address(uri), i);
    }

    public void closeSocketAndStreams() {
        IoUtils.closeQuietly(this.sslOutputStream);
        IoUtils.closeQuietly(this.sslInputStream);
        IoUtils.closeQuietly((Socket) this.sslSocket);
        IoUtils.closeQuietly(this.outputStream);
        IoUtils.closeQuietly(this.inputStream);
        IoUtils.closeQuietly(this.socket);
    }

    public void setSoTimeout(int i) throws SocketException {
        this.socket.setSoTimeout(i);
    }

    public OutputStream getOutputStream() throws IOException {
        if (this.sslSocket != null) {
            if (this.sslOutputStream == null) {
                this.sslOutputStream = this.sslSocket.getOutputStream();
            }
            return this.sslOutputStream;
        }
        if (this.outputStream == null) {
            this.outputStream = this.socket.getOutputStream();
        }
        return this.outputStream;
    }

    public InputStream getInputStream() throws IOException {
        if (this.sslSocket != null) {
            if (this.sslInputStream == null) {
                this.sslInputStream = this.sslSocket.getInputStream();
            }
            return this.sslInputStream;
        }
        if (this.inputStream == null) {
            this.inputStream = this.address.requiresTunnel ? this.socket.getInputStream() : new BufferedInputStream(this.socket.getInputStream(), 128);
        }
        return this.inputStream;
    }

    protected Socket getSocket() {
        return this.sslSocket != null ? this.sslSocket : this.socket;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setupSecureSocket(SSLSocketFactory sSLSocketFactory, boolean z) throws IOException {
        this.unverifiedSocket = (SSLSocket) sSLSocketFactory.createSocket(this.socket, this.address.uriHost, this.address.uriPort, true);
        if (z) {
            try {
                Class<?> cls = this.unverifiedSocket.getClass();
                cls.getMethod("setEnabledCompressionMethods", String[].class).invoke(this.unverifiedSocket, new String[]{"ZLIB"});
                cls.getMethod("setUseSessionTickets", Boolean.TYPE).invoke(this.unverifiedSocket, true);
                cls.getMethod("setHostname", String.class).invoke(this.unverifiedSocket, this.address.socketHost);
            } catch (Exception e) {
                this.unverifiedSocket.setEnabledProtocols(new String[]{"SSLv3"});
            }
        } else {
            this.unverifiedSocket.setEnabledProtocols(new String[]{"SSLv3"});
        }
        this.unverifiedSocket.startHandshake();
    }

    public SSLSocket verifySecureSocketHostname(HostnameVerifier hostnameVerifier) throws IOException {
        if (!hostnameVerifier.verify(this.address.uriHost, this.unverifiedSocket.getSession())) {
            throw new IOException("Hostname '" + this.address.uriHost + "' was not verified");
        }
        this.sslSocket = this.unverifiedSocket;
        return this.sslSocket;
    }

    public SSLSocket getSecureSocketIfConnected() {
        return this.sslSocket;
    }

    boolean isStale() throws IOException {
        if (!isEligibleForRecycling()) {
            return true;
        }
        InputStream inputStream = getInputStream();
        if (inputStream.available() > 0) {
            return false;
        }
        Socket socket = getSocket();
        int soTimeout = socket.getSoTimeout();
        try {
            socket.setSoTimeout(1);
            inputStream.mark(1);
            if (inputStream.read() == -1) {
                return true;
            }
            inputStream.reset();
            return false;
        } catch (SocketTimeoutException e) {
            return false;
        } catch (IOException e2) {
            return true;
        } finally {
            socket.setSoTimeout(soTimeout);
        }
    }

    protected boolean isEligibleForRecycling() {
        return (this.socket.isClosed() || this.socket.isInputShutdown() || this.socket.isOutputShutdown()) ? false : true;
    }

    /* JADX INFO: compiled from: ProGuard */
    public static final class Address {
        private final Proxy proxy;
        private final boolean requiresTunnel;
        private final String socketHost;
        private final int socketPort;
        private final String uriHost;
        private final int uriPort;

        public Address(URI uri) {
            this.proxy = null;
            this.requiresTunnel = false;
            this.uriHost = uri.getHost();
            this.uriPort = URIs.getEffectivePort(uri);
            this.socketHost = this.uriHost;
            this.socketPort = this.uriPort;
        }

        public Address(URI uri, Proxy proxy, boolean z) {
            this.proxy = proxy;
            this.requiresTunnel = z;
            this.uriHost = uri.getHost();
            this.uriPort = URIs.getEffectivePort(uri);
            SocketAddress socketAddressAddress = proxy.address();
            if (!(socketAddressAddress instanceof InetSocketAddress)) {
                throw new IllegalArgumentException("Proxy.address() is not an InetSocketAddress: " + socketAddressAddress.getClass());
            }
            InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddressAddress;
            this.socketHost = inetSocketAddress.getHostName();
            this.socketPort = inetSocketAddress.getPort();
        }

        public Proxy getProxy() {
            return this.proxy;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Address)) {
                return false;
            }
            Address address = (Address) obj;
            return Objects.equal(this.proxy, address.proxy) && this.uriHost.equals(address.uriHost) && this.uriPort == address.uriPort && this.requiresTunnel == address.requiresTunnel;
        }

        public int hashCode() {
            return (((this.proxy != null ? this.proxy.hashCode() : 0) + ((((this.uriHost.hashCode() + 527) * 31) + this.uriPort) * 31)) * 31) + (this.requiresTunnel ? 1 : 0);
        }

        public HttpConnection connect(int i) throws IOException {
            return new HttpConnection(this, i);
        }
    }
}
