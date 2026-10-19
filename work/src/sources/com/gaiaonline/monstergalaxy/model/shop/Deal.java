package com.gaiaonline.monstergalaxy.model.shop;

/* JADX INFO: loaded from: classes.dex */
public class Deal {
    private String buttonTitle;
    private String description;
    private String iconAssetName;
    private String title;
    private DealType type;

    public enum DealType {
        MOGA_CASH,
        STARSEEDS,
        BLUE_COFFEE;

        /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
        public static DealType[] valuesCustom() {
            DealType[] dealTypeArrValuesCustom = values();
            int length = dealTypeArrValuesCustom.length;
            DealType[] dealTypeArr = new DealType[length];
            System.arraycopy(dealTypeArrValuesCustom, 0, dealTypeArr, 0, length);
            return dealTypeArr;
        }
    }

    public Deal(DealType type, String title, String description, String buttonTitle, String iconAssetName) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.buttonTitle = buttonTitle;
        this.iconAssetName = iconAssetName;
    }

    public DealType getType() {
        return this.type;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getButtonTitle() {
        return this.buttonTitle;
    }

    public void setButtonTitle(String buttonTitle) {
        this.buttonTitle = buttonTitle;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconAssetName() {
        return this.iconAssetName;
    }

    public void setIconAssetName(String iconAssetName) {
        this.iconAssetName = iconAssetName;
    }
}
