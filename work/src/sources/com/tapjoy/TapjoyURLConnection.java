package com.tapjoy;

import android.net.Uri;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

/* JADX INFO: loaded from: classes.dex */
public class TapjoyURLConnection {
    private static final String TAPJOY_URL_CONNECTION = "TapjoyURLConnection";

    public String connectToURL(String url) {
        return connectToURL(url, "");
    }

    public String connectToURL(String url, String params) {
        String httpResponse = null;
        try {
            String requestURL = (String.valueOf(url) + params).replaceAll(" ", "%20");
            TapjoyLog.i(TAPJOY_URL_CONNECTION, "baseURL: " + url);
            TapjoyLog.i(TAPJOY_URL_CONNECTION, "requestURL: " + requestURL);
            URL httpURL = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) httpURL.openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(30000);
            httpResponse = connection.getResponseMessage();
            connection.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            try {
                StringBuilder sb = new StringBuilder();
                while (true) {
                    try {
                        String line = rd.readLine();
                        if (line != null) {
                            sb.append(String.valueOf(line) + '\n');
                        } else {
                            httpResponse = sb.toString();
                            TapjoyLog.i(TAPJOY_URL_CONNECTION, "--------------------");
                            TapjoyLog.i(TAPJOY_URL_CONNECTION, "response size: " + httpResponse.length());
                            TapjoyLog.i(TAPJOY_URL_CONNECTION, "response: ");
                            TapjoyLog.i(TAPJOY_URL_CONNECTION, httpResponse);
                            TapjoyLog.i(TAPJOY_URL_CONNECTION, "--------------------");
                            return httpResponse;
                        }
                    } catch (Exception e) {
                        e = e;
                        TapjoyLog.e(TAPJOY_URL_CONNECTION, "Exception: " + e.toString());
                        return httpResponse;
                    }
                }
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Exception e3) {
            e = e3;
        }
    }

    public String getContentLength(String url) {
        String contentLength = null;
        try {
            String requestURL = url.replaceAll(" ", "%20");
            TapjoyLog.i(TAPJOY_URL_CONNECTION, "requestURL: " + requestURL);
            URL httpURL = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) httpURL.openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(30000);
            contentLength = connection.getHeaderField("content-length");
        } catch (Exception e) {
            TapjoyLog.e(TAPJOY_URL_CONNECTION, "Exception: " + e.toString());
        }
        TapjoyLog.i(TAPJOY_URL_CONNECTION, "content-length: " + contentLength);
        return contentLength;
    }

    public String connectToURLwithPOST(String url, Hashtable<String, String> params, Hashtable<String, String> paramsData) {
        String httpResponse = null;
        try {
            String requestURL = url.replaceAll(" ", "%20");
            TapjoyLog.i(TAPJOY_URL_CONNECTION, "baseURL: " + url);
            TapjoyLog.i(TAPJOY_URL_CONNECTION, "requestURL: " + requestURL);
            HttpPost httpPost = new HttpPost(requestURL);
            List<NameValuePair> pairs = new ArrayList<>();
            Set<Map.Entry<String, String>> entries = params.entrySet();
            for (Map.Entry<String, String> item : entries) {
                pairs.add(new BasicNameValuePair(item.getKey(), item.getValue()));
                TapjoyLog.i(TAPJOY_URL_CONNECTION, "key: " + item.getKey() + ", value: " + Uri.encode(item.getValue()));
            }
            if (paramsData != null && paramsData.size() > 0) {
                Set<Map.Entry<String, String>> entries2 = paramsData.entrySet();
                for (Map.Entry<String, String> item2 : entries2) {
                    pairs.add(new BasicNameValuePair("data[" + item2.getKey() + "]", item2.getValue()));
                    TapjoyLog.i(TAPJOY_URL_CONNECTION, "key: " + item2.getKey() + ", value: " + Uri.encode(item2.getValue()));
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairs));
            TapjoyLog.i(TAPJOY_URL_CONNECTION, "HTTP POST: " + httpPost.toString());
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
            HttpConnectionParams.setSoTimeout(httpParameters, 30000);
            HttpClient client = new DefaultHttpClient(httpParameters);
            HttpResponse response = client.execute(httpPost);
            HttpEntity entity = response.getEntity();
            httpResponse = EntityUtils.toString(entity);
            TapjoyLog.i(TAPJOY_URL_CONNECTION, "--------------------");
            TapjoyLog.i(TAPJOY_URL_CONNECTION, "response status: " + response.getStatusLine().getStatusCode());
            TapjoyLog.i(TAPJOY_URL_CONNECTION, "response size: " + httpResponse.length());
            TapjoyLog.i(TAPJOY_URL_CONNECTION, "response: ");
            TapjoyLog.i(TAPJOY_URL_CONNECTION, httpResponse);
            TapjoyLog.i(TAPJOY_URL_CONNECTION, "--------------------");
            return httpResponse;
        } catch (Exception e) {
            TapjoyLog.e(TAPJOY_URL_CONNECTION, "Exception: " + e.toString());
            return httpResponse;
        }
    }
}
