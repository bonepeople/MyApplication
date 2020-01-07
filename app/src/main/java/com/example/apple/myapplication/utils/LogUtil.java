package com.example.apple.myapplication.utils;

import android.util.Log;

public class LogUtil {
    private static final String DEFAULT_TAG = "bone";
    private static final boolean SHOW = true;

    public static void e(String content) {
        e(DEFAULT_TAG, content);
    }

    public static void e(String tag, String content) {
        if (SHOW)
            Log.e(tag, content);
    }
}
