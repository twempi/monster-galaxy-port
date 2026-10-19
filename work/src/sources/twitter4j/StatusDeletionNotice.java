package twitter4j;

import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public interface StatusDeletionNotice extends Comparable<StatusDeletionNotice>, Serializable {
    long getStatusId();

    long getUserId();
}
