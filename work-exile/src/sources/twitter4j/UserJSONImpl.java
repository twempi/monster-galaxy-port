package twitter4j;

import com.sessionm.a.b;
import com.sessionm.ui.SessionMActivity;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.json.DataObjectFactoryUtil;
import twitter4j.internal.util.ParseUtil;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
final class UserJSONImpl extends TwitterResponseImpl implements User, Serializable {
    private static final long serialVersionUID = -6345893237975349030L;
    private Date createdAt;
    private String description;
    private int favouritesCount;
    private int followersCount;
    private int friendsCount;
    private long id;
    private boolean isContributorsEnabled;
    private boolean isFollowRequestSent;
    private boolean isGeoEnabled;
    private boolean isProtected;
    private boolean isVerified;
    private String lang;
    private int listedCount;
    private String location;
    private String name;
    private String profileBackgroundColor;
    private String profileBackgroundImageUrl;
    private String profileBackgroundImageUrlHttps;
    private boolean profileBackgroundTiled;
    private String profileImageUrl;
    private String profileImageUrlHttps;
    private String profileLinkColor;
    private String profileSidebarBorderColor;
    private String profileSidebarFillColor;
    private String profileTextColor;
    private boolean profileUseBackgroundImage;
    private String screenName;
    private boolean showAllInlineMedia;
    private Status status;
    private int statusesCount;
    private String timeZone;
    private boolean translator;
    private String url;
    private int utcOffset;

    @Override // java.lang.Comparable
    public int compareTo(User user) {
        return compareTo2(user);
    }

    UserJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
        }
        JSONObject json = res.asJSONObject();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.registerJSONObject(this, json);
        }
    }

    UserJSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        try {
            this.id = ParseUtil.getLong("id", json);
            this.name = ParseUtil.getRawString("name", json);
            this.screenName = ParseUtil.getRawString("screen_name", json);
            this.location = ParseUtil.getRawString("location", json);
            this.description = ParseUtil.getRawString("description", json);
            this.isContributorsEnabled = ParseUtil.getBoolean("contributors_enabled", json);
            this.profileImageUrl = ParseUtil.getRawString("profile_image_url", json);
            this.profileImageUrlHttps = ParseUtil.getRawString("profile_image_url_https", json);
            this.url = ParseUtil.getRawString(SessionMActivity.INTENT_PARAM_URL, json);
            this.isProtected = ParseUtil.getBoolean("protected", json);
            this.isGeoEnabled = ParseUtil.getBoolean("geo_enabled", json);
            this.isVerified = ParseUtil.getBoolean("verified", json);
            this.translator = ParseUtil.getBoolean("is_translator", json);
            this.followersCount = ParseUtil.getInt("followers_count", json);
            this.profileBackgroundColor = ParseUtil.getRawString("profile_background_color", json);
            this.profileTextColor = ParseUtil.getRawString("profile_text_color", json);
            this.profileLinkColor = ParseUtil.getRawString("profile_link_color", json);
            this.profileSidebarFillColor = ParseUtil.getRawString("profile_sidebar_fill_color", json);
            this.profileSidebarBorderColor = ParseUtil.getRawString("profile_sidebar_border_color", json);
            this.profileUseBackgroundImage = ParseUtil.getBoolean("profile_use_background_image", json);
            this.showAllInlineMedia = ParseUtil.getBoolean("show_all_inline_media", json);
            this.friendsCount = ParseUtil.getInt("friends_count", json);
            this.createdAt = ParseUtil.getDate("created_at", json, "EEE MMM dd HH:mm:ss z yyyy");
            this.favouritesCount = ParseUtil.getInt("favourites_count", json);
            this.utcOffset = ParseUtil.getInt("utc_offset", json);
            this.timeZone = ParseUtil.getRawString("time_zone", json);
            this.profileBackgroundImageUrl = ParseUtil.getRawString("profile_background_image_url", json);
            this.profileBackgroundImageUrlHttps = ParseUtil.getRawString("profile_background_image_url_https", json);
            this.profileBackgroundTiled = ParseUtil.getBoolean("profile_background_tile", json);
            this.lang = ParseUtil.getRawString("lang", json);
            this.statusesCount = ParseUtil.getInt("statuses_count", json);
            this.listedCount = ParseUtil.getInt("listed_count", json);
            this.isFollowRequestSent = ParseUtil.getBoolean("follow_request_sent", json);
            if (!json.isNull(b.K)) {
                JSONObject statusJSON = json.getJSONObject(b.K);
                this.status = new StatusJSONImpl(statusJSON);
            }
        } catch (JSONException jsone) {
            throw new TwitterException(new StringBuffer().append(jsone.getMessage()).append(":").append(json.toString()).toString(), jsone);
        }
    }

    /* JADX INFO: renamed from: compareTo, reason: avoid collision after fix types in other method */
    public int compareTo2(User that) {
        return (int) (this.id - that.getId());
    }

    @Override // twitter4j.User
    public long getId() {
        return this.id;
    }

    @Override // twitter4j.User
    public String getName() {
        return this.name;
    }

    @Override // twitter4j.User
    public String getScreenName() {
        return this.screenName;
    }

    @Override // twitter4j.User
    public String getLocation() {
        return this.location;
    }

    @Override // twitter4j.User
    public String getDescription() {
        return this.description;
    }

    @Override // twitter4j.User
    public boolean isContributorsEnabled() {
        return this.isContributorsEnabled;
    }

    @Override // twitter4j.User
    public URL getProfileImageURL() {
        try {
            return new URL(this.profileImageUrl);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Override // twitter4j.User
    public URL getProfileImageUrlHttps() {
        if (this.profileImageUrlHttps == null) {
            return null;
        }
        try {
            return new URL(this.profileImageUrlHttps);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Override // twitter4j.User
    public URL getURL() {
        try {
            return new URL(this.url);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Override // twitter4j.User
    public boolean isProtected() {
        return this.isProtected;
    }

    @Override // twitter4j.User
    public int getFollowersCount() {
        return this.followersCount;
    }

    @Override // twitter4j.User
    public String getProfileBackgroundColor() {
        return this.profileBackgroundColor;
    }

    @Override // twitter4j.User
    public String getProfileTextColor() {
        return this.profileTextColor;
    }

    @Override // twitter4j.User
    public String getProfileLinkColor() {
        return this.profileLinkColor;
    }

    @Override // twitter4j.User
    public String getProfileSidebarFillColor() {
        return this.profileSidebarFillColor;
    }

    @Override // twitter4j.User
    public String getProfileSidebarBorderColor() {
        return this.profileSidebarBorderColor;
    }

    @Override // twitter4j.User
    public boolean isProfileUseBackgroundImage() {
        return this.profileUseBackgroundImage;
    }

    @Override // twitter4j.User
    public boolean isShowAllInlineMedia() {
        return this.showAllInlineMedia;
    }

    @Override // twitter4j.User
    public int getFriendsCount() {
        return this.friendsCount;
    }

    @Override // twitter4j.User
    public Status getStatus() {
        return this.status;
    }

    @Override // twitter4j.User
    public Date getCreatedAt() {
        return this.createdAt;
    }

    @Override // twitter4j.User
    public int getFavouritesCount() {
        return this.favouritesCount;
    }

    @Override // twitter4j.User
    public int getUtcOffset() {
        return this.utcOffset;
    }

    @Override // twitter4j.User
    public String getTimeZone() {
        return this.timeZone;
    }

    @Override // twitter4j.User
    public String getProfileBackgroundImageUrl() {
        return this.profileBackgroundImageUrl;
    }

    @Override // twitter4j.User
    public String getProfileBackgroundImageUrlHttps() {
        return this.profileBackgroundImageUrlHttps;
    }

    @Override // twitter4j.User
    public boolean isProfileBackgroundTiled() {
        return this.profileBackgroundTiled;
    }

    @Override // twitter4j.User
    public String getLang() {
        return this.lang;
    }

    @Override // twitter4j.User
    public int getStatusesCount() {
        return this.statusesCount;
    }

    @Override // twitter4j.User
    public boolean isGeoEnabled() {
        return this.isGeoEnabled;
    }

    @Override // twitter4j.User
    public boolean isVerified() {
        return this.isVerified;
    }

    @Override // twitter4j.User
    public boolean isTranslator() {
        return this.translator;
    }

    @Override // twitter4j.User
    public int getListedCount() {
        return this.listedCount;
    }

    @Override // twitter4j.User
    public boolean isFollowRequestSent() {
        return this.isFollowRequestSent;
    }

    static PagableResponseList<User> createPagableUserList(HttpResponse res, Configuration conf) throws TwitterException {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            JSONObject json = res.asJSONObject();
            JSONArray list = json.getJSONArray("users");
            int size = list.length();
            PagableResponseList<User> users = new PagableResponseListImpl<>(size, json, res);
            for (int i = 0; i < size; i++) {
                JSONObject userJson = list.getJSONObject(i);
                User user = new UserJSONImpl(userJson);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(user, userJson);
                }
                users.add(user);
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(users, json);
            }
            return users;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }

    static ResponseList<User> createUserList(HttpResponse res, Configuration conf) throws TwitterException {
        return createUserList(res.asJSONArray(), res, conf);
    }

    static ResponseList<User> createUserList(JSONArray list, HttpResponse res, Configuration conf) throws TwitterException {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            int size = list.length();
            ResponseList<User> users = new ResponseListImpl<>(size, res);
            for (int i = 0; i < size; i++) {
                JSONObject json = list.getJSONObject(i);
                UserJSONImpl userJSONImpl = new UserJSONImpl(json);
                users.add(userJSONImpl);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(userJSONImpl, json);
                }
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(users, list);
            }
            return users;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }

    public int hashCode() {
        return (int) this.id;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this != obj) {
            return (obj instanceof User) && ((User) obj).getId() == this.id;
        }
        return true;
    }

    public String toString() {
        return new StringBuffer().append("UserJSONImpl{id=").append(this.id).append(", name='").append(this.name).append('\'').append(", screenName='").append(this.screenName).append('\'').append(", location='").append(this.location).append('\'').append(", description='").append(this.description).append('\'').append(", isContributorsEnabled=").append(this.isContributorsEnabled).append(", profileImageUrl='").append(this.profileImageUrl).append('\'').append(", profileImageUrlHttps='").append(this.profileImageUrlHttps).append('\'').append(", url='").append(this.url).append('\'').append(", isProtected=").append(this.isProtected).append(", followersCount=").append(this.followersCount).append(", status=").append(this.status).append(", profileBackgroundColor='").append(this.profileBackgroundColor).append('\'').append(", profileTextColor='").append(this.profileTextColor).append('\'').append(", profileLinkColor='").append(this.profileLinkColor).append('\'').append(", profileSidebarFillColor='").append(this.profileSidebarFillColor).append('\'').append(", profileSidebarBorderColor='").append(this.profileSidebarBorderColor).append('\'').append(", profileUseBackgroundImage=").append(this.profileUseBackgroundImage).append(", showAllInlineMedia=").append(this.showAllInlineMedia).append(", friendsCount=").append(this.friendsCount).append(", createdAt=").append(this.createdAt).append(", favouritesCount=").append(this.favouritesCount).append(", utcOffset=").append(this.utcOffset).append(", timeZone='").append(this.timeZone).append('\'').append(", profileBackgroundImageUrl='").append(this.profileBackgroundImageUrl).append('\'').append(", profileBackgroundImageUrlHttps='").append(this.profileBackgroundImageUrlHttps).append('\'').append(", profileBackgroundTiled=").append(this.profileBackgroundTiled).append(", lang='").append(this.lang).append('\'').append(", statusesCount=").append(this.statusesCount).append(", isGeoEnabled=").append(this.isGeoEnabled).append(", isVerified=").append(this.isVerified).append(", translator=").append(this.translator).append(", listedCount=").append(this.listedCount).append(", isFollowRequestSent=").append(this.isFollowRequestSent).append('}').toString();
    }
}
