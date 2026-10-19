package com.gaiaonline.monstergalaxy.model.map;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MusicManager;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.assets.Asset;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;
import java.io.Serializable;
import java.util.List;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Island implements Serializable {
    public static final int FIRST_ISLAND_ID = 2;
    private static final String MAP_BG_DIR = "island/maps/";
    private static final long serialVersionUID = 8167078574051618124L;
    private String ambienceAudio;
    private int id;
    private String mapAssetName;
    private int maxLevel;
    private int maxNumberOfMogas;
    private String name;
    private Island nextIsland;
    private List<Node> nodes;
    private int numberOfRequiredMogasToUnlock;
    private Vector2 position;
    private Island previousIsland;
    private int[] questIds;
    private int stars;
    private String stingAudio;

    public Vector2 getPosition() {
        return this.position;
    }

    public void setPosition(float x, float y) {
        this.position = new Vector2(x, y);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileHandle getAvailableAmbienceAudio() {
        if (this.ambienceAudio == null) {
            return null;
        }
        return MusicManager.getAvailableIslandMusic(this.ambienceAudio);
    }

    public String getAmbienceAudio() {
        return this.ambienceAudio;
    }

    public void setAmbienceAudio(String ambienceAudio) {
        this.ambienceAudio = ambienceAudio;
    }

    public String getStingAudio() {
        return this.stingAudio;
    }

    public void setStingAudio(String stingAudio) {
        this.stingAudio = stingAudio;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Node> getNodes() {
        return this.nodes;
    }

    public Node getFirstNode() {
        if (this.nodes.size() <= 0) {
            return null;
        }
        Node node = this.nodes.get(0);
        return node;
    }

    public Node getBossNode() {
        Node bossNode = getFirstNode();
        for (Node node : this.nodes) {
            if (bossNode.getId() < node.getId()) {
                bossNode = node;
            }
        }
        return bossNode;
    }

    public int getStars() {
        return this.stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getMapAssetName() {
        return this.mapAssetName;
    }

    public void setMapAssetName(String mapAssetName) {
        this.mapAssetName = mapAssetName;
    }

    public Asset getMapAsset() {
        String suffix = ResolutionManager.RESOLUTION_SUFFIX;
        Integer cropWidth = null;
        if (".800".equals(ResolutionManager.RESOLUTION_SUFFIX)) {
            suffix = ".854";
            if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2) {
                cropWidth = 800;
            }
        }
        return new Asset(MAP_BG_DIR, String.valueOf(this.mapAssetName.substring(0, this.mapAssetName.lastIndexOf("."))) + suffix, "jpg", cropWidth);
    }

    public String getIslandAssetName(boolean locked) {
        return "world." + getName().toLowerCase() + "." + (locked ? "locked" : "unlocked");
    }

    public int getMaxNumberOfMogas() {
        return (Game.getTrainer() == null || Game.getTrainer().getMogas() == null || Game.getTrainer().getMogas().size() <= 0 || Game.getStorage().getMogaCaptureIsland(Game.getTrainer().getMogas().get(0).getType().getId()) != getId()) ? this.maxNumberOfMogas : this.maxNumberOfMogas - 1;
    }

    public void setMaxNumberOfMogas(int maxNumberOfMogas) {
        this.maxNumberOfMogas = maxNumberOfMogas;
    }

    public int[] getQuestIds() {
        return this.questIds;
    }

    public void setQuestIds(int[] questIds) {
        this.questIds = questIds;
    }

    public int getQuestCount() {
        if (this.questIds == null) {
            return 0;
        }
        int questCount = this.questIds.length;
        return questCount;
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int getMaxNumberOfStars() {
        return this.nodes.size() * 3;
    }

    public void setNumberOfRequiredMogasToUnlock(int numberOfRequiredMogasToUnlock) {
        this.numberOfRequiredMogasToUnlock = numberOfRequiredMogasToUnlock;
    }

    public int getNumberOfRequiredMogasToUnlock() {
        return this.numberOfRequiredMogasToUnlock;
    }

    public Island getNextIsland() {
        if (this.nextIsland == null) {
            this.nextIsland = Game.getStorage().getIsland(getId() + 2);
        }
        return this.nextIsland;
    }

    public Island getPreviousIsland() {
        int previousIslandId = getId() - 2;
        if (this.previousIsland == null && previousIslandId > 0) {
            this.previousIsland = Game.getStorage().getIsland(previousIslandId);
        }
        return this.previousIsland;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Island)) {
            return false;
        }
        Island other = (Island) obj;
        if (this.id != other.getId()) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.id;
    }
}
