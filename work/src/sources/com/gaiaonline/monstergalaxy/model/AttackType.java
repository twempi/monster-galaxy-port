package com.gaiaonline.monstergalaxy.model;

/* JADX INFO: loaded from: classes.dex */
public enum AttackType {
    REGULAR,
    ZODIAC;

    /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
    public static AttackType[] valuesCustom() {
        AttackType[] attackTypeArrValuesCustom = values();
        int length = attackTypeArrValuesCustom.length;
        AttackType[] attackTypeArr = new AttackType[length];
        System.arraycopy(attackTypeArrValuesCustom, 0, attackTypeArr, 0, length);
        return attackTypeArr;
    }
}
