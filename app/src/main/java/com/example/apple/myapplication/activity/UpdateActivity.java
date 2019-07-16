package com.example.apple.myapplication.activity;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.apple.myapplication.R;
import com.example.apple.myapplication.utils.UpdateUtil;

public class UpdateActivity extends AppCompatActivity implements UpdateUtil.AppUpdateListener, View.OnClickListener {
    private TextView textView_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        findViewById(R.id.button_check).setOnClickListener(this);
        findViewById(R.id.button_stop).setOnClickListener(this);
        textView_progress = findViewById(R.id.textView_progress);

        UpdateUtil.setListener(this);
    }

    @Override
    protected void onDestroy() {
        UpdateUtil.setListener(null);
        UpdateUtil.stopDownload();
        super.onDestroy();
    }

    @Override
    public void showUpdateDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("版本更新")
                .setMessage("系统检测到您的版本过低，可能会影响到使用体验，是否更新到最新版本")
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("立即更新", null)
                .create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(0xFF484848);
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(v -> {
            alertDialog.dismiss();
            textView_progress.setText("0%");
            UpdateUtil.startDownload(this);
        });
    }

    @Override
    public void onUpdateProgressChanged(int progress) {
        runOnUiThread(() -> textView_progress.setText(progress + "%"));
    }

    @Override
    public void onUpdateError() {
        runOnUiThread(() -> textView_progress.setText("error"));
    }

    @Override
    public void onAppInstall() {
        runOnUiThread(() -> textView_progress.setText("install"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_check:
                UpdateUtil.checkVersion();
                break;
            case R.id.button_stop:
                UpdateUtil.stopDownload();
                break;
        }
    }
}
