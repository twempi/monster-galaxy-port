package twitter4j.api;

import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface GeoMethodsAsync {
    void createPlace(String str, String str2, String str3, GeoLocation geoLocation, String str4);

    void getGeoDetails(String str);

    void getSimilarPlaces(GeoLocation geoLocation, String str, String str2, String str3);

    void reverseGeoCode(GeoQuery geoQuery);

    void searchPlaces(GeoQuery geoQuery);
}
