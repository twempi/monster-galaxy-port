package twitter4j;

import java.io.Serializable;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface Friendship extends Serializable {
    long getId();

    String getName();

    String getScreenName();

    boolean isFollowedBy();

    boolean isFollowing();
}
