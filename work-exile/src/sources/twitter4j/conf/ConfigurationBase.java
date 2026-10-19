package twitter4j.conf;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import twitter4j.Version;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class ConfigurationBase implements Configuration, Serializable {
    public static final String DALVIK = "twitter4j.dalvik";
    private static final String DEFAULT_OAUTH_ACCESS_TOKEN_URL = "http://api.twitter.com/oauth/access_token";
    private static final String DEFAULT_OAUTH_AUTHENTICATION_URL = "http://api.twitter.com/oauth/authenticate";
    private static final String DEFAULT_OAUTH_AUTHORIZATION_URL = "http://api.twitter.com/oauth/authorize";
    private static final String DEFAULT_OAUTH_REQUEST_TOKEN_URL = "http://api.twitter.com/oauth/request_token";
    private static final String DEFAULT_REST_BASE_URL = "http://api.twitter.com/1/";
    private static final String DEFAULT_SEARCH_BASE_URL = "http://search.twitter.com/";
    private static final String DEFAULT_SITE_STREAM_BASE_URL = "http://sitestream.twitter.com/2b/";
    private static final String DEFAULT_STREAM_BASE_URL = "http://stream.twitter.com/1/";
    private static final String DEFAULT_USER_STREAM_BASE_URL = "https://userstream.twitter.com/2/";
    static String dalvikDetected = null;
    private static final long serialVersionUID = -6610497517837844232L;
    private boolean IS_DALVIK;
    private int asyncNumThreads;
    private String clientURL;
    private String clientVersion;
    private boolean debug;
    private int defaultMaxPerRoute;
    private String dispatcherImpl;
    private boolean gzipEnabled;
    private int httpConnectionTimeout;
    private String httpProxyHost;
    private String httpProxyPassword;
    private int httpProxyPort;
    private String httpProxyUser;
    private int httpReadTimeout;
    private int httpRetryCount;
    private int httpRetryIntervalSeconds;
    private int httpStreamingReadTimeout;
    private boolean includeEntitiesEnabled;
    private boolean includeRTsEnabled;
    private boolean jsonStoreEnabled;
    private int maxTotalConnections;
    private boolean mbeanEnabled;
    private String mediaProvider;
    private String mediaProviderAPIKey;
    private Properties mediaProviderParameters;
    private String oAuthAccessToken;
    private String oAuthAccessTokenSecret;
    private String oAuthAccessTokenURL;
    private String oAuthAuthenticationURL;
    private String oAuthAuthorizationURL;
    private String oAuthConsumerKey;
    private String oAuthConsumerSecret;
    private String oAuthRequestTokenURL;
    private String password;
    private boolean prettyDebug;
    Map<String, String> requestHeaders;
    private String restBaseURL;
    private String searchBaseURL;
    private String siteStreamBaseURL;
    private String streamBaseURL;
    private boolean useSSL;
    private String user;
    private String userAgent;
    private String userStreamBaseURL;
    private boolean userStreamRepliesAllEnabled;

    static {
        try {
            Class.forName("dalvik.system.VMRuntime");
            dalvikDetected = "true";
        } catch (ClassNotFoundException e) {
            dalvikDetected = "false";
        }
    }

    protected ConfigurationBase() {
        String isDalvik;
        setDebug(false);
        setUser(null);
        setPassword(null);
        setUseSSL(false);
        setPrettyDebugEnabled(false);
        setGZIPEnabled(true);
        setHttpProxyHost(null);
        setHttpProxyUser(null);
        setHttpProxyPassword(null);
        setHttpProxyPort(-1);
        setHttpConnectionTimeout(20000);
        setHttpReadTimeout(120000);
        setHttpStreamingReadTimeout(300000);
        setHttpRetryCount(0);
        setHttpRetryIntervalSeconds(5);
        setHttpMaxTotalConnections(20);
        setHttpDefaultMaxPerRoute(2);
        setOAuthConsumerKey(null);
        setOAuthConsumerSecret(null);
        setOAuthAccessToken(null);
        setOAuthAccessTokenSecret(null);
        setAsyncNumThreads(1);
        setClientVersion(Version.getVersion());
        setClientURL(new StringBuffer().append("http://twitter4j.org/en/twitter4j-").append(Version.getVersion()).append(".xml").toString());
        setUserAgent(new StringBuffer().append("twitter4j http://twitter4j.org/ /").append(Version.getVersion()).toString());
        setIncludeRTsEnbled(true);
        setIncludeEntitiesEnbled(false);
        setJSONStoreEnabled(false);
        setMBeanEnabled(false);
        setOAuthRequestTokenURL(DEFAULT_OAUTH_REQUEST_TOKEN_URL);
        setOAuthAuthorizationURL(DEFAULT_OAUTH_AUTHORIZATION_URL);
        setOAuthAccessTokenURL(DEFAULT_OAUTH_ACCESS_TOKEN_URL);
        setOAuthAuthenticationURL(DEFAULT_OAUTH_AUTHENTICATION_URL);
        setRestBaseURL(DEFAULT_REST_BASE_URL);
        setSearchBaseURL(DEFAULT_SEARCH_BASE_URL);
        setStreamBaseURL(DEFAULT_STREAM_BASE_URL);
        setUserStreamBaseURL(DEFAULT_USER_STREAM_BASE_URL);
        setSiteStreamBaseURL(DEFAULT_SITE_STREAM_BASE_URL);
        setDispatcherImpl("twitter4j.internal.async.DispatcherImpl");
        setIncludeRTsEnbled(true);
        setUserStreamRepliesAllEnabled(false);
        try {
            isDalvik = System.getProperty(DALVIK, dalvikDetected);
        } catch (SecurityException e) {
            isDalvik = dalvikDetected;
        }
        this.IS_DALVIK = Boolean.valueOf(isDalvik).booleanValue();
        setMediaProvider("YFROG");
        setMediaProviderAPIKey(null);
        setMediaProviderParameters(null);
    }

    @Override // twitter4j.conf.Configuration
    public final boolean isDalvik() {
        return this.IS_DALVIK;
    }

    @Override // twitter4j.conf.Configuration
    public final boolean isDebugEnabled() {
        return this.debug;
    }

    protected final void setDebug(boolean debug) {
        this.debug = debug;
    }

    @Override // twitter4j.conf.Configuration
    public final String getUserAgent() {
        return this.userAgent;
    }

    protected final void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        initRequestHeaders();
    }

    @Override // twitter4j.conf.Configuration, twitter4j.auth.AuthorizationConfiguration
    public final String getUser() {
        return this.user;
    }

    protected final void setUser(String user) {
        this.user = user;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.auth.AuthorizationConfiguration
    public final String getPassword() {
        return this.password;
    }

    protected final void setPassword(String password) {
        this.password = password;
    }

    @Override // twitter4j.internal.http.HttpClientConfiguration
    public boolean isPrettyDebugEnabled() {
        return this.prettyDebug;
    }

    protected final void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
        fixRestBaseURL();
    }

    protected final void setPrettyDebugEnabled(boolean prettyDebug) {
        this.prettyDebug = prettyDebug;
    }

    protected final void setGZIPEnabled(boolean gzipEnabled) {
        this.gzipEnabled = gzipEnabled;
        initRequestHeaders();
    }

    @Override // twitter4j.internal.http.HttpClientConfiguration
    public boolean isGZIPEnabled() {
        return this.gzipEnabled;
    }

    private void initRequestHeaders() {
        this.requestHeaders = new HashMap();
        this.requestHeaders.put("X-Twitter-Client-Version", getClientVersion());
        this.requestHeaders.put("X-Twitter-Client-URL", getClientURL());
        this.requestHeaders.put("X-Twitter-Client", "Twitter4J");
        this.requestHeaders.put("User-Agent", getUserAgent());
        if (this.gzipEnabled) {
            this.requestHeaders.put("Accept-Encoding", "gzip");
        }
        if (this.IS_DALVIK) {
            this.requestHeaders.put("Connection", "close");
        }
    }

    @Override // twitter4j.conf.Configuration, twitter4j.internal.http.HttpClientWrapperConfiguration
    public Map<String, String> getRequestHeaders() {
        return this.requestHeaders;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.internal.http.HttpClientConfiguration
    public final String getHttpProxyHost() {
        return this.httpProxyHost;
    }

    protected final void setHttpProxyHost(String proxyHost) {
        this.httpProxyHost = proxyHost;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.internal.http.HttpClientConfiguration
    public final String getHttpProxyUser() {
        return this.httpProxyUser;
    }

    protected final void setHttpProxyUser(String proxyUser) {
        this.httpProxyUser = proxyUser;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.internal.http.HttpClientConfiguration
    public final String getHttpProxyPassword() {
        return this.httpProxyPassword;
    }

    protected final void setHttpProxyPassword(String proxyPassword) {
        this.httpProxyPassword = proxyPassword;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.internal.http.HttpClientConfiguration
    public final int getHttpProxyPort() {
        return this.httpProxyPort;
    }

    protected final void setHttpProxyPort(int proxyPort) {
        this.httpProxyPort = proxyPort;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.internal.http.HttpClientConfiguration
    public final int getHttpConnectionTimeout() {
        return this.httpConnectionTimeout;
    }

    protected final void setHttpConnectionTimeout(int connectionTimeout) {
        this.httpConnectionTimeout = connectionTimeout;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.internal.http.HttpClientConfiguration
    public final int getHttpReadTimeout() {
        return this.httpReadTimeout;
    }

    protected final void setHttpReadTimeout(int readTimeout) {
        this.httpReadTimeout = readTimeout;
    }

    @Override // twitter4j.conf.Configuration
    public int getHttpStreamingReadTimeout() {
        return this.httpStreamingReadTimeout;
    }

    protected final void setHttpStreamingReadTimeout(int httpStreamingReadTimeout) {
        this.httpStreamingReadTimeout = httpStreamingReadTimeout;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.internal.http.HttpClientConfiguration
    public final int getHttpRetryCount() {
        return this.httpRetryCount;
    }

    protected final void setHttpRetryCount(int retryCount) {
        this.httpRetryCount = retryCount;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.internal.http.HttpClientConfiguration
    public final int getHttpRetryIntervalSeconds() {
        return this.httpRetryIntervalSeconds;
    }

    protected final void setHttpRetryIntervalSeconds(int retryIntervalSeconds) {
        this.httpRetryIntervalSeconds = retryIntervalSeconds;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.internal.http.HttpClientConfiguration
    public final int getHttpMaxTotalConnections() {
        return this.maxTotalConnections;
    }

    protected final void setHttpMaxTotalConnections(int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.internal.http.HttpClientConfiguration
    public final int getHttpDefaultMaxPerRoute() {
        return this.defaultMaxPerRoute;
    }

    protected final void setHttpDefaultMaxPerRoute(int defaultMaxPerRoute) {
        this.defaultMaxPerRoute = defaultMaxPerRoute;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.auth.AuthorizationConfiguration
    public final String getOAuthConsumerKey() {
        return this.oAuthConsumerKey;
    }

    protected final void setOAuthConsumerKey(String oAuthConsumerKey) {
        this.oAuthConsumerKey = oAuthConsumerKey;
        fixRestBaseURL();
    }

    @Override // twitter4j.conf.Configuration, twitter4j.auth.AuthorizationConfiguration
    public final String getOAuthConsumerSecret() {
        return this.oAuthConsumerSecret;
    }

    protected final void setOAuthConsumerSecret(String oAuthConsumerSecret) {
        this.oAuthConsumerSecret = oAuthConsumerSecret;
        fixRestBaseURL();
    }

    @Override // twitter4j.conf.Configuration, twitter4j.auth.AuthorizationConfiguration
    public String getOAuthAccessToken() {
        return this.oAuthAccessToken;
    }

    protected final void setOAuthAccessToken(String oAuthAccessToken) {
        this.oAuthAccessToken = oAuthAccessToken;
    }

    @Override // twitter4j.conf.Configuration, twitter4j.auth.AuthorizationConfiguration
    public String getOAuthAccessTokenSecret() {
        return this.oAuthAccessTokenSecret;
    }

    protected final void setOAuthAccessTokenSecret(String oAuthAccessTokenSecret) {
        this.oAuthAccessTokenSecret = oAuthAccessTokenSecret;
    }

    @Override // twitter4j.conf.Configuration
    public final int getAsyncNumThreads() {
        return this.asyncNumThreads;
    }

    protected final void setAsyncNumThreads(int asyncNumThreads) {
        this.asyncNumThreads = asyncNumThreads;
    }

    @Override // twitter4j.conf.Configuration
    public final String getClientVersion() {
        return this.clientVersion;
    }

    protected final void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
        initRequestHeaders();
    }

    @Override // twitter4j.conf.Configuration
    public final String getClientURL() {
        return this.clientURL;
    }

    protected final void setClientURL(String clientURL) {
        this.clientURL = clientURL;
        initRequestHeaders();
    }

    @Override // twitter4j.conf.Configuration
    public String getRestBaseURL() {
        return this.restBaseURL;
    }

    protected final void setRestBaseURL(String restBaseURL) {
        this.restBaseURL = restBaseURL;
        fixRestBaseURL();
    }

    private void fixRestBaseURL() {
        if (DEFAULT_REST_BASE_URL.equals(fixURL(false, this.restBaseURL))) {
            this.restBaseURL = fixURL(this.useSSL, this.restBaseURL);
        }
        if (DEFAULT_OAUTH_ACCESS_TOKEN_URL.equals(fixURL(false, this.oAuthAccessTokenURL))) {
            this.oAuthAccessTokenURL = fixURL(this.useSSL, this.oAuthAccessTokenURL);
        }
        if (DEFAULT_OAUTH_AUTHENTICATION_URL.equals(fixURL(false, this.oAuthAuthenticationURL))) {
            this.oAuthAuthenticationURL = fixURL(this.useSSL, this.oAuthAuthenticationURL);
        }
        if (DEFAULT_OAUTH_AUTHORIZATION_URL.equals(fixURL(false, this.oAuthAuthorizationURL))) {
            this.oAuthAuthorizationURL = fixURL(this.useSSL, this.oAuthAuthorizationURL);
        }
        if (DEFAULT_OAUTH_REQUEST_TOKEN_URL.equals(fixURL(false, this.oAuthRequestTokenURL))) {
            this.oAuthRequestTokenURL = fixURL(this.useSSL, this.oAuthRequestTokenURL);
        }
    }

    @Override // twitter4j.conf.Configuration
    public String getSearchBaseURL() {
        return this.searchBaseURL;
    }

    protected final void setSearchBaseURL(String searchBaseURL) {
        this.searchBaseURL = searchBaseURL;
    }

    @Override // twitter4j.conf.Configuration
    public String getStreamBaseURL() {
        return this.streamBaseURL;
    }

    protected final void setStreamBaseURL(String streamBaseURL) {
        this.streamBaseURL = streamBaseURL;
    }

    @Override // twitter4j.conf.Configuration
    public String getUserStreamBaseURL() {
        return this.userStreamBaseURL;
    }

    protected final void setUserStreamBaseURL(String siteStreamBaseURL) {
        this.userStreamBaseURL = siteStreamBaseURL;
    }

    @Override // twitter4j.conf.Configuration
    public String getSiteStreamBaseURL() {
        return this.siteStreamBaseURL;
    }

    protected final void setSiteStreamBaseURL(String siteStreamBaseURL) {
        this.siteStreamBaseURL = siteStreamBaseURL;
    }

    @Override // twitter4j.conf.Configuration
    public String getOAuthRequestTokenURL() {
        return this.oAuthRequestTokenURL;
    }

    protected final void setOAuthRequestTokenURL(String oAuthRequestTokenURL) {
        this.oAuthRequestTokenURL = oAuthRequestTokenURL;
        fixRestBaseURL();
    }

    @Override // twitter4j.conf.Configuration
    public String getOAuthAuthorizationURL() {
        return this.oAuthAuthorizationURL;
    }

    protected final void setOAuthAuthorizationURL(String oAuthAuthorizationURL) {
        this.oAuthAuthorizationURL = oAuthAuthorizationURL;
        fixRestBaseURL();
    }

    @Override // twitter4j.conf.Configuration
    public String getOAuthAccessTokenURL() {
        return this.oAuthAccessTokenURL;
    }

    protected final void setOAuthAccessTokenURL(String oAuthAccessTokenURL) {
        this.oAuthAccessTokenURL = oAuthAccessTokenURL;
        fixRestBaseURL();
    }

    @Override // twitter4j.conf.Configuration
    public String getOAuthAuthenticationURL() {
        return this.oAuthAuthenticationURL;
    }

    protected final void setOAuthAuthenticationURL(String oAuthAuthenticationURL) {
        this.oAuthAuthenticationURL = oAuthAuthenticationURL;
        fixRestBaseURL();
    }

    @Override // twitter4j.conf.Configuration
    public String getDispatcherImpl() {
        return this.dispatcherImpl;
    }

    protected final void setDispatcherImpl(String dispatcherImpl) {
        this.dispatcherImpl = dispatcherImpl;
    }

    @Override // twitter4j.conf.Configuration
    public boolean isIncludeRTsEnabled() {
        return this.includeRTsEnabled;
    }

    protected final void setIncludeRTsEnbled(boolean enabled) {
        this.includeRTsEnabled = enabled;
    }

    @Override // twitter4j.conf.Configuration
    public boolean isIncludeEntitiesEnabled() {
        return this.includeEntitiesEnabled;
    }

    protected final void setIncludeEntitiesEnbled(boolean enabled) {
        this.includeEntitiesEnabled = enabled;
    }

    @Override // twitter4j.conf.Configuration
    public boolean isJSONStoreEnabled() {
        return this.jsonStoreEnabled;
    }

    protected final void setJSONStoreEnabled(boolean enabled) {
        this.jsonStoreEnabled = enabled;
    }

    @Override // twitter4j.conf.Configuration
    public boolean isMBeanEnabled() {
        return this.mbeanEnabled;
    }

    protected final void setMBeanEnabled(boolean enabled) {
        this.mbeanEnabled = enabled;
    }

    @Override // twitter4j.conf.Configuration
    public boolean isUserStreamRepliesAllEnabled() {
        return this.userStreamRepliesAllEnabled;
    }

    protected final void setUserStreamRepliesAllEnabled(boolean enabled) {
        this.userStreamRepliesAllEnabled = enabled;
    }

    @Override // twitter4j.conf.Configuration
    public String getMediaProvider() {
        return this.mediaProvider;
    }

    protected final void setMediaProvider(String mediaProvider) {
        this.mediaProvider = mediaProvider;
    }

    @Override // twitter4j.conf.Configuration
    public String getMediaProviderAPIKey() {
        return this.mediaProviderAPIKey;
    }

    protected final void setMediaProviderAPIKey(String mediaProviderAPIKey) {
        this.mediaProviderAPIKey = mediaProviderAPIKey;
    }

    @Override // twitter4j.conf.Configuration
    public Properties getMediaProviderParameters() {
        return this.mediaProviderParameters;
    }

    protected final void setMediaProviderParameters(Properties props) {
        this.mediaProviderParameters = props;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConfigurationBase that = (ConfigurationBase) o;
        if (this.IS_DALVIK == that.IS_DALVIK && this.asyncNumThreads == that.asyncNumThreads && this.debug == that.debug && this.defaultMaxPerRoute == that.defaultMaxPerRoute && this.httpConnectionTimeout == that.httpConnectionTimeout && this.httpProxyPort == that.httpProxyPort && this.httpReadTimeout == that.httpReadTimeout && this.httpRetryCount == that.httpRetryCount && this.httpRetryIntervalSeconds == that.httpRetryIntervalSeconds && this.httpStreamingReadTimeout == that.httpStreamingReadTimeout && this.includeEntitiesEnabled == that.includeEntitiesEnabled && this.includeRTsEnabled == that.includeRTsEnabled && this.maxTotalConnections == that.maxTotalConnections && this.prettyDebug == that.prettyDebug && this.useSSL == that.useSSL && this.userStreamRepliesAllEnabled == that.userStreamRepliesAllEnabled) {
            if (this.clientURL == null ? that.clientURL != null : !this.clientURL.equals(that.clientURL)) {
                return false;
            }
            if (this.clientVersion == null ? that.clientVersion != null : !this.clientVersion.equals(that.clientVersion)) {
                return false;
            }
            if (this.dispatcherImpl == null ? that.dispatcherImpl != null : !this.dispatcherImpl.equals(that.dispatcherImpl)) {
                return false;
            }
            if (this.httpProxyHost == null ? that.httpProxyHost != null : !this.httpProxyHost.equals(that.httpProxyHost)) {
                return false;
            }
            if (this.httpProxyPassword == null ? that.httpProxyPassword != null : !this.httpProxyPassword.equals(that.httpProxyPassword)) {
                return false;
            }
            if (this.httpProxyUser == null ? that.httpProxyUser != null : !this.httpProxyUser.equals(that.httpProxyUser)) {
                return false;
            }
            if (this.mediaProvider == null ? that.mediaProvider != null : !this.mediaProvider.equals(that.mediaProvider)) {
                return false;
            }
            if (this.mediaProviderAPIKey == null ? that.mediaProviderAPIKey != null : !this.mediaProviderAPIKey.equals(that.mediaProviderAPIKey)) {
                return false;
            }
            if (this.mediaProviderParameters == null ? that.mediaProviderParameters != null : !this.mediaProviderParameters.equals(that.mediaProviderParameters)) {
                return false;
            }
            if (this.oAuthAccessToken == null ? that.oAuthAccessToken != null : !this.oAuthAccessToken.equals(that.oAuthAccessToken)) {
                return false;
            }
            if (this.oAuthAccessTokenSecret == null ? that.oAuthAccessTokenSecret != null : !this.oAuthAccessTokenSecret.equals(that.oAuthAccessTokenSecret)) {
                return false;
            }
            if (this.oAuthAccessTokenURL == null ? that.oAuthAccessTokenURL != null : !this.oAuthAccessTokenURL.equals(that.oAuthAccessTokenURL)) {
                return false;
            }
            if (this.oAuthAuthenticationURL == null ? that.oAuthAuthenticationURL != null : !this.oAuthAuthenticationURL.equals(that.oAuthAuthenticationURL)) {
                return false;
            }
            if (this.oAuthAuthorizationURL == null ? that.oAuthAuthorizationURL != null : !this.oAuthAuthorizationURL.equals(that.oAuthAuthorizationURL)) {
                return false;
            }
            if (this.oAuthConsumerKey == null ? that.oAuthConsumerKey != null : !this.oAuthConsumerKey.equals(that.oAuthConsumerKey)) {
                return false;
            }
            if (this.oAuthConsumerSecret == null ? that.oAuthConsumerSecret != null : !this.oAuthConsumerSecret.equals(that.oAuthConsumerSecret)) {
                return false;
            }
            if (this.oAuthRequestTokenURL == null ? that.oAuthRequestTokenURL != null : !this.oAuthRequestTokenURL.equals(that.oAuthRequestTokenURL)) {
                return false;
            }
            if (this.password == null ? that.password != null : !this.password.equals(that.password)) {
                return false;
            }
            if (this.requestHeaders == null ? that.requestHeaders != null : !this.requestHeaders.equals(that.requestHeaders)) {
                return false;
            }
            if (this.restBaseURL == null ? that.restBaseURL != null : !this.restBaseURL.equals(that.restBaseURL)) {
                return false;
            }
            if (this.searchBaseURL == null ? that.searchBaseURL != null : !this.searchBaseURL.equals(that.searchBaseURL)) {
                return false;
            }
            if (this.siteStreamBaseURL == null ? that.siteStreamBaseURL != null : !this.siteStreamBaseURL.equals(that.siteStreamBaseURL)) {
                return false;
            }
            if (this.streamBaseURL == null ? that.streamBaseURL != null : !this.streamBaseURL.equals(that.streamBaseURL)) {
                return false;
            }
            if (this.user == null ? that.user != null : !this.user.equals(that.user)) {
                return false;
            }
            if (this.userAgent == null ? that.userAgent != null : !this.userAgent.equals(that.userAgent)) {
                return false;
            }
            if (this.userStreamBaseURL != null) {
                if (this.userStreamBaseURL.equals(that.userStreamBaseURL)) {
                    return true;
                }
            } else if (that.userStreamBaseURL == null) {
                return true;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int result = this.debug ? 1 : 0;
        return (((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((result * 31) + (this.userAgent != null ? this.userAgent.hashCode() : 0)) * 31) + (this.user != null ? this.user.hashCode() : 0)) * 31) + (this.password != null ? this.password.hashCode() : 0)) * 31) + (this.useSSL ? 1 : 0)) * 31) + (this.prettyDebug ? 1 : 0)) * 31) + (this.httpProxyHost != null ? this.httpProxyHost.hashCode() : 0)) * 31) + (this.httpProxyUser != null ? this.httpProxyUser.hashCode() : 0)) * 31) + (this.httpProxyPassword != null ? this.httpProxyPassword.hashCode() : 0)) * 31) + this.httpProxyPort) * 31) + this.httpConnectionTimeout) * 31) + this.httpReadTimeout) * 31) + this.httpStreamingReadTimeout) * 31) + this.httpRetryCount) * 31) + this.httpRetryIntervalSeconds) * 31) + this.maxTotalConnections) * 31) + this.defaultMaxPerRoute) * 31) + (this.oAuthConsumerKey != null ? this.oAuthConsumerKey.hashCode() : 0)) * 31) + (this.oAuthConsumerSecret != null ? this.oAuthConsumerSecret.hashCode() : 0)) * 31) + (this.oAuthAccessToken != null ? this.oAuthAccessToken.hashCode() : 0)) * 31) + (this.oAuthAccessTokenSecret != null ? this.oAuthAccessTokenSecret.hashCode() : 0)) * 31) + (this.oAuthRequestTokenURL != null ? this.oAuthRequestTokenURL.hashCode() : 0)) * 31) + (this.oAuthAuthorizationURL != null ? this.oAuthAuthorizationURL.hashCode() : 0)) * 31) + (this.oAuthAccessTokenURL != null ? this.oAuthAccessTokenURL.hashCode() : 0)) * 31) + (this.oAuthAuthenticationURL != null ? this.oAuthAuthenticationURL.hashCode() : 0)) * 31) + (this.restBaseURL != null ? this.restBaseURL.hashCode() : 0)) * 31) + (this.searchBaseURL != null ? this.searchBaseURL.hashCode() : 0)) * 31) + (this.streamBaseURL != null ? this.streamBaseURL.hashCode() : 0)) * 31) + (this.userStreamBaseURL != null ? this.userStreamBaseURL.hashCode() : 0)) * 31) + (this.siteStreamBaseURL != null ? this.siteStreamBaseURL.hashCode() : 0)) * 31) + (this.dispatcherImpl != null ? this.dispatcherImpl.hashCode() : 0)) * 31) + this.asyncNumThreads) * 31) + (this.includeRTsEnabled ? 1 : 0)) * 31) + (this.userStreamRepliesAllEnabled ? 1 : 0)) * 31) + (this.clientVersion != null ? this.clientVersion.hashCode() : 0)) * 31) + (this.clientURL != null ? this.clientURL.hashCode() : 0)) * 31) + (this.IS_DALVIK ? 1 : 0)) * 31) + (this.requestHeaders != null ? this.requestHeaders.hashCode() : 0)) * 31) + (this.mediaProvider != null ? this.mediaProvider.hashCode() : 0)) * 31) + (this.mediaProviderAPIKey != null ? this.mediaProviderAPIKey.hashCode() : 0)) * 31) + (this.mediaProviderParameters != null ? this.mediaProviderParameters.hashCode() : 0);
    }

    public String toString() {
        return new StringBuffer().append("ConfigurationBase{debug=").append(this.debug).append(", userAgent='").append(this.userAgent).append('\'').append(", user='").append(this.user).append('\'').append(", password='").append(this.password).append('\'').append(", useSSL=").append(this.useSSL).append(", httpProxyHost='").append(this.httpProxyHost).append('\'').append(", httpProxyUser='").append(this.httpProxyUser).append('\'').append(", httpProxyPassword='").append(this.httpProxyPassword).append('\'').append(", httpProxyPort=").append(this.httpProxyPort).append(", httpConnectionTimeout=").append(this.httpConnectionTimeout).append(", httpReadTimeout=").append(this.httpReadTimeout).append(", httpStreamingReadTimeout=").append(this.httpStreamingReadTimeout).append(", httpRetryCount=").append(this.httpRetryCount).append(", httpRetryIntervalSeconds=").append(this.httpRetryIntervalSeconds).append(", maxTotalConnections=").append(this.maxTotalConnections).append(", defaultMaxPerRoute=").append(this.defaultMaxPerRoute).append(", oAuthConsumerKey='").append(this.oAuthConsumerKey).append('\'').append(", oAuthConsumerSecret='").append(this.oAuthConsumerSecret).append('\'').append(", oAuthAccessToken='").append(this.oAuthAccessToken).append('\'').append(", oAuthAccessTokenSecret='").append(this.oAuthAccessTokenSecret).append('\'').append(", oAuthRequestTokenURL='").append(this.oAuthRequestTokenURL).append('\'').append(", oAuthAuthorizationURL='").append(this.oAuthAuthorizationURL).append('\'').append(", oAuthAccessTokenURL='").append(this.oAuthAccessTokenURL).append('\'').append(", oAuthAuthenticationURL='").append(this.oAuthAuthenticationURL).append('\'').append(", restBaseURL='").append(this.restBaseURL).append('\'').append(", searchBaseURL='").append(this.searchBaseURL).append('\'').append(", streamBaseURL='").append(this.streamBaseURL).append('\'').append(", userStreamBaseURL='").append(this.userStreamBaseURL).append('\'').append(", siteStreamBaseURL='").append(this.siteStreamBaseURL).append('\'').append(", dispatcherImpl='").append(this.dispatcherImpl).append('\'').append(", asyncNumThreads=").append(this.asyncNumThreads).append(", includeRTsEnabled=").append(this.includeRTsEnabled).append(", includeEntitiesEnabled=").append(this.includeEntitiesEnabled).append(", userStreamRepliesAllEnabled=").append(this.userStreamRepliesAllEnabled).append(", clientVersion='").append(this.clientVersion).append('\'').append(", clientURL='").append(this.clientURL).append('\'').append(", IS_DALVIK=").append(this.IS_DALVIK).append(", requestHeaders=").append(this.requestHeaders).append(", mediaProvider=").append(this.mediaProvider).append(", mediaProviderAPIKey=").append(this.mediaProviderAPIKey).append(", mediaProviderParameters=").append(this.mediaProviderParameters).append('}').toString();
    }

    static String fixURL(boolean useSSL, String url) {
        if (url == null) {
            return null;
        }
        int index = url.indexOf("://");
        if (-1 == index) {
            throw new IllegalArgumentException("url should contain '://'");
        }
        String hostAndLater = url.substring(index + 3);
        if (useSSL) {
            return new StringBuffer().append("https://").append(hostAndLater).toString();
        }
        return new StringBuffer().append("http://").append(hostAndLater).toString();
    }
}
