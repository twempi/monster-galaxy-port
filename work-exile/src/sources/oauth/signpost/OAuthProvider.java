package oauth.signpost;

import java.io.Serializable;
import java.util.Map;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import oauth.signpost.http.HttpParameters;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface OAuthProvider extends Serializable {
    String getAccessTokenEndpointUrl();

    String getAuthorizationWebsiteUrl();

    @Deprecated
    Map<String, String> getRequestHeaders();

    String getRequestTokenEndpointUrl();

    HttpParameters getResponseParameters();

    boolean isOAuth10a();

    void removeListener(OAuthProviderListener oAuthProviderListener);

    void retrieveAccessToken(OAuthConsumer oAuthConsumer, String str) throws OAuthExpectationFailedException, OAuthNotAuthorizedException, OAuthMessageSignerException, OAuthCommunicationException;

    String retrieveRequestToken(OAuthConsumer oAuthConsumer, String str) throws OAuthExpectationFailedException, OAuthNotAuthorizedException, OAuthMessageSignerException, OAuthCommunicationException;

    void setListener(OAuthProviderListener oAuthProviderListener);

    void setOAuth10a(boolean z);

    @Deprecated
    void setRequestHeader(String str, String str2);

    void setResponseParameters(HttpParameters httpParameters);
}
