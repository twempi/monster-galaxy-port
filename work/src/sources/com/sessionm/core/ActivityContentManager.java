package com.sessionm.core;

import com.sessionm.json.JSONObject;
import com.sessionm.logging.Logger;
import com.sessionm.net.HttpClient;
import com.sessionm.net.Request;
import com.sessionm.net.RequestListener;
import com.sessionm.net.http.Client;
import java.util.ArrayList;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public class ActivityContentManager {
    private static final String TAG = "ActivityContentManager";
    private final ArrayList<String> pendingUrls = new ArrayList<>();
    private final ArrayList<String> loadedUrls = new ArrayList<>();
    private final HttpClient httpClient = new Client();

    /* JADX INFO: Access modifiers changed from: private */
    public void loadNextActivity() {
        synchronized (this) {
            if (this.pendingUrls.size() > 0) {
                String strRemove = this.pendingUrls.remove(0);
                Request request = new Request(strRemove, Request.Type.CONTENT, (Object) null);
                this.loadedUrls.add(strRemove);
                request.setClient(this.httpClient);
                request.setListener(new RequestListener() { // from class: com.sessionm.core.ActivityContentManager.1
                    @Override // com.sessionm.net.RequestListener
                    public void onRequestSent(Request request2) {
                    }

                    @Override // com.sessionm.net.RequestListener
                    public void onReplyReceived(Request request2) {
                        ActivityContentManager.this.loadNextActivity();
                    }
                });
                request.send();
                Logger.debug(TAG, String.format("Preloading, URL: %s", strRemove));
            }
        }
    }

    public synchronized void loadContent(JSONObject jSONObject) {
        this.httpClient.setCookieStore(Session.getSession().getRequestQueue().getHttpClient().getCookieStore());
        JSONObject[] jSONObjectArr = (JSONObject[]) jSONObject.getArray("ad_forecast", JSONObject.class);
        if (jSONObjectArr != null) {
            for (JSONObject jSONObject2 : jSONObjectArr) {
                String string = jSONObject2.getString("preload_url");
                if (string == null) {
                    Logger.error(TAG, "Missing preload_url property in forecast");
                } else if (!this.pendingUrls.contains(string) && !this.loadedUrls.contains(string)) {
                    this.pendingUrls.add(string);
                }
            }
            if (this.pendingUrls.size() > 0) {
                loadNextActivity();
            }
        }
    }

    public synchronized void cancelLoading() {
        this.pendingUrls.clear();
    }
}
