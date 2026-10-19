package com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
final class HttpConnectionPool {
    public static final HttpConnectionPool INSTANCE = new HttpConnectionPool();
    private final HashMap<HttpConnection.Address, List<HttpConnection>> connectionPool = new HashMap<>();
    private final int maxConnections;

    private HttpConnectionPool() {
        String property = System.getProperty("http.keepAlive");
        if (property != null && !Boolean.parseBoolean(property)) {
            this.maxConnections = 0;
        } else {
            String property2 = System.getProperty("http.maxConnections");
            this.maxConnections = property2 != null ? Integer.parseInt(property2) : 5;
        }
    }

    public HttpConnection get(HttpConnection.Address address, int i) throws IOException {
        synchronized (this.connectionPool) {
            List<HttpConnection> list = this.connectionPool.get(address);
            if (list != null) {
                while (!list.isEmpty()) {
                    HttpConnection httpConnectionRemove = list.remove(list.size() - 1);
                    if (!httpConnectionRemove.isStale()) {
                        return httpConnectionRemove;
                    }
                }
                this.connectionPool.remove(address);
            }
            return address.connect(i);
        }
    }

    public void recycle(HttpConnection httpConnection) {
        if (this.maxConnections > 0 && httpConnection.isEligibleForRecycling()) {
            HttpConnection.Address address = httpConnection.getAddress();
            synchronized (this.connectionPool) {
                List<HttpConnection> arrayList = this.connectionPool.get(address);
                if (arrayList == null) {
                    arrayList = new ArrayList<>();
                    this.connectionPool.put(address, arrayList);
                }
                if (arrayList.size() < this.maxConnections) {
                    arrayList.add(httpConnection);
                    return;
                }
            }
        }
        httpConnection.closeSocketAndStreams();
    }
}
