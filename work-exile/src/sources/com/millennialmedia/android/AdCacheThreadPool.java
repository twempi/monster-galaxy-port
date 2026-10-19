package com.millennialmedia.android;

import android.content.Context;
import java.lang.ref.WeakReference;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class AdCacheThreadPool {
    private static AdCacheThreadPool sharedThreadPool;
    private ThreadPoolExecutor executor;
    private PriorityBlockingQueue queue;

    private AdCacheThreadPool() {
        TimeUnit timeUnit = TimeUnit.SECONDS;
        PriorityBlockingQueue priorityBlockingQueue = new PriorityBlockingQueue(32);
        this.queue = priorityBlockingQueue;
        this.executor = new ThreadPoolExecutor(1, 2, 30L, timeUnit, priorityBlockingQueue);
    }

    static synchronized AdCacheThreadPool sharedThreadPool() {
        if (sharedThreadPool == null) {
            sharedThreadPool = new AdCacheThreadPool();
        }
        return sharedThreadPool;
    }

    synchronized void startDownloadTask(Context context, String adName, CachedAd ad, AdCache.AdCacheTaskListener listener) {
        if (context != null && ad != null) {
            AdCacheTask task = new AdCacheTask(context, adName, ad, listener);
            if (!this.queue.contains(task) && !ad.isOnDisk(context)) {
                this.executor.execute(task);
            }
        }
    }

    private class AdCacheTask implements Runnable, Comparable<AdCacheTask> {
        private CachedAd ad;
        private String adName;
        private WeakReference<Context> contextRef;
        private WeakReference<AdCache.AdCacheTaskListener> listenerRef;

        AdCacheTask(Context context, String adName, CachedAd ad, AdCache.AdCacheTaskListener listener) {
            this.contextRef = new WeakReference<>(context.getApplicationContext());
            this.adName = adName;
            this.ad = ad;
            if (listener != null) {
                this.listenerRef = new WeakReference<>(listener);
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            AdCache.AdCacheTaskListener listener = null;
            HandShake.sharedHandShake(this.contextRef.get()).lockAdTypeDownload(this.adName);
            boolean success = this.ad.download(this.contextRef.get());
            HandShake.sharedHandShake(this.contextRef.get()).unlockAdTypeDownload(this.adName);
            if (!success) {
                String incompleteId = AdCache.getIncompleteDownload(this.contextRef.get(), this.adName);
                if (incompleteId != null && this.ad.id.equals(incompleteId)) {
                    this.ad.delete(this.contextRef.get());
                    AdCache.setIncompleteDownload(this.contextRef.get(), this.adName, null);
                } else {
                    AdCache.setIncompleteDownload(this.contextRef.get(), this.adName, this.ad.downloadAllOrNothing ? null : this.ad.id);
                }
            } else {
                AdCache.setIncompleteDownload(this.contextRef.get(), this.adName, null);
            }
            if (this.listenerRef != null) {
                AdCache.AdCacheTaskListener listener2 = this.listenerRef.get();
                listener = listener2;
            }
            if (listener != null) {
                listener.downloadCompleted(this.ad, success);
            }
        }

        public boolean equals(Object task) {
            if (this == task) {
                return true;
            }
            return this.ad.equals(((AdCacheTask) task).ad);
        }

        @Override // java.lang.Comparable
        public int compareTo(AdCacheTask another) {
            return this.ad.downloadPriority - another.ad.downloadPriority;
        }
    }
}
