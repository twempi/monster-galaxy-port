package com.kontagent.util;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class NetworkConnectivityError extends Exception {
    private static final long serialVersionUID = 1;

    public NetworkConnectivityError() {
    }

    public NetworkConnectivityError(String detailMessage) {
        super(detailMessage);
    }

    public NetworkConnectivityError(Throwable throwable) {
        super(throwable);
    }

    public NetworkConnectivityError(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
