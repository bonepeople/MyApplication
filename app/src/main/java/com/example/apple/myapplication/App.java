package com.example.apple.myapplication;

import android.app.Application;

import com.aliyun.vod.common.httpfinal.QupaiHttpFinal;
import com.example.apple.myapplication.utils.ToastUtil;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtil.init(this);
        QupaiHttpFinal.getInstance().initOkHttpFinal();
    }
}
