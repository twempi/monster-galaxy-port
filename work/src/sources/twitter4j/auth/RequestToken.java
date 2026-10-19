package twitter4j.auth;

import java.io.Serializable;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;
import twitter4j.internal.http.HttpResponse;

/* JADX INFO: loaded from: classes.dex */
public final class RequestToken extends OAuthToken implements Serializable {
    private static final long serialVersionUID = -8214365845469757952L;
    private final Configuration conf;

    /* JADX INFO: renamed from: oauth, reason: collision with root package name */
    private OAuthSupport f0oauth;

    @Override // twitter4j.auth.OAuthToken
    public boolean equals(Object x0) {
        return super.equals(x0);
    }

    @Override // twitter4j.auth.OAuthToken
    public String getParameter(String x0) {
        return super.getParameter(x0);
    }

    @Override // twitter4j.auth.OAuthToken
    public String getToken() {
        return super.getToken();
    }

    @Override // twitter4j.auth.OAuthToken
    public String getTokenSecret() {
        return super.getTokenSecret();
    }

    @Override // twitter4j.auth.OAuthToken
    public int hashCode() {
        return super.hashCode();
    }

    @Override // twitter4j.auth.OAuthToken
    public String toString() {
        return super.toString();
    }

    RequestToken(HttpResponse res, OAuthSupport oauth2) throws TwitterException {
        super(res);
        this.conf = ConfigurationContext.getInstance();
        this.f0oauth = oauth2;
    }

    public RequestToken(String token, String tokenSecret) {
        super(token, tokenSecret);
        this.conf = ConfigurationContext.getInstance();
    }

    RequestToken(String token, String tokenSecret, OAuthSupport oauth2) {
        super(token, tokenSecret);
        this.conf = ConfigurationContext.getInstance();
        this.f0oauth = oauth2;
    }

    public String getAuthorizationURL() {
        return new StringBuffer().append(this.conf.getOAuthAuthorizationURL()).append("?oauth_token=").append(getToken()).toString();
    }

    public String getAuthenticationURL() {
        return new StringBuffer().append(this.conf.getOAuthAuthenticationURL()).append("?oauth_token=").append(getToken()).toString();
    }
}
