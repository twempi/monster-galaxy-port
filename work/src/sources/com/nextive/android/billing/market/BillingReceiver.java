package com.nextive.android.billing.market;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/* JADX INFO: loaded from: classes.dex */
public class BillingReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        BillingLogger.log("BillingReceiver=>ACTION=" + action);
        if (BillingConstants.ACTION_PURCHASE_STATE_CHANGED.equals(action)) {
            String signedData = intent.getStringExtra(BillingConstants.INAPP_SIGNED_DATA);
            String signature = intent.getStringExtra(BillingConstants.INAPP_SIGNATURE);
            purchaseStateChanged(context, signedData, signature);
        } else if (BillingConstants.ACTION_NOTIFY.equals(action)) {
            String notifyId = intent.getStringExtra(BillingConstants.NOTIFICATION_ID);
            notify(context, notifyId);
        } else {
            if (BillingConstants.ACTION_RESPONSE_CODE.equals(action)) {
                long requestId = intent.getLongExtra(BillingConstants.INAPP_REQUEST_ID, -1L);
                int responseCodeIndex = intent.getIntExtra(BillingConstants.INAPP_RESPONSE_CODE, BillingConstants.ResponseCode.RESULT_ERROR.ordinal());
                checkResponseCode(context, requestId, responseCodeIndex);
                return;
            }
            BillingLogger.log("BillingReceiver->Unexpected action: " + action);
        }
    }

    private void purchaseStateChanged(Context context, String signedData, String signature) {
        Intent intent = new Intent(BillingConstants.ACTION_PURCHASE_STATE_CHANGED);
        intent.setClass(context, BillingService.class);
        intent.putExtra(BillingConstants.INAPP_SIGNED_DATA, signedData);
        intent.putExtra(BillingConstants.INAPP_SIGNATURE, signature);
        context.startService(intent);
    }

    private void notify(Context context, String notifyId) {
        Intent intent = new Intent(BillingConstants.ACTION_GET_PURCHASE_INFORMATION);
        intent.setClass(context, BillingService.class);
        intent.putExtra(BillingConstants.NOTIFICATION_ID, notifyId);
        context.startService(intent);
    }

    private void checkResponseCode(Context context, long requestId, int responseCodeIndex) {
        Intent intent = new Intent(BillingConstants.ACTION_RESPONSE_CODE);
        intent.setClass(context, BillingService.class);
        intent.putExtra(BillingConstants.INAPP_REQUEST_ID, requestId);
        intent.putExtra(BillingConstants.INAPP_RESPONSE_CODE, responseCodeIndex);
        context.startService(intent);
    }
}
