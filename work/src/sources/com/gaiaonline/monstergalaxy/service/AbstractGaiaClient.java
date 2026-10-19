package com.gaiaonline.monstergalaxy.service;

import com.badlogic.gdx.Gdx;
import com.gaiaonline.monstergalaxy.app.Clock;
import com.gaiaonline.monstergalaxy.app.ConfigConstants;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;
import com.sessionm.a.b;
import com.tapjoy.TapjoyConstants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractGaiaClient {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$platform$PlatformHelper$MGSkin = null;
    protected static final String DEVICE_ID_PATH = "deviceUniqueId";
    protected static final String[] PUBLIC_IP_ADDRESS_PROVIDERS = {"http://checkip.dyndns.org/", "http://automation.whatismyip.com/n09230945.asp"};
    protected static final String SKIP_ENCODING_STRINGS = "!*'();:@$,/#[]";
    protected static String deviceUniqueId;
    protected static String guestPass;
    protected static String ipAddress;
    protected static String passportId;
    protected static String passportToken;
    protected static boolean sessionStarted;
    protected long deviceToServerDeltaSeconds;
    private String storageFolder;

    public interface GaiaHttpListener {
        void onHttpDone(String str);
    }

    protected abstract String calculateDeviceUniqueId();

    public abstract void claimReimbursement(GaiaHttpListener gaiaHttpListener);

    public abstract long fetchTimestamp() throws Exception;

    protected abstract String getAndroidId();

    protected abstract String getDeviceId();

    protected abstract Map<String, String> getInstallParams();

    protected abstract String getUrlContent(String str, boolean z) throws Exception;

    public abstract boolean hasReimbursement();

    public abstract void loadPassportIdAndGuestPass();

    public abstract void savePassportIdAndGuestPass(String str, String str2);

    public abstract void sendPurchaseData(String str, String str2);

    public abstract boolean verifyNewVersionAvailability();

    public abstract boolean verifyReimbursement();

    static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$platform$PlatformHelper$MGSkin() {
        int[] iArr = $SWITCH_TABLE$com$gaiaonline$monstergalaxy$platform$PlatformHelper$MGSkin;
        if (iArr == null) {
            iArr = new int[PlatformHelper.MGSkin.valuesCustom().length];
            try {
                iArr[PlatformHelper.MGSkin.SK1.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[PlatformHelper.MGSkin.SK2.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            $SWITCH_TABLE$com$gaiaonline$monstergalaxy$platform$PlatformHelper$MGSkin = iArr;
        }
        return iArr;
    }

    public boolean isSessionStarted() {
        return sessionStarted;
    }

    public boolean startGaiaSession() {
        if (sessionStarted) {
            return true;
        }
        Map<String, String> params = new HashMap<>();
        params.put(TapjoyConstants.TJC_ANDROID_ID, getAndroidId());
        params.put("device_id", getDeviceId());
        params.put("session_start", String.valueOf(Clock.getCurrentTimeSec()));
        params.putAll(getInstallParams());
        try {
            String response = getUrlContent(ConfigConstants.WEBSERVICE_SESSION, params, true);
            ObjectMapper m = new ObjectMapper();
            JsonNode rootNode = (JsonNode) m.readValue(response, JsonNode.class);
            if (b.N.equals(rootNode.get(b.K).getTextValue())) {
                JsonNode data = rootNode.get("data");
                if ("success".equals(data.get("outcome").getTextValue())) {
                    processPassportInfo(data);
                    sessionStarted = true;
                    return sessionStarted;
                }
                throw new Exception("Outcome not success");
            }
            throw new Exception("Status not ok");
        } catch (Exception e) {
            Gdx.app.error(Game.TAG, "Failed to start gaia session", e);
        }
    }

    private void processPassportInfo(JsonNode data) {
        if (data.get("passport_id") != null && data.get("guest_pass") != null) {
            String passportId2 = data.get("passport_id").getTextValue();
            String guestPass2 = data.get("guest_pass").getTextValue();
            savePassportIdAndGuestPass(passportId2, guestPass2);
        }
        if (data.get("passport_token") != null) {
            passportToken = data.get("passport_token").getTextValue();
        }
    }

    public long calculateServerTimestamp() {
        return (System.currentTimeMillis() / 1000) - this.deviceToServerDeltaSeconds;
    }

    public long toDeviceTime(long serverTime) {
        return this.deviceToServerDeltaSeconds + serverTime;
    }

    public AbstractGaiaClient(String storageFolder) {
        this.storageFolder = storageFolder;
    }

    public final String getDeviceUniqueId() {
        String str;
        synchronized (AbstractGaiaClient.class) {
            if (deviceUniqueId == null) {
                File idFile = new File(Gdx.files.internal(String.valueOf(this.storageFolder) + "/" + DEVICE_ID_PATH).path());
                try {
                    if (!idFile.exists()) {
                        writeDeviceUniqueIdFromFile(idFile, calculateDeviceUniqueId());
                    }
                    deviceUniqueId = readDeviceUniqueIdFromFile(idFile);
                } catch (IOException e) {
                    throw new RuntimeException("Cannot read/write device unique id", e);
                }
            }
            str = deviceUniqueId;
        }
        return str;
    }

    public boolean sendInstallInfo() {
        try {
            ipAddress = fetchPublicIpAddress();
            Map<String, String> params = new HashMap<>();
            if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK1) {
                params.put("t", TapjoyConstants.TJC_DEVICE_ID_NAME);
            }
            params.put(TapjoyConstants.TJC_ANDROID_ID, getAndroidId());
            params.put("device_id", getDeviceId());
            params.putAll(getInstallParams());
            try {
                String response = getUrlContent(ConfigConstants.WEBSERVICE_INSTALL_URL, params, true);
                ObjectMapper m = new ObjectMapper();
                JsonNode rootNode = (JsonNode) m.readValue(response, JsonNode.class);
                if (b.N.equals(rootNode.get(b.K).getTextValue())) {
                    JsonNode data = rootNode.get("data");
                    if ("success".equals(data.get("outcome").getTextValue())) {
                        processPassportInfo(data);
                        return true;
                    }
                    throw new Exception("Outcome not success");
                }
                throw new Exception("Status not ok");
            } catch (Exception e) {
                Gdx.app.error(Game.TAG, "Failed to send device unique id to gaia server.", e);
                return false;
            }
        } catch (Exception e2) {
            Gdx.app.log(Game.TAG, "Failed to retrieve public ip address. Internet connection is probably unavailable.");
            return false;
        }
    }

    public String getUrlContent(String url, Map<String, String> params, boolean addHash) throws Exception {
        String timestamp = new DecimalFormat("#.######").format(System.currentTimeMillis() / 1000.0d);
        params.put(TapjoyConstants.TJC_TIMESTAMP, timestamp);
        params.put(TapjoyConstants.TJC_DEVICE_ID_NAME, getDeviceUniqueId());
        if (ipAddress != null) {
            params.put("ip", ipAddress);
        }
        if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK1) {
            params.put("device_os", Game.getPlatformHelper().getPlatformVersion());
            params.put(TapjoyConstants.TJC_DEVICE_TYPE_NAME, TapjoyConstants.TJC_DEVICE_PLATFORM_TYPE);
            params.put("v", Game.getPlatformHelper().getVersionName());
            params.put(TapjoyConstants.TJC_APP_VERSION_NAME, String.valueOf(Game.getPlatformHelper().getVersionCode()));
        } else if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2) {
            if (passportId != null) {
                params.put("passport_id", passportId);
            }
            if (passportToken != null) {
                params.put("passport_token", passportToken);
            }
            params.put("device_os", TapjoyConstants.TJC_DEVICE_PLATFORM_TYPE);
            params.put(TapjoyConstants.TJC_DEVICE_TYPE_NAME, "android_phone");
            params.put(TapjoyConstants.TJC_DEVICE_OS_VERSION_NAME, Game.getPlatformHelper().getPlatformVersion());
            params.put("v", Game.getPlatformHelper().getVersionName());
            params.put(TapjoyConstants.TJC_APP_VERSION_NAME, String.valueOf(Game.getPlatformHelper().getVersionCode()));
        }
        String fullUrl = buildUrl(url, params);
        return getUrlContent(fullUrl, addHash);
    }

    private String fetchPublicIpAddress() throws Exception {
        for (String provider : PUBLIC_IP_ADDRESS_PROVIDERS) {
            try {
                Gdx.app.log(Game.TAG, "Fetching public ip address from " + provider);
                return getPublicIpAddress(provider);
            } catch (Exception e) {
                Gdx.app.error(Game.TAG, "Failed to fetch public ip address from " + provider);
                e.printStackTrace();
            }
        }
        throw new Exception("Failed to get public ip address");
    }

    protected String getPublicIpAddress(String providerUrl) throws Exception {
        return parseIpAddress(getUrlContent(providerUrl, false));
    }

    protected static String buildUrl(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            try {
                sb.append(URLEncoder.encode(entry.getKey(), "utf8"));
                sb.append("=");
                sb.append(URLEncoder.encode(entry.getValue(), "utf8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return String.valueOf(url) + "?" + sb.toString();
    }

    private static String parseIpAddress(String content) throws IOException {
        Pattern pattern = Pattern.compile("([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(0);
        }
        throw new IOException("Failed to parse ip address for " + content);
    }

    private static String readDeviceUniqueIdFromFile(File idFile) throws IOException {
        RandomAccessFile f = new RandomAccessFile(idFile, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeDeviceUniqueIdFromFile(File idFile, String id) throws IOException {
        idFile.getParentFile().mkdirs();
        idFile.createNewFile();
        FileOutputStream out = new FileOutputStream(idFile);
        out.write(id.getBytes());
        out.close();
    }

    protected static String getRandomClientId() {
        return UUID.randomUUID().toString();
    }

    protected static String getSHA1Hex(String input) {
        return getDigestHex(input, "SHA");
    }

    protected static String getMD5Hex(String input) {
        return getDigestHex(input, "MD5");
    }

    protected static String getMD5Hex(byte[] bytes) {
        return getDigestHex(bytes, "MD5");
    }

    private static String unencodeSkippedChars(String value) {
        for (int i = 0; i < SKIP_ENCODING_STRINGS.length(); i++) {
            String c = SKIP_ENCODING_STRINGS.substring(i, i + 1);
            try {
                value = value.replace(URLEncoder.encode(c, "utf8"), c);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to encode special character: " + c, e);
            }
        }
        value.replace("+", "%20");
        return value;
    }

    protected String getFormattedURL(String url) {
        String[] parameters = url.substring(url.indexOf("?") + 1).split("&");
        SortedMap<String, String> sortedParameters = new TreeMap<>();
        StringBuilder newUrl = new StringBuilder(url.substring(0, url.indexOf("?") + 1));
        for (String param : parameters) {
            int index = param.indexOf("=");
            if (index > -1) {
                String key = param.substring(0, index);
                String value = param.substring(index + 1);
                sortedParameters.put(key, value);
            }
        }
        int i = 0;
        for (Map.Entry<String, String> entry : sortedParameters.entrySet()) {
            if (i > 0) {
                newUrl.append("&");
            }
            newUrl.append(entry.getKey());
            newUrl.append("=");
            newUrl.append(unencodeSkippedChars(entry.getValue()));
            i++;
        }
        try {
            if (newUrl.length() > 0) {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                String salt = null;
                switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$platform$PlatformHelper$MGSkin()[Game.getPlatformHelper().getSkin().ordinal()]) {
                    case 1:
                        salt = Game.SALT_SK1;
                        break;
                    case 2:
                        salt = Game.SALT_SK2;
                        break;
                }
                byte[] hashValue = digest.digest((String.valueOf(salt) + newUrl.toString()).getBytes());
                newUrl.append("&hash=" + toHexString(hashValue));
            } else {
                newUrl.append(url);
            }
        } catch (NoSuchAlgorithmException e) {
            Gdx.app.error(Game.TAG, "Could not format URL", e);
        }
        return newUrl.toString();
    }

    private static String getDigestHex(String input, String algorithm) {
        if (input == null) {
            input = "";
        }
        return getDigestHex(input.getBytes(), algorithm);
    }

    private static String getDigestHex(byte[] bytes, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(bytes);
            byte[] digestBytes = digest.digest();
            String retValue = toHexString(digestBytes);
            return retValue;
        } catch (NoSuchAlgorithmException e) {
            Gdx.app.error(Game.TAG, "Could not get message digester.");
            return "";
        }
    }

    private static String toHexString(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            hex.append(Integer.toHexString((bytes[i] >> 4) & 15));
            hex.append(Integer.toHexString(bytes[i] & 15));
        }
        return hex.toString();
    }

    protected static String readStream(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                try {
                    String line = reader.readLine();
                    if (line != null) {
                        sb.append(String.valueOf(line) + "\n");
                    } else {
                        try {
                            break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                    try {
                        is.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
            } catch (Throwable th) {
                try {
                    is.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
                throw th;
            }
        }
        is.close();
        return sb.toString();
    }
}
