package com.gaiaonline.monstergalaxy.app;

import com.gaiaonline.monstergalaxy.platform.PlatformHelper;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Share {
    private static String getGameLink() {
        if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2) {
            return PlatformHelper.getSourceStore() == PlatformHelper.Store.AMAZON ? "http://m.facebook.com/appcenter/mogaexile" : "http://m.facebook.com/appcenter/mogaexile";
        }
        return PlatformHelper.getSourceStore() == PlatformHelper.Store.AMAZON ? "http://m.facebook.com/appcenter/monsterg" : "http://m.facebook.com/appcenter/monsterg";
    }

    public static void share() {
        String shareText = "I'm playing Monster Galaxy and you should too! You battle against awesome monsters and train them to fight for you--and it's FREE. Get it on your phone now so we can start playing together! " + getGameLink();
        Game.getPlatformHelper().share(shareText);
    }
}
