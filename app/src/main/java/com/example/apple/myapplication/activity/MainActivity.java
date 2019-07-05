package com.example.apple.myapplication.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.apple.myapplication.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button_appbar).setOnClickListener(this);
        findViewById(R.id.button_click).setOnClickListener(this);
        findViewById(R.id.button_wakeup).setOnClickListener(this);
        findViewById(R.id.button_viewpager).setOnClickListener(this);
        findViewById(R.id.button_drawable).setOnClickListener(this);
        findViewById(R.id.button_viewGroup).setOnClickListener(this);
        findViewById(R.id.button_recycler).setOnClickListener(this);
        findViewById(R.id.button_dialog).setOnClickListener(this);
        findViewById(R.id.button_record).setOnClickListener(this);
        findViewById(R.id.button_update).setOnClickListener(this);
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
                startActivity(new Intent(this, AliyunPlayerActivity.class));
                break;
            case R.id.button_viewpager:
                startActivity(new Intent(this, ViewPagerActivity.class));
                break;
            case R.id.button_drawable:
                startActivity(new Intent(this, DrawableActivity.class));
                break;
            case R.id.button_viewGroup:
                startActivity(new Intent(this, CustomViewGroupActivity.class));
                break;
            case R.id.button_recycler:
                startActivity(new Intent(this, ItemDecorationActivity.class));
                break;
            case R.id.button_dialog:
                startActivity(new Intent(this, DialogActivity.class));
                break;
            case R.id.button_record:
                startActivity(new Intent(this, RecordActivity.class));
                break;
            case R.id.button_update:
                startActivity(new Intent(this, UpdateActivity.class));
                break;
        }
    }
}
