package twitter4j.api;

import twitter4j.GeoLocation;
import twitter4j.Location;
import twitter4j.ResponseList;
import twitter4j.Trends;
import twitter4j.TwitterException;

/* JADX INFO: loaded from: classes.dex */
public interface LocalTrendsMethods {
    ResponseList<Location> getAvailableTrends() throws TwitterException;

    ResponseList<Location> getAvailableTrends(GeoLocation geoLocation) throws TwitterException;

    Trends getLocationTrends(int i) throws TwitterException;
}
