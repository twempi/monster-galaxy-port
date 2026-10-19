package com.millennialmedia.android;

import android.content.Context;
import android.net.Uri;
import android.os.StatFs;
import android.webkit.MimeTypeMap;
import com.sessionm.ui.SessionMActivity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.json.JSONArray;

/* JADX INFO: loaded from: classes.dex */
class MMFileManager extends MMJSObject {
    static final String CREATIVE_CACHE_DIR = "creativecache";
    private boolean isInitialized;
    private File root;

    MMFileManager() {
    }

    /* JADX WARN: Code duplicated, block: B:12:0x0035  */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:13:0x0037
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1478)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    static java.io.File getCreativeCacheDirectory(android.content.Context r4) {
        /*
            if (r4 == 0) goto L35
            java.io.File r0 = com.millennialmedia.android.AdCache.getCacheDirectory(r4)     // Catch: java.lang.Exception -> L37
            if (r0 == 0) goto L35
            java.io.File r1 = new java.io.File     // Catch: java.lang.Exception -> L37
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L37
            r2.<init>()     // Catch: java.lang.Exception -> L37
            java.lang.String r3 = "creativecache"
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Exception -> L37
            java.lang.String r3 = java.io.File.separator     // Catch: java.lang.Exception -> L37
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Exception -> L37
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Exception -> L37
            r1.<init>(r0, r2)     // Catch: java.lang.Exception -> L37
            boolean r2 = r1.exists()     // Catch: java.lang.Exception -> L37
            if (r2 != 0) goto L2f
            boolean r2 = r1.mkdirs()     // Catch: java.lang.Exception -> L37
            if (r2 == 0) goto L35
        L2e:
            return r1
        L2f:
            boolean r2 = r1.isDirectory()     // Catch: java.lang.Exception -> L37
            if (r2 != 0) goto L2e
        L35:
            r1 = 0
            goto L2e
        L37:
            r2 = move-exception
            goto L35
        */
        throw new UnsupportedOperationException("Method not decompiled: com.millennialmedia.android.MMFileManager.getCreativeCacheDirectory(android.content.Context):java.io.File");
    }

    private boolean initialize() {
        Context context;
        if (!this.isInitialized && (context = this.contextRef.get()) != null) {
            File creativeCacheDirectory = getCreativeCacheDirectory(context);
            this.root = creativeCacheDirectory;
            if (creativeCacheDirectory != null) {
                this.isInitialized = true;
            }
        }
        return this.isInitialized;
    }

    public MMJSResponse getFreeDiskSpace(HashMap<String, String> arguments) {
        if (!initialize()) {
            return null;
        }
        MMJSResponse response = new MMJSResponse();
        response.result = 1;
        StatFs stat = new StatFs(this.root.getAbsolutePath());
        response.response = new Long(((long) stat.getAvailableBlocks()) * ((long) stat.getBlockSize()));
        return response;
    }

    public MMJSResponse getDirectoryContents(HashMap<String, String> arguments) {
        File dir;
        if (!initialize()) {
            return null;
        }
        if (arguments.containsKey("path")) {
            dir = new File(this.root, arguments.get("path"));
        } else {
            dir = this.root;
        }
        JSONArray jsonArray = new JSONArray();
        String[] files = dir.list();
        for (String file : files) {
            jsonArray.put(file);
        }
        MMJSResponse response = new MMJSResponse();
        response.result = 1;
        response.response = jsonArray;
        return response;
    }

    public MMJSResponse getFileContents(HashMap<String, String> arguments) throws Throwable {
        byte[] contents;
        if (initialize()) {
            FileInputStream fileStream = null;
            if (arguments.containsKey("path")) {
                try {
                    File file = new File(this.root, arguments.get("path"));
                    FileInputStream fileStream2 = new FileInputStream(file);
                    try {
                        contents = new byte[(int) file.length()];
                        fileStream2.read(contents);
                        if (fileStream2 != null) {
                            try {
                                fileStream2.close();
                            } catch (Exception e) {
                            }
                        }
                    } catch (Exception e2) {
                        fileStream = fileStream2;
                        contents = null;
                        if (fileStream != null) {
                            try {
                                fileStream.close();
                            } catch (Exception e3) {
                            }
                        }
                    } catch (Throwable th) {
                        th = th;
                        fileStream = fileStream2;
                        if (fileStream != null) {
                            try {
                                fileStream.close();
                            } catch (Exception e4) {
                            }
                        }
                        throw th;
                    }
                } catch (Exception e5) {
                } catch (Throwable th2) {
                    th = th2;
                }
                if (contents != null) {
                    MMJSResponse response = new MMJSResponse();
                    response.result = 1;
                    response.dataResponse = contents;
                    return response;
                }
            }
        }
        return null;
    }

    public MMJSResponse writeData(HashMap<String, String> arguments) throws Throwable {
        if (initialize()) {
            FileOutputStream fileStream = null;
            boolean success = false;
            if (arguments.containsKey("path") && arguments.containsKey("data")) {
                try {
                    File file = new File(this.root, arguments.get("path"));
                    byte[] contents = Base64.decode(arguments.get("data"));
                    FileOutputStream fileStream2 = new FileOutputStream(file);
                    try {
                        fileStream2.write(contents);
                        success = true;
                        if (fileStream2 != null) {
                            try {
                                fileStream2.close();
                            } catch (Exception e) {
                            }
                        }
                    } catch (Exception e2) {
                        fileStream = fileStream2;
                        if (fileStream != null) {
                            try {
                                fileStream.close();
                            } catch (Exception e3) {
                            }
                        }
                    } catch (Throwable th) {
                        th = th;
                        fileStream = fileStream2;
                        if (fileStream != null) {
                            try {
                                fileStream.close();
                            } catch (Exception e4) {
                            }
                        }
                        throw th;
                    }
                } catch (Exception e5) {
                } catch (Throwable th2) {
                    th = th2;
                }
                if (success) {
                    return MMJSResponse.responseWithSuccess();
                }
            }
        }
        return null;
    }

    /* JADX WARN: Code duplicated, block: B:21:0x006a A[Catch: Exception -> 0x00b2, TRY_LEAVE, TryCatch #5 {Exception -> 0x00b2, blocks: (B:19:0x0065, B:21:0x006a), top: B:58:0x0065 }] */
    /* JADX WARN: Code duplicated, block: B:43:0x00a7 A[Catch: Exception -> 0x00ad, TRY_LEAVE, TryCatch #0 {Exception -> 0x00ad, blocks: (B:41:0x00a2, B:43:0x00a7), top: B:54:0x00a2 }] */
    public MMJSResponse downloadFile(HashMap<String, String> arguments) throws Throwable {
        if (initialize()) {
            InputStream inStream = null;
            FileOutputStream fileStream = null;
            boolean success = false;
            HttpURLConnection urlConnection = null;
            if (arguments.containsKey(SessionMActivity.INTENT_PARAM_URL)) {
                try {
                    URL url = new URL(arguments.get(SessionMActivity.INTENT_PARAM_URL));
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setConnectTimeout(30000);
                    inStream = urlConnection.getInputStream();
                    byte[] contents = new byte[Opcodes.ACC_SYNTHETIC];
                    String path = arguments.containsKey("path") ? arguments.get("path") : Uri.parse(arguments.get(SessionMActivity.INTENT_PARAM_URL)).getLastPathSegment();
                    File file = new File(this.root, path);
                    FileOutputStream fileStream2 = new FileOutputStream(file);
                    while (true) {
                        try {
                            int read = inStream.read(contents);
                            if (read <= 0) {
                                break;
                            }
                            fileStream2.write(contents, 0, read);
                        } catch (Exception e) {
                            fileStream = fileStream2;
                            if (urlConnection != null) {
                                urlConnection.disconnect();
                            }
                            if (inStream != null) {
                                try {
                                    inStream.close();
                                    if (fileStream != null) {
                                        fileStream.close();
                                    }
                                } catch (Exception e2) {
                                }
                            } else if (fileStream != null) {
                                fileStream.close();
                            }
                        } catch (Throwable th) {
                            th = th;
                            fileStream = fileStream2;
                            if (urlConnection != null) {
                                urlConnection.disconnect();
                            }
                            if (inStream != null) {
                                try {
                                    inStream.close();
                                    if (fileStream != null) {
                                        fileStream.close();
                                    }
                                } catch (Exception e3) {
                                    throw th;
                                }
                            } else if (fileStream != null) {
                                fileStream.close();
                            }
                            throw th;
                        }
                    }
                    success = true;
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (inStream != null) {
                        try {
                            inStream.close();
                        } catch (Exception e4) {
                        }
                    }
                    if (fileStream2 != null) {
                        fileStream2.close();
                    }
                } catch (Exception e5) {
                } catch (Throwable th2) {
                    th = th2;
                }
                if (success) {
                    return MMJSResponse.responseWithSuccess();
                }
            }
        }
        return null;
    }

    public MMJSResponse moveFile(HashMap<String, String> arguments) {
        if (initialize()) {
            try {
                String fromPath = arguments.get("fromPath");
                String toPath = arguments.get("toPath");
                if (fromPath != null && toPath != null) {
                    File fromFile = new File(this.root, fromPath);
                    File toFile = new File(this.root, toPath);
                    if (fromFile.renameTo(toFile)) {
                        return MMJSResponse.responseWithSuccess();
                    }
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    public MMJSResponse removeAtPath(HashMap<String, String> arguments) {
        if (initialize()) {
            try {
                String path = arguments.get("path");
                if (path != null) {
                    File file = new File(this.root, path);
                    if (file.delete()) {
                        return MMJSResponse.responseWithSuccess();
                    }
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    public MMJSResponse getMimeType(HashMap<String, String> arguments) {
        if (initialize()) {
            String path = arguments.get("path");
            String[] components = path.split("\\.");
            String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(components[components.length - 1]);
            if (mime != null) {
                MMJSResponse response = new MMJSResponse();
                response.result = 1;
                response.response = mime;
                return response;
            }
        }
        return null;
    }

    public MMJSResponse cleanupCache(HashMap<String, String> arguments) {
        boolean clear = false;
        long timeout = 259200000;
        if (initialize()) {
            if (arguments.containsKey("clear")) {
                clear = Boolean.parseBoolean(arguments.get("clear"));
            }
            if (clear) {
                timeout = 0;
            } else {
                Context context = this.contextRef.get();
                if (context != null) {
                    HandShake handShake = HandShake.sharedHandShake(context);
                    timeout = handShake.creativeCacheTimeout;
                }
            }
            try {
                cleanupDirectory(this.root, timeout);
                return null;
            } catch (SecurityException e) {
                return null;
            }
        }
        return null;
    }

    private static void cleanupDirectory(File file, long timeout) throws SecurityException {
        File[] entries = file.listFiles();
        for (File entry : entries) {
            if (entry.isFile()) {
                if (System.currentTimeMillis() - entry.lastModified() > timeout) {
                    entry.delete();
                }
            } else if (entry.isDirectory()) {
                try {
                    cleanupDirectory(entry, timeout);
                    if (entry.list().length == 0) {
                        entry.delete();
                    }
                } catch (SecurityException e) {
                }
            }
        }
    }

    static void cleanupCache(Context context, boolean clear) {
        long timeout;
        try {
            File cacheDir = AdCache.getCacheDirectory(context);
            if (cacheDir != null) {
                File root = new File(cacheDir, CREATIVE_CACHE_DIR);
                if (root.exists() && root.isDirectory()) {
                    if (clear) {
                        timeout = 0;
                    } else {
                        try {
                            HandShake handShake = HandShake.sharedHandShake(context);
                            timeout = handShake.creativeCacheTimeout;
                        } catch (SecurityException e) {
                            return;
                        }
                    }
                    cleanupDirectory(root, timeout);
                }
            }
        } catch (Exception e2) {
        }
    }
}
