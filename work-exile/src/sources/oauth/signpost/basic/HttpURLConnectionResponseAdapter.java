package oauth.signpost.basic;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import oauth.signpost.http.HttpResponse;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class HttpURLConnectionResponseAdapter implements HttpResponse {
    private HttpURLConnection connection;

    public HttpURLConnectionResponseAdapter(HttpURLConnection connection) {
        this.connection = connection;
    }

    @Override // oauth.signpost.http.HttpResponse
    public InputStream getContent() throws IOException {
        return this.connection.getInputStream();
    }

    @Override // oauth.signpost.http.HttpResponse
    public int getStatusCode() throws IOException {
        return this.connection.getResponseCode();
    }

    @Override // oauth.signpost.http.HttpResponse
    public String getReasonPhrase() throws Exception {
        return this.connection.getResponseMessage();
    }

    @Override // oauth.signpost.http.HttpResponse
    public Object unwrap() {
        return this.connection;
    }
}
