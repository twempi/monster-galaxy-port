package com.sessionm.api.ext;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public interface Achievement {
    int getAchievementId();

    String getName();

    int getTimesEarned();

    String getUnclaimed();

    Object getValueForKey(String str);
}
