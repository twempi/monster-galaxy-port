package com.gaiaonline.monstergalaxy.model.minigame;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class SlotmachinePrizeItem {
    private String assetName;
    private int count;
    private String id;
    private String name;
    private String rarityAssetName;
    private Slotmachine.PrizeType type;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssetName() {
        return this.assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Slotmachine.PrizeType getType() {
        return this.type;
    }

    public void setType(Slotmachine.PrizeType type) {
        this.type = type;
    }

    public String getRarityAssetName() {
        return this.rarityAssetName;
    }

    public void setRarityAssetName(String rarityAssetName) {
        this.rarityAssetName = rarityAssetName;
    }
}
