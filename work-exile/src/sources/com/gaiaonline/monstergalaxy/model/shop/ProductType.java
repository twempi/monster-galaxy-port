package com.gaiaonline.monstergalaxy.model.shop;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public enum ProductType {
    MOGA_CASH("mogacash", "capture.mcash"),
    BLUE_COFFEE("coffee", "blue.coffee.icon"),
    STARSEEDS("starseeds", "capture.starseed.icon"),
    MOGA(".moga.moga", null);

    private TextureRegion iconAsset;
    private String iconAssetName;
    private String typeId;

    /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
    public static ProductType[] valuesCustom() {
        ProductType[] productTypeArrValuesCustom = values();
        int length = productTypeArrValuesCustom.length;
        ProductType[] productTypeArr = new ProductType[length];
        System.arraycopy(productTypeArrValuesCustom, 0, productTypeArr, 0, length);
        return productTypeArr;
    }

    ProductType(String typeId, String iconAssetName) {
        this.typeId = typeId;
        this.iconAssetName = iconAssetName;
    }

    public static ProductType getById(String typeId) {
        for (ProductType type : valuesCustom()) {
            if (type.getTypeId().equals(typeId)) {
                return type;
            }
        }
        if ("moga".equals(typeId)) {
            return MOGA;
        }
        return null;
    }

    public String getTypeId() {
        return this.typeId;
    }

    public String getIconAssetName() {
        return this.iconAssetName;
    }

    public void setIconAsset(TextureRegion iconAsset) {
        this.iconAsset = iconAsset;
    }

    public TextureRegion getIconAsset() {
        return this.iconAsset;
    }
}
