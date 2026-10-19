package com.sessionm.net;

import com.sessionm.net.http.Client;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class RequestQueue implements RequestListener {
    private boolean started = false;
    private Request currentRequest = null;
    private final RequestStore store = new ListRequestStore();
    private final Map<String, List<RequestListener>> requestListeners = new ConcurrentHashMap();
    private final HttpClient client = new Client();

    public HttpClient getHttpClient() {
        return this.client;
    }

    public synchronized void start() {
        this.started = true;
    }

    public synchronized void stop() {
        this.started = false;
        this.currentRequest = null;
    }

    public synchronized void enqueRequest(Request request) {
        if (request.getType().equals(Request.Type.SESSION_START) || request.getType().equals(Request.Type.SESSION_END)) {
            stop();
            sendRequest(request);
        } else if (this.started) {
            this.store.add(request);
            flush();
        }
    }

    private void sendRequest(Request request) {
        this.currentRequest = request;
        request.setListener(this);
        request.setClient(getHttpClient());
        request.send();
    }

    public synchronized void flush() {
        if (this.currentRequest == null) {
            Request requestNextRequest = this.store.nextRequest();
            if (requestNextRequest != null) {
                sendRequest(requestNextRequest);
            }
        } else if (this.currentRequest.getError() != null) {
            sendRequest(this.currentRequest);
        }
    }

    public synchronized void addRequestListener(Request request, RequestListener requestListener) {
        List<RequestListener> arrayList = this.requestListeners.get(request.getID());
        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }
        arrayList.add(requestListener);
        this.requestListeners.put(request.getID(), arrayList);
    }

    public void addRequestQueueListener(RequestQueueListener requestQueueListener) {
    }

    public void removeRequestQueueListener(RequestQueueListener requestQueueListener) {
    }

    @Override // com.sessionm.net.RequestListener
    public void onRequestSent(Request request) {
        List<RequestListener> list = this.requestListeners.get(request.getID());
        if (list != null) {
            Iterator<RequestListener> it = list.iterator();
            while (it.hasNext()) {
                it.next().onRequestSent(request);
            }
        }
    }

    @Override // com.sessionm.net.RequestListener
    public void onReplyReceived(Request request) {
        List<RequestListener> list = this.requestListeners.get(request.getID());
        if (list != null) {
            Iterator<RequestListener> it = list.iterator();
            while (it.hasNext()) {
                it.next().onReplyReceived(request);
            }
        }
        if (request.getError() == null || request.statusCode() == 404) {
            synchronized (this) {
                this.store.remove(request);
                this.requestListeners.remove(request.getID());
                this.currentRequest = null;
                if (this.started) {
                    flush();
                }
            }
        }
    }
}
