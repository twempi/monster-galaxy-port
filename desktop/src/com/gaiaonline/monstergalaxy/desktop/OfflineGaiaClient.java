package com.gaiaonline.monstergalaxy.desktop;

import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.service.AbstractGaiaClient;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Replaces the Gaia Online web client. The gaiaonline.com endpoints are long gone,
 * so nothing here touches the network: the "server time" is the local clock, and
 * session/install/reimbursement calls succeed or no-op locally.
 */
public class OfflineGaiaClient extends AbstractGaiaClient {
    private final File storageFolder;

    public OfflineGaiaClient(String storageFolder) {
        super(storageFolder);
        this.storageFolder = new File(storageFolder);
    }

    @Override
    protected String calculateDeviceUniqueId() {
        File idFile = new File(storageFolder, "device-id");
        try {
            if (idFile.exists()) {
                return new String(Files.readAllBytes(idFile.toPath()), StandardCharsets.UTF_8).trim();
            }
            String id = UUID.randomUUID().toString().replace("-", "");
            Files.write(idFile.toPath(), id.getBytes(StandardCharsets.UTF_8));
            return id;
        } catch (IOException e) {
            return "desktop";
        }
    }

    @Override
    public void claimReimbursement(final GaiaHttpListener listener) {
        Game.runOnGameThread(new Runnable() {
            public void run() {
                listener.onHttpDone("No reimbursement available");
            }
        });
    }

    @Override
    public long fetchTimestamp() {
        this.deviceToServerDeltaSeconds = 0;
        return System.currentTimeMillis() / 1000;
    }

    @Override
    protected String getAndroidId() {
        return "";
    }

    @Override
    protected String getDeviceId() {
        return "";
    }

    @Override
    protected Map<String, String> getInstallParams() {
        return new HashMap<String, String>();
    }

    @Override
    protected String getUrlContent(String url, boolean addHash) throws Exception {
        throw new IOException("Offline: " + url);
    }

    @Override
    public String getUrlContent(String url, Map<String, String> params, boolean addHash) throws Exception {
        throw new IOException("Offline: " + url);
    }

    @Override
    public boolean hasReimbursement() {
        return false;
    }

    @Override
    public void loadPassportIdAndGuestPass() {
    }

    @Override
    public void savePassportIdAndGuestPass(String passportId, String guestPass) {
    }

    @Override
    public void sendPurchaseData(String productId, String data) {
    }

    @Override
    public boolean verifyNewVersionAvailability() {
        return false;
    }

    @Override
    public boolean verifyReimbursement() {
        return false;
    }

    @Override
    public boolean sendInstallInfo() {
        return true;
    }

    @Override
    public boolean startGaiaSession() {
        return true;
    }
}
