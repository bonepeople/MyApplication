package com.example.apple.myapplication.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.apple.myapplication.R;

public class LottieActivity extends AppCompatActivity implements View.OnClickListener, ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener, SeekBar.OnSeekBarChangeListener {
    private LottieAnimationView animationView_fix;
    private SeekBar seekBar;
    private TextView textView_status, textView_time;
    private boolean seeking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie);

        findViewById(R.id.button_start).setOnClickListener(this);
        findViewById(R.id.button_stop).setOnClickListener(this);
        findViewById(R.id.button_resume).setOnClickListener(this);
        findViewById(R.id.button_pause).setOnClickListener(this);
        animationView_fix = findViewById(R.id.lottieAnimationView_fix);
        seekBar = findViewById(R.id.seekBar);
        textView_status = findViewById(R.id.textView_status);
        textView_time = findViewById(R.id.textView_time);

        animationView_fix.addAnimatorUpdateListener(this);
        animationView_fix.addAnimatorListener(this);
        animationView_fix.setMaxFrame(80);
        animationView_fix.setMinFrame(40);
        seekBar.setOnSeekBarChangeListener(this);
        textView_status.setText("Ready");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_start:
                animationView_fix.playAnimation();
                break;
            case R.id.button_pause:
                animationView_fix.pauseAnimation();
                break;
            case R.id.button_resume:
                animationView_fix.resumeAnimation();
                break;
            case R.id.button_stop:
                animationView_fix.cancelAnimation();
                break;
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float value = (float) animation.getAnimatedValue();
        int frame = animationView_fix.getFrame();
        int percent = (int) (value * 100);
        Log.e("bone", "animation Value percent = " + percent + " frame = " + frame);
        if (!seeking)
            seekBar.setProgress(percent);
        textView_time.setText(String.valueOf(percent) + " - " + String.valueOf(frame));
    }

    @Override
    public void onAnimationStart(Animator animation) {
        Log.e("bone", "animation Status = Start");
        textView_status.setText("Start");
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        Log.e("bone", "animation Status = End");
        textView_status.setText("End");
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        Log.e("bone", "animation Status = Cancel");
        textView_status.setText("Cancel");
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        Log.e("bone", "animation Status = Repeat");
        textView_status.setText("Repeat");
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.d("bone", "onProgressChanged = " + progress);
        if (!animationView_fix.isAnimating() && fromUser) {
            animationView_fix.setProgress((float) progress / 100);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.d("bone", "onStartTrackingTouch");
        seeking = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.d("bone", "onStopTrackingTouch");
        seeking = false;
    }
}
