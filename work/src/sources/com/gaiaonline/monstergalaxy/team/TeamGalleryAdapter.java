package com.gaiaonline.monstergalaxy.team;

import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import com.gaiaonline.monstergalaxy.model.moga.MogaType;
import com.gaiaonline.monstergalaxy.screen.GalleryAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class TeamGalleryAdapter extends GalleryAdapter<Portrait> {
    private List<Moga> mogas = new ArrayList();
    private TeamScreen screen;

    /* JADX WARN: Multi-variable type inference failed */
    public TeamGalleryAdapter(TeamScreen teamScreen) {
        Object[] objArr = 0;
        this.screen = teamScreen;
        List<Moga> mogas = Game.getTrainer().getTeam().getMogas();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        CapturedMogaComparator capturedMogaComparator = new CapturedMogaComparator(this, null);
        this.mogas.addAll(mogas);
        Collections.sort(this.mogas, capturedMogaComparator);
        for (Moga moga : Game.getTrainer().getMogas()) {
            if (!this.mogas.contains(moga)) {
                arrayList.add(moga);
            }
        }
        Collections.sort(arrayList, capturedMogaComparator);
        this.mogas.addAll(arrayList);
        Iterator<Moga> it = this.mogas.iterator();
        while (it.hasNext()) {
            arrayList3.add(Integer.valueOf(it.next().getType().getId()));
        }
        for (MogaType mogaType : Game.getStorage().getMogaTypesBySubType(MogaType.SubType.NORMAL)) {
            if (!arrayList3.contains(Integer.valueOf(mogaType.getId()))) {
                arrayList2.add(Moga.newMogaFromType(mogaType));
            }
        }
        Collections.sort(arrayList2, new MogaTypeComparator(this, objArr == true ? 1 : 0));
        this.mogas.addAll(arrayList2);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.GalleryAdapter
    public Portrait newItem() {
        return new Portrait(this.screen);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.GalleryAdapter
    public void configureItem(Portrait portrait, int index) {
        portrait.setMoga(this.mogas.get(index));
    }

    @Override // com.gaiaonline.monstergalaxy.screen.GalleryAdapter
    public int getCount() {
        return this.mogas.size() - 1;
    }

    private class CapturedMogaComparator implements Comparator<Moga> {
        private CapturedMogaComparator() {
        }

        /* synthetic */ CapturedMogaComparator(TeamGalleryAdapter teamGalleryAdapter, CapturedMogaComparator capturedMogaComparator) {
            this();
        }

        @Override // java.util.Comparator
        public int compare(Moga moga1, Moga moga2) {
            if (moga1.isSleeping() && !moga2.isSleeping()) {
                return -1;
            }
            if (!moga1.isSleeping() && moga2.isSleeping()) {
                return 1;
            }
            if (moga1.isDead() && moga2.isAlive()) {
                return -1;
            }
            if (moga1.isAlive() && moga2.isDead()) {
                return 1;
            }
            if (moga1.getLevel() < moga2.getLevel()) {
                return -1;
            }
            if (moga1.getLevel() <= moga2.getLevel()) {
                return 0;
            }
            return 1;
        }
    }

    private class MogaTypeComparator implements Comparator<Moga> {
        private MogaTypeComparator() {
        }

        /* synthetic */ MogaTypeComparator(TeamGalleryAdapter teamGalleryAdapter, MogaTypeComparator mogaTypeComparator) {
            this();
        }

        @Override // java.util.Comparator
        public int compare(Moga moga1, Moga moga2) {
            MogaType.Rarity rarity1 = moga1.getType().getRarity();
            MogaType.Rarity rarity2 = moga2.getType().getRarity();
            String name1 = moga1.getName();
            String name2 = moga2.getName();
            if (rarity1.getOrder() < rarity2.getOrder()) {
                return -1;
            }
            if (rarity1.getOrder() > rarity2.getOrder()) {
                return 1;
            }
            int compare = name1.compareTo(name2);
            return compare;
        }
    }
}
