package twitter4j;

import java.io.Serializable;
import twitter4j.api.AccountMethods;
import twitter4j.api.BlockMethods;
import twitter4j.api.DirectMessageMethods;
import twitter4j.api.FavoriteMethods;
import twitter4j.api.FriendsFollowersMethods;
import twitter4j.api.FriendshipMethods;
import twitter4j.api.GeoMethods;
import twitter4j.api.HelpMethods;
import twitter4j.api.LegalResources;
import twitter4j.api.ListMembersMethods;
import twitter4j.api.ListMethods;
import twitter4j.api.ListSubscribersMethods;
import twitter4j.api.LocalTrendsMethods;
import twitter4j.api.NewTwitterMethods;
import twitter4j.api.NotificationMethods;
import twitter4j.api.SavedSearchesMethods;
import twitter4j.api.SearchMethods;
import twitter4j.api.SpamReportingMethods;
import twitter4j.api.StatusMethods;
import twitter4j.api.TimelineMethods;
import twitter4j.api.TrendsMethods;
import twitter4j.api.UserMethods;
import twitter4j.auth.OAuthSupport;

/* JADX INFO: loaded from: classes.dex */
public interface Twitter extends Serializable, OAuthSupport, TwitterBase, SearchMethods, TrendsMethods, TimelineMethods, StatusMethods, UserMethods, ListMethods, ListMembersMethods, ListSubscribersMethods, DirectMessageMethods, FriendshipMethods, FriendsFollowersMethods, AccountMethods, FavoriteMethods, NotificationMethods, BlockMethods, SpamReportingMethods, SavedSearchesMethods, LocalTrendsMethods, GeoMethods, LegalResources, NewTwitterMethods, HelpMethods {
}
