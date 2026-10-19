package com.badlogic.gdx.assets;

import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.PixmapLoader;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.TileAtlasLoader;
import com.badlogic.gdx.assets.loaders.TileMapRendererLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Iterator;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public class AssetManager implements Disposable {
    final ObjectMap<String, Array<String>> assetDependencies;
    final ObjectMap<String, Class> assetTypes;
    final ObjectMap<Class, ObjectMap<String, RefCountedContainer>> assets;
    AssetErrorListener listener;
    final Array<AssetDescriptor> loadQueue;
    int loaded;
    final ObjectMap<Class, AssetLoader> loaders;
    Logger log;
    Stack<AssetLoadingTask> tasks;
    final ExecutorService threadPool;
    int toLoad;

    public AssetManager() {
        this(new InternalFileHandleResolver());
    }

    public AssetManager(FileHandleResolver resolver) {
        this.assets = new ObjectMap<>();
        this.assetTypes = new ObjectMap<>();
        this.assetDependencies = new ObjectMap<>();
        this.loaders = new ObjectMap<>();
        this.loadQueue = new Array<>();
        this.tasks = new Stack<>();
        this.listener = null;
        this.loaded = 0;
        this.toLoad = 0;
        this.log = new Logger(AssetManager.class.getSimpleName(), 0);
        setLoader(BitmapFont.class, new BitmapFontLoader(resolver));
        setLoader(Music.class, new MusicLoader(resolver));
        setLoader(Pixmap.class, new PixmapLoader(resolver));
        setLoader(Sound.class, new SoundLoader(resolver));
        setLoader(TextureAtlas.class, new TextureAtlasLoader(resolver));
        setLoader(Texture.class, new TextureLoader(resolver));
        setLoader(Skin.class, new SkinLoader(resolver));
        setLoader(TileAtlas.class, new TileAtlasLoader(resolver));
        setLoader(TileMapRenderer.class, new TileMapRendererLoader(resolver));
        this.threadPool = Executors.newFixedThreadPool(1, new ThreadFactory() { // from class: com.badlogic.gdx.assets.AssetManager.1
            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "AssetManager-Loader-Thread");
                thread.setDaemon(true);
                return thread;
            }
        });
    }

    public synchronized <T> T get(String str) {
        T t;
        Class<T> cls = this.assetTypes.get(str);
        ObjectMap<String, RefCountedContainer> objectMap = this.assets.get(cls);
        if (objectMap == null) {
            throw new GdxRuntimeException("Asset not loaded: " + str);
        }
        RefCountedContainer refCountedContainer = objectMap.get(str);
        if (refCountedContainer == null) {
            throw new GdxRuntimeException("Asset not loaded: " + str);
        }
        t = (T) refCountedContainer.getObject(cls);
        if (t == null) {
            throw new GdxRuntimeException("Asset not loaded: " + str);
        }
        return t;
    }

    public synchronized <T> T get(String str, Class<T> cls) {
        T t;
        ObjectMap<String, RefCountedContainer> objectMap = this.assets.get(cls);
        if (objectMap == null) {
            throw new GdxRuntimeException("Asset not loaded: " + str);
        }
        RefCountedContainer refCountedContainer = objectMap.get(str);
        if (refCountedContainer == null) {
            throw new GdxRuntimeException("Asset not loaded: " + str);
        }
        t = (T) refCountedContainer.getObject(cls);
        if (t == null) {
            throw new GdxRuntimeException("Asset not loaded: " + str);
        }
        return t;
    }

    /* JADX WARN: Code duplicated, block: B:24:0x0078 A[Catch: all -> 0x0075, TRY_ENTER, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x0009, B:11:0x001d, B:15:0x003f, B:17:0x0047, B:19:0x0059, B:24:0x0078, B:26:0x0082, B:27:0x009a, B:28:0x009b, B:30:0x00b2, B:32:0x00d4, B:33:0x00df, B:34:0x00ef, B:36:0x00f9, B:37:0x00fd, B:39:0x0103, B:41:0x0126, B:43:0x012c, B:40:0x010d), top: B:45:0x0003 }] */
    /* JADX WARN: Code duplicated, block: B:26:0x0082 A[Catch: all -> 0x0075, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x0009, B:11:0x001d, B:15:0x003f, B:17:0x0047, B:19:0x0059, B:24:0x0078, B:26:0x0082, B:27:0x009a, B:28:0x009b, B:30:0x00b2, B:32:0x00d4, B:33:0x00df, B:34:0x00ef, B:36:0x00f9, B:37:0x00fd, B:39:0x0103, B:41:0x0126, B:43:0x012c, B:40:0x010d), top: B:45:0x0003 }] */
    /* JADX WARN: Code duplicated, block: B:28:0x009b A[Catch: all -> 0x0075, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x0009, B:11:0x001d, B:15:0x003f, B:17:0x0047, B:19:0x0059, B:24:0x0078, B:26:0x0082, B:27:0x009a, B:28:0x009b, B:30:0x00b2, B:32:0x00d4, B:33:0x00df, B:34:0x00ef, B:36:0x00f9, B:37:0x00fd, B:39:0x0103, B:41:0x0126, B:43:0x012c, B:40:0x010d), top: B:45:0x0003 }] */
    /* JADX WARN: Code duplicated, block: B:30:0x00b2 A[Catch: all -> 0x0075, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x0009, B:11:0x001d, B:15:0x003f, B:17:0x0047, B:19:0x0059, B:24:0x0078, B:26:0x0082, B:27:0x009a, B:28:0x009b, B:30:0x00b2, B:32:0x00d4, B:33:0x00df, B:34:0x00ef, B:36:0x00f9, B:37:0x00fd, B:39:0x0103, B:41:0x0126, B:43:0x012c, B:40:0x010d), top: B:45:0x0003 }] */
    /* JADX WARN: Code duplicated, block: B:32:0x00d4 A[Catch: all -> 0x0075, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x0009, B:11:0x001d, B:15:0x003f, B:17:0x0047, B:19:0x0059, B:24:0x0078, B:26:0x0082, B:27:0x009a, B:28:0x009b, B:30:0x00b2, B:32:0x00d4, B:33:0x00df, B:34:0x00ef, B:36:0x00f9, B:37:0x00fd, B:39:0x0103, B:41:0x0126, B:43:0x012c, B:40:0x010d), top: B:45:0x0003 }] */
    /* JADX WARN: Code duplicated, block: B:36:0x00f9 A[Catch: all -> 0x0075, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x0009, B:11:0x001d, B:15:0x003f, B:17:0x0047, B:19:0x0059, B:24:0x0078, B:26:0x0082, B:27:0x009a, B:28:0x009b, B:30:0x00b2, B:32:0x00d4, B:33:0x00df, B:34:0x00ef, B:36:0x00f9, B:37:0x00fd, B:39:0x0103, B:41:0x0126, B:43:0x012c, B:40:0x010d), top: B:45:0x0003 }] */
    /* JADX WARN: Code duplicated, block: B:39:0x0103 A[Catch: all -> 0x0075, LOOP:1: B:37:0x00fd->B:39:0x0103, LOOP_END, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x0009, B:11:0x001d, B:15:0x003f, B:17:0x0047, B:19:0x0059, B:24:0x0078, B:26:0x0082, B:27:0x009a, B:28:0x009b, B:30:0x00b2, B:32:0x00d4, B:33:0x00df, B:34:0x00ef, B:36:0x00f9, B:37:0x00fd, B:39:0x0103, B:41:0x0126, B:43:0x012c, B:40:0x010d), top: B:45:0x0003 }] */
    /* JADX WARN: Code duplicated, block: B:40:0x010d A[Catch: all -> 0x0075, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x0009, B:11:0x001d, B:15:0x003f, B:17:0x0047, B:19:0x0059, B:24:0x0078, B:26:0x0082, B:27:0x009a, B:28:0x009b, B:30:0x00b2, B:32:0x00d4, B:33:0x00df, B:34:0x00ef, B:36:0x00f9, B:37:0x00fd, B:39:0x0103, B:41:0x0126, B:43:0x012c, B:40:0x010d), top: B:45:0x0003 }] */
    /* JADX WARN: Code duplicated, block: B:43:0x012c A[Catch: all -> 0x0075, TRY_LEAVE, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x0009, B:11:0x001d, B:15:0x003f, B:17:0x0047, B:19:0x0059, B:24:0x0078, B:26:0x0082, B:27:0x009a, B:28:0x009b, B:30:0x00b2, B:32:0x00d4, B:33:0x00df, B:34:0x00ef, B:36:0x00f9, B:37:0x00fd, B:39:0x0103, B:41:0x0126, B:43:0x012c, B:40:0x010d), top: B:45:0x0003 }] */
    public synchronized void unload(String fileName) {
        Class type;
        RefCountedContainer assetRef;
        Array<String> dependencies;
        int foundIndex = -1;
        for (int i = 0; i < this.loadQueue.size; i++) {
            if (this.loadQueue.get(i).fileName.equals(fileName)) {
                foundIndex = i;
                break;
            }
        }
        if (foundIndex != -1) {
            this.loadQueue.removeIndex(foundIndex);
            this.log.debug("Unload (from queue): " + fileName);
        } else if (this.tasks.size() > 0) {
            AssetLoadingTask currAsset = this.tasks.firstElement();
            if (currAsset.assetDesc.fileName.equals(fileName)) {
                currAsset.cancel = true;
                this.log.debug("Unload (from tasks): " + fileName);
            } else {
                type = this.assetTypes.get(fileName);
                if (type == null) {
                    throw new GdxRuntimeException("Asset not loaded: " + fileName);
                }
                assetRef = this.assets.get(type).get(fileName);
                assetRef.decRefCount();
                if (assetRef.getRefCount() <= 0) {
                    this.log.debug("Unload (dispose): " + fileName);
                    if (assetRef.getObject(Object.class) instanceof Disposable) {
                        ((Disposable) assetRef.getObject(Object.class)).dispose();
                    }
                    this.assetTypes.remove(fileName);
                    this.assets.get(type).remove(fileName);
                } else {
                    this.log.debug("Unload (decrement): " + fileName);
                }
                dependencies = this.assetDependencies.get(fileName);
                if (dependencies != null) {
                    for (String dependency : dependencies) {
                        unload(dependency);
                    }
                }
                if (assetRef.getRefCount() <= 0) {
                    this.assetDependencies.remove(fileName);
                }
            }
        } else {
            type = this.assetTypes.get(fileName);
            if (type == null) {
                throw new GdxRuntimeException("Asset not loaded: " + fileName);
            }
            assetRef = this.assets.get(type).get(fileName);
            assetRef.decRefCount();
            if (assetRef.getRefCount() <= 0) {
                this.log.debug("Unload (dispose): " + fileName);
                if (assetRef.getObject(Object.class) instanceof Disposable) {
                    ((Disposable) assetRef.getObject(Object.class)).dispose();
                }
                this.assetTypes.remove(fileName);
                this.assets.get(type).remove(fileName);
            } else {
                this.log.debug("Unload (decrement): " + fileName);
            }
            dependencies = this.assetDependencies.get(fileName);
            if (dependencies != null) {
                while (r6.hasNext()) {
                    unload(dependency);
                }
            }
            if (assetRef.getRefCount() <= 0) {
                this.assetDependencies.remove(fileName);
            }
        }
    }

    public synchronized <T> boolean containsAsset(T asset) {
        boolean z;
        ObjectMap<String, RefCountedContainer> typedAssets = this.assets.get(asset.getClass());
        if (typedAssets == null) {
            z = false;
        } else {
            for (String fileName : typedAssets.keys()) {
                Object object = typedAssets.get(fileName).getObject(Object.class);
                if (object == asset || asset.equals(object)) {
                    z = true;
                }
            }
            z = false;
        }
        return z;
    }

    public synchronized <T> String getAssetFileName(T asset) {
        String fileName;
        for (Class assetType : this.assets.keys()) {
            ObjectMap<String, RefCountedContainer> typedAssets = this.assets.get(assetType);
            Iterator<String> it = typedAssets.keys().iterator();
            while (it.hasNext()) {
                fileName = it.next();
                Object object = typedAssets.get(fileName).getObject(Object.class);
                if (object == asset || asset.equals(object)) {
                }
            }
        }
        fileName = null;
        return fileName;
    }

    public synchronized boolean isLoaded(String fileName) {
        return fileName == null ? false : this.assetTypes.containsKey(fileName);
    }

    public synchronized boolean isLoaded(String fileName, Class type) {
        RefCountedContainer assetContainer;
        boolean z = false;
        synchronized (this) {
            ObjectMap<String, RefCountedContainer> assetsByType = this.assets.get(type);
            if (assetsByType != null && (assetContainer = assetsByType.get(fileName)) != null && assetContainer.getObject(type) != null) {
                z = true;
            }
        }
        return z;
    }

    public synchronized <T> void load(String fileName, Class<T> type) {
        load(fileName, type, null);
    }

    public synchronized <T> void load(String fileName, Class<T> type, AssetLoaderParameters<T> parameter) {
        AssetLoader loader = this.loaders.get(type);
        if (loader == null) {
            throw new GdxRuntimeException("No loader for type: " + type.getSimpleName());
        }
        if (this.loadQueue.size == 0) {
            this.loaded = 0;
            this.toLoad = 0;
        }
        for (int i = 0; i < this.loadQueue.size; i++) {
            AssetDescriptor desc = this.loadQueue.get(i);
            if (desc.fileName.equals(fileName) && !desc.type.equals(type)) {
                throw new GdxRuntimeException("Asset with name '" + fileName + "' already in preload queue, but has different type (expected: " + type.getSimpleName() + ", found: " + desc.type.getSimpleName() + ")");
            }
        }
        for (int i2 = 0; i2 < this.tasks.size(); i2++) {
            AssetDescriptor desc2 = this.tasks.get(i2).assetDesc;
            if (desc2.fileName.equals(fileName) && !desc2.type.equals(type)) {
                throw new GdxRuntimeException("Asset with name '" + fileName + "' already in task list, but has different type (expected: " + type.getSimpleName() + ", found: " + desc2.type.getSimpleName() + ")");
            }
        }
        Class otherType = this.assetTypes.get(fileName);
        if (otherType != null && !otherType.equals(type)) {
            throw new GdxRuntimeException("Asset with name '" + fileName + "' already loaded, but has different type (expected: " + type.getSimpleName() + ", found: " + otherType.getSimpleName() + ")");
        }
        this.toLoad++;
        AssetDescriptor assetDesc = new AssetDescriptor(fileName, type, parameter);
        this.loadQueue.add(assetDesc);
        this.log.debug("Queued: " + assetDesc);
    }

    public synchronized void load(AssetDescriptor desc) {
        load(desc.fileName, desc.type, desc.params);
    }

    private void disposeDependencies(String fileName) {
        Array<String> dependencies = this.assetDependencies.get(fileName);
        if (dependencies != null) {
            for (String dependency : dependencies) {
                disposeDependencies(dependency);
            }
        }
        Class type = this.assetTypes.get(fileName);
        Object asset = this.assets.get(type).get(fileName).getObject(Object.class);
        if (asset instanceof Disposable) {
            ((Disposable) asset).dispose();
        }
    }

    /* JADX WARN: Code duplicated, block: B:19:0x0031 A[Catch: Throwable -> 0x001d, all -> 0x0049, TryCatch #0 {Throwable -> 0x001d, blocks: (B:4:0x0003, B:6:0x000b, B:8:0x0011, B:10:0x0019, B:17:0x0029, B:19:0x0031, B:21:0x0037, B:23:0x003d), top: B:30:0x0003, outer: #1 }] */
    /* JADX WARN: Code duplicated, block: B:25:0x0045  */
    public synchronized boolean update() {
        boolean z = true;
        synchronized (this) {
            try {
                if (this.tasks.size() == 0) {
                    while (this.loadQueue.size != 0 && this.tasks.size() == 0) {
                        nextTask();
                    }
                    if (this.tasks.size() != 0) {
                        if (updateTask() || this.loadQueue.size != 0 || this.tasks.size() != 0) {
                            z = false;
                        }
                    }
                } else if (updateTask()) {
                    z = false;
                } else {
                    z = false;
                }
            } catch (Throwable t) {
                handleTaskError(t);
                if (this.loadQueue.size != 0) {
                    z = false;
                }
            }
        }
        return z;
    }

    public synchronized boolean update(int millis) {
        boolean done;
        long endTime = System.nanoTime() + ((long) (millis * 1000));
        while (true) {
            done = update();
            if (done || System.currentTimeMillis() > endTime) {
                break;
            }
            Thread.yield();
        }
        return done;
    }

    public void finishLoading() {
        this.log.debug("Waiting for loading to complete...");
        while (!update()) {
            Thread.yield();
        }
        this.log.debug("Loading complete.");
    }

    synchronized void injectDependency(String parentAssetFilename, AssetDescriptor dependendAssetDesc) {
        Array<String> dependencies = this.assetDependencies.get(parentAssetFilename);
        if (dependencies == null) {
            dependencies = new Array<>();
            this.assetDependencies.put(parentAssetFilename, dependencies);
        }
        dependencies.add(dependendAssetDesc.fileName);
        if (isLoaded(dependendAssetDesc.fileName)) {
            this.log.debug("Dependency already loaded: " + dependendAssetDesc);
            Class type = this.assetTypes.get(dependendAssetDesc.fileName);
            RefCountedContainer assetRef = this.assets.get(type).get(dependendAssetDesc.fileName);
            assetRef.incRefCount();
            incrementRefCountedDependencies(dependendAssetDesc.fileName);
        } else {
            this.log.info("Loading dependency: " + dependendAssetDesc);
            addTask(dependendAssetDesc);
        }
    }

    private void nextTask() {
        AssetDescriptor assetDesc = this.loadQueue.removeIndex(0);
        if (isLoaded(assetDesc.fileName)) {
            this.log.debug("Already loaded: " + assetDesc);
            Class type = this.assetTypes.get(assetDesc.fileName);
            RefCountedContainer assetRef = this.assets.get(type).get(assetDesc.fileName);
            assetRef.incRefCount();
            incrementRefCountedDependencies(assetDesc.fileName);
            this.loaded++;
            return;
        }
        this.log.info("Loading: " + assetDesc);
        addTask(assetDesc);
    }

    private void addTask(AssetDescriptor assetDesc) {
        AssetLoader loader = this.loaders.get(assetDesc.type);
        if (loader == null) {
            throw new GdxRuntimeException("No loader for type: " + assetDesc.type.getSimpleName());
        }
        this.tasks.push(new AssetLoadingTask(this, assetDesc, loader, this.threadPool));
    }

    private boolean updateTask() {
        AssetLoadingTask task = this.tasks.peek();
        if (task.update()) {
            this.assetTypes.put(task.assetDesc.fileName, task.assetDesc.type);
            ObjectMap<String, RefCountedContainer> typeToAssets = this.assets.get(task.assetDesc.type);
            if (typeToAssets == null) {
                typeToAssets = new ObjectMap<>();
                this.assets.put(task.assetDesc.type, typeToAssets);
            }
            typeToAssets.put(task.assetDesc.fileName, new RefCountedContainer(task.getAsset()));
            if (this.tasks.size() == 1) {
                this.loaded++;
            }
            this.tasks.pop();
            if (task.cancel) {
                unload(task.assetDesc.fileName);
                return true;
            }
            if (task.assetDesc.params != null && task.assetDesc.params.loadedCallback != null) {
                task.assetDesc.params.loadedCallback.finishedLoading(this, task.assetDesc.fileName, task.assetDesc.type);
            }
            long endTime = TimeUtils.nanoTime();
            this.log.debug("Loaded: " + ((endTime - task.startTime) / 1000000.0f) + "ms " + task.assetDesc);
            return true;
        }
        return false;
    }

    private void incrementRefCountedDependencies(String parent) {
        Array<String> dependencies = this.assetDependencies.get(parent);
        if (dependencies != null) {
            for (String dependency : dependencies) {
                Class type = this.assetTypes.get(dependency);
                RefCountedContainer assetRef = this.assets.get(type).get(dependency);
                assetRef.incRefCount();
                incrementRefCountedDependencies(dependency);
            }
        }
    }

    private void handleTaskError(Throwable t) {
        this.log.error("Error loading asset.", t);
        if (this.tasks.isEmpty()) {
            throw new GdxRuntimeException(t);
        }
        AssetLoadingTask task = this.tasks.pop();
        AssetDescriptor assetDesc = task.assetDesc;
        if (task.dependenciesLoaded && task.dependencies != null) {
            for (AssetDescriptor desc : task.dependencies) {
                unload(desc.fileName);
            }
        }
        this.tasks.clear();
        if (this.listener != null) {
            this.listener.error(assetDesc.fileName, assetDesc.type, t);
            return;
        }
        throw new GdxRuntimeException(t);
    }

    public synchronized <T, P extends AssetLoaderParameters<T>> void setLoader(Class<T> type, AssetLoader<T, P> loader) {
        try {
            if (type == null) {
                throw new IllegalArgumentException("type cannot be null.");
            }
            if (loader == null) {
                throw new IllegalArgumentException("loader cannot be null.");
            }
            this.log.debug("Loader set: " + type.getSimpleName() + " -> " + loader.getClass().getSimpleName());
            this.loaders.put(type, loader);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized int getLoadedAssets() {
        return this.assetTypes.size;
    }

    public synchronized int getQueuedAssets() {
        return this.loadQueue.size + this.tasks.size();
    }

    public synchronized float getProgress() {
        float fMin;
        synchronized (this) {
            fMin = this.toLoad != 0 ? Math.min(1.0f, this.loaded / this.toLoad) : 1.0f;
        }
        return fMin;
    }

    public synchronized void setErrorListener(AssetErrorListener listener) {
        this.listener = listener;
    }

    @Override // com.badlogic.gdx.utils.Disposable
    public synchronized void dispose() {
        try {
            this.log.debug("Disposing.");
            clear();
            this.threadPool.shutdown();
            try {
                this.threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                new GdxRuntimeException("Couldn't shutdown loading thread");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void clear() {
        this.loadQueue.clear();
        while (!update()) {
        }
        ObjectIntMap<String> dependencyCount = new ObjectIntMap<>();
        while (this.assetTypes.size > 0) {
            dependencyCount.clear();
            Array<String> assets = this.assetTypes.keys().toArray();
            Iterator<String> it = assets.iterator();
            while (it.hasNext()) {
                dependencyCount.put(it.next(), 0);
            }
            Iterator<String> it2 = assets.iterator();
            while (it2.hasNext()) {
                Array<String> dependencies = this.assetDependencies.get(it2.next());
                if (dependencies != null) {
                    for (String dependency : dependencies) {
                        int count = dependencyCount.get(dependency, 0);
                        dependencyCount.put(dependency, count + 1);
                    }
                }
            }
            for (String asset : assets) {
                if (dependencyCount.get(asset, 0) == 0) {
                    unload(asset);
                }
            }
        }
        this.assets.clear();
        this.assetTypes.clear();
        this.assetDependencies.clear();
        this.loaded = 0;
        this.toLoad = 0;
        this.loadQueue.clear();
        this.tasks.clear();
    }

    public Logger getLogger() {
        return this.log;
    }

    public synchronized int getReferenceCount(String fileName) {
        Class type;
        type = this.assetTypes.get(fileName);
        if (type == null) {
            throw new GdxRuntimeException("Asset not loaded: " + fileName);
        }
        return this.assets.get(type).get(fileName).getRefCount();
    }

    public synchronized void setReferenceCount(String fileName, int refCount) {
        Class type = this.assetTypes.get(fileName);
        if (type == null) {
            throw new GdxRuntimeException("Asset not loaded: " + fileName);
        }
        this.assets.get(type).get(fileName).setRefCount(refCount);
    }

    public synchronized String getDiagnostics() {
        StringBuffer buffer;
        buffer = new StringBuffer();
        for (String fileName : this.assetTypes.keys()) {
            buffer.append(fileName);
            buffer.append(", ");
            Class type = this.assetTypes.get(fileName);
            RefCountedContainer assetRef = this.assets.get(type).get(fileName);
            Array<String> dependencies = this.assetDependencies.get(fileName);
            buffer.append(type.getSimpleName());
            buffer.append(", refs: ");
            buffer.append(assetRef.getRefCount());
            if (dependencies != null) {
                buffer.append(", deps: [");
                for (String dep : dependencies) {
                    buffer.append(dep);
                    buffer.append(",");
                }
                buffer.append("]");
            }
            buffer.append("\n");
        }
        return buffer.toString();
    }

    public synchronized Array<String> getAssetNames() {
        return this.assetTypes.keys().toArray();
    }

    public synchronized Array<String> getDependencies(String fileName) {
        return this.assetDependencies.get(fileName);
    }

    public synchronized Class getAssetType(String fileName) {
        return this.assetTypes.get(fileName);
    }
}
