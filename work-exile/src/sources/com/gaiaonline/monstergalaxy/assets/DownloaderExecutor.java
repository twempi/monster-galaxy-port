package com.gaiaonline.monstergalaxy.assets;

import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class DownloaderExecutor extends ThreadPoolExecutor {
    private boolean isPaused;
    private ReentrantLock pauseLock;
    private Condition unpaused;

    public DownloaderExecutor() {
        super(1, 1, 0L, TimeUnit.MILLISECONDS, new PriorityBlockingQueue(30, new PriorityTaskComparator(null)), new ThreadFactory() { // from class: com.gaiaonline.monstergalaxy.assets.DownloaderExecutor.1
            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "AssetsDownloader-Thread");
                thread.setDaemon(true);
                return thread;
            }
        });
        this.pauseLock = new ReentrantLock();
        this.unpaused = this.pauseLock.newCondition();
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public <T> Future<T> submit(Callable<T> task) {
        if (task == null) {
            throw new NullPointerException();
        }
        FutureTask<T> ftask = new PriorityTask<>(task);
        execute(ftask);
        return ftask;
    }

    @Override // java.util.concurrent.ThreadPoolExecutor
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        this.pauseLock.lock();
        while (this.isPaused) {
            try {
                try {
                    this.unpaused.await();
                } catch (InterruptedException e) {
                    t.interrupt();
                    this.pauseLock.unlock();
                    return;
                }
            } catch (Throwable th) {
                this.pauseLock.unlock();
                throw th;
            }
        }
        this.pauseLock.unlock();
    }

    public void pause() {
        this.pauseLock.lock();
        try {
            this.isPaused = true;
        } finally {
            this.pauseLock.unlock();
        }
    }

    public void resume() {
        this.pauseLock.lock();
        try {
            this.isPaused = false;
            this.unpaused.signalAll();
        } finally {
            this.pauseLock.unlock();
        }
    }

    private static final class PriorityTask<T> extends FutureTask<T> implements Comparable<PriorityTask<T>> {
        private final int priority;

        public PriorityTask(Callable<T> tCallable) {
            super(tCallable);
            this.priority = ((DownloadTask) tCallable).getPriority();
        }

        @Override // java.lang.Comparable
        public int compareTo(PriorityTask<T> o) {
            long diff = o.priority - this.priority;
            if (0 == diff) {
                return 0;
            }
            return 0 > diff ? -1 : 1;
        }
    }

    private static class PriorityTaskComparator<T> implements Comparator<Runnable> {
        private PriorityTaskComparator() {
        }

        /* synthetic */ PriorityTaskComparator(PriorityTaskComparator priorityTaskComparator) {
            this();
        }

        @Override // java.util.Comparator
        public int compare(Runnable left, Runnable right) {
            return ((PriorityTask) left).compareTo((PriorityTask) right);
        }
    }
}
