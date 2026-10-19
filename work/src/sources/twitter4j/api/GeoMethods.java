package twitter4j.api;

import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.Place;
import twitter4j.ResponseList;
import twitter4j.SimilarPlaces;
import twitter4j.TwitterException;

/* JADX INFO: loaded from: classes.dex */
public interface GeoMethods {
    Place createPlace(String str, String str2, String str3, GeoLocation geoLocation, String str4) throws TwitterException;

    Place getGeoDetails(String str) throws TwitterException;

    SimilarPlaces getSimilarPlaces(GeoLocation geoLocation, String str, String str2, String str3) throws TwitterException;

    ResponseList<Place> reverseGeoCode(GeoQuery geoQuery) throws TwitterException;

    ResponseList<Place> searchPlaces(GeoQuery geoQuery) throws TwitterException;
}
