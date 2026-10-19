package twitter4j.api;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface BlockMethodsAsync {
    void createBlock(long j);

    void createBlock(String str);

    void destroyBlock(long j);

    void destroyBlock(String str);

    void existsBlock(long j);

    void existsBlock(String str);

    void getBlockingUsers();

    void getBlockingUsers(int i);

    void getBlockingUsersIDs();
}
