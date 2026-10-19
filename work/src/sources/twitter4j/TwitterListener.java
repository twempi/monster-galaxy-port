package twitter4j;

import java.util.List;
import twitter4j.api.HelpMethods;

/* JADX INFO: loaded from: classes.dex */
public interface TwitterListener {
    public static final TwitterMethod SEARCH = TwitterMethod.SEARCH;
    public static final TwitterMethod TRENDS = TwitterMethod.TRENDS;
    public static final TwitterMethod CURRENT_TRENDS = TwitterMethod.CURRENT_TRENDS;
    public static final TwitterMethod DAILY_TRENDS = TwitterMethod.DAILY_TRENDS;
    public static final TwitterMethod WEEKLY_TRENDS = TwitterMethod.WEEKLY_TRENDS;
    public static final TwitterMethod PUBLIC_TIMELINE = TwitterMethod.PUBLIC_TIMELINE;
    public static final TwitterMethod HOME_TIMELINE = TwitterMethod.HOME_TIMELINE;
    public static final TwitterMethod FRIENDS_TIMELINE = TwitterMethod.FRIENDS_TIMELINE;
    public static final TwitterMethod USER_TIMELINE = TwitterMethod.USER_TIMELINE;
    public static final TwitterMethod MENTIONS = TwitterMethod.MENTIONS;
    public static final TwitterMethod RETWEETED_BY_ME = TwitterMethod.RETWEETED_BY_ME;
    public static final TwitterMethod RETWEETED_TO_ME = TwitterMethod.RETWEETED_TO_ME;
    public static final TwitterMethod RETWEETS_OF_ME = TwitterMethod.RETWEETS_OF_ME;
    public static final TwitterMethod RETWEETED_BY_USER = TwitterMethod.RETWEETED_BY_USER;
    public static final TwitterMethod RETWEETED_TO_USER = TwitterMethod.RETWEETED_TO_USER;
    public static final TwitterMethod SHOW_STATUS = TwitterMethod.SHOW_STATUS;
    public static final TwitterMethod UPDATE_STATUS = TwitterMethod.UPDATE_STATUS;
    public static final TwitterMethod DESTROY_STATUS = TwitterMethod.DESTROY_STATUS;
    public static final TwitterMethod RETWEET_STATUS = TwitterMethod.RETWEET_STATUS;
    public static final TwitterMethod RETWEETS = TwitterMethod.RETWEETS;
    public static final TwitterMethod RETWEETED_BY = TwitterMethod.RETWEETED_BY;
    public static final TwitterMethod RETWEETED_BY_IDS = TwitterMethod.RETWEETED_BY_IDS;
    public static final TwitterMethod SHOW_USER = TwitterMethod.SHOW_USER;
    public static final TwitterMethod LOOKUP_USERS = TwitterMethod.LOOKUP_USERS;
    public static final TwitterMethod SEARCH_USERS = TwitterMethod.SEARCH_USERS;
    public static final TwitterMethod SUGGESTED_USER_CATEGORIES = TwitterMethod.SUGGESTED_USER_CATEGORIES;
    public static final TwitterMethod USER_SUGGESTIONS = TwitterMethod.USER_SUGGESTIONS;
    public static final TwitterMethod MEMBER_SUGGESTIONS = TwitterMethod.MEMBER_SUGGESTIONS;
    public static final TwitterMethod PROFILE_IMAGE = TwitterMethod.PROFILE_IMAGE;
    public static final TwitterMethod FRIENDS_STATUSES = TwitterMethod.FRIENDS_STATUSES;
    public static final TwitterMethod FOLLOWERS_STATUSES = TwitterMethod.FOLLOWERS_STATUSES;
    public static final TwitterMethod CREATE_USER_LIST = TwitterMethod.CREATE_USER_LIST;
    public static final TwitterMethod UPDATE_USER_LIST = TwitterMethod.UPDATE_USER_LIST;
    public static final TwitterMethod USER_LISTS = TwitterMethod.USER_LISTS;
    public static final TwitterMethod SHOW_USER_LIST = TwitterMethod.SHOW_USER_LIST;
    public static final TwitterMethod DSTROY_USER_LIST = TwitterMethod.DESTROY_USER_LIST;
    public static final TwitterMethod USER_LIST_STATUSES = TwitterMethod.USER_LIST_STATUSES;
    public static final TwitterMethod USER_LIST_MEMBERSHIPS = TwitterMethod.USER_LIST_MEMBERSHIPS;
    public static final TwitterMethod USER_LIST_SUBSCRIPTIONS = TwitterMethod.USER_LIST_SUBSCRIPTIONS;
    public static final TwitterMethod ALL_USER_LISTS = TwitterMethod.ALL_USER_LISTS;
    public static final TwitterMethod LIST_MEMBERS = TwitterMethod.LIST_MEMBERS;
    public static final TwitterMethod ADD_LIST_MEMBER = TwitterMethod.ADD_LIST_MEMBER;
    public static final TwitterMethod ADD_LIST_MEMBERS = TwitterMethod.ADD_LIST_MEMBERS;
    public static final TwitterMethod DELETE_LIST_MEMBER = TwitterMethod.DELETE_LIST_MEMBER;
    public static final TwitterMethod CHECK_LIST_MEMBERSHIP = TwitterMethod.CHECK_LIST_MEMBERSHIP;
    public static final TwitterMethod LIST_SUBSCRIBERS = TwitterMethod.LIST_SUBSCRIBERS;
    public static final TwitterMethod SUBSCRIBE_LIST = TwitterMethod.SUBSCRIBE_LIST;
    public static final TwitterMethod UNSUBSCRIBE_LIST = TwitterMethod.UNSUBSCRIBE_LIST;
    public static final TwitterMethod CHECK_LIST_SUBSCRIPTION = TwitterMethod.CHECK_LIST_SUBSCRIPTION;
    public static final TwitterMethod DIRECT_MESSAGES = TwitterMethod.DIRECT_MESSAGES;
    public static final TwitterMethod SENT_DIRECT_MESSAGES = TwitterMethod.SENT_DIRECT_MESSAGES;
    public static final TwitterMethod SEND_DIRECT_MESSAGE = TwitterMethod.SEND_DIRECT_MESSAGE;
    public static final TwitterMethod DESTROY_DIRECT_MESSAGE = TwitterMethod.DESTROY_DIRECT_MESSAGE;
    public static final TwitterMethod CREATE_FRIENDSHIP = TwitterMethod.CREATE_FRIENDSHIP;
    public static final TwitterMethod DESTROY_FRIENDSHIP = TwitterMethod.DESTROY_FRIENDSHIP;
    public static final TwitterMethod EXISTS_FRIENDSHIP = TwitterMethod.EXISTS_FRIENDSHIP;
    public static final TwitterMethod SHOW_FRIENDSHIP = TwitterMethod.SHOW_FRIENDSHIP;
    public static final TwitterMethod INCOMING_FRIENDSHIP = TwitterMethod.INCOMING_FRIENDSHIPS;
    public static final TwitterMethod OUTGOING_FRIENDSHIPS = TwitterMethod.OUTGOING_FRIENDSHIPS;
    public static final TwitterMethod LOOKUP_FRIENDSHIPS = TwitterMethod.LOOKUP_FRIENDSHIPS;
    public static final TwitterMethod UPDATE_FRIENDSHIP = TwitterMethod.UPDATE_FRIENDSHIP;
    public static final TwitterMethod FRIENDS_IDS = TwitterMethod.FRIENDS_IDS;
    public static final TwitterMethod FOLLOWERS_IDS = TwitterMethod.FOLLOWERS_IDS;
    public static final TwitterMethod VERIFY_CREDENTIALS = TwitterMethod.VERIFY_CREDENTIALS;
    public static final TwitterMethod RATE_LIMIT_STATUS = TwitterMethod.RATE_LIMIT_STATUS;
    public static final TwitterMethod UPDATE_PROFILE_COLORS = TwitterMethod.UPDATE_PROFILE_COLORS;
    public static final TwitterMethod UPDATE_PROFILE_IMAGE = TwitterMethod.UPDATE_PROFILE_IMAGE;
    public static final TwitterMethod UPDATE_PROFILE_BACKGROUND_IMAGE = TwitterMethod.UPDATE_PROFILE_BACKGROUND_IMAGE;
    public static final TwitterMethod UPDATE_PROFILE = TwitterMethod.UPDATE_PROFILE;
    public static final TwitterMethod ACCOUNT_TOTALS = TwitterMethod.ACCOUNT_TOTALS;
    public static final TwitterMethod ACCOUNT_SETTINGS = TwitterMethod.ACCOUNT_SETTINGS;
    public static final TwitterMethod FAVORITES = TwitterMethod.FAVORITES;
    public static final TwitterMethod CREATE_FAVORITE = TwitterMethod.CREATE_FAVORITE;
    public static final TwitterMethod DESTROY_FAVORITE = TwitterMethod.DESTROY_FAVORITE;
    public static final TwitterMethod ENABLE_NOTIFICATION = TwitterMethod.ENABLE_NOTIFICATION;
    public static final TwitterMethod DISABLE_NOTIFICATION = TwitterMethod.DISABLE_NOTIFICATION;
    public static final TwitterMethod CREATE_BLOCK = TwitterMethod.CREATE_BLOCK;
    public static final TwitterMethod DESTROY_BLOCK = TwitterMethod.DESTROY_BLOCK;
    public static final TwitterMethod EXISTS_BLOCK = TwitterMethod.EXISTS_BLOCK;
    public static final TwitterMethod BLOCKING_USERS = TwitterMethod.BLOCKING_USERS;
    public static final TwitterMethod BLOCKING_USERS_IDS = TwitterMethod.BLOCKING_USERS_IDS;
    public static final TwitterMethod REPORT_SPAM = TwitterMethod.REPORT_SPAM;
    public static final TwitterMethod AVAILABLE_TRENDS = TwitterMethod.AVAILABLE_TRENDS;
    public static final TwitterMethod LOCATION_TRENDS = TwitterMethod.LOCATION_TRENDS;
    public static final TwitterMethod SEARCH_PLACES = TwitterMethod.SEARCH_PLACES;
    public static final TwitterMethod SIMILAR_PLACES = TwitterMethod.SIMILAR_PLACES;
    public static final TwitterMethod NEAR_BY_PLACES = TwitterMethod.NEAR_BY_PLACES;
    public static final TwitterMethod REVERSE_GEO_CODE = TwitterMethod.REVERSE_GEO_CODE;
    public static final TwitterMethod GEO_DETAILS = TwitterMethod.GEO_DETAILS;
    public static final TwitterMethod CREATE_PLACE = TwitterMethod.CREATE_PLACE;
    public static final TwitterMethod TERMS_OF_SERVICE = TwitterMethod.TERMS_OF_SERVICE;
    public static final TwitterMethod PRIVACY_POLICY = TwitterMethod.PRIVACY_POLICY;
    public static final TwitterMethod RELATED_RESULT = TwitterMethod.RELATED_RESULTS;
    public static final TwitterMethod TEST = TwitterMethod.TEST;
    public static final TwitterMethod CONFIGURATION = TwitterMethod.CONFIGURATION;
    public static final TwitterMethod LANGUAGES = TwitterMethod.LANGUAGES;

    void addedUserListMember(UserList userList);

    void addedUserListMembers(UserList userList);

    void checkedUserListMembership(User user);

    void checkedUserListSubscription(User user);

    void createdBlock(User user);

    void createdFavorite(Status status);

    void createdFriendship(User user);

    void createdPlace(Place place);

    void createdUserList(UserList userList);

    void deletedUserListMember(UserList userList);

    void destroyedBlock(User user);

    void destroyedDirectMessage(DirectMessage directMessage);

    void destroyedFavorite(Status status);

    void destroyedFriendship(User user);

    void destroyedStatus(Status status);

    void destroyedUserList(UserList userList);

    void disabledNotification(User user);

    void enabledNotification(User user);

    void gotAPIConfiguration(TwitterAPIConfiguration twitterAPIConfiguration);

    void gotAccountSettings(AccountSettings accountSettings);

    void gotAccountTotals(AccountTotals accountTotals);

    void gotAllUserLists(ResponseList<UserList> responseList);

    void gotAvailableTrends(ResponseList<Location> responseList);

    void gotBlockingUsers(ResponseList<User> responseList);

    void gotBlockingUsersIDs(IDs iDs);

    void gotCurrentTrends(Trends trends);

    void gotDailyTrends(List<Trends> list);

    void gotDirectMessage(DirectMessage directMessage);

    void gotDirectMessages(ResponseList<DirectMessage> responseList);

    void gotExistsBlock(boolean z);

    void gotExistsFriendship(boolean z);

    void gotFavorites(ResponseList<Status> responseList);

    void gotFollowersIDs(IDs iDs);

    void gotFollowersStatuses(PagableResponseList<User> pagableResponseList);

    void gotFriendsIDs(IDs iDs);

    void gotFriendsStatuses(PagableResponseList<User> pagableResponseList);

    void gotFriendsTimeline(ResponseList<Status> responseList);

    void gotGeoDetails(Place place);

    void gotHomeTimeline(ResponseList<Status> responseList);

    void gotIncomingFriendships(IDs iDs);

    void gotLanguages(ResponseList<HelpMethods.Language> responseList);

    void gotLocationTrends(Trends trends);

    void gotMemberSuggestions(ResponseList<User> responseList);

    void gotMentions(ResponseList<Status> responseList);

    void gotNearByPlaces(ResponseList<Place> responseList);

    void gotNoRetweetIds(IDs iDs);

    void gotOutgoingFriendships(IDs iDs);

    void gotPrivacyPolicy(String str);

    void gotProfileImage(ProfileImage profileImage);

    void gotPublicTimeline(ResponseList<Status> responseList);

    void gotRateLimitStatus(RateLimitStatus rateLimitStatus);

    void gotRelatedResults(RelatedResults relatedResults);

    void gotRetweetedBy(ResponseList<User> responseList);

    void gotRetweetedByIDs(IDs iDs);

    void gotRetweetedByMe(ResponseList<Status> responseList);

    void gotRetweetedByUser(ResponseList<Status> responseList);

    void gotRetweetedToMe(ResponseList<Status> responseList);

    void gotRetweetedToUser(ResponseList<Status> responseList);

    void gotRetweets(ResponseList<Status> responseList);

    void gotRetweetsOfMe(ResponseList<Status> responseList);

    void gotReverseGeoCode(ResponseList<Place> responseList);

    void gotSentDirectMessages(ResponseList<DirectMessage> responseList);

    void gotShowFriendship(Relationship relationship);

    void gotShowStatus(Status status);

    void gotShowUserList(UserList userList);

    void gotSimilarPlaces(SimilarPlaces similarPlaces);

    void gotSuggestedUserCategories(ResponseList<Category> responseList);

    void gotTermsOfService(String str);

    void gotTrends(Trends trends);

    void gotUserDetail(User user);

    void gotUserListMembers(PagableResponseList<User> pagableResponseList);

    void gotUserListMemberships(PagableResponseList<UserList> pagableResponseList);

    void gotUserListStatuses(ResponseList<Status> responseList);

    void gotUserListSubscribers(PagableResponseList<User> pagableResponseList);

    void gotUserListSubscriptions(PagableResponseList<UserList> pagableResponseList);

    void gotUserLists(PagableResponseList<UserList> pagableResponseList);

    void gotUserSuggestions(ResponseList<User> responseList);

    void gotUserTimeline(ResponseList<Status> responseList);

    void gotWeeklyTrends(List<Trends> list);

    void lookedUpFriendships(ResponseList<Friendship> responseList);

    void lookedupUsers(ResponseList<User> responseList);

    void onException(TwitterException twitterException, TwitterMethod twitterMethod);

    void reportedSpam(User user);

    void retweetedStatus(Status status);

    void searched(QueryResult queryResult);

    void searchedPlaces(ResponseList<Place> responseList);

    void searchedUser(ResponseList<User> responseList);

    void sentDirectMessage(DirectMessage directMessage);

    void subscribedUserList(UserList userList);

    void tested(boolean z);

    void unsubscribedUserList(UserList userList);

    void updatedFriendship(Relationship relationship);

    void updatedProfile(User user);

    void updatedProfileBackgroundImage(User user);

    void updatedProfileColors(User user);

    void updatedProfileImage(User user);

    void updatedStatus(Status status);

    void updatedUserList(UserList userList);

    void verifiedCredentials(User user);
}
