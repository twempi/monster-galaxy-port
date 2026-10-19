package com.gaiaonline.monstergalaxy.app;

import android.util.Log;
import com.gaiaonline.monstergalaxy.model.shop.Shop;
import com.nextive.android.billing.market.BillingConstants;
import com.nextive.android.billing.market.BillingPurchaseObserver;
import com.nextive.android.billing.market.BillingService;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class MarketPurchaseListener implements BillingPurchaseObserver {
    @Override // com.nextive.android.billing.market.BillingPurchaseObserver
    public void onBillingSupported(boolean supported) {
        Log.i(Game.TAG, "onBillingSupported()->" + supported);
    }

    @Override // com.nextive.android.billing.market.BillingPurchaseObserver
    public void onPurchaseStateChange(BillingConstants.PurchaseState purchaseState, String itemId, String orderId, long purchaseTime, String developerPayload) {
        Log.i(Game.TAG, "onPurchaseStateChange()-> purchaseState=" + purchaseState.name() + " itemId=" + itemId + " orderId=" + orderId + " purchaseItem=" + purchaseTime + " developerPayload=" + developerPayload);
        if (BillingConstants.PurchaseState.PURCHASED.equals(purchaseState)) {
            Shop.addBoughtProduct(itemId);
            Game.getGaiaClient().sendPurchaseData(orderId, itemId);
        }
    }

    @Override // com.nextive.android.billing.market.BillingPurchaseObserver
    public void onRequestPurchaseResponse(BillingService.RequestPurchase request, BillingConstants.ResponseCode responseCode) {
        Log.i(Game.TAG, "onRequestPurchaseResponse()-> request=" + request.toString() + "responseCode=" + responseCode.name());
    }

    @Override // com.nextive.android.billing.market.BillingPurchaseObserver
    public void onRestoreTransactionsResponse(BillingService.RestoreTransactions request, BillingConstants.ResponseCode responseCode) {
        Log.i(Game.TAG, "onRestoreTransactionsResponse()-> request=" + request.toString() + "responseCode=" + responseCode.name());
    }
}
