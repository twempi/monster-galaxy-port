package com.gaiaonline.monstergalaxy.model.map;

import com.gaiaonline.monstergalaxy.model.moga.Moga;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class Encounter implements Serializable {
    private static final long serialVersionUID = 8667741351879595903L;
    private List<EncounterAppearance> appearances;
    private String capture;
    private Category category = Category.NORMAL;
    private int id;
    private String musicAsset;
    private String musicType;
    private int nodeId;
    private int questId;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNodeId() {
        return this.nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getQuestId() {
        return this.questId;
    }

    public void setQuestId(int questId) {
        this.questId = questId;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setCapture(String capture) {
        this.capture = capture;
    }

    public boolean isCaptureEnabled() {
        return "enabled".equals(this.capture);
    }

    public String getMusicAsset() {
        return this.musicAsset;
    }

    public String getMusicType() {
        return this.musicType;
    }

    public void setMusicAsset(String musicAsset) {
        this.musicAsset = musicAsset;
    }

    public void setMusicType(String musicType) {
        this.musicType = musicType;
    }

    public void setAppearances(List<EncounterAppearance> appearances) {
        this.appearances = appearances;
    }

    public List<EncounterAppearance> getAppearances() {
        return this.appearances;
    }

    public List<Moga> getMogas() {
        List<Moga> mogas = new ArrayList<>();
        if (this.category == Category.NORMAL) {
            double randomValue = new Random().nextDouble();
            float sum = 0.0f;
            for (EncounterAppearance appearance : this.appearances) {
                sum += appearance.getProbability();
                if (randomValue <= sum) {
                    mogas.add(Moga.newMogaFromAppearance(appearance));
                    break;
                }
            }
        } else {
            Iterator<EncounterAppearance> it = this.appearances.iterator();
            while (it.hasNext()) {
                mogas.add(Moga.newMogaFromAppearance(it.next()));
            }
        }
        return mogas;
    }

    public enum Category {
        NORMAL("NORMAL"),
        MULTIPLE("MULTIPLE");

        private String category;

        /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
        public static Category[] valuesCustom() {
            Category[] categoryArrValuesCustom = values();
            int length = categoryArrValuesCustom.length;
            Category[] categoryArr = new Category[length];
            System.arraycopy(categoryArrValuesCustom, 0, categoryArr, 0, length);
            return categoryArr;
        }

        Category(String category) {
            this.category = category;
        }

        public String getName() {
            return this.category;
        }
    }
}
