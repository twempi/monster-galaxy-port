package twitter4j;

import java.io.Serializable;
import java.util.Date;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface Status extends Comparable<Status>, TwitterResponse, Serializable {
    Annotations getAnnotations();

    long[] getContributors();

    Date getCreatedAt();

    GeoLocation getGeoLocation();

    HashtagEntity[] getHashtagEntities();

    long getId();

    String getInReplyToScreenName();

    long getInReplyToStatusId();

    long getInReplyToUserId();

    MediaEntity[] getMediaEntities();

    Place getPlace();

    long getRetweetCount();

    Status getRetweetedStatus();

    String getSource();

    String getText();

    URLEntity[] getURLEntities();

    User getUser();

    UserMentionEntity[] getUserMentionEntities();

    boolean isFavorited();

    boolean isRetweet();

    boolean isRetweetedByMe();

    boolean isTruncated();
}
