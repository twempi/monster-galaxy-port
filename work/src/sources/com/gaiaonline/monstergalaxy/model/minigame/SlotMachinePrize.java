package com.gaiaonline.monstergalaxy.model.minigame;

import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.model.moga.MogaType;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class SlotMachinePrize {
    private int blueCoffees;
    private String displayName;
    private int id;
    private List<SlotmachinePrizeItem> items;
    private int mogaCash;
    private int mogaTypeId;
    private String name;
    private int starSeeds;
    private String type;

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return (obj instanceof SlotMachinePrize) && this.id == ((SlotMachinePrize) obj).id;
    }

    public int hashCode() {
        return this.id;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getBlueCoffees() {
        return this.blueCoffees;
    }

    public void setBlueCoffees(int blueCoffees) {
        this.blueCoffees = blueCoffees;
    }

    public int getStarSeeds() {
        return this.starSeeds;
    }

    public void setStarSeeds(int starSeeds) {
        this.starSeeds = starSeeds;
    }

    public int getMogaCash() {
        return this.mogaCash;
    }

    public void setMogaCash(int mogaCash) {
        this.mogaCash = mogaCash;
    }

    public int getMogaTypeId() {
        return this.mogaTypeId;
    }

    public void setMogaTypeId(int mogaTypeId) {
        this.mogaTypeId = mogaTypeId;
    }

    public List<SlotmachinePrizeItem> getItems() {
        if (this.items == null) {
            this.items = new ArrayList();
            if (this.mogaTypeId > 0) {
                MogaType mogaType = Game.getStorage().getMogaType(this.mogaTypeId);
                SlotmachinePrizeItem item = new SlotmachinePrizeItem();
                item.setId(String.valueOf(this.mogaTypeId));
                item.setName(this.name);
                item.setAssetName(mogaType.getAssetName());
                item.setRarityAssetName(mogaType.getRarityAssetName());
                item.setCount(1);
                item.setType(Slotmachine.PrizeType.MOGA_TYPE);
                this.items.add(item);
            } else {
                if (this.starSeeds > 0) {
                    SlotmachinePrizeItem item2 = new SlotmachinePrizeItem();
                    item2.setName(this.name);
                    item2.setAssetName("capture.starseed.icon");
                    item2.setCount(this.starSeeds);
                    item2.setType(Slotmachine.PrizeType.STAR_SEED);
                    this.items.add(item2);
                }
                if (this.blueCoffees > 0) {
                    SlotmachinePrizeItem item3 = new SlotmachinePrizeItem();
                    item3.setName(this.name);
                    item3.setAssetName("blue.coffee.icon");
                    item3.setCount(this.blueCoffees);
                    item3.setType(Slotmachine.PrizeType.BLUE_COFFEE);
                    this.items.add(item3);
                }
                if (this.mogaCash > 0) {
                    SlotmachinePrizeItem item4 = new SlotmachinePrizeItem();
                    item4.setName(this.name);
                    item4.setAssetName("capture.mcash");
                    item4.setCount(this.mogaCash);
                    item4.setType(Slotmachine.PrizeType.MOGA_CASH);
                    this.items.add(item4);
                }
            }
        }
        return this.items;
    }

    public SlotmachinePrizeItem getDefaultPrizeItem() {
        return getItems().get(0);
    }
}
