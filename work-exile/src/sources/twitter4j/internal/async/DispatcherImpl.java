package twitter4j.internal.async;

import java.util.LinkedList;
import java.util.List;
import twitter4j.conf.Configuration;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class DispatcherImpl implements Dispatcher {
    private ExecuteThread[] threads;
    private final List<Runnable> q = new LinkedList();
    final Object ticket = new Object();
    private boolean active = true;

    public DispatcherImpl(Configuration conf) {
        this.threads = new ExecuteThread[conf.getAsyncNumThreads()];
        for (int i = 0; i < this.threads.length; i++) {
            this.threads[i] = new ExecuteThread("Twitter4J Async Dispatcher", this, i);
            this.threads[i].setDaemon(true);
            this.threads[i].start();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(this) { // from class: twitter4j.internal.async.DispatcherImpl.1
            private final DispatcherImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                if (this.this$0.active) {
                    this.this$0.shutdown();
                }
            }
        });
    }

    @Override // twitter4j.internal.async.Dispatcher
    public synchronized void invokeLater(Runnable task) {
        synchronized (this.q) {
            this.q.add(task);
        }
        synchronized (this.ticket) {
            this.ticket.notify();
        }
    }

    public Runnable poll() {
        Runnable task;
        while (this.active) {
            synchronized (this.q) {
                if (this.q.size() > 0 && (task = this.q.remove(0)) != null) {
                    return task;
                }
            }
            synchronized (this.ticket) {
                try {
                    this.ticket.wait();
                } catch (InterruptedException e) {
                }
            }
        }
        return null;
    }

    @Override // twitter4j.internal.async.Dispatcher
    public synchronized void shutdown() {
        if (this.active) {
            this.active = false;
            ExecuteThread[] arr$ = this.threads;
            for (ExecuteThread thread : arr$) {
                thread.shutdown();
            }
            synchronized (this.ticket) {
                this.ticket.notify();
            }
        }
    }
}
