package com.nextive.android.billing.market;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.android.vending.billing.IMarketBillingService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/* JADX INFO: loaded from: classes.dex */
public class BillingService extends Service implements ServiceConnection {
    private static LinkedList<BillingRequest> mPendingRequests = new LinkedList<>();
    private static HashMap<Long, BillingRequest> mSentRequests = new HashMap<>();
    private static IMarketBillingService mService;

    public abstract class BillingRequest {
        protected long mRequestId;
        private final int mStartId;

        protected abstract long run() throws RemoteException;

        public BillingRequest(int startId) {
            this.mStartId = startId;
        }

        public int getStartId() {
            return this.mStartId;
        }

        public boolean runRequest() {
            if (runIfConnected()) {
                return true;
            }
            if (BillingService.this.bindToMarketBillingService()) {
                BillingService.mPendingRequests.add(this);
                return true;
            }
            return false;
        }

        public boolean runIfConnected() {
            if (BillingService.mService != null) {
                try {
                    this.mRequestId = run();
                    if (this.mRequestId >= 0) {
                        BillingService.mSentRequests.put(Long.valueOf(this.mRequestId), this);
                    }
                    return true;
                } catch (RemoteException e) {
                    onRemoteException(e);
                } catch (NullPointerException e2) {
                    BillingLogger.log("Caught NullPointerException in billing service");
                }
            }
            return false;
        }

        protected void onRemoteException(RemoteException e) {
            BillingLogger.log("remote billing service crashed");
            BillingService.mService = null;
        }

        protected void responseCodeReceived(BillingConstants.ResponseCode responseCode) {
        }

        protected Bundle makeRequestBundle(String method) {
            Bundle request = new Bundle();
            request.putString(BillingConstants.BILLING_REQUEST_METHOD, method);
            request.putInt(BillingConstants.BILLING_REQUEST_API_VERSION, 1);
            request.putString(BillingConstants.BILLING_REQUEST_PACKAGE_NAME, BillingService.this.getPackageName());
            Log.i("BillingService", "PACKAGE_NAME :" + BillingService.this.getPackageName());
            return request;
        }

        protected void logResponseCode(String method, Bundle response) {
            BillingConstants.ResponseCode responseCode = BillingConstants.ResponseCode.valueOf(response.getInt(BillingConstants.BILLING_RESPONSE_RESPONSE_CODE));
            BillingLogger.log(String.valueOf(method) + " received " + responseCode.toString());
        }
    }

    public class CheckBillingSupported extends BillingRequest {
        public CheckBillingSupported() {
            super(-1);
        }

        @Override // com.nextive.android.billing.market.BillingService.BillingRequest
        protected long run() throws RemoteException {
            Bundle request = makeRequestBundle("CHECK_BILLING_SUPPORTED");
            Bundle response = BillingService.mService.sendBillingRequest(request);
            int responseCode = response.getInt(BillingConstants.BILLING_RESPONSE_RESPONSE_CODE);
            boolean billingSupported = responseCode == BillingConstants.ResponseCode.RESULT_OK.ordinal();
            BillingResponseHandler.checkBillingSupportedResponse(billingSupported);
            return BillingConstants.BILLING_RESPONSE_INVALID_REQUEST_ID;
        }
    }

    public class RequestPurchase extends BillingRequest {
        public final String mDeveloperPayload;
        public final String mProductId;

        public RequestPurchase(BillingService billingService, String itemId) {
            this(itemId, null);
        }

        public RequestPurchase(String itemId, String developerPayload) {
            super(-1);
            this.mProductId = itemId;
            this.mDeveloperPayload = developerPayload;
        }

        @Override // com.nextive.android.billing.market.BillingService.BillingRequest
        protected long run() throws RemoteException, NullPointerException {
            Bundle request = makeRequestBundle("REQUEST_PURCHASE");
            request.putString(BillingConstants.BILLING_REQUEST_ITEM_ID, this.mProductId);
            if (this.mDeveloperPayload != null) {
                request.putString(BillingConstants.BILLING_REQUEST_DEVELOPER_PAYLOAD, this.mDeveloperPayload);
            }
            if (BillingService.mService != null) {
                Bundle response = BillingService.mService.sendBillingRequest(request);
                PendingIntent pendingIntent = (PendingIntent) response.getParcelable(BillingConstants.BILLING_RESPONSE_PURCHASE_INTENT);
                if (pendingIntent == null) {
                    BillingLogger.log("Error with requestPurchase");
                    return BillingConstants.BILLING_RESPONSE_INVALID_REQUEST_ID;
                }
                Intent intent = new Intent();
                BillingResponseHandler.buyPageIntentResponse(pendingIntent, intent);
                return response.getLong(BillingConstants.BILLING_RESPONSE_REQUEST_ID, BillingConstants.BILLING_RESPONSE_INVALID_REQUEST_ID);
            }
            return BillingConstants.BILLING_RESPONSE_INVALID_REQUEST_ID;
        }

        @Override // com.nextive.android.billing.market.BillingService.BillingRequest
        protected void responseCodeReceived(BillingConstants.ResponseCode responseCode) {
            BillingResponseHandler.responseCodeReceived(BillingService.this, this, responseCode);
        }
    }

    public class ConfirmNotifications extends BillingRequest {
        final String[] mNotifyIds;

        public ConfirmNotifications(int startId, String[] notifyIds) {
            super(startId);
            this.mNotifyIds = notifyIds;
        }

        @Override // com.nextive.android.billing.market.BillingService.BillingRequest
        protected long run() throws RemoteException {
            BillingLogger.log("BillingService=>RUN= CONFIRM_NOTIFICATIONS");
            Bundle request = makeRequestBundle("CONFIRM_NOTIFICATIONS");
            request.putStringArray(BillingConstants.BILLING_REQUEST_NOTIFY_IDS, this.mNotifyIds);
            Bundle response = BillingService.mService.sendBillingRequest(request);
            logResponseCode("confirmNotifications", response);
            return response.getLong(BillingConstants.BILLING_RESPONSE_REQUEST_ID, BillingConstants.BILLING_RESPONSE_INVALID_REQUEST_ID);
        }
    }

    public class GetPurchaseInformation extends BillingRequest {
        long mNonce;
        final String[] mNotifyIds;

        public GetPurchaseInformation(int startId, String[] notifyIds) {
            super(startId);
            this.mNotifyIds = notifyIds;
        }

        @Override // com.nextive.android.billing.market.BillingService.BillingRequest
        protected long run() throws RemoteException {
            this.mNonce = BillingSecurity.generateNonce();
            Bundle request = makeRequestBundle("GET_PURCHASE_INFORMATION");
            request.putLong(BillingConstants.BILLING_REQUEST_NONCE, this.mNonce);
            request.putStringArray(BillingConstants.BILLING_REQUEST_NOTIFY_IDS, this.mNotifyIds);
            Bundle response = BillingService.mService.sendBillingRequest(request);
            logResponseCode("getPurchaseInformation", response);
            return response.getLong(BillingConstants.BILLING_RESPONSE_REQUEST_ID, BillingConstants.BILLING_RESPONSE_INVALID_REQUEST_ID);
        }

        @Override // com.nextive.android.billing.market.BillingService.BillingRequest
        protected void onRemoteException(RemoteException e) {
            super.onRemoteException(e);
            BillingSecurity.removeNonce(this.mNonce);
        }
    }

    public class RestoreTransactions extends BillingRequest {
        long mNonce;

        public RestoreTransactions() {
            super(-1);
        }

        @Override // com.nextive.android.billing.market.BillingService.BillingRequest
        protected long run() throws RemoteException {
            this.mNonce = BillingSecurity.generateNonce();
            Bundle request = makeRequestBundle("RESTORE_TRANSACTIONS");
            request.putLong(BillingConstants.BILLING_REQUEST_NONCE, this.mNonce);
            Bundle response = BillingService.mService.sendBillingRequest(request);
            logResponseCode("restoreTransactions", response);
            return response.getLong(BillingConstants.BILLING_RESPONSE_REQUEST_ID, BillingConstants.BILLING_RESPONSE_INVALID_REQUEST_ID);
        }

        @Override // com.nextive.android.billing.market.BillingService.BillingRequest
        protected void onRemoteException(RemoteException e) {
            super.onRemoteException(e);
            BillingSecurity.removeNonce(this.mNonce);
        }

        @Override // com.nextive.android.billing.market.BillingService.BillingRequest
        protected void responseCodeReceived(BillingConstants.ResponseCode responseCode) {
            BillingResponseHandler.responseCodeReceived(BillingService.this, this, responseCode);
        }
    }

    public void setContext(Context context) {
        attachBaseContext(context);
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onStart(Intent intent, int startId) {
        handleCommand(intent, startId);
    }

    public void handleCommand(Intent intent, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            BillingLogger.log("BillingService=>ACTION=" + action);
            if (BillingConstants.ACTION_CONFIRM_NOTIFICATION.equals(action)) {
                String[] notifyIds = intent.getStringArrayExtra(BillingConstants.NOTIFICATION_ID);
                confirmNotifications(startId, notifyIds);
                return;
            }
            if (BillingConstants.ACTION_GET_PURCHASE_INFORMATION.equals(action)) {
                String notifyId = intent.getStringExtra(BillingConstants.NOTIFICATION_ID);
                BillingLogger.log("NOTIFY-ID=>" + notifyId);
                getPurchaseInformation(startId, new String[]{notifyId});
            } else {
                if (BillingConstants.ACTION_PURCHASE_STATE_CHANGED.equals(action)) {
                    String signedData = intent.getStringExtra(BillingConstants.INAPP_SIGNED_DATA);
                    String signature = intent.getStringExtra(BillingConstants.INAPP_SIGNATURE);
                    BillingLogger.log("INAPP_SIGNED_DATA=>" + signedData);
                    BillingLogger.log("SIGNATURE=>" + signature);
                    purchaseStateChanged(startId, signedData, signature);
                    return;
                }
                if (BillingConstants.ACTION_RESPONSE_CODE.equals(action)) {
                    long requestId = intent.getLongExtra(BillingConstants.INAPP_REQUEST_ID, -1L);
                    int responseCodeIndex = intent.getIntExtra(BillingConstants.INAPP_RESPONSE_CODE, BillingConstants.ResponseCode.RESULT_ERROR.ordinal());
                    BillingConstants.ResponseCode responseCode = BillingConstants.ResponseCode.valueOf(responseCodeIndex);
                    checkResponseCode(requestId, responseCode);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean bindToMarketBillingService() {
        try {
            boolean bindResult = bindService(new Intent(BillingConstants.MARKET_BILLING_SERVICE_ACTION), this, 1);
            if (bindResult) {
                return true;
            }
            BillingLogger.log("Could not bind to service.");
        } catch (SecurityException e) {
            BillingLogger.log("Security exception: " + e);
        }
        return false;
    }

    public boolean checkBillingSupported() {
        return new CheckBillingSupported().runRequest();
    }

    public boolean requestPurchase(String productId, String developerPayload) {
        return new RequestPurchase(productId, developerPayload).runRequest();
    }

    public boolean requestPurchase(String productId) {
        return requestPurchase(productId, null);
    }

    public boolean restoreTransactions() {
        return new RestoreTransactions().runRequest();
    }

    private boolean confirmNotifications(int startId, String[] notifyIds) {
        return new ConfirmNotifications(startId, notifyIds).runRequest();
    }

    private boolean getPurchaseInformation(int startId, String[] notifyIds) {
        return new GetPurchaseInformation(startId, notifyIds).runRequest();
    }

    private void purchaseStateChanged(int startId, String signedData, String signature) {
        ArrayList<BillingSecurity.VerifiedPurchase> purchases = BillingSecurity.verifyPurchase(signedData, signature);
        if (purchases != null) {
            ArrayList<String> notifyList = new ArrayList<>();
            for (BillingSecurity.VerifiedPurchase vp : purchases) {
                if (vp.notificationId != null) {
                    notifyList.add(vp.notificationId);
                }
                BillingResponseHandler.purchaseResponse(this, vp.purchaseState, vp.productId, vp.orderId, vp.purchaseTime, vp.developerPayload);
            }
            if (!notifyList.isEmpty()) {
                String[] notifyIds = (String[]) notifyList.toArray(new String[notifyList.size()]);
                confirmNotifications(startId, notifyIds);
            }
        }
    }

    private void checkResponseCode(long requestId, BillingConstants.ResponseCode responseCode) {
        BillingLogger.log("BillingService=>checkResponseCode requestId=" + requestId);
        BillingLogger.log("BillingService=>checkResponseCode responseCode=" + responseCode);
        BillingRequest request = mSentRequests.get(Long.valueOf(requestId));
        if (request != null) {
            request.responseCodeReceived(responseCode);
        }
        mSentRequests.remove(Long.valueOf(requestId));
    }

    public boolean isPurchaseTransactionPending() {
        return mSentRequests != null && mSentRequests.size() > 1;
    }

    private void runPendingRequests() {
        int maxStartId = -1;
        while (true) {
            BillingRequest request = mPendingRequests.peek();
            if (request != null) {
                if (request.runIfConnected()) {
                    mPendingRequests.remove();
                    if (maxStartId < request.getStartId()) {
                        maxStartId = request.getStartId();
                    }
                } else {
                    bindToMarketBillingService();
                    return;
                }
            } else {
                if (maxStartId >= 0) {
                    stopSelf(maxStartId);
                    return;
                }
                return;
            }
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService = IMarketBillingService.Stub.asInterface(service);
        runPendingRequests();
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName name) {
        BillingLogger.log("Billing service disconnected");
        mService = null;
    }

    public void unbind() {
        try {
            unbindService(this);
        } catch (IllegalArgumentException e) {
        }
    }
}
