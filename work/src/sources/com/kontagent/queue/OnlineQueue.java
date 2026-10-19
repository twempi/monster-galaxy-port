package com.kontagent.queue;

import com.kontagent.KontagentLog;
import com.kontagent.QueueManager;
import com.kontagent.Stateful;
import com.kontagent.util.NetworkConnectivityError;
import com.kontagent.util.NetworkUtil;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/* JADX INFO: loaded from: classes.dex */
public class OnlineQueue extends Stateful implements KontagentQueue {
    private static final long REQUEST_RATE = 100;
    private static final String TAG = OnlineQueue.class.getSimpleName();
    private QueueManager queueManager;
    private SendThread sender;
    private final LinkedBlockingQueue<String> mUrls = new LinkedBlockingQueue<>();
    private final Object mCurrentUrlSync = new Object();
    private String mCurrentUrl = null;

    public OnlineQueue(QueueManager queueManager) {
        this.queueManager = queueManager;
    }

    protected void setQueueManager(QueueManager queueManager) {
        this.queueManager = queueManager;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changed() {
        setChanged();
        notifyObservers();
    }

    @Override // com.kontagent.Stateful
    protected void onStart() {
        KontagentLog.d(TAG, "onStart()");
        this.sender = new SendThread();
        this.sender.start();
    }

    @Override // com.kontagent.Stateful
    protected void onPause() {
        convertToOffline(this.queueManager.getOfflineQueue());
    }

    @Override // com.kontagent.Stateful
    protected void onStop() {
        KontagentLog.d(TAG, "onStop()");
        this.sender.interrupt();
        this.sender = null;
    }

    @Override // com.kontagent.queue.KontagentQueue
    public boolean enqueue(String url) {
        KontagentLog.d(TAG, "enqueue(): " + url);
        boolean result = this.mUrls.offer(url);
        changed();
        return result;
    }

    @Override // com.kontagent.queue.KontagentQueue
    public boolean enqueue(Collection<String> urls) {
        KontagentLog.d(TAG, "enqueue(Collection)");
        boolean result = this.mUrls.addAll(urls);
        changed();
        return result;
    }

    public int size() {
        return this.mUrls.size();
    }

    public void convertToOffline(OfflineQueue offlineQueue) {
        KontagentLog.d(TAG, "convertToOffline()");
        List<String> pending = new LinkedList<>();
        this.mUrls.drainTo(pending);
        synchronized (this.mCurrentUrlSync) {
            if (this.mCurrentUrl != null) {
                offlineQueue.enqueue(this.mCurrentUrl);
                this.mCurrentUrl = null;
            }
        }
        offlineQueue.enqueue(pending);
        changed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isOnline() {
        return this.queueManager.isOnline();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void failed(String url, Throwable ex) {
        this.queueManager.error();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dropped(String url) {
    }

    private class SendThread extends Thread {
        private long lastSentTimestamp;

        private SendThread() {
            this.lastSentTimestamp = 0L;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                runSafe();
            } catch (Throwable ex) {
                KontagentLog.e("Unexpected error in OnlineQueue thread.", ex);
            }
        }

        public void runSafe() {
            setName("KontagentAgent");
            while (!isInterrupted()) {
                try {
                    String url = (String) OnlineQueue.this.mUrls.take();
                    synchronized (OnlineQueue.this.mCurrentUrlSync) {
                        OnlineQueue.this.mCurrentUrl = url;
                    }
                    send(url);
                    synchronized (OnlineQueue.this.mCurrentUrlSync) {
                        OnlineQueue.this.mCurrentUrl = null;
                    }
                    OnlineQueue.this.changed();
                } catch (InterruptedException e) {
                }
            }
            OnlineQueue.this.stop();
        }

        public void send(String url) {
            KontagentLog.i(OnlineQueue.TAG, "Sending HTTP request " + url);
            try {
                long delta = System.currentTimeMillis() - this.lastSentTimestamp;
                if (delta < OnlineQueue.REQUEST_RATE) {
                    Thread.sleep(OnlineQueue.REQUEST_RATE - delta);
                }
                if (!OnlineQueue.this.isOnline() || interrupted()) {
                    OnlineQueue.this.dropped(url);
                    KontagentLog.d(OnlineQueue.TAG, "Offline! Url dropped (was re-queued to offline queue)");
                } else {
                    NetworkUtil.httpGet(url);
                    this.lastSentTimestamp = System.currentTimeMillis();
                    KontagentLog.d(OnlineQueue.TAG, "Sent HTTP request");
                }
            } catch (NetworkConnectivityError e) {
                KontagentLog.d(OnlineQueue.TAG, "Failed to send HTTP request.");
                OnlineQueue.this.failed(url, e);
            } catch (InterruptedException e2) {
                KontagentLog.d(OnlineQueue.TAG, "Failed to send HTTP request.");
                OnlineQueue.this.failed(url, e2);
            }
        }
    }
}
