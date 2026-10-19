package twitter4j;

import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public interface AccountTotals extends TwitterResponse, Serializable {
    int getFavorites();

    int getFollowers();

    int getFriends();

    int getUpdates();
}
