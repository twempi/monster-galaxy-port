package twitter4j.api;

import twitter4j.Paging;

/* JADX INFO: loaded from: classes.dex */
public interface DirectMessageMethodsAsync {
    void destroyDirectMessage(long j);

    void getDirectMessages();

    void getDirectMessages(Paging paging);

    void getSentDirectMessages();

    void getSentDirectMessages(Paging paging);

    void sendDirectMessage(long j, String str);

    void sendDirectMessage(String str, String str2);

    void showDirectMessage(long j);
}
