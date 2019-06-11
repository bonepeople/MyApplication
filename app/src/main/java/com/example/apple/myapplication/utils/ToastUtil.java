package com.example.apple.myapplication.utils;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Toast工具类
 * <p>请在Application的onCreate中进行初始化</p>
 *
 * @author bonepeople
 */
public class ToastUtil {
    @Nullable
    private static Application application;

    /**
     * 初始化工具类
     */
    public static void init(@Nullable Application application) {
        ToastUtil.application = application;
    }

    /**
     * 显示Toast提示
     *
     * @param content 提示内容
     */
    public static void showToast(@NonNull String content) {
        if (application == null)
            return;
        Toast.makeText(application, content, Toast.LENGTH_SHORT).show();
    }
}
