package com.sessionm.api;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public interface User {
    int getPointBalance();

    int getUnclaimedAchievementCount();

    int getUnclaimedAchievementValue();

    Object getValueForKey(String str);

    boolean isOptedOut();

    void setOptedOut(boolean z);
}
