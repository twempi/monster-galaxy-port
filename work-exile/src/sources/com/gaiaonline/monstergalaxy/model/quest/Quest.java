package com.gaiaonline.monstergalaxy.model.quest;

import com.badlogic.gdx.files.FileHandle;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.app.MusicManager;
import com.gaiaonline.monstergalaxy.model.map.Island;
import com.gaiaonline.monstergalaxy.model.map.Node;
import com.gaiaonline.monstergalaxy.model.moga.MogaType;
import java.io.Serializable;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Quest implements Serializable {
    private static final long serialVersionUID = -7911967722110908568L;
    private Node beatNode;
    private int beatNodeId;
    private int blockedNodeId;
    private QuestDialog completeDialog;
    private int contenderMogaType;
    private QuestDialog descriptionDialog;
    private int grantNodeId;
    private int grantQuestId;
    private int id;
    private Island islandToWinStars;
    private String music;
    private String name;
    private String npcAsset;
    private String npcName;
    private int numberOfContenders;
    private int numberOfStarsToWinInIsland;
    private Type questType;
    private int restrictionIslandId;
    private String restrictionNodeIds;
    private int restrictionQuestId;
    private RewardItem rewardItem;
    private QuestDialog startDialog;

    public enum Type {
        CAPTURE,
        BEAT_NODE,
        KILL,
        STARS;

        /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
        public static Type[] valuesCustom() {
            Type[] typeArrValuesCustom = values();
            int length = typeArrValuesCustom.length;
            Type[] typeArr = new Type[length];
            System.arraycopy(typeArrValuesCustom, 0, typeArr, 0, length);
            return typeArr;
        }
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Type getType() {
        return this.questType;
    }

    public void setType(Type questType) {
        this.questType = questType;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileHandle getAvailableMusic() {
        if (this.music == null) {
            return null;
        }
        return MusicManager.getAvailableQuestMusic(this.music);
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getNpcName() {
        return this.npcName;
    }

    public void setNpcName(String npcName) {
        this.npcName = npcName;
    }

    public String getNpcAsset() {
        return this.npcAsset;
    }

    public void setNpcAsset(String npcAsset) {
        this.npcAsset = npcAsset.substring(0, npcAsset.lastIndexOf(".")).replaceAll("_", ".");
    }

    public QuestDialog getStartDialog() {
        return this.startDialog;
    }

    public void setStartDialog(QuestDialog startDialog) {
        this.startDialog = startDialog;
    }

    public QuestDialog getDescriptionDialog() {
        return this.descriptionDialog;
    }

    public void setDescriptionDialog(QuestDialog descriptionDialog) {
        this.descriptionDialog = descriptionDialog;
    }

    public QuestDialog getCompleteDialog() {
        return this.completeDialog;
    }

    public void setCompleteDialog(QuestDialog completeDialog) {
        this.completeDialog = completeDialog;
    }

    public int getContenderMogaTypeId() {
        return this.contenderMogaType;
    }

    public void setContenderMogaTypeId(int contenderMogaType) {
        this.contenderMogaType = contenderMogaType;
    }

    public int getNumberOfContenders() {
        return this.numberOfContenders;
    }

    public void setNumberOfContenders(int numberOfContenders) {
        this.numberOfContenders = numberOfContenders;
    }

    public int getBeatNodeId() {
        return this.beatNodeId;
    }

    public void setBeatNodeId(int beatNodeId) {
        this.beatNodeId = beatNodeId;
    }

    public Node getBeatNode() {
        if (this.beatNode == null && this.questType == Type.BEAT_NODE) {
            this.beatNode = Game.getStorage().getNode(this.beatNodeId);
        }
        return this.beatNode;
    }

    public RewardItem getRewardItem() {
        return this.rewardItem;
    }

    public void setRewardItem(RewardItem rewardItem) {
        this.rewardItem = rewardItem;
    }

    public int getGrantNodeId() {
        return this.grantNodeId;
    }

    public void setGrantNodeId(int grantNodeId) {
        this.grantNodeId = grantNodeId;
    }

    public int getGrantQuestId() {
        return this.grantQuestId;
    }

    public void setGrantQuestId(int grantQuestId) {
        this.grantQuestId = grantQuestId;
    }

    public String getRestrictionNodeIds() {
        return this.restrictionNodeIds;
    }

    public void setRestrictionNodeIds(String restrictionNodeIds) {
        this.restrictionNodeIds = restrictionNodeIds;
    }

    public int getRestrictionQuestId() {
        return this.restrictionQuestId;
    }

    public void setRestrictionQuestId(int restrictionQuestId) {
        this.restrictionQuestId = restrictionQuestId;
    }

    public int getRestrictionIslandId() {
        return this.restrictionIslandId;
    }

    public void setRestrictionIslandId(int restrictionIslandId) {
        this.restrictionIslandId = restrictionIslandId;
    }

    public Island getIslandToWinStars() {
        return this.islandToWinStars;
    }

    public void setIslandToWinStars(Island islandToWinStars) {
        this.islandToWinStars = islandToWinStars;
    }

    public int getNumberOfStarsToWinInIsland() {
        return this.numberOfStarsToWinInIsland;
    }

    public void setNumberOfStarsToWinInIsland(int numberOfStarsToWinInIsland) {
        this.numberOfStarsToWinInIsland = numberOfStarsToWinInIsland;
    }

    public int getBlockedNodeId() {
        return this.blockedNodeId;
    }

    public void setBlockedNodeId(int blockedNodeId) {
        this.blockedNodeId = blockedNodeId;
    }

    public boolean isGrantedOnNode(Node node) {
        return this.grantNodeId == node.getId();
    }

    public String getCompletedText() {
        String completionText;
        if (getType() == Type.BEAT_NODE) {
            completionText = "completed " + getBeatNode().getName();
        } else if (getType() == Type.STARS) {
            completionText = "won " + getNumberOfStarsToWinInIsland() + " on " + getIslandToWinStars().getName();
        } else {
            MogaType mogaType = Game.getStorage().getMogaType(this.contenderMogaType);
            String completionText2 = getType() == Type.CAPTURE ? "captured " : "killed ";
            if (this.numberOfContenders > 1) {
                completionText = String.valueOf(completionText2) + this.numberOfContenders + " " + mogaType.getName();
            } else {
                completionText = String.valueOf(completionText2) + mogaType.getName();
            }
        }
        return "Good job tamer! You've " + completionText + "!";
    }

    public boolean equals(Object object) {
        if (!(object instanceof Quest)) {
            return false;
        }
        Quest other = (Quest) object;
        if (getId() != other.getId()) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return getId();
    }
}
