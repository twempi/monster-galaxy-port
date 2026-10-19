package twitter4j;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface User extends Comparable<User>, TwitterResponse, Serializable {
    Date getCreatedAt();

    String getDescription();

    int getFavouritesCount();

    int getFollowersCount();

    int getFriendsCount();

    long getId();

    String getLang();

    int getListedCount();

    String getLocation();

    String getName();

    String getProfileBackgroundColor();

    String getProfileBackgroundImageUrl();

    String getProfileBackgroundImageUrlHttps();

    URL getProfileImageURL();

    URL getProfileImageUrlHttps();

    String getProfileLinkColor();

    String getProfileSidebarBorderColor();

    String getProfileSidebarFillColor();

    String getProfileTextColor();

    String getScreenName();

    Status getStatus();

    int getStatusesCount();

    String getTimeZone();

    URL getURL();

    int getUtcOffset();

    boolean isContributorsEnabled();

    boolean isFollowRequestSent();

    boolean isGeoEnabled();

    boolean isProfileBackgroundTiled();

    boolean isProfileUseBackgroundImage();

    boolean isProtected();

    boolean isShowAllInlineMedia();

    boolean isTranslator();

    boolean isVerified();
}
