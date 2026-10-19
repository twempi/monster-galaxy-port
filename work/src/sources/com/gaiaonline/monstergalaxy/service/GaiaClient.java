package com.gaiaonline.monstergalaxy.service;

import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import com.badlogic.gdx.Gdx;
import com.gaiaonline.monstergalaxy.app.ConfigConstants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxyApp;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;
import com.gaiaonline.monstergalaxy.settings.SettingsManager;
import com.sessionm.a.b;
import com.tapjoy.TapjoyConstants;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class GaiaClient extends AbstractGaiaClient {
    private static boolean hasReimbursement = false;
    private DefaultHttpClient httpClient;

    public GaiaClient(String storageFolder) {
        super(storageFolder);
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setStaleCheckingEnabled(params, false);
        HttpConnectionParams.setConnectionTimeout(params, 20000);
        HttpConnectionParams.setSoTimeout(params, 20000);
        HttpConnectionParams.setSocketBufferSize(params, Opcodes.ACC_ANNOTATION);
        ConnManagerParams.setTimeout(params, 20000L);
        params.setBooleanParameter("http.protocol.handle-redirects", false);
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        ClientConnectionManager connectionManager = new ThreadSafeClientConnManager(params, registry);
        this.httpClient = new DefaultHttpClient(connectionManager, params);
    }

    @Override // com.gaiaonline.monstergalaxy.service.AbstractGaiaClient
    public void claimReimbursement(final AbstractGaiaClient.GaiaHttpListener listener) {
        Runnable claimReimbursement = new Runnable() { // from class: com.gaiaonline.monstergalaxy.service.GaiaClient.1
            @Override // java.lang.Runnable
            public void run() {
                String message = "Reimbursement: ";
                boolean addHash = true;
                try {
                    try {
                        HashMap<String, String> params = new HashMap<>();
                        if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK1) {
                            params.put("t", "reimbursement");
                            params.put("giveCreditsForUDID", "1");
                            addHash = false;
                        } else {
                            params.put("p", "2");
                            params.put("uid", GaiaClient.this.getDeviceUniqueId());
                        }
                        String response = GaiaClient.this.getUrlContent(ConfigConstants.WEBSERVICE_REIMBURSEMENT_PHASE2, params, addHash);
                        JSONObject phase2 = new JSONObject(response);
                        JSONObject credits = phase2.getJSONObject("data").getJSONObject("credits");
                        int mogacash = credits.getInt("mogacash");
                        int starseeds = credits.getInt("starseeds");
                        int bluecoffees = credits.getInt("bluecoffees");
                        boolean putComma = false;
                        if (bluecoffees > 0) {
                            Game.getTrainer().addBlueCoffeeCups(bluecoffees);
                            message = String.valueOf("Reimbursement: ") + bluecoffees + " bluecoffees";
                            putComma = true;
                        }
                        if (starseeds > 0) {
                            Game.getTrainer().addStarSeeds(starseeds);
                            message = String.valueOf(String.valueOf(message) + (putComma ? ", " : "")) + starseeds + " starseeds";
                            putComma = true;
                        }
                        if (mogacash > 0) {
                            Game.getTrainer().addCoins(mogacash);
                            message = String.valueOf(String.valueOf(message) + (putComma ? ", " : "")) + mogacash + " mogacash";
                        }
                        boolean addHash2 = true;
                        try {
                            try {
                                HashMap<String, String> params2 = new HashMap<>();
                                try {
                                    if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK1) {
                                        params2.put("t", "reimbursement");
                                        params2.put("didGiveCreditsToUDID", "1");
                                        addHash2 = false;
                                    } else {
                                        params2.put("p", "3");
                                        params2.put("uid", GaiaClient.this.getDeviceUniqueId());
                                    }
                                    String response2 = GaiaClient.this.getUrlContent(ConfigConstants.WEBSERVICE_REIMBURSEMENT_PHASE3, params2, addHash2);
                                    JSONObject phase3 = new JSONObject(response2);
                                    phase3.getJSONObject("data").getInt("didGiveCredits");
                                    GaiaClient.hasReimbursement = false;
                                } catch (Exception e) {
                                    e = e;
                                    Gdx.app.log("Error", "GaiaClient - Phase 3" + e);
                                    GaiaClient.hasReimbursement = false;
                                } catch (Throwable th) {
                                    th = th;
                                    GaiaClient.hasReimbursement = false;
                                    throw th;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                            }
                        } catch (Exception e2) {
                            e = e2;
                        }
                        final String amsg = message;
                        final AbstractGaiaClient.GaiaHttpListener gaiaHttpListener = listener;
                        Game.runOnGameThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.service.GaiaClient.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                gaiaHttpListener.onHttpDone(amsg);
                            }
                        });
                    } catch (Throwable th3) {
                        final String amsg2 = message;
                        final AbstractGaiaClient.GaiaHttpListener gaiaHttpListener2 = listener;
                        Game.runOnGameThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.service.GaiaClient.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                gaiaHttpListener2.onHttpDone(amsg2);
                            }
                        });
                        throw th3;
                    }
                } catch (Exception e3) {
                    Gdx.app.log("Error", "GaiaClient" + e3);
                    final String message2 = "Error retrieving reimbursement";
                    final AbstractGaiaClient.GaiaHttpListener gaiaHttpListener3 = listener;
                    Game.runOnGameThread(new Runnable() { // from class: com.gaiaonline.monstergalaxy.service.GaiaClient.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            gaiaHttpListener3.onHttpDone(message2);
                        }
                    });
                }
            }
        };
        Thread claimer = new Thread(claimReimbursement);
        claimer.start();
    }

    @Override // com.gaiaonline.monstergalaxy.service.AbstractGaiaClient
    public boolean verifyReimbursement() {
        Runnable runner = new Runnable() { // from class: com.gaiaonline.monstergalaxy.service.GaiaClient.2
            @Override // java.lang.Runnable
            public void run() {
                boolean addHash = true;
                try {
                    HashMap<String, String> params = new HashMap<>();
                    if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK1) {
                        params.put("t", "reimbursement");
                        params.put("hasUDIDPendingCredits", "1");
                        addHash = false;
                    } else {
                        params.put("uid", GaiaClient.this.getDeviceUniqueId());
                        params.put("p", "1");
                    }
                    String response = GaiaClient.this.getUrlContent(ConfigConstants.WEBSERVICE_REIMBURSEMENT_PHASE1, params, addHash);
                    JSONObject phase1 = new JSONObject(response);
                    int hasCredits = phase1.getJSONObject("data").getInt("hasCredits");
                    if (hasCredits > 0) {
                        GaiaClient.hasReimbursement = true;
                    } else {
                        GaiaClient.hasReimbursement = false;
                    }
                } catch (Exception e) {
                    Gdx.app.log("Error", "GaiaClient " + e, e);
                }
            }
        };
        Thread verifier = new Thread(runner);
        verifier.start();
        return false;
    }

    public JSONObject doGetRequest(String url) throws Exception {
        URI uri;
        if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK1) {
            uri = new URI(url);
        } else {
            uri = new URI(getFormattedURL(url));
        }
        HttpGet request = new HttpGet(uri);
        HttpResponse response = this.httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            return null;
        }
        InputStream inputStream = entity.getContent();
        String resultString = readStream(inputStream);
        inputStream.close();
        try {
            JSONObject jsonResponse = new JSONObject(resultString);
            return jsonResponse;
        } catch (JSONException e) {
            throw new IOException("Failed to parse json");
        }
    }

    @Override // com.gaiaonline.monstergalaxy.service.AbstractGaiaClient
    public long fetchTimestamp() throws Exception {
        HashMap<String, String> params = new HashMap<>();
        String resultString = getUrlContent(ConfigConstants.WEBSERVICE_TIME_URL, params, true);
        try {
            JSONObject jsonResponse = new JSONObject(resultString);
            String status = jsonResponse.getString(b.K);
            if (!b.N.equalsIgnoreCase(status)) {
                throw new IOException("Invalid response status");
            }
            long timestamp = jsonResponse.getJSONObject("data").getLong(TapjoyConstants.TJC_TIMESTAMP);
            this.deviceToServerDeltaSeconds = (System.currentTimeMillis() / 1000) - timestamp;
            return timestamp;
        } catch (JSONException e) {
            throw new IOException("Failed to parse json");
        }
    }

    @Override // com.gaiaonline.monstergalaxy.service.AbstractGaiaClient
    public boolean hasReimbursement() {
        return hasReimbursement;
    }

    @Override // com.gaiaonline.monstergalaxy.service.AbstractGaiaClient
    protected String getAndroidId() {
        String androidId = Settings.Secure.getString(MonsterGalaxyApp.getInstance().getContentResolver(), TapjoyConstants.TJC_ANDROID_ID);
        return androidId == null ? "" : androidId;
    }

    @Override // com.gaiaonline.monstergalaxy.service.AbstractGaiaClient
    protected String getDeviceId() {
        return "";
    }

    @Override // com.gaiaonline.monstergalaxy.service.AbstractGaiaClient
    protected Map<String, String> getInstallParams() {
        Map<String, String> params = new HashMap<>();
        params.put("hw_board", String.valueOf(Build.BOARD.length() % 10));
        params.put("hw_brand", String.valueOf(Build.BRAND.length() % 10));
        params.put("hw_cpu_abi", String.valueOf(Build.CPU_ABI.length() % 10));
        params.put("hw_device", String.valueOf(Build.DEVICE.length() % 10));
        params.put("hw_display", String.valueOf(Build.DISPLAY.length() % 10));
        params.put("hw_host", String.valueOf(Build.HOST.length() % 10));
        params.put("hw_id", String.valueOf(Build.ID.length() % 10));
        params.put("hw_manufacturer", String.valueOf(Build.MANUFACTURER.length() % 10));
        params.put("hw_model", String.valueOf(Build.MODEL.length() % 10));
        params.put("hw_product", String.valueOf(Build.PRODUCT.length() % 10));
        params.put("hw_tags", String.valueOf(Build.TAGS.length() % 10));
        params.put("hw_type", String.valueOf(Build.TYPE.length() % 10));
        params.put("hw_user", String.valueOf(Build.USER.length() % 10));
        String macAddress = getWifiMac();
        params.put("wlan_mac_md5", macAddress == null ? "" : getMD5Hex(macAddress));
        if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK1) {
            if (macAddress == null) {
                macAddress = "";
            }
            params.put("wlan_mac", macAddress);
        } else if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2) {
            if (macAddress == null) {
                macAddress = "";
            }
            params.put("mac_address", macAddress);
        }
        params.put("device", Build.DEVICE);
        params.put(TapjoyConstants.TJC_CARRIER_NAME, getCarrierName());
        params.put("v", Game.getPlatformHelper().getVersionName());
        return params;
    }

    private String getCarrierName() {
        String carrierName = null;
        try {
            TelephonyManager manager = (TelephonyManager) MonsterGalaxyApp.getInstance().getSystemService("phone");
            carrierName = manager.getNetworkOperatorName();
        } catch (Exception e) {
        }
        if (carrierName == null) {
            return "";
        }
        return carrierName;
    }

    @Override // com.gaiaonline.monstergalaxy.service.AbstractGaiaClient
    protected String calculateDeviceUniqueId() {
        String[] fakeImeiComponents = {Build.BOARD, Build.BRAND, Build.CPU_ABI, Build.DEVICE, Build.DISPLAY, Build.HOST, Build.ID, Build.MANUFACTURER, Build.MODEL, Build.PRODUCT, Build.TAGS, Build.TYPE, Build.USER};
        StringBuilder fakeImei = new StringBuilder();
        fakeImei.append("35");
        for (String component : fakeImeiComponents) {
            fakeImei.append(component.length() % 10);
        }
        String wifiMac = getWifiMac();
        if (wifiMac == null) {
            wifiMac = "";
        }
        String fullToken = String.valueOf(getDeviceId()) + fakeImei.toString() + getAndroidId() + wifiMac;
        return getSHA1Hex(fullToken);
    }

    private String getWifiMac() {
        WifiInfo wifiInfo;
        WifiManager wifiManager = (WifiManager) MonsterGalaxyApp.getInstance().getSystemService("wifi");
        if (wifiManager == null || (wifiInfo = wifiManager.getConnectionInfo()) == null) {
            return null;
        }
        return wifiInfo.getMacAddress();
    }

    @Override // com.gaiaonline.monstergalaxy.service.AbstractGaiaClient
    protected String getUrlContent(String url, boolean addHash) throws Exception {
        URI uri;
        if (addHash) {
            try {
                uri = new URI(getFormattedURL(url));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        } else {
            uri = new URI(url);
        }
        HttpGet request = new HttpGet(uri);
        try {
            HttpResponse response = this.httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            InputStream inputStream = entity.getContent();
            Header contentEncoding = response.getFirstHeader("Content-Encoding");
            if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                inputStream = new GZIPInputStream(inputStream);
            }
            String resultString = readStream(inputStream);
            inputStream.close();
            return resultString;
        } catch (ClientProtocolException e2) {
            throw new Exception(e2);
        } catch (IOException e3) {
            throw new Exception(e3);
        }
    }

    @Override // com.gaiaonline.monstergalaxy.service.AbstractGaiaClient
    public boolean verifyNewVersionAvailability() {
        try {
            int currentVersion = Game.getPlatformHelper().getVersionCode();
            HashMap<String, String> params = new HashMap<>();
            if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK1) {
                params.put("currentversion", String.valueOf(currentVersion));
            }
            String response = getUrlContent(ConfigConstants.WEBSERVICE_VERSION, params, true);
            JSONObject jsonResponse = new JSONObject(response);
            String status = jsonResponse.getString(b.K);
            int liveVersion = 0;
            if (b.N.equalsIgnoreCase(status)) {
                liveVersion = jsonResponse.getJSONObject("data").getInt("currentversion");
            }
            return currentVersion < liveVersion;
        } catch (Exception e) {
            return false;
        }
    }

    @Override // com.gaiaonline.monstergalaxy.service.AbstractGaiaClient
    public void sendPurchaseData(final String orderId, final String itemId) {
        Runnable runner = new Runnable() { // from class: com.gaiaonline.monstergalaxy.service.GaiaClient.3
            @Override // java.lang.Runnable
            public void run() {
                try {
                    if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK1) {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("sku", "inapp:" + itemId);
                        params.put("transaction_id", orderId);
                        params.put("merchant_txn_id", orderId);
                        String response = GaiaClient.this.getUrlContent(ConfigConstants.WEBSERVICE_PURCHASE, params, true);
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString(b.K);
                        b.N.equalsIgnoreCase(status);
                    } else if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2) {
                        HashMap<String, String> params2 = new HashMap<>();
                        params2.put("sku", "inapp:" + itemId);
                        params2.put("transaction_id", orderId);
                        params2.put("merchant_txn_id", orderId);
                        params2.put(TapjoyConstants.TJC_ANDROID_ID, GaiaClient.this.getAndroidId());
                        params2.put("device_id", GaiaClient.this.getDeviceId());
                        String response2 = GaiaClient.this.getUrlContent(ConfigConstants.WEBSERVICE_PURCHASE, params2, true);
                        JSONObject jsonResponse2 = new JSONObject(response2);
                        String status2 = jsonResponse2.getString(b.K);
                        b.N.equalsIgnoreCase(status2);
                    }
                } catch (Exception e) {
                    Gdx.app.log("ERROR", "Sending purchase data", e);
                }
            }
        };
        new Thread(runner).start();
    }

    @Override // com.gaiaonline.monstergalaxy.service.AbstractGaiaClient
    public void loadPassportIdAndGuestPass() {
        SharedPreferences preferences = MonsterGalaxyApp.getInstance().getSharedPreferences(SettingsManager.SETTINGS_NAME, 0);
        passportId = preferences.getString("gaiasession.user", null);
        guestPass = preferences.getString("gaiasession.pass", null);
    }

    @Override // com.gaiaonline.monstergalaxy.service.AbstractGaiaClient
    public void savePassportIdAndGuestPass(String passportId, String guestPass) {
        passportId = passportId;
        guestPass = guestPass;
        SharedPreferences preferences = MonsterGalaxyApp.getInstance().getSharedPreferences(SettingsManager.SETTINGS_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("gaiasession.user", passportId);
        editor.putString("gaiasession.pass", guestPass);
        editor.commit();
    }
}
