package twitter4j;

import java.io.Serializable;
import java.util.Date;

/* JADX INFO: loaded from: classes.dex */
public interface Trends extends Comparable<Trends>, Serializable {
    Date getAsOf();

    Location getLocation();

    Date getTrendAt();

    Trend[] getTrends();
}
