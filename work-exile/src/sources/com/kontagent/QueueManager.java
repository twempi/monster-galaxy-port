package com.kontagent;

import android.content.Context;
import com.kontagent.connectivity.ConnectivityListener;
import com.kontagent.connectivity.ConnectivityTracker;
import com.kontagent.queue.KontagentQueue;
import com.kontagent.queue.OfflineQueue;
import com.kontagent.queue.OnlineQueue;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class QueueManager extends Stateful {
    private static final int MAX_FLAPS = 10;
    private static final String TAG = QueueManager.class.getSimpleName();
    private KontagentQueue activeQueue;
    private ConnectivityListener connectivityListener;
    private final ConnectivityTracker connectivityTracker;
    private int flapCount;
    private final OfflineQueue offlineQueue;
    private final OnlineQueue onlineQueue;

    public QueueManager(Context context, ConnectivityTracker connectivityTracker, OfflineQueue offlineQueue, OnlineQueue onlineQueue) {
        this.flapCount = 0;
        this.connectivityListener = new ConnectivityListener() { // from class: com.kontagent.QueueManager.1
            @Override // com.kontagent.connectivity.ConnectivityListener
            public void onConnectivityChanged(ConnectivityTracker tracker, boolean hasConnectivity) {
                QueueManager.this.networkStatusChanged();
            }
        };
        this.offlineQueue = offlineQueue;
        this.onlineQueue = onlineQueue;
        this.connectivityTracker = connectivityTracker;
    }

    public QueueManager(Context context) {
        this.flapCount = 0;
        this.connectivityListener = new ConnectivityListener() { // from class: com.kontagent.QueueManager.1
            @Override // com.kontagent.connectivity.ConnectivityListener
            public void onConnectivityChanged(ConnectivityTracker tracker, boolean hasConnectivity) {
                QueueManager.this.networkStatusChanged();
            }
        };
        this.offlineQueue = new OfflineQueue(context);
        this.onlineQueue = new OnlineQueue(this);
        this.connectivityTracker = new ConnectivityTracker(context);
    }

    @Override // com.kontagent.Stateful
    protected synchronized void onStart() {
        KontagentLog.d(TAG, "onStart()");
        this.offlineQueue.start();
        this.onlineQueue.start();
        changeActiveQueue(this.offlineQueue);
    }

    @Override // com.kontagent.Stateful
    protected synchronized void onResume() {
        KontagentLog.d(TAG, "onResume()");
        this.onlineQueue.resume();
        this.offlineQueue.resume();
        this.connectivityTracker.startTracking();
        this.connectivityTracker.setListener(this.connectivityListener);
    }

    @Override // com.kontagent.Stateful
    protected synchronized void onPause() {
        KontagentLog.d(TAG, "onPause()");
        changeActiveQueue(this.offlineQueue);
        this.connectivityTracker.setListener(null);
        this.connectivityTracker.stopTracking();
        this.onlineQueue.pause();
        this.offlineQueue.pause();
    }

    @Override // com.kontagent.Stateful
    protected synchronized void onStop() {
        KontagentLog.d(TAG, "onStop()");
        this.activeQueue = null;
        this.onlineQueue.stop();
        this.offlineQueue.stop();
    }

    public synchronized OfflineQueue getOfflineQueue() {
        return this.offlineQueue;
    }

    public synchronized OnlineQueue getOnlineQueue() {
        return this.onlineQueue;
    }

    public synchronized ConnectivityTracker getConnectivityTracker() {
        return this.connectivityTracker;
    }

    private synchronized void changeActiveQueue(KontagentQueue queue) {
        KontagentLog.d(TAG, "changeActiveQueue(): " + queue.getClass().getSimpleName());
        if (this.activeQueue != queue) {
            if (this.flapCount > 10) {
                if (this.activeQueue == this.onlineQueue) {
                    this.activeQueue = this.offlineQueue;
                }
            } else if (this.activeQueue != queue) {
                if (this.activeQueue == this.offlineQueue) {
                    this.offlineQueue.convertToOnline(this.onlineQueue);
                } else if (this.activeQueue == this.onlineQueue) {
                    this.onlineQueue.convertToOffline(this.offlineQueue);
                }
                this.activeQueue = queue;
                this.flapCount++;
            }
        }
    }

    public synchronized void networkStatusChanged() {
        KontagentLog.d(TAG, "networkStatusChanged()");
        if (this.connectivityTracker.isOnline()) {
            KontagentLog.d("Switchin to ONLINE queue");
            changeActiveQueue(this.onlineQueue);
        } else {
            KontagentLog.d("Switchin to OFFLINE queue");
            changeActiveQueue(this.offlineQueue);
        }
    }

    public synchronized void enqueue(String url) {
        KontagentLog.d(TAG, "enqueue(): " + url);
        if (this.activeQueue != null) {
            this.activeQueue.enqueue(url);
        } else {
            KontagentLog.e("Cannot engueue url. Dropped url: " + url, null);
        }
    }

    public synchronized void error() {
        KontagentLog.d(TAG, "error()");
        changeActiveQueue(this.offlineQueue);
    }

    public synchronized boolean isOnline() {
        return this.connectivityTracker.isOnline() && this.activeQueue == this.onlineQueue && this.onlineQueue.getState() == Stateful.State.RUNNING;
    }
}
