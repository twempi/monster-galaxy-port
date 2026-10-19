package com.gaiaonline.monstergalaxy.battle.anim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.JumpByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.actions.TintToAction;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

/* JADX INFO: loaded from: classes.dex */
public class AnimationManager {
    private static final int LEFT_TO_RIGHT = 1;
    private static final int RIGHT_TO_LEFT = -1;
    private static final String TAG = "AnimationManager";
    private static AnimationManager instance;
    private JsonNode jsonMoves;
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
        return getAction(this.jsonMoves.get(name), mirrored ? -1 : 1);
    }

    private AnimationManager() {
    }

    private void loadAnimations() {
        FileHandle handle = Gdx.files.internal("animations.json");
        ObjectMapper m = new ObjectMapper();
        try {
            JsonNode rootNode = (JsonNode) m.readValue(handle.read(), JsonNode.class);
            this.jsonMoves = rootNode;
        } catch (Exception e) {
            throw new RuntimeException("Could not open animations file");
        }
    }

    private static Action getAction(JsonNode node, int dir) {
        String name = node.path("action-name").getTextValue();
        float arx = ResolutionManager.getHorizontalAR();
        float ary = ResolutionManager.getVerticalAR();
        if ("CCRepeatForever".equals(name)) {
            Action result = new RepeatAction();
            ((RepeatAction) result).setCount(-1);
            ((RepeatAction) result).setAction(getAction(node.path("action"), dir));
            return result;
        }
        if ("CCRepeat".equals(name)) {
            Action result2 = new RepeatAction();
            ((RepeatAction) result2).setCount(node.path("times").getIntValue());
            ((RepeatAction) result2).setAction(getAction(node.path("action"), dir));
            return result2;
        }
        if ("CCSequence".equals(name) || "CCSpawn".equals(name)) {
            JsonNode actionsNode = node.path("actions");
            List<Action> actions = new ArrayList<>(5);
            Map<String, JsonNode> actionIds = null;
            for (JsonNode actionNode : actionsNode) {
                if ("CCDelayTime".equals(actionNode.path("action-name").getTextValue())) {
                    MoveByAction action = new MoveByAction();
                    action.setAmount(0.0f, 0.0f);
                    action.setDuration(actionNode.path("duration").getNumberValue().floatValue());
                    actions.add(action);
                } else if ("CCReverse".equals(actionNode.path("action-name").getTextValue())) {
                    String actionToReverseId = actionNode.path("action-to-reverse-id").getTextValue();
                    if (actionIds == null || actionIds.get(actionToReverseId) == null) {
                        Gdx.app.error(TAG, "Tried to reverse an undefined action");
                        throw new RuntimeException("Move id not found: " + actionToReverseId);
                    }
                    actions.add(getReversedAction(actionIds.get(actionToReverseId), dir));
                } else {
                    Action action2 = getAction(actionNode, dir);
                    if (action2 != null) {
                        actions.add(action2);
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
                Action result3 = new SequenceAction();
                for (Action action3 : actions) {
                    ((SequenceAction) result3).addAction(action3);
                }
                return result3;
            }
            Action result4 = new ParallelAction();
            for (Action action4 : actions) {
                ((ParallelAction) result4).addAction(action4);
            }
            return result4;
        }
        if ("CCMoveBy".equals(name)) {
            MoveByAction moveBy = new MoveByAction();
            moveBy.setAmountX(node.path("position-x").getNumberValue().floatValue() * arx * dir);
            moveBy.setAmountY(node.path("position-y").getNumberValue().floatValue() * ary);
            moveBy.setDuration(node.path("duration").getNumberValue().floatValue());
            return moveBy;
        }
        if ("CCRotateBy".equals(name)) {
            RotateByAction rotateBy = new RotateByAction();
            rotateBy.setAmount(node.path("angle").getNumberValue().floatValue() * dir);
            rotateBy.setDuration(node.path("duration").getNumberValue().floatValue());
            return rotateBy;
        }
        if ("CCJumpBy".equals(name)) {
            JumpByAction jumpBy = new JumpByAction();
            jumpBy.setX((int) (node.path("position-x").getNumberValue().floatValue() * arx * dir));
            jumpBy.setY((int) (node.path("position-y").getNumberValue().floatValue() * ary));
            jumpBy.setHeight((int) (node.path("height").getNumberValue().floatValue() * ary));
            jumpBy.setJumps(node.path("jumps").getIntValue());
            jumpBy.setDuration(node.path("duration").getNumberValue().floatValue());
            return jumpBy;
        }
        if ("CCOrbitCamera".equals(name)) {
            Flip flip = new Flip();
            flip.setToLeft(node.path("deltaAngleZ").getIntValue() != 180);
            flip.setDuration(node.path("duration").getNumberValue().floatValue());
            return flip;
        }
        if ("CCFadeTo".equals(name)) {
            AlphaAction alpha = new AlphaAction();
            alpha.setAlpha(node.path("opacity").getNumberValue().floatValue() / 255.0f);
            alpha.setDuration(node.path("duration").getNumberValue().floatValue());
            return alpha;
        }
        if ("CCFadeOut".equals(name)) {
            AlphaAction alpha2 = new AlphaAction();
            alpha2.setAlpha(0.0f);
            alpha2.setDuration(node.path("duration").getNumberValue().floatValue());
            return alpha2;
        }
        if ("CCFadeIn".equals(name)) {
            AlphaAction alpha3 = new AlphaAction();
            alpha3.setAlpha(1.0f);
            alpha3.setDuration(node.path("duration").getNumberValue().floatValue());
            return alpha3;
        }
        if ("CCTintTo".equals(name)) {
            TintToAction tintTo = new TintToAction();
            tintTo.setTint(node.path("red").getNumberValue().floatValue(), node.path("green").getNumberValue().floatValue(), node.path("blue").getNumberValue().floatValue(), null);
            tintTo.setDuration(node.path("duration").getNumberValue().floatValue());
            return tintTo;
        }
        if ("CCScaleTo".equals(name)) {
            ScaleToAction scaleTo = new ScaleToAction();
            scaleTo.setScale(node.path("sx").getNumberValue().floatValue(), node.path("sy").getNumberValue().floatValue());
            scaleTo.setDuration(node.path("duration").getNumberValue().floatValue());
            return scaleTo;
        }
        if ("CCShaky3D".equals(name)) {
            Shaky3D shaky = new Shaky3D();
            shaky.setGridSizeX(node.path("grid-size-x").getIntValue());
            shaky.setGridSizeY(node.path("grid-size-y").getIntValue());
            shaky.setRange(node.path("range").getIntValue());
            shaky.setShakeZ(node.path("shakeZ").getIntValue() != 0);
            shaky.setDuration(node.path("duration").getNumberValue().floatValue());
            return shaky;
        }
        if ("CCEaseExponentialIn".equals(name)) {
            Action result5 = getAction(node.path("action-to-ease"), dir);
            ((TemporalAction) result5).setInterpolation(Interpolation.exp10In);
            return result5;
        }
        if ("CCEaseExponentialOut".equals(name)) {
            Action result6 = getAction(node.path("action-to-ease"), dir);
            ((TemporalAction) result6).setInterpolation(Interpolation.exp10Out);
            return result6;
        }
        if ("CCEaseElasticIn".equals(name)) {
            Interpolation i = Interpolation.elasticIn;
            Action result7 = getAction(node.path("action-to-ease"), dir);
            ((TemporalAction) result7).setInterpolation(i);
            return result7;
        }
        if ("CCEaseElasticOut".equals(name)) {
            Interpolation i2 = Interpolation.elasticOut;
            Action result8 = getAction(node.path("action-to-ease"), dir);
            ((TemporalAction) result8).setInterpolation(i2);
            return result8;
        }
        if ("CCCallFunc".equals(name) || "CCCallFuncO".equals(name)) {
            return MogaAction.newInstance(node.path("selector").getTextValue());
        }
        throw new RuntimeException("Move not supported: " + name);
    }

    private static Action getReversedAction(JsonNode node, int dir) {
        String name = node.path("action-name").getTextValue();
        float arx = ResolutionManager.getHorizontalAR();
        float ary = ResolutionManager.getVerticalAR();
        if ("CCMoveBy".equals(name)) {
            MoveByAction moveBy = new MoveByAction();
            moveBy.setAmountX(node.path("position-x").getNumberValue().floatValue() * arx * dir * (-1.0f));
            moveBy.setAmountY(node.path("position-y").getNumberValue().floatValue() * ary * (-1.0f));
            moveBy.setDuration(node.path("duration").getNumberValue().floatValue());
            return moveBy;
        }
        if (!"CCRotateBy".equals(name)) {
            return null;
        }
        RotateByAction rotate = new RotateByAction();
        rotate.setAmount(node.path("angle").getNumberValue().floatValue() * dir * (-1.0f));
        rotate.setDuration(node.path("duration").getNumberValue().floatValue());
        return rotate;
    }
}
