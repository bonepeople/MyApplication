package com.example.apple.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button_appbar).setOnClickListener(this);
        findViewById(R.id.button_click).setOnClickListener(this);
        findViewById(R.id.button_wakeup).setOnClickListener(this);
        findViewById(R.id.button_viewpager).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_appbar:
                startActivity(new Intent(this, AppBarActivity.class));
                break;
            case R.id.button_click:
                startActivity(new Intent(this, LongClickActivity.class));
                break;
            case R.id.button_wakeup:
                startActivity(new Intent(this, WakeUpActivity.class));
                break;
            case R.id.button_viewpager:
                startActivity(new Intent(this, ViewPagerActivity.class));
                break;
        }
    }
}
