package com.badlogic.gdx.assets;

import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class AssetLoadingTask implements Callable<Void> {
    final AssetDescriptor assetDesc;
    Array<AssetDescriptor> dependencies;
    final AssetLoader loader;
    AssetManager manager;
    final ExecutorService threadPool;
    boolean dependenciesLoaded = false;
    Future<Void> loadFuture = null;
    Future<Void> depsFuture = null;
    boolean updateOnRenderThread = false;
    Object asset = null;
    boolean cancel = false;

    public AssetLoadingTask(AssetManager manager, AssetDescriptor assetDesc, AssetLoader loader, ExecutorService threadPool) {
        this.manager = manager;
        this.assetDesc = assetDesc;
        this.loader = loader;
        this.threadPool = threadPool;
    }

    @Override // java.util.concurrent.Callable
    public Void call() throws Exception {
        AsynchronousAssetLoader asyncLoader = (AsynchronousAssetLoader) this.loader;
        if (!this.dependenciesLoaded) {
            this.dependencies = asyncLoader.getDependencies(this.assetDesc.fileName, this.assetDesc.params);
            if (this.dependencies != null) {
                for (AssetDescriptor desc : this.dependencies) {
                    this.manager.injectDependency(this.assetDesc.fileName, desc);
                }
                return null;
            }
            return null;
        }
        asyncLoader.loadAsync(this.manager, this.assetDesc.fileName, this.assetDesc.params);
        return null;
    }

    public boolean update() {
        if (this.loader instanceof SynchronousAssetLoader) {
            handleSyncLoader();
        } else {
            handleAsyncLoader();
        }
        return this.asset != null;
    }

    private void handleSyncLoader() {
        SynchronousAssetLoader syncLoader = (SynchronousAssetLoader) this.loader;
        if (!this.dependenciesLoaded) {
            this.dependenciesLoaded = true;
            this.dependencies = syncLoader.getDependencies(this.assetDesc.fileName, this.assetDesc.params);
            if (this.dependencies == null) {
                this.asset = syncLoader.load(this.manager, this.assetDesc.fileName, this.assetDesc.params);
                return;
            }
            for (AssetDescriptor desc : this.dependencies) {
                this.manager.injectDependency(this.assetDesc.fileName, desc);
            }
            return;
        }
        this.asset = syncLoader.load(this.manager, this.assetDesc.fileName, this.assetDesc.params);
    }

    private void handleAsyncLoader() {
        AsynchronousAssetLoader asyncLoader = (AsynchronousAssetLoader) this.loader;
        if (!this.dependenciesLoaded) {
            if (this.depsFuture == null) {
                this.depsFuture = this.threadPool.submit(this);
                return;
            } else {
                if (this.depsFuture.isDone()) {
                    try {
                        this.depsFuture.get();
                        this.dependenciesLoaded = true;
                        return;
                    } catch (Exception e) {
                        throw new GdxRuntimeException("Couldn't load dependencies of asset '" + this.assetDesc.fileName + "'", e);
                    }
                }
                return;
            }
        }
        if (this.loadFuture == null) {
            this.loadFuture = this.threadPool.submit(this);
        } else if (this.loadFuture.isDone()) {
            try {
                this.loadFuture.get();
                this.asset = asyncLoader.loadSync();
            } catch (Exception e2) {
                throw new GdxRuntimeException("Couldn't load asset '" + this.assetDesc.fileName + "'", e2);
            }
        }
    }

    public Object getAsset() {
        return this.asset;
    }
}
