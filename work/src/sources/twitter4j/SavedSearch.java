package twitter4j;

import java.io.Serializable;
import java.util.Date;

/* JADX INFO: loaded from: classes.dex */
public interface SavedSearch extends Comparable<SavedSearch>, TwitterResponse, Serializable {
    Date getCreatedAt();

    int getId();

    String getName();

    int getPosition();

    String getQuery();
}
