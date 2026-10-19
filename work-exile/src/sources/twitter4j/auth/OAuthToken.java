package twitter4j.auth;

import java.io.Serializable;
import javax.crypto.spec.SecretKeySpec;
import oauth.signpost.OAuth;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.util.T4JInternalStringUtil;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
abstract class OAuthToken implements Serializable {
    private static final long serialVersionUID = 3891133932519746686L;
    String[] responseStr;
    private transient SecretKeySpec secretKeySpec;
    private String token;
    private String tokenSecret;

    public OAuthToken(String token, String tokenSecret) {
        this.responseStr = null;
        this.token = token;
        this.tokenSecret = tokenSecret;
    }

    OAuthToken(HttpResponse response) throws TwitterException {
        this(response.asString());
    }

    OAuthToken(String string) {
        this.responseStr = null;
        this.responseStr = T4JInternalStringUtil.split(string, "&");
        this.tokenSecret = getParameter(OAuth.OAUTH_TOKEN_SECRET);
        this.token = getParameter(OAuth.OAUTH_TOKEN);
    }

    public String getToken() {
        return this.token;
    }

    public String getTokenSecret() {
        return this.tokenSecret;
    }

    void setSecretKeySpec(SecretKeySpec secretKeySpec) {
        this.secretKeySpec = secretKeySpec;
    }

    SecretKeySpec getSecretKeySpec() {
        return this.secretKeySpec;
    }

    public String getParameter(String parameter) {
        String[] arr$ = this.responseStr;
        for (String str : arr$) {
            if (str.startsWith(new StringBuffer().append(parameter).append(SignatureVisitor.INSTANCEOF).toString())) {
                String value = T4JInternalStringUtil.split(str, "=")[1].trim();
                return value;
            }
        }
        return null;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OAuthToken)) {
            return false;
        }
        OAuthToken that = (OAuthToken) o;
        return this.token.equals(that.token) && this.tokenSecret.equals(that.tokenSecret);
    }

    public int hashCode() {
        int result = this.token.hashCode();
        return (result * 31) + this.tokenSecret.hashCode();
    }

    public String toString() {
        return new StringBuffer().append("OAuthToken{token='").append(this.token).append('\'').append(", tokenSecret='").append(this.tokenSecret).append('\'').append(", secretKeySpec=").append(this.secretKeySpec).append('}').toString();
    }
}
