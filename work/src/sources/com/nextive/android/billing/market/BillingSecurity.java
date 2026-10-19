package com.nextive.android.billing.market;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashSet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class BillingSecurity {
    private static final String KEY_FACTORY_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static HashSet<Long> sKnownNonces = new HashSet<>();
    private static String base64EncodedPublicKey = null;

    public static class VerifiedPurchase {
        public String developerPayload;
        public String notificationId;
        public String orderId;
        public String productId;
        public BillingConstants.PurchaseState purchaseState;
        public long purchaseTime;

        public VerifiedPurchase(BillingConstants.PurchaseState purchaseState, String notificationId, String productId, String orderId, long purchaseTime, String developerPayload) {
            this.purchaseState = purchaseState;
            this.notificationId = notificationId;
            this.productId = productId;
            this.orderId = orderId;
            this.purchaseTime = purchaseTime;
            this.developerPayload = developerPayload;
        }
    }

    public static long generateNonce() {
        long nonce = RANDOM.nextLong();
        sKnownNonces.add(Long.valueOf(nonce));
        return nonce;
    }

    public static void removeNonce(long nonce) {
        sKnownNonces.remove(Long.valueOf(nonce));
    }

    public static boolean isNonceKnown(long nonce) {
        return sKnownNonces.contains(Long.valueOf(nonce));
    }

    public static ArrayList<VerifiedPurchase> verifyPurchase(String signedData, String signature) {
        if (signedData == null) {
            return null;
        }
        boolean verified = false;
        if (signature != null && signature.length() > 0) {
            PublicKey key = generatePublicKey(base64EncodedPublicKey);
            verified = verify(key, signedData, signature);
            if (!verified) {
                BillingLogger.log("signature does not match data.");
                return null;
            }
        }
        int numTransactions = 0;
        try {
            JSONObject jObject = new JSONObject(signedData);
            try {
                long nonce = jObject.optLong("nonce");
                JSONArray jTransactionsArray = jObject.optJSONArray("orders");
                if (jTransactionsArray != null) {
                    numTransactions = jTransactionsArray.length();
                }
                if (!isNonceKnown(nonce)) {
                    BillingLogger.log("Nonce not found: " + nonce);
                    return null;
                }
                ArrayList<VerifiedPurchase> purchases = new ArrayList<>();
                for (int i = 0; i < numTransactions; i++) {
                    try {
                        JSONObject jElement = jTransactionsArray.getJSONObject(i);
                        int response = jElement.getInt("purchaseState");
                        BillingConstants.PurchaseState purchaseState = BillingConstants.PurchaseState.valueOf(response);
                        String productId = jElement.getString("productId");
                        long purchaseTime = jElement.getLong("purchaseTime");
                        String orderId = jElement.optString("orderId", "");
                        String notifyId = null;
                        if (jElement.has("notificationId")) {
                            notifyId = jElement.getString("notificationId");
                        }
                        String developerPayload = jElement.optString("developerPayload", null);
                        boolean debug = BillingConstants.USE_MOCK_DATA;
                        if (debug || purchaseState != BillingConstants.PurchaseState.PURCHASED || verified) {
                            purchases.add(new VerifiedPurchase(purchaseState, notifyId, productId, orderId, purchaseTime, developerPayload));
                        }
                    } catch (JSONException e) {
                        BillingLogger.log("JSON exception: " + e);
                        return null;
                    }
                }
                removeNonce(nonce);
                return purchases;
            } catch (JSONException e2) {
                e = e2;
                BillingLogger.log("Parsing Failed! " + e);
                return null;
            }
        } catch (JSONException e3) {
            e = e3;
        }
    }

    public static void setBase64PublicKey(String key) {
        base64EncodedPublicKey = key;
    }

    public static PublicKey generatePublicKey(String encodedPublicKey) {
        try {
            byte[] decodedKey = Base64.decode(encodedPublicKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
            return keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
        } catch (Base64DecoderException e) {
            BillingLogger.log("Base64 decoding failed.");
            throw new IllegalArgumentException(e);
        } catch (NoSuchAlgorithmException e2) {
            throw new RuntimeException(e2);
        } catch (InvalidKeySpecException e3) {
            BillingLogger.log("Invalid key specification.");
            throw new IllegalArgumentException(e3);
        }
    }

    public static boolean verify(PublicKey publicKey, String signedData, String signature) {
        try {
            Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
            sig.initVerify(publicKey);
            sig.update(signedData.getBytes());
            if (!sig.verify(Base64.decode(signature))) {
                BillingLogger.log("Signature verification failed.");
                return false;
            }
            return true;
        } catch (Base64DecoderException e) {
            BillingLogger.log("Base64 decoding failed.");
            return false;
        } catch (InvalidKeyException e2) {
            BillingLogger.log("Invalid key specification.");
            return false;
        } catch (NoSuchAlgorithmException e3) {
            BillingLogger.log("NoSuchAlgorithmException.");
            return false;
        } catch (SignatureException e4) {
            BillingLogger.log("Signature exception.");
            return false;
        }
    }
}
