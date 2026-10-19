package com.millennialmedia.android;

import android.content.Context;
import com.sessionm.ui.SessionMActivity;
import java.io.IOException;
import java.util.HashMap;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONArray;

/* JADX INFO: loaded from: classes.dex */
class MMCache extends MMJSObject implements AdCache.AdCacheTaskListener {
    private boolean success;

    MMCache() {
    }

    public MMJSResponse videoIdExists(HashMap<String, String> arguments) {
        VideoAd ad;
        Context context = this.contextRef.get();
        String id = arguments.get("videoId");
        if (id == null || context == null || (ad = (VideoAd) AdCache.load(context, id)) == null || !ad.isOnDisk(context) || ad.isExpired()) {
            return null;
        }
        return MMJSResponse.responseWithSuccess();
    }

    public MMJSResponse availableCachedVideos(HashMap<String, String> arguments) throws Throwable {
        final Context context = this.contextRef.get();
        if (context == null) {
            return null;
        }
        final JSONArray array = new JSONArray();
        AdCache.iterateCachedAds(context, 2, new AdCache.Iterator() { // from class: com.millennialmedia.android.MMCache.1
            @Override // com.millennialmedia.android.AdCache.Iterator
            boolean callback(CachedAd ad) {
                if (ad.getType() == 1 && ad.isOnDisk(context) && !ad.isExpired()) {
                    array.put(ad.id);
                }
                return true;
            }
        });
        MMJSResponse response = new MMJSResponse();
        response.result = 1;
        response.response = array;
        return response;
    }

    public MMJSResponse playCachedVideo(HashMap<String, String> arguments) {
        VideoAd ad;
        Context context = this.contextRef.get();
        String id = arguments.get("videoId");
        if (id == null || context == null || (ad = (VideoAd) AdCache.load(context, id)) == null || !ad.canShow(context, null, false)) {
            return null;
        }
        ad.show(context, null);
        return MMJSResponse.responseWithSuccess();
    }

    @Override // com.millennialmedia.android.AdCache.AdCacheTaskListener
    public void downloadCompleted(CachedAd ad, boolean success) {
        synchronized (this) {
            Context context = this.contextRef.get();
            if (success && context != null) {
                AdCache.save(context, ad);
            }
            this.success = success;
            notify();
        }
    }

    public synchronized MMJSResponse cacheVideo(HashMap<String, String> arguments) {
        MMJSResponse mMJSResponseResponseWithSuccess;
        Context context = this.contextRef.get();
        String url = arguments.get(SessionMActivity.INTENT_PARAM_URL);
        if (url != null && context != null) {
            System.out.println(url);
            try {
                HttpGetRequest httpGetRequest = new HttpGetRequest();
                HttpResponse httpResponse = httpGetRequest.get(url);
                if (httpResponse == null) {
                    MMAdViewSDK.Log.d("HTTP response is null");
                    mMJSResponseResponseWithSuccess = null;
                } else {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    if (httpEntity == null) {
                        MMAdViewSDK.Log.d("Null HTTP entity");
                        mMJSResponseResponseWithSuccess = null;
                    } else if (httpEntity.getContentLength() == 0) {
                        MMAdViewSDK.Log.d("Millennial ad return failed. Zero content length returned.");
                        mMJSResponseResponseWithSuccess = null;
                    } else {
                        Header httpHeader = httpEntity.getContentType();
                        if (httpHeader != null && httpHeader.getValue() != null && httpHeader.getValue().equalsIgnoreCase("application/json")) {
                            try {
                                String json = HttpGetRequest.convertStreamToString(httpEntity.getContent());
                                VideoAd videoAd = new VideoAd(json);
                                if (videoAd != null && videoAd.isValid()) {
                                    try {
                                        videoAd.downloadPriority = 3;
                                        AdCache.startDownloadTask(context, null, videoAd, this);
                                        wait();
                                        if (this.success) {
                                            mMJSResponseResponseWithSuccess = MMJSResponse.responseWithSuccess();
                                        }
                                    } catch (InterruptedException e) {
                                        MMAdViewSDK.Log.e(e);
                                        MMAdViewSDK.Log.e("Caching interrupted: %s", e.getMessage());
                                    }
                                }
                            } catch (IOException e1) {
                                e1.printStackTrace();
                                MMAdViewSDK.Log.d("Millennial ad return failed. Invalid response data.");
                                mMJSResponseResponseWithSuccess = null;
                            } catch (IllegalStateException e2) {
                                e2.printStackTrace();
                                MMAdViewSDK.Log.d("Millennial ad return failed. Invalid response data.");
                                mMJSResponseResponseWithSuccess = null;
                            }
                        }
                    }
                }
            } catch (Exception e3) {
                MMAdViewSDK.Log.d("HTTP error: " + e3.getMessage());
                mMJSResponseResponseWithSuccess = null;
            }
        }
        mMJSResponseResponseWithSuccess = null;
        return mMJSResponseResponseWithSuccess;
    }
}
