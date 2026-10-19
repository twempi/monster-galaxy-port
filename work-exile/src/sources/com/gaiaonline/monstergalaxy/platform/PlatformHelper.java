package com.gaiaonline.monstergalaxy.platform;

import com.badlogic.gdx.Gdx;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.service.AdsService;
import com.gaiaonline.monstergalaxy.storage.StorageHelper;
import java.io.File;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public abstract class PlatformHelper {

    public enum MGSkin {
        SK1,
        SK2;

        /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
        public static MGSkin[] valuesCustom() {
            MGSkin[] mGSkinArrValuesCustom = values();
            int length = mGSkinArrValuesCustom.length;
            MGSkin[] mGSkinArr = new MGSkin[length];
            System.arraycopy(mGSkinArrValuesCustom, 0, mGSkinArr, 0, length);
            return mGSkinArr;
        }
    }

    public enum Model {
        KINDLE_FIRE;

        /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
        public static Model[] valuesCustom() {
            Model[] modelArrValuesCustom = values();
            int length = modelArrValuesCustom.length;
            Model[] modelArr = new Model[length];
            System.arraycopy(modelArrValuesCustom, 0, modelArr, 0, length);
            return modelArr;
        }
    }

    public enum Store {
        GOOGLE_PLAY,
        AMAZON;

        /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
        public static Store[] valuesCustom() {
            Store[] storeArrValuesCustom = values();
            int length = storeArrValuesCustom.length;
            Store[] storeArr = new Store[length];
            System.arraycopy(storeArrValuesCustom, 0, storeArr, 0, length);
            return storeArr;
        }
    }

    public abstract AdsService getAdsService();

    public abstract long getBootElapsedTimeMillis();

    public abstract File getExportDirectory();

    public abstract String getExternalStoragePath();

    public abstract String getPlatformVersion();

    public abstract MGSkin getSkin();

    public abstract StorageHelper getStorageHelper();

    public abstract int getVersionCode();

    public abstract String getVersionName();

    public abstract boolean isAndroid();

    public abstract boolean isPurchaseTransactionPending();

    public abstract void quit();

    public abstract void share(String str);

    public static Store getSourceStore() {
        try {
            Class<?> build = Class.forName("com.amazon.inapp.purchasing.ResponseReceiver");
            if (build != null) {
                return Store.AMAZON;
            }
        } catch (Exception e) {
            Gdx.app.error(Game.TAG, "Quering Store on PlatformHelper", e);
        }
        return Store.GOOGLE_PLAY;
    }

    public boolean shouldShowAds() {
        return isAndroid() && Game.getTrainer() != null && Game.getTrainer().isNodeCompleted(24);
    }

    public static Model getDeviceModel() {
        try {
            Class<?> build = Class.forName("android.os.Build");
            if (build == null) {
                return null;
            }
            Field aField = build.getDeclaredField("MODEL");
            String model = (String) aField.get(null);
            if ("Kindle Fire".equalsIgnoreCase(model)) {
                return Model.KINDLE_FIRE;
            }
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        } catch (IllegalAccessException e2) {
            return null;
        } catch (IllegalArgumentException e3) {
            return null;
        } catch (NoSuchFieldException e4) {
            return null;
        } catch (SecurityException e5) {
            return null;
        }
    }
}
