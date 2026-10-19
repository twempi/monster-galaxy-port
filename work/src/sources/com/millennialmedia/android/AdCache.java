package com.millennialmedia.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http.HttpEngine;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
final class AdCache {
    private static final String CACHED_AD_FILE = "ad.dat";
    private static final String CACHE_INCOMPLETE_PREFIX = "incompleteDownload_";
    private static final String CACHE_PREFIX = "nextCachedAd_";
    private static final String CACHE_PREFIX_APID = "nextCachedAd_apids";
    static final int ITERATE_ID = 0;
    static final int ITERATE_INFO = 1;
    static final int ITERATE_OBJECT = 2;
    private static final String LOCK_FILE = "ad.lock";
    static final int PRIORITY_FETCH = 3;
    static final int PRIORITY_PRECACHE = 1;
    static final int PRIORITY_REFRESH = 2;
    private static Set<String> apidListSet;
    private static String cachedVideoList;
    private static boolean cachedVideoListLoaded;
    private static HashSet cachedVideoSet;
    private static Map<String, String> incompleteDownloadHashMap;
    private static boolean incompleteDownloadHashMapLoaded;
    private static Map<String, String> nextCachedAdHashMap;
    private static boolean nextCachedAdHashMapLoaded;

    interface AdCacheTaskListener {
        void downloadCompleted(CachedAd cachedAd, boolean z);
    }

    private AdCache() {
    }

    static void startDownloadTask(Context context, String adName, CachedAd ad, AdCacheTaskListener listener) {
        AdCacheThreadPool.sharedThreadPool().startDownloadTask(context, adName, ad, listener);
    }

    static synchronized void cachedVideoWasAdded(Context context, String acid) {
        if (acid != null) {
            if (!cachedVideoListLoaded) {
                getCachedVideoList(context);
            }
            if (cachedVideoSet == null) {
                cachedVideoSet = new HashSet();
            }
            cachedVideoSet.add(acid);
            cachedVideoList = null;
        }
    }

    static synchronized void cachedVideoWasRemoved(Context context, String acid) {
        if (acid != null) {
            if (!cachedVideoListLoaded) {
                getCachedVideoList(context);
            }
            if (cachedVideoSet != null) {
                cachedVideoSet.remove(acid);
                cachedVideoList = null;
            }
        }
    }

    static synchronized String getCachedVideoList(final Context context) {
        if (cachedVideoList == null) {
            if (!cachedVideoListLoaded) {
                final HashSet hashSet = new HashSet();
                iterateCachedAds(context, 2, new Iterator() { // from class: com.millennialmedia.android.AdCache.1
                    @Override // com.millennialmedia.android.AdCache.Iterator
                    boolean callback(CachedAd cachedAd) {
                        if (cachedAd.acid != null && cachedAd.getType() == 1 && cachedAd.isOnDisk(context)) {
                            hashSet.add(cachedAd.acid);
                        }
                        return true;
                    }
                });
                cachedVideoSet = hashSet;
                cachedVideoListLoaded = true;
            }
            if (cachedVideoSet != null && cachedVideoSet.size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (Object acid : cachedVideoSet) {
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append("," + ((String) acid));
                    } else {
                        stringBuilder.append((String) acid);
                    }
                }
                cachedVideoList = stringBuilder.toString();
            }
        }
        return cachedVideoList;
    }

    private static void loadNextCachedAdHashMap(Context context) {
        SharedPreferences settings = context.getSharedPreferences("MillennialMediaSettings", 0);
        nextCachedAdHashMap = new ConcurrentHashMap();
        if (apidListSet == null) {
            loadApidListSet(settings);
        }
        for (String apid : apidListSet) {
            String[] arr$ = MMAdView.getAdTypes();
            for (String adType : arr$) {
                String result = settings.getString(CACHE_PREFIX + adType + '_' + apid, null);
                if (result != null) {
                    nextCachedAdHashMap.put(adType + '_' + apid, result);
                }
            }
        }
        nextCachedAdHashMapLoaded = true;
    }

    private static void saveNextCachedAdHashMapValue(Context context, String adName) {
        if (adName != null) {
            SharedPreferences settings = context.getSharedPreferences("MillennialMediaSettings", 0);
            SharedPreferences.Editor editor = settings.edit();
            saveApidListSet(editor, adName);
            editor.putString(CACHE_PREFIX + adName, nextCachedAdHashMap.get(adName));
            editor.commit();
        }
    }

    static synchronized String getNextCachedAd(Context context, String adName) {
        if (!nextCachedAdHashMapLoaded) {
            loadNextCachedAdHashMap(context);
        }
        return adName == null ? null : nextCachedAdHashMap.get(adName);
    }

    static CachedAd loadNextCachedAd(Context context, String adName) {
        String nextAd = getNextCachedAd(context, adName);
        if (nextAd == null || nextAd.equals("")) {
            return null;
        }
        return load(context, nextAd);
    }

    static synchronized void setNextCachedAd(Context context, String adName, String id) {
        if (!nextCachedAdHashMapLoaded) {
            loadNextCachedAdHashMap(context);
        }
        if (adName != null) {
            Map<String, String> map = nextCachedAdHashMap;
            if (id == null) {
                id = "";
            }
            map.put(adName, id);
            saveNextCachedAdHashMapValue(context, adName);
        }
    }

    private static void loadIncompleteDownloadHashMap(Context context) {
        SharedPreferences settings = context.getSharedPreferences("MillennialMediaSettings", 0);
        incompleteDownloadHashMap = new ConcurrentHashMap();
        if (apidListSet == null) {
            loadApidListSet(settings);
        }
        for (String apid : apidListSet) {
            String[] arr$ = MMAdView.getAdTypes();
            for (String adType : arr$) {
                String result = settings.getString(CACHE_INCOMPLETE_PREFIX + adType + '_' + apid, null);
                if (result != null) {
                    incompleteDownloadHashMap.put(adType + '_' + apid, result);
                }
            }
        }
        incompleteDownloadHashMapLoaded = true;
    }

    private static void saveIncompleteDownloadHashMap(Context context, String adName) {
        if (adName != null) {
            SharedPreferences settings = context.getSharedPreferences("MillennialMediaSettings", 0);
            SharedPreferences.Editor editor = settings.edit();
            saveApidListSet(editor, adName);
            editor.putString(CACHE_INCOMPLETE_PREFIX + adName, incompleteDownloadHashMap.get(adName));
            editor.commit();
        }
    }

    static synchronized String getIncompleteDownload(Context context, String adName) {
        if (!incompleteDownloadHashMapLoaded) {
            loadIncompleteDownloadHashMap(context);
        }
        return adName == null ? null : incompleteDownloadHashMap.get(adName);
    }

    static CachedAd loadIncompleteDownload(Context context, String adName) {
        String nextIncompleteAd = getIncompleteDownload(context, adName);
        if (nextIncompleteAd == null) {
            return null;
        }
        return load(context, nextIncompleteAd);
    }

    static synchronized void setIncompleteDownload(Context context, String adName, String id) {
        if (!incompleteDownloadHashMapLoaded) {
            loadIncompleteDownloadHashMap(context);
        }
        if (adName != null) {
            Map<String, String> map = incompleteDownloadHashMap;
            if (id == null) {
                id = "";
            }
            map.put(adName, id);
            saveIncompleteDownloadHashMap(context, adName);
        }
    }

    private static void loadApidListSet(SharedPreferences settings) {
        String[] apidArray;
        apidListSet = new HashSet();
        String apids = settings.getString(CACHE_PREFIX_APID, null);
        if (apids != null && (apidArray = apids.split(MMAdViewSDK.COMMA)) != null && apidArray.length > 0) {
            for (String apid : apidArray) {
                apidListSet.add(apid);
            }
        }
    }

    private static void saveApidListSet(SharedPreferences.Editor editor, String adName) {
        String apid;
        int start = adName.indexOf(95);
        if (start >= 0 && start < adName.length() && (apid = adName.substring(start + 1)) != null && !apidListSet.contains(apid)) {
            StringBuilder builder = null;
            if (!apidListSet.isEmpty()) {
                java.util.Iterator<String> apidIter = apidListSet.iterator();
                builder = new StringBuilder();
                while (apidIter.hasNext()) {
                    builder.append(apidIter.next() + MMAdViewSDK.COMMA);
                }
            }
            editor.putString(CACHE_PREFIX_APID, (builder == null ? "" : builder.toString()) + apid);
            apidListSet.add(apid);
        }
    }

    static class Iterator {
        Iterator() {
        }

        boolean callback(String id) {
            return false;
        }

        boolean callback(String id, int type, Date expiration, String acid, long deferredViewStart, ObjectInputStream objectInputStream) {
            return false;
        }

        boolean callback(CachedAd ad) {
            return false;
        }
    }

    /* JADX INFO: Removed unreachable split cross block B:85:0x0049 */
    /* JADX WARN: Code duplicated, block: B:11:0x0026 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:77:0x00f0  */
    /* JADX WARN: Code duplicated, block: B:92:0x0028 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    static void iterateCachedAds(Context context, int mode, Iterator iterator) throws Throwable {
        File cachedDir = getCacheDirectory(context);
        ObjectInputStream objectInputStream = null;
        if (cachedDir != null) {
            File[] adFiles = cachedDir.listFiles(new FileFilter() { // from class: com.millennialmedia.android.AdCache.2
                @Override // java.io.FileFilter
                public boolean accept(File file) {
                    return !file.isDirectory() && file.getName().endsWith(AdCache.CACHED_AD_FILE);
                }
            });
            int len$ = adFiles.length;
            int i$ = 0;
            while (true) {
                ObjectInputStream objectInputStream2 = objectInputStream;
                if (i$ >= len$) {
                    break;
                }
                File adFile = adFiles[i$];
                if (adFile != null) {
                    try {
                        if (adFile.exists()) {
                            if (mode != 0) {
                                objectInputStream = new ObjectInputStream(new FileInputStream(adFile));
                                try {
                                    try {
                                        int type = objectInputStream.readInt();
                                        Date expiration = (Date) objectInputStream.readObject();
                                        String acid = (String) objectInputStream.readObject();
                                        long deferredViewStart = objectInputStream.readLong();
                                        if (mode != 1) {
                                            CachedAd ad = (CachedAd) objectInputStream.readObject();
                                            if (!iterator.callback(ad)) {
                                                objectInputStream.close();
                                                ObjectInputStream objectInputStream3 = null;
                                                if (0 != 0) {
                                                    try {
                                                        objectInputStream3.close();
                                                        return;
                                                    } catch (IOException e) {
                                                        return;
                                                    }
                                                }
                                                return;
                                            }
                                        } else if (!iterator.callback(adFile.getName(), type, expiration, acid, deferredViewStart, objectInputStream)) {
                                            objectInputStream.close();
                                            ObjectInputStream objectInputStream4 = null;
                                            if (0 != 0) {
                                                try {
                                                    objectInputStream4.close();
                                                    return;
                                                } catch (IOException e2) {
                                                    return;
                                                }
                                            }
                                            return;
                                        }
                                        if (objectInputStream != null) {
                                            try {
                                                objectInputStream.close();
                                                objectInputStream = null;
                                            } catch (IOException e3) {
                                            }
                                        }
                                    } catch (Exception e4) {
                                        e = e4;
                                        MMAdViewSDK.Log.d("There was a problem reading the cached ad %s.", adFile.getName());
                                        MMAdViewSDK.Log.d(e);
                                        if (objectInputStream != null) {
                                            try {
                                                objectInputStream.close();
                                                objectInputStream = null;
                                            } catch (IOException e5) {
                                            }
                                        }
                                    }
                                } catch (Throwable th) {
                                    th = th;
                                    if (objectInputStream != null) {
                                        try {
                                            objectInputStream.close();
                                        } catch (IOException e6) {
                                        }
                                    }
                                    throw th;
                                }
                            } else {
                                if (!iterator.callback(adFile.getName())) {
                                    if (objectInputStream2 == null) {
                                        break;
                                    }
                                    try {
                                        objectInputStream2.close();
                                        return;
                                    } catch (IOException e7) {
                                        return;
                                    }
                                }
                                if (objectInputStream2 != null) {
                                    try {
                                        objectInputStream2.close();
                                        objectInputStream = null;
                                    } catch (IOException e8) {
                                        objectInputStream = objectInputStream2;
                                    }
                                } else {
                                    objectInputStream = objectInputStream2;
                                }
                            }
                        } else if (objectInputStream2 != null) {
                            try {
                                objectInputStream2.close();
                                objectInputStream = null;
                            } catch (IOException e9) {
                                objectInputStream = objectInputStream2;
                            }
                        } else {
                            objectInputStream = objectInputStream2;
                        }
                    } catch (Exception e10) {
                        e = e10;
                        objectInputStream = objectInputStream2;
                    } catch (Throwable th2) {
                        th = th2;
                        objectInputStream = objectInputStream2;
                    }
                } else if (objectInputStream2 != null) {
                    objectInputStream2.close();
                    objectInputStream = null;
                } else {
                    objectInputStream = objectInputStream2;
                }
                i$++;
            }
        }
    }

    static void cleanUpExpiredAds(final Context context) throws Throwable {
        iterateCachedAds(context, 1, new Iterator() { // from class: com.millennialmedia.android.AdCache.3
            @Override // com.millennialmedia.android.AdCache.Iterator
            boolean callback(String id, int type, Date expiration, String acid, long deferredViewStart, ObjectInputStream objectInputStream) {
                if (expiration != null && expiration.getTime() <= System.currentTimeMillis()) {
                    try {
                        CachedAd ad = (CachedAd) objectInputStream.readObject();
                        MMAdViewSDK.Log.d("Deleting expired ad %s.", ad.id);
                        ad.delete(context);
                    } catch (Exception e) {
                        MMAdViewSDK.Log.d("There was a problem reading the cached ad %s.", id);
                        MMAdViewSDK.Log.d(e);
                    }
                }
                return true;
            }
        });
    }

    static void resetCache(final Context context) {
        iterateCachedAds(context, 2, new Iterator() { // from class: com.millennialmedia.android.AdCache.4
            @Override // com.millennialmedia.android.AdCache.Iterator
            boolean callback(CachedAd ad) {
                MMAdViewSDK.Log.d("Deleting ad %s.", ad.id);
                ad.delete(context);
                return true;
            }
        });
        cachedVideoSet = null;
        cachedVideoList = null;
        cachedVideoListLoaded = false;
        if (nextCachedAdHashMap != null) {
            for (String key : nextCachedAdHashMap.keySet()) {
                setNextCachedAd(context, key, null);
            }
        }
        if (incompleteDownloadHashMap != null) {
            for (String key2 : incompleteDownloadHashMap.keySet()) {
                setIncompleteDownload(context, key2, null);
            }
        }
    }

    static File getCacheDirectory(Context context) {
        return getCacheDirectory(context, null);
    }

    static File getCacheDirectory(Context context, boolean[] isExternal) {
        File millennialMediaDir;
        File millennialMediaDir2;
        File dir;
        File millennialMediaDir3 = null;
        try {
            if (Environment.getExternalStorageState().equals("mounted")) {
                millennialMediaDir = new File(Environment.getExternalStorageDirectory(), ".mmsyscache");
                try {
                    millennialMediaDir3 = (millennialMediaDir.exists() || millennialMediaDir.mkdirs()) ? millennialMediaDir : null;
                    if (millennialMediaDir3 != null && isExternal != null) {
                        isExternal[0] = true;
                    }
                } catch (Exception e) {
                    e = e;
                    MMAdViewSDK.Log.e(e);
                    return null;
                }
            }
            millennialMediaDir = millennialMediaDir3;
            if (millennialMediaDir != null || (dir = context.getCacheDir()) == null) {
                millennialMediaDir2 = millennialMediaDir;
            } else {
                millennialMediaDir2 = new File(dir, ".mmsyscache");
                if (!millennialMediaDir2.exists() && !millennialMediaDir2.mkdirs()) {
                    millennialMediaDir2 = null;
                }
                if (millennialMediaDir2 != null && isExternal != null) {
                    isExternal[0] = false;
                }
            }
            return millennialMediaDir2;
        } catch (Exception e2) {
            e = e2;
        }
    }

    static File getCachedAdFile(Context context, String id) {
        return getCachedAdFile(context, id, null);
    }

    static File getCachedAdFile(Context context, String id, boolean[] isExternal) {
        File internalFile;
        boolean[] ext = new boolean[1];
        File cacheDir = getCacheDirectory(context, ext);
        File adFile = null;
        if (isExternal != null) {
            isExternal[0] = ext[0];
        }
        if (cacheDir != null) {
            try {
                if (cacheDir.isDirectory()) {
                    adFile = new File(cacheDir, id + CACHED_AD_FILE);
                }
            } catch (Exception e) {
                MMAdViewSDK.Log.e(e);
                return null;
            }
        }
        if ((adFile == null || !adFile.exists()) && ext[0] && (internalFile = context.getCacheDir()) != null) {
            File internalFile2 = new File(internalFile, ".mmsyscache" + File.separator + id + CACHED_AD_FILE);
            if (internalFile2.exists() && internalFile2.isFile()) {
                if (isExternal != null) {
                    isExternal[0] = false;
                    return internalFile2;
                }
                return internalFile2;
            }
        }
        return adFile;
    }

    static boolean downloadComponent(String urlString, String name, File path) throws Throwable {
        File file = new File(path, name);
        MMAdViewSDK.Log.v("Downloading Component: %s from %s", name, urlString);
        if (file.exists() && file.length() > 0) {
            MMAdViewSDK.Log.v(name + " already exists, skipping...");
            return true;
        }
        InputStream stream = null;
        FileOutputStream out = null;
        try {
            try {
                URL connectURL = new URL(urlString);
                HttpURLConnection.setFollowRedirects(true);
                HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
                conn.setConnectTimeout(30000);
                conn.setRequestMethod(HttpEngine.GET);
                conn.connect();
                stream = conn.getInputStream();
                String temp = conn.getHeaderField("Content-Length");
                long contentLength = temp != null ? Long.parseLong(temp) : -1L;
                if (stream == null) {
                    MMAdViewSDK.Log.e("Connection stream is null downloading %s.", name);
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException ex) {
                            MMAdViewSDK.Log.e("Content caching error: %s", ex.getMessage());
                            if (file != null) {
                                file.delete();
                            }
                            return false;
                        }
                    }
                    if (0 == 0) {
                        return false;
                    }
                    out.close();
                    return false;
                }
                FileOutputStream out2 = new FileOutputStream(file);
                try {
                    byte[] buf = new byte[1024];
                    while (true) {
                        int numread = stream.read(buf);
                        if (numread <= 0) {
                            break;
                        }
                        out2.write(buf, 0, numread);
                    }
                    if (file != null) {
                        try {
                            if (file.length() == contentLength || contentLength == -1) {
                                if (stream != null) {
                                    try {
                                        stream.close();
                                    } catch (IOException ex2) {
                                        MMAdViewSDK.Log.e("Content caching error: %s", ex2.getMessage());
                                        if (file != null) {
                                            file.delete();
                                        }
                                        return false;
                                    }
                                }
                                if (out2 == null) {
                                    return true;
                                }
                                out2.close();
                                return true;
                            }
                            MMAdViewSDK.Log.e("Content-Length does not match actual length.");
                        } catch (SecurityException e) {
                            MMAdViewSDK.Log.e("Exception downloading component %s: %s", name, e.getMessage());
                        }
                    }
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException ex3) {
                            MMAdViewSDK.Log.e("Content caching error: %s", ex3.getMessage());
                            if (file != null) {
                                file.delete();
                            }
                            return false;
                        }
                    }
                    if (out2 != null) {
                        out2.close();
                    }
                } catch (Exception e2) {
                    e = e2;
                    out = out2;
                    MMAdViewSDK.Log.e("Exception downloading component %s: %s", name, e.getMessage());
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException ex4) {
                            MMAdViewSDK.Log.e("Content caching error: %s", ex4.getMessage());
                            if (file != null) {
                                file.delete();
                            }
                            return false;
                        }
                    }
                    if (out != null) {
                        out.close();
                    }
                } catch (Throwable th) {
                    th = th;
                    out = out2;
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException ex5) {
                            MMAdViewSDK.Log.e("Content caching error: %s", ex5.getMessage());
                            if (file != null) {
                                file.delete();
                            }
                            return false;
                        }
                    }
                    if (out != null) {
                        out.close();
                    }
                    throw th;
                }
                if (file != null) {
                    file.delete();
                }
                return false;
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e3) {
            e = e3;
        }
    }

    static CachedAd load(Context context, String id) throws Throwable {
        if (id == null) {
            return null;
        }
        ObjectInputStream objectInputStream = null;
        CachedAd ad = null;
        boolean[] isExternal = new boolean[1];
        File cachedAdFile = getCachedAdFile(context, id, isExternal);
        try {
            if (cachedAdFile == null) {
                return null;
            }
            try {
                ObjectInputStream objectInputStream2 = new ObjectInputStream(new FileInputStream(cachedAdFile));
                try {
                    objectInputStream2.readInt();
                    objectInputStream2.readObject();
                    objectInputStream2.readLong();
                    ad = (CachedAd) objectInputStream2.readObject();
                    ad.storedOnSdCard = isExternal[0];
                    if (objectInputStream2 != null) {
                        try {
                            objectInputStream2.close();
                        } catch (IOException e) {
                            objectInputStream = objectInputStream2;
                        }
                    }
                    objectInputStream = objectInputStream2;
                } catch (FileNotFoundException e2) {
                    objectInputStream = objectInputStream2;
                    MMAdViewSDK.Log.e("There was a problem loading up the cached ad %s. Ad is not on disk.", id);
                    if (objectInputStream != null) {
                        try {
                            objectInputStream.close();
                        } catch (IOException e3) {
                        }
                    }
                } catch (Exception e4) {
                    e = e4;
                    objectInputStream = objectInputStream2;
                    MMAdViewSDK.Log.e("There was a problem loading up the cached ad %s.", id);
                    MMAdViewSDK.Log.d(e.getMessage());
                    MMAdViewSDK.Log.d(e);
                    if (objectInputStream != null) {
                        try {
                            objectInputStream.close();
                        } catch (IOException e5) {
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    objectInputStream = objectInputStream2;
                }
            } catch (FileNotFoundException e6) {
            } catch (Exception e7) {
                e = e7;
            }
            return ad;
        } catch (Throwable th2) {
            th = th2;
        }
        if (objectInputStream != null) {
            try {
                objectInputStream.close();
            } catch (IOException e8) {
            }
        }
        throw th;
    }

    static boolean save(Context context, CachedAd ad) throws Throwable {
        File cachedAdFile;
        boolean z = false;
        File lockFile = null;
        ObjectOutputStream objectOutputStream = null;
        boolean[] isExternal = new boolean[1];
        if (ad != null && (cachedAdFile = getCachedAdFile(context, ad.id, isExternal)) != null) {
            MMAdViewSDK.Log.v("Saving CachedAd %s to %s", ad.id, cachedAdFile);
            try {
                File lockFile2 = new File(cachedAdFile.getParent(), ad.id + LOCK_FILE);
                try {
                    if (lockFile2.createNewFile()) {
                        ObjectOutputStream objectOutputStream2 = new ObjectOutputStream(new FileOutputStream(cachedAdFile));
                        try {
                            objectOutputStream2.writeInt(ad.getType());
                            objectOutputStream2.writeObject(ad.expiration);
                            objectOutputStream2.writeObject(ad.acid);
                            objectOutputStream2.writeLong(ad.deferredViewStart);
                            objectOutputStream2.writeObject(ad);
                            ad.storedOnSdCard = isExternal[0];
                            try {
                                lockFile2.delete();
                                if (objectOutputStream2 != null) {
                                    objectOutputStream2.close();
                                }
                            } catch (IOException e) {
                            }
                            if (ad.saveAssets(context)) {
                                objectOutputStream = objectOutputStream2;
                                z = true;
                            } else {
                                ad.delete(context);
                                objectOutputStream = objectOutputStream2;
                            }
                        } catch (Exception e2) {
                            e = e2;
                            objectOutputStream = objectOutputStream2;
                            lockFile = lockFile2;
                            MMAdViewSDK.Log.e("There was a problem saving the cached ad %s.", ad.id);
                            MMAdViewSDK.Log.d(e.getMessage());
                            MMAdViewSDK.Log.d(e);
                            try {
                                lockFile.delete();
                                if (objectOutputStream != null) {
                                    objectOutputStream.close();
                                }
                            } catch (IOException e3) {
                            }
                        } catch (Throwable th) {
                            th = th;
                            objectOutputStream = objectOutputStream2;
                            lockFile = lockFile2;
                            try {
                                lockFile.delete();
                                if (objectOutputStream != null) {
                                    objectOutputStream.close();
                                }
                            } catch (IOException e4) {
                            }
                            throw th;
                        }
                    } else {
                        MMAdViewSDK.Log.v("Could not save the cached ad %s. Ad was locked.", ad.id);
                        try {
                            lockFile2.delete();
                            if (0 != 0) {
                                objectOutputStream.close();
                            }
                        } catch (IOException e5) {
                        }
                    }
                } catch (Exception e6) {
                    e = e6;
                    lockFile = lockFile2;
                } catch (Throwable th2) {
                    th = th2;
                    lockFile = lockFile2;
                }
            } catch (Exception e7) {
                e = e7;
            }
        }
        return z;
    }
}
