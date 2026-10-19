package twitter4j;

import java.io.Serializable;
import twitter4j.auth.AccessToken;
import twitter4j.auth.Authorization;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class AsyncTwitterFactory implements Serializable {
    private static final long serialVersionUID = -2565686715640816219L;
    private final Configuration conf;

    public AsyncTwitterFactory() {
        this(ConfigurationContext.getInstance());
    }

    public AsyncTwitterFactory(Configuration conf) {
        if (conf == null) {
            throw new NullPointerException("configuration cannot be null");
        }
        this.conf = conf;
    }

    public AsyncTwitterFactory(String configTreePath) {
        this.conf = ConfigurationContext.getInstance(configTreePath);
    }

    public AsyncTwitter getInstance() {
        return new AsyncTwitterImpl(this.conf);
    }

    public AsyncTwitter getInstance(AccessToken accessToken) {
        String consumerKey = this.conf.getOAuthConsumerKey();
        String consumerSecret = this.conf.getOAuthConsumerSecret();
        if (consumerKey == null && consumerSecret == null) {
            throw new IllegalStateException("Consumer key and Consumer secret not supplied.");
        }
        OAuthAuthorization oauth2 = new OAuthAuthorization(this.conf);
        oauth2.setOAuthConsumer(consumerKey, consumerSecret);
        oauth2.setOAuthAccessToken(accessToken);
        return new AsyncTwitterImpl(this.conf, oauth2);
    }

    public AsyncTwitter getInstance(Authorization auth) {
        return new AsyncTwitterImpl(this.conf, auth);
    }
}
