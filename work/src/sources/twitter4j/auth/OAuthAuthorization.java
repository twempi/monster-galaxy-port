package twitter4j.auth;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import oauth.signpost.OAuth;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.BASE64Encoder;
import twitter4j.internal.http.HttpClientWrapper;
import twitter4j.internal.http.HttpParameter;
import twitter4j.internal.http.HttpRequest;
import twitter4j.internal.logging.Logger;
import twitter4j.internal.util.T4JInternalStringUtil;

/* JADX INFO: loaded from: classes.dex */
public class OAuthAuthorization implements Authorization, Serializable, OAuthSupport {
    private static final String HMAC_SHA1 = "HmacSHA1";
    private static final HttpParameter OAUTH_SIGNATURE_METHOD = new HttpParameter(OAuth.OAUTH_SIGNATURE_METHOD, "HMAC-SHA1");
    private static Random RAND = null;
    static Class class$twitter4j$auth$OAuthAuthorization = null;
    private static transient HttpClientWrapper http = null;
    private static final Logger logger;
    private static final long serialVersionUID = -4368426677157998618L;
    private final Configuration conf;
    private String consumerSecret;
    private String consumerKey = "";
    private String realm = null;
    private OAuthToken oauthToken = null;

    static {
        Class clsClass$;
        if (class$twitter4j$auth$OAuthAuthorization == null) {
            clsClass$ = class$("twitter4j.auth.OAuthAuthorization");
            class$twitter4j$auth$OAuthAuthorization = clsClass$;
        } else {
            clsClass$ = class$twitter4j$auth$OAuthAuthorization;
        }
        logger = Logger.getLogger(clsClass$);
        RAND = new Random();
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public OAuthAuthorization(Configuration conf) {
        this.conf = conf;
        http = new HttpClientWrapper(conf);
        setOAuthConsumer(conf.getOAuthConsumerKey(), conf.getOAuthConsumerSecret());
        if (conf.getOAuthAccessToken() != null && conf.getOAuthAccessTokenSecret() != null) {
            setOAuthAccessToken(new AccessToken(conf.getOAuthAccessToken(), conf.getOAuthAccessTokenSecret()));
        }
    }

    @Override // twitter4j.auth.Authorization
    public String getAuthorizationHeader(HttpRequest req) {
        return generateAuthorizationHeader(req.getMethod().name(), req.getURL(), req.getParameters(), this.oauthToken);
    }

    private void ensureTokenIsAvailable() {
        if (this.oauthToken == null) {
            throw new IllegalStateException("No Token available.");
        }
    }

    @Override // twitter4j.auth.Authorization
    public boolean isEnabled() {
        return this.oauthToken != null && (this.oauthToken instanceof AccessToken);
    }

    @Override // twitter4j.auth.OAuthSupport
    public RequestToken getOAuthRequestToken() throws TwitterException {
        return getOAuthRequestToken(null, null);
    }

    @Override // twitter4j.auth.OAuthSupport
    public RequestToken getOAuthRequestToken(String callbackURL) throws TwitterException {
        return getOAuthRequestToken(callbackURL, null);
    }

    @Override // twitter4j.auth.OAuthSupport
    public RequestToken getOAuthRequestToken(String callbackURL, String xAuthAccessType) throws TwitterException {
        if (this.oauthToken instanceof AccessToken) {
            throw new IllegalStateException("Access token already available.");
        }
        List<HttpParameter> params = new ArrayList<>();
        if (callbackURL != null) {
            params.add(new HttpParameter(OAuth.OAUTH_CALLBACK, callbackURL));
        }
        if (xAuthAccessType != null) {
            params.add(new HttpParameter("x_auth_access_type", xAuthAccessType));
        }
        this.oauthToken = new RequestToken(http.post(this.conf.getOAuthRequestTokenURL(), (HttpParameter[]) params.toArray(new HttpParameter[params.size()]), this), this);
        return (RequestToken) this.oauthToken;
    }

    @Override // twitter4j.auth.OAuthSupport
    public AccessToken getOAuthAccessToken() throws TwitterException {
        ensureTokenIsAvailable();
        if (this.oauthToken instanceof AccessToken) {
            return (AccessToken) this.oauthToken;
        }
        this.oauthToken = new AccessToken(http.post(this.conf.getOAuthAccessTokenURL(), this));
        return (AccessToken) this.oauthToken;
    }

    @Override // twitter4j.auth.OAuthSupport
    public AccessToken getOAuthAccessToken(String oauthVerifier) throws TwitterException {
        ensureTokenIsAvailable();
        this.oauthToken = new AccessToken(http.post(this.conf.getOAuthAccessTokenURL(), new HttpParameter[]{new HttpParameter(OAuth.OAUTH_VERIFIER, oauthVerifier)}, this));
        return (AccessToken) this.oauthToken;
    }

    @Override // twitter4j.auth.OAuthSupport
    public AccessToken getOAuthAccessToken(RequestToken requestToken) throws TwitterException {
        this.oauthToken = requestToken;
        return getOAuthAccessToken();
    }

    @Override // twitter4j.auth.OAuthSupport
    public AccessToken getOAuthAccessToken(RequestToken requestToken, String oauthVerifier) throws TwitterException {
        this.oauthToken = requestToken;
        return getOAuthAccessToken(oauthVerifier);
    }

    @Override // twitter4j.auth.OAuthSupport
    public AccessToken getOAuthAccessToken(String screenName, String password) throws TwitterException {
        try {
            String url = this.conf.getOAuthAccessTokenURL();
            if (url.indexOf("http://") == 0) {
                url = new StringBuffer().append("https://").append(url.substring(7)).toString();
            }
            this.oauthToken = new AccessToken(http.post(url, new HttpParameter[]{new HttpParameter("x_auth_username", screenName), new HttpParameter("x_auth_password", password), new HttpParameter("x_auth_mode", "client_auth")}, this));
            return (AccessToken) this.oauthToken;
        } catch (TwitterException te) {
            throw new TwitterException("The screen name / password combination seems to be invalid.", te, te.getStatusCode());
        }
    }

    @Override // twitter4j.auth.OAuthSupport
    public void setOAuthAccessToken(AccessToken accessToken) {
        this.oauthToken = accessToken;
    }

    public void setOAuthRealm(String realm) {
        this.realm = realm;
    }

    String generateAuthorizationHeader(String method, String url, HttpParameter[] params, String nonce, String timestamp, OAuthToken otoken) {
        if (params == null) {
            params = new HttpParameter[0];
        }
        ArrayList arrayList = new ArrayList(5);
        arrayList.add(new HttpParameter(OAuth.OAUTH_CONSUMER_KEY, this.consumerKey));
        arrayList.add(OAUTH_SIGNATURE_METHOD);
        arrayList.add(new HttpParameter(OAuth.OAUTH_TIMESTAMP, timestamp));
        arrayList.add(new HttpParameter(OAuth.OAUTH_NONCE, nonce));
        arrayList.add(new HttpParameter(OAuth.OAUTH_VERSION, OAuth.VERSION_1_0));
        if (otoken != null) {
            arrayList.add(new HttpParameter(OAuth.OAUTH_TOKEN, otoken.getToken()));
        }
        List<HttpParameter> signatureBaseParams = new ArrayList<>(arrayList.size() + params.length);
        signatureBaseParams.addAll(arrayList);
        if (!HttpParameter.containsFile(params)) {
            signatureBaseParams.addAll(toParamList(params));
        }
        parseGetParameters(url, signatureBaseParams);
        StringBuffer base = new StringBuffer(method).append("&").append(HttpParameter.encode(constructRequestURL(url))).append("&");
        base.append(HttpParameter.encode(normalizeRequestParameters(signatureBaseParams)));
        String oauthBaseString = base.toString();
        logger.debug("OAuth base string: ", oauthBaseString);
        String signature = generateSignature(oauthBaseString, otoken);
        logger.debug("OAuth signature: ", signature);
        arrayList.add(new HttpParameter(OAuth.OAUTH_SIGNATURE, signature));
        if (this.realm != null) {
            arrayList.add(new HttpParameter("realm", this.realm));
        }
        return new StringBuffer().append("OAuth ").append(encodeParameters(arrayList, ",", true)).toString();
    }

    private void parseGetParameters(String url, List<HttpParameter> signatureBaseParams) {
        int queryStart = url.indexOf("?");
        if (-1 != queryStart) {
            String[] queryStrs = T4JInternalStringUtil.split(url.substring(queryStart + 1), "&");
            try {
                for (String query : queryStrs) {
                    String[] split = T4JInternalStringUtil.split(query, "=");
                    if (split.length == 2) {
                        signatureBaseParams.add(new HttpParameter(URLDecoder.decode(split[0], OAuth.ENCODING), URLDecoder.decode(split[1], OAuth.ENCODING)));
                    } else {
                        signatureBaseParams.add(new HttpParameter(URLDecoder.decode(split[0], OAuth.ENCODING), ""));
                    }
                }
            } catch (UnsupportedEncodingException e) {
            }
        }
    }

    String generateAuthorizationHeader(String method, String url, HttpParameter[] params, OAuthToken token) {
        long timestamp = System.currentTimeMillis() / 1000;
        long nonce = timestamp + ((long) RAND.nextInt());
        return generateAuthorizationHeader(method, url, params, String.valueOf(nonce), String.valueOf(timestamp), token);
    }

    public List<HttpParameter> generateOAuthSignatureHttpParams(String method, String url) {
        long timestamp = System.currentTimeMillis() / 1000;
        long nonce = timestamp + ((long) RAND.nextInt());
        ArrayList arrayList = new ArrayList(5);
        arrayList.add(new HttpParameter(OAuth.OAUTH_CONSUMER_KEY, this.consumerKey));
        arrayList.add(OAUTH_SIGNATURE_METHOD);
        arrayList.add(new HttpParameter(OAuth.OAUTH_TIMESTAMP, timestamp));
        arrayList.add(new HttpParameter(OAuth.OAUTH_NONCE, nonce));
        arrayList.add(new HttpParameter(OAuth.OAUTH_VERSION, OAuth.VERSION_1_0));
        if (this.oauthToken != null) {
            arrayList.add(new HttpParameter(OAuth.OAUTH_TOKEN, this.oauthToken.getToken()));
        }
        List<HttpParameter> signatureBaseParams = new ArrayList<>(arrayList.size());
        signatureBaseParams.addAll(arrayList);
        parseGetParameters(url, signatureBaseParams);
        StringBuffer base = new StringBuffer(method).append("&").append(HttpParameter.encode(constructRequestURL(url))).append("&");
        base.append(HttpParameter.encode(normalizeRequestParameters(signatureBaseParams)));
        String oauthBaseString = base.toString();
        String signature = generateSignature(oauthBaseString, this.oauthToken);
        arrayList.add(new HttpParameter(OAuth.OAUTH_SIGNATURE, signature));
        return arrayList;
    }

    String generateSignature(String data, OAuthToken token) {
        SecretKeySpec spec;
        try {
            Mac mac = Mac.getInstance(HMAC_SHA1);
            if (token == null) {
                String oauthSignature = new StringBuffer().append(HttpParameter.encode(this.consumerSecret)).append("&").toString();
                spec = new SecretKeySpec(oauthSignature.getBytes(), HMAC_SHA1);
            } else {
                spec = token.getSecretKeySpec();
                if (spec == null) {
                    String oauthSignature2 = new StringBuffer().append(HttpParameter.encode(this.consumerSecret)).append("&").append(HttpParameter.encode(token.getTokenSecret())).toString();
                    spec = new SecretKeySpec(oauthSignature2.getBytes(), HMAC_SHA1);
                    token.setSecretKeySpec(spec);
                }
            }
            mac.init(spec);
            byte[] byteHMAC = mac.doFinal(data.getBytes());
            return BASE64Encoder.encode(byteHMAC);
        } catch (InvalidKeyException ike) {
            logger.error("Failed initialize \"Message Authentication Code\" (MAC)", ike);
            throw new AssertionError(ike);
        } catch (NoSuchAlgorithmException nsae) {
            logger.error("Failed to get HmacSHA1 \"Message Authentication Code\" (MAC)", nsae);
            throw new AssertionError(nsae);
        }
    }

    String generateSignature(String data) {
        return generateSignature(data, null);
    }

    public static String normalizeRequestParameters(HttpParameter[] params) {
        return normalizeRequestParameters(toParamList(params));
    }

    public static String normalizeRequestParameters(List<HttpParameter> params) {
        Collections.sort(params);
        return encodeParameters(params);
    }

    public static String normalizeAuthorizationHeaders(List<HttpParameter> params) {
        Collections.sort(params);
        return encodeParameters(params);
    }

    public static List<HttpParameter> toParamList(HttpParameter[] params) {
        List<HttpParameter> paramList = new ArrayList<>(params.length);
        paramList.addAll(Arrays.asList(params));
        return paramList;
    }

    public static String encodeParameters(List<HttpParameter> httpParams) {
        return encodeParameters(httpParams, "&", false);
    }

    public static String encodeParameters(List<HttpParameter> httpParams, String splitter, boolean quot) {
        StringBuffer buf = new StringBuffer();
        for (HttpParameter param : httpParams) {
            if (!param.isFile()) {
                if (buf.length() != 0) {
                    if (quot) {
                        buf.append("\"");
                    }
                    buf.append(splitter);
                }
                buf.append(HttpParameter.encode(param.getName())).append("=");
                if (quot) {
                    buf.append("\"");
                }
                buf.append(HttpParameter.encode(param.getValue()));
            }
        }
        if (buf.length() != 0 && quot) {
            buf.append("\"");
        }
        return buf.toString();
    }

    public static String constructRequestURL(String url) {
        int index = url.indexOf("?");
        if (-1 != index) {
            url = url.substring(0, index);
        }
        int slashIndex = url.indexOf("/", 8);
        String baseURL = url.substring(0, slashIndex).toLowerCase();
        int colonIndex = baseURL.indexOf(":", 8);
        if (-1 != colonIndex) {
            if (baseURL.startsWith("http://") && baseURL.endsWith(":80")) {
                baseURL = baseURL.substring(0, colonIndex);
            } else if (baseURL.startsWith("https://") && baseURL.endsWith(":443")) {
                baseURL = baseURL.substring(0, colonIndex);
            }
        }
        return new StringBuffer().append(baseURL).append(url.substring(slashIndex)).toString();
    }

    @Override // twitter4j.auth.OAuthSupport
    public void setOAuthConsumer(String consumerKey, String consumerSecret) {
        if (consumerKey == null) {
            consumerKey = "";
        }
        this.consumerKey = consumerKey;
        if (consumerSecret == null) {
            consumerSecret = "";
        }
        this.consumerSecret = consumerSecret;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OAuthSupport)) {
            return false;
        }
        OAuthAuthorization that = (OAuthAuthorization) o;
        if (this.consumerKey == null ? that.consumerKey != null : !this.consumerKey.equals(that.consumerKey)) {
            return false;
        }
        if (this.consumerSecret == null ? that.consumerSecret != null : !this.consumerSecret.equals(that.consumerSecret)) {
            return false;
        }
        if (this.oauthToken != null) {
            if (this.oauthToken.equals(that.oauthToken)) {
                return true;
            }
        } else if (that.oauthToken == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.consumerKey != null ? this.consumerKey.hashCode() : 0;
        return (((result * 31) + (this.consumerSecret != null ? this.consumerSecret.hashCode() : 0)) * 31) + (this.oauthToken != null ? this.oauthToken.hashCode() : 0);
    }

    public String toString() {
        return new StringBuffer().append("OAuthAuthorization{consumerKey='").append(this.consumerKey).append('\'').append(", consumerSecret='******************************************'").append(", oauthToken=").append(this.oauthToken).append('}').toString();
    }
}
