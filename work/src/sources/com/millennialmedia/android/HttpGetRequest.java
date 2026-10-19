package com.millennialmedia.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;
import oauth.signpost.OAuth;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

/* JADX INFO: loaded from: classes.dex */
class HttpGetRequest {
    private HttpClient client = new DefaultHttpClient();
    private HttpGet getRequest = new HttpGet();

    HttpGetRequest() {
    }

    HttpResponse get(String url) throws Exception {
        try {
            this.getRequest.setURI(new URI(url));
            return this.client.execute(this.getRequest);
        } catch (OutOfMemoryError e) {
            MMAdViewSDK.Log.e("Out of memory!");
            return null;
        }
    }

    void trackConversion(String goalId, String auid, boolean isFirstLaunch, long installTime, TreeMap<String, String> extraParams) throws Exception {
        int i = isFirstLaunch ? 1 : 0;
        try {
            StringBuilder urlBuilder = new StringBuilder("http://cvt.mydas.mobi/handleConversion?firstlaunch=" + i);
            if (goalId != null) {
                urlBuilder.append("&goalId=" + goalId);
            }
            if (auid != null) {
                if (auid.startsWith("mmh_")) {
                    urlBuilder.append("&hdid=" + URLEncoder.encode(auid));
                } else {
                    urlBuilder.append("&auid=" + URLEncoder.encode(auid));
                }
            }
            if (installTime > 0) {
                urlBuilder.append("&installtime=" + (installTime / 1000));
            }
            if (extraParams != null) {
                for (Map.Entry<String, String> entry : extraParams.entrySet()) {
                    urlBuilder.append(String.format("&%s=%s", entry.getKey(), URLEncoder.encode(entry.getValue(), OAuth.ENCODING)));
                }
            }
            String url = urlBuilder.toString();
            MMAdViewSDK.Log.d("Sending conversion tracker report: %s", url);
            this.getRequest.setURI(new URI(url));
            HttpResponse response = this.client.execute(this.getRequest);
            if (response.getStatusLine().getStatusCode() == 200) {
                MMAdViewSDK.Log.v("Conversion tracker reponse code: %d", Integer.valueOf(response.getStatusLine().getStatusCode()));
            } else {
                MMAdViewSDK.Log.e("Conversion tracker unable to complete report: %d", Integer.valueOf(response.getStatusLine().getStatusCode()));
            }
        } catch (IOException e) {
            MMAdViewSDK.Log.e("Unable to track conversion. %s", e.getMessage());
        }
    }

    static String convertStreamToString(InputStream is) throws Throwable {
        BufferedReader reader = null;
        if (is == null) {
            throw new IOException("Stream is null.");
        }
        try {
            try {
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(is), Opcodes.ACC_SYNTHETIC);
                try {
                    StringBuilder sb = new StringBuilder();
                    while (true) {
                        String line = reader2.readLine();
                        if (line == null) {
                            break;
                        }
                        sb.append(line + "\n");
                    }
                    if (reader2 != null) {
                        try {
                            reader2.close();
                        } catch (IOException e) {
                            MMAdViewSDK.Log.d(e);
                        }
                    }
                    return sb.toString();
                } catch (OutOfMemoryError e2) {
                    e = e2;
                    reader = reader2;
                    MMAdViewSDK.Log.d(e);
                    throw new IOException("Out of memory.");
                } catch (Throwable th) {
                    th = th;
                    reader = reader2;
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e3) {
                            MMAdViewSDK.Log.d(e3);
                        }
                    }
                    throw th;
                }
            } catch (OutOfMemoryError e4) {
                e = e4;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    static void log(final String url) {
        if (url != null && url.length() > 0) {
            new Thread(new Runnable() { // from class: com.millennialmedia.android.HttpGetRequest.1
                @Override // java.lang.Runnable
                public void run() {
                    MMAdViewSDK.Log.v("Logging event to: %s", url);
                    HttpGetRequest getRequest = new HttpGetRequest();
                    try {
                        getRequest.get(url);
                    } catch (Exception e) {
                        MMAdViewSDK.Log.v(e.getMessage());
                    }
                }
            }).start();
        }
    }

    static void log(final String[] urls) {
        if (urls != null && urls.length > 0) {
            new Thread(new Runnable() { // from class: com.millennialmedia.android.HttpGetRequest.2
                @Override // java.lang.Runnable
                public void run() {
                    String[] arr$ = urls;
                    for (String url : arr$) {
                        MMAdViewSDK.Log.v("Logging event to: %s", url);
                        try {
                            HttpGetRequest getRequest = new HttpGetRequest();
                            getRequest.get(url);
                        } catch (Exception e) {
                            MMAdViewSDK.Log.v(e.getMessage());
                        }
                    }
                }
            }).start();
        }
    }
}
