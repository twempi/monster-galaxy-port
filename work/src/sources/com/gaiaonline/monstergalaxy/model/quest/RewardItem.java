package com.gaiaonline.monstergalaxy.model.quest;

import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class RewardItem implements Serializable {
    private static final long serialVersionUID = 859664386551702901L;
    private int count;
    private Type type;

    public enum RewardSource {
        QUEST,
        NODE,
        DAILY_PRICE;

        /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
        public static RewardSource[] valuesCustom() {
            RewardSource[] rewardSourceArrValuesCustom = values();
            int length = rewardSourceArrValuesCustom.length;
            RewardSource[] rewardSourceArr = new RewardSource[length];
            System.arraycopy(rewardSourceArrValuesCustom, 0, rewardSourceArr, 0, length);
            return rewardSourceArr;
        }
    }

    public RewardItem(int type, int count) {
        this.type = Type.getValue(type);
        this.count = count;
    }

    public Type getType() {
        return this.type;
    }

    public int getCount() {
        return this.count;
    }

    public String toString() {
        return String.valueOf(this.count) + " " + this.type.title;
    }

    public enum Type {
        REWARD_BLUE_COFFEE(2, "Blue Coffee"),
        REWARD_STAR_SEED(4, "Starseed");

        private String title;
        private int value;

        /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
        public static Type[] valuesCustom() {
            Type[] typeArrValuesCustom = values();
            int length = typeArrValuesCustom.length;
            Type[] typeArr = new Type[length];
            System.arraycopy(typeArrValuesCustom, 0, typeArr, 0, length);
            return typeArr;
        }

        Type(int value, String title) {
            this.value = value;
            this.title = title;
        }

        public int getValue() {
            return this.value;
        }

        public static Type getValue(int value) {
            for (Type type : valuesCustom()) {
                if (type.getValue() == value) {
                    return type;
                }
            }
            return null;
        }
    }
}
