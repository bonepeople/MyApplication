package com.example.apple.myapplication.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aliyun.vodplayer.media.AliyunLocalSource;
import com.aliyun.vodplayer.media.AliyunVodPlayer;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.example.apple.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 全屏播放器
 *
 * @author bonepeople
 */
public class FullScreenVideoView extends ConstraintLayout implements SurfaceHolder.Callback, SeekBar.OnSeekBarChangeListener, IAliyunVodPlayer.OnPreparedListener, IAliyunVodPlayer.OnFirstFrameStartListener, IAliyunVodPlayer.OnLoadingListener, IAliyunVodPlayer.OnSeekCompleteListener, IAliyunVodPlayer.OnCompletionListener, IAliyunVodPlayer.OnErrorListener, IAliyunVodPlayer.OnBufferingUpdateListener, View.OnTouchListener, View.OnClickListener {
    public static final int STATE_CREATE = 0;
    public static final int STATE_READY = 1;
    public static final int STATE_PLAY = 2;
    public static final int STATE_PAUSE = 3;
    public static final int STATE_COMPLETE = 4;
    public static final int STATE_RESTART = 5;
    public static final int STATE_ERROR = 6;
    private static final String TAG = "FullScreenVideoView_tag";
    private ConstraintLayout constraintLayout_control, constraintLayout_error;
    private ImageView imageView_play;
    private SeekBar seekBar;
    private ProgressBar progressBar;
    private TextView textView_player_time, textView_error;

    private int state = STATE_CREATE;
    private String url;
    private AliyunVodPlayer player;
    private Disposable disposable_time;
    private CountDownTimer timer_disappear, timer_doubleClick;
    private int seekProgress = -1;
    private boolean prepared = false, playing = false, seeking = false, showingButton = false, finishPlaying = false, firstClick = false;
    private OnStateChangeListener onStateChangeListener;
    private OnProgressChangeListener onProgressChangeListener;
    private OnControlButtonShowListener onControlButtonShowListener;

    public FullScreenVideoView(Context context) {
        this(context, null);
    }

    public FullScreenVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FullScreenVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_custom_video_view, this, true);

        SurfaceView surfaceView = findViewById(R.id.surfaceView_player);
        surfaceView.getHolder().addCallback(this);
        surfaceView.setOnTouchListener(this);
        constraintLayout_control = findViewById(R.id.constraintLayout_player_control);
        constraintLayout_error = findViewById(R.id.constraintLayout_player_error);
        imageView_play = findViewById(R.id.imageView_player_play);
        imageView_play.setOnClickListener(this);
        seekBar = findViewById(R.id.seekBar_player);
        seekBar.setOnSeekBarChangeListener(this);
        ImageView imageView_fullScreen = findViewById(R.id.imageView_player_fullScreen);
        imageView_fullScreen.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar_player);
        textView_player_time = findViewById(R.id.textView_player_time);
        textView_error = findViewById(R.id.textView_player_error);
        TextView textView_refresh = findViewById(R.id.textView_player_refresh);
        textView_refresh.setOnClickListener(this);

        player = new AliyunVodPlayer(context);
        player.setOnPreparedListener(this);
        player.setOnFirstFrameStartListener(this);
        player.setOnLoadingListener(this);
        player.setOnSeekCompleteListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        player.setOnBufferingUpdateListener(this);

        disposable_time = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> updateProgress());
        timer_disappear = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                showControlButton(false);
            }
        };
        timer_doubleClick = new CountDownTimer(500, 500) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                firstClick = false;
                if (showingButton) {
                    showControlButton(false);
                    timer_disappear.cancel();
                } else {
                    showControlButton(true);
                    timer_disappear.start();
                }
            }
        };
    }

    /**
     * 设置视频播放地址
     *
     * @param url 视频链接
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    public void setOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener) {
        this.onProgressChangeListener = onProgressChangeListener;
    }

    public void setOnControlButtonShowListener(OnControlButtonShowListener onControlButtonShowListener) {
        this.onControlButtonShowListener = onControlButtonShowListener;
    }

    /**
     * 准备播放
     */
    public void prepare() {
        if (url == null)
            return;
        progressBar.setVisibility(VISIBLE);
        showControlButton(false);
        AliyunLocalSource.AliyunLocalSourceBuilder builder = new AliyunLocalSource.AliyunLocalSourceBuilder();
        builder.setSource(url);
        AliyunLocalSource source = builder.build();
        player.prepareAsync(source);
    }

    /**
     * 播放视频
     */
    public void resume() {
        if (!prepared)
            return;
        playing = true;
        imageView_play.setImageResource(R.mipmap.icon_video_pause);
        progressBar.setVisibility(INVISIBLE);
        player.resume();
        state = STATE_PLAY;
        if (onStateChangeListener != null)
            onStateChangeListener.onStateChange(this, state);
    }

    /**
     * 暂停视频
     */
    public void pause() {
        playing = false;
        imageView_play.setImageResource(R.mipmap.icon_video_play);
        player.pause();
        state = STATE_PAUSE;
        if (onStateChangeListener != null)
            onStateChangeListener.onStateChange(this, state);
    }

    /**
     * 设置控制按钮的是否展示
     */
    public void showControlButton(boolean show) {
        if (show) {
            constraintLayout_control.setVisibility(VISIBLE);
        } else {
            constraintLayout_control.setVisibility(GONE);
        }
        showingButton = show;
        if (onControlButtonShowListener != null)
            onControlButtonShowListener.onControlButtonVisible(this, showingButton);
    }

    /**
     * 销毁播放器
     * <p>在页面销毁的时候一定要主动销毁播放器</p>
     */
    public void destroy() {
        disposable_time.dispose();
        timer_disappear.cancel();
        timer_doubleClick.cancel();
        player.stop();
        player.release();
    }

    private void updateProgress() {
        if (playing && !seeking) {
            long duration = player.getDuration();
            long currentPosition = player.getCurrentPosition();
            long bufferPosition = player.getBufferingPosition();

            seekBar.setProgress((int) (100 * currentPosition / duration));
            seekBar.setSecondaryProgress((int) (100 * bufferPosition / duration));

            updateTime(currentPosition, duration);
            if (onProgressChangeListener != null)
                onProgressChangeListener.onProgressChange(this, currentPosition);
        }
    }

    private void updateTime(long currentPosition, long duration) {
        String time = formatTime(currentPosition) + "/" + formatTime(duration);
        textView_player_time.setText(time);
    }

    private String formatTime(long milliseconds) {
        SimpleDateFormat format;
        if (milliseconds > 60 * 60 * 1000)
            format = new SimpleDateFormat("H:mm:ss", Locale.getDefault());
        else
            format = new SimpleDateFormat("mm:ss", Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("GMT"));

        return format.format(new Date(milliseconds));
    }

    /**
     * 切换全屏
     */
    private void fullScreen() {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player.setDisplay(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        player.surfaceChanged();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            seekProgress = progress;
            long duration = player.getDuration();
            updateTime(seekProgress * duration / 100, duration);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        seekProgress = -1;
        seeking = true;
        timer_disappear.cancel();
        pause();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        timer_disappear.start();
        if (seekProgress != -1) {
            player.seekTo((int) (seekProgress * player.getDuration() / 100));
            progressBar.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onPrepared() {
        player.start();
    }

    @Override
    public void onFirstFrameStart() {
        prepared = true;
        pause();
        timer_disappear.cancel();
        timer_disappear.start();
        showControlButton(true);
        updateTime(0, player.getDuration());
        if (finishPlaying) {
            state = STATE_RESTART;
            finishPlaying = false;
        } else {
            state = STATE_READY;
        }
        if (onStateChangeListener != null)
            onStateChangeListener.onStateChange(this, state);
    }

    @Override
    public void onLoadStart() {
        progressBar.setVisibility(VISIBLE);
    }

    @Override
    public void onLoadEnd() {
        progressBar.setVisibility(INVISIBLE);
    }

    @Override
    public void onLoadProgress(int i) {

    }

    @Override
    public void onSeekComplete() {
        seeking = false;
        resume();
    }

    @Override
    public void onCompletion() {
        prepared = false;
        playing = false;
        finishPlaying = true;
        player.replay();
        seekBar.setProgress(0);
        updateTime(0, 0);
        state = STATE_COMPLETE;
        if (onStateChangeListener != null)
            onStateChangeListener.onStateChange(this, state);
    }

    @Override
    public void onError(int i, int i1, String s) {
        constraintLayout_error.setVisibility(VISIBLE);
        textView_error.setText(s);
        state = STATE_ERROR;
        if (onStateChangeListener != null)
            onStateChangeListener.onStateChange(this, state);
    }

    @Override
    public void onBufferingUpdate(int i) {
        Log.v(TAG, "onBufferingUpdate " + i);
        updateProgress();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {

                break;
            }
            case MotionEvent.ACTION_MOVE: {

                break;
            }
            case MotionEvent.ACTION_UP: {
                if (!firstClick) {
                    firstClick = true;
                    timer_doubleClick.start();
                } else {
                    firstClick = false;
                    timer_doubleClick.cancel();
                    timer_disappear.cancel();
                    timer_disappear.start();
                    if (playing)
                        pause();
                    else
                        resume();
                }
                break;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_player_play:
                timer_disappear.cancel();
                timer_disappear.start();
                if (playing)
                    pause();
                else
                    resume();
                break;
            case R.id.imageView_player_fullScreen:
                timer_disappear.cancel();
                timer_disappear.start();
                fullScreen();
                break;
            case R.id.textView_player_refresh:
                prepare();
                constraintLayout_error.setVisibility(GONE);
                break;
        }
    }

    /**
     * 播放状态切换的监听器
     */
    public interface OnStateChangeListener {
        void onStateChange(FullScreenVideoView view, int state);
    }

    /**
     * 播放进度更新的监听器
     */
    public interface OnProgressChangeListener {
        void onProgressChange(FullScreenVideoView view, long time);
    }

    /**
     * 播放界面变化的监听器
     */
    public interface OnControlButtonShowListener {
        void onControlButtonVisible(FullScreenVideoView view, boolean show);
    }
}
