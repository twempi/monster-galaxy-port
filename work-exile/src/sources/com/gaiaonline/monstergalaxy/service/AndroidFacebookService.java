package com.gaiaonline.monstergalaxy.service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import com.badlogic.gdx.Gdx;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxyAndroid;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;
import com.millennialmedia.android.MMAdViewSDK;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import org.json.JSONObject;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class AndroidFacebookService implements FacebookService {
    private static final String[] PERMISSIONS = {MMAdViewSDK.Event.INTENT_EMAIL, "publish_stream"};
    public String FACEBOOK_APP_ID;
    private MonsterGalaxyAndroid activity;
    private AsyncFacebookRunner asyncFacebookRunner;
    private Facebook facebook;

    public AndroidFacebookService(PlatformHelper.MGSkin sk) {
        this.FACEBOOK_APP_ID = "215607168503093";
        if (sk == PlatformHelper.MGSkin.SK2) {
            this.FACEBOOK_APP_ID = "429621697062014";
        } else {
            this.FACEBOOK_APP_ID = "215607168503093";
        }
        this.facebook = new Facebook(this.FACEBOOK_APP_ID);
        this.asyncFacebookRunner = new AsyncFacebookRunner(this.facebook);
    }

    @Override // com.gaiaonline.monstergalaxy.service.FacebookService
    public boolean isSessionValid() {
        return this.facebook.isSessionValid();
    }

    @Override // com.gaiaonline.monstergalaxy.service.FacebookService
    public void authorize(final FacebookService.OnCompletedHandler onCompletedHandler) {
        if (!this.facebook.isSessionValid()) {
            this.activity.runOnUiThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.service.AndroidFacebookService.1
                @Override // java.lang.Runnable
                public void run() {
                    Facebook facebook = AndroidFacebookService.this.facebook;
                    MonsterGalaxyAndroid monsterGalaxyAndroid = AndroidFacebookService.this.activity;
                    String[] strArr = AndroidFacebookService.PERMISSIONS;
                    final FacebookService.OnCompletedHandler onCompletedHandler2 = onCompletedHandler;
                    facebook.authorize(monsterGalaxyAndroid, strArr, new Facebook.DialogListener() { // from class: com.gaiaonline.monstergalaxy.service.AndroidFacebookService.1.1
                        @Override // com.facebook.android.Facebook.DialogListener
                        public void onComplete(Bundle values) {
                            SharedPreferences.Editor editor = AndroidFacebookService.this.getSharedPreferences().edit();
                            editor.putString("facebook_access_token", AndroidFacebookService.this.facebook.getAccessToken());
                            editor.putLong("facebook_access_expires", AndroidFacebookService.this.facebook.getAccessExpires());
                            editor.commit();
                            onCompletedHandler2.onCompleted(true);
                        }

                        @Override // com.facebook.android.Facebook.DialogListener
                        public void onFacebookError(FacebookError error) {
                            AndroidFacebookService.this.showMessage(error.getMessage());
                            onCompletedHandler2.onCompleted(false);
                        }

                        @Override // com.facebook.android.Facebook.DialogListener
                        public void onError(DialogError e) {
                            AndroidFacebookService.this.showMessage(e.getMessage());
                            onCompletedHandler2.onCompleted(false);
                        }

                        @Override // com.facebook.android.Facebook.DialogListener
                        public void onCancel() {
                            onCompletedHandler2.onCompleted(false);
                        }
                    });
                }
            });
        }
    }

    @Override // com.gaiaonline.monstergalaxy.service.FacebookService
    public void logout(final FacebookService.OnCompletedHandler onCompletedHandler) {
        this.asyncFacebookRunner.logout(this.activity, new AsyncFacebookRunner.RequestListener() { // from class: com.gaiaonline.monstergalaxy.service.AndroidFacebookService.2
            @Override // com.facebook.android.AsyncFacebookRunner.RequestListener
            public void onComplete(String response, Object state) {
                onCompletedHandler.onCompleted(true);
            }

            @Override // com.facebook.android.AsyncFacebookRunner.RequestListener
            public void onIOException(IOException e, Object state) {
                AndroidFacebookService.this.showMessage(e.getMessage());
                onCompletedHandler.onCompleted(false);
            }

            @Override // com.facebook.android.AsyncFacebookRunner.RequestListener
            public void onFileNotFoundException(FileNotFoundException e, Object state) {
                AndroidFacebookService.this.showMessage(e.getMessage());
                onCompletedHandler.onCompleted(false);
            }

            @Override // com.facebook.android.AsyncFacebookRunner.RequestListener
            public void onMalformedURLException(MalformedURLException e, Object state) {
                AndroidFacebookService.this.showMessage(e.getMessage());
                onCompletedHandler.onCompleted(false);
            }

            @Override // com.facebook.android.AsyncFacebookRunner.RequestListener
            public void onFacebookError(FacebookError e, Object state) {
                AndroidFacebookService.this.showMessage(e.getMessage());
                onCompletedHandler.onCompleted(false);
            }
        });
    }

    @Override // com.gaiaonline.monstergalaxy.service.FacebookService
    public void postMessage(final FacebookService.OnCompletedHandler onCompletedHandler, String text) {
        String shortUrl;
        String caption;
        String pic;
        if (PlatformHelper.getSourceStore() == PlatformHelper.Store.AMAZON) {
            if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2) {
                shortUrl = "http://amzn.to/SUDnG4";
                caption = "Monster Galaxy: Exile";
                pic = "http://d.cdn.gaiaonline.com/mogaexpansion/mge_icon_fb.png";
            } else {
                shortUrl = "http://amzn.to/MfUA0l";
                caption = "Monster Galaxy";
                pic = "http://d.cdn.gaiaonline.com/mogaios/promo/monsterg_icon_fb.png";
            }
        } else if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2) {
            shortUrl = "http://bit.ly/OFlkbm";
            caption = "Monster Galaxy: Exile";
            pic = "http://d.cdn.gaiaonline.com/mogaexpansion/mge_icon_fb.png";
        } else {
            shortUrl = "http://bit.ly/GIQfzt";
            caption = "Monster Galaxy";
            pic = "http://d.cdn.gaiaonline.com/mogaios/promo/monsterg_icon_fb.png";
        }
        final Bundle bundle = new Bundle();
        bundle.putString("description", text);
        bundle.putString("link", shortUrl);
        bundle.putString("caption", caption);
        bundle.putString("picture", pic);
        this.activity.runOnUiThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.service.AndroidFacebookService.3
            @Override // java.lang.Runnable
            public void run() {
                Facebook facebook = AndroidFacebookService.this.facebook;
                MonsterGalaxyAndroid monsterGalaxyAndroid = AndroidFacebookService.this.activity;
                Bundle bundle2 = bundle;
                final FacebookService.OnCompletedHandler onCompletedHandler2 = onCompletedHandler;
                facebook.dialog(monsterGalaxyAndroid, "feed", bundle2, new Facebook.DialogListener() { // from class: com.gaiaonline.monstergalaxy.service.AndroidFacebookService.3.1
                    @Override // com.facebook.android.Facebook.DialogListener
                    public void onFacebookError(FacebookError e) {
                        AndroidFacebookService.this.showMessage(e.getMessage());
                        onCompletedHandler2.onCompleted(false);
                    }

                    @Override // com.facebook.android.Facebook.DialogListener
                    public void onError(DialogError e) {
                        AndroidFacebookService.this.showMessage(e.getMessage());
                        onCompletedHandler2.onCompleted(false);
                    }

                    @Override // com.facebook.android.Facebook.DialogListener
                    public void onComplete(Bundle values) {
                        onCompletedHandler2.onCompleted(true);
                    }

                    @Override // com.facebook.android.Facebook.DialogListener
                    public void onCancel() {
                        onCompletedHandler2.onCompleted(false);
                    }
                });
            }
        });
    }

    public void setActivity(MonsterGalaxyAndroid activity) {
        this.activity = activity;
        Boolean resp = Boolean.valueOf(this.facebook.publishInstall(activity));
        Gdx.app.log("FACEBOOK TRACK", resp.toString());
    }

    public void updateAccessToken() {
        SharedPreferences sharedPrefs = getSharedPreferences();
        String accessToken = sharedPrefs.getString("facebook_access_token", null);
        long expires = sharedPrefs.getLong("facebook_access_expires", 0L);
        if (accessToken != null) {
            this.facebook.setAccessToken(accessToken);
        }
        if (expires != 0) {
            this.facebook.setAccessExpires(expires);
        }
    }

    public void authorizeCallback(int requestCode, int resultCode, Intent data) {
        this.facebook.authorizeCallback(requestCode, resultCode, data);
    }

    public void extendAccessTokenIfNeeded() {
        this.facebook.extendAccessTokenIfNeeded(this.activity, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SharedPreferences getSharedPreferences() {
        return this.activity.getPreferences(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showMessage(final String message) {
        this.activity.runOnUiThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.service.AndroidFacebookService.4
            @Override // java.lang.Runnable
            public void run() {
                Toast.makeText(AndroidFacebookService.this.activity, message, 1).show();
            }
        });
    }

    public String getFacebookId() {
        try {
            if (!this.facebook.isSessionValid()) {
                return null;
            }
            JSONObject request = new JSONObject(this.facebook.request("me"));
            String facebookId = request.getString("id");
            return facebookId;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
