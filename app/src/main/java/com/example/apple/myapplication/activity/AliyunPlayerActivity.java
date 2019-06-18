package com.example.apple.myapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.apple.myapplication.R;
import com.example.apple.myapplication.widget.FullScreenVideoView;

public class AliyunPlayerActivity extends AppCompatActivity implements View.OnClickListener, FullScreenVideoView.OnStateChangeListener, FullScreenVideoView.OnProgressChangeListener, FullScreenVideoView.OnControlButtonShowListener {
    private FullScreenVideoView fullScreenVideoView;
    private TextView textView_hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aliyun_player);

        findViewById(R.id.button_prepare).setOnClickListener(this);
        textView_hint = findViewById(R.id.textView_hint);
        fullScreenVideoView = findViewById(R.id.fullScreenVideoView);
        fullScreenVideoView.setOnStateChangeListener(this);
        fullScreenVideoView.setOnProgressChangeListener(this);
        fullScreenVideoView.setOnControlButtonShowListener(this);
    }

    @Override
    protected void onPause() {
        fullScreenVideoView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        fullScreenVideoView.resume();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (fullScreenVideoView.isFullScreen())
            fullScreenVideoView.exitFullScreen();
        else
            super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        fullScreenVideoView.destroy();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_prepare:
                String url = "https://video.mydaydream.com/sv/59723e4a-1691317d171/59723e4a-1691317d171.mp4";
                fullScreenVideoView.setUrl(url);
                fullScreenVideoView.prepare();
                break;
        }
    }

    @Override
    public void onStateChange(FullScreenVideoView view, int state) {
        switch (state) {
            case FullScreenVideoView.STATE_READY:
                fullScreenVideoView.resume();
                break;
        }
    }

    @Override
    public void onProgressChange(FullScreenVideoView view, long time) {
        if (time > 15 * 1000)
            textView_hint.setText("time > 15 @ " + time);
        else
            textView_hint.setText("time <= 15 @ " + time);
    }

    @Override
    public void onControlButtonVisible(FullScreenVideoView view, boolean show) {
        if (show)
            textView_hint.setTextColor(0xFF000000);
        else
            textView_hint.setTextColor(0xFF666666);
    }
}
