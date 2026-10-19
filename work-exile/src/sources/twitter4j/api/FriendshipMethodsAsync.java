package twitter4j.api;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface FriendshipMethodsAsync {
    void createFriendship(long j);

    void createFriendship(long j, boolean z);

    void createFriendship(String str);

    void createFriendship(String str, boolean z);

    void destroyFriendship(long j);

    void destroyFriendship(String str);

    void existsFriendship(String str, String str2);

    void getIncomingFriendships(long j);

    void getNoRetweetIds();

    void getOutgoingFriendships(long j);

    void lookupFriendships(long[] jArr);

    void lookupFriendships(String[] strArr);

    void showFriendship(long j, long j2);

    void showFriendship(String str, String str2);

    void updateFriendship(long j, boolean z, boolean z2);

    void updateFriendship(String str, boolean z, boolean z2);
}
