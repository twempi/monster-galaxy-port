package com.gaiaonline.monstergalaxy.battle.anim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.AnimationAction;
import com.badlogic.gdx.scenes.scene2d.Interpolator;
import com.badlogic.gdx.scenes.scene2d.actions.FadeOut;
import com.badlogic.gdx.scenes.scene2d.actions.FadeTo;
import com.badlogic.gdx.scenes.scene2d.actions.Forever;
import com.badlogic.gdx.scenes.scene2d.actions.JumpBy;
import com.badlogic.gdx.scenes.scene2d.actions.MoveBy;
import com.badlogic.gdx.scenes.scene2d.actions.Parallel;
import com.badlogic.gdx.scenes.scene2d.actions.Repeat;
import com.badlogic.gdx.scenes.scene2d.actions.RotateBy;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleTo;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;
import com.badlogic.gdx.scenes.scene2d.actions.TintTo;
import com.badlogic.gdx.scenes.scene2d.interpolators.EaseElasticInInterpolator;
import com.badlogic.gdx.scenes.scene2d.interpolators.EaseElasticOutInterpolator;
import com.badlogic.gdx.scenes.scene2d.interpolators.EaseExponentialInInterpolator;
import com.badlogic.gdx.scenes.scene2d.interpolators.EaseExponentialOutInterpolator;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class AnimationManager {
    private static final int LEFT_TO_RIGHT = 1;
    private static final int RIGHT_TO_LEFT = -1;
    private static final String TAG = "AnimationManager";
    private static AnimationManager instance;
    private Map<String, Action> normalAnimations = new HashMap(60);
    private Map<String, Action> mirroredAnimations = new HashMap(60);

    public static void initialize() {
        instance = new AnimationManager();
        instance.loadAnimations();
    }

    public static AnimationManager getInstance() {
        if (instance == null) {
            throw new RuntimeException("AnimationManager not initialized");
        }
        return instance;
    }

    public Action getAnimation(String name, boolean mirrored) {
        Map<String, Action> animations = mirrored ? this.mirroredAnimations : this.normalAnimations;
        return animations.get(name).copy();
    }

    private AnimationManager() {
    }

    private void loadAnimations() {
        FileHandle handle = Gdx.files.internal("animations.json");
        ObjectMapper m = new ObjectMapper();
        try {
            JsonNode rootNode = (JsonNode) m.readValue(handle.read(), JsonNode.class);
            Iterator<Map.Entry<String, JsonNode>> moves = rootNode.getFields();
            while (moves.hasNext()) {
                Map.Entry<String, JsonNode> move = moves.next();
                String moveName = move.getKey();
                try {
                    this.normalAnimations.put(moveName, getAction(move.getValue(), 1));
                    this.mirroredAnimations.put(moveName, getAction(move.getValue(), -1));
                } catch (Exception e) {
                    Gdx.app.log(TAG, "Animation " + moveName + " failed: " + e.getMessage());
                }
            }
        } catch (Exception e2) {
            throw new RuntimeException("Could not open animations file");
        }
    }

    private static Action getAction(JsonNode node, int dir) {
        String name = node.path("action-name").getTextValue();
        float arx = ResolutionManager.getHorizontalAR();
        float ary = ResolutionManager.getVerticalAR();
        if ("CCRepeatForever".equals(name)) {
            Action result = Forever.$(getAction(node.path("action"), dir));
            return result;
        }
        if ("CCRepeat".equals(name)) {
            Action result2 = Repeat.$(getAction(node.path("action"), dir), node.path("times").getIntValue());
            return result2;
        }
        if ("CCSequence".equals(name) || "CCSpawn".equals(name)) {
            JsonNode actionsNode = node.path("actions");
            List<Action> actions = new ArrayList<>(5);
            Map<String, JsonNode> actionIds = null;
            for (JsonNode actionNode : actionsNode) {
                if ("CCDelayTime".equals(actionNode.path("action-name").getTextValue())) {
                    actions.add(MoveBy.$(0.0f, 0.0f, actionNode.path("duration").getNumberValue().floatValue()));
                } else if ("CCReverse".equals(actionNode.path("action-name").getTextValue())) {
                    String actionToReverseId = actionNode.path("action-to-reverse-id").getTextValue();
                    if (actionIds == null || actionIds.get(actionToReverseId) == null) {
                        Gdx.app.error(TAG, "Tried to reverse an undefined action");
                        throw new RuntimeException("Move id not found: " + actionToReverseId);
                    }
                    actions.add(getReversedAction(actionIds.get(actionToReverseId), dir));
                } else {
                    Action action = getAction(actionNode, dir);
                    if (action != null) {
                        actions.add(action);
                    }
                }
                if (actionNode.path("action-id").getTextValue() != null) {
                    if (actionIds == null) {
                        actionIds = new HashMap<>(3);
                    }
                    String id = actionNode.path("action-id").getTextValue();
                    actionIds.put(id, actionNode);
                }
            }
            if ("CCSequence".equals(name)) {
                Action result3 = Sequence.$((Action[]) actions.toArray(new Action[actions.size()]));
                return result3;
            }
            Action result4 = Parallel.$((Action[]) actions.toArray(new Action[actions.size()]));
            return result4;
        }
        if ("CCMoveBy".equals(name)) {
            Action result5 = MoveBy.$(node.path("position-x").getNumberValue().floatValue() * arx * dir, node.path("position-y").getNumberValue().floatValue() * ary, node.path("duration").getNumberValue().floatValue());
            return result5;
        }
        if ("CCRotateBy".equals(name)) {
            Action result6 = RotateBy.$(node.path("angle").getNumberValue().floatValue() * dir, node.path("duration").getNumberValue().floatValue());
            return result6;
        }
        if ("CCJumpBy".equals(name)) {
            Action result7 = JumpBy.$(node.path("position-x").getNumberValue().floatValue() * arx * dir, node.path("position-y").getNumberValue().floatValue() * ary, node.path("height").getNumberValue().floatValue() * ary, node.path("jumps").getIntValue(), node.path("duration").getNumberValue().floatValue());
            return result7;
        }
        if ("CCOrbitCamera".equals(name)) {
            Action result8 = Flip.$(node.path("deltaAngleZ").getIntValue() != 180, node.path("duration").getNumberValue().floatValue());
            return result8;
        }
        if ("CCFadeTo".equals(name)) {
            Action result9 = FadeTo.$(node.path("opacity").getNumberValue().floatValue(), node.path("duration").getNumberValue().floatValue());
            return result9;
        }
        if ("CCFadeOut".equals(name)) {
            Action result10 = FadeOut.$(node.path("duration").getNumberValue().floatValue());
            return result10;
        }
        if ("CCFadeIn".equals(name)) {
            Action result11 = FadeOut.$(node.path("duration").getNumberValue().floatValue());
            return result11;
        }
        if ("CCTintTo".equals(name)) {
            Action result12 = TintTo.$(node.path("red").getNumberValue().floatValue(), node.path("green").getNumberValue().floatValue(), node.path("blue").getNumberValue().floatValue(), node.path("duration").getNumberValue().floatValue());
            return result12;
        }
        if ("CCScaleTo".equals(name)) {
            Action result13 = ScaleTo.$(node.path("sx").getNumberValue().floatValue(), node.path("sy").getNumberValue().floatValue(), node.path("duration").getNumberValue().floatValue());
            return result13;
        }
        if ("CCShaky3D".equals(name)) {
            Action result14 = Shaky3D.$(node.path("grid-size-x").getIntValue(), node.path("grid-size-y").getIntValue(), node.path("range").getIntValue(), node.path("shakeZ").getIntValue() != 0, node.path("duration").getNumberValue().floatValue());
            return result14;
        }
        if ("CCEaseExponentialIn".equals(name)) {
            Action result15 = getAction(node.path("action-to-ease"), dir);
            ((AnimationAction) result15).setInterpolator(new EaseExponentialInInterpolator());
            return result15;
        }
        if ("CCEaseExponentialOut".equals(name)) {
            Action result16 = getAction(node.path("action-to-ease"), dir);
            ((AnimationAction) result16).setInterpolator(new EaseExponentialOutInterpolator());
            return result16;
        }
        if ("CCEaseElasticIn".equals(name)) {
            Interpolator i = EaseElasticInInterpolator.$(node.path("period").getNumberValue().floatValue());
            Action result17 = getAction(node.path("action-to-ease"), dir);
            ((AnimationAction) result17).setInterpolator(i);
            return result17;
        }
        if ("CCEaseElasticOut".equals(name)) {
            Interpolator i2 = EaseElasticOutInterpolator.$(node.path("period").getNumberValue().floatValue());
            Action result18 = getAction(node.path("action-to-ease"), dir);
            ((AnimationAction) result18).setInterpolator(i2);
            return result18;
        }
        if ("CCCallFunc".equals(name) || "CCCallFuncO".equals(name)) {
            Action result19 = MogaAction.newInstance(node.path("selector").getTextValue());
            return result19;
        }
        throw new RuntimeException("Move not supported: " + name);
    }

    private static Action getReversedAction(JsonNode node, int dir) {
        String name = node.path("action-name").getTextValue();
        float arx = ResolutionManager.getHorizontalAR();
        float ary = ResolutionManager.getVerticalAR();
        if ("CCMoveBy".equals(name)) {
            Action result = MoveBy.$(node.path("position-x").getNumberValue().floatValue() * arx * dir * (-1.0f), node.path("position-y").getNumberValue().floatValue() * ary * (-1.0f), node.path("duration").getNumberValue().floatValue());
            return result;
        }
        if (!"CCRotateBy".equals(name)) {
            return null;
        }
        Action result2 = RotateBy.$(node.path("angle").getNumberValue().floatValue() * dir * (-1.0f), node.path("duration").getNumberValue().floatValue());
        return result2;
    }
}
