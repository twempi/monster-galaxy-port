package com.gaiaonline.monstergalaxy.battle.anim;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Logger;
import com.gaiaonline.monstergalaxy.battle.effects.EffectType;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class MogaAction extends Action {
    private EffectType effect;
    private Actor target;
    private ScreenElement.RelPoint where;
    private static Logger logger = new Logger("MogaAction");
    private static Set<String> ignoredSelectors = new HashSet();

    static {
        ignoredSelectors.add("showRunAwayParticles");
    }

    public static MogaAction newInstance(String selector) {
        MogaAction mogaAction = new MogaAction();
        String effectName = null;
        mogaAction.where = ScreenElement.RelPoint.CENTER;
        if (selector.startsWith("showHit")) {
            effectName = "HIT_" + selector.charAt(7);
        } else if ("showAquarius".equals(selector)) {
            effectName = "AQUARIUS_1";
        } else if ("showAquarius2".equals(selector)) {
            effectName = "AQUARIUS_2";
        } else if ("showAries".equals(selector)) {
            effectName = "ARIES_1";
        } else if ("showAries2".equals(selector)) {
            effectName = "ARIES_2";
        } else if ("showCancer".equals(selector)) {
            effectName = "CANCER_1";
        } else if ("showCancer2".equals(selector)) {
            effectName = "CANCER_2";
        } else if ("showCapricorn".equals(selector)) {
            effectName = "CAPRICORN";
        } else if ("showGemini".equals(selector)) {
            effectName = "GEMINI";
        } else if ("showLeo".equals(selector)) {
            effectName = "LEO";
        } else if ("showLibra".equals(selector)) {
            effectName = "LIBRA";
        } else if ("showPisces".equals(selector)) {
            effectName = "PISCES";
        } else if ("showSagittarius".equals(selector)) {
            effectName = "SAGITTARIUS";
        } else if ("showScorpio".equals(selector)) {
            effectName = "SCORPIO";
        } else if ("showTaurus".equals(selector)) {
            effectName = "TAURUS";
        } else if ("showVirgo".equals(selector)) {
            effectName = "VIRGO";
        } else if ("chargeA".equals(selector)) {
            effectName = "CHARGE_A";
            mogaAction.where = ScreenElement.RelPoint.CENTER_BOTTOM;
        } else if ("chargeB".equals(selector)) {
            effectName = "CHARGE_B";
            mogaAction.where = ScreenElement.RelPoint.CENTER_BOTTOM;
        } else if ("explode".equals(selector)) {
            effectName = "EXPLOSION";
        }
        if (effectName != null) {
            mogaAction.effect = EffectType.valueOf(effectName);
            return mogaAction;
        }
        logger.log("Selector not supported: " + selector);
        if (ignoredSelectors.contains(selector)) {
            return null;
        }
        return mogaAction;
    }

    private MogaAction() {
    }

    public MogaAction(EffectType effect) {
        this.effect = effect;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void setTarget(Actor actor) {
        this.target = actor;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public Actor getTarget() {
        return this.target;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public void act(float delta) {
        if (this.effect != null) {
            ((MogaActor) this.target).doEffect(this.effect, this.where);
        }
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public boolean isDone() {
        return true;
    }

    @Override // com.badlogic.gdx.scenes.scene2d.Action
    public Action copy() {
        Action ma = new MogaAction(this.effect);
        ma.setTarget(this.target);
        return ma;
    }
}
