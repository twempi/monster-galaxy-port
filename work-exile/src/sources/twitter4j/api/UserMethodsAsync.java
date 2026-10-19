package twitter4j.api;

import twitter4j.ProfileImage;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface UserMethodsAsync {
    void getFollowersStatuses(long j);

    void getFollowersStatuses(long j, long j2);

    void getFollowersStatuses(String str, long j);

    void getFriendsStatuses(long j);

    void getFriendsStatuses(long j, long j2);

    void getFriendsStatuses(String str, long j);

    void getMemberSuggestions(String str);

    void getProfileImage(String str, ProfileImage.ImageSize imageSize);

    void getSuggestedUserCategories();

    void getUserSuggestions(String str);

    void lookupUsers(long[] jArr);

    void lookupUsers(String[] strArr);

    void searchUsers(String str, int i);

    void showUser(long j);

    void showUser(String str);
}
