package twitter4j.api;

import twitter4j.GeoLocation;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface LocalTrendsMethodsAsync {
    void getAvailableTrends();

    void getAvailableTrends(GeoLocation geoLocation);

    void getLocationTrends(int i);
}
