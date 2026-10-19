package com.sessionm.core;

import com.sessionm.api.User;
import com.sessionm.json.JSONObject;
import com.sessionm.net.Request;
import com.sessionm.net.RequestListener;
import java.util.HashMap;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class UserImpl implements User {
    private boolean optedOut;
    private final JSONObject playerData;
    private final int pointBalance;
    private final int unclaimedAchievementCount;
    private final int unclaimedAchievementValue;

    UserImpl() {
        this.playerData = null;
        this.pointBalance = 0;
        this.unclaimedAchievementCount = 0;
        this.unclaimedAchievementValue = 0;
    }

    UserImpl(JSONObject jSONObject) {
        this.playerData = jSONObject;
        this.pointBalance = jSONObject.getInt("point_balance");
        this.optedOut = jSONObject.getBoolean("opted_out");
        this.unclaimedAchievementCount = jSONObject.getInt("unclaimed_achievement_count");
        this.unclaimedAchievementValue = jSONObject.getInt("unclaimed_achievement_value");
    }

    @Override // com.sessionm.api.User
    public int getPointBalance() {
        return this.pointBalance;
    }

    @Override // com.sessionm.api.User
    public boolean isOptedOut() {
        return this.optedOut;
    }

    @Override // com.sessionm.api.User
    public void setOptedOut(boolean z) {
        if (Session.getSession() != null) {
            this.optedOut = z;
            HashMap map = new HashMap();
            map.put("member[opted_out]", z ? "1" : "0");
            Request request = new Request(Request.Type.OPT_IN_OUT, map);
            request.setListener(new RequestListener() { // from class: com.sessionm.core.UserImpl.1
                @Override // com.sessionm.net.RequestListener
                public void onRequestSent(Request request2) {
                }

                @Override // com.sessionm.net.RequestListener
                public void onReplyReceived(Request request2) {
                }
            });
            request.send();
        }
    }

    @Override // com.sessionm.api.User
    public int getUnclaimedAchievementCount() {
        return this.unclaimedAchievementCount;
    }

    @Override // com.sessionm.api.User
    public int getUnclaimedAchievementValue() {
        return this.unclaimedAchievementValue;
    }

    @Override // com.sessionm.api.User
    public Object getValueForKey(String str) {
        if (this.playerData == null) {
            return null;
        }
        return this.playerData.getObject(str);
    }
}
