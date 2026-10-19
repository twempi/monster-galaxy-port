package twitter4j;

import java.util.List;
import twitter4j.api.HelpMethods;

/* JADX INFO: loaded from: classes.dex */
public class TwitterAdapter implements TwitterListener {
    @Override // twitter4j.TwitterListener
    public void searched(QueryResult result) {
    }

    @Override // twitter4j.TwitterListener
    public void gotTrends(Trends trends) {
    }

    @Override // twitter4j.TwitterListener
    public void gotCurrentTrends(Trends trends) {
    }

    @Override // twitter4j.TwitterListener
    public void gotDailyTrends(List<Trends> trendsList) {
    }

    @Override // twitter4j.TwitterListener
    public void gotWeeklyTrends(List<Trends> trendsList) {
    }

    @Override // twitter4j.TwitterListener
    public void gotPublicTimeline(ResponseList<Status> statuses) {
    }

    @Override // twitter4j.TwitterListener
    public void gotHomeTimeline(ResponseList<Status> statuses) {
    }

    @Override // twitter4j.TwitterListener
    public void gotFriendsTimeline(ResponseList<Status> statuses) {
    }

    @Override // twitter4j.TwitterListener
    public void gotUserTimeline(ResponseList<Status> statuses) {
    }

    @Override // twitter4j.TwitterListener
    public void gotMentions(ResponseList<Status> statuses) {
    }

    @Override // twitter4j.TwitterListener
    public void gotRetweetedByMe(ResponseList<Status> statuses) {
    }

    @Override // twitter4j.TwitterListener
    public void gotRetweetedToMe(ResponseList<Status> statuses) {
    }

    @Override // twitter4j.TwitterListener
    public void gotRetweetsOfMe(ResponseList<Status> statuses) {
    }

    @Override // twitter4j.TwitterListener
    public void gotRetweetedByUser(ResponseList<Status> statuses) {
    }

    @Override // twitter4j.TwitterListener
    public void gotRetweetedToUser(ResponseList<Status> statuses) {
    }

    @Override // twitter4j.TwitterListener
    public void gotShowStatus(Status statuses) {
    }

    @Override // twitter4j.TwitterListener
    public void updatedStatus(Status statuses) {
    }

    @Override // twitter4j.TwitterListener
    public void destroyedStatus(Status destroyedStatus) {
    }

    @Override // twitter4j.TwitterListener
    public void retweetedStatus(Status retweetedStatus) {
    }

    @Override // twitter4j.TwitterListener
    public void gotRetweets(ResponseList<Status> retweets) {
    }

    @Override // twitter4j.TwitterListener
    public void gotRetweetedBy(ResponseList<User> users) {
    }

    @Override // twitter4j.TwitterListener
    public void gotRetweetedByIDs(IDs ids) {
    }

    @Override // twitter4j.TwitterListener
    public void gotUserDetail(User user) {
    }

    @Override // twitter4j.TwitterListener
    public void lookedupUsers(ResponseList<User> users) {
    }

    @Override // twitter4j.TwitterListener
    public void searchedUser(ResponseList<User> userList) {
    }

    @Override // twitter4j.TwitterListener
    public void gotSuggestedUserCategories(ResponseList<Category> categories) {
    }

    @Override // twitter4j.TwitterListener
    public void gotUserSuggestions(ResponseList<User> users) {
    }

    @Override // twitter4j.TwitterListener
    public void gotMemberSuggestions(ResponseList<User> users) {
    }

    @Override // twitter4j.TwitterListener
    public void gotProfileImage(ProfileImage image) {
    }

    @Override // twitter4j.TwitterListener
    public void gotFriendsStatuses(PagableResponseList<User> users) {
    }

    @Override // twitter4j.TwitterListener
    public void gotFollowersStatuses(PagableResponseList<User> users) {
    }

    @Override // twitter4j.TwitterListener
    public void createdUserList(UserList userList) {
    }

    @Override // twitter4j.TwitterListener
    public void updatedUserList(UserList userList) {
    }

    @Override // twitter4j.TwitterListener
    public void gotUserLists(PagableResponseList<UserList> userLists) {
    }

    @Override // twitter4j.TwitterListener
    public void gotShowUserList(UserList userList) {
    }

    @Override // twitter4j.TwitterListener
    public void destroyedUserList(UserList userList) {
    }

    @Override // twitter4j.TwitterListener
    public void gotUserListStatuses(ResponseList<Status> statuses) {
    }

    @Override // twitter4j.TwitterListener
    public void gotUserListMemberships(PagableResponseList<UserList> userLists) {
    }

    @Override // twitter4j.TwitterListener
    public void gotUserListSubscriptions(PagableResponseList<UserList> userLists) {
    }

    @Override // twitter4j.TwitterListener
    public void gotAllUserLists(ResponseList<UserList> lists) {
    }

    @Override // twitter4j.TwitterListener
    public void gotUserListMembers(PagableResponseList<User> users) {
    }

    @Override // twitter4j.TwitterListener
    public void addedUserListMember(UserList userList) {
    }

    @Override // twitter4j.TwitterListener
    public void addedUserListMembers(UserList userList) {
    }

    @Override // twitter4j.TwitterListener
    public void deletedUserListMember(UserList userList) {
    }

    @Override // twitter4j.TwitterListener
    public void checkedUserListMembership(User user) {
    }

    @Override // twitter4j.TwitterListener
    public void gotUserListSubscribers(PagableResponseList<User> users) {
    }

    @Override // twitter4j.TwitterListener
    public void subscribedUserList(UserList userList) {
    }

    @Override // twitter4j.TwitterListener
    public void unsubscribedUserList(UserList userList) {
    }

    @Override // twitter4j.TwitterListener
    public void checkedUserListSubscription(User user) {
    }

    @Override // twitter4j.TwitterListener
    public void gotDirectMessages(ResponseList<DirectMessage> messages) {
    }

    @Override // twitter4j.TwitterListener
    public void gotSentDirectMessages(ResponseList<DirectMessage> messages) {
    }

    @Override // twitter4j.TwitterListener
    public void sentDirectMessage(DirectMessage message) {
    }

    @Override // twitter4j.TwitterListener
    public void destroyedDirectMessage(DirectMessage message) {
    }

    @Override // twitter4j.TwitterListener
    public void gotDirectMessage(DirectMessage message) {
    }

    @Override // twitter4j.TwitterListener
    public void createdFriendship(User user) {
    }

    @Override // twitter4j.TwitterListener
    public void destroyedFriendship(User user) {
    }

    @Override // twitter4j.TwitterListener
    public void gotExistsFriendship(boolean exists) {
    }

    @Override // twitter4j.TwitterListener
    public void gotShowFriendship(Relationship relationship) {
    }

    @Override // twitter4j.TwitterListener
    public void gotIncomingFriendships(IDs ids) {
    }

    @Override // twitter4j.TwitterListener
    public void gotOutgoingFriendships(IDs ids) {
    }

    @Override // twitter4j.TwitterListener
    public void lookedUpFriendships(ResponseList<Friendship> friendships) {
    }

    @Override // twitter4j.TwitterListener
    public void updatedFriendship(Relationship relationship) {
    }

    @Override // twitter4j.TwitterListener
    public void gotNoRetweetIds(IDs ids) {
    }

    @Override // twitter4j.TwitterListener
    public void gotFriendsIDs(IDs ids) {
    }

    @Override // twitter4j.TwitterListener
    public void gotFollowersIDs(IDs ids) {
    }

    @Override // twitter4j.TwitterListener
    public void verifiedCredentials(User user) {
    }

    @Override // twitter4j.TwitterListener
    public void gotRateLimitStatus(RateLimitStatus status) {
    }

    @Override // twitter4j.TwitterListener
    public void updatedProfileColors(User user) {
    }

    @Override // twitter4j.TwitterListener
    public void gotAccountTotals(AccountTotals totals) {
    }

    @Override // twitter4j.TwitterListener
    public void gotAccountSettings(AccountSettings settings) {
    }

    @Override // twitter4j.TwitterListener
    public void updatedProfileImage(User user) {
    }

    @Override // twitter4j.TwitterListener
    public void updatedProfileBackgroundImage(User user) {
    }

    @Override // twitter4j.TwitterListener
    public void updatedProfile(User user) {
    }

    @Override // twitter4j.TwitterListener
    public void gotFavorites(ResponseList<Status> statuses) {
    }

    @Override // twitter4j.TwitterListener
    public void createdFavorite(Status status) {
    }

    @Override // twitter4j.TwitterListener
    public void destroyedFavorite(Status status) {
    }

    @Override // twitter4j.TwitterListener
    public void enabledNotification(User user) {
    }

    @Override // twitter4j.TwitterListener
    public void disabledNotification(User user) {
    }

    @Override // twitter4j.TwitterListener
    public void createdBlock(User user) {
    }

    @Override // twitter4j.TwitterListener
    public void destroyedBlock(User user) {
    }

    @Override // twitter4j.TwitterListener
    public void gotExistsBlock(boolean blockExists) {
    }

    @Override // twitter4j.TwitterListener
    public void gotBlockingUsers(ResponseList<User> blockingUsers) {
    }

    @Override // twitter4j.TwitterListener
    public void gotBlockingUsersIDs(IDs blockingUsersIDs) {
    }

    @Override // twitter4j.TwitterListener
    public void reportedSpam(User reportedSpammer) {
    }

    @Override // twitter4j.TwitterListener
    public void gotAvailableTrends(ResponseList<Location> locations) {
    }

    @Override // twitter4j.TwitterListener
    public void gotLocationTrends(Trends trends) {
    }

    @Override // twitter4j.TwitterListener
    public void searchedPlaces(ResponseList<Place> places) {
    }

    @Override // twitter4j.TwitterListener
    public void gotSimilarPlaces(SimilarPlaces places) {
    }

    @Override // twitter4j.TwitterListener
    public void gotNearByPlaces(ResponseList<Place> places) {
    }

    @Override // twitter4j.TwitterListener
    public void gotReverseGeoCode(ResponseList<Place> places) {
    }

    @Override // twitter4j.TwitterListener
    public void gotGeoDetails(Place place) {
    }

    @Override // twitter4j.TwitterListener
    public void createdPlace(Place place) {
    }

    @Override // twitter4j.TwitterListener
    public void gotTermsOfService(String tof) {
    }

    @Override // twitter4j.TwitterListener
    public void gotPrivacyPolicy(String privacyPolicy) {
    }

    @Override // twitter4j.TwitterListener
    public void gotRelatedResults(RelatedResults relatedResults) {
    }

    @Override // twitter4j.TwitterListener
    public void tested(boolean test) {
    }

    @Override // twitter4j.TwitterListener
    public void gotAPIConfiguration(TwitterAPIConfiguration conf) {
    }

    @Override // twitter4j.TwitterListener
    public void gotLanguages(ResponseList<HelpMethods.Language> languages) {
    }

    @Override // twitter4j.TwitterListener
    public void onException(TwitterException ex, TwitterMethod method) {
    }
}
