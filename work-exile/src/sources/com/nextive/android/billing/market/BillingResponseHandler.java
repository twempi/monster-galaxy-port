package com.nextive.android.billing.market;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class BillingResponseHandler {
    private static Activity mActivity;
    private static Method mStartIntentSender;
    private static BillingPurchaseObserver sPurchaseObserver;
    private static final Class[] START_INTENT_SENDER_SIG = {IntentSender.class, Intent.class, Integer.TYPE, Integer.TYPE, Integer.TYPE};
    private static Object[] mStartIntentSenderArgs = new Object[5];

    public static synchronized void registerPurchaseListener(BillingPurchaseObserver observer) {
        sPurchaseObserver = observer;
    }

    public static synchronized void unregisterPurchaseListener() {
        sPurchaseObserver = null;
    }

    public static synchronized void registerActivity(Activity activity) {
        mActivity = activity;
        initCompatibilityLayer();
    }

    public static synchronized void unregisterActivity() {
        mActivity = null;
    }

    public static void checkBillingSupportedResponse(boolean supported) {
        if (sPurchaseObserver != null) {
            sPurchaseObserver.onBillingSupported(supported);
        }
    }

    public static void buyPageIntentResponse(PendingIntent pendingIntent, Intent intent) {
        if (mActivity != null) {
            startBuyPageActivity(pendingIntent, intent);
        }
    }

    public static void purchaseResponse(Context context, final BillingConstants.PurchaseState purchaseState, final String productId, final String orderId, final long purchaseTime, final String developerPayload) {
        new Thread(new Runnable() { // from class: com.nextive.android.billing.market.BillingResponseHandler.1
            @Override // java.lang.Runnable
            public void run() {
                synchronized (BillingResponseHandler.class) {
                    if (BillingResponseHandler.sPurchaseObserver != null) {
                        BillingResponseHandler.sPurchaseObserver.onPurchaseStateChange(purchaseState, productId, orderId, purchaseTime, developerPayload);
                    }
                }
            }
        }).start();
    }

    public static void responseCodeReceived(Context context, BillingService.RequestPurchase request, BillingConstants.ResponseCode responseCode) {
        if (sPurchaseObserver != null) {
            sPurchaseObserver.onRequestPurchaseResponse(request, responseCode);
        }
    }

    public static void responseCodeReceived(Context context, BillingService.RestoreTransactions request, BillingConstants.ResponseCode responseCode) {
        if (sPurchaseObserver != null) {
            sPurchaseObserver.onRestoreTransactionsResponse(request, responseCode);
        }
    }

    private static void initCompatibilityLayer() {
        try {
            mStartIntentSender = mActivity.getClass().getMethod("startIntentSender", START_INTENT_SENDER_SIG);
        } catch (NoSuchMethodException e) {
            mStartIntentSender = null;
        } catch (SecurityException e2) {
            mStartIntentSender = null;
        }
    }

    private static void startBuyPageActivity(PendingIntent pendingIntent, Intent intent) {
        if (mStartIntentSender != null) {
            try {
                mStartIntentSenderArgs[0] = pendingIntent.getIntentSender();
                mStartIntentSenderArgs[1] = intent;
                mStartIntentSenderArgs[2] = 0;
                mStartIntentSenderArgs[3] = 0;
                mStartIntentSenderArgs[4] = 0;
                mStartIntentSender.invoke(mActivity, mStartIntentSenderArgs);
                return;
            } catch (Exception e) {
                BillingLogger.log("BillingResponseHandler->error starting activity" + e);
                return;
            }
        }
        try {
            pendingIntent.send(mActivity, 0, intent);
        } catch (PendingIntent.CanceledException e2) {
            BillingLogger.log("BillingResponseHandler->error starting activity" + e2);
        }
    }
}
