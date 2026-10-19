package com.millennialmedia.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import com.sessionm.ui.SessionMActivity;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;

/* JADX INFO: loaded from: classes.dex */
class MMMedia extends MMJSObject {
    private static final String PATH = "path";
    private static final String PICTURES = "Pictures";
    private static Object pickerActivityObject;
    MediaScannerConnection mediaScanner;

    MMMedia() {
    }

    private boolean isCameraAvailable() {
        Context context = this.contextRef.get();
        if (context == null || context.getPackageManager().checkPermission("android.permission.CAMERA", context.getPackageName()) != 0) {
            return false;
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, 65536);
        return list.size() > 0;
    }

    private boolean isPictureChooserAvailable() {
        Context context = this.contextRef.get();
        if (context == null) {
            return false;
        }
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, 65536);
        return list.size() > 0;
    }

    public MMJSResponse isSourceTypeAvailable(HashMap<String, String> arguments) {
        String type = arguments.get("sourceType");
        if (type != null) {
            if (type.equalsIgnoreCase("Camera") && isCameraAvailable()) {
                return MMJSResponse.responseWithSuccess();
            }
            if (type.equalsIgnoreCase("Photo Library") && isPictureChooserAvailable()) {
                return MMJSResponse.responseWithSuccess();
            }
        }
        return null;
    }

    public MMJSResponse availableSourceTypes(HashMap<String, String> arguments) {
        JSONArray jsonArray = new JSONArray();
        if (isCameraAvailable()) {
            jsonArray.put("Camera");
        }
        if (isPictureChooserAvailable()) {
            jsonArray.put("Photo Library");
        }
        MMJSResponse response = new MMJSResponse();
        response.result = 1;
        response.response = jsonArray;
        return response;
    }

    static class PickerActivity extends MMBaseActivity {
        private Uri fileUri;

        PickerActivity() {
        }

        @Override // com.millennialmedia.android.MMBaseActivity
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getIntent().getStringExtra(SessionMActivity.INTENT_PARAM_TYPE).equalsIgnoreCase("Camera")) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                this.fileUri = getIntent().getData();
                intent.putExtra("output", this.fileUri);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, 0);
                return;
            }
            Intent intent2 = new Intent();
            intent2.setType("image/*");
            intent2.setAction("android.intent.action.GET_CONTENT");
            startActivityForResult(intent2, 0);
        }

        /* JADX WARN: Code duplicated, block: B:23:0x007b A[Catch: Exception -> 0x00b4, TRY_LEAVE, TryCatch #6 {Exception -> 0x00b4, blocks: (B:21:0x0076, B:23:0x007b), top: B:64:0x0076 }] */
        /* JADX WARN: Code duplicated, block: B:33:0x0096 A[Catch: Exception -> 0x009a, TRY_LEAVE, TryCatch #2 {Exception -> 0x009a, blocks: (B:31:0x0091, B:33:0x0096), top: B:60:0x0091 }] */
        /* JADX WARN: Code duplicated, block: B:41:0x00a4 A[Catch: Exception -> 0x00ab, TRY_LEAVE, TryCatch #1 {Exception -> 0x00ab, blocks: (B:39:0x009f, B:41:0x00a4), top: B:58:0x009f }] */
        @Override // com.millennialmedia.android.MMBaseActivity
        protected void onActivityResult(int requestCode, int resultCode, Intent data) throws Throwable {
            int index;
            if (data != null && getIntent().getStringExtra(SessionMActivity.INTENT_PARAM_TYPE).equalsIgnoreCase("Photo Library")) {
                InputStream in = null;
                OutputStream out = null;
                Uri contentUri = data.getData();
                String[] proj = {"_data"};
                Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
                if (cursor != null && (index = cursor.getColumnIndex("_data")) != -1) {
                    cursor.moveToFirst();
                    File chosenFile = new File(cursor.getString(index));
                    cursor.close();
                    try {
                        File file = new File(getIntent().getData().getPath());
                        InputStream in2 = new FileInputStream(chosenFile);
                        try {
                            OutputStream out2 = new FileOutputStream(file);
                            try {
                                byte[] buf = new byte[1024];
                                while (true) {
                                    int len = in2.read(buf);
                                    if (len <= 0) {
                                        break;
                                    } else {
                                        out2.write(buf, 0, len);
                                    }
                                }
                                if (in2 != null) {
                                    try {
                                        in2.close();
                                        if (out2 != null) {
                                            out2.close();
                                        }
                                    } catch (Exception e) {
                                    }
                                } else if (out2 != null) {
                                    out2.close();
                                }
                            } catch (Exception e2) {
                                out = out2;
                                in = in2;
                                if (in != null) {
                                    try {
                                        in.close();
                                        if (out != null) {
                                            out.close();
                                        }
                                    } catch (Exception e3) {
                                    }
                                } else if (out != null) {
                                    out.close();
                                }
                            } catch (Throwable th) {
                                th = th;
                                out = out2;
                                in = in2;
                                if (in != null) {
                                    try {
                                        in.close();
                                        if (out != null) {
                                            out.close();
                                        }
                                    } catch (Exception e4) {
                                        throw th;
                                    }
                                } else if (out != null) {
                                    out.close();
                                }
                                throw th;
                            }
                        } catch (Exception e5) {
                            in = in2;
                        } catch (Throwable th2) {
                            th = th2;
                            in = in2;
                        }
                    } catch (Exception e6) {
                    } catch (Throwable th3) {
                        th = th3;
                    }
                }
            }
            synchronized (MMMedia.pickerActivityObject) {
                MMMedia.pickerActivityObject.notify();
            }
            finish();
        }
    }

    static Bitmap resizeImage(Bitmap image, String contentMode, int toW, int toH, int quality) {
        float horizontalRatio = toW / image.getWidth();
        float verticalRatio = toH / image.getHeight();
        if (contentMode.equals("Center")) {
            return centerOfImage(image, toW, toH);
        }
        if (contentMode.equals("ScaleToAspectFit")) {
            float ratio = Math.min(horizontalRatio, verticalRatio);
            int newW = (int) (image.getWidth() * ratio);
            int newH = (int) (image.getHeight() * ratio);
            return resizeImage(image, newW, newH, quality);
        }
        if (contentMode.equals("ScaleToAspectFill")) {
            float ratio2 = Math.max(horizontalRatio, verticalRatio);
            int newW2 = (int) (image.getWidth() * ratio2);
            int newH2 = (int) (image.getHeight() * ratio2);
            return cropImage(resizeImage(image, newW2, newH2, quality), 0, 0, toW, toH);
        }
        return resizeImage(image, toW, toH, quality);
    }

    private static Bitmap resizeImage(Bitmap image, int newW, int newH, int quality) {
        return Bitmap.createScaledBitmap(image, newW, newH, true);
    }

    private static Bitmap centerOfImage(Bitmap image, int width, int height) {
        float x = (image.getWidth() - width) / 2;
        float y = (image.getHeight() - height) / 2;
        return cropImage(image, (int) x, (int) y, width, height);
    }

    private static Bitmap cropImage(Bitmap bitmap, int left, int top, int width, int height) {
        return Bitmap.createBitmap(bitmap, left, top, width, height);
    }

    /* JADX WARN: Code duplicated, block: B:30:0x00ae A[Catch: Exception -> 0x010d, TryCatch #1 {Exception -> 0x010d, blocks: (B:28:0x00a9, B:30:0x00ae, B:32:0x00b3), top: B:96:0x00a9 }] */
    /* JADX WARN: Code duplicated, block: B:32:0x00b3 A[Catch: Exception -> 0x010d, TRY_LEAVE, TryCatch #1 {Exception -> 0x010d, blocks: (B:28:0x00a9, B:30:0x00ae, B:32:0x00b3), top: B:96:0x00a9 }] */
    /* JADX WARN: Code duplicated, block: B:43:0x00d4 A[Catch: IOException -> 0x00d8, TRY_LEAVE, TryCatch #0 {IOException -> 0x00d8, blocks: (B:41:0x00cf, B:43:0x00d4), top: B:94:0x00cf }] */
    /* JADX WARN: Code duplicated, block: B:51:0x00e2 A[Catch: IOException -> 0x012d, TRY_LEAVE, TryCatch #6 {IOException -> 0x012d, blocks: (B:49:0x00dd, B:51:0x00e2), top: B:100:0x00dd }] */
    /* JADX WARN: Code duplicated, block: B:62:0x00ff A[Catch: Exception -> 0x0108, TryCatch #12 {Exception -> 0x0108, blocks: (B:60:0x00fa, B:62:0x00ff, B:64:0x0104), top: B:105:0x00fa }] */
    /* JADX WARN: Code duplicated, block: B:64:0x0104 A[Catch: Exception -> 0x0108, TRY_LEAVE, TryCatch #12 {Exception -> 0x0108, blocks: (B:60:0x00fa, B:62:0x00ff, B:64:0x0104), top: B:105:0x00fa }] */
    /* JADX WARN: Code duplicated, block: B:74:0x011a A[Catch: Exception -> 0x0123, TryCatch #15 {Exception -> 0x0123, blocks: (B:72:0x0115, B:74:0x011a, B:76:0x011f), top: B:107:0x0115 }] */
    /* JADX WARN: Code duplicated, block: B:76:0x011f A[Catch: Exception -> 0x0123, TRY_LEAVE, TryCatch #15 {Exception -> 0x0123, blocks: (B:72:0x0115, B:74:0x011a, B:76:0x011f), top: B:107:0x0115 }] */
    private static final byte[] scaleBitmapToBytes(File file, int w, int h, String contentMode) throws Throwable {
        FileInputStream fis = null;
        FileInputStream fis2 = null;
        Bitmap scaledBitmap = null;
        try {
            FileInputStream fis3 = new FileInputStream(file);
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(fis3, null, options);
                int height = options.outHeight;
                int width = options.outWidth;
                int inSampleSize = 1;
                if (height > h || width > w) {
                    if (width > height) {
                        inSampleSize = Math.round(height / h);
                    } else {
                        inSampleSize = Math.round(width / w);
                    }
                }
                FileInputStream fis4 = new FileInputStream(file);
                try {
                    options.inJustDecodeBounds = false;
                    options.inSampleSize = inSampleSize;
                    scaledBitmap = BitmapFactory.decodeStream(fis4, null, options);
                    if (fis3 != null) {
                        try {
                            fis3.close();
                        } catch (IOException e) {
                        }
                    }
                    if (fis4 != null) {
                        fis4.close();
                    }
                } catch (FileNotFoundException e2) {
                    fis2 = fis4;
                    fis = fis3;
                    if (fis != null) {
                        try {
                            fis.close();
                            if (fis2 != null) {
                                fis2.close();
                            }
                        } catch (IOException e3) {
                        }
                    } else if (fis2 != null) {
                        fis2.close();
                    }
                } catch (Throwable th) {
                    th = th;
                    fis2 = fis4;
                    fis = fis3;
                    if (fis != null) {
                        try {
                            fis.close();
                            if (fis2 != null) {
                                fis2.close();
                            }
                        } catch (IOException e4) {
                            throw th;
                        }
                    } else if (fis2 != null) {
                        fis2.close();
                    }
                    throw th;
                }
            } catch (FileNotFoundException e5) {
                fis = fis3;
            } catch (Throwable th2) {
                th = th2;
                fis = fis3;
            }
        } catch (FileNotFoundException e6) {
        } catch (Throwable th3) {
            th = th3;
        }
        byte[] contents = null;
        if (scaledBitmap != null) {
            Bitmap finalBitmap = resizeImage(scaledBitmap, contentMode, w, h, 1);
            ByteArrayOutputStream baos = null;
            try {
                try {
                    ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                    try {
                        if (contentMode.equals("")) {
                            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos2);
                        } else {
                            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos2);
                        }
                        contents = baos2.toByteArray();
                        if (scaledBitmap != null) {
                            try {
                                scaledBitmap.recycle();
                                if (finalBitmap != null) {
                                    finalBitmap.recycle();
                                }
                                if (baos2 != null) {
                                    baos2.close();
                                }
                            } catch (Exception e7) {
                                e7.printStackTrace();
                            }
                        } else {
                            if (finalBitmap != null) {
                                finalBitmap.recycle();
                            }
                            if (baos2 != null) {
                                baos2.close();
                            }
                        }
                    } catch (Exception e8) {
                        e = e8;
                        baos = baos2;
                        contents = null;
                        e.printStackTrace();
                        if (scaledBitmap != null) {
                            try {
                                scaledBitmap.recycle();
                                if (finalBitmap != null) {
                                    finalBitmap.recycle();
                                }
                                if (baos != null) {
                                    baos.close();
                                }
                            } catch (Exception e9) {
                                e9.printStackTrace();
                            }
                        } else {
                            if (finalBitmap != null) {
                                finalBitmap.recycle();
                            }
                            if (baos != null) {
                                baos.close();
                            }
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        baos = baos2;
                        if (scaledBitmap != null) {
                            try {
                                scaledBitmap.recycle();
                                if (finalBitmap != null) {
                                    finalBitmap.recycle();
                                }
                                if (baos != null) {
                                    baos.close();
                                }
                            } catch (Exception e10) {
                                e10.printStackTrace();
                                throw th;
                            }
                        } else {
                            if (finalBitmap != null) {
                                finalBitmap.recycle();
                            }
                            if (baos != null) {
                                baos.close();
                            }
                        }
                        throw th;
                    }
                } catch (Exception e11) {
                    e = e11;
                }
            } catch (Throwable th5) {
                th = th5;
            }
        }
        return contents;
    }

    /* JADX WARN: Code duplicated, block: B:55:0x0116  */
    public synchronized MMJSResponse getPicture(HashMap<String, String> arguments) {
        MMJSResponse response;
        Context context = this.contextRef.get();
        String type = arguments.get("sourceType");
        String height = arguments.get("constrainHeight");
        String width = arguments.get("constrainWidth");
        String contentMode = arguments.get("contentMode");
        if (contentMode == null) {
            contentMode = "";
        }
        if (height == null || width == null) {
            response = MMJSResponse.responseWithError("Missing constrainHeight and/or constrainWidth");
        } else {
            int h = Integer.parseInt(height);
            int w = Integer.parseInt(width);
            if (h * w > 360000) {
                response = MMJSResponse.responseWithError("constrainHeight * constrainWidth > 360000");
            } else if (context != null && type != null) {
                File file = new File(Environment.getExternalStorageDirectory(), "tmp_mm_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                if ((type.equalsIgnoreCase("Camera") && isCameraAvailable()) || (type.equalsIgnoreCase("Photo Library") && isPictureChooserAvailable())) {
                    try {
                        try {
                            pickerActivityObject = new Object();
                            Intent intent = new Intent(context, (Class<?>) MMActivity.class);
                            intent.setData(Uri.fromFile(file));
                            intent.putExtra(SessionMActivity.INTENT_PARAM_TYPE, type);
                            intent.putExtra("class", "com.millennialmedia.android.MMMedia$PickerActivity");
                            if (!(context instanceof Activity)) {
                                intent.addFlags(268435456);
                            }
                            synchronized (pickerActivityObject) {
                                context.startActivity(intent);
                                pickerActivityObject.wait();
                            }
                            pickerActivityObject = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                            pickerActivityObject = null;
                        }
                        if (file != null && file.exists() && file.length() > 0) {
                            byte[] contents = scaleBitmapToBytes(file, w, h, contentMode);
                            file.delete();
                            if (contents != null) {
                                response = new MMJSResponse();
                                response.result = 1;
                                response.dataResponse = contents;
                            } else {
                                response = null;
                            }
                        } else {
                            response = null;
                        }
                    } catch (Throwable th) {
                        pickerActivityObject = null;
                        throw th;
                    }
                } else {
                    response = null;
                }
            } else {
                response = null;
            }
        }
        return response;
    }

    public synchronized MMJSResponse writeToPhotoLibrary(HashMap<String, String> arguments) {
        MMJSResponse mMJSResponseResponseWithError;
        Context context = this.contextRef.get();
        Uri path = Uri.parse(arguments.get(PATH));
        File dest = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + PICTURES + File.separator + path.getLastPathSegment());
        String scheme = path.getScheme();
        if (scheme != null && scheme.equals("http")) {
            arguments.put(SessionMActivity.INTENT_PARAM_URL, path.toString());
            arguments.put(PATH, path.getLastPathSegment());
            MMFileManager fileManager = new MMFileManager();
            fileManager.setContext(context);
            if (fileManager.downloadFile(arguments) == null) {
                mMJSResponseResponseWithError = MMJSResponse.responseWithError("Failed to download");
            } else {
                File source = new File(MMFileManager.getCreativeCacheDirectory(context), File.separator + path.getLastPathSegment());
                scanMedia(moveFile(source, dest) ? dest.getAbsolutePath() : source.getAbsolutePath());
                mMJSResponseResponseWithError = MMJSResponse.responseWithSuccess();
            }
        } else {
            File source2 = new File(MMFileManager.getCreativeCacheDirectory(context), File.separator + path.getLastPathSegment());
            if (!source2.exists()) {
                mMJSResponseResponseWithError = MMJSResponse.responseWithError("No file at " + source2.getAbsolutePath());
            } else {
                scanMedia(moveFile(source2, dest) ? dest.getAbsolutePath() : source2.getAbsolutePath());
                mMJSResponseResponseWithError = MMJSResponse.responseWithSuccess();
            }
        }
        return mMJSResponseResponseWithError;
    }

    private boolean moveFile(File source, File dest) {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            return false;
        }
        try {
            FileChannel inChannel = new FileInputStream(source).getChannel();
            dest.createNewFile();
            FileChannel outChannel = new FileOutputStream(dest).getChannel();
            inChannel.transferTo(0L, inChannel.size(), outChannel);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void scanMedia(final String path) {
        Context context = this.contextRef.get();
        if (context != null) {
            this.mediaScanner = new MediaScannerConnection(context.getApplicationContext(), new MediaScannerConnection.MediaScannerConnectionClient() { // from class: com.millennialmedia.android.MMMedia.1
                @Override // android.media.MediaScannerConnection.OnScanCompletedListener
                public void onScanCompleted(String path2, Uri uri) {
                    if (uri == null) {
                        MMAdViewSDK.Log.d("Failed to scan " + path2);
                    }
                }

                @Override // android.media.MediaScannerConnection.MediaScannerConnectionClient
                public void onMediaScannerConnected() {
                    if (MMMedia.this.mediaScanner != null) {
                        MMMedia.this.mediaScanner.scanFile(path, null);
                    }
                }
            });
            if (this.mediaScanner != null) {
                this.mediaScanner.connect();
            }
        }
    }

    public MMJSResponse playVideo(HashMap<String, String> arguments) {
        Context context = this.contextRef.get();
        String path = arguments.get(PATH);
        if (context != null && path != null) {
            if (path.startsWith("http")) {
                Intent intent = new Intent(context, (Class<?>) MMActivity.class);
                intent.setData(Uri.parse(path));
                intent.putExtra("class", "com.millennialmedia.android.VideoPlayerActivity");
                if (!(context instanceof Activity)) {
                    intent.addFlags(268435456);
                }
                context.startActivity(intent);
                return MMJSResponse.responseWithSuccess();
            }
            File cacheDir = MMFileManager.getCreativeCacheDirectory(context);
            if (cacheDir != null) {
                File file = new File(cacheDir, path);
                if (file.exists()) {
                    Intent intent2 = new Intent(context, (Class<?>) VideoPlayer.class);
                    intent2.setData(Uri.fromFile(file));
                    intent2.putExtra("class", "com.millennialmedia.android.VideoPlayerActivity");
                    if (!(context instanceof Activity)) {
                        intent2.addFlags(268435456);
                    }
                    context.startActivity(intent2);
                    return MMJSResponse.responseWithSuccess();
                }
            }
        }
        return null;
    }

    public MMJSResponse playAudio(HashMap<String, String> arguments) {
        Context context = this.contextRef.get();
        String path = arguments.get(PATH);
        if (context != null && path != null) {
            if (Audio.sharedAudio(context).isPlaying()) {
                return MMJSResponse.responseWithError("Audio already playing.");
            }
            if (path.startsWith("http")) {
                return Audio.sharedAudio(context).playAudio(Uri.parse(path), Boolean.parseBoolean(arguments.get("repeat")));
            }
            File cacheDir = MMFileManager.getCreativeCacheDirectory(context);
            if (cacheDir != null) {
                File file = new File(cacheDir, path);
                if (file.exists()) {
                    return Audio.sharedAudio(context).playAudio(Uri.fromFile(file), Boolean.parseBoolean(arguments.get("repeat")));
                }
            }
        }
        return null;
    }

    public MMJSResponse playSound(HashMap<String, String> arguments) {
        File cacheDir;
        Context context = this.contextRef.get();
        String path = arguments.get(PATH);
        if (context != null && path != null && (cacheDir = MMFileManager.getCreativeCacheDirectory(context)) != null) {
            File file = new File(cacheDir, path);
            if (file.exists()) {
                return Audio.sharedAudio(this.contextRef.get()).playSound(file);
            }
        }
        return null;
    }

    public MMJSResponse stopAudio(HashMap<String, String> arguments) {
        return Audio.sharedAudio(this.contextRef.get()).stop();
    }

    static class Audio {
        private static final int MAX_SOUNDS = 4;
        private static Audio sharedInstance;
        private OnLoadCompleteListener completionListener;
        private WeakReference<Context> contextRef;
        private MediaPlayer mediaPlayer;
        private SoundPool soundPool;

        private Audio() {
        }

        private Audio(Context context) {
            this.contextRef = new WeakReference<>(context.getApplicationContext());
        }

        static synchronized Audio sharedAudio(Context context) {
            if (sharedInstance == null) {
                sharedInstance = new Audio(context);
            }
            return sharedInstance;
        }

        synchronized boolean isPlaying() {
            return this.mediaPlayer != null && this.mediaPlayer.isPlaying();
        }

        synchronized MMJSResponse playAudio(Uri uri, boolean loop) {
            try {
                if (this.mediaPlayer != null) {
                    this.mediaPlayer.release();
                    this.mediaPlayer = null;
                }
                this.mediaPlayer = MediaPlayer.create(this.contextRef.get(), uri);
                this.mediaPlayer.setLooping(loop);
                this.mediaPlayer.start();
                this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.millennialmedia.android.MMMedia.Audio.1
                    @Override // android.media.MediaPlayer.OnCompletionListener
                    public synchronized void onCompletion(MediaPlayer mp) {
                        if (Audio.this.mediaPlayer != null) {
                            Audio.this.mediaPlayer.release();
                            Audio.this.mediaPlayer = null;
                        }
                    }
                });
            } catch (Exception e) {
                MMAdViewSDK.Log.e(e.getCause());
            }
            return MMJSResponse.responseWithSuccess();
        }

        synchronized MMJSResponse playSound(File file) {
            try {
                if (this.soundPool == null) {
                    this.soundPool = new SoundPool(4, 3, 0);
                    this.completionListener = new OnLoadCompleteListener(this.soundPool) { // from class: com.millennialmedia.android.MMMedia.Audio.2
                        @Override // com.millennialmedia.android.MMMedia.Audio.OnLoadCompleteListener
                        public synchronized void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                            if (soundPool != null) {
                                AudioManager audioManager = (AudioManager) ((Context) Audio.this.contextRef.get()).getSystemService("audio");
                                float streamVolume = (audioManager.getStreamVolume(3) + 0.0f) / audioManager.getStreamMaxVolume(3);
                                soundPool.play(sampleId, streamVolume, streamVolume, 1, 0, 1.0f);
                            }
                        }
                    };
                }
                this.completionListener.testSample(this.soundPool.load(file.getAbsolutePath(), 1));
            } catch (Exception e) {
            }
            return MMJSResponse.responseWithSuccess();
        }

        synchronized MMJSResponse stop() {
            if (this.mediaPlayer != null) {
                this.mediaPlayer.release();
                this.mediaPlayer = null;
            }
            if (this.soundPool != null) {
                this.soundPool.release();
                this.soundPool = null;
            }
            if (this.completionListener != null) {
                this.completionListener.release();
                this.completionListener = null;
            }
            return MMJSResponse.responseWithSuccess();
        }

        private abstract class OnLoadCompleteListener {
            private static final int TEST_PERIOD_MS = 100;
            private ArrayList<Integer> sampleIds = new ArrayList<>();
            private SoundPool soundPool;
            private Timer timer;

            abstract void onLoadComplete(SoundPool soundPool, int i, int i2);

            public OnLoadCompleteListener(SoundPool soundPool) {
                this.soundPool = soundPool;
            }

            synchronized void testSample(int sampleId) {
                this.sampleIds.add(Integer.valueOf(sampleId));
                if (this.sampleIds.size() == 1) {
                    this.timer = new Timer();
                    this.timer.scheduleAtFixedRate(new TimerTask() { // from class: com.millennialmedia.android.MMMedia.Audio.OnLoadCompleteListener.1
                        @Override // java.util.TimerTask, java.lang.Runnable
                        public void run() {
                            ArrayList<Integer> completedOnes = new ArrayList<>();
                            for (Integer sampleId2 : OnLoadCompleteListener.this.sampleIds) {
                                int streamId = OnLoadCompleteListener.this.soundPool.play(sampleId2.intValue(), 0.0f, 0.0f, 0, 0, 1.0f);
                                if (streamId != 0) {
                                    OnLoadCompleteListener.this.soundPool.stop(streamId);
                                    OnLoadCompleteListener.this.onLoadComplete(OnLoadCompleteListener.this.soundPool, sampleId2.intValue(), 0);
                                    completedOnes.add(sampleId2);
                                }
                            }
                            OnLoadCompleteListener.this.sampleIds.removeAll(completedOnes);
                            if (OnLoadCompleteListener.this.sampleIds.size() == 0) {
                                OnLoadCompleteListener.this.timer.cancel();
                                OnLoadCompleteListener.this.timer.purge();
                            }
                        }
                    }, 0L, 100L);
                }
            }

            synchronized void release() {
                if (this.timer != null) {
                    this.timer.cancel();
                    this.timer.purge();
                }
            }
        }
    }
}
