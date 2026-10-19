package com.tapjoy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class TapjoyVideoView extends Activity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {
    private static final String BUNDLE_DIALOG_SHOWING = "dialog_showing";
    private static final String BUNDLE_SEEK_TIME = "seek_time";
    private static final int DIALOG_WARNING_ID = 0;
    private static final String TJC_VIDEO_AD_TEXT_FONT_COLOR = "#909090";
    private static final String TJC_VIDEO_CURRENCY_TEXT_COLOR = "#004194";
    private static final String TJC_VIDEO_FONT_COLOR = "\"black\"";
    private static final String TJC_VIDEO_FONT_TYPE = "\"helvetica\"";
    static final int stateFocused = 16842908;
    static final int statePressed = 16842919;
    static final int stateSelected = 16842913;
    private static TapjoyVideoObject videoData = null;
    private static final String videoSecondsText = " seconds";
    private static final String videoWillResumeText = "";
    private String adText;
    private String currencyAmount;
    private String currencyName;
    int deviceScreenDensity;
    int deviceScreenLayoutSize;
    Dialog dialog;
    private String iconURL;
    private LinearLayout linearLayout;
    private RelativeLayout relativeLayout;
    private ImageView tapjoyImage;
    private ImageView thumbnailImage;
    private Bitmap videoScreenshot;
    private static boolean videoError = false;
    private static boolean streamingVideo = false;
    private static String TJC_VIDEO_FONT_SIZE = "\"4\"";
    private static String TJC_VIDEO_AD_TEXT_FONT_SIZE = "\"3\"";
    static int textSize = 16;
    static int padding = 10;
    static int buttonHeight = -1;
    static final int[] colorsBackground = {-7697782, -12303292};
    static final int[] colorsInfo = {-1, -1513240};
    static final int[] colorsButtons = {-1, -1513240};
    static final float[] cornerAll = {10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f};
    static final float[] outerRadii = {12.0f, 12.0f, 12.0f, 12.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    static final RectF inset = new RectF(6.0f, 6.0f, 6.0f, 6.0f);
    static final float[] innerRadii = {12.0f, 12.0f, 0.0f, 0.0f, 12.0f, 12.0f, 0.0f, 0.0f};
    private VideoView videoView = null;
    private String videoPath = null;
    private TextView overlayText = null;
    private TextView detailsText = null;
    private TextView whatNowText = null;
    private Button replayButton = null;
    private Button closeButton = null;
    private Button linkButton = null;
    private Button linkAltButton = null;
    Timer timer = null;
    private boolean dialogShowing = false;
    private boolean clickRequestSuccess = false;
    private boolean allowBackKey = false;
    private int timeRemaining = 0;
    private int seekTime = 0;
    final String TAPJOY_VIDEO = "VIDEO";
    final Handler mHandler = new Handler();
    final Runnable mUpdateResults = new Runnable() { // from class: com.tapjoy.TapjoyVideoView.1
        @Override // java.lang.Runnable
        public void run() {
            TapjoyVideoView.this.overlayText.setText(TapjoyVideoView.this.getRemainingVideoTime() + TapjoyVideoView.videoSecondsText);
        }
    };

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        TapjoyLog.i("VIDEO", "onCreate");
        if (savedInstanceState != null) {
            TapjoyLog.i("VIDEO", "*** Loading saved data from bundle ***");
            this.seekTime = savedInstanceState.getInt(BUNDLE_SEEK_TIME);
            this.dialogShowing = savedInstanceState.getBoolean(BUNDLE_DIALOG_SHOWING);
        }
        TapjoyLog.i("VIDEO", "dialogShowing: " + this.dialogShowing + ", seekTime: " + this.seekTime);
        streamingVideo = false;
        if (TapjoyVideo.getInstance() == null) {
            finish();
            return;
        }
        videoData = TapjoyVideo.getInstance().getCurrentVideoData();
        this.videoPath = videoData.dataLocation;
        if (this.videoPath == null || this.videoPath.length() == 0) {
            TapjoyLog.i("VIDEO", "no cached video, try streaming video at location: " + videoData.videoURL);
            this.videoPath = videoData.videoURL;
            streamingVideo = true;
        }
        this.currencyAmount = videoData.currencyAmount;
        this.currencyName = videoData.currencyName;
        this.adText = videoData.videoAdName;
        this.iconURL = videoData.iconURL;
        if (this.iconURL != null && this.iconURL.length() > 0) {
            new Thread(new Runnable() { // from class: com.tapjoy.TapjoyVideoView.2
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        URL fileURL = new URL(TapjoyVideoView.this.iconURL);
                        URLConnection connection = fileURL.openConnection();
                        connection.setConnectTimeout(15000);
                        connection.setReadTimeout(25000);
                        connection.connect();
                        TapjoyVideoView.this.videoScreenshot = BitmapFactory.decodeStream(fileURL.openConnection().getInputStream());
                    } catch (Exception e) {
                        TapjoyLog.i("VIDEO", "e: " + e.toString());
                    }
                }
            }).start();
        }
        TapjoyLog.i("VIDEO", "videoPath: " + this.videoPath);
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        this.relativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
        this.relativeLayout.setLayoutParams(params);
        setContentView(this.relativeLayout);
        if (Integer.parseInt(Build.VERSION.SDK) > 3) {
            TapjoyDisplayMetricsUtil displayMetricsUtil = new TapjoyDisplayMetricsUtil(this);
            this.deviceScreenLayoutSize = displayMetricsUtil.getScreenLayoutSize();
            TapjoyLog.i("VIDEO", "deviceScreenLayoutSize: " + this.deviceScreenLayoutSize);
            if (this.deviceScreenLayoutSize == 4) {
                Display display = getWindowManager().getDefaultDisplay();
                int width = display.getWidth();
                int height = display.getHeight();
                TapjoyLog.i("VIDEO", "width: " + width + ", height: " + height);
                padding = 20;
                textSize = 32;
                buttonHeight = height / 7;
                TJC_VIDEO_FONT_SIZE = "\"10\"";
                TJC_VIDEO_AD_TEXT_FONT_SIZE = "\"6\"";
                TapjoyLog.i("VIDEO", "buttonHeight: " + buttonHeight);
            }
        }
        TapjoyLog.i("VIDEO", "padding: " + padding);
        TapjoyLog.i("VIDEO", "textSize: " + textSize);
        initVideoView();
        TapjoyLog.i("VIDEO", "onCreate DONE");
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        if (this.videoView.isPlaying()) {
            TapjoyLog.i("VIDEO", "onPause");
            this.seekTime = this.videoView.getCurrentPosition();
            TapjoyLog.i("VIDEO", "seekTime: " + this.seekTime);
        }
    }

    @Override // android.app.Activity
    protected void onResume() {
        TapjoyLog.i("VIDEO", "onResume");
        setRequestedOrientation(0);
        super.onResume();
        if (this.seekTime > 0) {
            TapjoyLog.i("VIDEO", "seekTime: " + this.seekTime);
            this.videoView.seekTo(this.seekTime);
            if (!this.dialogShowing || this.dialog == null || !this.dialog.isShowing()) {
                this.videoView.start();
            }
        }
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        TapjoyLog.i("VIDEO", "*** onSaveInstanceState ***");
        TapjoyLog.i("VIDEO", "dialogShowing: " + this.dialogShowing + ", seekTime: " + this.seekTime);
        outState.putBoolean(BUNDLE_DIALOG_SHOWING, this.dialogShowing);
        outState.putInt(BUNDLE_SEEK_TIME, this.seekTime);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean hasFocus) {
        TapjoyLog.i("VIDEO", "onWindowFocusChanged");
        super.onWindowFocusChanged(hasFocus);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initVideoView() {
        this.relativeLayout.removeAllViews();
        this.relativeLayout.setBackgroundColor(-16777216);
        if (this.videoView == null && this.overlayText == null) {
            this.tapjoyImage = new ImageView(this);
            Bitmap bitmap = BitmapFactory.decodeFile(TapjoyVideo.imageTapjoyLocation);
            if (bitmap != null) {
                this.tapjoyImage.setImageBitmap(bitmap);
            }
            RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(-2, -2);
            imageParams.addRule(12);
            imageParams.addRule(11);
            this.tapjoyImage.setLayoutParams(imageParams);
            this.videoView = new VideoView(this);
            this.videoView.setOnCompletionListener(this);
            this.videoView.setOnErrorListener(this);
            this.videoView.setOnPreparedListener(this);
            if (streamingVideo) {
                TapjoyLog.i("VIDEO", "streaming video: " + this.videoPath);
                this.videoView.setVideoURI(Uri.parse(this.videoPath));
            } else {
                TapjoyLog.i("VIDEO", "cached video: " + this.videoPath);
                this.videoView.setVideoPath(this.videoPath);
            }
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
            layoutParams.addRule(13);
            this.videoView.setLayoutParams(layoutParams);
            this.timeRemaining = this.videoView.getDuration() / 1000;
            TapjoyLog.i("VIDEO", "videoView.getDuration(): " + this.videoView.getDuration());
            TapjoyLog.i("VIDEO", "timeRemaining: " + this.timeRemaining);
            this.overlayText = new TextView(this);
            this.overlayText.setTextSize(textSize);
            this.overlayText.setTypeface(Typeface.create("default", 1), 1);
            RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(-2, -2);
            textParams.addRule(12);
            this.overlayText.setLayoutParams(textParams);
        }
        startVideo();
        this.relativeLayout.addView(this.videoView);
        this.relativeLayout.addView(this.tapjoyImage);
        this.relativeLayout.addView(this.overlayText);
    }

    private void initVideoUI() {
        Bitmap bitmap;
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        TapjoyLog.i("VIDEO", "view dimensions: " + width + "x" + height);
        if (Integer.parseInt(Build.VERSION.SDK) > 3) {
            TapjoyDisplayMetricsUtil displayMetricsUtil = new TapjoyDisplayMetricsUtil(this);
            this.deviceScreenLayoutSize = displayMetricsUtil.getScreenLayoutSize();
            TapjoyLog.i("VIDEO", "deviceScreenLayoutSize: " + this.deviceScreenLayoutSize);
            if (this.deviceScreenLayoutSize == 4) {
                padding = 20;
                textSize = 32;
                buttonHeight = height / 7;
                TJC_VIDEO_FONT_SIZE = "\"10\"";
                TJC_VIDEO_AD_TEXT_FONT_SIZE = "\"6\"";
                TapjoyLog.i("VIDEO", "buttonHeight: " + buttonHeight);
            }
        }
        int infoViewHeight = height / 3;
        int buttonWidth = (display.getWidth() / 2) - (padding * 2);
        this.linearLayout = new LinearLayout(this);
        this.linearLayout.setOrientation(1);
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(-1, -1);
        linearLayoutParams.setMargins(padding, padding, padding, padding);
        this.linearLayout.setLayoutParams(linearLayoutParams);
        this.detailsText = new TextView(this);
        this.detailsText.setText("Details");
        this.detailsText.setTextColor(-1);
        this.detailsText.setTextSize(textSize);
        this.detailsText.setTypeface(Typeface.create("default", 1), 1);
        this.detailsText.setShadowLayer(1.0f, 1.0f, 1.0f, -16777216);
        LinearLayout.LayoutParams detailsParams = new LinearLayout.LayoutParams(-2, -2);
        detailsParams.setMargins(padding, padding, padding, 0);
        this.detailsText.setLayoutParams(detailsParams);
        this.detailsText.setId(2111);
        try {
            Bitmap bitmap2 = BitmapFactory.decodeFile(TapjoyVideo.imageBackgroundLocation);
            if (bitmap2 != null) {
                BitmapDrawable tiledBackground = new BitmapDrawable(bitmap2);
                tiledBackground.setTileModeX(Shader.TileMode.REPEAT);
                tiledBackground.setTileModeY(Shader.TileMode.REPEAT);
                this.linearLayout.setBackgroundDrawable(tiledBackground);
            }
        } catch (Exception e) {
            TapjoyLog.i("VIDEO", "error decoding background image: " + e.toString());
        }
        this.thumbnailImage = new ImageView(this);
        try {
            if (this.videoScreenshot != null) {
                bitmap = this.videoScreenshot;
            } else {
                bitmap = BitmapFactory.decodeFile(TapjoyVideo.imagePlaceholderLocation);
            }
            if (bitmap != null) {
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();
                Bitmap rounder = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(rounder);
                Paint xferPaint = new Paint(1);
                xferPaint.setColor(-65536);
                canvas.drawRoundRect(new RectF(0.0f, 0.0f, w, h), 25.0f, 25.0f, xferPaint);
                xferPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                Bitmap roundedBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas result = new Canvas(roundedBitmap);
                result.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
                int desiredWidth = (w * infoViewHeight) / h;
                Bitmap scaled = Bitmap.createScaledBitmap(roundedBitmap, desiredWidth, infoViewHeight, true);
                this.thumbnailImage.setImageBitmap(scaled);
                this.thumbnailImage.setPadding(0, 0, 0, 0);
            }
        } catch (Exception e2) {
            TapjoyLog.i("VIDEO", "error decoding thumbnail image: " + e2.toString());
        }
        WebView webview = new WebView(this);
        webview.setBackgroundColor(0);
        String summary = "<html><body><font face=\"helvetica\" color=\"black\" size=" + TJC_VIDEO_FONT_SIZE + "><p>You just earned <font color=" + TJC_VIDEO_CURRENCY_TEXT_COLOR + "><b>" + this.currencyAmount + "</b></font> " + this.currencyName + "! <br /> <font color=" + TJC_VIDEO_AD_TEXT_FONT_COLOR + " size=" + TJC_VIDEO_AD_TEXT_FONT_SIZE + "><b>" + this.adText + "</b></p></font></font></body></html>";
        webview.loadData(summary, "text/html", "utf-8");
        LinearLayout webLinearLayout = new LinearLayout(this);
        webLinearLayout.addView(webview);
        webLinearLayout.setPadding((-padding) / 2, padding / 2, 0, 0);
        LinearLayout customLinearLayoutView = new LinearLayout(this);
        LinearLayout.LayoutParams customLinearLayoutViewParams = new LinearLayout.LayoutParams(width - (padding * 2), infoViewHeight);
        customLinearLayoutViewParams.setMargins(padding, padding, padding, padding);
        customLinearLayoutView.setLayoutParams(customLinearLayoutViewParams);
        customLinearLayoutView.setOrientation(0);
        customLinearLayoutView.setPadding(0, padding * 2, 0, padding * 2);
        customLinearLayoutView.setBackgroundDrawable(createGradient(colorsInfo));
        customLinearLayoutView.setGravity(19);
        customLinearLayoutView.addView(this.thumbnailImage);
        customLinearLayoutView.addView(webLinearLayout);
        this.whatNowText = new TextView(this);
        this.whatNowText.setText("What now?");
        this.whatNowText.setTextColor(-1);
        this.whatNowText.setTextSize(textSize);
        this.whatNowText.setTypeface(Typeface.create("default", 1), 1);
        this.whatNowText.setShadowLayer(1.0f, 1.0f, 1.0f, -16777216);
        LinearLayout.LayoutParams whatNowParams = new LinearLayout.LayoutParams(-2, -2);
        whatNowParams.setMargins(padding, padding, padding, 0);
        this.whatNowText.setLayoutParams(whatNowParams);
        this.whatNowText.setId(3112);
        this.replayButton = createButton("Replay this video");
        LinearLayout.LayoutParams replayParams = new LinearLayout.LayoutParams(buttonWidth, -2);
        replayParams.setMargins(padding, padding, padding, padding);
        this.replayButton.setLayoutParams(replayParams);
        this.replayButton.setId(1113);
        this.replayButton.setOnClickListener(new View.OnClickListener() { // from class: com.tapjoy.TapjoyVideoView.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                TapjoyVideoView.this.initVideoView();
            }
        });
        TapjoyLog.i("VIDEO", "button dimensions: " + this.replayButton.getMeasuredWidth() + "x" + this.replayButton.getMeasuredHeight());
        this.closeButton = createButton("View More Offers");
        LinearLayout.LayoutParams closeParams = new LinearLayout.LayoutParams(buttonWidth, -2);
        closeParams.setMargins(padding, padding, padding, padding);
        this.closeButton.setLayoutParams(closeParams);
        this.closeButton.setOnClickListener(new View.OnClickListener() { // from class: com.tapjoy.TapjoyVideoView.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                TapjoyVideoView.this.finish();
            }
        });
        LinearLayout linearLayoutRow = new LinearLayout(this);
        LinearLayout.LayoutParams linearLayoutRowParams = new LinearLayout.LayoutParams(width, -2);
        linearLayoutRow.setLayoutParams(linearLayoutRowParams);
        linearLayoutRow.setOrientation(0);
        linearLayoutRow.addView(this.replayButton);
        linearLayoutRow.addView(this.closeButton);
        TapjoyLog.i("VIDEO", "linear width: " + linearLayoutRow.getMeasuredWidth());
        this.linearLayout.addView(this.detailsText);
        this.linearLayout.addView(customLinearLayoutView);
        this.linearLayout.addView(this.whatNowText);
        this.linearLayout.addView(linearLayoutRow);
        if (videoData.buttonCount >= 1) {
            this.linkButton = createButton(videoData.buttonData[0][0]);
            LinearLayout.LayoutParams linkParams = new LinearLayout.LayoutParams(buttonWidth, -2);
            linkParams.setMargins(padding, padding, padding, padding);
            this.linkButton.setLayoutParams(linkParams);
            this.linkButton.setOnClickListener(new View.OnClickListener() { // from class: com.tapjoy.TapjoyVideoView.5
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    Uri uri = Uri.parse(TapjoyVideoView.videoData.buttonData[0][1]);
                    TapjoyLog.i("VIDEO", "buton url: " + uri.toString());
                    try {
                        Intent intent = new Intent("android.intent.action.VIEW", uri);
                        TapjoyVideoView.this.startActivity(intent);
                    } catch (Exception e3) {
                        Log.e("VIDEO BUTTON", "Bad URI " + uri + ": " + e3.toString());
                    }
                }
            });
            if (videoData.buttonCount >= 2) {
                this.linkAltButton = createButton(videoData.buttonData[1][0]);
                LinearLayout.LayoutParams linkAltParams = new LinearLayout.LayoutParams(buttonWidth, -2);
                linkAltParams.setMargins(padding, padding, padding, padding);
                this.linkAltButton.setLayoutParams(linkParams);
                this.linkAltButton.setOnClickListener(new View.OnClickListener() { // from class: com.tapjoy.TapjoyVideoView.6
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        Uri uri = Uri.parse(TapjoyVideoView.videoData.buttonData[1][1]);
                        TapjoyLog.i("VIDEO", "buton url: " + uri.toString());
                        try {
                            Intent intent = new Intent("android.intent.action.VIEW", uri);
                            TapjoyVideoView.this.startActivity(intent);
                        } catch (Exception e3) {
                            Log.e("VIDEO BUTTON", "Bad URI " + uri + ": " + e3.toString());
                        }
                    }
                });
                LinearLayout secondLayoutRow = new LinearLayout(this);
                LinearLayout.LayoutParams secondLayoutRowParams = new LinearLayout.LayoutParams(width, -2);
                secondLayoutRow.setLayoutParams(secondLayoutRowParams);
                secondLayoutRow.setOrientation(0);
                secondLayoutRow.addView(this.linkButton);
                secondLayoutRow.addView(this.linkAltButton);
                this.linearLayout.addView(secondLayoutRow);
                return;
            }
            this.linearLayout.addView(this.linkButton);
        }
    }

    private void showVideoUI() {
        this.relativeLayout.removeAllViews();
        this.relativeLayout.setBackgroundDrawable(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colorsBackground));
        this.relativeLayout.addView(this.linearLayout);
    }

    private Button createButton(String text) {
        Drawable s1 = createGradient(colorsButtons);
        Drawable n1 = createGradient(colorsButtons);
        Drawable f1 = createGradient(colorsButtons);
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{16842919}, s1);
        drawable.addState(new int[]{-16842908, -16842913}, n1);
        drawable.addState(new int[]{-16842908, 16842913}, f1);
        Button button = new Button(this);
        button.setBackgroundDrawable(drawable);
        button.setText(text);
        button.setTextColor(-16565102);
        button.setTextSize(textSize);
        button.setTypeface(Typeface.create("default", 1), 1);
        if (buttonHeight > 0) {
            button.setHeight(buttonHeight);
        }
        TapjoyLog.i("VIDEO", "padding bottom: " + button.getPaddingBottom());
        TapjoyLog.i("VIDEO", "padding top: " + button.getPaddingTop());
        TapjoyLog.i("VIDEO", "padding left: " + button.getPaddingLeft());
        TapjoyLog.i("VIDEO", "padding right: " + button.getPaddingRight());
        button.setPadding(button.getPaddingLeft(), button.getPaddingBottom(), button.getPaddingRight(), button.getPaddingBottom());
        return button;
    }

    private static GradientDrawable createGradient(int[] colors) {
        GradientDrawable.Orientation orientation = GradientDrawable.Orientation.TOP_BOTTOM;
        GradientDrawable gradientDrawable = new GradientDrawable(orientation, colors);
        gradientDrawable.setCornerRadii(cornerAll);
        gradientDrawable.setGradientRadius(50.0f);
        return gradientDrawable;
    }

    private void startVideo() {
        this.videoView.requestFocus();
        if (this.dialogShowing) {
            this.videoView.seekTo(this.seekTime);
            TapjoyLog.i("VIDEO", "dialog is showing -- don't start");
        } else {
            TapjoyLog.i("VIDEO", "start");
            this.videoView.seekTo(0);
            this.videoView.start();
        }
        if (this.timer != null) {
            this.timer.cancel();
        }
        this.timer = new Timer();
        this.timer.schedule(new RemainingTime(this, null), 500L, 100L);
        new Thread(new Runnable() { // from class: com.tapjoy.TapjoyVideoView.7
            @Override // java.lang.Runnable
            public void run() {
                String response = new TapjoyURLConnection().connectToURL(TapjoyVideoView.videoData.clickURL);
                if (response != null && response.contains("OK")) {
                    TapjoyLog.i("VIDEO", "CLICK REQUEST SUCCESS!");
                    TapjoyVideoView.this.clickRequestSuccess = true;
                }
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getRemainingVideoTime() {
        int timeRemaining = (this.videoView.getDuration() - this.videoView.getCurrentPosition()) / 1000;
        if (timeRemaining < 0) {
            return 0;
        }
        return timeRemaining;
    }

    private class RemainingTime extends TimerTask {
        private RemainingTime() {
        }

        /* synthetic */ RemainingTime(TapjoyVideoView tapjoyVideoView, RemainingTime remainingTime) {
            this();
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            TapjoyVideoView.this.mHandler.post(TapjoyVideoView.this.mUpdateResults);
        }
    }

    @Override // android.media.MediaPlayer.OnPreparedListener
    public void onPrepared(MediaPlayer mp) {
        TapjoyLog.i("VIDEO", "onPrepared");
    }

    @Override // android.media.MediaPlayer.OnErrorListener
    public boolean onError(MediaPlayer mp, int what, int extra) {
        videoError = true;
        TapjoyLog.i("VIDEO", "onError");
        TapjoyVideo.getVideoNotifier().videoError(3);
        this.allowBackKey = true;
        if (this.timer != null) {
            this.timer.cancel();
            return false;
        }
        return false;
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public void onCompletion(MediaPlayer mp) {
        TapjoyLog.i("VIDEO", "onCompletion");
        if (this.timer != null) {
            this.timer.cancel();
        }
        initVideoUI();
        showVideoUI();
        if (!videoError) {
            TapjoyVideo.getVideoNotifier().videoComplete();
            new Thread(new Runnable() { // from class: com.tapjoy.TapjoyVideoView.8
                @Override // java.lang.Runnable
                public void run() {
                    if (TapjoyVideoView.this.clickRequestSuccess) {
                        TapjoyConnectCore.getInstance().actionComplete(TapjoyVideoView.videoData.offerID);
                    }
                }
            }).start();
        }
        videoError = false;
        this.allowBackKey = true;
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            if (!this.allowBackKey) {
                this.seekTime = this.videoView.getCurrentPosition();
                this.videoView.pause();
                this.dialogShowing = true;
                showDialog(0);
                TapjoyLog.i("VIDEO", "PAUSE VIDEO time: " + this.seekTime);
                TapjoyLog.i("VIDEO", "currentPosition: " + this.videoView.getCurrentPosition());
                TapjoyLog.i("VIDEO", "duration: " + this.videoView.getDuration() + ", elapsed: " + (this.videoView.getDuration() - this.videoView.getCurrentPosition()));
                return true;
            }
            if (this.videoView.isPlaying()) {
                this.videoView.stopPlayback();
                showVideoUI();
                if (this.timer == null) {
                    return true;
                }
                this.timer.cancel();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int id) {
        TapjoyLog.i("VIDEO", "dialog onCreateDialog");
        if (!this.dialogShowing) {
            return this.dialog;
        }
        switch (id) {
            case 0:
                this.dialog = new AlertDialog.Builder(this).setTitle("Cancel Video?").setMessage("Currency will not be awarded, are you sure you want to cancel the video?").setNegativeButton("End", new DialogInterface.OnClickListener() { // from class: com.tapjoy.TapjoyVideoView.9
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int whichButton) {
                        TapjoyVideoView.this.finish();
                    }
                }).setPositiveButton("Resume", new DialogInterface.OnClickListener() { // from class: com.tapjoy.TapjoyVideoView.10
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        TapjoyVideoView.this.videoView.seekTo(TapjoyVideoView.this.seekTime);
                        TapjoyVideoView.this.videoView.start();
                        TapjoyVideoView.this.dialogShowing = false;
                        TapjoyLog.i("VIDEO", "RESUME VIDEO time: " + TapjoyVideoView.this.seekTime);
                        TapjoyLog.i("VIDEO", "currentPosition: " + TapjoyVideoView.this.videoView.getCurrentPosition());
                        TapjoyLog.i("VIDEO", "duration: " + TapjoyVideoView.this.videoView.getDuration() + ", elapsed: " + (TapjoyVideoView.this.videoView.getDuration() - TapjoyVideoView.this.videoView.getCurrentPosition()));
                    }
                }).create();
                this.dialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.tapjoy.TapjoyVideoView.11
                    @Override // android.content.DialogInterface.OnCancelListener
                    public void onCancel(DialogInterface dialog) {
                        TapjoyLog.i("VIDEO", "dialog onCancel");
                        dialog.dismiss();
                        TapjoyVideoView.this.videoView.seekTo(TapjoyVideoView.this.seekTime);
                        TapjoyVideoView.this.videoView.start();
                        TapjoyVideoView.this.dialogShowing = false;
                    }
                });
                this.dialog.show();
                this.dialogShowing = true;
                break;
            default:
                this.dialog = null;
                break;
        }
        return this.dialog;
    }
}
