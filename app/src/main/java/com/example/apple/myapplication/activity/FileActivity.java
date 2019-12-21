package com.example.apple.myapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.apple.myapplication.R;
import com.example.apple.myapplication.utils.ToastUtil;

public class FileActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textView_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        findViewById(R.id.button_file).setOnClickListener(this);
        findViewById(R.id.button_folder).setOnClickListener(this);
        textView_path = findViewById(R.id.textView_path);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_file:
                ToastUtil.showToast("file");
                break;
            case R.id.button_folder:
                ToastUtil.showToast("folder");
                break;
        }
    }
}
