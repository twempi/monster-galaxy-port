package com.gaiaonline.monstergalaxy.settings;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public enum NotificationType {
    DAILY_PRIZE("ClaimDailyPrize"),
    MOGAS_RESTED("AllMogasRested"),
    TAMER_ENERGY_FULL("EnergyFullyCharged"),
    ISLAND("Island");

    private String code;

    /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
    public static NotificationType[] valuesCustom() {
        NotificationType[] notificationTypeArrValuesCustom = values();
        int length = notificationTypeArrValuesCustom.length;
        NotificationType[] notificationTypeArr = new NotificationType[length];
        System.arraycopy(notificationTypeArrValuesCustom, 0, notificationTypeArr, 0, length);
        return notificationTypeArr;
    }

    NotificationType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static NotificationType getByCode(String code) {
        for (NotificationType type : valuesCustom()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
