package twitter4j.auth;

import twitter4j.conf.Configuration;

/* JADX INFO: loaded from: classes.dex */
public final class AuthorizationFactory {
    public static Authorization getInstance(Configuration conf) {
        Authorization auth = null;
        String consumerKey = conf.getOAuthConsumerKey();
        String consumerSecret = conf.getOAuthConsumerSecret();
        if (consumerKey != null && consumerSecret != null) {
            OAuthAuthorization oauth2 = new OAuthAuthorization(conf);
            String accessToken = conf.getOAuthAccessToken();
            String accessTokenSecret = conf.getOAuthAccessTokenSecret();
            if (accessToken != null && accessTokenSecret != null) {
                oauth2.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));
            }
            auth = oauth2;
        } else {
            String screenName = conf.getUser();
            String password = conf.getPassword();
            if (screenName != null && password != null) {
                auth = new BasicAuthorization(screenName, password);
            }
        }
        if (auth == null) {
            return NullAuthorization.getInstance();
        }
        return auth;
    }
}
