package twitter4j;

import java.io.Serializable;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface Place extends Comparable<Place>, Serializable {
    GeoLocation[][] getBoundingBoxCoordinates();

    String getBoundingBoxType();

    Place[] getContainedWithIn();

    String getCountry();

    String getCountryCode();

    String getFullName();

    GeoLocation[][] getGeometryCoordinates();

    String getGeometryType();

    String getId();

    String getName();

    String getPlaceType();

    String getStreetAddress();

    String getURL();
}
