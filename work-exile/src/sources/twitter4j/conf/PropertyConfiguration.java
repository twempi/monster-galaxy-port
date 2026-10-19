package twitter4j.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import twitter4j.internal.util.T4JInternalStringUtil;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public final class PropertyConfiguration extends ConfigurationBase implements Serializable {
    public static final String ASYNC_DISPATCHER_IMPL = "async.dispatcherImpl";
    public static final String ASYNC_NUM_THREADS = "async.numThreads";
    public static final String CLIENT_URL = "clientURL";
    public static final String CLIENT_VERSION = "clientVersion";
    public static final String DEBUG = "debug";
    public static final String HTTP_CONNECTION_TIMEOUT = "http.connectionTimeout";
    public static final String HTTP_DEFAULT_MAX_PER_ROUTE = "http.defaultMaxPerRoute";
    public static final String HTTP_GZIP = "http.gzip";
    public static final String HTTP_MAX_TOTAL_CONNECTIONS = "http.maxTotalConnections";
    public static final String HTTP_PRETTY_DEBUG = "http.prettyDebug";
    public static final String HTTP_PROXY_HOST = "http.proxyHost";
    public static final String HTTP_PROXY_HOST_FALLBACK = "http.proxyHost";
    public static final String HTTP_PROXY_PASSWORD = "http.proxyPassword";
    public static final String HTTP_PROXY_PORT = "http.proxyPort";
    public static final String HTTP_PROXY_PORT_FALLBACK = "http.proxyPort";
    public static final String HTTP_PROXY_USER = "http.proxyUser";
    public static final String HTTP_READ_TIMEOUT = "http.readTimeout";
    public static final String HTTP_RETRY_COUNT = "http.retryCount";
    public static final String HTTP_RETRY_INTERVAL_SECS = "http.retryIntervalSecs";
    public static final String HTTP_STREAMING_READ_TIMEOUT = "http.streamingReadTimeout";
    public static final String HTTP_USER_AGENT = "http.userAgent";
    public static final String HTTP_USE_SSL = "http.useSSL";
    public static final String INCLUDE_ENTITIES = "includeEntities";
    public static final String INCLUDE_RTS = "includeRTs";
    public static final String JSON_STORE_ENABLED = "jsonStoreEnabled";
    public static final String MBEAN_ENABLED = "mbeanEnabled";
    public static final String MEDIA_PROVIDER = "media.provider";
    public static final String MEDIA_PROVIDER_API_KEY = "media.providerAPIKey";
    public static final String MEDIA_PROVIDER_PARAMETERS = "media.providerParameters";
    public static final String OAUTH_ACCESS_TOKEN = "oauth.accessToken";
    public static final String OAUTH_ACCESS_TOKEN_SECRET = "oauth.accessTokenSecret";
    public static final String OAUTH_ACCESS_TOKEN_URL = "oauth.accessTokenURL";
    public static final String OAUTH_AUTHENTICATION_URL = "oauth.authenticationURL";
    public static final String OAUTH_AUTHORIZATION_URL = "oauth.authorizationURL";
    public static final String OAUTH_CONSUMER_KEY = "oauth.consumerKey";
    public static final String OAUTH_CONSUMER_SECRET = "oauth.consumerSecret";
    public static final String OAUTH_REQUEST_TOKEN_URL = "oauth.requestTokenURL";
    public static final String PASSWORD = "password";
    public static final String REST_BASE_URL = "restBaseURL";
    public static final String SEARCH_BASE_URL = "searchBaseURL";
    public static final String SITE_STREAM_BASE_URL = "siteStreamBaseURL";
    public static final String STREAM_BASE_URL = "streamBaseURL";
    public static final String STREAM_USER_REPLIES_ALL = "stream.user.repliesAll";
    public static final String USER = "user";
    public static final String USER_STREAM_BASE_URL = "userStreamBaseURL";
    static Class class$twitter4j$conf$Configuration = null;
    private static final long serialVersionUID = 6458764415636588373L;

    @Override // twitter4j.conf.ConfigurationBase
    public boolean equals(Object x0) {
        return super.equals(x0);
    }

    @Override // twitter4j.conf.ConfigurationBase, twitter4j.conf.Configuration
    public String getDispatcherImpl() {
        return super.getDispatcherImpl();
    }

    @Override // twitter4j.conf.ConfigurationBase, twitter4j.conf.Configuration
    public int getHttpStreamingReadTimeout() {
        return super.getHttpStreamingReadTimeout();
    }

    @Override // twitter4j.conf.ConfigurationBase, twitter4j.conf.Configuration
    public String getMediaProvider() {
        return super.getMediaProvider();
    }

    @Override // twitter4j.conf.ConfigurationBase, twitter4j.conf.Configuration
    public String getMediaProviderAPIKey() {
        return super.getMediaProviderAPIKey();
    }

    @Override // twitter4j.conf.ConfigurationBase, twitter4j.conf.Configuration
    public Properties getMediaProviderParameters() {
        return super.getMediaProviderParameters();
    }

    @Override // twitter4j.conf.ConfigurationBase, twitter4j.conf.Configuration, twitter4j.auth.AuthorizationConfiguration
    public String getOAuthAccessToken() {
        return super.getOAuthAccessToken();
    }

    @Override // twitter4j.conf.ConfigurationBase, twitter4j.conf.Configuration, twitter4j.auth.AuthorizationConfiguration
    public String getOAuthAccessTokenSecret() {
        return super.getOAuthAccessTokenSecret();
    }

    @Override // twitter4j.conf.ConfigurationBase, twitter4j.conf.Configuration
    public String getOAuthAccessTokenURL() {
        return super.getOAuthAccessTokenURL();
    }

    @Override // twitter4j.conf.ConfigurationBase, twitter4j.conf.Configuration
    public String getOAuthAuthenticationURL() {
        return super.getOAuthAuthenticationURL();
    }

    @Override // twitter4j.conf.ConfigurationBase, twitter4j.conf.Configuration
    public String getOAuthAuthorizationURL() {
        return super.getOAuthAuthorizationURL();
    }

    @Override // twitter4j.conf.ConfigurationBase, twitter4j.conf.Configuration
    public String getOAuthRequestTokenURL() {
        return super.getOAuthRequestTokenURL();
    }

    @Override // twitter4j.conf.ConfigurationBase, twitter4j.conf.Configuration, twitter4j.internal.http.HttpClientWrapperConfiguration
    public Map getRequestHeaders() {
        return super.getRequestHeaders();
    }

    @Override // twitter4j.conf.ConfigurationBase, twitter4j.conf.Configuration
    public String getRestBaseURL() {
        return super.getRestBaseURL();
    }

    @Override // twitter4j.conf.ConfigurationBase, twitter4j.conf.Configuration
    public String getSearchBaseURL() {
        return super.getSearchBaseURL();
    }

    @Override // twitter4j.conf.ConfigurationBase, twitter4j.conf.Configuration
    public String getSiteStreamBaseURL() {
        return super.getSiteStreamBaseURL();
    }

    @Override // twitter4j.conf.ConfigurationBase, twitter4j.conf.Configuration
    public String getStreamBaseURL() {
        return super.getStreamBaseURL();
    }

    @Override // twitter4j.conf.ConfigurationBase, twitter4j.conf.Configuration
    public String getUserStreamBaseURL() {
        return super.getUserStreamBaseURL();
    }

    @Override // twitter4j.conf.ConfigurationBase
    public int hashCode() {
        return super.hashCode();
    }

    @Override // twitter4j.conf.ConfigurationBase, twitter4j.internal.http.HttpClientConfiguration
    public boolean isGZIPEnabled() {
        return super.isGZIPEnabled();
    }

    @Override // twitter4j.conf.ConfigurationBase, twitter4j.conf.Configuration
    public boolean isIncludeEntitiesEnabled() {
        return super.isIncludeEntitiesEnabled();
    }

    @Override // twitter4j.conf.ConfigurationBase, twitter4j.conf.Configuration
    public boolean isIncludeRTsEnabled() {
        return super.isIncludeRTsEnabled();
    }

    @Override // twitter4j.conf.ConfigurationBase, twitter4j.conf.Configuration
    public boolean isJSONStoreEnabled() {
        return super.isJSONStoreEnabled();
    }

    @Override // twitter4j.conf.ConfigurationBase, twitter4j.conf.Configuration
    public boolean isMBeanEnabled() {
        return super.isMBeanEnabled();
    }

    @Override // twitter4j.conf.ConfigurationBase, twitter4j.internal.http.HttpClientConfiguration
    public boolean isPrettyDebugEnabled() {
        return super.isPrettyDebugEnabled();
    }

    @Override // twitter4j.conf.ConfigurationBase, twitter4j.conf.Configuration
    public boolean isUserStreamRepliesAllEnabled() {
        return super.isUserStreamRepliesAllEnabled();
    }

    @Override // twitter4j.conf.ConfigurationBase
    public String toString() {
        return super.toString();
    }

    public PropertyConfiguration(InputStream is) {
        Properties props = new Properties();
        loadProperties(props, is);
        setFieldsWithTreePath(props, "/");
    }

    public PropertyConfiguration(Properties props) {
        this(props, "/");
    }

    public PropertyConfiguration(Properties props, String treePath) {
        setFieldsWithTreePath(props, treePath);
    }

    PropertyConfiguration(String treePath) throws Throwable {
        Properties props;
        Class clsClass$;
        Class clsClass$2;
        try {
            props = (Properties) System.getProperties().clone();
            normalize(props);
        } catch (SecurityException e) {
            props = new Properties();
        }
        loadProperties(props, new StringBuffer().append(".").append(File.separatorChar).append("twitter4j.properties").toString());
        if (class$twitter4j$conf$Configuration == null) {
            clsClass$ = class$("twitter4j.conf.Configuration");
            class$twitter4j$conf$Configuration = clsClass$;
        } else {
            clsClass$ = class$twitter4j$conf$Configuration;
        }
        loadProperties(props, clsClass$.getResourceAsStream("/twitter4j.properties"));
        if (class$twitter4j$conf$Configuration == null) {
            clsClass$2 = class$("twitter4j.conf.Configuration");
            class$twitter4j$conf$Configuration = clsClass$2;
        } else {
            clsClass$2 = class$twitter4j$conf$Configuration;
        }
        loadProperties(props, clsClass$2.getResourceAsStream("/WEB-INF/twitter4j.properties"));
        setFieldsWithTreePath(props, treePath);
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    PropertyConfiguration() {
        this("/");
    }

    private boolean notNull(Properties props, String prefix, String name) {
        return props.getProperty(new StringBuffer().append(prefix).append(name).toString()) != null;
    }

    private boolean loadProperties(Properties props, String path) throws Throwable {
        FileInputStream fis = null;
        try {
            File file = new File(path);
            if (!file.exists() || !file.isFile()) {
                if (0 != 0) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                    }
                }
                return false;
            }
            FileInputStream fis2 = new FileInputStream(file);
            try {
                props.load(fis2);
                normalize(props);
                if (fis2 != null) {
                    try {
                        fis2.close();
                    } catch (IOException e2) {
                    }
                }
                return true;
            } catch (Exception e3) {
                fis = fis2;
            } catch (Throwable th) {
                th = th;
                fis = fis2;
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e4) {
                    }
                }
                throw th;
            }
        } catch (Exception e5) {
        } catch (Throwable th2) {
            th = th2;
        }
        if (fis != null) {
            try {
                fis.close();
            } catch (IOException e6) {
            }
        }
        return false;
    }

    private boolean loadProperties(Properties props, InputStream is) {
        try {
            props.load(is);
            normalize(props);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void normalize(Properties props) {
        Set keys = props.keySet();
        ArrayList<String> toBeNormalized = new ArrayList<>(10);
        for (Object key : keys) {
            String keyStr = (String) key;
            if (-1 != keyStr.indexOf("twitter4j.")) {
                toBeNormalized.add(keyStr);
            }
        }
        for (String keyStr2 : toBeNormalized) {
            String property = props.getProperty(keyStr2);
            int index = keyStr2.indexOf("twitter4j.");
            String newKey = new StringBuffer().append(keyStr2.substring(0, index)).append(keyStr2.substring(index + 10)).toString();
            props.setProperty(newKey, property);
        }
    }

    private void setFieldsWithTreePath(Properties props, String treePath) {
        setFieldsWithPrefix(props, "");
        String[] splitArray = T4JInternalStringUtil.split(treePath, "/");
        String prefix = null;
        for (String split : splitArray) {
            if (!"".equals(split)) {
                if (prefix == null) {
                    prefix = new StringBuffer().append(split).append(".").toString();
                } else {
                    prefix = new StringBuffer().append(prefix).append(split).append(".").toString();
                }
                setFieldsWithPrefix(props, prefix);
            }
        }
    }

    private void setFieldsWithPrefix(Properties props, String prefix) {
        if (notNull(props, prefix, DEBUG)) {
            setDebug(getBoolean(props, prefix, DEBUG));
        }
        if (notNull(props, prefix, USER)) {
            setUser(getString(props, prefix, USER));
        }
        if (notNull(props, prefix, PASSWORD)) {
            setPassword(getString(props, prefix, PASSWORD));
        }
        if (notNull(props, prefix, HTTP_USE_SSL)) {
            setUseSSL(getBoolean(props, prefix, HTTP_USE_SSL));
        }
        if (notNull(props, prefix, HTTP_PRETTY_DEBUG)) {
            setPrettyDebugEnabled(getBoolean(props, prefix, HTTP_PRETTY_DEBUG));
        }
        if (notNull(props, prefix, HTTP_GZIP)) {
            setGZIPEnabled(getBoolean(props, prefix, HTTP_GZIP));
        }
        if (notNull(props, prefix, "http.proxyHost") || notNull(props, prefix, "http.proxyHost")) {
            setHttpProxyHost(getString(props, prefix, "http.proxyHost"));
        }
        if (notNull(props, prefix, HTTP_PROXY_USER)) {
            setHttpProxyUser(getString(props, prefix, HTTP_PROXY_USER));
        }
        if (notNull(props, prefix, HTTP_PROXY_PASSWORD)) {
            setHttpProxyPassword(getString(props, prefix, HTTP_PROXY_PASSWORD));
        }
        if (notNull(props, prefix, "http.proxyPort") || notNull(props, prefix, "http.proxyPort")) {
            setHttpProxyPort(getIntProperty(props, prefix, "http.proxyPort"));
        }
        if (notNull(props, prefix, HTTP_CONNECTION_TIMEOUT)) {
            setHttpConnectionTimeout(getIntProperty(props, prefix, HTTP_CONNECTION_TIMEOUT));
        }
        if (notNull(props, prefix, HTTP_READ_TIMEOUT)) {
            setHttpReadTimeout(getIntProperty(props, prefix, HTTP_READ_TIMEOUT));
        }
        if (notNull(props, prefix, HTTP_STREAMING_READ_TIMEOUT)) {
            setHttpStreamingReadTimeout(getIntProperty(props, prefix, HTTP_STREAMING_READ_TIMEOUT));
        }
        if (notNull(props, prefix, HTTP_RETRY_COUNT)) {
            setHttpRetryCount(getIntProperty(props, prefix, HTTP_RETRY_COUNT));
        }
        if (notNull(props, prefix, HTTP_RETRY_INTERVAL_SECS)) {
            setHttpRetryIntervalSeconds(getIntProperty(props, prefix, HTTP_RETRY_INTERVAL_SECS));
        }
        if (notNull(props, prefix, HTTP_MAX_TOTAL_CONNECTIONS)) {
            setHttpMaxTotalConnections(getIntProperty(props, prefix, HTTP_MAX_TOTAL_CONNECTIONS));
        }
        if (notNull(props, prefix, HTTP_DEFAULT_MAX_PER_ROUTE)) {
            setHttpDefaultMaxPerRoute(getIntProperty(props, prefix, HTTP_DEFAULT_MAX_PER_ROUTE));
        }
        if (notNull(props, prefix, OAUTH_CONSUMER_KEY)) {
            setOAuthConsumerKey(getString(props, prefix, OAUTH_CONSUMER_KEY));
        }
        if (notNull(props, prefix, OAUTH_CONSUMER_SECRET)) {
            setOAuthConsumerSecret(getString(props, prefix, OAUTH_CONSUMER_SECRET));
        }
        if (notNull(props, prefix, OAUTH_ACCESS_TOKEN)) {
            setOAuthAccessToken(getString(props, prefix, OAUTH_ACCESS_TOKEN));
        }
        if (notNull(props, prefix, OAUTH_ACCESS_TOKEN_SECRET)) {
            setOAuthAccessTokenSecret(getString(props, prefix, OAUTH_ACCESS_TOKEN_SECRET));
        }
        if (notNull(props, prefix, ASYNC_NUM_THREADS)) {
            setAsyncNumThreads(getIntProperty(props, prefix, ASYNC_NUM_THREADS));
        }
        if (notNull(props, prefix, ASYNC_DISPATCHER_IMPL)) {
            setDispatcherImpl(getString(props, prefix, ASYNC_DISPATCHER_IMPL));
        }
        if (notNull(props, prefix, CLIENT_VERSION)) {
            setClientVersion(getString(props, prefix, CLIENT_VERSION));
        }
        if (notNull(props, prefix, CLIENT_URL)) {
            setClientURL(getString(props, prefix, CLIENT_URL));
        }
        if (notNull(props, prefix, HTTP_USER_AGENT)) {
            setUserAgent(getString(props, prefix, HTTP_USER_AGENT));
        }
        if (notNull(props, prefix, OAUTH_REQUEST_TOKEN_URL)) {
            setOAuthRequestTokenURL(getString(props, prefix, OAUTH_REQUEST_TOKEN_URL));
        }
        if (notNull(props, prefix, OAUTH_AUTHORIZATION_URL)) {
            setOAuthAuthorizationURL(getString(props, prefix, OAUTH_AUTHORIZATION_URL));
        }
        if (notNull(props, prefix, OAUTH_ACCESS_TOKEN_URL)) {
            setOAuthAccessTokenURL(getString(props, prefix, OAUTH_ACCESS_TOKEN_URL));
        }
        if (notNull(props, prefix, OAUTH_AUTHENTICATION_URL)) {
            setOAuthAuthenticationURL(getString(props, prefix, OAUTH_AUTHENTICATION_URL));
        }
        if (notNull(props, prefix, REST_BASE_URL)) {
            setRestBaseURL(getString(props, prefix, REST_BASE_URL));
        }
        if (notNull(props, prefix, SEARCH_BASE_URL)) {
            setSearchBaseURL(getString(props, prefix, SEARCH_BASE_URL));
        }
        if (notNull(props, prefix, STREAM_BASE_URL)) {
            setStreamBaseURL(getString(props, prefix, STREAM_BASE_URL));
        }
        if (notNull(props, prefix, USER_STREAM_BASE_URL)) {
            setUserStreamBaseURL(getString(props, prefix, USER_STREAM_BASE_URL));
        }
        if (notNull(props, prefix, SITE_STREAM_BASE_URL)) {
            setSiteStreamBaseURL(getString(props, prefix, SITE_STREAM_BASE_URL));
        }
        if (notNull(props, prefix, INCLUDE_RTS)) {
            setIncludeRTsEnbled(getBoolean(props, prefix, INCLUDE_RTS));
        }
        if (notNull(props, prefix, INCLUDE_ENTITIES)) {
            setIncludeEntitiesEnbled(getBoolean(props, prefix, INCLUDE_ENTITIES));
        }
        if (notNull(props, prefix, JSON_STORE_ENABLED)) {
            setJSONStoreEnabled(getBoolean(props, prefix, JSON_STORE_ENABLED));
        }
        if (notNull(props, prefix, MBEAN_ENABLED)) {
            setMBeanEnabled(getBoolean(props, prefix, MBEAN_ENABLED));
        }
        if (notNull(props, prefix, STREAM_USER_REPLIES_ALL)) {
            setUserStreamRepliesAllEnabled(getBoolean(props, prefix, STREAM_USER_REPLIES_ALL));
        }
        if (notNull(props, prefix, MEDIA_PROVIDER)) {
            setMediaProvider(getString(props, prefix, MEDIA_PROVIDER));
        }
        if (notNull(props, prefix, MEDIA_PROVIDER_API_KEY)) {
            setMediaProviderAPIKey(getString(props, prefix, MEDIA_PROVIDER_API_KEY));
        }
        if (notNull(props, prefix, MEDIA_PROVIDER_PARAMETERS)) {
            String[] propsAry = T4JInternalStringUtil.split(getString(props, prefix, MEDIA_PROVIDER_PARAMETERS), "&");
            Properties p = new Properties();
            for (String str : propsAry) {
                String[] parameter = T4JInternalStringUtil.split(str, "=");
                p.setProperty(parameter[0], parameter[1]);
            }
            setMediaProviderParameters(p);
        }
    }

    protected boolean getBoolean(Properties props, String prefix, String name) {
        String value = props.getProperty(new StringBuffer().append(prefix).append(name).toString());
        return Boolean.valueOf(value).booleanValue();
    }

    protected int getIntProperty(Properties props, String prefix, String name) {
        String value = props.getProperty(new StringBuffer().append(prefix).append(name).toString());
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    protected String getString(Properties props, String prefix, String name) {
        return props.getProperty(new StringBuffer().append(prefix).append(name).toString());
    }
}
