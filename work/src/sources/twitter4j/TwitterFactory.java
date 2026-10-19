package twitter4j;

import java.io.Serializable;
import twitter4j.auth.AccessToken;
import twitter4j.auth.Authorization;
import twitter4j.auth.AuthorizationFactory;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;

/* JADX INFO: loaded from: classes.dex */
public final class TwitterFactory implements Serializable {
    private static final long serialVersionUID = 5193900138477709155L;
    private final Configuration conf;

    public TwitterFactory() {
        this(ConfigurationContext.getInstance());
    }

    public TwitterFactory(Configuration conf) {
        if (conf == null) {
            throw new NullPointerException("configuration cannot be null");
        }
        this.conf = conf;
    }

    public TwitterFactory(String configTreePath) {
        this(ConfigurationContext.getInstance(configTreePath));
    }

    public Twitter getInstance() {
        return new TwitterImpl(this.conf, AuthorizationFactory.getInstance(this.conf));
    }

    public Twitter getInstance(AccessToken accessToken) {
        String consumerKey = this.conf.getOAuthConsumerKey();
        String consumerSecret = this.conf.getOAuthConsumerSecret();
        if (consumerKey == null && consumerSecret == null) {
            throw new IllegalStateException("Consumer key and Consumer secret not supplied.");
        }
        OAuthAuthorization oauth2 = new OAuthAuthorization(this.conf);
        oauth2.setOAuthAccessToken(accessToken);
        return new TwitterImpl(this.conf, oauth2);
    }

    public Twitter getInstance(Authorization auth) {
        return new TwitterImpl(this.conf, auth);
    }
}
