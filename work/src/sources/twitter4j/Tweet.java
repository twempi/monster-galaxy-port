package twitter4j;

import java.io.Serializable;
import java.util.Date;

/* JADX INFO: loaded from: classes.dex */
public interface Tweet extends Comparable<Tweet>, Serializable {
    Annotations getAnnotations();

    Date getCreatedAt();

    String getFromUser();

    long getFromUserId();

    GeoLocation getGeoLocation();

    long getId();

    String getIsoLanguageCode();

    String getLocation();

    Place getPlace();

    String getProfileImageUrl();

    String getSource();

    String getText();

    String getToUser();

    long getToUserId();
}
