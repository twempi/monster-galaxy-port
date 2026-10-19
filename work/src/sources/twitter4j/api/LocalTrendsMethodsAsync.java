package twitter4j.api;

import twitter4j.GeoLocation;

/* JADX INFO: loaded from: classes.dex */
public interface LocalTrendsMethodsAsync {
    void getAvailableTrends();

    void getAvailableTrends(GeoLocation geoLocation);

    void getLocationTrends(int i);
}
