package twitter4j;

import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public interface RelatedResults extends TwitterResponse, Serializable {
    ResponseList<Status> getTweetsFromUser();

    ResponseList<Status> getTweetsWithConversation();

    ResponseList<Status> getTweetsWithReply();
}
