package twitter4j.api;

import twitter4j.Category;
import twitter4j.PagableResponseList;
import twitter4j.ProfileImage;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.User;

/* JADX INFO: loaded from: classes.dex */
public interface UserMethods {
    PagableResponseList<User> getFollowersStatuses(long j) throws TwitterException;

    PagableResponseList<User> getFollowersStatuses(long j, long j2) throws TwitterException;

    PagableResponseList<User> getFollowersStatuses(String str, long j) throws TwitterException;

    PagableResponseList<User> getFriendsStatuses(long j) throws TwitterException;

    PagableResponseList<User> getFriendsStatuses(long j, long j2) throws TwitterException;

    PagableResponseList<User> getFriendsStatuses(String str, long j) throws TwitterException;

    ResponseList<User> getMemberSuggestions(String str) throws TwitterException;

    ProfileImage getProfileImage(String str, ProfileImage.ImageSize imageSize) throws TwitterException;

    ResponseList<Category> getSuggestedUserCategories() throws TwitterException;

    ResponseList<User> getUserSuggestions(String str) throws TwitterException;

    ResponseList<User> lookupUsers(long[] jArr) throws TwitterException;

    ResponseList<User> lookupUsers(String[] strArr) throws TwitterException;

    ResponseList<User> searchUsers(String str, int i) throws TwitterException;

    User showUser(long j) throws TwitterException;

    User showUser(String str) throws TwitterException;
}
