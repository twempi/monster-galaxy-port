package twitter4j.api;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface ListMembersMethodsAsync {
    void addUserListMember(int i, long j);

    void addUserListMembers(int i, long[] jArr);

    void addUserListMembers(int i, String[] strArr);

    void checkUserListMembership(String str, int i, long j);

    void deleteUserListMember(int i, long j);

    void getUserListMembers(int i, long j);

    void getUserListMembers(long j, int i, long j2);

    void getUserListMembers(String str, int i, long j);

    void showUserListMembership(int i, long j);
}
