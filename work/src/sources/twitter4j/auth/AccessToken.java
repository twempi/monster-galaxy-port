package twitter4j.auth;

import java.io.Serializable;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

/* JADX INFO: loaded from: classes.dex */
public class AccessToken extends OAuthToken implements Serializable {
    private static final long serialVersionUID = -8344528374458826291L;
    private String screenName;
    private long userId;

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

    AccessToken(HttpResponse res) throws TwitterException {
        this(res.asString());
    }

    AccessToken(String str) {
        super(str);
        this.screenName = getParameter("screen_name");
        String sUserId = getParameter("user_id");
        if (sUserId != null) {
            this.userId = Long.parseLong(sUserId);
        }
    }

    public AccessToken(String token, String tokenSecret) {
        super(token, tokenSecret);
        try {
            String sUserId = token.substring(0, token.indexOf("-"));
            if (sUserId != null) {
                this.userId = Long.parseLong(sUserId);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Invalid access token format.");
        }
    }

    public String getScreenName() {
        return this.screenName;
    }

    public long getUserId() {
        return this.userId;
    }

    @Override // twitter4j.auth.OAuthToken
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
        AccessToken that = (AccessToken) o;
        if (this.userId != that.userId) {
            return false;
        }
        if (this.screenName != null) {
            if (this.screenName.equals(that.screenName)) {
                return true;
            }
        } else if (that.screenName == null) {
            return true;
        }
        return false;
    }

    @Override // twitter4j.auth.OAuthToken
    public int hashCode() {
        int result = super.hashCode();
        return (((result * 31) + (this.screenName != null ? this.screenName.hashCode() : 0)) * 31) + ((int) (this.userId ^ (this.userId >>> 32)));
    }

    @Override // twitter4j.auth.OAuthToken
    public String toString() {
        return new StringBuffer().append("AccessToken{screenName='").append(this.screenName).append('\'').append(", userId=").append(this.userId).append('}').toString();
    }
}
