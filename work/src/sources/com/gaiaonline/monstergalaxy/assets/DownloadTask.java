package com.gaiaonline.monstergalaxy.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.gaiaonline.monstergalaxy.app.ConfigConstants;
import com.sessionm.integralblue.httpresponsecache.compat.libcore.net.http.HttpEngine;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes.dex */
public class DownloadTask implements Callable<Boolean> {
    public static final int PRIORITY_NORMAL = 2;
    public static final int PRIORITY_NOW = 1;
    private static final String TEMP_SUFFIX = ".part";
    private Asset asset;
    private int priority;

    public DownloadTask(Asset asset, int priority) {
        this.asset = asset;
        this.priority = priority;
    }

    /* JADX INFO: Removed unreachable split cross block B:132:0x004c */
    /* JADX INFO: Removed unreachable split cross block B:142:0x004c */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.concurrent.Callable
    public Boolean call() throws Exception {
        Boolean bool;
        if (this.asset == null) {
            throw new RuntimeException("Internal error: null file to download");
        }
        Gdx.app.log("DownloadTask", "Asset: " + this.asset.getFilename());
        if (assetExists()) {
            Gdx.app.log("DownloadTask", "Asset already downloaded: " + this.asset.getFilename());
            return Boolean.TRUE;
        }
        OutputStream out = null;
        InputStream in = null;
        try {
            try {
                try {
                    URL url = new URL(String.valueOf(ConfigConstants.ASSETS_DOWNLOAD_URL) + this.asset.getFilename() + "." + this.asset.getExtension());
                    Gdx.app.log("DownloadTask", "Downloading " + url.toString());
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod(HttpEngine.GET);
                    urlConnection.setDoOutput(true);
                    urlConnection.connect();
                    if (Gdx.files.isExternalStorageAvailable()) {
                        Gdx.files.external(this.asset.getFolderPath()).mkdirs();
                        out = Gdx.files.external(String.valueOf(this.asset.getCompletePath()) + TEMP_SUFFIX).write(false);
                        in = urlConnection.getInputStream();
                        if (Thread.interrupted()) {
                            bool = Boolean.FALSE;
                            if (in != null) {
                                try {
                                    in.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (out != null) {
                                try {
                                    out.close();
                                } catch (IOException e2) {
                                    e2.printStackTrace();
                                }
                            }
                        } else {
                            byte[] buffer = new byte[1024];
                            do {
                                int bufferLength = in.read(buffer);
                                if (bufferLength <= 0) {
                                    Gdx.files.external(String.valueOf(this.asset.getCompletePath()) + TEMP_SUFFIX).moveTo(Gdx.files.external(this.asset.getCompletePath()));
                                    if (in != null) {
                                        try {
                                            in.close();
                                        } catch (IOException e3) {
                                            e3.printStackTrace();
                                        }
                                    }
                                    if (out != null) {
                                        try {
                                            out.close();
                                        } catch (IOException e4) {
                                            e4.printStackTrace();
                                        }
                                    }
                                    Gdx.app.log("DownloadTask", "Download finished");
                                    bool = Boolean.TRUE;
                                } else {
                                    out.write(buffer, 0, bufferLength);
                                }
                            } while (!Thread.interrupted());
                            bool = Boolean.FALSE;
                            if (in != null) {
                                try {
                                    in.close();
                                } catch (IOException e5) {
                                    e5.printStackTrace();
                                }
                            }
                            if (out != null) {
                                try {
                                    out.close();
                                } catch (IOException e6) {
                                    e6.printStackTrace();
                                }
                            }
                        }
                    } else {
                        Gdx.app.log("DownloadTask", "No external storage available");
                        bool = Boolean.FALSE;
                        if (0 != 0) {
                            try {
                                in.close();
                            } catch (IOException e7) {
                                e7.printStackTrace();
                            }
                        }
                        if (0 != 0) {
                            try {
                                out.close();
                            } catch (IOException e8) {
                                e8.printStackTrace();
                            }
                        }
                    }
                } catch (Throwable th) {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e9) {
                            e9.printStackTrace();
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e10) {
                            e10.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (GdxRuntimeException e11) {
                e11.printStackTrace();
                bool = Boolean.FALSE;
                if (0 != 0) {
                    try {
                        in.close();
                    } catch (IOException e12) {
                        e12.printStackTrace();
                    }
                }
                if (0 != 0) {
                    try {
                        out.close();
                    } catch (IOException e13) {
                        e13.printStackTrace();
                    }
                }
            }
        } catch (MalformedURLException e14) {
            e14.printStackTrace();
            bool = Boolean.FALSE;
            if (0 != 0) {
                try {
                    in.close();
                } catch (IOException e15) {
                    e15.printStackTrace();
                }
            }
            if (0 != 0) {
                try {
                    out.close();
                } catch (IOException e16) {
                    e16.printStackTrace();
                }
            }
        } catch (IOException e17) {
            e17.printStackTrace();
            bool = Boolean.FALSE;
            if (0 != 0) {
                try {
                    in.close();
                } catch (IOException e18) {
                    e18.printStackTrace();
                }
            }
            if (0 != 0) {
                try {
                    out.close();
                } catch (IOException e19) {
                    e19.printStackTrace();
                }
            }
        }
        return bool;
    }

    public int getPriority() {
        return this.priority;
    }

    private boolean assetExists() {
        return Gdx.files.internal(this.asset.getCompletePath()).exists() || Gdx.files.external(this.asset.getCompletePath()).exists();
    }
}
