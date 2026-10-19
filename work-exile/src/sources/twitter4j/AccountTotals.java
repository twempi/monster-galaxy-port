package twitter4j;

import java.io.Serializable;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface AccountTotals extends TwitterResponse, Serializable {
    int getFavorites();

    int getFollowers();

    int getFriends();

    int getUpdates();
}
