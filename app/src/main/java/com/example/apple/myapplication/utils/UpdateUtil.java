package com.example.apple.myapplication.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 版本更新工具类
 *
 * @author bonepeople
 */
public class UpdateUtil {
    private static Call call;
    private static File downloadFile;
    private static String updateUrl = "";
    private static AppUpdateListener listener;

    /**
     * 设置版本更新的状态监听器
     */
    public static void setListener(AppUpdateListener listener) {
        UpdateUtil.listener = listener;
    }

    /**
     * 检查版本
     * <p>检查之前请先设置AppUpdateListener</p>
     */
    public static void checkVersion() {
        updateUrl = "https://resources.mydaydream.com/file/app_apk/mydaydream_2.0.4-debug%4020190708.apk";
        if (listener != null)
            listener.showUpdateDialog();
    }

    /**
     * 开始下载
     * <p>开始下载之前请先调用checkVersion进行版本检查，下载的路径是在版本检查时获取的</p>
     */
    public static void startDownload(@Nullable Context context) {
        if (context == null || updateUrl.isEmpty() || !initDownload(context)) {
            return;
        }

        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(updateUrl).build();
        call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (listener != null)
                    listener.onUpdateError();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    ResponseBody body = response.body();
                    if (body == null) {
                        if (listener != null)
                            listener.onUpdateError();
                        return;
                    }
                    long totalLength = body.contentLength();
                    InputStream inputStream = body.byteStream();
                    byte[] buffer = new byte[2048];
                    int length;
                    long downloadLength = 0;
                    RandomAccessFile randomAccessFile = new RandomAccessFile(downloadFile, "rw");
                    while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
                        randomAccessFile.write(buffer, 0, length);
                        downloadLength += length;
                        if (listener != null)
                            listener.onUpdateProgressChanged((int) (downloadLength * 100 / totalLength));
                    }
                    randomAccessFile.close();
                    inputStream.close();
                    body.close();
                    if (listener != null)
                        listener.onAppInstall();
                    installApk(context);
                } catch (Exception e) {
                    if (listener != null)
                        listener.onUpdateError();
                }
            }
        });
    }

    /**
     * 停止下载
     */
    public static void stopDownload() {
        if (call != null && call.isExecuted()) {
            call.cancel();
        }
        call = null;
        downloadFile = null;
    }

    /**
     * 初始化下载父路径
     */
    private static boolean initDownload(@NonNull Context context) {
        boolean prepare;
        File parentPath = new File(context.getExternalCacheDir(), "Update");
        if (!parentPath.exists()) {
            prepare = parentPath.mkdir();
        } else {
            prepare = true;
        }
        if (!prepare) {
            if (listener != null) {
                listener.onUpdateError();
            }
            return false;
        }
        downloadFile = new File(parentPath, "updateApp.apk");
        return true;
    }

    /**
     * 安装apk
     */
    private static void installApk(@NonNull Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uri = FileProvider.getUriForFile(context, context.getPackageName(), downloadFile);
        } else {
            uri = Uri.fromFile(downloadFile);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * APP更新状态的回调接口
     */
    public interface AppUpdateListener {

        void showUpdateDialog();

        void onUpdateProgressChanged(int progress);

        void onUpdateError();

        void onAppInstall();
    }
}
