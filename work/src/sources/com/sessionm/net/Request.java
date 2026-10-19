package com.sessionm.net;

import android.util.Log;
import com.sessionm.a.b;
import com.sessionm.a.e;
import com.sessionm.core.Session;
import com.sessionm.core.SessionMAndroidConfig;
import com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http.HttpEngine;
import com.sessionm.logging.Logger;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public class Request extends HttpResponseHandler implements Serializable {
    private static final long serialVersionUID = 5724977901695201545L;
    private String TAG;
    private HttpClient client;
    private Throwable error;
    private int httpStatus;
    private final String id;
    private RequestListener listener;
    private Method method;
    private String paramString;
    private final Object params;
    private String reply;
    private final Type type;
    private String urlString;

    /* JADX INFO: compiled from: ProGuard */
    public enum Method {
        POST,
        GET,
        DELETE,
        PUT,
        GENERIC
    }

    /* JADX INFO: compiled from: ProGuard */
    public enum Type {
        SESSION_START("%s/%s/%s/%s", Method.POST),
        ACTION("%s/%s", Method.POST),
        SESSION_END("%s/v%s/%s", Method.POST),
        CONTENT("", Method.GET),
        BUG("%s/%s", Method.GET),
        ERROR("%s/%s", Method.POST),
        DELIVERED("%s", Method.GET),
        GENERIC("", Method.GENERIC),
        OPT_IN_OUT("%s/%s", Method.PUT),
        INSTALL("%s", Method.POST);

        private final Method method;
        private final String urlFormat;

        Type(String str, Method method) {
            this.urlFormat = str;
            this.method = method;
        }

        Method getMethod() {
            return this.method;
        }

        String getUrl() {
            return this.urlFormat;
        }
    }

    public Request(Type type) {
        this(type, null);
    }

    public Request(Type type, Object obj) {
        this.TAG = "Request";
        this.type = type;
        this.params = obj;
        this.id = UUID.randomUUID().toString();
    }

    public Request(String str, Type type, Object obj) {
        this(type, obj);
        this.urlString = str;
    }

    public Request(String str, String str2, Object obj) {
        this(Type.GENERIC, obj);
        this.urlString = str;
        if (str2.equalsIgnoreCase(HttpEngine.GET)) {
            this.method = Method.GET;
            return;
        }
        if (str2.equalsIgnoreCase(HttpEngine.POST)) {
            this.method = Method.POST;
        } else if (str2.equalsIgnoreCase(HttpEngine.PUT)) {
            this.method = Method.PUT;
        } else if (str2.equalsIgnoreCase(HttpEngine.DELETE)) {
            this.method = Method.DELETE;
        }
    }

    public void setClient(HttpClient httpClient) {
        this.client = httpClient;
    }

    public void setListener(RequestListener requestListener) {
        this.listener = requestListener;
    }

    public String getID() {
        return this.id;
    }

    public Type getType() {
        return this.type;
    }

    public Throwable getError() {
        return this.error;
    }

    public String getUrl() {
        return this.urlString;
    }

    public void send() {
        String strA;
        SessionMAndroidConfig sessionMAndroidConfig = SessionMAndroidConfig.getInstance();
        String str = (String) sessionMAndroidConfig.getConfigurationByKey(b.b);
        if (this.type.equals(Type.SESSION_START)) {
            this.urlString = String.format(this.type.getUrl(), str, (String) sessionMAndroidConfig.getConfigurationByKey(b.q), Session.getSession().getApiKey(), (String) sessionMAndroidConfig.getConfigurationByKey(b.y));
        } else if (this.type.equals(Type.SESSION_END)) {
            this.urlString = String.format(this.type.getUrl(), str, (String) sessionMAndroidConfig.getConfigurationByKey(b.x), (String) sessionMAndroidConfig.getConfigurationByKey(b.y));
        } else if (this.type.equals(Type.BUG)) {
            this.urlString = String.format("%s/%s", str, (String) sessionMAndroidConfig.getConfigurationByKey(b.s));
        } else if (this.type.equals(Type.ACTION)) {
            this.urlString = String.format(this.type.getUrl(), str, (String) sessionMAndroidConfig.getConfigurationByKey(b.A));
        } else if (this.type.equals(Type.ERROR)) {
            this.urlString = String.format(this.type.getUrl(), str, (String) sessionMAndroidConfig.getConfigurationByKey(b.v));
        } else if (this.type.equals(Type.OPT_IN_OUT)) {
            this.urlString = String.format(this.type.getUrl(), str, (String) sessionMAndroidConfig.getConfigurationByKey(b.E));
        } else if (this.type.equals(Type.INSTALL)) {
            this.urlString = String.format(this.type.getUrl(), (String) sessionMAndroidConfig.getConfigurationByKey(b.l));
        }
        Method method = this.type.method.equals(Method.GENERIC) ? this.method : this.type.method;
        if (this.params == null) {
            strA = "";
        } else {
            try {
                strA = e.a(this.params);
            } catch (Exception e) {
                Log.e(this.TAG, "Error parsing request parameters. Bailing out of request.");
                notifyRequestFinished(null, e, -1);
                return;
            }
        }
        if (!this.type.equals(Type.CONTENT) && SessionMAndroidConfig.getInstance().isSecureAPI() && e.b(this.urlString)) {
            if (method.equals(Method.GET) || method.equals(Method.DELETE)) {
                try {
                    URI uri = new URI(this.urlString);
                    if (uri.getQuery() != null) {
                        if (strA.equals("")) {
                            strA = uri.getQuery();
                        } else {
                            strA = strA + "&" + uri.getQuery();
                        }
                    }
                } catch (URISyntaxException e2) {
                    Logger.error(this.TAG, "invalid url: " + this.urlString + ", error: " + e2);
                    notifyRequestFinished(null, e2, -1);
                    return;
                }
            }
            strA = e.a(strA).get("query").toString();
            String strA2 = e.a(this.urlString, method, strA);
            if (strA2 != null) {
                strA = strA + "&sig=" + strA2;
            }
        }
        if (!this.type.equals(Type.CONTENT) && (method.equals(Method.GET) || method.equals(Method.DELETE))) {
            this.urlString = String.format("%s?%s", this.urlString, strA);
        }
        if (this.client == null) {
            this.client = Session.getSession().getRequestQueue().getHttpClient();
        }
        this.client.sendRequest(this.urlString, method.toString(), strA, this);
    }

    public String getReply() {
        return this.reply;
    }

    @Override // com.sessionm.net.HttpResponseHandler
    public void notifyRequestSent() {
        Logger.debug(this.TAG, "Sent request with url " + this.urlString);
        if (this.listener != null) {
            this.listener.onRequestSent(this);
        }
    }

    @Override // com.sessionm.net.HttpResponseHandler
    public void notifyRequestFinished(String str, Throwable th, int i) {
        this.reply = str;
        this.error = th;
        this.httpStatus = i;
        if (this.type == Type.SESSION_START) {
            this.client.syncCookies();
        }
        Logger.debug(this.TAG, "Finished request with url " + this.urlString);
        if (this.listener != null) {
            this.listener.onReplyReceived(this);
        }
    }

    public int statusCode() {
        return this.httpStatus;
    }

    public Method getMethod() {
        return this.method;
    }

    void setReply(String str) {
        this.reply = str;
    }

    public void setError(Throwable th) {
        this.error = th;
    }

    void setStatusCode(int i) {
        this.httpStatus = i;
    }

    public String getParams() {
        return this.paramString;
    }
}
