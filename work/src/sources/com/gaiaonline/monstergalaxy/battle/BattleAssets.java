package com.gaiaonline.monstergalaxy.battle;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.gaiaonline.monstergalaxy.app.Game;
import com.gaiaonline.monstergalaxy.assets.Asset;
import com.gaiaonline.monstergalaxy.assets.AssetsDownloader;
import com.gaiaonline.monstergalaxy.model.map.Node;
import com.gaiaonline.monstergalaxy.model.moga.Moga;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class BattleAssets {
    private static List<Texture> cpuMogaTextures;
    static List<MogaAsset> mogaAssets;
    private static Texture playerMogaTexture;

    private static void setNode(Node node) {
        MogaAsset mogaAsset = null;
        List<Moga> playerMogas = Game.getTrainer().getTeam().getMogas();
        mogaAssets = new ArrayList(playerMogas.size() + 3);
        cpuMogaTextures = new ArrayList(3);
        for (Moga cpuMoga : node.getTeam().getMogas()) {
            MogaAsset ma = new MogaAsset(cpuMoga, true, mogaAsset);
            mogaAssets.add(ma);
        }
        for (Moga playerMoga : playerMogas) {
            MogaAsset ma2 = new MogaAsset(playerMoga, false, mogaAsset);
            mogaAssets.add(ma2);
        }
    }

    public static void load() {
        setNode(Game.getTrainer().getCurrentNode());
        int width = 0;
        int height = 0;
        for (MogaAsset m : mogaAssets) {
            Pixmap pm = new Pixmap(AssetsDownloader.getAsset(m.asset));
            m.pm = pm;
            if (!m.isCpu) {
                if (pm.getHeight() > height) {
                    height = pm.getHeight();
                }
                width += pm.getWidth();
            }
        }
        playerMogaTexture = new Texture(MathUtils.nextPowerOfTwo(width), MathUtils.nextPowerOfTwo(height), Pixmap.Format.RGBA8888);
        playerMogaTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        int offset = 0;
        for (MogaAsset m2 : mogaAssets) {
            if (m2.isCpu) {
                int cpuWidth = MathUtils.nextPowerOfTwo(m2.pm.getWidth());
                int cpuHeight = MathUtils.nextPowerOfTwo(m2.pm.getHeight());
                Texture cpuTex = new Texture(cpuWidth, cpuHeight, Pixmap.Format.RGBA8888);
                cpuTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                cpuTex.draw(m2.pm, 0, 0);
                cpuMogaTextures.add(cpuTex);
                m2.texRegion = new TextureRegion(cpuTex, 0, 0, m2.pm.getWidth(), m2.pm.getHeight());
                m2.texRegion.flip(true, false);
            } else {
                playerMogaTexture.draw(m2.pm, offset, 0);
                m2.texRegion = new TextureRegion(playerMogaTexture, offset, 0, m2.pm.getWidth(), m2.pm.getHeight());
                offset += m2.pm.getWidth();
            }
            m2.pm = null;
        }
    }

    public static TextureRegion getTexture(Moga moga, boolean isCpu) {
        for (MogaAsset ma : mogaAssets) {
            if (ma.moga.equals(moga) && ma.isCpu == isCpu) {
                return ma.texRegion;
            }
        }
        return null;
    }

    public static void dispose() {
        clearTextures();
        for (MogaAsset ma : mogaAssets) {
            ma.texRegion = null;
            Pixmap unused = ma.pm;
            ma.pm = null;
            ma.moga = null;
            ma.asset = null;
        }
        mogaAssets.clear();
        mogaAssets = null;
    }

    private static void clearTextures() {
        if (playerMogaTexture != null) {
            playerMogaTexture = null;
        }
        for (Texture texture : cpuMogaTextures) {
        }
        cpuMogaTextures.clear();
        cpuMogaTextures = null;
    }

    private static class MogaAsset {
        private Asset asset;
        private boolean isCpu;
        private Moga moga;
        private Pixmap pm;
        private TextureRegion texRegion;

        private MogaAsset(Moga m, boolean isCpu) {
            this.moga = m;
            this.isCpu = isCpu;
            this.asset = m.getType().getAsset();
        }

        /* synthetic */ MogaAsset(Moga moga, boolean z, MogaAsset mogaAsset) {
            this(moga, z);
        }
    }
}
