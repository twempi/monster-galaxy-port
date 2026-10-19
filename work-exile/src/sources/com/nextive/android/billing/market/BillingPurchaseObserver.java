package com.nextive.android.billing.market;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface BillingPurchaseObserver {
    void onBillingSupported(boolean z);

    void onPurchaseStateChange(BillingConstants.PurchaseState purchaseState, String str, String str2, long j, String str3);

    void onRequestPurchaseResponse(BillingService.RequestPurchase requestPurchase, BillingConstants.ResponseCode responseCode);

    void onRestoreTransactionsResponse(BillingService.RestoreTransactions restoreTransactions, BillingConstants.ResponseCode responseCode);
}
