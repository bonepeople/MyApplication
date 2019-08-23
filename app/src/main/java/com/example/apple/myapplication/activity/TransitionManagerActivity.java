package com.example.apple.myapplication.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.apple.myapplication.R;
import com.example.apple.myapplication.activity.transition.LayoutTransitionActivity;

/**
 * 动画测试主界面
 */
public class TransitionManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_manager);
    }

    public void layout(View view) {
        startActivity(new Intent(this, LayoutTransitionActivity.class));
    }
}
