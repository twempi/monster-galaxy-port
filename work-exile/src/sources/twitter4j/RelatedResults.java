package twitter4j;

import java.io.Serializable;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface RelatedResults extends TwitterResponse, Serializable {
    ResponseList<Status> getTweetsFromUser();

    ResponseList<Status> getTweetsWithConversation();

    ResponseList<Status> getTweetsWithReply();
}
