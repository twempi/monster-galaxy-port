package twitter4j;

/* JADX INFO: loaded from: classes.dex */
public interface ConnectionLifeCycleListener {
    void onCleanUp();

    void onConnect();

    void onDisconnect();
}
