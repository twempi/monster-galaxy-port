package com.gaiaonline.monstergalaxy.assets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.gaiaonline.monstergalaxy.screen.ScreenElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class AssetsCache {
    private static AssetsCache instance;
    private Map<Asset, TextureRegion> cache = new HashMap();
    private Map<ScreenElement, List<Asset>> screenAssets = new HashMap();

    private AssetsCache() {
    }

    private static AssetsCache getInstance() {
        if (instance == null) {
            instance = new AssetsCache();
        }
        return instance;
    }

    public static void clearTextures() {
        getInstance().cache.clear();
    }

    public static TextureRegion get(ScreenElement screenElement, Asset asset) {
        Map<ScreenElement, List<Asset>> sa = getInstance().screenAssets;
        if (sa.get(screenElement) == null) {
            sa.put(screenElement, new ArrayList());
        }
        sa.get(screenElement).add(asset);
        return get(asset);
    }

    private static TextureRegion get(Asset asset) {
        TextureRegion region = getInstance().cache.get(asset);
        if (region == null) {
            Pixmap pm = new Pixmap(AssetsDownloader.getAsset(asset));
            int width = pm.getWidth();
            int height = pm.getHeight();
            Texture texture = new Texture(MathUtils.nextPowerOfTwo(width), MathUtils.nextPowerOfTwo(height), pm.getFormat());
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            texture.draw(pm, 0, 0);
            int x = 0;
            if (asset.getCropWidth() != null) {
                x = (int) ((width - asset.getCropWidth().intValue()) / 2.0f);
                width = asset.getCropWidth().intValue();
            }
            TextureRegion newRegion = new TextureRegion(texture, x, 0, width, height);
            getInstance().cache.put(asset, newRegion);
            pm.dispose();
            return newRegion;
        }
        return region;
    }

    public static TextureRegion newSolidRectangle(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(MathUtils.nextPowerOfTwo(width), MathUtils.nextPowerOfTwo(height), Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        TextureRegion tr = new TextureRegion(new Texture(pixmap), 0, 0, width, height);
        pixmap.dispose();
        return tr;
    }

    public static void disposeAssets(ScreenElement screenElement) {
        List<Asset> assetsList = getInstance().screenAssets.get(screenElement);
        if (assetsList != null) {
            for (Asset a : assetsList) {
                TextureRegion tr = getInstance().cache.get(a);
                if (tr != null) {
                    tr.getTexture().dispose();
                    getInstance().cache.remove(a);
                }
            }
        }
    }
}
