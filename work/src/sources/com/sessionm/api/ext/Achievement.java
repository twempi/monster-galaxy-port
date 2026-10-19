package com.sessionm.api.ext;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public interface Achievement {
    int getAchievementId();

    String getName();

    int getTimesEarned();

    String getUnclaimed();

    Object getValueForKey(String str);
}
