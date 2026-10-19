package twitter4j.internal.http;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oauth.signpost.OAuth;
import twitter4j.TwitterException;
import twitter4j.conf.ConfigurationContext;
import twitter4j.internal.logging.Logger;
import twitter4j.internal.util.T4JInternalStringUtil;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class HttpClientImpl implements HttpClient, HttpResponseCode, Serializable {
    static Class class$twitter4j$internal$http$HttpClientImpl = null;
    private static final Map<HttpClientConfiguration, HttpClient> instanceMap;
    private static boolean isJDK14orEarlier = false;
    private static final Logger logger;
    private static final long serialVersionUID = -8819171414069621503L;
    private final HttpClientConfiguration CONF;

    static {
        Class clsClass$;
        if (class$twitter4j$internal$http$HttpClientImpl == null) {
            clsClass$ = class$("twitter4j.internal.http.HttpClientImpl");
            class$twitter4j$internal$http$HttpClientImpl = clsClass$;
        } else {
            clsClass$ = class$twitter4j$internal$http$HttpClientImpl;
        }
        logger = Logger.getLogger(clsClass$);
        isJDK14orEarlier = false;
        try {
            String versionStr = System.getProperty("java.specification.version");
            if (versionStr != null) {
                isJDK14orEarlier = 1.5d > Double.parseDouble(versionStr);
            }
            if (ConfigurationContext.getInstance().isDalvik()) {
                System.setProperty("http.keepAlive", "false");
            }
        } catch (SecurityException e) {
            isJDK14orEarlier = true;
        }
        instanceMap = new HashMap(1);
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public HttpClientImpl() {
        this.CONF = ConfigurationContext.getInstance();
    }

    public HttpClientImpl(HttpClientConfiguration conf) {
        this.CONF = conf;
        if (isProxyConfigured() && isJDK14orEarlier) {
            logger.warn("HTTP Proxy is not supported on JDK1.4 or earlier. Try twitter4j-httpclient-supoprt artifact");
        }
    }

    @Override // twitter4j.internal.http.HttpClient
    public void shutdown() {
    }

    public static HttpClient getInstance(HttpClientConfiguration conf) {
        HttpClient client = instanceMap.get(conf);
        if (client == null) {
            HttpClient client2 = new HttpClientImpl(conf);
            instanceMap.put(conf, client2);
            return client2;
        }
        return client;
    }

    public HttpResponse get(String url) throws TwitterException {
        return request(new HttpRequest(RequestMethod.GET, url, null, null, null));
    }

    public HttpResponse post(String url, HttpParameter[] params) throws TwitterException {
        return request(new HttpRequest(RequestMethod.POST, url, params, null, null));
    }

    @Override // twitter4j.internal.http.HttpClient
    public HttpResponse request(HttpRequest req) throws Throwable {
        int retry = this.CONF.getHttpRetryCount() + 1;
        HttpResponse res = null;
        int retriedCount = 0;
        while (true) {
            HttpResponse res2 = res;
            if (retriedCount >= retry) {
                return res2;
            }
            OutputStream os = null;
            try {
                HttpURLConnection con = getConnection(req.getURL());
                con.setDoInput(true);
                setHeaders(req, con);
                con.setRequestMethod(req.getMethod().name());
                if (req.getMethod() == RequestMethod.POST) {
                    if (HttpParameter.containsFile(req.getParameters())) {
                        String boundary = new StringBuffer().append("----Twitter4J-upload").append(System.currentTimeMillis()).toString();
                        con.setRequestProperty("Content-Type", new StringBuffer().append("multipart/form-data; boundary=").append(boundary).toString());
                        String boundary2 = new StringBuffer().append("--").append(boundary).toString();
                        con.setDoOutput(true);
                        os = con.getOutputStream();
                        DataOutputStream out = new DataOutputStream(os);
                        HttpParameter[] arr$ = req.getParameters();
                        for (HttpParameter param : arr$) {
                            if (param.isFile()) {
                                write(out, new StringBuffer().append(boundary2).append("\r\n").toString());
                                write(out, new StringBuffer().append("Content-Disposition: form-data; name=\"").append(param.getName()).append("\"; filename=\"").append(param.getFile().getName()).append("\"\r\n").toString());
                                write(out, new StringBuffer().append("Content-Type: ").append(param.getContentType()).append("\r\n\r\n").toString());
                                BufferedInputStream in = new BufferedInputStream(param.hasFileBody() ? param.getFileBody() : new FileInputStream(param.getFile()));
                                while (true) {
                                    int buff = in.read();
                                    if (buff == -1) {
                                        break;
                                    }
                                    out.write(buff);
                                }
                                write(out, "\r\n");
                                in.close();
                            } else {
                                write(out, new StringBuffer().append(boundary2).append("\r\n").toString());
                                write(out, new StringBuffer().append("Content-Disposition: form-data; name=\"").append(param.getName()).append("\"\r\n").toString());
                                write(out, "Content-Type: text/plain; charset=UTF-8\r\n\r\n");
                                logger.debug(param.getValue());
                                out.write(param.getValue().getBytes(OAuth.ENCODING));
                                write(out, "\r\n");
                            }
                        }
                        write(out, new StringBuffer().append(boundary2).append("--\r\n").toString());
                        write(out, "\r\n");
                    } else {
                        con.setRequestProperty("Content-Type", OAuth.FORM_ENCODED);
                        String postParam = HttpParameter.encodeParameters(req.getParameters());
                        logger.debug("Post Params: ", postParam);
                        byte[] bytes = postParam.getBytes(OAuth.ENCODING);
                        con.setRequestProperty("Content-Length", Integer.toString(bytes.length));
                        con.setDoOutput(true);
                        os = con.getOutputStream();
                        os.write(bytes);
                    }
                    os.flush();
                    os.close();
                }
                res = new HttpResponseImpl(con, this.CONF);
                try {
                    int responseCode = con.getResponseCode();
                    if (logger.isDebugEnabled()) {
                        logger.debug("Response: ");
                        Map<String, List<String>> responseHeaders = con.getHeaderFields();
                        for (String key : responseHeaders.keySet()) {
                            List<String> values = responseHeaders.get(key);
                            for (String value : values) {
                                if (key != null) {
                                    logger.debug(new StringBuffer().append(key).append(": ").append(value).toString());
                                } else {
                                    logger.debug(value);
                                }
                            }
                        }
                    }
                    if (responseCode < 200 || (responseCode != 302 && 300 <= responseCode)) {
                        if (responseCode == 420 || responseCode == 400 || responseCode < 500 || retriedCount == this.CONF.getHttpRetryCount()) {
                            throw new TwitterException(res.asString(), res);
                        }
                        try {
                            try {
                                os.close();
                            } catch (Exception e) {
                            }
                        } catch (IOException ioe) {
                            if (retriedCount == this.CONF.getHttpRetryCount()) {
                                throw new TwitterException(ioe.getMessage(), ioe, responseCode);
                            }
                        }
                        try {
                            if (logger.isDebugEnabled() && res != null) {
                                res.asString();
                            }
                            logger.debug(new StringBuffer().append("Sleeping ").append(this.CONF.getHttpRetryIntervalSeconds()).append(" seconds until the next retry.").toString());
                            Thread.sleep(this.CONF.getHttpRetryIntervalSeconds() * 1000);
                        } catch (InterruptedException e2) {
                        }
                        retriedCount++;
                    } else {
                        try {
                            os.close();
                            return res;
                        } catch (Exception e3) {
                            return res;
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    try {
                        os.close();
                    } catch (Exception e4) {
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    private void write(DataOutputStream out, String outStr) throws IOException {
        out.writeBytes(outStr);
        logger.debug(outStr);
    }

    public static String encode(String str) {
        try {
            return URLEncoder.encode(str, OAuth.ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("will never happen");
        }
    }

    private void setHeaders(HttpRequest req, HttpURLConnection connection) {
        String authorizationHeader;
        if (logger.isDebugEnabled()) {
            logger.debug("Request: ");
            logger.debug(new StringBuffer().append(req.getMethod().name()).append(" ").toString(), req.getURL());
        }
        if (req.getAuthorization() != null && (authorizationHeader = req.getAuthorization().getAuthorizationHeader(req)) != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Authorization: ", T4JInternalStringUtil.maskString(authorizationHeader));
            }
            connection.addRequestProperty(OAuth.HTTP_AUTHORIZATION_HEADER, authorizationHeader);
        }
        if (req.getRequestHeaders() != null) {
            for (String key : req.getRequestHeaders().keySet()) {
                connection.addRequestProperty(key, req.getRequestHeaders().get(key));
                logger.debug(new StringBuffer().append(key).append(": ").append(req.getRequestHeaders().get(key)).toString());
            }
        }
    }

    private HttpURLConnection getConnection(String url) throws IOException {
        HttpURLConnection con;
        if (isProxyConfigured() && !isJDK14orEarlier) {
            if (this.CONF.getHttpProxyUser() != null && !this.CONF.getHttpProxyUser().equals("")) {
                if (logger.isDebugEnabled()) {
                    logger.debug(new StringBuffer().append("Proxy AuthUser: ").append(this.CONF.getHttpProxyUser()).toString());
                    logger.debug(new StringBuffer().append("Proxy AuthPassword: ").append(T4JInternalStringUtil.maskString(this.CONF.getHttpProxyPassword())).toString());
                }
                Authenticator.setDefault(new Authenticator(this) { // from class: twitter4j.internal.http.HttpClientImpl.1
                    private final HttpClientImpl this$0;

                    {
                        this.this$0 = this;
                    }

                    @Override // java.net.Authenticator
                    protected PasswordAuthentication getPasswordAuthentication() {
                        if (getRequestorType().equals(Authenticator.RequestorType.PROXY)) {
                            return new PasswordAuthentication(this.this$0.CONF.getHttpProxyUser(), this.this$0.CONF.getHttpProxyPassword().toCharArray());
                        }
                        return null;
                    }
                });
            }
            Proxy proxy = new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(this.CONF.getHttpProxyHost(), this.CONF.getHttpProxyPort()));
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuffer().append("Opening proxied connection(").append(this.CONF.getHttpProxyHost()).append(":").append(this.CONF.getHttpProxyPort()).append(")").toString());
            }
            con = (HttpURLConnection) new URL(url).openConnection(proxy);
        } else {
            con = (HttpURLConnection) new URL(url).openConnection();
        }
        if (this.CONF.getHttpConnectionTimeout() > 0 && !isJDK14orEarlier) {
            con.setConnectTimeout(this.CONF.getHttpConnectionTimeout());
        }
        if (this.CONF.getHttpReadTimeout() > 0 && !isJDK14orEarlier) {
            con.setReadTimeout(this.CONF.getHttpReadTimeout());
        }
        con.setInstanceFollowRedirects(false);
        return con;
    }

    private boolean isProxyConfigured() {
        return (this.CONF.getHttpProxyHost() == null || this.CONF.getHttpProxyHost().equals("")) ? false : true;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HttpClientImpl that = (HttpClientImpl) o;
        if (this.CONF != null) {
            if (this.CONF.equals(that.CONF)) {
                return true;
            }
        } else if (that.CONF == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (this.CONF != null) {
            return this.CONF.hashCode();
        }
        return 0;
    }

    public String toString() {
        return new StringBuffer().append("HttpClientImpl{CONF=").append(this.CONF).append('}').toString();
    }
}
