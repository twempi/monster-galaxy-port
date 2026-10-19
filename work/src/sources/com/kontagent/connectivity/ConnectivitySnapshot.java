package com.kontagent.connectivity;

/* JADX INFO: loaded from: classes.dex */
public class ConnectivitySnapshot {
    private int mConnectivityCounter;
    private final ConnectivityTracker mTracker;

    public ConnectivitySnapshot(ConnectivityTracker tracker) {
        this.mTracker = tracker;
        this.mConnectivityCounter = tracker.getCounter();
    }

    public void sync() {
        this.mConnectivityCounter = this.mTracker.getCounter();
    }

    public boolean isChanged() {
        return this.mConnectivityCounter != this.mTracker.getCounter();
    }
}
