package twitter4j.internal.async;

/* JADX INFO: loaded from: classes.dex */
public interface Dispatcher {
    void invokeLater(Runnable runnable);

    void shutdown();
}
