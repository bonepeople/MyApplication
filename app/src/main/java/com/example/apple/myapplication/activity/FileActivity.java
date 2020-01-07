package com.example.apple.myapplication.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.apple.myapplication.R;
import com.example.apple.myapplication.utils.FileUtil;
import com.example.apple.myapplication.utils.LogUtil;
import com.example.apple.myapplication.utils.ToastUtil;

public class FileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_FILE_CHOOSER = 1;
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_FILE_CHOOSER: {
                    if (data == null || data.getData() == null)
                        return;
                    Uri uri = data.getData();
                    LogUtil.e("uri = " + uri.toString());
                    String path = FileUtil.getFilePath(this, uri);
                    LogUtil.e("path = " + path);
                    textView_path.setText(path);
                    break;
                }
            }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "选择一个文件呵呵"), REQUEST_FILE_CHOOSER);
        } catch (android.content.ActivityNotFoundException ex) {
            ToastUtil.showToast("Please install a File Manager.");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_file:
                showFileChooser();
                break;
            case R.id.button_folder:
                ToastUtil.showToast("folder");
                break;
        }
    }
}
