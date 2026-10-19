package com.facebook.android;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import android.webkit.CookieSyncManager;
import com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http.HttpEngine;
import com.sessionm.ui.SessionMActivity;
import com.tapjoy.TapjoyConstants;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class Facebook {
    private static final String ANALYTICS_EVENT = "event";
    private static final String APPLICATION_FIELDS = "fields";
    public static final String ATTRIBUTION_ID_COLUMN_NAME = "aid";
    private static final String ATTRIBUTION_KEY = "attribution";
    private static final String ATTRIBUTION_PREFERENCES = "com.facebook.sdk.attributionTracking";
    public static final String CANCEL_URI = "fbconnect://cancel";
    private static final int DEFAULT_AUTH_ACTIVITY_CODE = 32665;
    public static final String EXPIRES = "expires_in";
    public static final String FB_APP_SIGNATURE = "30820268308201d102044a9c4610300d06092a864886f70d0101040500307a310b3009060355040613025553310b3009060355040813024341311230100603550407130950616c6f20416c746f31183016060355040a130f46616365626f6f6b204d6f62696c653111300f060355040b130846616365626f6f6b311d301b0603550403131446616365626f6f6b20436f72706f726174696f6e3020170d3039303833313231353231365a180f32303530303932353231353231365a307a310b3009060355040613025553310b3009060355040813024341311230100603550407130950616c6f20416c746f31183016060355040a130f46616365626f6f6b204d6f62696c653111300f060355040b130846616365626f6f6b311d301b0603550403131446616365626f6f6b20436f72706f726174696f6e30819f300d06092a864886f70d010101050003818d0030818902818100c207d51df8eb8c97d93ba0c8c1002c928fab00dc1b42fca5e66e99cc3023ed2d214d822bc59e8e35ddcf5f44c7ae8ade50d7e0c434f500e6c131f4a2834f987fc46406115de2018ebbb0d5a3c261bd97581ccfef76afc7135a6d59e8855ecd7eacc8f8737e794c60a761c536b72b11fac8e603f5da1a2d54aa103b8a13c0dbc10203010001300d06092a864886f70d0101040500038181005ee9be8bcbb250648d3b741290a82a1c9dc2e76a0af2f2228f1d9f9c4007529c446a70175c5a900d5141812866db46be6559e2141616483998211f4a673149fb2232a10d247663b26a9031e15f84bc1c74d141ff98a02d76f85b2c8ab2571b6469b232d8e768a7f7ca04f7abe4a775615916c07940656b58717457b42bd928a2";
    public static final int FORCE_DIALOG_AUTH = -1;
    private static final String LOGIN = "oauth";
    private static final String MOBILE_INSTALL_EVENT = "MOBILE_APP_INSTALL";
    private static final String PUBLISH_ACTIVITY_PATH = "%s/activities";
    public static final String REDIRECT_URI = "fbconnect://success";
    public static final String SINGLE_SIGN_ON_DISABLED = "service_disabled";
    private static final String SUPPORTS_ATTRIBUTION = "supports_attribution";
    public static final String TOKEN = "access_token";
    private String mAppId;
    private Activity mAuthActivity;
    private int mAuthActivityCode;
    private DialogListener mAuthDialogListener;
    private String[] mAuthPermissions;
    public static final Uri ATTRIBUTION_ID_CONTENT_URI = Uri.parse("content://com.facebook.katana.provider.AttributionIdProvider");
    protected static String DIALOG_BASE_URL = "https://m.facebook.com/dialog/";
    protected static String GRAPH_BASE_URL = "https://graph.facebook.com/";
    protected static String RESTSERVER_URL = "https://api.facebook.com/restserver.php";
    private String mAccessToken = null;
    private long mLastAccessUpdate = 0;
    private long mAccessExpires = 0;
    private final long REFRESH_TOKEN_BARRIER = 86400000;
    private boolean shouldAutoPublishInstall = true;
    private AutoPublishAsyncTask mAutoPublishAsyncTask = null;

    public interface DialogListener {
        void onCancel();

        void onComplete(Bundle bundle);

        void onError(DialogError dialogError);

        void onFacebookError(FacebookError facebookError);
    }

    public interface ServiceListener {
        void onComplete(Bundle bundle);

        void onError(Error error);

        void onFacebookError(FacebookError facebookError);
    }

    public Facebook(String appId) {
        if (appId == null) {
            throw new IllegalArgumentException("You must specify your application ID when instantiating a Facebook object. See README for details.");
        }
        this.mAppId = appId;
    }

    public void authorize(Activity activity, DialogListener listener) {
        authorize(activity, new String[0], DEFAULT_AUTH_ACTIVITY_CODE, listener);
    }

    public void authorize(Activity activity, String[] permissions, DialogListener listener) {
        authorize(activity, permissions, DEFAULT_AUTH_ACTIVITY_CODE, listener);
    }

    public void authorize(Activity activity, String[] permissions, int activityCode, DialogListener listener) {
        boolean singleSignOnStarted = false;
        this.mAuthDialogListener = listener;
        autoPublishAsync(activity.getApplicationContext());
        if (activityCode >= 0) {
            singleSignOnStarted = startSingleSignOn(activity, this.mAppId, permissions, activityCode);
        }
        if (!singleSignOnStarted) {
            startDialogAuth(activity, permissions);
        }
    }

    private boolean startSingleSignOn(Activity activity, String applicationId, String[] permissions, int activityCode) {
        boolean didSucceed = true;
        Intent intent = new Intent();
        intent.setClassName("com.facebook.katana", "com.facebook.katana.ProxyAuth");
        intent.putExtra("client_id", applicationId);
        if (permissions.length > 0) {
            intent.putExtra("scope", TextUtils.join(",", permissions));
        }
        if (!validateActivityIntent(activity, intent)) {
            return false;
        }
        this.mAuthActivity = activity;
        this.mAuthPermissions = permissions;
        this.mAuthActivityCode = activityCode;
        try {
            activity.startActivityForResult(intent, activityCode);
        } catch (ActivityNotFoundException e) {
            didSucceed = false;
        }
        return didSucceed;
    }

    private boolean validateActivityIntent(Context context, Intent intent) {
        ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(intent, 0);
        if (resolveInfo == null) {
            return false;
        }
        return validateAppSignatureForPackage(context, resolveInfo.activityInfo.packageName);
    }

    private boolean validateServiceIntent(Context context, Intent intent) {
        ResolveInfo resolveInfo = context.getPackageManager().resolveService(intent, 0);
        if (resolveInfo == null) {
            return false;
        }
        return validateAppSignatureForPackage(context, resolveInfo.serviceInfo.packageName);
    }

    private boolean validateAppSignatureForPackage(Context context, String packageName) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 64);
            for (Signature signature : packageInfo.signatures) {
                if (signature.toCharsString().equals(FB_APP_SIGNATURE)) {
                    return true;
                }
            }
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void startDialogAuth(Activity activity, String[] permissions) {
        Bundle params = new Bundle();
        if (permissions.length > 0) {
            params.putString("scope", TextUtils.join(",", permissions));
        }
        CookieSyncManager.createInstance(activity);
        dialog(activity, LOGIN, params, new DialogListener() { // from class: com.facebook.android.Facebook.1
            @Override // com.facebook.android.Facebook.DialogListener
            public void onComplete(Bundle values) {
                CookieSyncManager.getInstance().sync();
                Facebook.this.setAccessToken(values.getString(Facebook.TOKEN));
                Facebook.this.setAccessExpiresIn(values.getString(Facebook.EXPIRES));
                if (Facebook.this.isSessionValid()) {
                    Util.logd("Facebook-authorize", "Login Success! access_token=" + Facebook.this.getAccessToken() + " expires=" + Facebook.this.getAccessExpires());
                    Facebook.this.mAuthDialogListener.onComplete(values);
                } else {
                    Facebook.this.mAuthDialogListener.onFacebookError(new FacebookError("Failed to receive access token."));
                }
            }

            @Override // com.facebook.android.Facebook.DialogListener
            public void onError(DialogError error) {
                Util.logd("Facebook-authorize", "Login failed: " + error);
                Facebook.this.mAuthDialogListener.onError(error);
            }

            @Override // com.facebook.android.Facebook.DialogListener
            public void onFacebookError(FacebookError error) {
                Util.logd("Facebook-authorize", "Login failed: " + error);
                Facebook.this.mAuthDialogListener.onFacebookError(error);
            }

            @Override // com.facebook.android.Facebook.DialogListener
            public void onCancel() {
                Util.logd("Facebook-authorize", "Login canceled");
                Facebook.this.mAuthDialogListener.onCancel();
            }
        });
    }

    public void authorizeCallback(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.mAuthActivityCode) {
            if (resultCode == -1) {
                String error = data.getStringExtra("error");
                if (error == null) {
                    error = data.getStringExtra("error_type");
                }
                if (error != null) {
                    if (error.equals(SINGLE_SIGN_ON_DISABLED) || error.equals("AndroidAuthKillSwitchException")) {
                        Util.logd("Facebook-authorize", "Hosted auth currently disabled. Retrying dialog auth...");
                        startDialogAuth(this.mAuthActivity, this.mAuthPermissions);
                        return;
                    } else {
                        if (error.equals("access_denied") || error.equals("OAuthAccessDeniedException")) {
                            Util.logd("Facebook-authorize", "Login canceled by user.");
                            this.mAuthDialogListener.onCancel();
                            return;
                        }
                        String description = data.getStringExtra("error_description");
                        if (description != null) {
                            error = String.valueOf(error) + ":" + description;
                        }
                        Util.logd("Facebook-authorize", "Login failed: " + error);
                        this.mAuthDialogListener.onFacebookError(new FacebookError(error));
                        return;
                    }
                }
                setAccessToken(data.getStringExtra(TOKEN));
                setAccessExpiresIn(data.getStringExtra(EXPIRES));
                if (isSessionValid()) {
                    Util.logd("Facebook-authorize", "Login Success! access_token=" + getAccessToken() + " expires=" + getAccessExpires());
                    this.mAuthDialogListener.onComplete(data.getExtras());
                    return;
                } else {
                    this.mAuthDialogListener.onFacebookError(new FacebookError("Failed to receive access token."));
                    return;
                }
            }
            if (resultCode == 0) {
                if (data != null) {
                    Util.logd("Facebook-authorize", "Login failed: " + data.getStringExtra("error"));
                    this.mAuthDialogListener.onError(new DialogError(data.getStringExtra("error"), data.getIntExtra("error_code", -1), data.getStringExtra("failing_url")));
                } else {
                    Util.logd("Facebook-authorize", "Login canceled by user.");
                    this.mAuthDialogListener.onCancel();
                }
            }
        }
    }

    public boolean extendAccessToken(Context context, ServiceListener serviceListener) {
        Intent intent = new Intent();
        intent.setClassName("com.facebook.katana", "com.facebook.katana.platform.TokenRefreshService");
        if (!validateServiceIntent(context, intent)) {
            return false;
        }
        return context.bindService(intent, new TokenRefreshServiceConnection(context, serviceListener), 1);
    }

    public boolean extendAccessTokenIfNeeded(Context context, ServiceListener serviceListener) {
        if (shouldExtendAccessToken()) {
            return extendAccessToken(context, serviceListener);
        }
        return true;
    }

    public boolean shouldExtendAccessToken() {
        return isSessionValid() && System.currentTimeMillis() - this.mLastAccessUpdate >= 86400000;
    }

    private class TokenRefreshServiceConnection implements ServiceConnection {
        final Context applicationsContext;
        final Messenger messageReceiver = new Messenger(new Handler() { // from class: com.facebook.android.Facebook.TokenRefreshServiceConnection.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                String token = msg.getData().getString(Facebook.TOKEN);
                long expiresAt = msg.getData().getLong(Facebook.EXPIRES) * 1000;
                Bundle resultBundle = (Bundle) msg.getData().clone();
                resultBundle.putLong(Facebook.EXPIRES, expiresAt);
                if (token != null) {
                    Facebook.this.setAccessToken(token);
                    Facebook.this.setAccessExpires(expiresAt);
                    if (TokenRefreshServiceConnection.this.serviceListener != null) {
                        TokenRefreshServiceConnection.this.serviceListener.onComplete(resultBundle);
                    }
                } else if (TokenRefreshServiceConnection.this.serviceListener != null) {
                    String error = msg.getData().getString("error");
                    if (msg.getData().containsKey("error_code")) {
                        int errorCode = msg.getData().getInt("error_code");
                        TokenRefreshServiceConnection.this.serviceListener.onFacebookError(new FacebookError(error, null, errorCode));
                    } else {
                        ServiceListener serviceListener = TokenRefreshServiceConnection.this.serviceListener;
                        if (error == null) {
                            error = "Unknown service error";
                        }
                        serviceListener.onError(new Error(error));
                    }
                }
                TokenRefreshServiceConnection.this.applicationsContext.unbindService(TokenRefreshServiceConnection.this);
            }
        });
        Messenger messageSender = null;
        final ServiceListener serviceListener;

        public TokenRefreshServiceConnection(Context applicationsContext, ServiceListener serviceListener) {
            this.applicationsContext = applicationsContext;
            this.serviceListener = serviceListener;
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName className, IBinder service) {
            this.messageSender = new Messenger(service);
            refreshToken();
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName arg) {
            if (this.serviceListener != null) {
                this.serviceListener.onError(new Error("Service disconnected"));
            }
            this.applicationsContext.unbindService(this);
        }

        private void refreshToken() {
            Bundle requestData = new Bundle();
            requestData.putString(Facebook.TOKEN, Facebook.this.mAccessToken);
            Message request = Message.obtain();
            request.setData(requestData);
            request.replyTo = this.messageReceiver;
            try {
                this.messageSender.send(request);
            } catch (RemoteException e) {
                this.serviceListener.onError(new Error("Service connection error"));
            }
        }
    }

    public String logout(Context context) throws IOException {
        Util.clearCookies(context);
        Bundle b = new Bundle();
        b.putString("method", "auth.expireSession");
        String response = request(b);
        setAccessToken(null);
        setAccessExpires(0L);
        return response;
    }

    public String request(Bundle parameters) throws IOException {
        if (!parameters.containsKey("method")) {
            throw new IllegalArgumentException("API method must be specified. (parameters must contain key \"method\" and value). See http://developers.facebook.com/docs/reference/rest/");
        }
        return request(null, parameters, HttpEngine.GET);
    }

    public String request(String graphPath) throws IOException {
        return request(graphPath, new Bundle(), HttpEngine.GET);
    }

    public String request(String graphPath, Bundle parameters) throws IOException {
        return request(graphPath, parameters, HttpEngine.GET);
    }

    public String request(String graphPath, Bundle params, String httpMethod) throws IOException {
        params.putString("format", "json");
        if (isSessionValid()) {
            params.putString(TOKEN, getAccessToken());
        }
        String url = graphPath != null ? String.valueOf(GRAPH_BASE_URL) + graphPath : RESTSERVER_URL;
        return Util.openUrl(url, httpMethod, params);
    }

    public void dialog(Context context, String action, DialogListener listener) {
        dialog(context, action, new Bundle(), listener);
    }

    public void dialog(Context context, String action, Bundle parameters, DialogListener listener) {
        String endpoint = String.valueOf(DIALOG_BASE_URL) + action;
        parameters.putString("display", "touch");
        parameters.putString("redirect_uri", REDIRECT_URI);
        if (action.equals(LOGIN)) {
            parameters.putString(SessionMActivity.INTENT_PARAM_TYPE, "user_agent");
            parameters.putString("client_id", this.mAppId);
        } else {
            parameters.putString(TapjoyConstants.TJC_APP_ID_NAME, this.mAppId);
        }
        if (isSessionValid()) {
            parameters.putString(TOKEN, getAccessToken());
        }
        String url = String.valueOf(endpoint) + "?" + Util.encodeUrl(parameters);
        if (context.checkCallingOrSelfPermission("android.permission.INTERNET") != 0) {
            Util.showAlert(context, "Error", "Application requires permission to access the Internet");
        } else {
            new FbDialog(context, url, listener).show();
        }
    }

    public boolean isSessionValid() {
        return getAccessToken() != null && (getAccessExpires() == 0 || System.currentTimeMillis() < getAccessExpires());
    }

    public String getAccessToken() {
        return this.mAccessToken;
    }

    public long getAccessExpires() {
        return this.mAccessExpires;
    }

    public long getLastAccessUpdate() {
        return this.mLastAccessUpdate;
    }

    public void setTokenFromCache(String accessToken, long accessExpires, long lastAccessUpdate) {
        this.mAccessToken = accessToken;
        this.mAccessExpires = accessExpires;
        this.mLastAccessUpdate = lastAccessUpdate;
    }

    public void setAccessToken(String token) {
        this.mAccessToken = token;
        this.mLastAccessUpdate = System.currentTimeMillis();
    }

    public void setAccessExpires(long time) {
        this.mAccessExpires = time;
    }

    public void setAccessExpiresIn(String expiresIn) {
        long expires;
        if (expiresIn != null) {
            if (expiresIn.equals("0")) {
                expires = 0;
            } else {
                expires = System.currentTimeMillis() + (Long.parseLong(expiresIn) * 1000);
            }
            setAccessExpires(expires);
        }
    }

    public String getAppId() {
        return this.mAppId;
    }

    public void setAppId(String appId) {
        this.mAppId = appId;
    }

    public static String getAttributionId(ContentResolver contentResolver) {
        String[] projection = {ATTRIBUTION_ID_COLUMN_NAME};
        Cursor c = contentResolver.query(ATTRIBUTION_ID_CONTENT_URI, projection, null, null, null);
        if (c == null || !c.moveToFirst()) {
            return null;
        }
        return c.getString(c.getColumnIndex(ATTRIBUTION_ID_COLUMN_NAME));
    }

    public boolean getShouldAutoPublishInstall() {
        return this.shouldAutoPublishInstall;
    }

    public void setShouldAutoPublishInstall(boolean value) {
        this.shouldAutoPublishInstall = value;
    }

    public boolean publishInstall(Context context) {
        try {
            String applicationId = this.mAppId;
            if (applicationId != null) {
                publishInstall(this, applicationId, context);
                return true;
            }
        } catch (Exception e) {
            Util.logd("Facebook-publish", e.getMessage());
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void publishInstall(Facebook fb, String applicationId, Context context) throws JSONException, IOException, FacebookError {
        String attributionId = getAttributionId(context.getContentResolver());
        SharedPreferences preferences = context.getSharedPreferences(ATTRIBUTION_PREFERENCES, 0);
        String pingKey = String.valueOf(applicationId) + "ping";
        long lastPing = preferences.getLong(pingKey, 0L);
        if (lastPing == 0 && attributionId != null) {
            Bundle supportsAttributionParams = new Bundle();
            supportsAttributionParams.putString(APPLICATION_FIELDS, SUPPORTS_ATTRIBUTION);
            JSONObject supportResponse = Util.parseJson(fb.request(applicationId, supportsAttributionParams));
            Boolean doesSupportAttribution = (Boolean) supportResponse.get(SUPPORTS_ATTRIBUTION);
            if (!(doesSupportAttribution instanceof Boolean)) {
                throw new JSONException(String.format("%s contains %s instead of a Boolean", SUPPORTS_ATTRIBUTION, doesSupportAttribution));
            }
            if (doesSupportAttribution.booleanValue()) {
                Bundle publishParams = new Bundle();
                publishParams.putString(ANALYTICS_EVENT, MOBILE_INSTALL_EVENT);
                publishParams.putString(ATTRIBUTION_KEY, attributionId);
                String publishUrl = String.format(PUBLISH_ACTIVITY_PATH, applicationId);
                fb.request(publishUrl, publishParams, HttpEngine.POST);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putLong(pingKey, System.currentTimeMillis());
                editor.commit();
            }
        }
    }

    void autoPublishAsync(Context context) {
        String applicationId;
        AutoPublishAsyncTask asyncTask = null;
        synchronized (this) {
            if (this.mAutoPublishAsyncTask == null && (applicationId = this.mAppId) != null) {
                AutoPublishAsyncTask asyncTask2 = new AutoPublishAsyncTask(applicationId, context);
                this.mAutoPublishAsyncTask = asyncTask2;
                asyncTask = asyncTask2;
            }
        }
        if (asyncTask != null) {
            asyncTask.execute(new Void[0]);
        }
    }

    private class AutoPublishAsyncTask extends AsyncTask<Void, Void, Void> {
        private final Context mApplicationContext;
        private final String mApplicationId;

        public AutoPublishAsyncTask(String applicationId, Context context) {
            this.mApplicationId = applicationId;
            this.mApplicationContext = context.getApplicationContext();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... voids) {
            try {
                Facebook.publishInstall(Facebook.this, this.mApplicationId, this.mApplicationContext);
                return null;
            } catch (Exception e) {
                Util.logd("Facebook-publish", e.getMessage());
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Void result) {
            synchronized (Facebook.this) {
                Facebook.this.mAutoPublishAsyncTask = null;
            }
        }
    }
}
