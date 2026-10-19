package twitter4j;

import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public interface UserMentionEntity extends Serializable {
    int getEnd();

    long getId();

    String getName();

    String getScreenName();

    int getStart();
}
