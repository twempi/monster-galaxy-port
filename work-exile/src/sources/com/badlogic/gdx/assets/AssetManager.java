package com.badlogic.gdx.assets;

import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.PixmapLoader;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ObjectMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class AssetManager implements Disposable {
    final ExecutorService threadPool;
    final ObjectMap<Class, ObjectMap<String, Object>> assets = new ObjectMap<>();
    final ObjectMap<String, Class> assetTypes = new ObjectMap<>();
    final ObjectMap<String, Array<String>> assetDependencies = new ObjectMap<>();
    final ObjectMap<Class, AssetLoader> loaders = new ObjectMap<>();
    final Array<AssetDescriptor> preloadQueue = new Array<>();
    Stack<AssetLoadingTask> tasks = new Stack<>();
    AssetErrorListener listener = null;
    int loaded = 0;
    int toLoad = 0;
    Logger log = new Logger(AssetManager.class.getSimpleName());

    public AssetManager() {
        this.log.setEnabled(false);
        setLoader(BitmapFont.class, new BitmapFontLoader(new InternalFileHandleResolver()));
        setLoader(Music.class, new MusicLoader(new InternalFileHandleResolver()));
        setLoader(Pixmap.class, new PixmapLoader(new InternalFileHandleResolver()));
        setLoader(Sound.class, new SoundLoader(new InternalFileHandleResolver()));
        setLoader(TextureAtlas.class, new TextureAtlasLoader(new InternalFileHandleResolver()));
        setLoader(Texture.class, new TextureLoader(new InternalFileHandleResolver()));
        this.threadPool = Executors.newFixedThreadPool(1, new ThreadFactory() { // from class: com.badlogic.gdx.assets.AssetManager.1
            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "AssetManager-Loader-Thread");
                thread.setDaemon(true);
                return thread;
            }
        });
    }

    public synchronized <T> T get(String str, Class<T> cls) {
        T t;
        ObjectMap<String, Object> objectMap = this.assets.get(cls);
        if (objectMap == null) {
            throw new GdxRuntimeException("Asset '" + str + "' not loaded");
        }
        t = (T) objectMap.get(str);
        if (t == null) {
            throw new GdxRuntimeException("Asset '" + str + "' not loaded");
        }
        return t;
    }

    /* JADX WARN: Code duplicated, block: B:12:0x002a  */
    /* JADX WARN: Code duplicated, block: B:15:0x0032 A[Catch: all -> 0x0065, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x000b, B:7:0x0013, B:9:0x0025, B:13:0x002c, B:15:0x0032, B:20:0x0046, B:21:0x0064, B:26:0x006b, B:27:0x0071, B:29:0x0081, B:30:0x0088, B:32:0x008c, B:34:0x0094, B:36:0x00ae, B:37:0x00b2, B:39:0x00b8), top: B:41:0x0001 }] */
    /* JADX WARN: Code duplicated, block: B:20:0x0046 A[Catch: all -> 0x0065, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x000b, B:7:0x0013, B:9:0x0025, B:13:0x002c, B:15:0x0032, B:20:0x0046, B:21:0x0064, B:26:0x006b, B:27:0x0071, B:29:0x0081, B:30:0x0088, B:32:0x008c, B:34:0x0094, B:36:0x00ae, B:37:0x00b2, B:39:0x00b8), top: B:41:0x0001 }] */
    /* JADX WARN: Code duplicated, block: B:25:0x0068 A[LOOP:0: B:13:0x002c->B:25:0x0068, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:26:0x006b A[Catch: all -> 0x0065, TRY_ENTER, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x000b, B:7:0x0013, B:9:0x0025, B:13:0x002c, B:15:0x0032, B:20:0x0046, B:21:0x0064, B:26:0x006b, B:27:0x0071, B:29:0x0081, B:30:0x0088, B:32:0x008c, B:34:0x0094, B:36:0x00ae, B:37:0x00b2, B:39:0x00b8), top: B:41:0x0001 }] */
    /* JADX WARN: Code duplicated, block: B:42:0x0042 A[SYNTHETIC] */
    public synchronized void remove(String fileName) {
        int foundIndex;
        int i;
        Class type = this.assetTypes.get(fileName);
        if (type == null) {
            if (this.tasks.size() > 0) {
                AssetLoadingTask currAsset = this.tasks.firstElement();
                if (currAsset.assetDesc.fileName.equals(fileName)) {
                    currAsset.cancel = true;
                } else {
                    foundIndex = -1;
                    for (i = 0; i < this.preloadQueue.size; i++) {
                        if (this.preloadQueue.get(i).fileName.equals(fileName)) {
                            foundIndex = i;
                            break;
                        }
                    }
                    if (foundIndex == -1) {
                        throw new GdxRuntimeException("Asset '" + fileName + "' not loaded");
                    }
                    this.preloadQueue.removeIndex(foundIndex);
                }
            } else {
                foundIndex = -1;
                while (i < this.preloadQueue.size) {
                    if (this.preloadQueue.get(i).fileName.equals(fileName)) {
                        foundIndex = i;
                        break;
                    }
                }
                if (foundIndex == -1) {
                    throw new GdxRuntimeException("Asset '" + fileName + "' not loaded");
                }
                this.preloadQueue.removeIndex(foundIndex);
            }
        } else {
            Object asset = this.assets.get(type).get(fileName);
            if (asset instanceof Disposable) {
                ((Disposable) asset).dispose();
            }
            if (!(asset instanceof ReferenceCountedAsset) || ((ReferenceCountedAsset) asset).getRefCount() <= 0) {
                this.assetTypes.remove(fileName);
                this.assets.get(type).remove(fileName);
                Array<String> dependencies = this.assetDependencies.remove(fileName);
                if (dependencies != null) {
                    for (String dependency : dependencies) {
                        remove(dependency);
                    }
                }
            }
        }
    }

    public synchronized <T> boolean containsAsset(T asset) {
        boolean z;
        ObjectMap<String, Object> typedAssets = this.assets.get(asset.getClass());
        for (String fileName : typedAssets.keys()) {
            Object otherAsset = typedAssets.get(fileName);
            if (otherAsset == asset || asset.equals(otherAsset)) {
                z = true;
            }
        }
        z = false;
        return z;
    }

    public synchronized <T> String getAssetFileName(T asset) {
        String fileName;
        for (Class assetType : this.assets.keys()) {
            ObjectMap<String, Object> typedAssets = this.assets.get(assetType);
            Iterator<String> it = typedAssets.keys().iterator();
            while (it.hasNext()) {
                fileName = it.next();
                Object otherAsset = typedAssets.get(fileName);
                if (otherAsset == asset || asset.equals(otherAsset)) {
                }
            }
        }
        fileName = null;
        return fileName;
    }

    public synchronized boolean isLoaded(String fileName) {
        return this.assetTypes.containsKey(fileName);
    }

    public synchronized <T> void preload(String fileName, Class<T> type) {
        preload(fileName, type, null);
    }

    public synchronized <T> void preload(String fileName, Class<T> type, AssetLoaderParameters<T> parameter) {
        AssetLoader loader = this.loaders.get(type);
        if (loader == null) {
            throw new GdxRuntimeException("No loader for type '" + type.getSimpleName() + "'");
        }
        if (this.preloadQueue.size == 0) {
            this.loaded = 0;
            this.toLoad = 0;
        }
        this.toLoad++;
        AssetDescriptor assetDesc = new AssetDescriptor(fileName, type, parameter);
        this.preloadQueue.add(assetDesc);
        this.log.log("Added asset '" + assetDesc + "' to preload queue");
    }

    /* JADX WARN: Code duplicated, block: B:19:0x0031 A[Catch: Throwable -> 0x001d, all -> 0x0049, TryCatch #0 {Throwable -> 0x001d, blocks: (B:4:0x0003, B:6:0x000b, B:8:0x0011, B:10:0x0019, B:17:0x0029, B:19:0x0031, B:21:0x0037, B:23:0x003d), top: B:30:0x0003, outer: #1 }] */
    /* JADX WARN: Code duplicated, block: B:25:0x0045  */
    public synchronized boolean update() {
        boolean z = true;
        synchronized (this) {
            try {
                if (this.tasks.size() == 0) {
                    while (this.preloadQueue.size != 0 && this.tasks.size() == 0) {
                        nextTask();
                    }
                    if (this.tasks.size() != 0) {
                        if (updateTask() || this.preloadQueue.size != 0 || this.tasks.size() != 0) {
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
                if (this.preloadQueue.size != 0) {
                    z = false;
                }
            }
        }
        return z;
    }

    synchronized void injectDependency(String parentAssetFilename, AssetDescriptor dependendAssetDesc) {
        Array<String> dependencies = this.assetDependencies.get(parentAssetFilename);
        if (dependencies == null) {
            dependencies = new Array<>();
            this.assetDependencies.put(parentAssetFilename, dependencies);
        }
        dependencies.add(dependendAssetDesc.fileName);
        if (isLoaded(dependendAssetDesc.fileName)) {
            Class type = this.assetTypes.get(dependendAssetDesc.fileName);
            Object asset = this.assets.get(type).get(dependendAssetDesc.fileName);
            if (asset instanceof ReferenceCountedAsset) {
                ((ReferenceCountedAsset) asset).incRefCount();
            }
        } else {
            addTask(dependendAssetDesc);
        }
        this.log.log("Injected dependency '" + dependendAssetDesc + "' for asset '" + parentAssetFilename + "'");
    }

    private void nextTask() {
        AssetDescriptor assetDesc = this.preloadQueue.removeIndex(0);
        if (isLoaded(assetDesc.fileName)) {
            Class type = this.assetTypes.get(assetDesc.fileName);
            Object asset = this.assets.get(type).get(assetDesc.fileName);
            if (asset instanceof ReferenceCountedAsset) {
                ((ReferenceCountedAsset) asset).incRefCount();
                return;
            }
            return;
        }
        addTask(assetDesc);
    }

    private void addTask(AssetDescriptor assetDesc) {
        AssetLoader loader = this.loaders.get(assetDesc.type);
        if (loader == null) {
            throw new GdxRuntimeException("No loader for type '" + assetDesc.type.getSimpleName() + "'");
        }
        this.tasks.push(new AssetLoadingTask(this, assetDesc, loader, this.threadPool));
    }

    private boolean updateTask() {
        AssetLoadingTask task = this.tasks.peek();
        if (!task.update()) {
            return false;
        }
        this.assetTypes.put(task.assetDesc.fileName, task.assetDesc.type);
        ObjectMap<String, Object> typeToAssets = this.assets.get(task.assetDesc.type);
        if (typeToAssets == null) {
            typeToAssets = new ObjectMap<>();
            this.assets.put(task.assetDesc.type, typeToAssets);
        }
        typeToAssets.put(task.assetDesc.fileName, task.getAsset());
        incrementRefCountedDependencies(task.assetDesc.fileName);
        if (this.tasks.size() == 1) {
            this.loaded++;
        }
        this.tasks.pop();
        if (!task.cancel) {
            return true;
        }
        remove(task.assetDesc.fileName);
        return true;
    }

    private void incrementRefCountedDependencies(String parent) {
        Array<String> dependencies = this.assetDependencies.get(parent);
        if (dependencies != null) {
            for (String dependency : dependencies) {
                Class type = this.assetTypes.get(dependency);
                if (type == null) {
                    throw new GdxRuntimeException("Asset '" + dependency + "' not loaded");
                }
                Object asset = this.assets.get(type).get(dependency);
                if (asset instanceof ReferenceCountedAsset) {
                    ((ReferenceCountedAsset) asset).incRefCount();
                } else {
                    incrementRefCountedDependencies(dependency);
                }
            }
        }
    }

    private void handleTaskError(Throwable t) {
        AssetLoadingTask task = this.tasks.pop();
        AssetDescriptor assetDesc = task.assetDesc;
        if (task.dependenciesLoaded && task.dependencies != null) {
            for (AssetDescriptor desc : task.dependencies) {
                remove(desc.fileName);
            }
        }
        this.tasks.clear();
        if (this.listener != null) {
            this.listener.error(assetDesc.fileName, assetDesc.type, t);
        } else {
            t.printStackTrace();
            throw new GdxRuntimeException(t);
        }
    }

    public synchronized <T, P> void setLoader(Class<T> type, AssetLoader<T, P> loader) {
        this.loaders.put(type, loader);
    }

    public synchronized int getLoadedAssets() {
        return this.assetTypes.size;
    }

    public synchronized int getQueuedAssets() {
        return this.preloadQueue.size + this.tasks.size();
    }

    public synchronized float getProgress() {
        return this.loaded / this.toLoad;
    }

    public synchronized void setErrorListener(AssetErrorListener listener) {
        this.listener = listener;
    }

    @Override // com.badlogic.gdx.utils.Disposable
    public synchronized void dispose() {
        try {
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

    /* JADX WARN: Code duplicated, block: B:12:0x0029 A[Catch: all -> 0x0033, LOOP:0: B:10:0x0023->B:12:0x0029, LOOP_END, TRY_LEAVE, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0007, B:9:0x0015, B:10:0x0023, B:12:0x0029, B:19:0x003b, B:7:0x000f, B:18:0x0037), top: B:22:0x0001, inners: #1 }] */
    public synchronized void clear() {
        if (this.preloadQueue.size > 0 || this.tasks.size() != 0) {
            do {
                try {
                } catch (Throwable t) {
                    handleTaskError(t);
                }
            } while (!updateTask());
            Array<String> assets = this.assetTypes.keys().toArray();
            for (String asset : assets) {
                remove(asset);
            }
            this.assets.clear();
            this.assetTypes.clear();
            this.assetDependencies.clear();
            this.loaded = 0;
            this.preloadQueue.clear();
            this.tasks.clear();
        } else {
            Array<String> assets2 = this.assetTypes.keys().toArray();
            while (r2.hasNext()) {
                remove(asset);
            }
            this.assets.clear();
            this.assetTypes.clear();
            this.assetDependencies.clear();
            this.loaded = 0;
            this.preloadQueue.clear();
            this.tasks.clear();
        }
        throw th;
    }

    public Logger getLogger() {
        return this.log;
    }

    public synchronized String getDiagonistics() {
        StringBuffer buffer;
        buffer = new StringBuffer();
        for (String fileName : this.assetTypes.keys()) {
            buffer.append(fileName);
            buffer.append(", ");
            Class type = this.assetTypes.get(fileName);
            Object asset = this.assets.get(type).get(fileName);
            Array<String> dependencies = this.assetDependencies.get(fileName);
            buffer.append(type.getSimpleName());
            if (asset instanceof ReferenceCountedAsset) {
                buffer.append(", refs: ");
                buffer.append(((ReferenceCountedAsset) asset).getRefCount());
            }
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
}
