package com.chartboost.sdk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import com.sessionm.a.b;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import oauth.signpost.OAuth;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/* JADX INFO: loaded from: classes.dex */
public class CBAPIConnection extends AsyncTask<CBAPIRequest, Void, JSONObject> {
    private static final String CB_DEFAULT_ENDPOINT = "https://www.chartboost.com/";
    public static final int DEFAULT_CONNECT_TIMEOUT = 30000;
    public static final int DEFAULT_READ_TIMEOUT = 30000;
    public static final int MIN_TIMEOUT = 10000;
    protected Context context;
    public Object data;
    private String endpoint;
    private HttpClient httpClient;
    public boolean shouldShowProgress = false;
    public String loadingMessage = "Loading...";
    private ProgressDialog progressDialog = null;

    public CBAPIConnection(Context context) {
        this.context = null;
        this.context = context;
        setEndpoint(CB_DEFAULT_ENDPOINT);
        this.httpClient = createHttpClient();
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        if (this.shouldShowProgress) {
            this.progressDialog = ProgressDialog.show(this.context, null, this.loadingMessage, true, true, new DialogInterface.OnCancelListener() { // from class: com.chartboost.sdk.CBAPIConnection.1
                @Override // android.content.DialogInterface.OnCancelListener
                public void onCancel(DialogInterface arg0) {
                    CBAPIConnection.this.cancel(true);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public JSONObject doInBackground(CBAPIRequest... requests) {
        CBAPIRequest request = requests[0];
        String urlString = String.valueOf(getEndpoint()) + request.getController() + "/" + request.getAction() + ".json";
        Map<String, String> query = request.getQuery();
        if (query != null) {
            String queryString = "";
            for (String key : query.keySet()) {
                String value = query.get(key);
                try {
                    queryString = String.valueOf(queryString) + URLEncoder.encode(key, OAuth.ENCODING) + "=" + URLEncoder.encode(value, OAuth.ENCODING) + "&";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            urlString = String.valueOf(urlString) + "?" + queryString;
        }
        HttpPost httpRequest = new HttpPost(urlString);
        Map<String, String> body = request.getBody();
        if (body != null) {
            List<NameValuePair> postPairs = new ArrayList<>();
            for (String key2 : body.keySet()) {
                postPairs.add(new BasicNameValuePair(key2, body.get(key2)));
            }
            try {
                httpRequest.setEntity(new UrlEncodedFormEntity(postPairs));
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
            }
        }
        HttpClient finalHttpClient = this.httpClient;
        try {
            HttpResponse response = finalHttpClient.execute(httpRequest);
            int status = response.getStatusLine().getStatusCode();
            if (status < 300 && status >= 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), OAuth.ENCODING), Opcodes.ACC_ANNOTATION);
                StringBuilder builder = new StringBuilder();
                while (true) {
                    String line = reader.readLine();
                    if (line != null) {
                        builder.append(line).append("\n");
                    } else {
                        JSONTokener tokener = new JSONTokener(builder.toString());
                        JSONObject jsonObject = new JSONObject(tokener);
                        Log.i(ChartBoost.TAG, "Request response received: " + jsonObject.optString("message"));
                        return jsonObject;
                    }
                }
            } else {
                Log.w(ChartBoost.TAG, "Request failed: " + response);
                return null;
            }
        } catch (Exception e3) {
            Log.e(ChartBoost.TAG, "Exception on http request: " + e3.getLocalizedMessage());
            return null;
        }
    }

    protected void validateJson(JSONObject json) throws JSONException, IOException {
        if (json != null) {
            int status = json.getInt(b.K);
            if (status < 300 && status >= 200) {
                Log.i(JsonFactory.FORMAT_NAME_JSON, json.toString());
                return;
            }
            throw new IOException();
        }
        throw new IOException();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(JSONObject json) {
        if (this.progressDialog != null) {
            try {
                this.progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.progressDialog = null;
    }

    private HttpClient createHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory sf = new TrustingSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, OAuth.ENCODING);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            HttpConnectionParams.setConnectionTimeout(params, ChartBoost.getSharedChartBoost(this.context).getTimeoutConnect());
            HttpConnectionParams.setSoTimeout(params, ChartBoost.getSharedChartBoost(this.context).getTimeoutRead());
            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    public void sendRequest() {
    }

    public String getEndpoint() {
        return this.endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    private class TrustingSocketFactory extends SSLSocketFactory {
        SSLContext sslContext;

        public TrustingSocketFactory(KeyStore truststore) throws Exception {
            super(truststore);
            this.sslContext = SSLContext.getInstance("TLS");
            TrustManager tm = new X509TrustManager() { // from class: com.chartboost.sdk.CBAPIConnection.TrustingSocketFactory.1
                @Override // javax.net.ssl.X509TrustManager
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override // javax.net.ssl.X509TrustManager
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override // javax.net.ssl.X509TrustManager
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            this.sslContext.init(null, new TrustManager[]{tm}, null);
        }

        @Override // org.apache.http.conn.ssl.SSLSocketFactory, org.apache.http.conn.scheme.LayeredSocketFactory
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
            return this.sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override // org.apache.http.conn.ssl.SSLSocketFactory, org.apache.http.conn.scheme.SocketFactory
        public Socket createSocket() throws IOException {
            return this.sslContext.getSocketFactory().createSocket();
        }
    }
}
