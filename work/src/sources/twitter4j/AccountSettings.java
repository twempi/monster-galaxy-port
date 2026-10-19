package twitter4j;

import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public interface AccountSettings extends TwitterResponse, Serializable {
    String getSleepEndTime();

    String getSleepStartTime();

    Location[] getTrendLocations();

    boolean isGeoEnabled();

    boolean isSleepTimeEnabled();
}
