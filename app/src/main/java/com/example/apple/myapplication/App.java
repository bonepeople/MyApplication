package com.example.apple.myapplication;

import android.app.Application;

import com.aliyun.vod.common.httpfinal.QupaiHttpFinal;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        QupaiHttpFinal.getInstance().initOkHttpFinal();
    }
}
