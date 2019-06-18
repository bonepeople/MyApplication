package com.example.apple.myapplication.activity;

import android.animation.ArgbEvaluator;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.apple.myapplication.R;

public class AppBarActivity extends AppCompatActivity {
    private int startColor = 0xFF008D96, endColor = 0xFFFFFFFF;
    private LinearLayout item_title;
    private ArgbEvaluator evaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_bar);

        AppBarLayout appBarLayout = findViewById(R.id.appbar_layout);
        item_title = findViewById(R.id.item_title);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                float percent = i / (float) -140;
                item_title.setBackgroundColor((int) evaluator.evaluate(percent, startColor, endColor));
            }
        });


    }
}
