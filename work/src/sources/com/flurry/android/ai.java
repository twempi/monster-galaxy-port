package com.flurry.android;

import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import org.apache.http.conn.ssl.SSLSocketFactory;

/* JADX INFO: loaded from: classes.dex */
final class ai extends SSLSocketFactory {
    private SSLContext a;

    public ai(FlurryAgent flurryAgent, KeyStore keyStore) throws KeyManagementException {
        super(keyStore);
        this.a = SSLContext.getInstance("TLS");
        this.a.init(null, new TrustManager[]{new n()}, null);
    }

    @Override // org.apache.http.conn.ssl.SSLSocketFactory, org.apache.http.conn.scheme.LayeredSocketFactory
    public final Socket createSocket(Socket socket, String str, int i, boolean z) {
        return this.a.getSocketFactory().createSocket(socket, str, i, z);
    }

    @Override // org.apache.http.conn.ssl.SSLSocketFactory, org.apache.http.conn.scheme.SocketFactory
    public final Socket createSocket() {
        return this.a.getSocketFactory().createSocket();
    }
}
