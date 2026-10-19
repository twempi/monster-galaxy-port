package com.sessionm.net.http;

import com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http.HttpEngine;
import com.sessionm.logging.Logger;
import com.sessionm.net.CookieUtils;
import com.sessionm.net.HttpResponseHandler;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import oauth.signpost.OAuth;
import org.apache.http.client.CookieStore;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.cookie.Cookie;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class Request implements Runnable {
    private static final String TAG = "AsyncRequest";
    private CookieStore cookieStore;
    private Throwable error;
    private final String method;
    private final String paramString;
    private String response;
    private final HttpResponseHandler responseHandler;
    private int statusCode;
    private final String url;

    public Request(String str, String str2, String str3, HttpResponseHandler httpResponseHandler) {
        this.responseHandler = httpResponseHandler;
        this.url = str;
        this.method = str2;
        this.paramString = str3;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            if (this.responseHandler != null) {
                this.responseHandler.notifyRequestSent();
            }
            sendRequest(this.url, this.method, this.paramString, this.responseHandler);
            if (this.responseHandler != null) {
                this.responseHandler.notifyRequestFinished(this.response, this.error, this.statusCode);
            }
        } catch (IOException e) {
            if (this.responseHandler != null) {
                this.responseHandler.notifyRequestFinished(null, e, -1);
            }
        }
    }

    private void sendRequest(String str, String str2, String str3, HttpResponseHandler httpResponseHandler) throws IOException {
        HttpURLConnection.setFollowRedirects(true);
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        if (httpURLConnection instanceof HttpsURLConnection) {
            ((HttpsURLConnection) httpURLConnection).setHostnameVerifier(new AllowAllHostnameVerifier());
        }
        httpURLConnection.setRequestProperty("User-Agent", System.getProperties().getProperty("http.agent") + " SessionM Android SDK");
        httpURLConnection.setRequestMethod(str2.toString());
        httpResponseHandler.notifyRequestSent();
        StringBuilder sb = new StringBuilder();
        List<Cookie> cookies = this.cookieStore.getCookies();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= cookies.size()) {
                break;
            }
            Cookie cookie = cookies.get(i2);
            sb.append(cookie.getName());
            sb.append("=");
            sb.append(cookie.getValue());
            if (i2 < cookies.size() - 1) {
                sb.append("; ");
            }
            i = i2 + 1;
        }
        httpURLConnection.setRequestProperty("Cookie", sb.toString());
        if (str2.equalsIgnoreCase(HttpEngine.POST) || str2.equalsIgnoreCase(HttpEngine.PUT)) {
            httpURLConnection.setRequestProperty("Content-Type", OAuth.FORM_ENCODED);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(httpURLConnection.getOutputStream());
            bufferedOutputStream.write(str3.getBytes());
            bufferedOutputStream.flush();
        }
        Logger.debug(TAG, "Sending request with url: " + str);
        try {
            this.response = readStream(new BufferedInputStream(httpURLConnection.getInputStream()));
            this.statusCode = httpURLConnection.getResponseCode();
            List<String> list = httpURLConnection.getHeaderFields().get("Set-Cookie");
            if (list != null) {
                Iterator<String> it = list.iterator();
                while (it.hasNext()) {
                    Iterator<Cookie> it2 = CookieUtils.parse(it.next()).iterator();
                    while (it2.hasNext()) {
                        this.cookieStore.addCookie(it2.next());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            this.response = readStream(new BufferedInputStream(httpURLConnection.getErrorStream()));
            this.error = e;
            this.statusCode = httpURLConnection.getResponseCode();
        }
    }

    private static String readStream(InputStream inputStream) throws IOException {
        byte[] bArr = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        while (true) {
            int i = inputStream.read(bArr);
            if (i != -1) {
                byteArrayOutputStream.write(bArr, 0, i);
            } else {
                return new String(byteArrayOutputStream.toByteArray());
            }
        }
    }

    public void setCookieStore(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }
}
