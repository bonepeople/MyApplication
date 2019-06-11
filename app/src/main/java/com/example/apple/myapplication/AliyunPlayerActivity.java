package com.example.apple.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.aliyun.vodplayer.media.AliyunLocalSource;
import com.aliyun.vodplayer.media.AliyunVodPlayer;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;

public class AliyunPlayerActivity extends AppCompatActivity implements View.OnClickListener, IAliyunVodPlayer.OnPreparedListener, IAliyunVodPlayer.OnFirstFrameStartListener, IAliyunVodPlayer.OnErrorListener, IAliyunVodPlayer.OnCompletionListener, IAliyunVodPlayer.OnStoppedListener {
    private AliyunVodPlayer player;
    private SurfaceView surfaceView;
    private static final String TAG = "aliPlayer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aliyun_player);

        findViewById(R.id.button_wakeup).setOnClickListener(this);
        findViewById(R.id.button_play).setOnClickListener(this);
        surfaceView = findViewById(R.id.surfaceView);

        player = new AliyunVodPlayer(this);
        player.setOnPreparedListener(this);
        player.setOnFirstFrameStartListener(this);
        player.setOnErrorListener(this);
        player.setOnCompletionListener(this);
        player.setOnStoppedListner(this);


        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.e(TAG, "surfaceCreated");
                player.setDisplay(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.e(TAG, "surfaceChanged");
                player.surfaceChanged();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.e(TAG, "surfaceDestroyed");
            }
        });
    }

    @Override
    protected void onPause() {
        Log.w(TAG, "onPause");
        player.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.w(TAG, "onResume");
        player.resume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.w(TAG, "onDestroy");
        player.release();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_wakeup:
                String url = "https://video.mydaydream.com/sv/59723e4a-1691317d171/59723e4a-1691317d171.mp4";
                AliyunLocalSource.AliyunLocalSourceBuilder builder = new AliyunLocalSource.AliyunLocalSourceBuilder();
                builder.setSource(url);
                AliyunLocalSource source = builder.build();
                player.prepareAsync(source);
                break;
            case R.id.button_play:
                player.start();
                break;
        }
    }

    @Override
    public void onPrepared() {
        Toast.makeText(this, "onPrepared", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onPrepared");
        player.start();
    }

    @Override
    public void onFirstFrameStart() {
        Toast.makeText(this, "onFirstFrameStart", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onFirstFrameStart");
        player.pause();
    }

    @Override
    public void onError(int i, int i1, String s) {
        Toast.makeText(this, "onError", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onError " + i);
    }

    @Override
    public void onCompletion() {
        Toast.makeText(this, "onFirstFrameStart", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onFirstFrameStart");
    }

    @Override
    public void onStopped() {
        Toast.makeText(this, "onStopped", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onStopped");
    }
}
