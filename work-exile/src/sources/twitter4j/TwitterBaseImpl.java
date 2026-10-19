package twitter4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import twitter4j.auth.AccessToken;
import twitter4j.auth.Authorization;
import twitter4j.auth.AuthorizationFactory;
import twitter4j.auth.BasicAuthorization;
import twitter4j.auth.NullAuthorization;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.OAuthSupport;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpClientWrapper;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.http.HttpResponseEvent;
import twitter4j.internal.http.HttpResponseListener;
import twitter4j.internal.http.XAuthAuthorization;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
abstract class TwitterBaseImpl implements Serializable, OAuthSupport, HttpResponseListener {
    private static final long serialVersionUID = -3812176145960812140L;
    protected Authorization auth;
    protected Configuration conf;
    protected transient HttpClientWrapper http;
    protected transient String screenName = null;
    protected transient long id = 0;
    private List<RateLimitStatusListener> rateLimitStatusListeners = new ArrayList(0);

    TwitterBaseImpl(Configuration conf) {
        this.conf = conf;
        init();
    }

    TwitterBaseImpl(Configuration conf, Authorization auth) {
        this.conf = conf;
        this.auth = auth;
        init();
    }

    private void init() {
        if (this.auth == null) {
            String consumerKey = this.conf.getOAuthConsumerKey();
            String consumerSecret = this.conf.getOAuthConsumerSecret();
            if (consumerKey != null && consumerSecret != null) {
                OAuthAuthorization oauth2 = new OAuthAuthorization(this.conf);
                String accessToken = this.conf.getOAuthAccessToken();
                String accessTokenSecret = this.conf.getOAuthAccessTokenSecret();
                if (accessToken != null && accessTokenSecret != null) {
                    oauth2.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));
                }
                this.auth = oauth2;
            } else {
                this.auth = NullAuthorization.getInstance();
            }
        }
        this.http = new HttpClientWrapper(this.conf);
        this.http.setHttpResponseListener(this);
    }

    public String getScreenName() throws IllegalStateException, TwitterException {
        if (!this.auth.isEnabled()) {
            throw new IllegalStateException("Neither user ID/password combination nor OAuth consumer key/secret combination supplied");
        }
        if (this.screenName == null) {
            if (this.auth instanceof BasicAuthorization) {
                this.screenName = ((BasicAuthorization) this.auth).getUserId();
                if (-1 != this.screenName.indexOf("@")) {
                    this.screenName = null;
                }
            }
            if (this.screenName == null) {
                fillInIDAndScreenName();
            }
        }
        return this.screenName;
    }

    public long getId() throws IllegalStateException, TwitterException {
        if (!this.auth.isEnabled()) {
            throw new IllegalStateException("Neither user ID/password combination nor OAuth consumer key/secret combination supplied");
        }
        if (0 == this.id) {
            fillInIDAndScreenName();
        }
        return this.id;
    }

    protected User fillInIDAndScreenName() throws TwitterException {
        ensureAuthorizationEnabled();
        User user = new UserJSONImpl(this.http.get(new StringBuffer().append(this.conf.getRestBaseURL()).append("account/verify_credentials.json?include_entities=").append(this.conf.isIncludeEntitiesEnabled()).toString(), this.auth), this.conf);
        this.screenName = user.getScreenName();
        this.id = user.getId();
        return user;
    }

    public void addRateLimitStatusListener(RateLimitStatusListener listener) {
        this.rateLimitStatusListeners.add(listener);
    }

    @Override // twitter4j.internal.http.HttpResponseListener
    public void httpResponseReceived(HttpResponseEvent event) {
        RateLimitStatus rateLimitStatus;
        int statusCode;
        if (this.rateLimitStatusListeners.size() != 0) {
            HttpResponse res = event.getResponse();
            TwitterException te = event.getTwitterException();
            if (te != null) {
                rateLimitStatus = te.getRateLimitStatus();
                statusCode = te.getStatusCode();
            } else {
                rateLimitStatus = RateLimitStatusJSONImpl.createFromResponseHeader(res);
                statusCode = res.getStatusCode();
            }
            if (rateLimitStatus != null) {
                RateLimitStatusEvent statusEvent = new RateLimitStatusEvent(this, rateLimitStatus, event.isAuthenticated());
                if (statusCode == 420 || statusCode == 503) {
                    for (RateLimitStatusListener listener : this.rateLimitStatusListeners) {
                        listener.onRateLimitStatus(statusEvent);
                        listener.onRateLimitReached(statusEvent);
                    }
                    return;
                }
                Iterator<RateLimitStatusListener> it = this.rateLimitStatusListeners.iterator();
                while (it.hasNext()) {
                    it.next().onRateLimitStatus(statusEvent);
                }
            }
        }
    }

    public final Authorization getAuthorization() {
        return this.auth;
    }

    public Configuration getConfiguration() {
        return this.conf;
    }

    public void shutdown() {
        if (this.http != null) {
            this.http.shutdown();
        }
    }

    protected final void ensureAuthorizationEnabled() {
        if (!this.auth.isEnabled()) {
            throw new IllegalStateException("Authentication credentials are missing. See http://twitter4j.org/configuration.html for the detail.");
        }
    }

    protected final void ensureOAuthEnabled() {
        if (!(this.auth instanceof OAuthAuthorization)) {
            throw new IllegalStateException("OAuth required. Authentication credentials are missing. See http://twitter4j.org/configuration.html for the detail.");
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(this.conf);
        out.writeObject(this.auth);
        List<RateLimitStatusListener> serializableRateLimitStatusListeners = new ArrayList<>(0);
        for (RateLimitStatusListener listener : this.rateLimitStatusListeners) {
            if (listener instanceof Serializable) {
                serializableRateLimitStatusListeners.add(listener);
            }
        }
        out.writeObject(serializableRateLimitStatusListeners);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        this.conf = (Configuration) stream.readObject();
        this.auth = (Authorization) stream.readObject();
        this.rateLimitStatusListeners = (List) stream.readObject();
        this.http = new HttpClientWrapper(this.conf);
        this.http.setHttpResponseListener(this);
    }

    public synchronized void setOAuthConsumer(String consumerKey, String consumerSecret) {
        try {
            if (consumerKey == null) {
                throw new NullPointerException("consumer key is null");
            }
            if (consumerSecret == null) {
                throw new NullPointerException("consumer secret is null");
            }
            if (this.auth instanceof NullAuthorization) {
                OAuthAuthorization oauth2 = new OAuthAuthorization(this.conf);
                oauth2.setOAuthConsumer(consumerKey, consumerSecret);
                this.auth = oauth2;
            } else if (this.auth instanceof BasicAuthorization) {
                XAuthAuthorization xauth = new XAuthAuthorization((BasicAuthorization) this.auth);
                xauth.setOAuthConsumer(consumerKey, consumerSecret);
                this.auth = xauth;
            } else if (this.auth instanceof OAuthAuthorization) {
                throw new IllegalStateException("consumer key/secret pair already set.");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public RequestToken getOAuthRequestToken() throws TwitterException {
        return getOAuthRequestToken(null);
    }

    public RequestToken getOAuthRequestToken(String callbackUrl) throws TwitterException {
        return getOAuth().getOAuthRequestToken(callbackUrl);
    }

    @Override // twitter4j.auth.OAuthSupport
    public RequestToken getOAuthRequestToken(String callbackUrl, String xAuthAccessType) throws TwitterException {
        return getOAuth().getOAuthRequestToken(callbackUrl, xAuthAccessType);
    }

    public synchronized AccessToken getOAuthAccessToken() throws TwitterException {
        AccessToken oauthAccessToken;
        Authorization auth = getAuthorization();
        if (auth instanceof BasicAuthorization) {
            BasicAuthorization basicAuth = (BasicAuthorization) auth;
            Authorization auth2 = AuthorizationFactory.getInstance(this.conf);
            if (auth2 instanceof OAuthAuthorization) {
                this.auth = auth2;
                oauthAccessToken = ((OAuthAuthorization) auth2).getOAuthAccessToken(basicAuth.getUserId(), basicAuth.getPassword());
            } else {
                throw new IllegalStateException("consumer key / secret combination not supplied.");
            }
        } else if (auth instanceof XAuthAuthorization) {
            XAuthAuthorization xauth = (XAuthAuthorization) auth;
            this.auth = xauth;
            OAuthAuthorization oauthAuth = new OAuthAuthorization(this.conf);
            oauthAuth.setOAuthConsumer(xauth.getConsumerKey(), xauth.getConsumerSecret());
            oauthAccessToken = oauthAuth.getOAuthAccessToken(xauth.getUserId(), xauth.getPassword());
        } else {
            oauthAccessToken = getOAuth().getOAuthAccessToken();
        }
        this.screenName = oauthAccessToken.getScreenName();
        this.id = oauthAccessToken.getUserId();
        return oauthAccessToken;
    }

    public synchronized AccessToken getOAuthAccessToken(String oauthVerifier) throws TwitterException {
        AccessToken oauthAccessToken;
        oauthAccessToken = getOAuth().getOAuthAccessToken(oauthVerifier);
        this.screenName = oauthAccessToken.getScreenName();
        return oauthAccessToken;
    }

    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken) throws TwitterException {
        AccessToken oauthAccessToken;
        OAuthSupport oauth2 = getOAuth();
        oauthAccessToken = oauth2.getOAuthAccessToken(requestToken);
        this.screenName = oauthAccessToken.getScreenName();
        return oauthAccessToken;
    }

    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken, String oauthVerifier) throws TwitterException {
        return getOAuth().getOAuthAccessToken(requestToken, oauthVerifier);
    }

    public synchronized void setOAuthAccessToken(AccessToken accessToken) {
        getOAuth().setOAuthAccessToken(accessToken);
    }

    public synchronized AccessToken getOAuthAccessToken(String screenName, String password) throws TwitterException {
        return getOAuth().getOAuthAccessToken(screenName, password);
    }

    private OAuthSupport getOAuth() {
        if (!(this.auth instanceof OAuthSupport)) {
            throw new IllegalStateException("OAuth consumer key/secret combination not supplied");
        }
        return (OAuthSupport) this.auth;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TwitterBaseImpl)) {
            return false;
        }
        TwitterBaseImpl that = (TwitterBaseImpl) o;
        if (this.auth == null ? that.auth != null : !this.auth.equals(that.auth)) {
            return false;
        }
        if (!this.conf.equals(that.conf)) {
            return false;
        }
        if (this.http == null ? that.http != null : !this.http.equals(that.http)) {
            return false;
        }
        return this.rateLimitStatusListeners.equals(that.rateLimitStatusListeners);
    }

    public int hashCode() {
        int result = this.conf.hashCode();
        return (((((result * 31) + (this.http != null ? this.http.hashCode() : 0)) * 31) + this.rateLimitStatusListeners.hashCode()) * 31) + (this.auth != null ? this.auth.hashCode() : 0);
    }

    public String toString() {
        return new StringBuffer().append("TwitterBase{conf=").append(this.conf).append(", http=").append(this.http).append(", rateLimitStatusListeners=").append(this.rateLimitStatusListeners).append(", auth=").append(this.auth).append('}').toString();
    }
}
