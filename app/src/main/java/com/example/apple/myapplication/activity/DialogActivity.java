package com.example.apple.myapplication.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.apple.myapplication.R;

/**
 * 测试各种各样的对话框
 *
 * @author bonepeople
 */
public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
    }

    public void showAlertDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("AlertDialog");
        builder.setPositiveButton("关闭", null);
        builder.show();
    }

    public void showCustomDialog(View view) {
        new customDialog(this).show();
    }

    public void showFullScreenDialog(View view) {
        new FullScreenDialog(this).show();
    }

    public void showAnimationDialog(View view) {
        new AnimationDialog(this).show();
    }

    private static class customDialog extends AlertDialog {

        customDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_simple);
        }
    }

    private static class FullScreenDialog extends AlertDialog {

        public FullScreenDialog(@NonNull Context context) {
            super(context, R.style.FullScreenDialog);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_simple);

            Window window = getWindow();
            if (window != null) {
                window.setGravity(Gravity.BOTTOM);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            }
        }
    }

    private static class AnimationDialog extends AlertDialog {

        protected AnimationDialog(@NonNull Context context) {
            super(context, R.style.AnimationDialog);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_simple);

            Window window = getWindow();
            if (window != null) {
                window.setGravity(Gravity.BOTTOM);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            }
        }
    }
}
