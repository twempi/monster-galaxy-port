package com.gaiaonline.monstergalaxy.app;

import android.app.Application;
import com.gaiaonline.monstergalaxy.storage.SQLiteHelper;
import com.nextive.android.billing.market.BillingConstants;
import com.nextive.android.billing.market.BillingResponseHandler;
import com.nextive.android.billing.market.BillingSecurity;
import com.nextive.android.billing.market.BillingService;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class MonsterGalaxyApp extends Application {
    public static BillingService BILLING_SERVICE;
    public static final MarketPurchaseListener PURCHASE_LISTENER = new MarketPurchaseListener();
    private static MonsterGalaxyApp instance;
    private SQLiteHelper dbHelper;

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        BillingSecurity.setBase64PublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsswC4eF0Uvaj7PAUzyucWUZ+tNAoiXJRolYDp9K88cvD0El1uSCR9wDDfzMnEjPnyQNKVyPVsHS2FYdcl85TQru5CmbezJyg9SNKRoe2fTZ+xukqKuDEwCWjJpgNZuwyQ1lkU9aJar++2LEWRqVRUvn/jVNkvA+8p1PH+JZmnwgFzWUJnxaFJs8LPvWv5FYkvECtG2StrKW7/CyTGX3rAKfKj3dk352BiUxXlcDGovmA1ss4yPufQu8wUSWD6HGkbEN2R7XqQSr+H6iEC80+oheyPu/FmzU4mpTlOhQASm2spoqQJ3TVi4S663Vkw6AVvNQxC5PRhickxpp2obr7PQIDAQAB");
        BillingResponseHandler.registerPurchaseListener(PURCHASE_LISTENER);
        BillingConstants.USE_MOCK_DATA = true;
        instance = this;
    }

    @Override // android.app.Application
    public void onTerminate() {
        if (this.dbHelper != null) {
            this.dbHelper.close();
        }
        instance = null;
        super.onTerminate();
    }

    public SQLiteHelper getDbHelper() {
        if (this.dbHelper == null) {
            this.dbHelper = new SQLiteHelper(getApplicationContext());
        }
        return this.dbHelper;
    }

    public static MonsterGalaxyApp getInstance() {
        return instance;
    }
}
