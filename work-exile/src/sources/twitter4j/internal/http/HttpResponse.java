package twitter4j.internal.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import oauth.signpost.OAuth;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.TwitterException;
import twitter4j.conf.ConfigurationContext;
import twitter4j.internal.logging.Logger;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class HttpResponse {
    static Class class$twitter4j$internal$http$HttpResponseImpl;
    private static final Logger logger;
    protected final HttpClientConfiguration CONF;
    protected InputStream is;
    private JSONObject json;
    protected String responseAsString;
    protected int statusCode;
    private boolean streamConsumed;

    public abstract void disconnect() throws IOException;

    public abstract String getResponseHeader(String str);

    public abstract Map<String, List<String>> getResponseHeaderFields();

    static {
        Class clsClass$;
        if (class$twitter4j$internal$http$HttpResponseImpl == null) {
            clsClass$ = class$("twitter4j.internal.http.HttpResponseImpl");
            class$twitter4j$internal$http$HttpResponseImpl = clsClass$;
        } else {
            clsClass$ = class$twitter4j$internal$http$HttpResponseImpl;
        }
        logger = Logger.getLogger(clsClass$);
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    HttpResponse() {
        this.responseAsString = null;
        this.streamConsumed = false;
        this.json = null;
        this.CONF = ConfigurationContext.getInstance();
    }

    public HttpResponse(HttpClientConfiguration conf) {
        this.responseAsString = null;
        this.streamConsumed = false;
        this.json = null;
        this.CONF = conf;
    }

    public final int getStatusCode() {
        return this.statusCode;
    }

    public final InputStream asStream() {
        if (this.streamConsumed) {
            throw new IllegalStateException("Stream has already been consumed.");
        }
        return this.is;
    }

    public final String asString() throws Throwable {
        if (this.responseAsString == null) {
            BufferedReader br = null;
            InputStream stream = null;
            try {
                try {
                    stream = asStream();
                    if (stream == null) {
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (IOException e) {
                            }
                        }
                        if (0 != 0) {
                            try {
                                br.close();
                            } catch (IOException e2) {
                            }
                        }
                        disconnectForcibly();
                        return null;
                    }
                    BufferedReader br2 = new BufferedReader(new InputStreamReader(stream, OAuth.ENCODING));
                    try {
                        StringBuffer buf = new StringBuffer();
                        while (true) {
                            String line = br2.readLine();
                            if (line == null) {
                                break;
                            }
                            buf.append(line).append("\n");
                        }
                        this.responseAsString = buf.toString();
                        logger.debug(this.responseAsString);
                        stream.close();
                        this.streamConsumed = true;
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (IOException e3) {
                            }
                        }
                        if (br2 != null) {
                            try {
                                br2.close();
                            } catch (IOException e4) {
                            }
                        }
                        disconnectForcibly();
                    } catch (IOException e5) {
                        ioe = e5;
                        br = br2;
                    } catch (Throwable th) {
                        th = th;
                        br = br2;
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (IOException e6) {
                            }
                        }
                        if (br != null) {
                            try {
                                br.close();
                            } catch (IOException e7) {
                            }
                        }
                        disconnectForcibly();
                        throw th;
                    }
                } catch (IOException e8) {
                    ioe = e8;
                }
            } catch (Throwable th2) {
                th = th2;
            }
            throw new TwitterException(ioe.getMessage(), ioe);
        }
        return this.responseAsString;
    }

    public final JSONObject asJSONObject() throws TwitterException {
        if (this.json == null) {
            InputStreamReader reader = null;
            try {
                try {
                    if (logger.isDebugEnabled() && this.CONF.isPrettyDebugEnabled()) {
                        this.json = new JSONObject(asString());
                        logger.debug(this.json.toString(1));
                    } else {
                        this.json = new JSONObject(asString());
                    }
                    if (0 != 0) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                        }
                    }
                    disconnectForcibly();
                } catch (JSONException jsone) {
                    if (logger.isDebugEnabled()) {
                        throw new TwitterException(new StringBuffer().append(jsone.getMessage()).append(":").append(this.responseAsString).toString(), jsone);
                    }
                    throw new TwitterException(jsone.getMessage(), jsone);
                }
            } catch (Throwable th) {
                if (0 != 0) {
                    try {
                        reader.close();
                    } catch (IOException e2) {
                    }
                }
                disconnectForcibly();
                throw th;
            }
        }
        return this.json;
    }

    public final JSONArray asJSONArray() throws Throwable {
        JSONArray json;
        InputStreamReader reader = null;
        try {
            try {
                if (logger.isDebugEnabled()) {
                    if (this.CONF.isPrettyDebugEnabled()) {
                        JSONArray json2 = new JSONArray(asString());
                        try {
                            logger.debug(json2.toString(1));
                            json = json2;
                        } catch (JSONException e) {
                            jsone = e;
                            if (logger.isDebugEnabled()) {
                                throw new TwitterException(new StringBuffer().append(jsone.getMessage()).append(":").append(this.responseAsString).toString(), jsone);
                            }
                            throw new TwitterException(jsone.getMessage(), jsone);
                        } catch (Throwable th) {
                            th = th;
                            if (0 != 0) {
                                try {
                                    reader.close();
                                } catch (IOException e2) {
                                }
                            }
                            disconnectForcibly();
                            throw th;
                        }
                    } else {
                        JSONArray json3 = new JSONArray(asString());
                        json = json3;
                    }
                } else {
                    JSONArray json4 = new JSONArray(asString());
                    json = json4;
                }
                if (0 != 0) {
                    try {
                        reader.close();
                    } catch (IOException e3) {
                    }
                }
                disconnectForcibly();
                return json;
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (JSONException e4) {
            jsone = e4;
        }
    }

    public final InputStreamReader asReader() {
        try {
            return new InputStreamReader(this.is, OAuth.ENCODING);
        } catch (UnsupportedEncodingException e) {
            return new InputStreamReader(this.is);
        }
    }

    private void disconnectForcibly() {
        try {
            disconnect();
        } catch (Exception e) {
        }
    }

    public String toString() {
        return new StringBuffer().append("HttpResponse{statusCode=").append(this.statusCode).append(", responseAsString='").append(this.responseAsString).append('\'').append(", is=").append(this.is).append(", streamConsumed=").append(this.streamConsumed).append('}').toString();
    }
}
