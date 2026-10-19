package twitter4j.internal.async;

import twitter4j.internal.logging.Logger;

/* JADX INFO: compiled from: DispatcherImpl.java */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class ExecuteThread extends Thread {
    static Class class$twitter4j$internal$async$ExecuteThread;
    private static Logger logger;
    private boolean alive;
    DispatcherImpl q;

    static {
        Class clsClass$;
        if (class$twitter4j$internal$async$ExecuteThread == null) {
            clsClass$ = class$("twitter4j.internal.async.ExecuteThread");
            class$twitter4j$internal$async$ExecuteThread = clsClass$;
        } else {
            clsClass$ = class$twitter4j$internal$async$ExecuteThread;
        }
        logger = Logger.getLogger(clsClass$);
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    ExecuteThread(String name, DispatcherImpl q, int index) {
        super(new StringBuffer().append(name).append("[").append(index).append("]").toString());
        this.alive = true;
        this.q = q;
    }

    public void shutdown() {
        this.alive = false;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.alive) {
            Runnable task = this.q.poll();
            if (task != null) {
                try {
                    task.run();
                } catch (Exception ex) {
                    logger.error("Got an exception while running a taks:", ex);
                }
            }
        }
    }
}
