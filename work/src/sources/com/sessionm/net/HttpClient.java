package com.sessionm.net;

import org.apache.http.client.CookieStore;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public interface HttpClient {
    CookieStore getCookieStore();

    void sendRequest(String str, String str2, String str3, HttpResponseHandler httpResponseHandler);

    void setCookieStore(CookieStore cookieStore);

    void storeCookies();

    void syncCookies();
}
