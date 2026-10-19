package twitter4j;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface ConnectionLifeCycleListener {
    void onCleanUp();

    void onConnect();

    void onDisconnect();
}
