package twitter4j;

import java.io.Serializable;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface Trend extends TwitterResponse, Serializable {
    String getName();

    String getQuery();

    String getUrl();
}
