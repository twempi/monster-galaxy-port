package com.gaiaonline.monstergalaxy.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.gaiaonline.monstergalaxy.model.shop.DealListener;
import com.gaiaonline.monstergalaxy.platform.AndroidPlatformHelper;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;
import com.gaiaonline.monstergalaxy.service.AdsService;
import com.gaiaonline.monstergalaxy.service.FacebookService;
import com.gaiaonline.monstergalaxy.service.TwitterService;
import com.gaiaonline.monstergalaxy.storage.StorageHelper;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Desktop platform layer. isAndroid() returns false, which makes the game use its
 * own in-engine text fields and dialogue rendering (the original developers'
 * desktop code paths). Ads, billing, social sharing and native Android widgets
 * are no-ops.
 */
public class DesktopPlatformHelper extends PlatformHelper implements AndroidPlatformHelper {
    private static final String VERSION_NAME = "1.3";
    private static final int VERSION_CODE = 20;

    private final File assetsDir;
    private final File dataDir;
    private DesktopStorageHelper storage;
    private DealListener dealListener;

    private final AdsService adsService = new AdsService() {
        public void dismissInterstitial() {
        }

        public void doCacheInterstitials() {
        }

        public boolean isShowingInterstitial() {
            return false;
        }

        public void showInterstitial(Location location, Runnable onDone) {
            if (onDone != null) {
                onDone.run();
            }
        }
    };

    private final FacebookService facebookService = new FacebookService() {
        public void authorize(OnCompletedHandler handler) {
            if (handler != null) {
                handler.onCompleted(false);
            }
        }

        public String getFacebookId() {
            return null;
        }

        public boolean isSessionValid() {
            return false;
        }

        public void logout(OnCompletedHandler handler) {
            if (handler != null) {
                handler.onCompleted(true);
            }
        }

        public void postMessage(OnCompletedHandler handler, String message) {
            if (handler != null) {
                handler.onCompleted(false);
            }
        }
    };

    private final TwitterService twitterService = new TwitterService() {
        public boolean authorize(Runnable onAuthorized) {
            return false;
        }

        public void closeView() {
        }

        public boolean isAuthorized() {
            return false;
        }

        public boolean isViewOpen() {
            return false;
        }

        public boolean updateStatus(String status) {
            return false;
        }
    };

    public DesktopPlatformHelper(File assetsDir, File dataDir) {
        this.assetsDir = assetsDir;
        this.dataDir = dataDir;
    }

    // --- PlatformHelper -------------------------------------------------------

    public AdsService getAdsService() {
        return adsService;
    }

    public long getBootElapsedTimeMillis() {
        return System.nanoTime() / 1000000L;
    }

    public File getExportDirectory() {
        File dir = new File(dataDir, "snapshots");
        dir.mkdirs();
        return dir;
    }

    public String getExternalStoragePath() {
        return dataDir.getAbsolutePath();
    }

    public FacebookService getFacebookService() {
        return facebookService;
    }

    public String getPlatformVersion() {
        return System.getProperty("os.name") + " " + System.getProperty("os.version");
    }

    public MGSkin getSkin() {
        return MGSkin.SK1;
    }

    public synchronized StorageHelper getStorageHelper() {
        if (storage == null) {
            storage = new DesktopStorageHelper(assetsDir);
        }
        return storage;
    }

    public TwitterService getTwitterService() {
        return twitterService;
    }

    // Read before libGDX is initialised (Android used SharedPreferences), so this
    // lives in a plain file rather than Gdx Preferences.
    private File localeFile() {
        return new File(dataDir, "locale");
    }

    public String getUserSelectedLocale() {
        try {
            String locale = new String(Files.readAllBytes(localeFile().toPath()), StandardCharsets.UTF_8).trim();
            return locale.isEmpty() ? null : locale;
        } catch (IOException e) {
            return null;
        }
    }

    public void setUserSelectedLocale(String locale) {
        try {
            Files.write(localeFile().toPath(), (locale == null ? "" : locale).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            Gdx.app.error("MonsterGalaxy", "Could not save locale", e);
        }
    }

    public int getVersionCode() {
        return VERSION_CODE;
    }

    public String getVersionName() {
        return VERSION_NAME;
    }

    public boolean isAndroid() {
        return false;
    }

    public boolean isPurchaseTransactionPending() {
        return false;
    }

    public void quit() {
        Gdx.app.exit();
    }

    public synchronized void reloadDatabase() {
        if (storage != null) {
            storage.close();
        }
        storage = new DesktopStorageHelper(assetsDir);
    }

    public void share(String text) {
    }

    // --- AndroidPlatformHelper (native Android widgets: unused on desktop) ----

    public void addOptionsView(String udid) {
    }

    public void addTextField(String name, int x, int y, int width, int height, boolean multiline,
            boolean fullScreenKeyboard, Integer maxChars, String disallowedChars, boolean capWords,
            boolean requestFocus) {
    }

    public void addTextView(int width, int left, int top, String text, Color color) {
    }

    public void buyProduct(String productId) {
    }

    public void composeEmail(String subject, String message) {
    }

    public void composeSms(String message) {
    }

    public void dispose() {
    }

    public DealListener getDealListener() {
        return dealListener;
    }

    public void getDealRewards() {
    }

    public String getTextfieldValue(String name) {
        return "";
    }

    public void hideNativeProgressDialog() {
    }

    public boolean isSMSCapable() {
        return false;
    }

    public void openMarketProductPage() {
    }

    public void removeOptionsView() {
    }

    public void removeTextField(String name) {
    }

    public void removeTextView() {
    }

    public void requestFocus(String name) {
    }

    public void setDealListener(DealListener listener) {
        this.dealListener = listener;
    }

    public void setTextViewText(String text) {
    }

    public void setTextfieldValue(String name, String value) {
    }

    public void showAdColonyVideo() {
    }

    public void showNativeProgressDialog() {
    }

    public void showTapJoyOffers() {
    }

    public void showW3iOfferWall() {
    }

    public void switchKeyboard(boolean visible) {
    }
}
