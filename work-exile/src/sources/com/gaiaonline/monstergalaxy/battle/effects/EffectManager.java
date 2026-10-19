package com.gaiaonline.monstergalaxy.battle.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class EffectManager {
    private static EffectManager instance;
    private Map<String, Effect> effects = new HashMap();

    private EffectManager() {
    }

    public static EffectManager getInstance() {
        if (instance == null) {
            instance = new EffectManager();
            instance.loadEffects();
        }
        return instance;
    }

    public Effect getEffect(EffectType effectType) {
        return this.effects.get(effectType.getEffectName());
    }

    public Effect getEffect(String effectName) {
        return this.effects.get(effectName);
    }

    public Collection<Effect> getEffects() {
        return this.effects.values();
    }

    private void addCompositeEffects() {
        this.effects.put(EffectType.EXPLOSION.getEffectName(), new CompositeEffect(EffectType.EXPLOSION.getEffectName(), getEffect(EffectType.STARS), getEffect(EffectType.RING)));
        this.effects.put(EffectType.BLUE_COFFEE.getEffectName(), new BlueCoffeeEffect(EffectType.BLUE_COFFEE.getEffectName()));
        this.effects.put(EffectType.CHARGE_A.getEffectName(), new CompositeEffect(EffectType.CHARGE_A.getEffectName(), getEffect(EffectType.CHARGE_A).getCopy(), getEffect(EffectType.CHARGE_A).getFlippedHorizontalCopy()));
        this.effects.put(EffectType.CHARGE_B.getEffectName(), new CompositeEffect(EffectType.CHARGE_B.getEffectName(), getEffect(EffectType.CHARGE_B).getCopy(), getEffect(EffectType.CHARGE_B).getFlippedHorizontalCopy()));
    }

    private void loadEffects() {
        loadEffects("effects.json");
    }

    public void loadEffects(String path) {
        FileHandle handle = Gdx.files.internal(path);
        ObjectMapper m = new ObjectMapper();
        try {
            JsonNode rootNode = (JsonNode) m.readValue(handle.read(), JsonNode.class);
            Iterator<JsonNode> nodesIterator = rootNode.getElements();
            float scaleFactor = ResolutionManager.getScaleFactor();
            while (nodesIterator.hasNext()) {
                JsonNode node = nodesIterator.next();
                EffectData data = new EffectData();
                data.name = node.get("name").getTextValue();
                data.maxParticleCount = node.get("maxParticles").getIntValue();
                data.duration = (float) node.get("duration").getDoubleValue();
                data.angle = (float) node.get("angle").getDoubleValue();
                data.angleVar = (float) node.get("angleVariance").getDoubleValue();
                data.assetPath = node.get("assetPath").getTextValue();
                data.speed = getFloatFromNode(node, "speed");
                data.life = (float) node.get("particleLifespan").getDoubleValue();
                data.lifeVar = (float) node.get("particleLifespanVariance").getDoubleValue();
                data.sourcePositionVarx = getFloatFromNode(node, "sourcePositionVariancex") * scaleFactor;
                data.sourcePositionVary = getFloatFromNode(node, "sourcePositionVariancey") * scaleFactor;
                data.startParticleSize = getFloatFromNode(node, "startParticleSize") * scaleFactor;
                data.finishParticleSize = getFloatFromNode(node, "finishParticleSize") * scaleFactor;
                data.gravityx = getFloatFromNode(node, "gravityx") * scaleFactor;
                data.gravityy = getFloatFromNode(node, "gravityy") * scaleFactor;
                data.startColor = new float[4];
                data.startColor[0] = getFloatFromNode(node, "startColorRed");
                data.startColor[1] = getFloatFromNode(node, "startColorGreen");
                data.startColor[2] = getFloatFromNode(node, "startColorBlue");
                data.startColor[3] = getFloatFromNode(node, "startColorAlpha");
                data.finishColor = new float[4];
                data.finishColor[0] = getFloatFromNode(node, "finishColorRed");
                data.finishColor[1] = getFloatFromNode(node, "finishColorGreen");
                data.finishColor[2] = getFloatFromNode(node, "finishColorBlue");
                data.finishColor[3] = getFloatFromNode(node, "finishColorAlpha");
                data.startColorVar = new float[4];
                data.startColorVar[0] = getFloatFromNode(node, "startColorVarianceRed");
                data.startColorVar[1] = getFloatFromNode(node, "startColorVarianceGreen");
                data.startColorVar[2] = getFloatFromNode(node, "startColorVarianceBlue");
                data.startColorVar[3] = getFloatFromNode(node, "startColorVarianceAlpha");
                data.finishColorVar = new float[4];
                data.finishColorVar[0] = getFloatFromNode(node, "finishColorVarianceRed");
                data.finishColorVar[1] = getFloatFromNode(node, "finishColorVarianceGreen");
                data.finishColorVar[2] = getFloatFromNode(node, "finishColorVarianceBlue");
                data.finishColorVar[3] = getFloatFromNode(node, "finishColorVarianceAlpha");
                this.effects.put(data.name, new Effect(data));
            }
            addCompositeEffects();
        } catch (Exception e) {
            throw new RuntimeException("Could not open effects file");
        }
    }

    private float getFloatFromNode(JsonNode node, String name) {
        return (float) node.get(name).getDoubleValue();
    }

    public void removeParticles() {
        for (Effect effect : getEffects()) {
            effect.particleEffect.dispose();
        }
    }
}
