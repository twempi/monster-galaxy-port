package com.millennialmedia.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.MediaController;
import java.io.IOException;
import java.util.Map;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class VideoView extends SurfaceView implements MediaController.MediaPlayerControl {
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_RESUME = 7;
    private static final int STATE_SUSPEND = 6;
    private static final int STATE_SUSPEND_UNSUPPORTED = 8;
    private String TAG;
    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener;
    private boolean mCanPause;
    private boolean mCanSeekBack;
    private boolean mCanSeekForward;
    private MediaPlayer.OnCompletionListener mCompletionListener;
    private Context mContext;
    private int mCurrentBufferPercentage;
    private int mCurrentState;
    private int mDuration;
    private MediaPlayer.OnErrorListener mErrorListener;
    private Map<String, String> mHeaders;
    private MediaPlayer.OnInfoListener mInfoListener;
    private MediaController mMediaController;
    private MediaPlayer mMediaPlayer;
    private MediaPlayer.OnCompletionListener mOnCompletionListener;
    private MediaPlayer.OnErrorListener mOnErrorListener;
    private MediaPlayer.OnPreparedListener mOnPreparedListener;
    MediaPlayer.OnPreparedListener mPreparedListener;
    SurfaceHolder.Callback mSHCallback;
    private int mSeekWhenPrepared;
    MediaPlayer.OnVideoSizeChangedListener mSizeChangedListener;
    private int mStateWhenSuspended;
    private int mSurfaceHeight;
    private SurfaceHolder mSurfaceHolder;
    private int mSurfaceWidth;
    private int mTargetState;
    private Uri mUri;
    private int mVideoHeight;
    private int mVideoWidth;
    VideoPlayerActivity.VideoHackBgListener videoHackBgListener;

    public VideoView(Context context) {
        super(context);
        this.TAG = "VideoView";
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() { // from class: com.millennialmedia.android.VideoView.1
            @Override // android.media.MediaPlayer.OnVideoSizeChangedListener
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                VideoView.this.mVideoWidth = mp.getVideoWidth();
                VideoView.this.mVideoHeight = mp.getVideoHeight();
                if (VideoView.this.mVideoWidth != 0 && VideoView.this.mVideoHeight != 0) {
                    VideoView.this.getHolder().setFixedSize(VideoView.this.mVideoWidth, VideoView.this.mVideoHeight);
                }
            }
        };
        this.mPreparedListener = new MediaPlayer.OnPreparedListener() { // from class: com.millennialmedia.android.VideoView.2
            @Override // android.media.MediaPlayer.OnPreparedListener
            public void onPrepared(MediaPlayer mp) {
                if (VideoView.this.videoHackBgListener != null) {
                    VideoView.this.videoHackBgListener.callback(100L);
                }
                VideoView.this.mCurrentState = 2;
                VideoView.this.mCanPause = VideoView.this.mCanSeekBack = VideoView.this.mCanSeekForward = true;
                if (VideoView.this.mOnPreparedListener != null) {
                    VideoView.this.mOnPreparedListener.onPrepared(VideoView.this.mMediaPlayer);
                }
                if (VideoView.this.mMediaController != null) {
                    VideoView.this.mMediaController.setEnabled(true);
                }
                VideoView.this.mVideoWidth = mp.getVideoWidth();
                VideoView.this.mVideoHeight = mp.getVideoHeight();
                int seekToPosition = VideoView.this.mSeekWhenPrepared;
                if (seekToPosition != 0) {
                    VideoView.this.seekTo(seekToPosition);
                }
                if (VideoView.this.mVideoWidth == 0 || VideoView.this.mVideoHeight == 0) {
                    if (VideoView.this.mTargetState == 3) {
                        VideoView.this.start();
                        return;
                    }
                    return;
                }
                VideoView.this.getHolder().setFixedSize(VideoView.this.mVideoWidth, VideoView.this.mVideoHeight);
                if (VideoView.this.mSurfaceWidth == VideoView.this.mVideoWidth && VideoView.this.mSurfaceHeight == VideoView.this.mVideoHeight) {
                    if (VideoView.this.mTargetState == 3) {
                        VideoView.this.start();
                        if (VideoView.this.mMediaController != null) {
                            VideoView.this.mMediaController.show();
                            return;
                        }
                        return;
                    }
                    if (!VideoView.this.isPlaying()) {
                        if ((seekToPosition != 0 || VideoView.this.getCurrentPosition() > 0) && VideoView.this.mMediaController != null) {
                            VideoView.this.mMediaController.show(0);
                        }
                    }
                }
            }
        };
        this.mInfoListener = new MediaPlayer.OnInfoListener() { // from class: com.millennialmedia.android.VideoView.3
            @Override // android.media.MediaPlayer.OnInfoListener
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == 801) {
                    VideoView.this.mCanSeekBack = VideoView.this.mCanSeekForward = false;
                }
                return false;
            }
        };
        this.mCompletionListener = new MediaPlayer.OnCompletionListener() { // from class: com.millennialmedia.android.VideoView.4
            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer mp) {
                VideoView.this.mCurrentState = 5;
                VideoView.this.mTargetState = 5;
                if (VideoView.this.mMediaController != null) {
                    VideoView.this.mMediaController.hide();
                }
                if (VideoView.this.mOnCompletionListener != null) {
                    VideoView.this.mOnCompletionListener.onCompletion(VideoView.this.mMediaPlayer);
                }
            }
        };
        this.mErrorListener = new MediaPlayer.OnErrorListener() { // from class: com.millennialmedia.android.VideoView.5
            @Override // android.media.MediaPlayer.OnErrorListener
            public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
                int messageId;
                if (VideoView.this.videoHackBgListener != null) {
                    VideoView.this.videoHackBgListener.callback(0L);
                }
                MMAdViewSDK.Log.d("Error: " + framework_err + "," + impl_err);
                VideoView.this.mCurrentState = -1;
                VideoView.this.mTargetState = -1;
                if (VideoView.this.mMediaController != null) {
                    VideoView.this.mMediaController.hide();
                }
                if ((VideoView.this.mOnErrorListener == null || !VideoView.this.mOnErrorListener.onError(VideoView.this.mMediaPlayer, framework_err, impl_err)) && VideoView.this.getWindowToken() != null) {
                    VideoView.this.mContext.getResources();
                    if (framework_err == 200) {
                        messageId = android.R.string.VideoView_error_text_invalid_progressive_playback;
                    } else {
                        messageId = android.R.string.VideoView_error_text_unknown;
                    }
                    new AlertDialog.Builder(VideoView.this.mContext).setTitle("Sorry").setMessage(messageId).setPositiveButton("OK", new DialogInterface.OnClickListener() { // from class: com.millennialmedia.android.VideoView.5.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (VideoView.this.mOnCompletionListener != null) {
                                VideoView.this.mOnCompletionListener.onCompletion(VideoView.this.mMediaPlayer);
                            }
                        }
                    }).setCancelable(false).show();
                }
                return true;
            }
        };
        this.mBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() { // from class: com.millennialmedia.android.VideoView.6
            @Override // android.media.MediaPlayer.OnBufferingUpdateListener
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                VideoView.this.mCurrentBufferPercentage = percent;
            }
        };
        this.mSHCallback = new SurfaceHolder.Callback() { // from class: com.millennialmedia.android.VideoView.7
            @Override // android.view.SurfaceHolder.Callback
            public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
                VideoView.this.mSurfaceWidth = w;
                VideoView.this.mSurfaceHeight = h;
                boolean isValidState = VideoView.this.mTargetState == 3;
                boolean hasValidSize = VideoView.this.mVideoWidth == w && VideoView.this.mVideoHeight == h;
                if (VideoView.this.mMediaPlayer != null && isValidState && hasValidSize) {
                    if (VideoView.this.mSeekWhenPrepared != 0) {
                        VideoView.this.seekTo(VideoView.this.mSeekWhenPrepared);
                    }
                    VideoView.this.start();
                    if (VideoView.this.mMediaController != null) {
                        if (VideoView.this.mMediaController.isShowing()) {
                            VideoView.this.mMediaController.hide();
                        }
                        VideoView.this.mMediaController.show();
                    }
                }
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceCreated(SurfaceHolder holder) {
                VideoView.this.mSurfaceHolder = holder;
                if (VideoView.this.mMediaPlayer != null && VideoView.this.mCurrentState == 6 && VideoView.this.mTargetState == 7) {
                    VideoView.this.mMediaPlayer.setDisplay(VideoView.this.mSurfaceHolder);
                } else {
                    VideoView.this.openVideo();
                }
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceDestroyed(SurfaceHolder holder) {
                VideoView.this.mSurfaceHolder = null;
                if (VideoView.this.mMediaController != null) {
                    VideoView.this.mMediaController.hide();
                }
                if (VideoView.this.mCurrentState != 6) {
                    VideoView.this.release(true);
                }
            }
        };
        this.mContext = context;
        initVideoView();
    }

    public VideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.mContext = context;
        initVideoView();
    }

    public VideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.TAG = "VideoView";
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() { // from class: com.millennialmedia.android.VideoView.1
            @Override // android.media.MediaPlayer.OnVideoSizeChangedListener
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                VideoView.this.mVideoWidth = mp.getVideoWidth();
                VideoView.this.mVideoHeight = mp.getVideoHeight();
                if (VideoView.this.mVideoWidth != 0 && VideoView.this.mVideoHeight != 0) {
                    VideoView.this.getHolder().setFixedSize(VideoView.this.mVideoWidth, VideoView.this.mVideoHeight);
                }
            }
        };
        this.mPreparedListener = new MediaPlayer.OnPreparedListener() { // from class: com.millennialmedia.android.VideoView.2
            @Override // android.media.MediaPlayer.OnPreparedListener
            public void onPrepared(MediaPlayer mp) {
                if (VideoView.this.videoHackBgListener != null) {
                    VideoView.this.videoHackBgListener.callback(100L);
                }
                VideoView.this.mCurrentState = 2;
                VideoView.this.mCanPause = VideoView.this.mCanSeekBack = VideoView.this.mCanSeekForward = true;
                if (VideoView.this.mOnPreparedListener != null) {
                    VideoView.this.mOnPreparedListener.onPrepared(VideoView.this.mMediaPlayer);
                }
                if (VideoView.this.mMediaController != null) {
                    VideoView.this.mMediaController.setEnabled(true);
                }
                VideoView.this.mVideoWidth = mp.getVideoWidth();
                VideoView.this.mVideoHeight = mp.getVideoHeight();
                int seekToPosition = VideoView.this.mSeekWhenPrepared;
                if (seekToPosition != 0) {
                    VideoView.this.seekTo(seekToPosition);
                }
                if (VideoView.this.mVideoWidth == 0 || VideoView.this.mVideoHeight == 0) {
                    if (VideoView.this.mTargetState == 3) {
                        VideoView.this.start();
                        return;
                    }
                    return;
                }
                VideoView.this.getHolder().setFixedSize(VideoView.this.mVideoWidth, VideoView.this.mVideoHeight);
                if (VideoView.this.mSurfaceWidth == VideoView.this.mVideoWidth && VideoView.this.mSurfaceHeight == VideoView.this.mVideoHeight) {
                    if (VideoView.this.mTargetState == 3) {
                        VideoView.this.start();
                        if (VideoView.this.mMediaController != null) {
                            VideoView.this.mMediaController.show();
                            return;
                        }
                        return;
                    }
                    if (!VideoView.this.isPlaying()) {
                        if ((seekToPosition != 0 || VideoView.this.getCurrentPosition() > 0) && VideoView.this.mMediaController != null) {
                            VideoView.this.mMediaController.show(0);
                        }
                    }
                }
            }
        };
        this.mInfoListener = new MediaPlayer.OnInfoListener() { // from class: com.millennialmedia.android.VideoView.3
            @Override // android.media.MediaPlayer.OnInfoListener
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == 801) {
                    VideoView.this.mCanSeekBack = VideoView.this.mCanSeekForward = false;
                }
                return false;
            }
        };
        this.mCompletionListener = new MediaPlayer.OnCompletionListener() { // from class: com.millennialmedia.android.VideoView.4
            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer mp) {
                VideoView.this.mCurrentState = 5;
                VideoView.this.mTargetState = 5;
                if (VideoView.this.mMediaController != null) {
                    VideoView.this.mMediaController.hide();
                }
                if (VideoView.this.mOnCompletionListener != null) {
                    VideoView.this.mOnCompletionListener.onCompletion(VideoView.this.mMediaPlayer);
                }
            }
        };
        this.mErrorListener = new MediaPlayer.OnErrorListener() { // from class: com.millennialmedia.android.VideoView.5
            @Override // android.media.MediaPlayer.OnErrorListener
            public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
                int messageId;
                if (VideoView.this.videoHackBgListener != null) {
                    VideoView.this.videoHackBgListener.callback(0L);
                }
                MMAdViewSDK.Log.d("Error: " + framework_err + "," + impl_err);
                VideoView.this.mCurrentState = -1;
                VideoView.this.mTargetState = -1;
                if (VideoView.this.mMediaController != null) {
                    VideoView.this.mMediaController.hide();
                }
                if ((VideoView.this.mOnErrorListener == null || !VideoView.this.mOnErrorListener.onError(VideoView.this.mMediaPlayer, framework_err, impl_err)) && VideoView.this.getWindowToken() != null) {
                    VideoView.this.mContext.getResources();
                    if (framework_err == 200) {
                        messageId = android.R.string.VideoView_error_text_invalid_progressive_playback;
                    } else {
                        messageId = android.R.string.VideoView_error_text_unknown;
                    }
                    new AlertDialog.Builder(VideoView.this.mContext).setTitle("Sorry").setMessage(messageId).setPositiveButton("OK", new DialogInterface.OnClickListener() { // from class: com.millennialmedia.android.VideoView.5.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (VideoView.this.mOnCompletionListener != null) {
                                VideoView.this.mOnCompletionListener.onCompletion(VideoView.this.mMediaPlayer);
                            }
                        }
                    }).setCancelable(false).show();
                }
                return true;
            }
        };
        this.mBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() { // from class: com.millennialmedia.android.VideoView.6
            @Override // android.media.MediaPlayer.OnBufferingUpdateListener
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                VideoView.this.mCurrentBufferPercentage = percent;
            }
        };
        this.mSHCallback = new SurfaceHolder.Callback() { // from class: com.millennialmedia.android.VideoView.7
            @Override // android.view.SurfaceHolder.Callback
            public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
                VideoView.this.mSurfaceWidth = w;
                VideoView.this.mSurfaceHeight = h;
                boolean isValidState = VideoView.this.mTargetState == 3;
                boolean hasValidSize = VideoView.this.mVideoWidth == w && VideoView.this.mVideoHeight == h;
                if (VideoView.this.mMediaPlayer != null && isValidState && hasValidSize) {
                    if (VideoView.this.mSeekWhenPrepared != 0) {
                        VideoView.this.seekTo(VideoView.this.mSeekWhenPrepared);
                    }
                    VideoView.this.start();
                    if (VideoView.this.mMediaController != null) {
                        if (VideoView.this.mMediaController.isShowing()) {
                            VideoView.this.mMediaController.hide();
                        }
                        VideoView.this.mMediaController.show();
                    }
                }
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceCreated(SurfaceHolder holder) {
                VideoView.this.mSurfaceHolder = holder;
                if (VideoView.this.mMediaPlayer != null && VideoView.this.mCurrentState == 6 && VideoView.this.mTargetState == 7) {
                    VideoView.this.mMediaPlayer.setDisplay(VideoView.this.mSurfaceHolder);
                } else {
                    VideoView.this.openVideo();
                }
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceDestroyed(SurfaceHolder holder) {
                VideoView.this.mSurfaceHolder = null;
                if (VideoView.this.mMediaController != null) {
                    VideoView.this.mMediaController.hide();
                }
                if (VideoView.this.mCurrentState != 6) {
                    VideoView.this.release(true);
                }
            }
        };
        this.mContext = context;
        initVideoView();
    }

    @Override // android.view.SurfaceView, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(this.mVideoWidth, widthMeasureSpec);
        int height = getDefaultSize(this.mVideoHeight, heightMeasureSpec);
        if (this.mVideoWidth > 0 && this.mVideoHeight > 0) {
            if (this.mVideoWidth * height > this.mVideoHeight * width) {
                height = (this.mVideoHeight * width) / this.mVideoWidth;
            } else if (this.mVideoWidth * height < this.mVideoHeight * width) {
                width = (this.mVideoWidth * height) / this.mVideoHeight;
            }
        }
        setMeasuredDimension(width, height);
    }

    public int resolveAdjustedSize(int desiredSize, int measureSpec) {
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case Integer.MIN_VALUE:
                int result = Math.min(desiredSize, specSize);
                return result;
            case 0:
                return desiredSize;
            case 1073741824:
                return specSize;
            default:
                return desiredSize;
        }
    }

    private void initVideoView() {
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
        getHolder().addCallback(this.mSHCallback);
        getHolder().setType(3);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        this.mCurrentState = 0;
        this.mTargetState = 0;
    }

    public void setVideoPath(String path) {
        setVideoURI(Uri.parse(path));
    }

    public void setVideoURI(Uri uri) {
        setVideoURI(uri, null);
    }

    public void setVideoURI(Uri uri, Map<String, String> headers) {
        this.mUri = uri;
        this.mHeaders = headers;
        this.mSeekWhenPrepared = 0;
        openVideo();
        requestLayout();
        invalidate();
    }

    public void stopPlayback() {
        if (this.mMediaPlayer != null) {
            if (this.mMediaPlayer.isPlaying()) {
                this.mMediaPlayer.stop();
            }
            this.mMediaPlayer.release();
            this.mUri = null;
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            this.mTargetState = 0;
        }
    }

    public void openVideo() {
        if (this.mUri != null && this.mSurfaceHolder != null) {
            Intent i = new Intent("com.android.music.musicservicecommand");
            i.putExtra("command", "pause");
            this.mContext.sendBroadcast(i);
            release(false);
            try {
                this.mMediaPlayer = new MediaPlayer();
                this.mMediaPlayer.setOnPreparedListener(this.mPreparedListener);
                this.mMediaPlayer.setOnVideoSizeChangedListener(this.mSizeChangedListener);
                this.mDuration = -1;
                this.mMediaPlayer.setOnCompletionListener(this.mCompletionListener);
                this.mMediaPlayer.setOnErrorListener(this.mErrorListener);
                this.mMediaPlayer.setOnBufferingUpdateListener(this.mBufferingUpdateListener);
                this.mCurrentBufferPercentage = 0;
                this.mMediaPlayer.setDataSource(this.mContext, this.mUri);
                this.mMediaPlayer.setDisplay(this.mSurfaceHolder);
                this.mMediaPlayer.setAudioStreamType(3);
                this.mMediaPlayer.setScreenOnWhilePlaying(true);
                this.mMediaPlayer.prepareAsync();
                this.mCurrentState = 1;
                attachMediaController();
            } catch (IOException ex) {
                Log.w(this.TAG, "Unable to open content: " + this.mUri, ex);
                this.mCurrentState = -1;
                this.mTargetState = -1;
                this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
            } catch (IllegalArgumentException ex2) {
                Log.w(this.TAG, "Unable to open content: " + this.mUri, ex2);
                this.mCurrentState = -1;
                this.mTargetState = -1;
                this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
            }
        }
    }

    public void setMediaController(MediaController controller) {
        if (this.mMediaController != null) {
            this.mMediaController.hide();
        }
        this.mMediaController = controller;
        attachMediaController();
    }

    private void attachMediaController() {
        if (this.mMediaPlayer != null && this.mMediaController != null) {
            this.mMediaController.setMediaPlayer(this);
            View anchorView = getParent() instanceof View ? (View) getParent() : this;
            this.mMediaController.setAnchorView(anchorView);
            this.mMediaController.setEnabled(isInPlaybackState());
        }
    }

    void setCallBack(VideoPlayerActivity.VideoHackBgListener listener) {
        this.videoHackBgListener = listener;
    }

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener l) {
        this.mOnPreparedListener = l;
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener l) {
        this.mOnCompletionListener = l;
    }

    public void setOnErrorListener(MediaPlayer.OnErrorListener l) {
        this.mOnErrorListener = l;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void release(boolean cleartargetstate) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.reset();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            if (cleartargetstate) {
                this.mTargetState = 0;
            }
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        if (isInPlaybackState() && this.mMediaController != null) {
            toggleMediaControlsVisiblity();
            return false;
        }
        return false;
    }

    @Override // android.view.View
    public boolean onTrackballEvent(MotionEvent ev) {
        if (isInPlaybackState() && this.mMediaController != null) {
            toggleMediaControlsVisiblity();
            return false;
        }
        return false;
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean isKeyCodeSupported = (keyCode == 4 || keyCode == 24 || keyCode == 25 || keyCode == 82 || keyCode == 5 || keyCode == 6) ? false : true;
        if (isInPlaybackState() && isKeyCodeSupported && this.mMediaController != null) {
            if (keyCode == 79 || keyCode == 85) {
                if (this.mMediaPlayer.isPlaying()) {
                    pause();
                    this.mMediaController.show();
                    return true;
                }
                start();
                this.mMediaController.hide();
                return true;
            }
            if (keyCode == 86 && this.mMediaPlayer.isPlaying()) {
                pause();
                this.mMediaController.show();
            } else {
                toggleMediaControlsVisiblity();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void toggleMediaControlsVisiblity() {
        if (this.mMediaController.isShowing()) {
            this.mMediaController.hide();
        } else {
            this.mMediaController.show();
        }
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public void start() {
        if (isInPlaybackState()) {
            this.mMediaPlayer.start();
            this.mCurrentState = 3;
        }
        this.mTargetState = 3;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public void pause() {
        if (isInPlaybackState() && this.mMediaPlayer.isPlaying()) {
            this.mMediaPlayer.pause();
            this.mCurrentState = 4;
        }
        this.mTargetState = 4;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public int getDuration() {
        if (isInPlaybackState()) {
            if (this.mDuration > 0) {
                return this.mDuration;
            }
            this.mDuration = this.mMediaPlayer.getDuration();
            return this.mDuration;
        }
        this.mDuration = -1;
        return this.mDuration;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return this.mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public void seekTo(int msec) {
        if (isInPlaybackState()) {
            this.mMediaPlayer.seekTo(msec);
            this.mSeekWhenPrepared = 0;
        } else {
            this.mSeekWhenPrepared = msec;
        }
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public boolean isPlaying() {
        return isInPlaybackState() && this.mMediaPlayer.isPlaying();
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public int getBufferPercentage() {
        if (this.mMediaPlayer != null) {
            return this.mCurrentBufferPercentage;
        }
        return 0;
    }

    private boolean isInPlaybackState() {
        return (this.mMediaPlayer == null || this.mCurrentState == -1 || this.mCurrentState == 0 || this.mCurrentState == 1) ? false : true;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public boolean canPause() {
        return this.mCanPause;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public boolean canSeekBackward() {
        return this.mCanSeekBack;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public boolean canSeekForward() {
        return this.mCanSeekForward;
    }

    public boolean isInErrorState() {
        return this.mCurrentState == -1;
    }
}
