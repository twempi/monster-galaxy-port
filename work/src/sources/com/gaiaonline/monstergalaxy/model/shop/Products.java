package com.gaiaonline.monstergalaxy.model.shop;

import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class Products {
    private List<Product> products;

    public List<Product> getProducts() {
        if (this.products == null) {
            this.products = Game.getStorage().getProducts();
        }
        return this.products;
    }

    public Product getProduct(String id) {
        if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2 && !id.endsWith(".mge")) {
            id = String.valueOf(id) + ".mge";
        }
        for (Product p : getProducts()) {
            if (p.getId().equalsIgnoreCase(id)) {
                return p;
            }
        }
        return null;
    }
}
