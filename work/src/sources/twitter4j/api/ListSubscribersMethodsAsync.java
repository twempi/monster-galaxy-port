package twitter4j.api;

/* JADX INFO: loaded from: classes.dex */
public interface ListSubscribersMethodsAsync {
    void checkUserListSubscription(String str, int i, long j);

    void createUserListSubscription(int i);

    void destroyUserListSubscription(int i);

    void getUserListSubscribers(int i, long j);

    void getUserListSubscribers(String str, int i, long j);

    void showUserListSubscription(int i, long j);

    void subscribeUserList(String str, int i);

    void unsubscribeUserList(String str, int i);
}
