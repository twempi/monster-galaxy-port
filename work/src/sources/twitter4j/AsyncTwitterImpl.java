package twitter4j;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import twitter4j.api.HelpMethods;
import twitter4j.auth.AccessToken;
import twitter4j.auth.Authorization;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.internal.async.Dispatcher;
import twitter4j.internal.async.DispatcherFactory;

/* JADX INFO: loaded from: classes.dex */
class AsyncTwitterImpl extends TwitterBaseImpl implements AsyncTwitter {
    static Class class$twitter4j$AsyncTwitterImpl = null;
    private static transient Dispatcher dispatcher = null;
    private static final long serialVersionUID = -2008667933225051907L;
    private final List<TwitterListener> listeners;
    private final Twitter twitter;

    AsyncTwitterImpl(Configuration conf) {
        super(conf);
        this.listeners = new ArrayList();
        this.twitter = new TwitterFactory(conf).getInstance(this.auth);
    }

    AsyncTwitterImpl(Configuration conf, Authorization auth) {
        super(conf, auth);
        this.listeners = new ArrayList();
        this.twitter = new TwitterFactory(conf).getInstance(auth);
    }

    @Override // twitter4j.AsyncTwitter
    public void addListener(TwitterListener listener) {
        this.listeners.add(listener);
    }

    @Override // twitter4j.api.SearchMethodsAsync
    public void search(Query query) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.SEARCH, this.listeners, query) { // from class: twitter4j.AsyncTwitterImpl.1
            private final AsyncTwitterImpl this$0;
            private final Query val$query;

            {
                this.this$0 = this;
                this.val$query = query;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                QueryResult result = this.this$0.twitter.search(this.val$query);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.searched(result);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TrendsMethodsAsync
    public void getTrends() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.TRENDS, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.2
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Trends trends = this.this$0.twitter.getTrends();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotTrends(trends);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TrendsMethodsAsync
    public void getCurrentTrends() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.CURRENT_TRENDS, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.3
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Trends trends = this.this$0.twitter.getCurrentTrends();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotCurrentTrends(trends);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TrendsMethodsAsync
    public void getCurrentTrends(boolean excludeHashTags) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.CURRENT_TRENDS, this.listeners, excludeHashTags) { // from class: twitter4j.AsyncTwitterImpl.4
            private final AsyncTwitterImpl this$0;
            private final boolean val$excludeHashTags;

            {
                this.this$0 = this;
                this.val$excludeHashTags = excludeHashTags;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Trends trends = this.this$0.twitter.getCurrentTrends(this.val$excludeHashTags);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotCurrentTrends(trends);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TrendsMethodsAsync
    public void getDailyTrends() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.DAILY_TRENDS, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.5
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                List<Trends> trendsList = this.this$0.twitter.getDailyTrends();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotDailyTrends(trendsList);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TrendsMethodsAsync
    public void getDailyTrends(Date date, boolean excludeHashTags) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.DAILY_TRENDS, this.listeners, date, excludeHashTags) { // from class: twitter4j.AsyncTwitterImpl.6
            private final AsyncTwitterImpl this$0;
            private final Date val$date;
            private final boolean val$excludeHashTags;

            {
                this.this$0 = this;
                this.val$date = date;
                this.val$excludeHashTags = excludeHashTags;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                List<Trends> trendsList = this.this$0.twitter.getDailyTrends(this.val$date, this.val$excludeHashTags);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotDailyTrends(trendsList);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TrendsMethodsAsync
    public void getWeeklyTrends() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.WEEKLY_TRENDS, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.7
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                List<Trends> trendsList = this.this$0.twitter.getWeeklyTrends();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotWeeklyTrends(trendsList);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TrendsMethodsAsync
    public void getWeeklyTrends(Date date, boolean excludeHashTags) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.WEEKLY_TRENDS, this.listeners, date, excludeHashTags) { // from class: twitter4j.AsyncTwitterImpl.8
            private final AsyncTwitterImpl this$0;
            private final Date val$date;
            private final boolean val$excludeHashTags;

            {
                this.this$0 = this;
                this.val$date = date;
                this.val$excludeHashTags = excludeHashTags;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                List<Trends> trendsList = this.this$0.twitter.getWeeklyTrends(this.val$date, this.val$excludeHashTags);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotWeeklyTrends(trendsList);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TimelineMethodsAsync
    public void getPublicTimeline() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.PUBLIC_TIMELINE, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.9
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = this.this$0.twitter.getPublicTimeline();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotPublicTimeline(statuses);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TimelineMethodsAsync
    public void getHomeTimeline() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.HOME_TIMELINE, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.10
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = this.this$0.twitter.getHomeTimeline();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotHomeTimeline(statuses);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TimelineMethodsAsync
    public void getHomeTimeline(Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.HOME_TIMELINE, this.listeners, paging) { // from class: twitter4j.AsyncTwitterImpl.11
            private final AsyncTwitterImpl this$0;
            private final Paging val$paging;

            {
                this.this$0 = this;
                this.val$paging = paging;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = this.this$0.twitter.getHomeTimeline(this.val$paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotHomeTimeline(statuses);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TimelineMethodsAsync
    public void getFriendsTimeline() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.FRIENDS_TIMELINE, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.12
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = this.this$0.twitter.getFriendsTimeline();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFriendsTimeline(statuses);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TimelineMethodsAsync
    public void getFriendsTimeline(Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.FRIENDS_TIMELINE, this.listeners, paging) { // from class: twitter4j.AsyncTwitterImpl.13
            private final AsyncTwitterImpl this$0;
            private final Paging val$paging;

            {
                this.this$0 = this;
                this.val$paging = paging;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = this.this$0.twitter.getFriendsTimeline(this.val$paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFriendsTimeline(statuses);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TimelineMethodsAsync
    public void getUserTimeline(String screenName, Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.USER_TIMELINE, this.listeners, screenName, paging) { // from class: twitter4j.AsyncTwitterImpl.14
            private final AsyncTwitterImpl this$0;
            private final Paging val$paging;
            private final String val$screenName;

            {
                this.this$0 = this;
                this.val$screenName = screenName;
                this.val$paging = paging;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = this.this$0.twitter.getUserTimeline(this.val$screenName, this.val$paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserTimeline(statuses);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TimelineMethodsAsync
    public void getUserTimeline(long userId, Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.USER_TIMELINE, this.listeners, userId, paging) { // from class: twitter4j.AsyncTwitterImpl.15
            private final AsyncTwitterImpl this$0;
            private final Paging val$paging;
            private final long val$userId;

            {
                this.this$0 = this;
                this.val$userId = userId;
                this.val$paging = paging;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = this.this$0.twitter.getUserTimeline(this.val$userId, this.val$paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserTimeline(statuses);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TimelineMethodsAsync
    public void getUserTimeline(Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.USER_TIMELINE, this.listeners, paging) { // from class: twitter4j.AsyncTwitterImpl.16
            private final AsyncTwitterImpl this$0;
            private final Paging val$paging;

            {
                this.this$0 = this;
                this.val$paging = paging;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = this.this$0.twitter.getUserTimeline(this.val$paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserTimeline(statuses);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TimelineMethodsAsync
    public void getUserTimeline(String screenName) {
        getUserTimeline(screenName, new Paging());
    }

    @Override // twitter4j.api.TimelineMethodsAsync
    public void getUserTimeline(long userId) {
        getUserTimeline(userId, new Paging());
    }

    @Override // twitter4j.api.TimelineMethodsAsync
    public void getUserTimeline() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.USER_TIMELINE, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.17
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = this.this$0.twitter.getUserTimeline();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserTimeline(statuses);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TimelineMethodsAsync
    public void getMentions() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.MENTIONS, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.18
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = this.this$0.twitter.getMentions();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotMentions(statuses);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TimelineMethodsAsync
    public void getMentions(Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.MENTIONS, this.listeners, paging) { // from class: twitter4j.AsyncTwitterImpl.19
            private final AsyncTwitterImpl this$0;
            private final Paging val$paging;

            {
                this.this$0 = this;
                this.val$paging = paging;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = this.this$0.twitter.getMentions(this.val$paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotMentions(statuses);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TimelineMethodsAsync
    public void getRetweetedByMe() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.RETWEETED_BY_ME, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.20
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = this.this$0.twitter.getRetweetedByMe();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweetedByMe(statuses);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TimelineMethodsAsync
    public void getRetweetedByMe(Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.RETWEETED_BY_ME, this.listeners, paging) { // from class: twitter4j.AsyncTwitterImpl.21
            private final AsyncTwitterImpl this$0;
            private final Paging val$paging;

            {
                this.this$0 = this;
                this.val$paging = paging;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = this.this$0.twitter.getRetweetedByMe(this.val$paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweetedByMe(statuses);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TimelineMethodsAsync
    public void getRetweetedToMe() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.RETWEETED_TO_ME, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.22
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = this.this$0.twitter.getRetweetedToMe();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweetedToMe(statuses);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TimelineMethodsAsync
    public void getRetweetedToMe(Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.RETWEETED_TO_ME, this.listeners, paging) { // from class: twitter4j.AsyncTwitterImpl.23
            private final AsyncTwitterImpl this$0;
            private final Paging val$paging;

            {
                this.this$0 = this;
                this.val$paging = paging;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = this.this$0.twitter.getRetweetedToMe(this.val$paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweetedToMe(statuses);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TimelineMethodsAsync
    public void getRetweetsOfMe() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.RETWEETS_OF_ME, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.24
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = this.this$0.twitter.getRetweetsOfMe();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweetsOfMe(statuses);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TimelineMethodsAsync
    public void getRetweetsOfMe(Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.RETWEETS_OF_ME, this.listeners, paging) { // from class: twitter4j.AsyncTwitterImpl.25
            private final AsyncTwitterImpl this$0;
            private final Paging val$paging;

            {
                this.this$0 = this;
                this.val$paging = paging;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = this.this$0.twitter.getRetweetsOfMe(this.val$paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweetsOfMe(statuses);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TimelineMethodsAsync
    public void getRetweetedByUser(String screenName, Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.RETWEETED_BY_USER, this.listeners, screenName, paging) { // from class: twitter4j.AsyncTwitterImpl.26
            private final AsyncTwitterImpl this$0;
            private final Paging val$paging;
            private final String val$screenName;

            {
                this.this$0 = this;
                this.val$screenName = screenName;
                this.val$paging = paging;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = this.this$0.twitter.getRetweetedByUser(this.val$screenName, this.val$paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweetedByUser(statuses);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TimelineMethodsAsync
    public void getRetweetedByUser(long userId, Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.RETWEETED_BY_USER, this.listeners, userId, paging) { // from class: twitter4j.AsyncTwitterImpl.27
            private final AsyncTwitterImpl this$0;
            private final Paging val$paging;
            private final long val$userId;

            {
                this.this$0 = this;
                this.val$userId = userId;
                this.val$paging = paging;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = this.this$0.twitter.getRetweetedByUser(this.val$userId, this.val$paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweetedByUser(statuses);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TimelineMethodsAsync
    public void getRetweetedToUser(String screenName, Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.RETWEETED_TO_USER, this.listeners, screenName, paging) { // from class: twitter4j.AsyncTwitterImpl.28
            private final AsyncTwitterImpl this$0;
            private final Paging val$paging;
            private final String val$screenName;

            {
                this.this$0 = this;
                this.val$screenName = screenName;
                this.val$paging = paging;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = this.this$0.twitter.getRetweetedToUser(this.val$screenName, this.val$paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweetedToUser(statuses);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.TimelineMethodsAsync
    public void getRetweetedToUser(long userId, Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.RETWEETED_TO_USER, this.listeners, userId, paging) { // from class: twitter4j.AsyncTwitterImpl.29
            private final AsyncTwitterImpl this$0;
            private final Paging val$paging;
            private final long val$userId;

            {
                this.this$0 = this;
                this.val$userId = userId;
                this.val$paging = paging;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = this.this$0.twitter.getRetweetedToUser(this.val$userId, this.val$paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweetedToUser(statuses);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.StatusMethodsAsync
    public void showStatus(long id) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.SHOW_STATUS, this.listeners, id) { // from class: twitter4j.AsyncTwitterImpl.30
            private final AsyncTwitterImpl this$0;
            private final long val$id;

            {
                this.this$0 = this;
                this.val$id = id;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Status status = this.this$0.twitter.showStatus(this.val$id);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotShowStatus(status);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.StatusMethodsAsync
    public void updateStatus(String statusText) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.UPDATE_STATUS, this.listeners, statusText) { // from class: twitter4j.AsyncTwitterImpl.31
            private final AsyncTwitterImpl this$0;
            private final String val$statusText;

            {
                this.this$0 = this;
                this.val$statusText = statusText;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Status status = this.this$0.twitter.updateStatus(this.val$statusText);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedStatus(status);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.StatusMethodsAsync
    public void updateStatus(StatusUpdate latestStatus) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.UPDATE_STATUS, this.listeners, latestStatus) { // from class: twitter4j.AsyncTwitterImpl.32
            private final AsyncTwitterImpl this$0;
            private final StatusUpdate val$latestStatus;

            {
                this.this$0 = this;
                this.val$latestStatus = latestStatus;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Status status = this.this$0.twitter.updateStatus(this.val$latestStatus);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedStatus(status);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.StatusMethodsAsync
    public void destroyStatus(long statusId) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.DESTROY_STATUS, this.listeners, statusId) { // from class: twitter4j.AsyncTwitterImpl.33
            private final AsyncTwitterImpl this$0;
            private final long val$statusId;

            {
                this.this$0 = this;
                this.val$statusId = statusId;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Status status = this.this$0.twitter.destroyStatus(this.val$statusId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedStatus(status);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.StatusMethodsAsync
    public void retweetStatus(long statusId) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.RETWEET_STATUS, this.listeners, statusId) { // from class: twitter4j.AsyncTwitterImpl.34
            private final AsyncTwitterImpl this$0;
            private final long val$statusId;

            {
                this.this$0 = this;
                this.val$statusId = statusId;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Status status = this.this$0.twitter.retweetStatus(this.val$statusId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.retweetedStatus(status);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.StatusMethodsAsync
    public void getRetweets(long statusId) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.RETWEETS, this.listeners, statusId) { // from class: twitter4j.AsyncTwitterImpl.35
            private final AsyncTwitterImpl this$0;
            private final long val$statusId;

            {
                this.this$0 = this;
                this.val$statusId = statusId;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = this.this$0.twitter.getRetweets(this.val$statusId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweets(statuses);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.StatusMethodsAsync
    public void getRetweetedBy(long statusId) {
        getRetweetedBy(statusId, new Paging(1, 100));
    }

    @Override // twitter4j.api.StatusMethodsAsync
    public void getRetweetedBy(long statusId, Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.RETWEETED_BY, this.listeners, statusId, paging) { // from class: twitter4j.AsyncTwitterImpl.36
            private final AsyncTwitterImpl this$0;
            private final Paging val$paging;
            private final long val$statusId;

            {
                this.this$0 = this;
                this.val$statusId = statusId;
                this.val$paging = paging;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = this.this$0.twitter.getRetweetedBy(this.val$statusId, this.val$paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweetedBy(users);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.StatusMethodsAsync
    public void getRetweetedByIDs(long statusId) {
        getRetweetedByIDs(statusId, new Paging(1, 100));
    }

    @Override // twitter4j.api.StatusMethodsAsync
    public void getRetweetedByIDs(long statusId, Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.RETWEETED_BY_IDS, this.listeners, statusId, paging) { // from class: twitter4j.AsyncTwitterImpl.37
            private final AsyncTwitterImpl this$0;
            private final Paging val$paging;
            private final long val$statusId;

            {
                this.this$0 = this;
                this.val$statusId = statusId;
                this.val$paging = paging;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = this.this$0.twitter.getRetweetedByIDs(this.val$statusId, this.val$paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRetweetedByIDs(ids);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.UserMethodsAsync
    public void showUser(String screenName) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.SHOW_USER, this.listeners, screenName) { // from class: twitter4j.AsyncTwitterImpl.38
            private final AsyncTwitterImpl this$0;
            private final String val$screenName;

            {
                this.this$0 = this;
                this.val$screenName = screenName;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = this.this$0.twitter.showUser(this.val$screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserDetail(user);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.UserMethodsAsync
    public void showUser(long userId) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.SHOW_USER, this.listeners, userId) { // from class: twitter4j.AsyncTwitterImpl.39
            private final AsyncTwitterImpl this$0;
            private final long val$userId;

            {
                this.this$0 = this;
                this.val$userId = userId;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = this.this$0.twitter.showUser(this.val$userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserDetail(user);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.UserMethodsAsync
    public void lookupUsers(String[] screenNames) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.LOOKUP_USERS, this.listeners, screenNames) { // from class: twitter4j.AsyncTwitterImpl.40
            private final AsyncTwitterImpl this$0;
            private final String[] val$screenNames;

            {
                this.this$0 = this;
                this.val$screenNames = screenNames;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = this.this$0.twitter.lookupUsers(this.val$screenNames);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.lookedupUsers(users);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.UserMethodsAsync
    public void lookupUsers(long[] ids) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.LOOKUP_USERS, this.listeners, ids) { // from class: twitter4j.AsyncTwitterImpl.41
            private final AsyncTwitterImpl this$0;
            private final long[] val$ids;

            {
                this.this$0 = this;
                this.val$ids = ids;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = this.this$0.twitter.lookupUsers(this.val$ids);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.lookedupUsers(users);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.UserMethodsAsync
    public void searchUsers(String query, int page) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.SEARCH_USERS, this.listeners, query, page) { // from class: twitter4j.AsyncTwitterImpl.42
            private final AsyncTwitterImpl this$0;
            private final int val$page;
            private final String val$query;

            {
                this.this$0 = this;
                this.val$query = query;
                this.val$page = page;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = this.this$0.twitter.searchUsers(this.val$query, this.val$page);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.searchedUser(users);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.UserMethodsAsync
    public void getSuggestedUserCategories() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.SUGGESTED_USER_CATEGORIES, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.43
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Category> categories = this.this$0.twitter.getSuggestedUserCategories();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotSuggestedUserCategories(categories);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.UserMethodsAsync
    public void getUserSuggestions(String categorySlug) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.USER_SUGGESTIONS, this.listeners, categorySlug) { // from class: twitter4j.AsyncTwitterImpl.44
            private final AsyncTwitterImpl this$0;
            private final String val$categorySlug;

            {
                this.this$0 = this;
                this.val$categorySlug = categorySlug;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = this.this$0.twitter.getUserSuggestions(this.val$categorySlug);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserSuggestions(users);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.UserMethodsAsync
    public void getMemberSuggestions(String categorySlug) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.MEMBER_SUGGESTIONS, this.listeners, categorySlug) { // from class: twitter4j.AsyncTwitterImpl.45
            private final AsyncTwitterImpl this$0;
            private final String val$categorySlug;

            {
                this.this$0 = this;
                this.val$categorySlug = categorySlug;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = this.this$0.twitter.getMemberSuggestions(this.val$categorySlug);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotMemberSuggestions(users);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.UserMethodsAsync
    public void getProfileImage(String screenName, ProfileImage.ImageSize size) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.PROFILE_IMAGE, this.listeners, screenName, size) { // from class: twitter4j.AsyncTwitterImpl.46
            private final AsyncTwitterImpl this$0;
            private final String val$screenName;
            private final ProfileImage.ImageSize val$size;

            {
                this.this$0 = this;
                this.val$screenName = screenName;
                this.val$size = size;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ProfileImage profileImage = this.this$0.twitter.getProfileImage(this.val$screenName, this.val$size);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotProfileImage(profileImage);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.AccountMethodsAsync
    public void getAccountTotals() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.ACCOUNT_TOTALS, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.47
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                AccountTotals accountTotals = this.this$0.twitter.getAccountTotals();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotAccountTotals(accountTotals);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.AccountMethodsAsync
    public void getAccountSettings() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.ACCOUNT_SETTINGS, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.48
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                AccountSettings accountSettings = this.this$0.twitter.getAccountSettings();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotAccountSettings(accountSettings);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.UserMethodsAsync
    public void getFriendsStatuses(long cursor) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.FRIENDS_STATUSES, this.listeners, cursor) { // from class: twitter4j.AsyncTwitterImpl.49
            private final AsyncTwitterImpl this$0;
            private final long val$cursor;

            {
                this.this$0 = this;
                this.val$cursor = cursor;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<User> users = this.this$0.twitter.getFriendsStatuses(this.val$cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFriendsStatuses(users);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.UserMethodsAsync
    public void getFriendsStatuses(String screenName, long cursor) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.FRIENDS_STATUSES, this.listeners, screenName, cursor) { // from class: twitter4j.AsyncTwitterImpl.50
            private final AsyncTwitterImpl this$0;
            private final long val$cursor;
            private final String val$screenName;

            {
                this.this$0 = this;
                this.val$screenName = screenName;
                this.val$cursor = cursor;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<User> users = this.this$0.twitter.getFriendsStatuses(this.val$screenName, this.val$cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFriendsStatuses(users);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.UserMethodsAsync
    public void getFriendsStatuses(long userId, long cursor) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.FRIENDS_STATUSES, this.listeners, userId, cursor) { // from class: twitter4j.AsyncTwitterImpl.51
            private final AsyncTwitterImpl this$0;
            private final long val$cursor;
            private final long val$userId;

            {
                this.this$0 = this;
                this.val$userId = userId;
                this.val$cursor = cursor;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<User> users = this.this$0.twitter.getFriendsStatuses(this.val$userId, this.val$cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFriendsStatuses(users);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.UserMethodsAsync
    public void getFollowersStatuses(long cursor) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.FOLLOWERS_STATUSES, this.listeners, cursor) { // from class: twitter4j.AsyncTwitterImpl.52
            private final AsyncTwitterImpl this$0;
            private final long val$cursor;

            {
                this.this$0 = this;
                this.val$cursor = cursor;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<User> users = this.this$0.twitter.getFollowersStatuses(this.val$cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFollowersStatuses(users);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.UserMethodsAsync
    public void getFollowersStatuses(String screenName, long cursor) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.FOLLOWERS_STATUSES, this.listeners, screenName, cursor) { // from class: twitter4j.AsyncTwitterImpl.53
            private final AsyncTwitterImpl this$0;
            private final long val$cursor;
            private final String val$screenName;

            {
                this.this$0 = this;
                this.val$screenName = screenName;
                this.val$cursor = cursor;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<User> users = this.this$0.twitter.getFollowersStatuses(this.val$screenName, this.val$cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFollowersStatuses(users);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.UserMethodsAsync
    public void getFollowersStatuses(long userId, long cursor) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.FOLLOWERS_STATUSES, this.listeners, userId, cursor) { // from class: twitter4j.AsyncTwitterImpl.54
            private final AsyncTwitterImpl this$0;
            private final long val$cursor;
            private final long val$userId;

            {
                this.this$0 = this;
                this.val$userId = userId;
                this.val$cursor = cursor;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<User> users = this.this$0.twitter.getFollowersStatuses(this.val$userId, this.val$cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFollowersStatuses(users);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.ListMethodsAsync
    public void createUserList(String listName, boolean isPublicList, String description) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.CREATE_USER_LIST, this.listeners, listName, isPublicList, description) { // from class: twitter4j.AsyncTwitterImpl.55
            private final AsyncTwitterImpl this$0;
            private final String val$description;
            private final boolean val$isPublicList;
            private final String val$listName;

            {
                this.this$0 = this;
                this.val$listName = listName;
                this.val$isPublicList = isPublicList;
                this.val$description = description;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = this.this$0.twitter.createUserList(this.val$listName, this.val$isPublicList, this.val$description);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdUserList(list);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.ListMethodsAsync
    public void updateUserList(int listId, String newListName, boolean isPublicList, String newDescription) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.UPDATE_USER_LIST, this.listeners, listId, newListName, isPublicList, newDescription) { // from class: twitter4j.AsyncTwitterImpl.56
            private final AsyncTwitterImpl this$0;
            private final boolean val$isPublicList;
            private final int val$listId;
            private final String val$newDescription;
            private final String val$newListName;

            {
                this.this$0 = this;
                this.val$listId = listId;
                this.val$newListName = newListName;
                this.val$isPublicList = isPublicList;
                this.val$newDescription = newDescription;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = this.this$0.twitter.updateUserList(this.val$listId, this.val$newListName, this.val$isPublicList, this.val$newDescription);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedUserList(list);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.ListMethodsAsync
    public void getUserLists(String listOwnerScreenName, long cursor) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.USER_LISTS, this.listeners, listOwnerScreenName, cursor) { // from class: twitter4j.AsyncTwitterImpl.57
            private final AsyncTwitterImpl this$0;
            private final long val$cursor;
            private final String val$listOwnerScreenName;

            {
                this.this$0 = this;
                this.val$listOwnerScreenName = listOwnerScreenName;
                this.val$cursor = cursor;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<UserList> lists = this.this$0.twitter.getUserLists(this.val$listOwnerScreenName, this.val$cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserLists(lists);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.ListMethodsAsync
    public void getUserLists(long listOwnerUserId, long cursor) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.USER_LISTS, this.listeners, listOwnerUserId, cursor) { // from class: twitter4j.AsyncTwitterImpl.58
            private final AsyncTwitterImpl this$0;
            private final long val$cursor;
            private final long val$listOwnerUserId;

            {
                this.this$0 = this;
                this.val$listOwnerUserId = listOwnerUserId;
                this.val$cursor = cursor;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<UserList> lists = this.this$0.twitter.getUserLists(this.val$listOwnerUserId, this.val$cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserLists(lists);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.ListMethodsAsync
    public void showUserList(String listOwnerScreenName, int listId) {
        showUserList(listId);
    }

    @Override // twitter4j.api.ListMethodsAsync
    public void showUserList(int listId) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.UPDATE_USER_LIST, this.listeners, listId) { // from class: twitter4j.AsyncTwitterImpl.59
            private final AsyncTwitterImpl this$0;
            private final int val$listId;

            {
                this.this$0 = this;
                this.val$listId = listId;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = this.this$0.twitter.showUserList(this.val$listId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotShowUserList(list);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.ListMethodsAsync
    public void destroyUserList(int listId) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.DESTROY_USER_LIST, this.listeners, listId) { // from class: twitter4j.AsyncTwitterImpl.60
            private final AsyncTwitterImpl this$0;
            private final int val$listId;

            {
                this.this$0 = this;
                this.val$listId = listId;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = this.this$0.twitter.destroyUserList(this.val$listId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedUserList(list);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.ListMethodsAsync
    public void getUserListStatuses(String listOwnerScreenName, int id, Paging paging) {
        getUserListStatuses(id, paging);
    }

    @Override // twitter4j.api.ListMethodsAsync
    public void getUserListStatuses(long listOwnerId, int id, Paging paging) {
        getUserListStatuses(id, paging);
    }

    @Override // twitter4j.api.ListMethodsAsync
    public void getUserListStatuses(int listId, Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.USER_LIST_STATUSES, this.listeners, listId, paging) { // from class: twitter4j.AsyncTwitterImpl.61
            private final AsyncTwitterImpl this$0;
            private final int val$listId;
            private final Paging val$paging;

            {
                this.this$0 = this;
                this.val$listId = listId;
                this.val$paging = paging;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = this.this$0.twitter.getUserListStatuses(this.val$listId, this.val$paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserListStatuses(statuses);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.ListMethodsAsync
    public void getUserListMemberships(String listMemberScreenName, long cursor) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.USER_LIST_MEMBERSHIPS, this.listeners, listMemberScreenName, cursor) { // from class: twitter4j.AsyncTwitterImpl.62
            private final AsyncTwitterImpl this$0;
            private final long val$cursor;
            private final String val$listMemberScreenName;

            {
                this.this$0 = this;
                this.val$listMemberScreenName = listMemberScreenName;
                this.val$cursor = cursor;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<UserList> lists = this.this$0.twitter.getUserListMemberships(this.val$listMemberScreenName, this.val$cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserListMemberships(lists);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.ListMethodsAsync
    public void getUserListSubscriptions(String listOwnerScreenName, long cursor) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.USER_LIST_SUBSCRIPTIONS, this.listeners, listOwnerScreenName, cursor) { // from class: twitter4j.AsyncTwitterImpl.63
            private final AsyncTwitterImpl this$0;
            private final long val$cursor;
            private final String val$listOwnerScreenName;

            {
                this.this$0 = this;
                this.val$listOwnerScreenName = listOwnerScreenName;
                this.val$cursor = cursor;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<UserList> lists = this.this$0.twitter.getUserListSubscriptions(this.val$listOwnerScreenName, this.val$cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserListSubscriptions(lists);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.ListMethodsAsync
    public void getAllSubscribingUserLists(String screenName) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.ALL_USER_LISTS, this.listeners, screenName) { // from class: twitter4j.AsyncTwitterImpl.64
            private final AsyncTwitterImpl this$0;
            private final String val$screenName;

            {
                this.this$0 = this;
                this.val$screenName = screenName;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<UserList> lists = this.this$0.twitter.getAllUserLists(this.val$screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotAllUserLists(lists);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.ListMethodsAsync
    public void getAllSubscribingUserLists(long userId) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.ALL_USER_LISTS, this.listeners, userId) { // from class: twitter4j.AsyncTwitterImpl.65
            private final AsyncTwitterImpl this$0;
            private final long val$userId;

            {
                this.this$0 = this;
                this.val$userId = userId;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<UserList> lists = this.this$0.twitter.getAllUserLists(this.val$userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotAllUserLists(lists);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.ListMembersMethodsAsync
    public void getUserListMembers(String listOwnerScreenName, int listId, long cursor) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.LIST_MEMBERS, this.listeners, listOwnerScreenName, listId, cursor) { // from class: twitter4j.AsyncTwitterImpl.66
            private final AsyncTwitterImpl this$0;
            private final long val$cursor;
            private final int val$listId;
            private final String val$listOwnerScreenName;

            {
                this.this$0 = this;
                this.val$listOwnerScreenName = listOwnerScreenName;
                this.val$listId = listId;
                this.val$cursor = cursor;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<User> users = this.this$0.twitter.getUserListMembers(this.val$listOwnerScreenName, this.val$listId, this.val$cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserListMembers(users);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.ListMembersMethodsAsync
    public void getUserListMembers(long listOwnerId, int listId, long cursor) {
        getUserListMembers(listId, cursor);
    }

    @Override // twitter4j.api.ListMembersMethodsAsync
    public void getUserListMembers(int listId, long cursor) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.LIST_MEMBERS, this.listeners, listId, cursor) { // from class: twitter4j.AsyncTwitterImpl.67
            private final AsyncTwitterImpl this$0;
            private final long val$cursor;
            private final int val$listId;

            {
                this.this$0 = this;
                this.val$listId = listId;
                this.val$cursor = cursor;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<User> users = this.this$0.twitter.getUserListMembers(this.val$listId, this.val$cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserListMembers(users);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.ListMembersMethodsAsync
    public void addUserListMember(int listId, long userId) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.ADD_LIST_MEMBER, this.listeners, listId, userId) { // from class: twitter4j.AsyncTwitterImpl.68
            private final AsyncTwitterImpl this$0;
            private final int val$listId;
            private final long val$userId;

            {
                this.this$0 = this;
                this.val$listId = listId;
                this.val$userId = userId;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = this.this$0.twitter.addUserListMember(this.val$listId, this.val$userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.addedUserListMember(list);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.ListMembersMethodsAsync
    public void addUserListMembers(int listId, long[] userIds) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.ADD_LIST_MEMBERS, this.listeners, listId, userIds) { // from class: twitter4j.AsyncTwitterImpl.69
            private final AsyncTwitterImpl this$0;
            private final int val$listId;
            private final long[] val$userIds;

            {
                this.this$0 = this;
                this.val$listId = listId;
                this.val$userIds = userIds;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = this.this$0.twitter.addUserListMembers(this.val$listId, this.val$userIds);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.addedUserListMembers(list);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.ListMembersMethodsAsync
    public void addUserListMembers(int listId, String[] screenNames) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.ADD_LIST_MEMBERS, this.listeners, listId, screenNames) { // from class: twitter4j.AsyncTwitterImpl.70
            private final AsyncTwitterImpl this$0;
            private final int val$listId;
            private final String[] val$screenNames;

            {
                this.this$0 = this;
                this.val$listId = listId;
                this.val$screenNames = screenNames;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = this.this$0.twitter.addUserListMembers(this.val$listId, this.val$screenNames);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.addedUserListMembers(list);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.ListMembersMethodsAsync
    public void deleteUserListMember(int listId, long userId) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.DELETE_LIST_MEMBER, this.listeners, listId, userId) { // from class: twitter4j.AsyncTwitterImpl.71
            private final AsyncTwitterImpl this$0;
            private final int val$listId;
            private final long val$userId;

            {
                this.this$0 = this;
                this.val$listId = listId;
                this.val$userId = userId;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = this.this$0.twitter.deleteUserListMember(this.val$listId, this.val$userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.deletedUserListMember(list);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.ListMembersMethodsAsync
    public void checkUserListMembership(String listOwnerScreenName, int listId, long userId) {
        showUserListMembership(listId, userId);
    }

    @Override // twitter4j.api.ListMembersMethodsAsync
    public void showUserListMembership(int listId, long userId) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.CHECK_LIST_MEMBERSHIP, this.listeners, listId, userId) { // from class: twitter4j.AsyncTwitterImpl.72
            private final AsyncTwitterImpl this$0;
            private final int val$listId;
            private final long val$userId;

            {
                this.this$0 = this;
                this.val$listId = listId;
                this.val$userId = userId;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = this.this$0.twitter.showUserListMembership(this.val$listId, this.val$userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.checkedUserListMembership(user);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.ListSubscribersMethodsAsync
    public void getUserListSubscribers(String listOwnerScreenName, int listId, long cursor) {
        getUserListSubscribers(listId, cursor);
    }

    @Override // twitter4j.api.ListSubscribersMethodsAsync
    public void getUserListSubscribers(int listId, long cursor) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.LIST_SUBSCRIBERS, this.listeners, listId, cursor) { // from class: twitter4j.AsyncTwitterImpl.73
            private final AsyncTwitterImpl this$0;
            private final long val$cursor;
            private final int val$listId;

            {
                this.this$0 = this;
                this.val$listId = listId;
                this.val$cursor = cursor;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                PagableResponseList<User> users = this.this$0.twitter.getUserListSubscribers(this.val$listId, this.val$cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotUserListSubscribers(users);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.ListSubscribersMethodsAsync
    public void subscribeUserList(String listOwnerScreenName, int listId) {
        createUserListSubscription(listId);
    }

    @Override // twitter4j.api.ListSubscribersMethodsAsync
    public void createUserListSubscription(int listId) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.SUBSCRIBE_LIST, this.listeners, listId) { // from class: twitter4j.AsyncTwitterImpl.74
            private final AsyncTwitterImpl this$0;
            private final int val$listId;

            {
                this.this$0 = this;
                this.val$listId = listId;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = this.this$0.twitter.createUserListSubscription(this.val$listId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.subscribedUserList(list);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.ListSubscribersMethodsAsync
    public void unsubscribeUserList(String listOwnerScreenName, int listId) {
        destroyUserListSubscription(listId);
    }

    @Override // twitter4j.api.ListSubscribersMethodsAsync
    public void destroyUserListSubscription(int listId) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.UNSUBSCRIBE_LIST, this.listeners, listId) { // from class: twitter4j.AsyncTwitterImpl.75
            private final AsyncTwitterImpl this$0;
            private final int val$listId;

            {
                this.this$0 = this;
                this.val$listId = listId;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                UserList list = this.this$0.twitter.destroyUserListSubscription(this.val$listId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.unsubscribedUserList(list);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.ListSubscribersMethodsAsync
    public void checkUserListSubscription(String listOwnerScreenName, int listId, long userId) {
        showUserListSubscription(listId, userId);
    }

    @Override // twitter4j.api.ListSubscribersMethodsAsync
    public void showUserListSubscription(int listId, long userId) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.CHECK_LIST_SUBSCRIPTION, this.listeners, listId, userId) { // from class: twitter4j.AsyncTwitterImpl.76
            private final AsyncTwitterImpl this$0;
            private final int val$listId;
            private final long val$userId;

            {
                this.this$0 = this;
                this.val$listId = listId;
                this.val$userId = userId;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = this.this$0.twitter.showUserListSubscription(this.val$listId, this.val$userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.checkedUserListSubscription(user);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.DirectMessageMethodsAsync
    public void getDirectMessages() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.DIRECT_MESSAGES, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.77
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<DirectMessage> directMessages = this.this$0.twitter.getDirectMessages();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotDirectMessages(directMessages);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.DirectMessageMethodsAsync
    public void getDirectMessages(Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.DIRECT_MESSAGES, this.listeners, paging) { // from class: twitter4j.AsyncTwitterImpl.78
            private final AsyncTwitterImpl this$0;
            private final Paging val$paging;

            {
                this.this$0 = this;
                this.val$paging = paging;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<DirectMessage> directMessages = this.this$0.twitter.getDirectMessages(this.val$paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotDirectMessages(directMessages);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.DirectMessageMethodsAsync
    public void getSentDirectMessages() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.SENT_DIRECT_MESSAGES, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.79
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<DirectMessage> directMessages = this.this$0.twitter.getSentDirectMessages();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotSentDirectMessages(directMessages);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.DirectMessageMethodsAsync
    public void getSentDirectMessages(Paging paging) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.SENT_DIRECT_MESSAGES, this.listeners, paging) { // from class: twitter4j.AsyncTwitterImpl.80
            private final AsyncTwitterImpl this$0;
            private final Paging val$paging;

            {
                this.this$0 = this;
                this.val$paging = paging;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<DirectMessage> directMessages = this.this$0.twitter.getSentDirectMessages(this.val$paging);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotSentDirectMessages(directMessages);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.DirectMessageMethodsAsync
    public void sendDirectMessage(String screenName, String text) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.SEND_DIRECT_MESSAGE, this.listeners, screenName, text) { // from class: twitter4j.AsyncTwitterImpl.81
            private final AsyncTwitterImpl this$0;
            private final String val$screenName;
            private final String val$text;

            {
                this.this$0 = this;
                this.val$screenName = screenName;
                this.val$text = text;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                DirectMessage directMessage = this.this$0.twitter.sendDirectMessage(this.val$screenName, this.val$text);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.sentDirectMessage(directMessage);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.DirectMessageMethodsAsync
    public void sendDirectMessage(long userId, String text) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.SEND_DIRECT_MESSAGE, this.listeners, userId, text) { // from class: twitter4j.AsyncTwitterImpl.82
            private final AsyncTwitterImpl this$0;
            private final String val$text;
            private final long val$userId;

            {
                this.this$0 = this;
                this.val$userId = userId;
                this.val$text = text;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                DirectMessage directMessage = this.this$0.twitter.sendDirectMessage(this.val$userId, this.val$text);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.sentDirectMessage(directMessage);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.DirectMessageMethodsAsync
    public void destroyDirectMessage(long id) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.DESTROY_DIRECT_MESSAGE, this.listeners, id) { // from class: twitter4j.AsyncTwitterImpl.83
            private final AsyncTwitterImpl this$0;
            private final long val$id;

            {
                this.this$0 = this;
                this.val$id = id;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                DirectMessage directMessage = this.this$0.twitter.destroyDirectMessage(this.val$id);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedDirectMessage(directMessage);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.DirectMessageMethodsAsync
    public void showDirectMessage(long id) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.DIRECT_MESSAGE, this.listeners, id) { // from class: twitter4j.AsyncTwitterImpl.84
            private final AsyncTwitterImpl this$0;
            private final long val$id;

            {
                this.this$0 = this;
                this.val$id = id;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                DirectMessage directMessage = this.this$0.twitter.showDirectMessage(this.val$id);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotDirectMessage(directMessage);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FriendshipMethodsAsync
    public void createFriendship(String screenName) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.CREATE_FRIENDSHIP, this.listeners, screenName) { // from class: twitter4j.AsyncTwitterImpl.85
            private final AsyncTwitterImpl this$0;
            private final String val$screenName;

            {
                this.this$0 = this;
                this.val$screenName = screenName;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = this.this$0.twitter.createFriendship(this.val$screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdFriendship(user);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FriendshipMethodsAsync
    public void createFriendship(long userId) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.CREATE_FRIENDSHIP, this.listeners, userId) { // from class: twitter4j.AsyncTwitterImpl.86
            private final AsyncTwitterImpl this$0;
            private final long val$userId;

            {
                this.this$0 = this;
                this.val$userId = userId;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = this.this$0.twitter.createFriendship(this.val$userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdFriendship(user);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FriendshipMethodsAsync
    public void createFriendship(String screenName, boolean follow) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.CREATE_FRIENDSHIP, this.listeners, screenName, follow) { // from class: twitter4j.AsyncTwitterImpl.87
            private final AsyncTwitterImpl this$0;
            private final boolean val$follow;
            private final String val$screenName;

            {
                this.this$0 = this;
                this.val$screenName = screenName;
                this.val$follow = follow;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = this.this$0.twitter.createFriendship(this.val$screenName, this.val$follow);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdFriendship(user);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FriendshipMethodsAsync
    public void createFriendship(long userId, boolean follow) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.CREATE_FRIENDSHIP, this.listeners, userId, follow) { // from class: twitter4j.AsyncTwitterImpl.88
            private final AsyncTwitterImpl this$0;
            private final boolean val$follow;
            private final long val$userId;

            {
                this.this$0 = this;
                this.val$userId = userId;
                this.val$follow = follow;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = this.this$0.twitter.createFriendship(this.val$userId, this.val$follow);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdFriendship(user);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FriendshipMethodsAsync
    public void destroyFriendship(String screenName) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.DESTROY_FRIENDSHIP, this.listeners, screenName) { // from class: twitter4j.AsyncTwitterImpl.89
            private final AsyncTwitterImpl this$0;
            private final String val$screenName;

            {
                this.this$0 = this;
                this.val$screenName = screenName;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = this.this$0.twitter.destroyFriendship(this.val$screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedFriendship(user);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FriendshipMethodsAsync
    public void destroyFriendship(long userId) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.DESTROY_FRIENDSHIP, this.listeners, userId) { // from class: twitter4j.AsyncTwitterImpl.90
            private final AsyncTwitterImpl this$0;
            private final long val$userId;

            {
                this.this$0 = this;
                this.val$userId = userId;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = this.this$0.twitter.destroyFriendship(this.val$userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedFriendship(user);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FriendshipMethodsAsync
    public void existsFriendship(String userA, String userB) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.EXISTS_FRIENDSHIP, this.listeners, userA, userB) { // from class: twitter4j.AsyncTwitterImpl.91
            private final AsyncTwitterImpl this$0;
            private final String val$userA;
            private final String val$userB;

            {
                this.this$0 = this;
                this.val$userA = userA;
                this.val$userB = userB;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                for (TwitterListener listener : listeners) {
                    boolean exists = this.this$0.twitter.existsFriendship(this.val$userA, this.val$userB);
                    try {
                        listener.gotExistsFriendship(exists);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FriendshipMethodsAsync
    public void showFriendship(String sourceScreenName, String targetScreenName) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.SHOW_FRIENDSHIP, this.listeners, sourceScreenName, targetScreenName) { // from class: twitter4j.AsyncTwitterImpl.92
            private final AsyncTwitterImpl this$0;
            private final String val$sourceScreenName;
            private final String val$targetScreenName;

            {
                this.this$0 = this;
                this.val$sourceScreenName = sourceScreenName;
                this.val$targetScreenName = targetScreenName;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Relationship relationship = this.this$0.twitter.showFriendship(this.val$sourceScreenName, this.val$targetScreenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotShowFriendship(relationship);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FriendshipMethodsAsync
    public void showFriendship(long sourceId, long targetId) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.SHOW_FRIENDSHIP, this.listeners, sourceId, targetId) { // from class: twitter4j.AsyncTwitterImpl.93
            private final AsyncTwitterImpl this$0;
            private final long val$sourceId;
            private final long val$targetId;

            {
                this.this$0 = this;
                this.val$sourceId = sourceId;
                this.val$targetId = targetId;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Relationship relationship = this.this$0.twitter.showFriendship(this.val$sourceId, this.val$targetId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotShowFriendship(relationship);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FriendshipMethodsAsync
    public void getIncomingFriendships(long cursor) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.INCOMING_FRIENDSHIPS, this.listeners, cursor) { // from class: twitter4j.AsyncTwitterImpl.94
            private final AsyncTwitterImpl this$0;
            private final long val$cursor;

            {
                this.this$0 = this;
                this.val$cursor = cursor;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = this.this$0.twitter.getIncomingFriendships(this.val$cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotIncomingFriendships(ids);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FriendshipMethodsAsync
    public void getOutgoingFriendships(long cursor) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.OUTGOING_FRIENDSHIPS, this.listeners, cursor) { // from class: twitter4j.AsyncTwitterImpl.95
            private final AsyncTwitterImpl this$0;
            private final long val$cursor;

            {
                this.this$0 = this;
                this.val$cursor = cursor;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = this.this$0.twitter.getOutgoingFriendships(this.val$cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotOutgoingFriendships(ids);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FriendshipMethodsAsync
    public void lookupFriendships(String[] screenNames) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.LOOKUP_FRIENDSHIPS, this.listeners, screenNames) { // from class: twitter4j.AsyncTwitterImpl.96
            private final AsyncTwitterImpl this$0;
            private final String[] val$screenNames;

            {
                this.this$0 = this;
                this.val$screenNames = screenNames;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Friendship> friendships = this.this$0.twitter.lookupFriendships(this.val$screenNames);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.lookedUpFriendships(friendships);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FriendshipMethodsAsync
    public void lookupFriendships(long[] ids) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.LOOKUP_FRIENDSHIPS, this.listeners, ids) { // from class: twitter4j.AsyncTwitterImpl.97
            private final AsyncTwitterImpl this$0;
            private final long[] val$ids;

            {
                this.this$0 = this;
                this.val$ids = ids;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Friendship> friendships = this.this$0.twitter.lookupFriendships(this.val$ids);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.lookedUpFriendships(friendships);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FriendshipMethodsAsync
    public void updateFriendship(String screenName, boolean enableDeviceNotification, boolean retweet) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.UPDATE_FRIENDSHIP, this.listeners, screenName, enableDeviceNotification, retweet) { // from class: twitter4j.AsyncTwitterImpl.98
            private final AsyncTwitterImpl this$0;
            private final boolean val$enableDeviceNotification;
            private final boolean val$retweet;
            private final String val$screenName;

            {
                this.this$0 = this;
                this.val$screenName = screenName;
                this.val$enableDeviceNotification = enableDeviceNotification;
                this.val$retweet = retweet;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Relationship relationship = this.this$0.twitter.updateFriendship(this.val$screenName, this.val$enableDeviceNotification, this.val$retweet);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedFriendship(relationship);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FriendshipMethodsAsync
    public void updateFriendship(long userId, boolean enableDeviceNotification, boolean retweet) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.UPDATE_FRIENDSHIP, this.listeners, userId, enableDeviceNotification, retweet) { // from class: twitter4j.AsyncTwitterImpl.99
            private final AsyncTwitterImpl this$0;
            private final boolean val$enableDeviceNotification;
            private final boolean val$retweet;
            private final long val$userId;

            {
                this.this$0 = this;
                this.val$userId = userId;
                this.val$enableDeviceNotification = enableDeviceNotification;
                this.val$retweet = retweet;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Relationship relationship = this.this$0.twitter.updateFriendship(this.val$userId, this.val$enableDeviceNotification, this.val$retweet);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedFriendship(relationship);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FriendshipMethodsAsync
    public void getNoRetweetIds() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.NO_RETWEET_IDS, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.100
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = this.this$0.twitter.getNoRetweetIds();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotNoRetweetIds(ids);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FriendsFollowersMethodsAsync
    public void getFriendsIDs(long cursor) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.FRIENDS_IDS, this.listeners, cursor) { // from class: twitter4j.AsyncTwitterImpl.101
            private final AsyncTwitterImpl this$0;
            private final long val$cursor;

            {
                this.this$0 = this;
                this.val$cursor = cursor;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = this.this$0.twitter.getFriendsIDs(this.val$cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFriendsIDs(ids);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FriendsFollowersMethodsAsync
    public void getFriendsIDs(long userId, long cursor) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.FRIENDS_IDS, this.listeners, userId, cursor) { // from class: twitter4j.AsyncTwitterImpl.102
            private final AsyncTwitterImpl this$0;
            private final long val$cursor;
            private final long val$userId;

            {
                this.this$0 = this;
                this.val$userId = userId;
                this.val$cursor = cursor;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = this.this$0.twitter.getFriendsIDs(this.val$userId, this.val$cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFriendsIDs(ids);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FriendsFollowersMethodsAsync
    public void getFriendsIDs(String screenName, long cursor) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.FRIENDS_IDS, this.listeners, screenName, cursor) { // from class: twitter4j.AsyncTwitterImpl.103
            private final AsyncTwitterImpl this$0;
            private final long val$cursor;
            private final String val$screenName;

            {
                this.this$0 = this;
                this.val$screenName = screenName;
                this.val$cursor = cursor;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = this.this$0.twitter.getFriendsIDs(this.val$screenName, this.val$cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFriendsIDs(ids);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FriendsFollowersMethodsAsync
    public void getFollowersIDs(long cursor) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.FOLLOWERS_IDS, this.listeners, cursor) { // from class: twitter4j.AsyncTwitterImpl.104
            private final AsyncTwitterImpl this$0;
            private final long val$cursor;

            {
                this.this$0 = this;
                this.val$cursor = cursor;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = this.this$0.twitter.getFollowersIDs(this.val$cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFollowersIDs(ids);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FriendsFollowersMethodsAsync
    public void getFollowersIDs(long userId, long cursor) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.FOLLOWERS_IDS, this.listeners, userId, cursor) { // from class: twitter4j.AsyncTwitterImpl.105
            private final AsyncTwitterImpl this$0;
            private final long val$cursor;
            private final long val$userId;

            {
                this.this$0 = this;
                this.val$userId = userId;
                this.val$cursor = cursor;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = this.this$0.twitter.getFollowersIDs(this.val$userId, this.val$cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFollowersIDs(ids);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FriendsFollowersMethodsAsync
    public void getFollowersIDs(String screenName, long cursor) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.FOLLOWERS_IDS, this.listeners, screenName, cursor) { // from class: twitter4j.AsyncTwitterImpl.106
            private final AsyncTwitterImpl this$0;
            private final long val$cursor;
            private final String val$screenName;

            {
                this.this$0 = this;
                this.val$screenName = screenName;
                this.val$cursor = cursor;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = this.this$0.twitter.getFollowersIDs(this.val$screenName, this.val$cursor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFollowersIDs(ids);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.AccountMethodsAsync
    public void verifyCredentials() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.VERIFY_CREDENTIALS, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.107
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = this.this$0.twitter.verifyCredentials();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.verifiedCredentials(user);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.AccountMethodsAsync
    public void updateProfile(String name, String url, String location, String description) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.UPDATE_PROFILE, this.listeners, name, url, location, description) { // from class: twitter4j.AsyncTwitterImpl.108
            private final AsyncTwitterImpl this$0;
            private final String val$description;
            private final String val$location;
            private final String val$name;
            private final String val$url;

            {
                this.this$0 = this;
                this.val$name = name;
                this.val$url = url;
                this.val$location = location;
                this.val$description = description;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = this.this$0.twitter.updateProfile(this.val$name, this.val$url, this.val$location, this.val$description);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedProfile(user);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.AccountMethodsAsync
    public void getRateLimitStatus() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.RATE_LIMIT_STATUS, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.109
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                RateLimitStatus rateLimitStatus = this.this$0.twitter.getRateLimitStatus();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRateLimitStatus(rateLimitStatus);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.AccountMethodsAsync
    public void updateProfileColors(String profileBackgroundColor, String profileTextColor, String profileLinkColor, String profileSidebarFillColor, String profileSidebarBorderColor) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.UPDATE_PROFILE_COLORS, this.listeners, profileBackgroundColor, profileTextColor, profileLinkColor, profileSidebarFillColor, profileSidebarBorderColor) { // from class: twitter4j.AsyncTwitterImpl.110
            private final AsyncTwitterImpl this$0;
            private final String val$profileBackgroundColor;
            private final String val$profileLinkColor;
            private final String val$profileSidebarBorderColor;
            private final String val$profileSidebarFillColor;
            private final String val$profileTextColor;

            {
                this.this$0 = this;
                this.val$profileBackgroundColor = profileBackgroundColor;
                this.val$profileTextColor = profileTextColor;
                this.val$profileLinkColor = profileLinkColor;
                this.val$profileSidebarFillColor = profileSidebarFillColor;
                this.val$profileSidebarBorderColor = profileSidebarBorderColor;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = this.this$0.twitter.updateProfileColors(this.val$profileBackgroundColor, this.val$profileTextColor, this.val$profileLinkColor, this.val$profileSidebarFillColor, this.val$profileSidebarBorderColor);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedProfileColors(user);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.AccountMethodsAsync
    public void updateProfileImage(File image) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.UPDATE_PROFILE_IMAGE, this.listeners, image) { // from class: twitter4j.AsyncTwitterImpl.111
            private final AsyncTwitterImpl this$0;
            private final File val$image;

            {
                this.this$0 = this;
                this.val$image = image;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = this.this$0.twitter.updateProfileImage(this.val$image);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedProfileImage(user);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.AccountMethodsAsync
    public void updateProfileImage(InputStream image) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.UPDATE_PROFILE_IMAGE, this.listeners, image) { // from class: twitter4j.AsyncTwitterImpl.112
            private final AsyncTwitterImpl this$0;
            private final InputStream val$image;

            {
                this.this$0 = this;
                this.val$image = image;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = this.this$0.twitter.updateProfileImage(this.val$image);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedProfileImage(user);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.AccountMethodsAsync
    public void updateProfileBackgroundImage(File image, boolean tile) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.UPDATE_PROFILE_BACKGROUND_IMAGE, this.listeners, image, tile) { // from class: twitter4j.AsyncTwitterImpl.113
            private final AsyncTwitterImpl this$0;
            private final File val$image;
            private final boolean val$tile;

            {
                this.this$0 = this;
                this.val$image = image;
                this.val$tile = tile;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = this.this$0.twitter.updateProfileBackgroundImage(this.val$image, this.val$tile);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedProfileBackgroundImage(user);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.AccountMethodsAsync
    public void updateProfileBackgroundImage(InputStream image, boolean tile) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.UPDATE_PROFILE_BACKGROUND_IMAGE, this.listeners, image, tile) { // from class: twitter4j.AsyncTwitterImpl.114
            private final AsyncTwitterImpl this$0;
            private final InputStream val$image;
            private final boolean val$tile;

            {
                this.this$0 = this;
                this.val$image = image;
                this.val$tile = tile;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = this.this$0.twitter.updateProfileBackgroundImage(this.val$image, this.val$tile);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.updatedProfileBackgroundImage(user);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FavoriteMethodsAsync
    public void getFavorites() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.FAVORITES, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.115
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = this.this$0.twitter.getFavorites();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFavorites(statuses);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FavoriteMethodsAsync
    public void getFavorites(int page) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.FAVORITES, this.listeners, page) { // from class: twitter4j.AsyncTwitterImpl.116
            private final AsyncTwitterImpl this$0;
            private final int val$page;

            {
                this.this$0 = this;
                this.val$page = page;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = this.this$0.twitter.getFavorites(this.val$page);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFavorites(statuses);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FavoriteMethodsAsync
    public void getFavorites(String id) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.FAVORITES, this.listeners, id) { // from class: twitter4j.AsyncTwitterImpl.117
            private final AsyncTwitterImpl this$0;
            private final String val$id;

            {
                this.this$0 = this;
                this.val$id = id;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = this.this$0.twitter.getFavorites(this.val$id);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFavorites(statuses);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FavoriteMethodsAsync
    public void getFavorites(String id, int page) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.FAVORITES, this.listeners, id, page) { // from class: twitter4j.AsyncTwitterImpl.118
            private final AsyncTwitterImpl this$0;
            private final String val$id;
            private final int val$page;

            {
                this.this$0 = this;
                this.val$id = id;
                this.val$page = page;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Status> statuses = this.this$0.twitter.getFavorites(this.val$id, this.val$page);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotFavorites(statuses);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FavoriteMethodsAsync
    public void createFavorite(long id) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.CREATE_FAVORITE, this.listeners, id) { // from class: twitter4j.AsyncTwitterImpl.119
            private final AsyncTwitterImpl this$0;
            private final long val$id;

            {
                this.this$0 = this;
                this.val$id = id;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Status status = this.this$0.twitter.createFavorite(this.val$id);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdFavorite(status);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.FavoriteMethodsAsync
    public void destroyFavorite(long id) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.DESTROY_FAVORITE, this.listeners, id) { // from class: twitter4j.AsyncTwitterImpl.120
            private final AsyncTwitterImpl this$0;
            private final long val$id;

            {
                this.this$0 = this;
                this.val$id = id;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Status status = this.this$0.twitter.destroyFavorite(this.val$id);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedFavorite(status);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.NotificationMethodsAsync
    public void enableNotification(String screenName) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.ENABLE_NOTIFICATION, this.listeners, screenName) { // from class: twitter4j.AsyncTwitterImpl.121
            private final AsyncTwitterImpl this$0;
            private final String val$screenName;

            {
                this.this$0 = this;
                this.val$screenName = screenName;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = this.this$0.twitter.enableNotification(this.val$screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.enabledNotification(user);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.NotificationMethodsAsync
    public void enableNotification(long userId) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.ENABLE_NOTIFICATION, this.listeners, userId) { // from class: twitter4j.AsyncTwitterImpl.122
            private final AsyncTwitterImpl this$0;
            private final long val$userId;

            {
                this.this$0 = this;
                this.val$userId = userId;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = this.this$0.twitter.enableNotification(this.val$userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.enabledNotification(user);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.NotificationMethodsAsync
    public void disableNotification(String screenName) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.DISABLE_NOTIFICATION, this.listeners, screenName) { // from class: twitter4j.AsyncTwitterImpl.123
            private final AsyncTwitterImpl this$0;
            private final String val$screenName;

            {
                this.this$0 = this;
                this.val$screenName = screenName;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = this.this$0.twitter.disableNotification(this.val$screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.disabledNotification(user);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.NotificationMethodsAsync
    public void disableNotification(long userId) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.DISABLE_NOTIFICATION, this.listeners, userId) { // from class: twitter4j.AsyncTwitterImpl.124
            private final AsyncTwitterImpl this$0;
            private final long val$userId;

            {
                this.this$0 = this;
                this.val$userId = userId;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = this.this$0.twitter.disableNotification(this.val$userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.disabledNotification(user);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.BlockMethodsAsync
    public void createBlock(String screenName) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.CREATE_BLOCK, this.listeners, screenName) { // from class: twitter4j.AsyncTwitterImpl.125
            private final AsyncTwitterImpl this$0;
            private final String val$screenName;

            {
                this.this$0 = this;
                this.val$screenName = screenName;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = this.this$0.twitter.createBlock(this.val$screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdBlock(user);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.BlockMethodsAsync
    public void createBlock(long userId) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.CREATE_BLOCK, this.listeners, userId) { // from class: twitter4j.AsyncTwitterImpl.126
            private final AsyncTwitterImpl this$0;
            private final long val$userId;

            {
                this.this$0 = this;
                this.val$userId = userId;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = this.this$0.twitter.createBlock(this.val$userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdBlock(user);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.BlockMethodsAsync
    public void destroyBlock(String screenName) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.DESTROY_BLOCK, this.listeners, screenName) { // from class: twitter4j.AsyncTwitterImpl.127
            private final AsyncTwitterImpl this$0;
            private final String val$screenName;

            {
                this.this$0 = this;
                this.val$screenName = screenName;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = this.this$0.twitter.destroyBlock(this.val$screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedBlock(user);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.BlockMethodsAsync
    public void destroyBlock(long userId) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.DESTROY_BLOCK, this.listeners, userId) { // from class: twitter4j.AsyncTwitterImpl.128
            private final AsyncTwitterImpl this$0;
            private final long val$userId;

            {
                this.this$0 = this;
                this.val$userId = userId;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = this.this$0.twitter.destroyBlock(this.val$userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.destroyedBlock(user);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.BlockMethodsAsync
    public void existsBlock(String screenName) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.EXISTS_BLOCK, this.listeners, screenName) { // from class: twitter4j.AsyncTwitterImpl.129
            private final AsyncTwitterImpl this$0;
            private final String val$screenName;

            {
                this.this$0 = this;
                this.val$screenName = screenName;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                boolean exists = this.this$0.twitter.existsBlock(this.val$screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotExistsBlock(exists);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.BlockMethodsAsync
    public void existsBlock(long userId) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.EXISTS_BLOCK, this.listeners, userId) { // from class: twitter4j.AsyncTwitterImpl.130
            private final AsyncTwitterImpl this$0;
            private final long val$userId;

            {
                this.this$0 = this;
                this.val$userId = userId;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                boolean exists = this.this$0.twitter.existsBlock(this.val$userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotExistsBlock(exists);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.BlockMethodsAsync
    public void getBlockingUsers() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.BLOCKING_USERS, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.131
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = this.this$0.twitter.getBlockingUsers();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotBlockingUsers(users);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.BlockMethodsAsync
    public void getBlockingUsers(int page) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.BLOCKING_USERS, this.listeners, page) { // from class: twitter4j.AsyncTwitterImpl.132
            private final AsyncTwitterImpl this$0;
            private final int val$page;

            {
                this.this$0 = this;
                this.val$page = page;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<User> users = this.this$0.twitter.getBlockingUsers(this.val$page);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotBlockingUsers(users);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.BlockMethodsAsync
    public void getBlockingUsersIDs() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.BLOCKING_USERS_IDS, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.133
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                IDs ids = this.this$0.twitter.getBlockingUsersIDs();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotBlockingUsersIDs(ids);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.SpamReportingMethodsAsync
    public void reportSpam(long userId) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.REPORT_SPAM, this.listeners, userId) { // from class: twitter4j.AsyncTwitterImpl.134
            private final AsyncTwitterImpl this$0;
            private final long val$userId;

            {
                this.this$0 = this;
                this.val$userId = userId;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = this.this$0.twitter.reportSpam(this.val$userId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.reportedSpam(user);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.SpamReportingMethodsAsync
    public void reportSpam(String screenName) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.REPORT_SPAM, this.listeners, screenName) { // from class: twitter4j.AsyncTwitterImpl.135
            private final AsyncTwitterImpl this$0;
            private final String val$screenName;

            {
                this.this$0 = this;
                this.val$screenName = screenName;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                User user = this.this$0.twitter.reportSpam(this.val$screenName);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.reportedSpam(user);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.LocalTrendsMethodsAsync
    public void getAvailableTrends() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.AVAILABLE_TRENDS, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.136
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Location> locations = this.this$0.twitter.getAvailableTrends();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotAvailableTrends(locations);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.LocalTrendsMethodsAsync
    public void getAvailableTrends(GeoLocation location) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.AVAILABLE_TRENDS, this.listeners, location) { // from class: twitter4j.AsyncTwitterImpl.137
            private final AsyncTwitterImpl this$0;
            private final GeoLocation val$location;

            {
                this.this$0 = this;
                this.val$location = location;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Location> locations = this.this$0.twitter.getAvailableTrends(this.val$location);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotAvailableTrends(locations);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.LocalTrendsMethodsAsync
    public void getLocationTrends(int woeid) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.LOCATION_TRENDS, this.listeners, woeid) { // from class: twitter4j.AsyncTwitterImpl.138
            private final AsyncTwitterImpl this$0;
            private final int val$woeid;

            {
                this.this$0 = this;
                this.val$woeid = woeid;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Trends trends = this.this$0.twitter.getLocationTrends(this.val$woeid);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotLocationTrends(trends);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.GeoMethodsAsync
    public void searchPlaces(GeoQuery query) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.SEARCH_PLACES, this.listeners, query) { // from class: twitter4j.AsyncTwitterImpl.139
            private final AsyncTwitterImpl this$0;
            private final GeoQuery val$query;

            {
                this.this$0 = this;
                this.val$query = query;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Place> places = this.this$0.twitter.searchPlaces(this.val$query);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.searchedPlaces(places);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.GeoMethodsAsync
    public void getSimilarPlaces(GeoLocation location, String name, String containedWithin, String streetAddress) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.SIMILAR_PLACES, this.listeners, location, name, containedWithin, streetAddress) { // from class: twitter4j.AsyncTwitterImpl.140
            private final AsyncTwitterImpl this$0;
            private final String val$containedWithin;
            private final GeoLocation val$location;
            private final String val$name;
            private final String val$streetAddress;

            {
                this.this$0 = this;
                this.val$location = location;
                this.val$name = name;
                this.val$containedWithin = containedWithin;
                this.val$streetAddress = streetAddress;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                SimilarPlaces similarPlaces = this.this$0.twitter.getSimilarPlaces(this.val$location, this.val$name, this.val$containedWithin, this.val$streetAddress);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotSimilarPlaces(similarPlaces);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.GeoMethodsAsync
    public void reverseGeoCode(GeoQuery query) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.REVERSE_GEO_CODE, this.listeners, query) { // from class: twitter4j.AsyncTwitterImpl.141
            private final AsyncTwitterImpl this$0;
            private final GeoQuery val$query;

            {
                this.this$0 = this;
                this.val$query = query;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<Place> places = this.this$0.twitter.reverseGeoCode(this.val$query);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotReverseGeoCode(places);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.GeoMethodsAsync
    public void getGeoDetails(String id) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.GEO_DETAILS, this.listeners, id) { // from class: twitter4j.AsyncTwitterImpl.142
            private final AsyncTwitterImpl this$0;
            private final String val$id;

            {
                this.this$0 = this;
                this.val$id = id;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Place place = this.this$0.twitter.getGeoDetails(this.val$id);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotGeoDetails(place);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.GeoMethodsAsync
    public void createPlace(String name, String containedWithin, String token, GeoLocation location, String streetAddress) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.CREATE_PLACE, this.listeners, name, containedWithin, token, location, streetAddress) { // from class: twitter4j.AsyncTwitterImpl.143
            private final AsyncTwitterImpl this$0;
            private final String val$containedWithin;
            private final GeoLocation val$location;
            private final String val$name;
            private final String val$streetAddress;
            private final String val$token;

            {
                this.this$0 = this;
                this.val$name = name;
                this.val$containedWithin = containedWithin;
                this.val$token = token;
                this.val$location = location;
                this.val$streetAddress = streetAddress;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                Place place = this.this$0.twitter.createPlace(this.val$name, this.val$containedWithin, this.val$token, this.val$location, this.val$streetAddress);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.createdPlace(place);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.LegalResourcesAsync
    public void getTermsOfService() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.TERMS_OF_SERVICE, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.144
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                String tos = this.this$0.twitter.getTermsOfService();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotTermsOfService(tos);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.LegalResourcesAsync
    public void getPrivacyPolicy() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.PRIVACY_POLICY, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.145
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                String privacyPolicy = this.this$0.twitter.getPrivacyPolicy();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotPrivacyPolicy(privacyPolicy);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.NewTwitterMethodsAsync
    public void getRelatedResults(long statusId) {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.RELATED_RESULTS, this.listeners, statusId) { // from class: twitter4j.AsyncTwitterImpl.146
            private final AsyncTwitterImpl this$0;
            private final long val$statusId;

            {
                this.this$0 = this;
                this.val$statusId = statusId;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            void invoke(List<TwitterListener> listeners) throws TwitterException {
                RelatedResults relatedResults = this.this$0.twitter.getRelatedResults(this.val$statusId);
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotRelatedResults(relatedResults);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.HelpMethodsAsync
    public void test() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.TEST, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.147
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                boolean ok = this.this$0.twitter.test();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.tested(ok);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.HelpMethodsAsync
    public void getAPIConfiguration() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.CONFIGURATION, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.148
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                TwitterAPIConfiguration apiConf = this.this$0.twitter.getAPIConfiguration();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotAPIConfiguration(apiConf);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.api.HelpMethodsAsync
    public void getLanguages() {
        getDispatcher().invokeLater(new AsyncTask(this, TwitterMethod.LANGUAGES, this.listeners) { // from class: twitter4j.AsyncTwitterImpl.149
            private final AsyncTwitterImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // twitter4j.AsyncTwitterImpl.AsyncTask
            public void invoke(List<TwitterListener> listeners) throws TwitterException {
                ResponseList<HelpMethods.Language> languages = this.this$0.twitter.getLanguages();
                for (TwitterListener listener : listeners) {
                    try {
                        listener.gotLanguages(languages);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    @Override // twitter4j.TwitterBaseImpl, twitter4j.TwitterBase
    public void shutdown() throws Throwable {
        Class clsClass$;
        super.shutdown();
        if (class$twitter4j$AsyncTwitterImpl == null) {
            clsClass$ = class$("twitter4j.AsyncTwitterImpl");
            class$twitter4j$AsyncTwitterImpl = clsClass$;
        } else {
            clsClass$ = class$twitter4j$AsyncTwitterImpl;
        }
        synchronized (clsClass$) {
            if (dispatcher != null) {
                dispatcher.shutdown();
                dispatcher = null;
            }
        }
        this.twitter.shutdown();
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    private Dispatcher getDispatcher() throws Throwable {
        Class clsClass$;
        if (dispatcher == null) {
            if (class$twitter4j$AsyncTwitterImpl == null) {
                clsClass$ = class$("twitter4j.AsyncTwitterImpl");
                class$twitter4j$AsyncTwitterImpl = clsClass$;
            } else {
                clsClass$ = class$twitter4j$AsyncTwitterImpl;
            }
            synchronized (clsClass$) {
                if (dispatcher == null) {
                    dispatcher = new DispatcherFactory(this.conf).getInstance();
                }
            }
        }
        return dispatcher;
    }

    @Override // twitter4j.TwitterBaseImpl, twitter4j.auth.OAuthSupport
    public void setOAuthConsumer(String consumerKey, String consumerSecret) {
        this.twitter.setOAuthConsumer(consumerKey, consumerSecret);
    }

    @Override // twitter4j.TwitterBaseImpl, twitter4j.auth.OAuthSupport
    public RequestToken getOAuthRequestToken() throws TwitterException {
        return this.twitter.getOAuthRequestToken();
    }

    @Override // twitter4j.TwitterBaseImpl, twitter4j.auth.OAuthSupport
    public RequestToken getOAuthRequestToken(String callbackUrl) throws TwitterException {
        return this.twitter.getOAuthRequestToken(callbackUrl);
    }

    @Override // twitter4j.TwitterBaseImpl, twitter4j.auth.OAuthSupport
    public AccessToken getOAuthAccessToken() throws TwitterException {
        return this.twitter.getOAuthAccessToken();
    }

    @Override // twitter4j.TwitterBaseImpl, twitter4j.auth.OAuthSupport
    public AccessToken getOAuthAccessToken(String oauthVerifier) throws TwitterException {
        return this.twitter.getOAuthAccessToken(oauthVerifier);
    }

    @Override // twitter4j.TwitterBaseImpl, twitter4j.auth.OAuthSupport
    public AccessToken getOAuthAccessToken(RequestToken requestToken) throws TwitterException {
        return this.twitter.getOAuthAccessToken(requestToken);
    }

    @Override // twitter4j.TwitterBaseImpl, twitter4j.auth.OAuthSupport
    public AccessToken getOAuthAccessToken(RequestToken requestToken, String oauthVerifier) throws TwitterException {
        return this.twitter.getOAuthAccessToken(requestToken, oauthVerifier);
    }

    @Override // twitter4j.TwitterBaseImpl, twitter4j.auth.OAuthSupport
    public void setOAuthAccessToken(AccessToken accessToken) {
        this.twitter.setOAuthAccessToken(accessToken);
    }

    @Override // twitter4j.TwitterBaseImpl, twitter4j.auth.OAuthSupport
    public AccessToken getOAuthAccessToken(String screenName, String password) throws TwitterException {
        return this.twitter.getOAuthAccessToken(screenName, password);
    }

    abstract class AsyncTask implements Runnable {
        List<TwitterListener> listeners;
        TwitterMethod method;
        private final AsyncTwitterImpl this$0;

        abstract void invoke(List<TwitterListener> list) throws TwitterException;

        AsyncTask(AsyncTwitterImpl asyncTwitterImpl, TwitterMethod method, List<TwitterListener> listeners) {
            this.this$0 = asyncTwitterImpl;
            this.method = method;
            this.listeners = listeners;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                invoke(this.listeners);
            } catch (TwitterException te) {
                if (this.listeners != null) {
                    for (TwitterListener listener : this.listeners) {
                        try {
                            listener.onException(te, this.method);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
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
        AsyncTwitterImpl that = (AsyncTwitterImpl) o;
        if (this.listeners == null ? that.listeners != null : !this.listeners.equals(that.listeners)) {
            return false;
        }
        if (this.twitter != null) {
            if (this.twitter.equals(that.twitter)) {
                return true;
            }
        } else if (that.twitter == null) {
            return true;
        }
        return false;
    }

    @Override // twitter4j.TwitterBaseImpl
    public int hashCode() {
        int result = super.hashCode();
        return (((result * 31) + (this.twitter != null ? this.twitter.hashCode() : 0)) * 31) + (this.listeners != null ? this.listeners.hashCode() : 0);
    }

    @Override // twitter4j.TwitterBaseImpl
    public String toString() {
        return new StringBuffer().append("AsyncTwitterImpl{twitter=").append(this.twitter).append(", listeners=").append(this.listeners).append('}').toString();
    }
}
