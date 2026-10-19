package com.sessionm.net.http;

import android.webkit.CookieManager;
import com.sessionm.a.b;
import com.sessionm.a.e;
import com.sessionm.core.SessionMAndroidConfig;
import com.sessionm.net.HttpClient;
import com.sessionm.net.HttpResponseHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Client implements HttpClient {
    private CookieStore cookieStore = new BasicCookieStore();
    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Override // com.sessionm.net.HttpClient
    public CookieStore getCookieStore() {
        return this.cookieStore;
    }

    @Override // com.sessionm.net.HttpClient
    public void setCookieStore(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }

    @Override // com.sessionm.net.HttpClient
    public void sendRequest(String str, String str2, String str3, HttpResponseHandler httpResponseHandler) {
        Request request = new Request(str, str2, str3, httpResponseHandler);
        request.setCookieStore(this.cookieStore);
        this.executorService.execute(request);
    }

    @Override // com.sessionm.net.HttpClient
    public void syncCookies() {
        synchronized (CookieManager.class) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            String str = (String) SessionMAndroidConfig.getInstance().getConfigurationByKey(b.b);
            String strD = e.d(str);
            for (Cookie cookie : getCookieStore().getCookies()) {
                String str2 = cookie.getName() + "=" + cookie.getValue();
                String domain = cookie.getDomain();
                if (domain != null && domain.length() > 0) {
                    domain = strD;
                }
                cookieManager.setCookie(str, str2 + "; domain=" + domain);
            }
        }
    }

    @Override // com.sessionm.net.HttpClient
    public void storeCookies() {
        synchronized (CookieManager.class) {
            CookieManager cookieManager = CookieManager.getInstance();
            String str = (String) SessionMAndroidConfig.getInstance().getConfigurationByKey(b.b);
            String strD = e.d(str);
            String cookie = cookieManager.getCookie(str);
            if (cookie != null) {
                String[] strArrSplit = cookie.split("; ");
                CookieStore cookieStore = getCookieStore();
                for (String str2 : strArrSplit) {
                    int iIndexOf = str2.indexOf(61);
                    BasicClientCookie basicClientCookie = new BasicClientCookie(str2.substring(0, iIndexOf), str2.substring(iIndexOf + 1));
                    basicClientCookie.setDomain(strD);
                    cookieStore.addCookie(basicClientCookie);
                }
            }
        }
    }
}
