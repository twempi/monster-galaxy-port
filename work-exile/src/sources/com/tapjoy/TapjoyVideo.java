package com.tapjoy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import oauth.signpost.OAuth;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class TapjoyVideo {
    private static final String BACKGROUND_FILE_NAME = "background";
    private static final String BACKGROUND_PLACEHOLDER_NAME = "default";
    private static final String BACKGROUND_WATERMARK_NAME = "watermark";
    public static final String TAPJOY_VIDEO = "TapjoyVideo";
    public static String imageBackgroundLocation = null;
    private static final String imageBackgroundUrl = "https://s3.amazonaws.com/tapjoy/videos/assets/background.png";
    public static String imagePlaceholderLocation = null;
    private static final String imagePlaceholderUrl = "https://s3.amazonaws.com/tapjoy/videos/assets/default.png";
    public static String imageTapjoyLocation = null;
    private static final String imageTapjoyUrl = "https://s3.amazonaws.com/tapjoy/videos/assets/watermark.png";
    private static TapjoyVideo tapjoyVideo = null;
    private static TapjoyVideoNotifier tapjoyVideoNotifier;
    private Hashtable<String, TapjoyVideoObject> cachedVideos;
    Context context;
    private Vector<String> imageQueue;
    private Hashtable<String, String> uncachedImages;
    private Hashtable<String, TapjoyVideoObject> uncachedVideos;
    private Vector<String> videoQueue;
    public TapjoyVideoObject videoToPlay;
    private String videoCacheDir = null;
    private String imageCacheDir = null;
    private int videoCacheLimit = 5;

    public TapjoyVideo(Context applicationContext) {
        this.context = applicationContext;
        tapjoyVideo = this;
    }

    public static TapjoyVideo getInstance() {
        return tapjoyVideo;
    }

    public void setVideoCacheCount(int count) {
        this.videoCacheLimit = count;
    }

    public void initVideoAd(TapjoyVideoNotifier notifier) {
        TapjoyLog.i(TAPJOY_VIDEO, "initVideoAd");
        if (notifier == null) {
            Log.e(TAPJOY_VIDEO, "Error during initVideoAd -- TapjoyVideoNotifier is null");
            return;
        }
        tapjoyVideoNotifier = notifier;
        this.videoCacheDir = String.valueOf(Environment.getExternalStorageDirectory().toString()) + "/tjcache/data/";
        this.imageCacheDir = String.valueOf(Environment.getExternalStorageDirectory().toString()) + "/tjcache/tmp/";
        TapjoyUtil.deleteFileOrDirectory(new File(String.valueOf(Environment.getExternalStorageDirectory().toString()) + "/tapjoy/"));
        this.videoQueue = new Vector<>();
        this.uncachedVideos = new Hashtable<>();
        this.cachedVideos = new Hashtable<>();
        this.imageQueue = new Vector<>();
        this.imageQueue.addElement(imageBackgroundUrl);
        this.imageQueue.addElement(imagePlaceholderUrl);
        this.imageQueue.addElement(imageTapjoyUrl);
        this.uncachedImages = new Hashtable<>();
        this.uncachedImages.put(BACKGROUND_FILE_NAME, imageBackgroundUrl);
        this.uncachedImages.put(BACKGROUND_PLACEHOLDER_NAME, imagePlaceholderUrl);
        this.uncachedImages.put(BACKGROUND_WATERMARK_NAME, imageTapjoyUrl);
        imageBackgroundLocation = String.valueOf(this.imageCacheDir) + BACKGROUND_FILE_NAME;
        imagePlaceholderLocation = String.valueOf(this.imageCacheDir) + BACKGROUND_PLACEHOLDER_NAME;
        imageTapjoyLocation = String.valueOf(this.imageCacheDir) + BACKGROUND_WATERMARK_NAME;
        setVideoIDs();
        String state = Environment.getExternalStorageState();
        if (!"mounted".equals(state)) {
            TapjoyLog.i(TAPJOY_VIDEO, "Media storage unavailable.");
            tapjoyVideoNotifier.videoError(1);
        } else {
            new Thread(new Runnable() { // from class: com.tapjoy.TapjoyVideo.1
                @Override // java.lang.Runnable
                public void run() {
                    boolean returnValue = false;
                    String urlParams = TapjoyConnectCore.getURLParams();
                    String result = new TapjoyURLConnection().connectToURL("https://ws.tapjoyads.com/videos?", String.valueOf(urlParams) + "&publisher_user_id=" + TapjoyConnectCore.getUserID());
                    if (result != null && result.length() > 0) {
                        returnValue = TapjoyVideo.this.handleGetVideosResponse(result);
                    }
                    if (returnValue) {
                        File[] cachedFilesOnDisk = new File(TapjoyVideo.this.videoCacheDir).listFiles();
                        if (cachedFilesOnDisk != null) {
                            for (int i = 0; i < cachedFilesOnDisk.length; i++) {
                                String key = cachedFilesOnDisk[i].getName();
                                TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "file[" + i + "]: " + cachedFilesOnDisk[i].getAbsolutePath() + " --- " + cachedFilesOnDisk[i].getName());
                                TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "key: " + key + ", " + key);
                                TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "uncachedVideos: " + TapjoyVideo.this.uncachedVideos.keySet().toString());
                                if (TapjoyVideo.this.uncachedVideos.containsKey(key)) {
                                    TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "*** Match found, comparing file sizes... ***");
                                    TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "local file size: " + cachedFilesOnDisk[i].length());
                                    TapjoyVideoObject videoObject = (TapjoyVideoObject) TapjoyVideo.this.uncachedVideos.get(key);
                                    String contentLength = new TapjoyURLConnection().getContentLength(videoObject.videoURL);
                                    if (contentLength != null && Integer.parseInt(contentLength) == cachedFilesOnDisk[i].length()) {
                                        TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "*** VIDEO ALREADY DOWNLOAD!!! " + key + " ***");
                                        videoObject.dataLocation = cachedFilesOnDisk[i].getAbsolutePath();
                                        TapjoyVideo.this.cachedVideos.put(key, (TapjoyVideoObject) TapjoyVideo.this.uncachedVideos.get(key));
                                        TapjoyVideo.this.uncachedVideos.remove(key);
                                        TapjoyVideo.this.videoQueue.remove(key);
                                        TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "setting video location to: " + videoObject.dataLocation);
                                    } else {
                                        TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "file size mismatch --- deleting video: " + cachedFilesOnDisk[i].getAbsolutePath());
                                        TapjoyUtil.deleteFileOrDirectory(cachedFilesOnDisk[i]);
                                    }
                                } else {
                                    TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "VIDEO EXPIRED? removing video from cache: " + key + " --- " + cachedFilesOnDisk[i].getAbsolutePath());
                                    TapjoyUtil.deleteFileOrDirectory(cachedFilesOnDisk[i]);
                                }
                            }
                        }
                        TapjoyVideo.this.setVideoIDs();
                        if (TapjoyVideo.this.cachedVideos.size() > 0) {
                            TapjoyVideo.tapjoyVideoNotifier.videoReady();
                        }
                        TapjoyVideo.this.loadNextVideo();
                        File[] cachedImagesOnDisk = new File(TapjoyVideo.this.imageCacheDir).listFiles();
                        TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "------------------------------");
                        TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "checking image cache...");
                        if (cachedImagesOnDisk != null) {
                            for (int i2 = 0; i2 < cachedImagesOnDisk.length; i2++) {
                                String key2 = cachedImagesOnDisk[i2].getName();
                                TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "file[" + i2 + "]: " + cachedImagesOnDisk[i2].getAbsolutePath() + " --- " + cachedImagesOnDisk[i2].getName());
                                TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "key: " + key2);
                                TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "uncachedVideos: " + TapjoyVideo.this.uncachedVideos.keySet().toString());
                                if (TapjoyVideo.this.uncachedImages.containsKey(key2)) {
                                    TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "*** Match found, comparing file sizes... ***");
                                    TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "local file size: " + cachedImagesOnDisk[i2].length());
                                    String contentLength2 = new TapjoyURLConnection().getContentLength((String) TapjoyVideo.this.uncachedImages.get(key2));
                                    if (contentLength2 != null && Integer.parseInt(contentLength2) == cachedImagesOnDisk[i2].length()) {
                                        TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "*** image already downloaded, removing from queue: " + ((String) TapjoyVideo.this.uncachedImages.get(key2)));
                                        TapjoyVideo.this.imageQueue.remove(TapjoyVideo.this.uncachedImages.get(key2));
                                    } else {
                                        TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "file size mismatch --- deleting image");
                                        TapjoyUtil.deleteFileOrDirectory(cachedImagesOnDisk[i2]);
                                    }
                                } else {
                                    TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "invalid image");
                                    TapjoyUtil.deleteFileOrDirectory(cachedImagesOnDisk[i2]);
                                }
                            }
                        }
                        if (TapjoyVideo.this.uncachedVideos.size() != 0 || TapjoyVideo.this.cachedVideos.size() != 0) {
                            if (TapjoyVideo.this.imageQueue.size() > 0) {
                                TapjoyVideo.this.cacheImage((String) TapjoyVideo.this.imageQueue.elementAt(0));
                            }
                        } else {
                            TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "*** no videos returned ***");
                            TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "*** delete cache directory ***");
                            TapjoyUtil.deleteFileOrDirectory(new File(String.valueOf(Environment.getExternalStorageDirectory().toString()) + "/tjcache/"));
                        }
                        TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "------------------------------");
                        TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "------------------------------");
                        TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "INIT DONE!");
                        TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "------------------------------");
                    }
                }
            }).start();
            TapjoyConnectCore.setVideoEnabled(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean handleGetVideosResponse(String response) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        TapjoyLog.i(TAPJOY_VIDEO, "========================================");
        try {
            InputStream is = new ByteArrayInputStream(response.getBytes(OAuth.ENCODING));
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(is);
            document.getDocumentElement().normalize();
            NodeList nodelistParent = document.getElementsByTagName("TapjoyVideos");
            NodeList nodelist = nodelistParent.item(0).getChildNodes();
            TapjoyLog.i(TAPJOY_VIDEO, "nodelistParent length: " + nodelistParent.getLength());
            TapjoyLog.i(TAPJOY_VIDEO, "nodelist length: " + nodelist.getLength());
            for (int i = 0; i < nodelist.getLength(); i++) {
                Node node = nodelist.item(i);
                TapjoyVideoObject videoObject = new TapjoyVideoObject();
                if (node != null && node.getNodeType() == 1) {
                    Element element = (Element) node;
                    String value = TapjoyUtil.getNodeTrimValue(element.getElementsByTagName("ClickURL"));
                    if (value != null && !value.equals("")) {
                        videoObject.clickURL = value;
                    }
                    String value2 = TapjoyUtil.getNodeTrimValue(element.getElementsByTagName("OfferID"));
                    if (value2 != null && !value2.equals("")) {
                        videoObject.offerID = value2;
                    }
                    String value3 = TapjoyUtil.getNodeTrimValue(element.getElementsByTagName("Name"));
                    if (value3 != null && !value3.equals("")) {
                        videoObject.videoAdName = value3;
                    }
                    String value4 = TapjoyUtil.getNodeTrimValue(element.getElementsByTagName("Amount"));
                    if (value4 != null && !value4.equals("")) {
                        videoObject.currencyAmount = value4;
                    }
                    String value5 = TapjoyUtil.getNodeTrimValue(element.getElementsByTagName("CurrencyName"));
                    if (value5 != null && !value5.equals("")) {
                        videoObject.currencyName = value5;
                    }
                    String value6 = TapjoyUtil.getNodeTrimValue(element.getElementsByTagName("VideoURL"));
                    if (value6 != null && !value6.equals("")) {
                        videoObject.videoURL = value6;
                    }
                    String value7 = TapjoyUtil.getNodeTrimValue(element.getElementsByTagName("IconURL"));
                    if (value7 != null && !value7.equals("")) {
                        videoObject.iconURL = value7;
                    }
                    TapjoyLog.i(TAPJOY_VIDEO, "-----");
                    TapjoyLog.i(TAPJOY_VIDEO, "videoObject.clickURL: " + videoObject.clickURL);
                    TapjoyLog.i(TAPJOY_VIDEO, "videoObject.offerID: " + videoObject.offerID);
                    TapjoyLog.i(TAPJOY_VIDEO, "videoObject.videoAdName: " + videoObject.videoAdName);
                    TapjoyLog.i(TAPJOY_VIDEO, "videoObject.currencyAmount: " + videoObject.currencyAmount);
                    TapjoyLog.i(TAPJOY_VIDEO, "videoObject.currencyName: " + videoObject.currencyName);
                    TapjoyLog.i(TAPJOY_VIDEO, "videoObject.videoURL: " + videoObject.videoURL);
                    TapjoyLog.i(TAPJOY_VIDEO, "videoObject.iconURL: " + videoObject.iconURL);
                    NodeList buttonData = element.getElementsByTagName("Buttons");
                    NodeList itemNodeList = buttonData.item(0).getChildNodes();
                    for (int j = 0; j < itemNodeList.getLength(); j++) {
                        NodeList child = itemNodeList.item(j).getChildNodes();
                        if (child.getLength() != 0) {
                            String name = child.item(0).getFirstChild().getNodeValue();
                            TapjoyLog.i(TAPJOY_VIDEO, "name: " + name);
                            String url = child.item(1).getFirstChild().getNodeValue();
                            TapjoyLog.i(TAPJOY_VIDEO, "url: " + url);
                            videoObject.addButton(name, url);
                        }
                    }
                    this.videoQueue.addElement(videoObject.offerID);
                    this.uncachedVideos.put(videoObject.offerID, videoObject);
                    setVideoIDs();
                }
            }
            TapjoyLog.i(TAPJOY_VIDEO, "========================================");
            return true;
        } catch (Exception e) {
            TapjoyLog.e(TAPJOY_VIDEO, "Error parsing XML: " + e.toString());
            return false;
        }
    }

    public TapjoyVideoObject getCurrentVideoData() {
        return this.videoToPlay;
    }

    public boolean startVideo(String videoID, String currencyName, String currencyAmount, String clickURL) {
        File video;
        boolean cachedVideo = true;
        TapjoyLog.i(TAPJOY_VIDEO, "Starting video activity with video: " + videoID);
        this.videoToPlay = this.cachedVideos.get(videoID);
        String state = Environment.getExternalStorageState();
        if (!"mounted".equals(state)) {
            TapjoyLog.e(TAPJOY_VIDEO, "Cannot access external storage");
            tapjoyVideoNotifier.videoError(1);
            return false;
        }
        if (this.videoToPlay == null) {
            TapjoyLog.i(TAPJOY_VIDEO, "video not cached... checking uncached videos");
            this.videoToPlay = this.uncachedVideos.get(videoID);
            if (this.videoToPlay == null) {
                TapjoyLog.e(TAPJOY_VIDEO, "null video object? aborting.");
                return false;
            }
            cachedVideo = false;
        }
        this.videoToPlay.currencyName = currencyName;
        this.videoToPlay.currencyAmount = currencyAmount;
        this.videoToPlay.clickURL = clickURL;
        TapjoyLog.i(TAPJOY_VIDEO, "videoToPlay: " + this.videoToPlay.offerID);
        TapjoyLog.i(TAPJOY_VIDEO, "amount: " + this.videoToPlay.currencyAmount);
        TapjoyLog.i(TAPJOY_VIDEO, "currency: " + this.videoToPlay.currencyName);
        TapjoyLog.i(TAPJOY_VIDEO, "clickURL: " + this.videoToPlay.clickURL);
        TapjoyLog.i(TAPJOY_VIDEO, "location: " + this.videoToPlay.dataLocation);
        if (cachedVideo && this.videoToPlay.dataLocation != null && ((video = new File(this.videoToPlay.dataLocation)) == null || !video.exists())) {
            TapjoyLog.e(TAPJOY_VIDEO, "video file does not exist.");
            return false;
        }
        Intent videoIntent = new Intent(this.context, (Class<?>) TapjoyVideoView.class);
        videoIntent.setFlags(268435456);
        videoIntent.putExtra(TapjoyConstants.EXTRA_VIDEO_PATH, videoID);
        this.context.startActivity(videoIntent);
        return true;
    }

    public void loadNextVideo() {
        TapjoyLog.i(TAPJOY_VIDEO, "++++++++++++++++++++++++++++++++++++++++");
        TapjoyLog.i(TAPJOY_VIDEO, "LOAD NEXT VIDEO");
        TapjoyLog.i(TAPJOY_VIDEO, "videoQueue size: " + this.videoQueue.size());
        TapjoyLog.i(TAPJOY_VIDEO, "uncachedVideos size: " + this.uncachedVideos.size());
        TapjoyLog.i(TAPJOY_VIDEO, "cachedVideos size: " + this.cachedVideos.size());
        if (this.cachedVideos.size() < this.videoCacheLimit && this.videoQueue.size() > 0) {
            String url = this.uncachedVideos.get(this.videoQueue.elementAt(0)).videoURL;
            cacheVideo(url);
        }
        TapjoyLog.i(TAPJOY_VIDEO, "++++++++++++++++++++++++++++++++++++++++");
    }

    public static TapjoyVideoNotifier getVideoNotifier() {
        return tapjoyVideoNotifier;
    }

    private void cacheVideo(final String url) {
        TapjoyLog.i(TAPJOY_VIDEO, "download and cache video from: " + url);
        new Thread(new Runnable() { // from class: com.tapjoy.TapjoyVideo.2
            @Override // java.lang.Runnable
            public void run() {
                long time = System.currentTimeMillis();
                boolean networkTimeout = false;
                boolean downloadError = false;
                BufferedInputStream inputStream = null;
                OutputStream out = null;
                File savedFile = null;
                try {
                    URL fileURL = new URL(url);
                    URLConnection connection = fileURL.openConnection();
                    connection.setConnectTimeout(15000);
                    connection.setReadTimeout(30000);
                    connection.connect();
                    BufferedInputStream inputStream2 = new BufferedInputStream(connection.getInputStream());
                    try {
                        File fileDir = new File(TapjoyVideo.this.videoCacheDir);
                        String path = url.substring(0, url.lastIndexOf("/") + 1);
                        String fileName = url.substring(url.lastIndexOf("/") + 1);
                        String fileName2 = fileName.substring(0, fileName.indexOf(46));
                        TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "fileDir: " + fileDir);
                        TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "path: " + path);
                        TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "file name: " + fileName2);
                        if (fileDir.mkdirs()) {
                            TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "created directory at: " + fileDir.getPath());
                        }
                        File savedFile2 = new File(TapjoyVideo.this.videoCacheDir, fileName2);
                        try {
                            OutputStream out2 = new FileOutputStream(savedFile2);
                            try {
                                TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "downloading video file to: " + savedFile2.toString());
                                byte[] buf = new byte[1024];
                                long total = 0;
                                while (true) {
                                    int len = inputStream2.read(buf);
                                    if (len == -1) {
                                        break;
                                    }
                                    total += (long) len;
                                    out2.write(buf, 0, len);
                                }
                                out2.close();
                                inputStream2.close();
                                TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "FILE SIZE: " + savedFile2.length());
                                if (savedFile2.length() == 0) {
                                    networkTimeout = true;
                                    savedFile = savedFile2;
                                    out = out2;
                                    inputStream = inputStream2;
                                } else {
                                    savedFile = savedFile2;
                                    out = out2;
                                    inputStream = inputStream2;
                                }
                            } catch (SocketTimeoutException e) {
                                e = e;
                                savedFile = savedFile2;
                                out = out2;
                                inputStream = inputStream2;
                                TapjoyLog.e(TapjoyVideo.TAPJOY_VIDEO, "Network timeout: " + e.toString());
                                networkTimeout = true;
                                downloadError = true;
                            } catch (Exception e2) {
                                e = e2;
                                savedFile = savedFile2;
                                out = out2;
                                inputStream = inputStream2;
                                TapjoyLog.e(TapjoyVideo.TAPJOY_VIDEO, "Error caching video file: " + e.toString());
                                downloadError = true;
                            }
                        } catch (SocketTimeoutException e3) {
                            e = e3;
                            savedFile = savedFile2;
                            inputStream = inputStream2;
                        } catch (Exception e4) {
                            e = e4;
                            savedFile = savedFile2;
                            inputStream = inputStream2;
                        }
                    } catch (SocketTimeoutException e5) {
                        e = e5;
                        inputStream = inputStream2;
                    } catch (Exception e6) {
                        e = e6;
                        inputStream = inputStream2;
                    }
                } catch (SocketTimeoutException e7) {
                    e = e7;
                } catch (Exception e8) {
                    e = e8;
                }
                if (networkTimeout) {
                    TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "Network timeout");
                    try {
                        inputStream.close();
                        out.close();
                    } catch (Exception e9) {
                    }
                }
                if (networkTimeout || downloadError) {
                    TapjoyVideo.tapjoyVideoNotifier.videoError(2);
                    return;
                }
                String key = (String) TapjoyVideo.this.videoQueue.elementAt(0);
                TapjoyVideoObject newVideo = (TapjoyVideoObject) TapjoyVideo.this.uncachedVideos.get(key);
                newVideo.dataLocation = savedFile.getAbsolutePath();
                SharedPreferences settings = TapjoyVideo.this.context.getSharedPreferences(TapjoyConstants.TJC_VIDEOS_PREFERENCE, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(key, key);
                editor.commit();
                TapjoyVideo.this.cachedVideos.put(key, newVideo);
                TapjoyVideo.this.uncachedVideos.remove(key);
                TapjoyVideo.this.videoQueue.removeElementAt(0);
                TapjoyVideo.this.setVideoIDs();
                TapjoyVideo.this.loadNextVideo();
                TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "video cached in: " + (System.currentTimeMillis() - time) + "ms");
                TapjoyVideo.tapjoyVideoNotifier.videoReady();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cacheImage(final String url) {
        TapjoyLog.i(TAPJOY_VIDEO, "cacheImage: " + url);
        new Thread(new Runnable() { // from class: com.tapjoy.TapjoyVideo.3
            @Override // java.lang.Runnable
            public void run() {
                long time = System.currentTimeMillis();
                boolean networkTimeout = false;
                boolean downloadError = false;
                BufferedInputStream inputStream = null;
                OutputStream out = null;
                try {
                    URL fileURL = new URL(url);
                    URLConnection connection = fileURL.openConnection();
                    connection.setConnectTimeout(15000);
                    connection.setReadTimeout(30000);
                    connection.connect();
                    BufferedInputStream inputStream2 = new BufferedInputStream(connection.getInputStream());
                    try {
                        File fileDir = new File(TapjoyVideo.this.imageCacheDir);
                        String path = url.substring(0, url.lastIndexOf("/") + 1);
                        String fileName = url.substring(url.lastIndexOf("/") + 1);
                        String fileName2 = fileName.substring(0, fileName.indexOf(46));
                        TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "fileDir: " + fileDir);
                        TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "path: " + path);
                        TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "file name: " + fileName2);
                        if (fileDir.mkdirs()) {
                            TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "created directory at: " + fileDir.getPath());
                        }
                        File savedFile = new File(TapjoyVideo.this.imageCacheDir, fileName2);
                        try {
                            OutputStream out2 = new FileOutputStream(savedFile);
                            try {
                                TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "downloading image file to: " + savedFile.toString());
                                byte[] buf = new byte[1024];
                                long total = 0;
                                while (true) {
                                    int len = inputStream2.read(buf);
                                    if (len == -1) {
                                        break;
                                    }
                                    total += (long) len;
                                    out2.write(buf, 0, len);
                                }
                                out2.close();
                                inputStream2.close();
                                TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "FILE SIZE: " + savedFile.length());
                                if (savedFile.length() == 0) {
                                    networkTimeout = true;
                                    out = out2;
                                    inputStream = inputStream2;
                                } else {
                                    out = out2;
                                    inputStream = inputStream2;
                                }
                            } catch (SocketTimeoutException e) {
                                e = e;
                                out = out2;
                                inputStream = inputStream2;
                                TapjoyLog.e(TapjoyVideo.TAPJOY_VIDEO, "Network timeout: " + e.toString());
                                networkTimeout = true;
                                downloadError = true;
                            } catch (Exception e2) {
                                e = e2;
                                out = out2;
                                inputStream = inputStream2;
                                TapjoyLog.e(TapjoyVideo.TAPJOY_VIDEO, "Error caching image file: " + e.toString());
                                downloadError = true;
                            }
                        } catch (SocketTimeoutException e3) {
                            e = e3;
                            inputStream = inputStream2;
                        } catch (Exception e4) {
                            e = e4;
                            inputStream = inputStream2;
                        }
                    } catch (SocketTimeoutException e5) {
                        e = e5;
                        inputStream = inputStream2;
                    } catch (Exception e6) {
                        e = e6;
                        inputStream = inputStream2;
                    }
                } catch (SocketTimeoutException e7) {
                    e = e7;
                } catch (Exception e8) {
                    e = e8;
                }
                if (networkTimeout) {
                    TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "Network timeout");
                    try {
                        inputStream.close();
                        out.close();
                    } catch (Exception e9) {
                    }
                }
                if (networkTimeout || downloadError) {
                    if (TapjoyVideo.this.imageQueue.size() > 0) {
                        TapjoyVideo.this.cacheImage((String) TapjoyVideo.this.imageQueue.elementAt(0));
                    }
                } else {
                    TapjoyVideo.this.imageQueue.removeElementAt(0);
                    if (TapjoyVideo.this.imageQueue.size() > 0) {
                        TapjoyVideo.this.cacheImage((String) TapjoyVideo.this.imageQueue.elementAt(0));
                    }
                    TapjoyLog.i(TapjoyVideo.TAPJOY_VIDEO, "image cached in: " + (System.currentTimeMillis() - time) + "ms");
                }
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setVideoIDs() {
        String videoIDs = "";
        if (this.cachedVideos != null && this.cachedVideos.size() > 0) {
            Enumeration<String> keys = this.cachedVideos.keys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                videoIDs = String.valueOf(videoIDs) + key;
                if (keys.hasMoreElements()) {
                    videoIDs = String.valueOf(videoIDs) + ",";
                }
            }
            TapjoyLog.i(TAPJOY_VIDEO, "cachedVideos size: " + this.cachedVideos.size());
        }
        TapjoyLog.i(TAPJOY_VIDEO, "videoIDs: [" + videoIDs + "]");
        TapjoyConnectCore.setVideoIDs(videoIDs);
    }
}
