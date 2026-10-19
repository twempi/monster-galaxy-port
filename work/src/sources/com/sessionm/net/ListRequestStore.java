package com.sessionm.net;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public class ListRequestStore implements RequestStore {
    List<Request> requestList = new ArrayList();

    @Override // com.sessionm.net.RequestStore
    public void add(Request request) {
        this.requestList.add(request);
    }

    @Override // com.sessionm.net.RequestStore
    public void insert(Request request, int i) {
        this.requestList.add(i, request);
    }

    @Override // com.sessionm.net.RequestStore
    public void remove(Request request) {
        this.requestList.remove(request);
    }

    @Override // com.sessionm.net.RequestStore
    public Request nextRequest() {
        if (this.requestList.size() > 0) {
            return this.requestList.get(0);
        }
        return null;
    }
}
