package com.gaiaonline.monstergalaxy.model.map;

import com.badlogic.gdx.math.Vector2;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.ResolutionManager;
import com.gaiaonline.monstergalaxy.assets.Asset;
import com.gaiaonline.monstergalaxy.model.quest.QuestProgress;
import com.gaiaonline.monstergalaxy.model.quest.RewardItem;
import com.gaiaonline.monstergalaxy.model.trainer.Team;
import com.gaiaonline.monstergalaxy.platform.PlatformHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Node implements Serializable {
    private static final String NODE_BG_DIR = "island/nodeBackgrounds/";
    public static final int STARTING_NODE_ID = 2;
    private static final long serialVersionUID = 6900601900669153345L;
    private int[] adjacentNodeIds;
    private String backgroundAsset;
    private String description;
    private int dropId;
    private float dropProbability;
    private List<Encounter> encounters;
    private int energyCost;
    private int id;
    private Island island;
    private String name;
    private Vector2 position;
    private Team team;
    private String thumbnailAsset;

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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int[] getAdjacentNodes() {
        return this.adjacentNodeIds;
    }

    public void setAdjacentNodeIds(int[] adjacentNodes) {
        this.adjacentNodeIds = adjacentNodes;
    }

    public int getEnergyCost() {
        return this.energyCost;
    }

    public void setEnergyCost(int energyCost) {
        this.energyCost = energyCost;
    }

    public String getThumbnailAsset() {
        return this.thumbnailAsset;
    }

    public void setThumbnailAsset(String thumbnailAsset) {
        this.thumbnailAsset = thumbnailAsset.replaceAll("_", ".");
        this.thumbnailAsset = this.thumbnailAsset.substring(0, this.thumbnailAsset.lastIndexOf("."));
    }

    public Asset getBackgroundAsset() {
        String suffix = ResolutionManager.RESOLUTION_SUFFIX;
        Integer cropWidth = null;
        if (".800".equals(ResolutionManager.RESOLUTION_SUFFIX)) {
            suffix = ".854";
            if (Game.getPlatformHelper().getSkin() == PlatformHelper.MGSkin.SK2) {
                cropWidth = 800;
            }
        }
        return new Asset(NODE_BG_DIR, String.valueOf(this.backgroundAsset.substring(0, this.backgroundAsset.lastIndexOf("."))) + suffix, "jpg", cropWidth);
    }

    public void setBackgroundAsset(String backgroundAsset) {
        this.backgroundAsset = backgroundAsset;
    }

    public int getDropId() {
        return this.dropId;
    }

    public void setDropId(int dropId) {
        this.dropId = dropId;
    }

    public float getDropProbability() {
        return this.dropProbability;
    }

    public void setDropProbability(float dropProbability) {
        this.dropProbability = dropProbability;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Island getIsland() {
        return this.island;
    }

    public void setIsland(Island island) {
        this.island = island;
    }

    public boolean isFirstNode() {
        return getIsland().getFirstNode().equals(this);
    }

    public RewardItem getRewardItem() {
        int random = new Random().nextInt() % 100;
        if (random >= this.dropProbability * 100.0f) {
            return null;
        }
        RewardItem reward = new RewardItem(this.dropId, 1);
        return reward;
    }

    public Team getTeam() {
        return this.team;
    }

    public void createTeam() {
        this.team = new Team();
        this.team.getMogas().addAll(getEncounter().getMogas());
    }

    public Encounter getEncounter() {
        Encounter encounter = null;
        List<Integer> activeQuests = new ArrayList<>();
        for (QuestProgress progress : Game.getTrainer().getQuestsProgressions()) {
            activeQuests.add(Integer.valueOf(progress.getQuest().getId()));
        }
        if (this.encounters == null) {
            this.encounters = Game.getStorage().getEncounters(this.id);
        }
        for (Encounter e : this.encounters) {
            if (activeQuests.contains(Integer.valueOf(e.getQuestId()))) {
                encounter = e;
                break;
            }
        }
        if (encounter == null) {
            for (Encounter e2 : this.encounters) {
                if (e2.getQuestId() == 0) {
                    return e2;
                }
            }
            return encounter;
        }
        return encounter;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Node)) {
            return false;
        }
        Node other = (Node) obj;
        if (this.id != other.getId()) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.id;
    }
}
