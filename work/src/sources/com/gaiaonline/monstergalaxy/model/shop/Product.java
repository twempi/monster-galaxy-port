package com.gaiaonline.monstergalaxy.model.shop;

import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;

/* JADX INFO: loaded from: classes.dex */
public class Product {
    private String description;
    private String id;
    private float price;
    private int quantity;
    private String title;
    private ProductType type;

    public Product(String id) {
        setId(id);
    }

    public Product(String id, String title, String description, float price) {
        setId(id);
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public Product(String id, String title, String description, float price, ProductType type, int quantity) {
        if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2) {
            this.id = String.valueOf(id) + ".mge";
        } else {
            this.id = id;
        }
        this.title = title;
        this.description = description;
        this.price = price;
        this.type = type;
        this.quantity = quantity;
    }

    public void setId(String id) {
        this.id = id;
        for (ProductType type : ProductType.valuesCustom()) {
            if (id.toLowerCase().contains(type.getTypeId())) {
                this.type = type;
                if (this.type != ProductType.MOGA) {
                    this.quantity = Integer.valueOf(id.replaceAll("\\.", "").replace(type.getTypeId(), "")).intValue();
                }
            }
        }
        if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2) {
            this.id = String.valueOf(id) + ".mge";
        } else {
            this.id = id;
        }
    }

    public String getId() {
        return this.id;
    }

    public ProductType getType() {
        return this.type;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
