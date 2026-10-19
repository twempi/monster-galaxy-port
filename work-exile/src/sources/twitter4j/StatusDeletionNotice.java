package twitter4j;

import java.io.Serializable;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface StatusDeletionNotice extends Comparable<StatusDeletionNotice>, Serializable {
    long getStatusId();

    long getUserId();
}
