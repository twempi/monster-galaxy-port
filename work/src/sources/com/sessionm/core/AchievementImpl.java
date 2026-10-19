package com.sessionm.core;

import com.sessionm.api.ext.Achievement;
import com.sessionm.json.JSONObject;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public class AchievementImpl implements Achievement {
    private int achievementId;
    protected JSONObject achievementJson;
    private String name;
    private int timesEarned;
    private String unclaimed;

    public AchievementImpl(JSONObject jSONObject) {
        this.name = jSONObject.getString("name");
        this.unclaimed = jSONObject.getString("unclaimed");
        this.timesEarned = jSONObject.getInt("times_earned");
        this.achievementId = jSONObject.getInt("achievement_id");
        this.achievementJson = jSONObject;
    }

    @Override // com.sessionm.api.ext.Achievement
    public int getTimesEarned() {
        return this.timesEarned;
    }

    @Override // com.sessionm.api.ext.Achievement
    public String getName() {
        return this.name;
    }

    @Override // com.sessionm.api.ext.Achievement
    public String getUnclaimed() {
        return this.unclaimed;
    }

    @Override // com.sessionm.api.ext.Achievement
    public int getAchievementId() {
        return this.achievementId;
    }

    @Override // com.sessionm.api.ext.Achievement
    public Object getValueForKey(String str) {
        return this.achievementJson.getObject(str);
    }
}
