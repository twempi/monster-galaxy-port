package twitter4j;

import java.io.Serializable;
import twitter4j.api.AccountMethodsAsync;
import twitter4j.api.BlockMethodsAsync;
import twitter4j.api.DirectMessageMethodsAsync;
import twitter4j.api.FavoriteMethodsAsync;
import twitter4j.api.FriendsFollowersMethodsAsync;
import twitter4j.api.FriendshipMethodsAsync;
import twitter4j.api.GeoMethodsAsync;
import twitter4j.api.HelpMethodsAsync;
import twitter4j.api.LegalResourcesAsync;
import twitter4j.api.ListMembersMethodsAsync;
import twitter4j.api.ListMethodsAsync;
import twitter4j.api.ListSubscribersMethodsAsync;
import twitter4j.api.LocalTrendsMethodsAsync;
import twitter4j.api.NewTwitterMethodsAsync;
import twitter4j.api.NotificationMethodsAsync;
import twitter4j.api.SavedSearchesMethodsAsync;
import twitter4j.api.SearchMethodsAsync;
import twitter4j.api.SpamReportingMethodsAsync;
import twitter4j.api.StatusMethodsAsync;
import twitter4j.api.TimelineMethodsAsync;
import twitter4j.api.TrendsMethodsAsync;
import twitter4j.api.UserMethodsAsync;
import twitter4j.auth.OAuthSupport;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface AsyncTwitter extends Serializable, OAuthSupport, TwitterBase, SearchMethodsAsync, TrendsMethodsAsync, TimelineMethodsAsync, StatusMethodsAsync, UserMethodsAsync, ListMethodsAsync, ListMembersMethodsAsync, ListSubscribersMethodsAsync, DirectMessageMethodsAsync, FriendshipMethodsAsync, FriendsFollowersMethodsAsync, AccountMethodsAsync, FavoriteMethodsAsync, NotificationMethodsAsync, BlockMethodsAsync, SpamReportingMethodsAsync, SavedSearchesMethodsAsync, LocalTrendsMethodsAsync, GeoMethodsAsync, LegalResourcesAsync, NewTwitterMethodsAsync, HelpMethodsAsync {
    void addListener(TwitterListener twitterListener);
}
