package com.gaiaonline.monstergalaxy.model.trainer;

import com.gaiaonline.monstergalaxy.model.moga.Moga;
import java.io.Serializable;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class Friend implements Serializable {
    private static final long serialVersionUID = -8883615343681485911L;
    private List<Moga> mogas;
    private String name;
    private int rank;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRank() {
        return this.rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public List<Moga> getMogas() {
        return this.mogas;
    }

    public void setMogas(List<Moga> mogas) {
        this.mogas = mogas;
    }
}
