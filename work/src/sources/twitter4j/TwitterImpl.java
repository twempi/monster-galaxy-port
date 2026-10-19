package twitter4j;

import com.sessionm.a.b;
import com.sessionm.ui.SessionMActivity;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONException;
import twitter4j.api.HelpMethods;
import twitter4j.auth.Authorization;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpParameter;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.util.T4JInternalStringUtil;

/* JADX INFO: loaded from: classes.dex */
class TwitterImpl extends TwitterBaseImpl implements Twitter {
    private static final long serialVersionUID = -1486360080128882436L;
    private final HttpParameter INCLUDE_ENTITIES;
    private final HttpParameter INCLUDE_RTS;

    TwitterImpl(Configuration conf, Authorization auth) {
        super(conf, auth);
        this.INCLUDE_ENTITIES = new HttpParameter("include_entities", conf.isIncludeEntitiesEnabled());
        this.INCLUDE_RTS = new HttpParameter("include_rts", conf.isIncludeRTsEnabled());
    }

    private HttpParameter[] mergeParameters(HttpParameter[] params1, HttpParameter[] params2) {
        if (params1 != null && params2 != null) {
            HttpParameter[] params = new HttpParameter[params1.length + params2.length];
            System.arraycopy(params1, 0, params, 0, params1.length);
            System.arraycopy(params2, 0, params, params1.length, params2.length);
            return params;
        }
        if (params1 == null && params2 == null) {
            return new HttpParameter[0];
        }
        return params1 != null ? params1 : params2;
    }

    private HttpParameter[] mergeParameters(HttpParameter[] params1, HttpParameter params2) {
        if (params1 != null && params2 != null) {
            HttpParameter[] params = new HttpParameter[params1.length + 1];
            System.arraycopy(params1, 0, params, 0, params1.length);
            params[params.length - 1] = params2;
            return params;
        }
        if (params1 == null && params2 == null) {
            return new HttpParameter[0];
        }
        return params1 != null ? params1 : new HttpParameter[]{params2};
    }

    @Override // twitter4j.api.SearchMethods
    public QueryResult search(Query query) throws TwitterException {
        try {
            return new QueryResultJSONImpl(get(new StringBuffer().append(this.conf.getSearchBaseURL()).append("search.json").toString(), query.asHttpParameterArray()), this.conf);
        } catch (TwitterException te) {
            if (404 == te.getStatusCode()) {
                return new QueryResultJSONImpl(query);
            }
            throw te;
        }
    }

    @Override // twitter4j.api.TrendsMethods
    public Trends getTrends() throws TwitterException {
        return new TrendsJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("trends.json").toString()), this.conf);
    }

    @Override // twitter4j.api.TrendsMethods
    public Trends getCurrentTrends() throws TwitterException {
        return TrendsJSONImpl.createTrendsList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("trends/current.json").toString()), this.conf.isJSONStoreEnabled()).get(0);
    }

    @Override // twitter4j.api.TrendsMethods
    public Trends getCurrentTrends(boolean excludeHashTags) throws TwitterException {
        return TrendsJSONImpl.createTrendsList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("trends/current.json").append(excludeHashTags ? "?exclude=hashtags" : "").toString()), this.conf.isJSONStoreEnabled()).get(0);
    }

    @Override // twitter4j.api.TrendsMethods
    public List<Trends> getDailyTrends() throws TwitterException {
        return TrendsJSONImpl.createTrendsList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("trends/daily.json").toString()), this.conf.isJSONStoreEnabled());
    }

    @Override // twitter4j.api.TrendsMethods
    public List<Trends> getDailyTrends(Date date, boolean excludeHashTags) throws TwitterException {
        return TrendsJSONImpl.createTrendsList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("trends/daily.json?date=").append(toDateStr(date)).append(excludeHashTags ? "&exclude=hashtags" : "").toString()), this.conf.isJSONStoreEnabled());
    }

    private String toDateStr(Date date) {
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    @Override // twitter4j.api.TrendsMethods
    public List<Trends> getWeeklyTrends() throws TwitterException {
        return TrendsJSONImpl.createTrendsList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("trends/weekly.json").toString()), this.conf.isJSONStoreEnabled());
    }

    @Override // twitter4j.api.TrendsMethods
    public List<Trends> getWeeklyTrends(Date date, boolean excludeHashTags) throws TwitterException {
        return TrendsJSONImpl.createTrendsList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("trends/weekly.json?date=").append(toDateStr(date)).append(excludeHashTags ? "&exclude=hashtags" : "").toString()), this.conf.isJSONStoreEnabled());
    }

    @Override // twitter4j.api.TimelineMethods
    public ResponseList<Status> getPublicTimeline() throws TwitterException {
        return StatusJSONImpl.createStatusList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/public_timeline.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&include_rts=").append(this.conf.isIncludeRTsEnabled()).toString()), this.conf);
    }

    @Override // twitter4j.api.TimelineMethods
    public ResponseList<Status> getHomeTimeline() throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/home_timeline.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).toString()), this.conf);
    }

    @Override // twitter4j.api.TimelineMethods
    public ResponseList<Status> getHomeTimeline(Paging paging) throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/home_timeline.json").toString(), mergeParameters(paging.asPostParameterArray(), this.INCLUDE_ENTITIES)), this.conf);
    }

    @Override // twitter4j.api.TimelineMethods
    public ResponseList<Status> getFriendsTimeline() throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/friends_timeline.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&include_rts=").append(this.conf.isIncludeRTsEnabled()).toString()), this.conf);
    }

    @Override // twitter4j.api.TimelineMethods
    public ResponseList<Status> getFriendsTimeline(Paging paging) throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/friends_timeline.json").toString(), mergeParameters(new HttpParameter[]{this.INCLUDE_RTS, this.INCLUDE_ENTITIES}, paging.asPostParameterArray())), this.conf);
    }

    @Override // twitter4j.api.TimelineMethods
    public ResponseList<Status> getUserTimeline(String screenName, Paging paging) throws TwitterException {
        return StatusJSONImpl.createStatusList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/user_timeline.json").toString(), mergeParameters(new HttpParameter[]{new HttpParameter("screen_name", screenName), this.INCLUDE_RTS, this.INCLUDE_ENTITIES}, paging.asPostParameterArray())), this.conf);
    }

    @Override // twitter4j.api.TimelineMethods
    public ResponseList<Status> getUserTimeline(long userId, Paging paging) throws TwitterException {
        return StatusJSONImpl.createStatusList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/user_timeline.json").toString(), mergeParameters(new HttpParameter[]{new HttpParameter("user_id", userId), this.INCLUDE_RTS, this.INCLUDE_ENTITIES}, paging.asPostParameterArray())), this.conf);
    }

    @Override // twitter4j.api.TimelineMethods
    public ResponseList<Status> getUserTimeline(String screenName) throws TwitterException {
        return getUserTimeline(screenName, new Paging());
    }

    @Override // twitter4j.api.TimelineMethods
    public ResponseList<Status> getUserTimeline(long userId) throws TwitterException {
        return getUserTimeline(userId, new Paging());
    }

    @Override // twitter4j.api.TimelineMethods
    public ResponseList<Status> getUserTimeline() throws TwitterException {
        return getUserTimeline(new Paging());
    }

    @Override // twitter4j.api.TimelineMethods
    public ResponseList<Status> getUserTimeline(Paging paging) throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/user_timeline.json").toString(), mergeParameters(new HttpParameter[]{this.INCLUDE_RTS, this.INCLUDE_ENTITIES}, paging.asPostParameterArray())), this.conf);
    }

    @Override // twitter4j.api.TimelineMethods
    public ResponseList<Status> getMentions() throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/mentions.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&include_rts=").append(this.conf.isIncludeRTsEnabled()).toString()), this.conf);
    }

    @Override // twitter4j.api.TimelineMethods
    public ResponseList<Status> getMentions(Paging paging) throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/mentions.json").toString(), mergeParameters(new HttpParameter[]{this.INCLUDE_RTS, this.INCLUDE_ENTITIES}, paging.asPostParameterArray())), this.conf);
    }

    @Override // twitter4j.api.TimelineMethods
    public ResponseList<Status> getRetweetedByMe() throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/retweeted_by_me.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).toString()), this.conf);
    }

    @Override // twitter4j.api.TimelineMethods
    public ResponseList<Status> getRetweetedByMe(Paging paging) throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/retweeted_by_me.json").toString(), mergeParameters(paging.asPostParameterArray(), this.INCLUDE_ENTITIES)), this.conf);
    }

    @Override // twitter4j.api.TimelineMethods
    public ResponseList<Status> getRetweetedToMe() throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/retweeted_to_me.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).toString()), this.conf);
    }

    @Override // twitter4j.api.TimelineMethods
    public ResponseList<Status> getRetweetedToMe(Paging paging) throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/retweeted_to_me.json").toString(), mergeParameters(paging.asPostParameterArray(), this.INCLUDE_ENTITIES)), this.conf);
    }

    @Override // twitter4j.api.TimelineMethods
    public ResponseList<Status> getRetweetsOfMe() throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/retweets_of_me.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).toString()), this.conf);
    }

    @Override // twitter4j.api.TimelineMethods
    public ResponseList<Status> getRetweetsOfMe(Paging paging) throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/retweets_of_me.json").toString(), mergeParameters(paging.asPostParameterArray(), this.INCLUDE_ENTITIES)), this.conf);
    }

    @Override // twitter4j.api.TimelineMethods
    public ResponseList<Status> getRetweetedToUser(String screenName, Paging paging) throws TwitterException {
        return StatusJSONImpl.createStatusList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/retweeted_to_user.json").toString(), mergeParameters(paging.asPostParameterArray(), new HttpParameter[]{new HttpParameter("screen_name", screenName), this.INCLUDE_ENTITIES})), this.conf);
    }

    @Override // twitter4j.api.TimelineMethods
    public ResponseList<Status> getRetweetedToUser(long userId, Paging paging) throws TwitterException {
        return StatusJSONImpl.createStatusList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/retweeted_to_user.json").toString(), mergeParameters(paging.asPostParameterArray(), new HttpParameter[]{new HttpParameter("user_id", userId), this.INCLUDE_ENTITIES})), this.conf);
    }

    @Override // twitter4j.api.TimelineMethods
    public ResponseList<Status> getRetweetedByUser(String screenName, Paging paging) throws TwitterException {
        return StatusJSONImpl.createStatusList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/retweeted_by_user.json").toString(), mergeParameters(paging.asPostParameterArray(), new HttpParameter[]{new HttpParameter("screen_name", screenName), this.INCLUDE_ENTITIES})), this.conf);
    }

    @Override // twitter4j.api.TimelineMethods
    public ResponseList<Status> getRetweetedByUser(long userId, Paging paging) throws TwitterException {
        return StatusJSONImpl.createStatusList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/retweeted_by_user.json").toString(), mergeParameters(paging.asPostParameterArray(), new HttpParameter[]{new HttpParameter("user_id", userId), this.INCLUDE_ENTITIES})), this.conf);
    }

    @Override // twitter4j.api.StatusMethods
    public ResponseList<User> getRetweetedBy(long statusId) throws TwitterException {
        return getRetweetedBy(statusId, new Paging(1, 100));
    }

    @Override // twitter4j.api.StatusMethods
    public ResponseList<User> getRetweetedBy(long statusId, Paging paging) throws TwitterException {
        return UserJSONImpl.createUserList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/").append(statusId).append("/retweeted_by.json").toString(), paging.asPostParameterArray()), this.conf);
    }

    @Override // twitter4j.api.StatusMethods
    public IDs getRetweetedByIDs(long statusId) throws TwitterException {
        return getRetweetedByIDs(statusId, new Paging(1, 100));
    }

    @Override // twitter4j.api.StatusMethods
    public IDs getRetweetedByIDs(long statusId, Paging paging) throws TwitterException {
        ensureAuthorizationEnabled();
        return new IDsJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/").append(statusId).append("/retweeted_by/ids.json").toString(), paging.asPostParameterArray()), this.conf);
    }

    @Override // twitter4j.api.StatusMethods
    public Status showStatus(long id) throws TwitterException {
        return new StatusJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/show/").append(id).append(".json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).toString()), this.conf);
    }

    @Override // twitter4j.api.StatusMethods
    public Status updateStatus(String status) throws TwitterException {
        ensureAuthorizationEnabled();
        return new StatusJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/update.json").toString(), new HttpParameter[]{new HttpParameter(b.K, status), this.INCLUDE_ENTITIES}), this.conf);
    }

    @Override // twitter4j.api.StatusMethods
    public Status updateStatus(StatusUpdate latestStatus) throws TwitterException {
        ensureAuthorizationEnabled();
        return new StatusJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/update.json").toString(), mergeParameters(latestStatus.asHttpParameterArray(), this.INCLUDE_ENTITIES)), this.conf);
    }

    @Override // twitter4j.api.StatusMethods
    public Status destroyStatus(long statusId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new StatusJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/destroy/").append(statusId).append(".json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).toString()), this.conf);
    }

    @Override // twitter4j.api.StatusMethods
    public Status retweetStatus(long statusId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new StatusJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/retweet/").append(statusId).append(".json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).toString()), this.conf);
    }

    @Override // twitter4j.api.StatusMethods
    public ResponseList<Status> getRetweets(long statusId) throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/retweets/").append(statusId).append(".json?count=100&include_entities=").append(this.conf.isIncludeEntitiesEnabled()).toString()), this.conf);
    }

    @Override // twitter4j.api.UserMethods
    public User showUser(String screenName) throws TwitterException {
        return new UserJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("users/show.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&screen_name=").append(screenName).toString()), this.conf);
    }

    @Override // twitter4j.api.UserMethods
    public User showUser(long userId) throws TwitterException {
        return new UserJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("users/show.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&user_id=").append(userId).toString()), this.conf);
    }

    @Override // twitter4j.api.UserMethods
    public ResponseList<User> lookupUsers(String[] screenNames) throws TwitterException {
        ensureAuthorizationEnabled();
        return UserJSONImpl.createUserList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("users/lookup.json").toString(), new HttpParameter[]{new HttpParameter("screen_name", T4JInternalStringUtil.join(screenNames)), this.INCLUDE_ENTITIES}), this.conf);
    }

    @Override // twitter4j.api.UserMethods
    public ResponseList<User> lookupUsers(long[] ids) throws TwitterException {
        ensureAuthorizationEnabled();
        return UserJSONImpl.createUserList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("users/lookup.json").toString(), new HttpParameter[]{new HttpParameter("user_id", T4JInternalStringUtil.join(ids)), this.INCLUDE_ENTITIES}), this.conf);
    }

    @Override // twitter4j.api.UserMethods
    public ResponseList<User> searchUsers(String query, int page) throws TwitterException {
        ensureAuthorizationEnabled();
        return UserJSONImpl.createUserList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("users/search.json").toString(), new HttpParameter[]{new HttpParameter("q", query), new HttpParameter("per_page", 20), new HttpParameter("page", page), this.INCLUDE_ENTITIES}), this.conf);
    }

    @Override // twitter4j.api.UserMethods
    public ResponseList<Category> getSuggestedUserCategories() throws TwitterException {
        return CategoryJSONImpl.createCategoriesList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("users/suggestions.json").toString()), this.conf);
    }

    @Override // twitter4j.api.UserMethods
    public ResponseList<User> getUserSuggestions(String categorySlug) throws TwitterException {
        HttpResponse res = get(new StringBuffer().append(this.conf.getRestBaseURL()).append("users/suggestions/").append(categorySlug).append(".json").toString());
        try {
            return UserJSONImpl.createUserList(res.asJSONObject().getJSONArray("users"), res, this.conf);
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    @Override // twitter4j.api.UserMethods
    public ResponseList<User> getMemberSuggestions(String categorySlug) throws TwitterException {
        HttpResponse res = get(new StringBuffer().append(this.conf.getRestBaseURL()).append("users/suggestions/").append(categorySlug).append("/members.json").toString());
        return UserJSONImpl.createUserList(res.asJSONArray(), res, this.conf);
    }

    @Override // twitter4j.api.UserMethods
    public ProfileImage getProfileImage(String screenName, ProfileImage.ImageSize size) throws TwitterException {
        return new ProfileImageImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("users/profile_image/").append(screenName).append(".json?size=").append(size.getName()).toString()));
    }

    @Override // twitter4j.api.UserMethods
    public PagableResponseList<User> getFriendsStatuses(long cursor) throws TwitterException {
        return UserJSONImpl.createPagableUserList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/friends.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&cursor=").append(cursor).toString()), this.conf);
    }

    @Override // twitter4j.api.UserMethods
    public PagableResponseList<User> getFriendsStatuses(String screenName, long cursor) throws TwitterException {
        return UserJSONImpl.createPagableUserList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/friends.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&screen_name=").append(screenName).append("&cursor=").append(cursor).toString()), this.conf);
    }

    @Override // twitter4j.api.UserMethods
    public PagableResponseList<User> getFriendsStatuses(long userId, long cursor) throws TwitterException {
        return UserJSONImpl.createPagableUserList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/friends.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&user_id=").append(userId).append("&cursor=").append(cursor).toString(), null), this.conf);
    }

    @Override // twitter4j.api.UserMethods
    public PagableResponseList<User> getFollowersStatuses(long cursor) throws TwitterException {
        return UserJSONImpl.createPagableUserList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/followers.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&cursor=").append(cursor).toString()), this.conf);
    }

    @Override // twitter4j.api.UserMethods
    public PagableResponseList<User> getFollowersStatuses(String screenName, long cursor) throws TwitterException {
        return UserJSONImpl.createPagableUserList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/followers.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&screen_name=").append(screenName).append("&cursor=").append(cursor).toString()), this.conf);
    }

    @Override // twitter4j.api.UserMethods
    public PagableResponseList<User> getFollowersStatuses(long userId, long cursor) throws TwitterException {
        return UserJSONImpl.createPagableUserList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("statuses/followers.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&user_id=").append(userId).append("&cursor=").append(cursor).toString()), this.conf);
    }

    @Override // twitter4j.api.ListMethods
    public UserList createUserList(String listName, boolean isPublicList, String description) throws TwitterException {
        ensureAuthorizationEnabled();
        List<HttpParameter> httpParams = new ArrayList<>();
        httpParams.add(new HttpParameter("name", listName));
        httpParams.add(new HttpParameter("mode", isPublicList ? "public" : "private"));
        if (description != null) {
            httpParams.add(new HttpParameter("description", description));
        }
        return new UserListJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("lists/create.json").toString(), (HttpParameter[]) httpParams.toArray(new HttpParameter[httpParams.size()])), this.conf);
    }

    @Override // twitter4j.api.ListMethods
    public UserList updateUserList(int listId, String newListName, boolean isPublicList, String newDescription) throws TwitterException {
        ensureAuthorizationEnabled();
        List<HttpParameter> httpParams = new ArrayList<>();
        httpParams.add(new HttpParameter("list_id", listId));
        if (newListName != null) {
            httpParams.add(new HttpParameter("name", newListName));
        }
        httpParams.add(new HttpParameter("mode", isPublicList ? "public" : "private"));
        if (newDescription != null) {
            httpParams.add(new HttpParameter("description", newDescription));
        }
        return new UserListJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("lists/update.json").toString(), (HttpParameter[]) httpParams.toArray(new HttpParameter[httpParams.size()])), this.conf);
    }

    @Override // twitter4j.api.ListMethods
    public PagableResponseList<UserList> getUserLists(String listOwnerScreenName, long cursor) throws TwitterException {
        return UserListJSONImpl.createPagableUserListList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("lists.json?screen_name=").append(listOwnerScreenName).append("&cursor=").append(cursor).toString()), this.conf);
    }

    @Override // twitter4j.api.ListMethods
    public PagableResponseList<UserList> getUserLists(long listOwnerUserId, long cursor) throws TwitterException {
        return UserListJSONImpl.createPagableUserListList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("lists.json?user_id=").append(listOwnerUserId).append("&cursor=").append(cursor).toString()), this.conf);
    }

    @Override // twitter4j.api.ListMethods
    public UserList showUserList(String listOwnerScreenName, int id) throws TwitterException {
        return showUserList(id);
    }

    @Override // twitter4j.api.ListMethods
    public UserList showUserList(int listId) throws TwitterException {
        return new UserListJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("lists/show.json?list_id=").append(listId).toString()), this.conf);
    }

    @Override // twitter4j.api.ListMethods
    public UserList destroyUserList(int listId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserListJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("lists/destroy.json").toString(), new HttpParameter[]{new HttpParameter("list_id", listId)}), this.conf);
    }

    @Override // twitter4j.api.ListMethods
    public ResponseList<Status> getUserListStatuses(String listOwnerScreenName, int id, Paging paging) throws TwitterException {
        return getUserListStatuses(id, paging);
    }

    @Override // twitter4j.api.ListMethods
    public ResponseList<Status> getUserListStatuses(long listOwnerId, int id, Paging paging) throws TwitterException {
        return getUserListStatuses(id, paging);
    }

    @Override // twitter4j.api.ListMethods
    public ResponseList<Status> getUserListStatuses(int listId, Paging paging) throws TwitterException {
        return StatusJSONImpl.createStatusList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("lists/statuses.json").toString(), mergeParameters(paging.asPostParameterArray(Paging.SMCP, "per_page"), new HttpParameter[]{new HttpParameter("list_id", listId), this.INCLUDE_ENTITIES, this.INCLUDE_RTS})), this.conf);
    }

    @Override // twitter4j.api.ListMethods
    public PagableResponseList<UserList> getUserListMemberships(String listMemberScreenName, long cursor) throws TwitterException {
        return UserListJSONImpl.createPagableUserListList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("lists/memberships.json?screen_name=").append(listMemberScreenName).append("&cursor=").append(cursor).toString()), this.conf);
    }

    @Override // twitter4j.api.ListMethods
    public PagableResponseList<UserList> getUserListSubscriptions(String listOwnerScreenName, long cursor) throws TwitterException {
        return UserListJSONImpl.createPagableUserListList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("lists/subscriptions.json?screen_name=").append(listOwnerScreenName).append("&cursor=").append(cursor).toString()), this.conf);
    }

    @Override // twitter4j.api.ListMethods
    public ResponseList<UserList> getAllUserLists(String screenName) throws TwitterException {
        return UserListJSONImpl.createUserListList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("lists/all.json?screen_name=").append(screenName).toString()), this.conf);
    }

    @Override // twitter4j.api.ListMethods
    public ResponseList<UserList> getAllUserLists(long userId) throws TwitterException {
        return UserListJSONImpl.createUserListList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("lists/all.json?user_id=").append(userId).toString()), this.conf);
    }

    @Override // twitter4j.api.ListMembersMethods
    public PagableResponseList<User> getUserListMembers(String listOwnerScreenName, int listId, long cursor) throws TwitterException {
        return getUserListMembers(listId, cursor);
    }

    @Override // twitter4j.api.ListMembersMethods
    public PagableResponseList<User> getUserListMembers(long listOwnerId, int listId, long cursor) throws TwitterException {
        return getUserListMembers(listId, cursor);
    }

    @Override // twitter4j.api.ListMembersMethods
    public PagableResponseList<User> getUserListMembers(int listId, long cursor) throws TwitterException {
        ensureAuthorizationEnabled();
        return UserJSONImpl.createPagableUserList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("lists/members.json?list_id=").append(listId).append("&include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&cursor=").append(cursor).toString()), this.conf);
    }

    @Override // twitter4j.api.ListMembersMethods
    public UserList addUserListMember(int listId, long userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserListJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("lists/members/create.json").toString(), new HttpParameter[]{new HttpParameter("user_id", userId), new HttpParameter("list_id", listId)}), this.conf);
    }

    @Override // twitter4j.api.ListMembersMethods
    public UserList addUserListMembers(int listId, long[] userIds) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserListJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("lists/members/create_all.json").toString(), new HttpParameter[]{new HttpParameter("list_id", listId), new HttpParameter("user_id", T4JInternalStringUtil.join(userIds))}), this.conf);
    }

    @Override // twitter4j.api.ListMembersMethods
    public UserList addUserListMembers(int listId, String[] screenNames) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserListJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("lists/members/create_all.json").toString(), new HttpParameter[]{new HttpParameter("list_id", listId), new HttpParameter("screen_name", T4JInternalStringUtil.join(screenNames))}), this.conf);
    }

    @Override // twitter4j.api.ListMembersMethods
    public UserList deleteUserListMember(int listId, long userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserListJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("lists/members/destroy.json").toString(), new HttpParameter[]{new HttpParameter("list_id", listId), new HttpParameter("user_id", userId)}), this.conf);
    }

    @Override // twitter4j.api.ListMembersMethods
    public User checkUserListMembership(String listOwnerScreenName, int listId, long userId) throws TwitterException {
        return showUserListMembership(listId, userId);
    }

    @Override // twitter4j.api.ListMembersMethods
    public User showUserListMembership(int listId, long userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("lists/members/show.json?list_id=").append(listId).append("&user_id=").append(userId).append("&include_entities=").append(this.conf.isIncludeEntitiesEnabled()).toString()), this.conf);
    }

    @Override // twitter4j.api.ListSubscribersMethods
    public PagableResponseList<User> getUserListSubscribers(String listOwnerScreenName, int listId, long cursor) throws TwitterException {
        return getUserListSubscribers(listId, cursor);
    }

    @Override // twitter4j.api.ListSubscribersMethods
    public PagableResponseList<User> getUserListSubscribers(int listId, long cursor) throws TwitterException {
        return UserJSONImpl.createPagableUserList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("lists/subscribers.json?list_id=").append(listId).append("&include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&cursor=").append(cursor).toString()), this.conf);
    }

    @Override // twitter4j.api.ListSubscribersMethods
    public UserList subscribeUserList(String listOwnerScreenName, int listId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserListJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append(listOwnerScreenName).append("/").append(listId).append("/subscribers.json").toString()), this.conf);
    }

    @Override // twitter4j.api.ListSubscribersMethods
    public UserList createUserListSubscription(int listId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserListJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("lists/subscribers/create.json").toString(), new HttpParameter[]{new HttpParameter("list_id", listId)}), this.conf);
    }

    @Override // twitter4j.api.ListSubscribersMethods
    public UserList unsubscribeUserList(String listOwnerScreenName, int listId) throws TwitterException {
        return destroyUserListSubscription(listId);
    }

    @Override // twitter4j.api.ListSubscribersMethods
    public UserList destroyUserListSubscription(int listId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserListJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("lists/subscribers/destroy.json").toString(), new HttpParameter[]{new HttpParameter("list_id", listId)}), this.conf);
    }

    @Override // twitter4j.api.ListSubscribersMethods
    public User checkUserListSubscription(String listOwnerScreenName, int listId, long userId) throws TwitterException {
        return showUserListSubscription(listId, userId);
    }

    @Override // twitter4j.api.ListSubscribersMethods
    public User showUserListSubscription(int listId, long userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("lists/subscribers/show.json?list_id=").append(listId).append("&user_id=").append(userId).append("&include_entities=").append(this.conf.isIncludeEntitiesEnabled()).toString()), this.conf);
    }

    @Override // twitter4j.api.DirectMessageMethods
    public ResponseList<DirectMessage> getDirectMessages() throws TwitterException {
        ensureAuthorizationEnabled();
        return DirectMessageJSONImpl.createDirectMessageList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("direct_messages.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).toString()), this.conf);
    }

    @Override // twitter4j.api.DirectMessageMethods
    public ResponseList<DirectMessage> getDirectMessages(Paging paging) throws TwitterException {
        ensureAuthorizationEnabled();
        return DirectMessageJSONImpl.createDirectMessageList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("direct_messages.json").toString(), mergeParameters(paging.asPostParameterArray(), this.INCLUDE_ENTITIES)), this.conf);
    }

    @Override // twitter4j.api.DirectMessageMethods
    public ResponseList<DirectMessage> getSentDirectMessages() throws TwitterException {
        ensureAuthorizationEnabled();
        return DirectMessageJSONImpl.createDirectMessageList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("direct_messages/sent.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).toString()), this.conf);
    }

    @Override // twitter4j.api.DirectMessageMethods
    public ResponseList<DirectMessage> getSentDirectMessages(Paging paging) throws TwitterException {
        ensureAuthorizationEnabled();
        return DirectMessageJSONImpl.createDirectMessageList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("direct_messages/sent.json").toString(), mergeParameters(paging.asPostParameterArray(), this.INCLUDE_ENTITIES)), this.conf);
    }

    @Override // twitter4j.api.DirectMessageMethods
    public DirectMessage sendDirectMessage(String screenName, String text) throws TwitterException {
        ensureAuthorizationEnabled();
        return new DirectMessageJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("direct_messages/new.json").toString(), new HttpParameter[]{new HttpParameter("screen_name", screenName), new HttpParameter("text", text), this.INCLUDE_ENTITIES}), this.conf);
    }

    @Override // twitter4j.api.DirectMessageMethods
    public DirectMessage sendDirectMessage(long userId, String text) throws TwitterException {
        ensureAuthorizationEnabled();
        return new DirectMessageJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("direct_messages/new.json").toString(), new HttpParameter[]{new HttpParameter("user_id", userId), new HttpParameter("text", text), this.INCLUDE_ENTITIES}), this.conf);
    }

    @Override // twitter4j.api.DirectMessageMethods
    public DirectMessage destroyDirectMessage(long id) throws TwitterException {
        ensureAuthorizationEnabled();
        return new DirectMessageJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("direct_messages/destroy/").append(id).append(".json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).toString()), this.conf);
    }

    @Override // twitter4j.api.DirectMessageMethods
    public DirectMessage showDirectMessage(long id) throws TwitterException {
        ensureAuthorizationEnabled();
        return new DirectMessageJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("direct_messages/show/").append(id).append(".json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).toString()), this.conf);
    }

    @Override // twitter4j.api.FriendshipMethods
    public User createFriendship(String screenName) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("friendships/create.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&screen_name=").append(screenName).toString()), this.conf);
    }

    @Override // twitter4j.api.FriendshipMethods
    public User createFriendship(long userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("friendships/create.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&user_id=").append(userId).toString()), this.conf);
    }

    @Override // twitter4j.api.FriendshipMethods
    public User createFriendship(String screenName, boolean follow) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("friendships/create.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&screen_name=").append(screenName).append("&follow=").append(follow).toString()), this.conf);
    }

    @Override // twitter4j.api.FriendshipMethods
    public User createFriendship(long userId, boolean follow) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("friendships/create.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&user_id=").append(userId).append("&follow=").append(follow).toString()), this.conf);
    }

    @Override // twitter4j.api.FriendshipMethods
    public User destroyFriendship(String screenName) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("friendships/destroy.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&screen_name=").append(screenName).toString()), this.conf);
    }

    @Override // twitter4j.api.FriendshipMethods
    public User destroyFriendship(long userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("friendships/destroy.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&user_id=").append(userId).toString()), this.conf);
    }

    @Override // twitter4j.api.FriendshipMethods
    public boolean existsFriendship(String userA, String userB) throws TwitterException {
        return -1 != get(new StringBuffer().append(this.conf.getRestBaseURL()).append("friendships/exists.json").toString(), HttpParameter.getParameterArray("user_a", userA, "user_b", userB)).asString().indexOf("true");
    }

    @Override // twitter4j.api.FriendshipMethods
    public Relationship showFriendship(String sourceScreenName, String targetScreenName) throws TwitterException {
        return new RelationshipJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("friendships/show.json").toString(), HttpParameter.getParameterArray("source_screen_name", sourceScreenName, "target_screen_name", targetScreenName)), this.conf);
    }

    @Override // twitter4j.api.FriendshipMethods
    public Relationship showFriendship(long sourceId, long targetId) throws TwitterException {
        return new RelationshipJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("friendships/show.json").toString(), new HttpParameter[]{new HttpParameter("source_id", sourceId), new HttpParameter("target_id", targetId)}), this.conf);
    }

    @Override // twitter4j.api.FriendshipMethods
    public IDs getIncomingFriendships(long cursor) throws TwitterException {
        ensureAuthorizationEnabled();
        return new IDsJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("friendships/incoming.json?cursor=").append(cursor).toString()), this.conf);
    }

    @Override // twitter4j.api.FriendshipMethods
    public IDs getOutgoingFriendships(long cursor) throws TwitterException {
        ensureAuthorizationEnabled();
        return new IDsJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("friendships/outgoing.json?cursor=").append(cursor).toString()), this.conf);
    }

    @Override // twitter4j.api.FriendshipMethods
    public ResponseList<Friendship> lookupFriendships(String[] screenNames) throws TwitterException {
        ensureAuthorizationEnabled();
        return FriendshipJSONImpl.createFriendshipList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("friendships/lookup.json?screen_name=").append(T4JInternalStringUtil.join(screenNames)).toString()), this.conf);
    }

    @Override // twitter4j.api.FriendshipMethods
    public ResponseList<Friendship> lookupFriendships(long[] ids) throws TwitterException {
        ensureAuthorizationEnabled();
        return FriendshipJSONImpl.createFriendshipList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("friendships/lookup.json?user_id=").append(T4JInternalStringUtil.join(ids)).toString()), this.conf);
    }

    @Override // twitter4j.api.FriendshipMethods
    public Relationship updateFriendship(String screenName, boolean enableDeviceNotification, boolean retweets) throws TwitterException {
        ensureAuthorizationEnabled();
        return new RelationshipJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("friendships/update.json").toString(), new HttpParameter[]{new HttpParameter("screen_name", screenName), new HttpParameter("device", enableDeviceNotification), new HttpParameter("retweets", enableDeviceNotification)}), this.conf);
    }

    @Override // twitter4j.api.FriendshipMethods
    public Relationship updateFriendship(long userId, boolean enableDeviceNotification, boolean retweets) throws TwitterException {
        ensureAuthorizationEnabled();
        return new RelationshipJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("friendships/update.json").toString(), new HttpParameter[]{new HttpParameter("user_id", userId), new HttpParameter("device", enableDeviceNotification), new HttpParameter("retweets", enableDeviceNotification)}), this.conf);
    }

    @Override // twitter4j.api.FriendshipMethods
    public IDs getNoRetweetIds() throws TwitterException {
        ensureAuthorizationEnabled();
        return new IDsJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("friendships/no_retweet_ids.json").toString()), this.conf);
    }

    @Override // twitter4j.api.FriendsFollowersMethods
    public IDs getFriendsIDs(long cursor) throws TwitterException {
        return new IDsJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("friends/ids.json?cursor=").append(cursor).toString()), this.conf);
    }

    @Override // twitter4j.api.FriendsFollowersMethods
    public IDs getFriendsIDs(long userId, long cursor) throws TwitterException {
        return new IDsJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("friends/ids.json?user_id=").append(userId).append("&cursor=").append(cursor).toString()), this.conf);
    }

    @Override // twitter4j.api.FriendsFollowersMethods
    public IDs getFriendsIDs(String screenName, long cursor) throws TwitterException {
        return new IDsJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("friends/ids.json?screen_name=").append(screenName).append("&cursor=").append(cursor).toString()), this.conf);
    }

    @Override // twitter4j.api.FriendsFollowersMethods
    public IDs getFollowersIDs(long cursor) throws TwitterException {
        return new IDsJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("followers/ids.json?cursor=").append(cursor).toString()), this.conf);
    }

    @Override // twitter4j.api.FriendsFollowersMethods
    public IDs getFollowersIDs(long userId, long cursor) throws TwitterException {
        return new IDsJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("followers/ids.json?user_id=").append(userId).append("&cursor=").append(cursor).toString()), this.conf);
    }

    @Override // twitter4j.api.FriendsFollowersMethods
    public IDs getFollowersIDs(String screenName, long cursor) throws TwitterException {
        return new IDsJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("followers/ids.json?screen_name=").append(screenName).append("&cursor=").append(cursor).toString()), this.conf);
    }

    @Override // twitter4j.api.AccountMethods
    public User verifyCredentials() throws TwitterException {
        return super.fillInIDAndScreenName();
    }

    @Override // twitter4j.api.AccountMethods
    public RateLimitStatus getRateLimitStatus() throws TwitterException {
        return new RateLimitStatusJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("account/rate_limit_status.json").toString()), this.conf);
    }

    @Override // twitter4j.api.AccountMethods
    public User updateProfile(String name, String url, String location, String description) throws TwitterException {
        ensureAuthorizationEnabled();
        List<HttpParameter> profile = new ArrayList<>(4);
        addParameterToList(profile, "name", name);
        addParameterToList(profile, SessionMActivity.INTENT_PARAM_URL, url);
        addParameterToList(profile, "location", location);
        addParameterToList(profile, "description", description);
        profile.add(this.INCLUDE_ENTITIES);
        return new UserJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("account/update_profile.json").toString(), (HttpParameter[]) profile.toArray(new HttpParameter[profile.size()])), this.conf);
    }

    @Override // twitter4j.api.AccountMethods
    public AccountTotals getAccountTotals() throws TwitterException {
        ensureAuthorizationEnabled();
        return new AccountTotalsJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("account/totals.json").toString()), this.conf);
    }

    @Override // twitter4j.api.AccountMethods
    public AccountSettings getAccountSettings() throws TwitterException {
        ensureAuthorizationEnabled();
        return new AccountSettingsJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("account/settings.json").toString()), this.conf);
    }

    @Override // twitter4j.api.AccountMethods
    public User updateProfileColors(String profileBackgroundColor, String profileTextColor, String profileLinkColor, String profileSidebarFillColor, String profileSidebarBorderColor) throws TwitterException {
        ensureAuthorizationEnabled();
        List<HttpParameter> colors = new ArrayList<>(6);
        addParameterToList(colors, "profile_background_color", profileBackgroundColor);
        addParameterToList(colors, "profile_text_color", profileTextColor);
        addParameterToList(colors, "profile_link_color", profileLinkColor);
        addParameterToList(colors, "profile_sidebar_fill_color", profileSidebarFillColor);
        addParameterToList(colors, "profile_sidebar_border_color", profileSidebarBorderColor);
        colors.add(this.INCLUDE_ENTITIES);
        return new UserJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("account/update_profile_colors.json").toString(), (HttpParameter[]) colors.toArray(new HttpParameter[colors.size()])), this.conf);
    }

    private void addParameterToList(List<HttpParameter> colors, String paramName, String color) {
        if (color != null) {
            colors.add(new HttpParameter(paramName, color));
        }
    }

    @Override // twitter4j.api.AccountMethods
    public User updateProfileImage(File image) throws TwitterException {
        checkFileValidity(image);
        ensureAuthorizationEnabled();
        return new UserJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("account/update_profile_image.json").toString(), new HttpParameter[]{new HttpParameter("image", image), this.INCLUDE_ENTITIES}), this.conf);
    }

    @Override // twitter4j.api.AccountMethods
    public User updateProfileImage(InputStream image) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("account/update_profile_image.json").toString(), new HttpParameter[]{new HttpParameter("image", "image", image), this.INCLUDE_ENTITIES}), this.conf);
    }

    @Override // twitter4j.api.AccountMethods
    public User updateProfileBackgroundImage(File image, boolean tile) throws TwitterException {
        ensureAuthorizationEnabled();
        checkFileValidity(image);
        return new UserJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("account/update_profile_background_image.json").toString(), new HttpParameter[]{new HttpParameter("image", image), new HttpParameter("tile", tile), this.INCLUDE_ENTITIES}), this.conf);
    }

    @Override // twitter4j.api.AccountMethods
    public User updateProfileBackgroundImage(InputStream image, boolean tile) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("account/update_profile_background_image.json").toString(), new HttpParameter[]{new HttpParameter("image", "image", image), new HttpParameter("tile", tile), this.INCLUDE_ENTITIES}), this.conf);
    }

    private void checkFileValidity(File image) throws TwitterException {
        if (!image.exists()) {
            throw new TwitterException(new FileNotFoundException(new StringBuffer().append(image).append(" is not found.").toString()));
        }
        if (!image.isFile()) {
            throw new TwitterException(new IOException(new StringBuffer().append(image).append(" is not a file.").toString()));
        }
    }

    @Override // twitter4j.api.FavoriteMethods
    public ResponseList<Status> getFavorites() throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("favorites.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).toString()), this.conf);
    }

    @Override // twitter4j.api.FavoriteMethods
    public ResponseList<Status> getFavorites(int page) throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("favorites.json").toString(), new HttpParameter[]{new HttpParameter("page", page), this.INCLUDE_ENTITIES}), this.conf);
    }

    @Override // twitter4j.api.FavoriteMethods
    public ResponseList<Status> getFavorites(String id) throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("favorites/").append(id).append(".json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).toString()), this.conf);
    }

    @Override // twitter4j.api.FavoriteMethods
    public ResponseList<Status> getFavorites(String id, int page) throws TwitterException {
        ensureAuthorizationEnabled();
        return StatusJSONImpl.createStatusList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("favorites/").append(id).append(".json").toString(), mergeParameters(HttpParameter.getParameterArray("page", page), this.INCLUDE_ENTITIES)), this.conf);
    }

    @Override // twitter4j.api.FavoriteMethods
    public Status createFavorite(long id) throws TwitterException {
        ensureAuthorizationEnabled();
        return new StatusJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("favorites/create/").append(id).append(".json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).toString()), this.conf);
    }

    @Override // twitter4j.api.FavoriteMethods
    public Status destroyFavorite(long id) throws TwitterException {
        ensureAuthorizationEnabled();
        return new StatusJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("favorites/destroy/").append(id).append(".json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).toString()), this.conf);
    }

    @Override // twitter4j.api.NotificationMethods
    public User enableNotification(String screenName) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("notifications/follow.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&screen_name=").append(screenName).toString()), this.conf);
    }

    @Override // twitter4j.api.NotificationMethods
    public User enableNotification(long userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("notifications/follow.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&user_id=").append(userId).toString()), this.conf);
    }

    @Override // twitter4j.api.NotificationMethods
    public User disableNotification(String screenName) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("notifications/leave.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&screen_name=").append(screenName).toString()), this.conf);
    }

    @Override // twitter4j.api.NotificationMethods
    public User disableNotification(long userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("notifications/leave.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&user_id=").append(userId).toString()), this.conf);
    }

    @Override // twitter4j.api.BlockMethods
    public User createBlock(String screenName) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("blocks/create.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&screen_name=").append(screenName).toString()), this.conf);
    }

    @Override // twitter4j.api.BlockMethods
    public User createBlock(long userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("blocks/create.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&user_id=").append(userId).toString()), this.conf);
    }

    @Override // twitter4j.api.BlockMethods
    public User destroyBlock(String screen_name) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("blocks/destroy.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&screen_name=").append(screen_name).toString()), this.conf);
    }

    @Override // twitter4j.api.BlockMethods
    public User destroyBlock(long userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("blocks/destroy.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&user_id=").append(userId).toString()), this.conf);
    }

    @Override // twitter4j.api.BlockMethods
    public boolean existsBlock(String screenName) throws TwitterException {
        ensureAuthorizationEnabled();
        try {
            return -1 == get(new StringBuffer().append(this.conf.getRestBaseURL()).append("blocks/exists.json?screen_name=").append(screenName).toString()).asString().indexOf("You are not blocking this user.");
        } catch (TwitterException te) {
            if (te.getStatusCode() != 404) {
                throw te;
            }
            return false;
        }
    }

    @Override // twitter4j.api.BlockMethods
    public boolean existsBlock(long userId) throws TwitterException {
        ensureAuthorizationEnabled();
        try {
            return -1 == get(new StringBuffer().append(this.conf.getRestBaseURL()).append("blocks/exists.json?user_id=").append(userId).toString()).asString().indexOf("<error>You are not blocking this user.</error>");
        } catch (TwitterException te) {
            if (te.getStatusCode() != 404) {
                throw te;
            }
            return false;
        }
    }

    @Override // twitter4j.api.BlockMethods
    public ResponseList<User> getBlockingUsers() throws TwitterException {
        ensureAuthorizationEnabled();
        return UserJSONImpl.createUserList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("blocks/blocking.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).toString()), this.conf);
    }

    @Override // twitter4j.api.BlockMethods
    public ResponseList<User> getBlockingUsers(int page) throws TwitterException {
        ensureAuthorizationEnabled();
        return UserJSONImpl.createUserList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("blocks/blocking.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&page=").append(page).toString()), this.conf);
    }

    @Override // twitter4j.api.BlockMethods
    public IDs getBlockingUsersIDs() throws TwitterException {
        ensureAuthorizationEnabled();
        return new IDsJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("blocks/blocking/ids.json").toString()), this.conf);
    }

    @Override // twitter4j.api.SpamReportingMethods
    public User reportSpam(long userId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("report_spam.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&user_id=").append(userId).toString()), this.conf);
    }

    @Override // twitter4j.api.SpamReportingMethods
    public User reportSpam(String screenName) throws TwitterException {
        ensureAuthorizationEnabled();
        return new UserJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("report_spam.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).append("&screen_name=").append(screenName).toString()), this.conf);
    }

    @Override // twitter4j.api.SavedSearchesMethods
    public List<SavedSearch> getSavedSearches() throws TwitterException {
        ensureAuthorizationEnabled();
        return SavedSearchJSONImpl.createSavedSearchList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("saved_searches.json").toString()), this.conf);
    }

    @Override // twitter4j.api.SavedSearchesMethods
    public SavedSearch showSavedSearch(int id) throws TwitterException {
        ensureAuthorizationEnabled();
        return new SavedSearchJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("saved_searches/show/").append(id).append(".json").toString()), this.conf);
    }

    @Override // twitter4j.api.SavedSearchesMethods
    public SavedSearch createSavedSearch(String query) throws TwitterException {
        ensureAuthorizationEnabled();
        return new SavedSearchJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("saved_searches/create.json").toString(), new HttpParameter[]{new HttpParameter("query", query)}), this.conf);
    }

    @Override // twitter4j.api.SavedSearchesMethods
    public SavedSearch destroySavedSearch(int id) throws TwitterException {
        ensureAuthorizationEnabled();
        return new SavedSearchJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("saved_searches/destroy/").append(id).append(".json").toString()), this.conf);
    }

    @Override // twitter4j.api.LocalTrendsMethods
    public ResponseList<Location> getAvailableTrends() throws TwitterException {
        return LocationJSONImpl.createLocationList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("trends/available.json").toString()), this.conf);
    }

    @Override // twitter4j.api.LocalTrendsMethods
    public ResponseList<Location> getAvailableTrends(GeoLocation location) throws TwitterException {
        return LocationJSONImpl.createLocationList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("trends/available.json").toString(), new HttpParameter[]{new HttpParameter("lat", location.getLatitude()), new HttpParameter("long", location.getLongitude())}), this.conf);
    }

    @Override // twitter4j.api.LocalTrendsMethods
    public Trends getLocationTrends(int woeid) throws TwitterException {
        return new TrendsJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("trends/").append(woeid).append(".json").toString()), this.conf);
    }

    @Override // twitter4j.api.GeoMethods
    public ResponseList<Place> searchPlaces(GeoQuery query) throws TwitterException {
        try {
            return PlaceJSONImpl.createPlaceList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("geo/search.json").toString(), query.asHttpParameterArray()), this.conf);
        } catch (TwitterException te) {
            if (te.getStatusCode() == 404) {
                return new ResponseListImpl(0, null);
            }
            throw te;
        }
    }

    @Override // twitter4j.api.GeoMethods
    public SimilarPlaces getSimilarPlaces(GeoLocation location, String name, String containedWithin, String streetAddress) throws TwitterException {
        List<HttpParameter> params = new ArrayList<>(3);
        params.add(new HttpParameter("lat", location.getLatitude()));
        params.add(new HttpParameter("long", location.getLongitude()));
        params.add(new HttpParameter("name", name));
        if (containedWithin != null) {
            params.add(new HttpParameter("contained_within", containedWithin));
        }
        if (streetAddress != null) {
            params.add(new HttpParameter("attribute:street_address", streetAddress));
        }
        return SimilarPlacesImpl.createSimilarPlaces(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("geo/similar_places.json").toString(), (HttpParameter[]) params.toArray(new HttpParameter[params.size()])), this.conf);
    }

    @Override // twitter4j.api.GeoMethods
    public ResponseList<Place> reverseGeoCode(GeoQuery query) throws TwitterException {
        try {
            return PlaceJSONImpl.createPlaceList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("geo/reverse_geocode.json").toString(), query.asHttpParameterArray()), this.conf);
        } catch (TwitterException te) {
            if (te.getStatusCode() == 404) {
                return new ResponseListImpl(0, null);
            }
            throw te;
        }
    }

    @Override // twitter4j.api.GeoMethods
    public Place getGeoDetails(String id) throws TwitterException {
        return new PlaceJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("geo/id/").append(id).append(".json").toString()), this.conf);
    }

    @Override // twitter4j.api.GeoMethods
    public Place createPlace(String name, String containedWithin, String token, GeoLocation location, String streetAddress) throws TwitterException {
        ensureAuthorizationEnabled();
        List<HttpParameter> params = new ArrayList<>(3);
        params.add(new HttpParameter("name", name));
        params.add(new HttpParameter("contained_within", containedWithin));
        params.add(new HttpParameter("token", token));
        params.add(new HttpParameter("lat", location.getLatitude()));
        params.add(new HttpParameter("long", location.getLongitude()));
        if (streetAddress != null) {
            params.add(new HttpParameter("attribute:street_address", streetAddress));
        }
        return new PlaceJSONImpl(post(new StringBuffer().append(this.conf.getRestBaseURL()).append("geo/place.json").toString(), (HttpParameter[]) params.toArray(new HttpParameter[params.size()])), this.conf);
    }

    @Override // twitter4j.api.LegalResources
    public String getTermsOfService() throws TwitterException {
        try {
            return get(new StringBuffer().append(this.conf.getRestBaseURL()).append("legal/tos.json").toString()).asJSONObject().getString("tos");
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }

    @Override // twitter4j.api.LegalResources
    public String getPrivacyPolicy() throws TwitterException {
        try {
            return get(new StringBuffer().append(this.conf.getRestBaseURL()).append("legal/privacy.json").toString()).asJSONObject().getString("privacy");
        } catch (JSONException e) {
            throw new TwitterException(e);
        }
    }

    @Override // twitter4j.api.NewTwitterMethods
    public RelatedResults getRelatedResults(long statusId) throws TwitterException {
        ensureAuthorizationEnabled();
        return new RelatedResultsJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("related_results/show/").append(Long.toString(statusId)).append(".json").toString()), this.conf);
    }

    @Override // twitter4j.api.HelpMethods
    public boolean test() throws TwitterException {
        return -1 != get(new StringBuffer().append(this.conf.getRestBaseURL()).append("help/test.json").toString()).asString().indexOf(b.N);
    }

    @Override // twitter4j.api.HelpMethods
    public TwitterAPIConfiguration getAPIConfiguration() throws TwitterException {
        return new TwitterAPIConfigurationJSONImpl(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("help/configuration.json").toString()), this.conf);
    }

    @Override // twitter4j.api.HelpMethods
    public ResponseList<HelpMethods.Language> getLanguages() throws TwitterException {
        return LanguageJSONImpl.createLanguageList(get(new StringBuffer().append(this.conf.getRestBaseURL()).append("help/languages.json").toString()), this.conf);
    }

    @Override // twitter4j.TwitterBaseImpl
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        TwitterImpl twitter = (TwitterImpl) o;
        return this.INCLUDE_ENTITIES.equals(twitter.INCLUDE_ENTITIES) && this.INCLUDE_RTS.equals(twitter.INCLUDE_RTS);
    }

    private HttpResponse get(String url) throws TwitterException {
        if (!this.conf.isMBeanEnabled()) {
            return this.http.get(url, this.auth);
        }
        HttpResponse response = null;
        long start = System.currentTimeMillis();
        try {
            response = this.http.get(url, this.auth);
            return response;
        } finally {
            long elapsedTime = System.currentTimeMillis() - start;
            TwitterAPIMonitor.getInstance().methodCalled(url, elapsedTime, isOk(response));
        }
    }

    private HttpResponse get(String url, HttpParameter[] parameters) throws TwitterException {
        if (!this.conf.isMBeanEnabled()) {
            return this.http.get(url, parameters, this.auth);
        }
        HttpResponse response = null;
        long start = System.currentTimeMillis();
        try {
            response = this.http.get(url, parameters, this.auth);
            return response;
        } finally {
            long elapsedTime = System.currentTimeMillis() - start;
            TwitterAPIMonitor.getInstance().methodCalled(url, elapsedTime, isOk(response));
        }
    }

    private HttpResponse post(String url) throws TwitterException {
        if (!this.conf.isMBeanEnabled()) {
            return this.http.post(url, this.auth);
        }
        HttpResponse response = null;
        long start = System.currentTimeMillis();
        try {
            response = this.http.post(url, this.auth);
            return response;
        } finally {
            long elapsedTime = System.currentTimeMillis() - start;
            TwitterAPIMonitor.getInstance().methodCalled(url, elapsedTime, isOk(response));
        }
    }

    private HttpResponse post(String url, HttpParameter[] parameters) throws TwitterException {
        if (!this.conf.isMBeanEnabled()) {
            return this.http.post(url, parameters, this.auth);
        }
        HttpResponse response = null;
        long start = System.currentTimeMillis();
        try {
            response = this.http.post(url, parameters, this.auth);
            return response;
        } finally {
            long elapsedTime = System.currentTimeMillis() - start;
            TwitterAPIMonitor.getInstance().methodCalled(url, elapsedTime, isOk(response));
        }
    }

    private HttpResponse delete(String url) throws TwitterException {
        if (!this.conf.isMBeanEnabled()) {
            return this.http.delete(url, this.auth);
        }
        HttpResponse response = null;
        long start = System.currentTimeMillis();
        try {
            response = this.http.delete(url, this.auth);
            return response;
        } finally {
            long elapsedTime = System.currentTimeMillis() - start;
            TwitterAPIMonitor.getInstance().methodCalled(url, elapsedTime, isOk(response));
        }
    }

    private boolean isOk(HttpResponse response) {
        return response != null && response.getStatusCode() < 300;
    }

    @Override // twitter4j.TwitterBaseImpl
    public int hashCode() {
        int result = super.hashCode();
        return (((result * 31) + this.INCLUDE_ENTITIES.hashCode()) * 31) + this.INCLUDE_RTS.hashCode();
    }

    @Override // twitter4j.TwitterBaseImpl
    public String toString() {
        return new StringBuffer().append("TwitterImpl{INCLUDE_ENTITIES=").append(this.INCLUDE_ENTITIES).append(", INCLUDE_RTS=").append(this.INCLUDE_RTS).append('}').toString();
    }
}
