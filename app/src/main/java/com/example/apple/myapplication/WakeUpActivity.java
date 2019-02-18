package com.example.apple.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WakeUpActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button_wakeup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wake_up);

        button_wakeup = findViewById(R.id.button_wakeup);
        button_wakeup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            Intent intent = getPackageManager().getLaunchIntentForPackage("com.daydream.journey");
            startActivity(intent);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
