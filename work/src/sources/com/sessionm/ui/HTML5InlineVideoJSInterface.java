package com.sessionm.ui;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.VideoView;
import java.util.Timer;
import java.util.TimerTask;

/* JADX INFO: compiled from: ProGuard */
/* JADX INFO: loaded from: classes.dex */
public class HTML5InlineVideoJSInterface {
    protected static final String TAG = "HTML5InlineVideoJSInterface";
    protected Activity mActivity;
    protected String mEventCallback;
    protected FrameLayout mLayout;
    protected FrameLayout.LayoutParams mLayoutParams;
    protected TimerTask mProgressTask;
    protected Timer mProgressTimer;
    protected Uri mUri;
    protected VideoView mVideoView;
    protected android.webkit.WebView mWebView;
    protected boolean mReady = false;
    protected boolean mPlaying = false;
    protected boolean mVideoError = false;
    protected int mVideoErrorWhat = 0;
    protected int mVideoErrorExtra = 0;

    public HTML5InlineVideoJSInterface(Activity activity, android.webkit.WebView webView) {
        this.mActivity = activity;
        this.mWebView = webView;
    }

    public void finalize() {
        destroy();
    }

    public void destroy() {
        dismiss();
        this.mVideoView = null;
        this.mActivity = null;
    }

    public void dismiss() {
        Log.i(TAG, "dismiss video");
        this.mReady = false;
        this.mPlaying = false;
        if (this.mLayout != null) {
            ViewGroup viewGroup = (ViewGroup) this.mLayout.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(this.mLayout);
            } else {
                Log.w(TAG, "layout had no parent?");
            }
            this.mLayout = null;
        }
        if (this.mVideoView != null) {
            if (this.mVideoView.isPlaying()) {
                this.mVideoView.stopPlayback();
            }
            ViewGroup viewGroup2 = (ViewGroup) this.mVideoView.getParent();
            if (viewGroup2 != null) {
                viewGroup2.removeView(this.mVideoView);
            } else {
                Log.w(TAG, "video view had no parent?");
            }
            this.mVideoView = null;
        }
        if (this.mProgressTask != null) {
            this.mProgressTask.cancel();
            this.mProgressTask = null;
        }
        if (this.mProgressTimer != null) {
            this.mProgressTimer.cancel();
            this.mProgressTimer = null;
        }
        this.mLayoutParams = null;
        this.mEventCallback = null;
        this.mUri = null;
    }

    public void setup(final String str, final int i, final int i2, final int i3, final int i4, final String str2) {
        Log.i(TAG, "video setup for " + str);
        if (this.mActivity != null) {
            this.mActivity.runOnUiThread(new Runnable() { // from class: com.sessionm.ui.HTML5InlineVideoJSInterface.1
                @Override // java.lang.Runnable
                public void run() {
                    if (HTML5InlineVideoJSInterface.this.mUri != null) {
                        Log.w(HTML5InlineVideoJSInterface.TAG, "removing video " + HTML5InlineVideoJSInterface.this.mUri.toString());
                        HTML5InlineVideoJSInterface.this.dismiss();
                    }
                    HTML5InlineVideoJSInterface.this.mUri = Uri.parse(str);
                    HTML5InlineVideoJSInterface.this.mEventCallback = str2;
                    HTML5InlineVideoJSInterface.this.mLayoutParams = new FrameLayout.LayoutParams(HTML5InlineVideoJSInterface.this.convertDpToPixels(i3), HTML5InlineVideoJSInterface.this.convertDpToPixels(i4));
                    HTML5InlineVideoJSInterface.this.mLayoutParams.leftMargin = HTML5InlineVideoJSInterface.this.convertDpToPixels(i);
                    HTML5InlineVideoJSInterface.this.mLayoutParams.topMargin = HTML5InlineVideoJSInterface.this.convertDpToPixels(i2);
                    HTML5InlineVideoJSInterface.this.mLayoutParams.gravity = 51;
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
                    layoutParams.gravity = 17;
                    HTML5InlineVideoJSInterface.this.mVideoView = new VideoView(HTML5InlineVideoJSInterface.this.mActivity);
                    HTML5InlineVideoJSInterface.this.mVideoView.setZOrderMediaOverlay(true);
                    HTML5InlineVideoJSInterface.this.mVideoView.setLayoutParams(layoutParams);
                    HTML5InlineVideoJSInterface.this.mVideoView.setVideoURI(HTML5InlineVideoJSInterface.this.mUri);
                    HTML5InlineVideoJSInterface.this.mLayout = new FrameLayout(HTML5InlineVideoJSInterface.this.mActivity);
                    HTML5InlineVideoJSInterface.this.mLayout.setLayoutParams(HTML5InlineVideoJSInterface.this.mLayoutParams);
                    HTML5InlineVideoJSInterface.this.mLayout.addView(HTML5InlineVideoJSInterface.this.mVideoView);
                    HTML5InlineVideoJSInterface.this.mVideoView.setBackgroundColor(-16777216);
                    HTML5InlineVideoJSInterface.this.mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.sessionm.ui.HTML5InlineVideoJSInterface.1.1
                        @Override // android.media.MediaPlayer.OnPreparedListener
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            Log.i(HTML5InlineVideoJSInterface.TAG, "video is ready for playback");
                            HTML5InlineVideoJSInterface.this.mReady = true;
                            HTML5InlineVideoJSInterface.this.mVideoView.setBackgroundColor(0);
                            HTML5InlineVideoJSInterface.this.notifyWebView("ready");
                            if (HTML5InlineVideoJSInterface.this.mPlaying) {
                                HTML5InlineVideoJSInterface.this.show();
                                HTML5InlineVideoJSInterface.this.playVideo();
                            }
                        }
                    });
                    HTML5InlineVideoJSInterface.this.mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.sessionm.ui.HTML5InlineVideoJSInterface.1.2
                        @Override // android.media.MediaPlayer.OnCompletionListener
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            if (!HTML5InlineVideoJSInterface.this.mVideoError) {
                                Log.i(HTML5InlineVideoJSInterface.TAG, "video completed");
                                HTML5InlineVideoJSInterface.this.pauseVideo();
                                HTML5InlineVideoJSInterface.this.notifyWebView("complete");
                            }
                        }
                    });
                    HTML5InlineVideoJSInterface.this.mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() { // from class: com.sessionm.ui.HTML5InlineVideoJSInterface.1.3
                        @Override // android.media.MediaPlayer.OnErrorListener
                        public boolean onError(MediaPlayer mediaPlayer, int i5, int i6) {
                            Log.i(HTML5InlineVideoJSInterface.TAG, "video error, what: " + i5 + ", extra: " + i6);
                            HTML5InlineVideoJSInterface.this.mVideoErrorWhat = i5;
                            HTML5InlineVideoJSInterface.this.mVideoErrorExtra = i6;
                            HTML5InlineVideoJSInterface.this.mVideoError = true;
                            HTML5InlineVideoJSInterface.this.notifyWebView("error");
                            HTML5InlineVideoJSInterface.this.remove();
                            return false;
                        }
                    });
                    HTML5InlineVideoJSInterface.this.mProgressTimer = new Timer();
                    ((ViewGroup) HTML5InlineVideoJSInterface.this.mWebView.getParent()).addView(HTML5InlineVideoJSInterface.this.mLayout);
                }
            });
        }
    }

    public void play() {
        Log.i(TAG, "play video");
        if (this.mActivity != null) {
            this.mPlaying = true;
            this.mActivity.runOnUiThread(new Runnable() { // from class: com.sessionm.ui.HTML5InlineVideoJSInterface.2
                @Override // java.lang.Runnable
                public void run() {
                    if (HTML5InlineVideoJSInterface.this.mReady) {
                        HTML5InlineVideoJSInterface.this.playVideo();
                    } else {
                        Log.w(HTML5InlineVideoJSInterface.TAG, "trying to play video before video is ready for playback");
                    }
                }
            });
        }
    }

    public void remove() {
        Log.i(TAG, "remove video");
        if (this.mActivity != null) {
            this.mActivity.runOnUiThread(new Runnable() { // from class: com.sessionm.ui.HTML5InlineVideoJSInterface.3
                @Override // java.lang.Runnable
                public void run() {
                    HTML5InlineVideoJSInterface.this.dismiss();
                }
            });
        }
    }

    public void pause() {
        Log.i(TAG, "pause video");
        if (this.mActivity != null) {
            this.mPlaying = false;
            this.mActivity.runOnUiThread(new Runnable() { // from class: com.sessionm.ui.HTML5InlineVideoJSInterface.4
                @Override // java.lang.Runnable
                public void run() {
                    if (HTML5InlineVideoJSInterface.this.mReady) {
                        HTML5InlineVideoJSInterface.this.pauseVideo();
                    } else {
                        Log.w(HTML5InlineVideoJSInterface.TAG, "trying to pause video before video is read for playback");
                    }
                }
            });
        }
    }

    public void seek(final int i) {
        Log.i(TAG, "seeking video");
        if (this.mActivity != null) {
            this.mActivity.runOnUiThread(new Runnable() { // from class: com.sessionm.ui.HTML5InlineVideoJSInterface.5
                @Override // java.lang.Runnable
                public void run() {
                    if (!HTML5InlineVideoJSInterface.this.mReady) {
                        Log.w(HTML5InlineVideoJSInterface.TAG, "trying to seek before video is ready for playback");
                    } else {
                        HTML5InlineVideoJSInterface.this.mVideoView.seekTo(i);
                        HTML5InlineVideoJSInterface.this.notifyWebView("seeking");
                    }
                }
            });
        }
    }

    public void move(final int i, final int i2) {
        this.mActivity.runOnUiThread(new Runnable() { // from class: com.sessionm.ui.HTML5InlineVideoJSInterface.6
            @Override // java.lang.Runnable
            public void run() {
                if (HTML5InlineVideoJSInterface.this.mVideoView == null) {
                    Log.w(HTML5InlineVideoJSInterface.TAG, "trying to move video view before it is initialized");
                    return;
                }
                HTML5InlineVideoJSInterface.this.mLayoutParams.leftMargin = HTML5InlineVideoJSInterface.this.convertDpToPixels(i);
                HTML5InlineVideoJSInterface.this.mLayoutParams.topMargin = HTML5InlineVideoJSInterface.this.convertDpToPixels(i2);
                HTML5InlineVideoJSInterface.this.mLayout.setLayoutParams(HTML5InlineVideoJSInterface.this.mLayoutParams);
                HTML5InlineVideoJSInterface.this.mLayout.invalidate();
                Log.i(HTML5InlineVideoJSInterface.TAG, "video is playing: " + HTML5InlineVideoJSInterface.this.mPlaying + " is ready: " + HTML5InlineVideoJSInterface.this.mReady + " x: " + i + " y: " + i2);
                if (i >= 0 && i2 >= 0) {
                    HTML5InlineVideoJSInterface.this.notifyWebView("moved");
                    return;
                }
                HTML5InlineVideoJSInterface.this.mVideoError = true;
                HTML5InlineVideoJSInterface.this.notifyWebView("error");
                HTML5InlineVideoJSInterface.this.remove();
            }
        });
    }

    public int getX() {
        if (this.mVideoView == null || !this.mReady) {
            return 0;
        }
        return convertPixelsToDp(this.mVideoView.getLeft());
    }

    public int getY() {
        if (this.mVideoView == null || !this.mReady) {
            return 0;
        }
        return convertPixelsToDp(this.mVideoView.getTop());
    }

    public int getWidth() {
        if (this.mVideoView == null || !this.mReady) {
            return 0;
        }
        return convertPixelsToDp(this.mVideoView.getWidth());
    }

    public int getHeight() {
        if (this.mVideoView == null || !this.mReady) {
            return 0;
        }
        return convertPixelsToDp(this.mVideoView.getHeight());
    }

    public boolean ready() {
        return this.mReady;
    }

    public boolean playing() {
        return this.mReady && this.mVideoView.isPlaying();
    }

    public int duration() {
        if (this.mReady) {
            return this.mVideoView.getDuration();
        }
        return 0;
    }

    public int currentTime() {
        if (this.mReady) {
            return this.mVideoView.getCurrentPosition();
        }
        return 0;
    }

    public int videoErrorWhat() {
        return this.mVideoErrorWhat;
    }

    public int videoErrorExtra() {
        return this.mVideoErrorExtra;
    }

    public int percentageComplete() {
        if (!this.mReady || this.mVideoView == null || this.mVideoView.getDuration() <= 0 || this.mVideoView.getCurrentPosition() <= 0) {
            return 0;
        }
        int currentPosition = (int) ((100.0d * ((double) this.mVideoView.getCurrentPosition())) / ((double) this.mVideoView.getDuration()));
        if (currentPosition <= 100) {
            return currentPosition;
        }
        return 100;
    }

    public void hide() {
        Log.i(TAG, "hide video");
        if (this.mActivity != null) {
            this.mPlaying = false;
            this.mActivity.runOnUiThread(new Runnable() { // from class: com.sessionm.ui.HTML5InlineVideoJSInterface.7
                @Override // java.lang.Runnable
                public void run() {
                    if (HTML5InlineVideoJSInterface.this.mReady) {
                        HTML5InlineVideoJSInterface.this.mLayout.setVisibility(4);
                    }
                }
            });
        }
    }

    public void show() {
        Log.i(TAG, "show video");
        if (this.mActivity != null) {
            this.mActivity.runOnUiThread(new Runnable() { // from class: com.sessionm.ui.HTML5InlineVideoJSInterface.8
                @Override // java.lang.Runnable
                public void run() {
                    if (HTML5InlineVideoJSInterface.this.mReady) {
                        HTML5InlineVideoJSInterface.this.mLayout.setVisibility(0);
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyWebView(final String str) {
        if (this.mActivity != null) {
            this.mActivity.runOnUiThread(new Runnable() { // from class: com.sessionm.ui.HTML5InlineVideoJSInterface.9
                @Override // java.lang.Runnable
                public void run() {
                    if (HTML5InlineVideoJSInterface.this.mWebView != null) {
                        try {
                            HTML5InlineVideoJSInterface.this.mWebView.loadUrl("javascript:" + HTML5InlineVideoJSInterface.this.mEventCallback + "(\"" + str + "\")");
                        } catch (NullPointerException e) {
                            Log.e(HTML5InlineVideoJSInterface.TAG, "NPE from web view, error: " + e);
                        }
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int convertDpToPixels(int i) {
        return (int) ((this.mActivity.getResources().getDisplayMetrics().density * i) + 0.5f);
    }

    private int convertPixelsToDp(int i) {
        if (this.mActivity.getResources().getDisplayMetrics().density == 0.0f) {
            return 0;
        }
        return (int) ((i / this.mActivity.getResources().getDisplayMetrics().density) + 0.5f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playVideo() {
        if (this.mReady && this.mVideoView != null && !this.mVideoView.isPlaying()) {
            this.mVideoView.start();
            checkProgress();
            notifyWebView("playing");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pauseVideo() {
        if (this.mReady && this.mVideoView != null && this.mVideoView.isPlaying()) {
            this.mVideoView.pause();
            checkProgress();
            notifyWebView("paused");
        }
    }

    private void addProgressTask() {
        this.mProgressTask = new VideoProgressTask();
        this.mProgressTimer.scheduleAtFixedRate(this.mProgressTask, 100L, 1000L);
    }

    private void removeProgressTask() {
        this.mProgressTask.cancel();
        this.mProgressTask = null;
        this.mProgressTimer.purge();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkProgress() {
        if (this.mProgressTimer != null) {
            if (this.mVideoView != null && this.mReady && this.mVideoView.isPlaying()) {
                if (this.mProgressTask == null) {
                    addProgressTask();
                }
            } else if (this.mProgressTask != null || this.mVideoView.isPlaying()) {
                removeProgressTask();
            }
        }
    }

    /* JADX INFO: compiled from: ProGuard */
    private class VideoProgressTask extends TimerTask {
        private VideoProgressTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            HTML5InlineVideoJSInterface.this.notifyWebView("timeupdate");
            HTML5InlineVideoJSInterface.this.checkProgress();
        }
    }
}
