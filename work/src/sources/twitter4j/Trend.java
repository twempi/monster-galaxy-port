package twitter4j;

import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public interface Trend extends TwitterResponse, Serializable {
    String getName();

    String getQuery();

    String getUrl();
}
