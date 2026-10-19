package com.gaiaonline.monstergalaxy.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.gaiaonline.monstergalaxy.assets.Asset;
import com.gaiaonline.monstergalaxy.assets.AssetsCache;
import com.gaiaonline.monstergalaxy.assets.AssetsDownloader;

/* JADX INFO: loaded from: classes.dex */
public class DownloadableElement extends TextureElement {
    private Asset asset;
    private AssetsDownloader downloader;
    private boolean isDownloaded;

    public DownloadableElement(Asset asset) {
        super(null);
        this.size.x = 1.0f;
        this.size.y = 1.0f;
        this.asset = asset;
        this.isDownloaded = false;
        this.downloader = AssetsDownloader.getInstance();
        this.downloader.downloadNow(asset);
    }

    @Override // com.gaiaonline.monstergalaxy.screen.TextureElement, com.gaiaonline.monstergalaxy.screen.ScreenElement
    protected void present(Vector2 pos, SpriteBatch batcher) {
        if (this.isDownloaded) {
            super.present(pos, batcher);
        } else if (this.downloader.isDownloadFinished(this.asset)) {
            setTexture(AssetsCache.get(this, this.asset));
            this.isDownloaded = true;
        }
    }

    @Override // com.gaiaonline.monstergalaxy.screen.TextureElement, com.gaiaonline.monstergalaxy.screen.ScreenElement
    public void dispose() {
        this.isDownloaded = false;
    }
}
