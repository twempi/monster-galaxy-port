package com.gaiaonline.monstergalaxy.model.shop;

import com.gaiaonline.monstergalaxy.analytics.Analytics;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MonsterGalaxy;
import com.gaiaonline.monstergalaxy.app.ScreenCode;
import com.gaiaonline.monstergalaxy.app.SoundManager;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.model.moga.MogaType;
import com.gaiaonline.monstergalaxy.model.trainer.Trainer;

/* JADX INFO: loaded from: classes.dex */
public class Shop {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$shop$ProductType;
    private static ShopListener listener;
    private static Products products;

    public interface ShopListener {
        void onProductReceived(ProductType productType);
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$shop$ProductType() {
        int[] iArr = $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$shop$ProductType;
        if (iArr == null) {
            iArr = new int[ProductType.valuesCustom().length];
            try {
                iArr[ProductType.BLUE_COFFEE.ordinal()] = 2;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ProductType.MOGA.ordinal()] = 4;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[ProductType.MOGA_CASH.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[ProductType.STARSEEDS.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            $SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$shop$ProductType = iArr;
        }
        return iArr;
    }

    public static void setListener(ShopListener listener2) {
        listener = listener2;
    }

    public static synchronized Products getProducts() {
        if (products == null) {
            products = new Products();
        }
        return products;
    }

    public static Product addBoughtProduct(String productId) {
        return addBoughtProduct(getProducts().getProduct(productId));
    }

    public static Product getProduct(String productId) {
        return getProducts().getProduct(productId);
    }

    public static Product getMogaProduct(int mogaTypeId) {
        return getProduct(String.valueOf(mogaTypeId) + ProductType.MOGA.getTypeId());
    }

    public static Product addBoughtProduct(Product product) {
        if (product == null) {
            return null;
        }
        Trainer trainer = Game.getTrainer();
        String productType = null;
        String productQuantity = null;
        switch ($SWITCH_TABLE$com$gaiaonline$monstergalaxy$model$shop$ProductType()[product.getType().ordinal()]) {
            case 1:
                trainer.addCoins(product.getQuantity());
                productType = "moga_cash";
                productQuantity = "mogacash" + product.getQuantity();
                break;
            case 2:
                trainer.addBlueCoffeeCups(product.getQuantity());
                productType = "blue_coffee";
                productQuantity = "coffee" + product.getQuantity();
                break;
            case 3:
                trainer.addStarSeeds(product.getQuantity());
                productType = "starseed";
                productQuantity = "starseeds" + product.getQuantity();
                break;
            case 4:
                String productId = product.getId();
                int mogaTypeId = Integer.parseInt(productId.substring(0, productId.indexOf(".")));
                MogaType mogaType = Game.getStorage().getMogaType(mogaTypeId);
                Moga moga = Moga.newMogaFromType(mogaType);
                moga.setName(null);
                trainer.getTeam().getMogas().clear();
                trainer.getMogas().clear();
                trainer.addMoga(moga);
                trainer.getTeam().addMoga(moga);
                productType = "starting_moga";
                productQuantity = "mg" + mogaTypeId;
                MonsterGalaxy.showScreen(ScreenCode.CHOOSE_MOGA_NAME);
                break;
        }
        Game.save();
        if (product.getPrice() > 0.0f) {
            int priceInCents = (int) (product.getPrice() * 100.0f);
            String price = String.valueOf(product.getPrice());
            String currentIslandId = String.valueOf(trainer.getCurrentIsland().getId());
            Analytics analytics = Game.getAnalytics();
            analytics.trackEvent("monetization", "in_app_purchase", productType, productQuantity, price, currentIslandId);
            analytics.trackRevenue(productType, productQuantity, priceInCents);
            analytics.uploadPurchaseEvent("Purchase Product: " + product.getId(), product.getPrice());
        }
        SoundManager.playSound(SoundManager.SOUND_MOGA_CASH);
        if (listener != null) {
            listener.onProductReceived(product.getType());
            return product;
        }
        return product;
    }
}
