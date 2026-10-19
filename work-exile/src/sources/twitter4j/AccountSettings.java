package twitter4j;

import java.io.Serializable;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface AccountSettings extends TwitterResponse, Serializable {
    String getSleepEndTime();

    String getSleepStartTime();

    Location[] getTrendLocations();

    boolean isGeoEnabled();

    boolean isSleepTimeEnabled();
}
