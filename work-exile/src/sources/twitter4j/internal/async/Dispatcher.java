package twitter4j.internal.async;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface Dispatcher {
    void invokeLater(Runnable runnable);

    void shutdown();
}
