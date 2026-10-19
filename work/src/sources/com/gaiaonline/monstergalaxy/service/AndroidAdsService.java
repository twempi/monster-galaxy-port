package com.gaiaonline.monstergalaxy.service;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.badlogic.gdx.Gdx;
import com.chartboost.sdk.ChartBoost;
import com.chartboost.sdk.ChartBoostDelegate;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxyAndroid;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;

/* JADX INFO: loaded from: classes.dex */
public class AndroidAdsService extends AdsService {
    private static final String AD_CONTAINER_TAG = "adContainer";
    public static String MYAPID = "00000";
    private static final String TAG = "AndroidAdsService";
    private static ChartBoost chartBoost;
    private static AndroidAdsService instance;
    private static boolean isShowingInterstitial;
    private MonsterGalaxyAndroid activity;
    private FrameLayout adContainer;
    private Runnable onInterstitialDone;
    private long interval = 300000;
    private long lastAdTimeStamp = 0;
    ChartBoostDelegate aChartBoostDelegate = new ChartBoostDelegate() { // from class: com.gaiaonline.monstergalaxy.service.AndroidAdsService.1
        @Override // com.chartboost.sdk.ChartBoostDelegate
        public void didClickInterstitial(View interstitialView) {
            Gdx.app.log(AndroidAdsService.TAG, "didClickInterstitial(" + interstitialView + ")");
            super.didClickInterstitial(interstitialView);
            AndroidAdsService.this.onInterstitialDone();
        }

        @Override // com.chartboost.sdk.ChartBoostDelegate
        public void didCloseInterstitial(View interstitialView) {
            Gdx.app.log(AndroidAdsService.TAG, "ChartBoostDelegate didCloseInterstitial(" + interstitialView + ")");
            super.didCloseInterstitial(interstitialView);
            AndroidAdsService.this.onInterstitialDone();
        }

        @Override // com.chartboost.sdk.ChartBoostDelegate
        public void didDismissInterstitial(View interstitialView) {
            Gdx.app.log(AndroidAdsService.TAG, "ChartBoostDelegate didDismissInterstitial(" + interstitialView + ")");
            super.didDismissInterstitial(interstitialView);
            AndroidAdsService.this.onInterstitialDone();
        }

        @Override // com.chartboost.sdk.ChartBoostDelegate
        public void didFailToLoadInterstitial() {
            Gdx.app.log(AndroidAdsService.TAG, "ChartBoostDelegate didFailToLoadInterstitial()");
            super.didFailToLoadInterstitial();
            AndroidAdsService.this.onInterstitialDone();
        }

        @Override // com.chartboost.sdk.ChartBoostDelegate
        public boolean shouldDisplayInterstitial(View interstitialView) {
            Gdx.app.log(AndroidAdsService.TAG, String.valueOf(true) + " shouldDisplayInterstitial(" + interstitialView + ")");
            return true;
        }

        @Override // com.chartboost.sdk.ChartBoostDelegate
        public boolean shouldRequestInterstitial() {
            boolean ret = super.shouldRequestInterstitial();
            Gdx.app.log(AndroidAdsService.TAG, String.valueOf(ret) + " shouldRequestInterstitial()");
            return ret;
        }
    };

    private AndroidAdsService() {
    }

    public static synchronized AndroidAdsService getInstance() {
        if (instance == null) {
            instance = new AndroidAdsService();
        }
        return instance;
    }

    public void initialize(final Context context) {
        PlatformHelper.MGSkin sk = Game.getPlatformHelper().getSkin();
        if (sk == PlatformHelper.MGSkin.SK1) {
            if (PlatformHelper.getSourceStore() == PlatformHelper.Store.AMAZON) {
                APP_ID = "4fd01911f776591974000034";
                APP_SIGNATURE = "b05c03a55a7e7c25b24f92cf9dc3fee0c3e70d6e";
                Gdx.app.log(TAG, "Chartboost INIT AMAZON");
            } else {
                APP_ID = "4f764fe0f77659b310000043";
                APP_SIGNATURE = "e0e4be837dad4bccef8cf4021d10938d2ccecb36";
                Gdx.app.log(TAG, "Chartboost INIT GOOGLE PLAY");
            }
        } else if (PlatformHelper.getSourceStore() == PlatformHelper.Store.AMAZON) {
            APP_ID = "50049b1c2284bb0f2f00000f";
            APP_SIGNATURE = "6b657975c085d8480d850099c81616b3f3e2172b";
            Gdx.app.log(TAG, "Chartboost INIT AMAZON");
        } else {
            APP_ID = "5004797a9c890dc828000012";
            APP_SIGNATURE = "7574a17489349418daf091dc1999e131a5f18879";
            Gdx.app.log(TAG, "Chartboost INIT GOOGLE PLAY");
        }
        chartBoost = ChartBoost.getSharedChartBoost(context);
        chartBoost.setAppId(APP_ID);
        chartBoost.setAppSignature(APP_SIGNATURE);
        this.activity = (MonsterGalaxyAndroid) context;
        final FrameLayout rootGroup = (FrameLayout) ((MonsterGalaxyAndroid) context).getView().getParent();
        this.activity.runOnUiThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.service.AndroidAdsService.2
            @Override // java.lang.Runnable
            public void run() {
                AndroidAdsService.this.adContainer = (FrameLayout) rootGroup.findViewWithTag(AndroidAdsService.AD_CONTAINER_TAG);
                if (AndroidAdsService.this.adContainer == null) {
                    AndroidAdsService.this.adContainer = new FrameLayout(context);
                    AndroidAdsService.this.adContainer.setTag(AndroidAdsService.AD_CONTAINER_TAG);
                    AndroidAdsService.this.adContainer.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
                    rootGroup.addView(AndroidAdsService.this.adContainer, new ViewGroup.LayoutParams(-1, -1));
                    AndroidAdsService.chartBoost.setInterstitialDisplayContainer(AndroidAdsService.this.adContainer);
                }
            }
        });
        chartBoost.setDelegate(this.aChartBoostDelegate);
        if (!chartBoost.hasCachedInterstitial(AdsService.Location.APP_START.getId())) {
            chartBoost.cacheInterstitial(AdsService.Location.APP_START.getId());
        }
        Gdx.app.log(TAG, "INITIALIZING MILLENIAL");
        isShowingInterstitial = false;
    }

    public void dispose() {
        this.activity = null;
    }

    @Override // com.gaiaonline.monstergalaxy.service.AdsService
    public void dismissInterstitial() {
        Gdx.app.log(TAG, " dismissInterstitial()");
        if (this.adContainer != null && this.activity != null) {
            this.activity.runOnUiThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.service.AndroidAdsService.3
                @Override // java.lang.Runnable
                public void run() {
                    AndroidAdsService.this.adContainer.removeAllViews();
                    AndroidAdsService.this.onInterstitialDone();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onInterstitialDone() {
        Gdx.app.log(TAG, "onInterstitialDone()");
        Game.runOnGameThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.service.AndroidAdsService.4
            @Override // java.lang.Runnable
            public void run() {
                Gdx.app.log(AndroidAdsService.TAG, "onInterstitialDone() @ Game thread");
                if (AndroidAdsService.this.onInterstitialDone != null) {
                    AndroidAdsService.this.onInterstitialDone.run();
                }
                AndroidAdsService.isShowingInterstitial = false;
            }
        });
    }

    @Override // com.gaiaonline.monstergalaxy.service.AdsService
    public boolean isShowingInterstitial() {
        return isShowingInterstitial;
    }

    @Override // com.gaiaonline.monstergalaxy.service.AdsService
    public void doCacheInterstitials() {
        Gdx.app.log(TAG, "cacheInterstitials()");
        ((Activity) chartBoost.getContext()).runOnUiThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.service.AndroidAdsService.5
            @Override // java.lang.Runnable
            public void run() {
                for (AdsService.Location location : AdsService.Location.valuesCustom()) {
                    AndroidAdsService.this.cacheInterstitial(location);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cacheInterstitial(AdsService.Location location) {
        Gdx.app.log(TAG, "cacheInterstitial(" + location.getId() + ")");
        if (!chartBoost.hasCachedInterstitial(location.getId())) {
            chartBoost.cacheInterstitial(location.getId());
        }
    }

    @Override // com.gaiaonline.monstergalaxy.service.AdsService
    public void showInterstitial(final AdsService.Location location, Runnable onDone) {
        Gdx.app.log(TAG, "App has requested an Ad " + location.name());
        if (System.currentTimeMillis() - this.lastAdTimeStamp < this.interval && this.lastAdTimeStamp > 0) {
            Gdx.app.log(TAG, "No enough time beetwen ads");
            return;
        }
        Gdx.app.log(TAG, "Enough time beetwen ads");
        if (!isShowingInterstitial) {
            this.onInterstitialDone = onDone;
            if (chartBoost.hasCachedInterstitial(location.getId())) {
                Gdx.app.log(TAG, "chartboost has cached ads will show it");
                ((Activity) chartBoost.getContext()).runOnUiThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.service.AndroidAdsService.6
                    @Override // java.lang.Runnable
                    public void run() {
                        Gdx.app.log(AndroidAdsService.TAG, "showInterstitial(" + location.getId() + ")");
                        AndroidAdsService.chartBoost.showInterstitial(location.getId());
                        AndroidAdsService.this.cacheInterstitial(location);
                        AndroidAdsService.isShowingInterstitial = true;
                        AndroidAdsService.this.lastAdTimeStamp = System.currentTimeMillis();
                    }
                });
                return;
            }
            return;
        }
        Log.e(TAG, "showInterstitial(" + location.getId() + ") invoked while already showing interstitial");
    }
}
