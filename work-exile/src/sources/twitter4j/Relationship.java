package twitter4j;

import java.io.Serializable;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface Relationship extends TwitterResponse, Serializable {
    long getSourceUserId();

    String getSourceUserScreenName();

    long getTargetUserId();

    String getTargetUserScreenName();

    boolean isSourceBlockingTarget();

    boolean isSourceFollowedByTarget();

    boolean isSourceFollowingTarget();

    boolean isSourceNotificationsEnabled();

    boolean isTargetFollowedBySource();

    boolean isTargetFollowingSource();
}
