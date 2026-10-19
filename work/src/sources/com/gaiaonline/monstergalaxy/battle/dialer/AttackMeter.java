package com.gaiaonline.monstergalaxy.battle.dialer;

import com.gaiaonline.monstergalaxy.screen.ScreenGroup;

/* JADX INFO: loaded from: classes.dex */
public abstract class AttackMeter extends ScreenGroup {
    protected final float STRUCK_DELAY = 0.5f;
    protected final int NORMAL = 0;
    protected final int PERFECT = 1;
    protected final int VERY_GOOD = 2;

    public abstract float getDamageMultiplier();

    public abstract void start();
}
