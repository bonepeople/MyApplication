package com.example.apple.myapplication;

import android.Manifest;
import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.aliyun.common.utils.ToastUtil;
import com.aliyun.recorder.AliyunRecorderCreator;
import com.aliyun.recorder.supply.AliyunIClipManager;
import com.aliyun.recorder.supply.AliyunIRecorder;
import com.aliyun.recorder.supply.RecordCallback;
import com.aliyun.svideo.sdk.external.struct.common.VideoQuality;
import com.aliyun.svideo.sdk.external.struct.encoder.VideoCodecs;
import com.aliyun.svideo.sdk.external.struct.recorder.CameraParam;
import com.aliyun.svideo.sdk.external.struct.recorder.CameraType;
import com.aliyun.svideo.sdk.external.struct.recorder.FlashType;
import com.aliyun.svideo.sdk.external.struct.recorder.MediaInfo;
import com.example.apple.myapplication.utils.OrientationDetector;

import java.io.File;
import java.util.UUID;

import pub.devrel.easypermissions.EasyPermissions;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PERMISSION_ALL = 1;
    private String[] permissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SurfaceView surfaceView;
    private Button button_light, button_camera, button_record;
    private TextView textView_time;

    private AliyunIRecorder mRecorder;
    private AliyunIClipManager mClipManager;
    private FlashType flashType = FlashType.OFF;
    private CameraType cameraType = CameraType.BACK;
    private OrientationDetector mOrientationDetector;
    private int mMaxDuration = 15 * 1000;
    private String videoPath = "";
    private boolean recording = false, recordFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        initView();
        initData();
    }

    private void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        surfaceView = findViewById(R.id.surfaceView);
        button_light = findViewById(R.id.button_light);
        button_light.setOnClickListener(this);
        button_camera = findViewById(R.id.button_camera);
        button_camera.setOnClickListener(this);
        button_record = findViewById(R.id.button_record);
        button_record.setOnClickListener(this);
        textView_time = findViewById(R.id.textView_time);
        mOrientationDetector = new OrientationDetector(getApplicationContext());
    }

    private void initData() {
        if (!EasyPermissions.hasPermissions(this, permissions)) {
            EasyPermissions.requestPermissions(this, "需要相机相关权限", PERMISSION_ALL, permissions);
        } else {
            initSDK();
        }
    }

    private void initSDK() {
        mRecorder = AliyunRecorderCreator.getRecorderInstance(this);
        mRecorder.setDisplayView(surfaceView);


        mClipManager = mRecorder.getClipManager();
        mClipManager.setMinDuration(2 * 1000);
        mClipManager.setMaxDuration(mMaxDuration);
        MediaInfo info = new MediaInfo();
        info.setVideoWidth(720);
        info.setVideoHeight(1280);
        info.setVideoCodec(VideoCodecs.H264_HARDWARE);
        info.setCrf(25);
        mRecorder.setMediaInfo(info);
        mRecorder.setCamera(cameraType);
        mRecorder.setGop(5);
        mRecorder.setVideoBitrate(0);
        mRecorder.setVideoQuality(VideoQuality.HD);
        mRecorder.setExposureCompensationRatio(0.5f);
        mRecorder.setFocusMode(CameraParam.FOCUS_MODE_AUTO);
        videoPath = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + File.separator + UUID.randomUUID().toString() + ".mp4";
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                ToastUtil.showToast(this, "初始化异常");
                finishAfterTransition();
            }
        }
        mRecorder.setOutputPath(videoPath);

        mRecorder.setRecordCallback(new RecordCallback() {
            @Override
            public void onComplete(boolean validClip, long clipDuration) {
                recording = false;
                mRecorder.finishRecording();
            }

            @Override
            public void onFinish(String outputPath) {
                recordFinish = true;
                MediaScannerConnection.scanFile(getApplicationContext(), new String[]{outputPath}, new String[]{"video/mp4"}, null);
                mClipManager.deleteAllPart();
                runOnUiThread(() -> ToastUtil.showToast(RecordActivity.this, outputPath));
            }

            @Override
            public void onProgress(final long duration) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int time = (int) (mClipManager.getDuration() + duration) / 1000;
                        int min = time / 60;
                        int sec = time % 60;
                        textView_time.setText(String.format("%1$02d:%2$02d", min, sec));
                    }
                });
            }

            @Override
            public void onMaxDuration() {
                mRecorder.stopRecording();
                recording = false;
                if (flashType == FlashType.TORCH) {
                    changeLight();
                }
            }

            @Override
            public void onError(int errorCode) {
            }

            @Override
            public void onInitReady() {
            }

            @Override
            public void onDrawReady() {
            }

            @Override
            public void onPictureBack(Bitmap bitmap) {
            }

            @Override
            public void onPictureDataBack(byte[] data) {
            }

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (EasyPermissions.hasPermissions(this, this.permissions)) {
            initData();
        } else {
            finishAfterTransition();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mRecorder != null)
            mRecorder.startPreview();
        if (mOrientationDetector != null && mOrientationDetector.canDetectOrientation()) {
            mOrientationDetector.enable();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mRecorder == null)
            return;
        if (recording) {
            mRecorder.cancelRecording();
            recording = false;
        }
        mRecorder.stopPreview();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mOrientationDetector != null) {
            mOrientationDetector.disable();
        }
    }

    @Override
    public void onBackPressed() {
        if (!recording) {
            setResult(Activity.RESULT_CANCELED);
            finish();
            if (mClipManager != null) {
                mClipManager.deleteAllPart();//直接返回则删除所有临时文件
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mRecorder != null)
            mRecorder.destroy();
        super.onDestroy();
    }

    private void changeLight() {
        if (recording)
            return;
        if (cameraType == CameraType.FRONT) {
            flashType = FlashType.OFF;
        } else {
            switch (flashType) {
                case TORCH:
                default:
                    flashType = FlashType.OFF;
                    break;
                case OFF:
                    flashType = FlashType.TORCH;
                    break;
            }
        }
        mRecorder.setLight(flashType);
    }

    private void startRecording() {
        mRecorder.setRotation(getScreenRotation());
        mRecorder.startRecording();
        recording = true;
    }

    private int getScreenRotation() {
        int orientation = mOrientationDetector.getOrientation();
        int rotation = 90;
        if ((orientation >= 45) && (orientation < 135)) {
            rotation = 180;
        }
        if ((orientation >= 135) && (orientation < 225)) {
            rotation = 270;
        }
        if ((orientation >= 225) && (orientation < 315)) {
            rotation = 0;
        }
        if (cameraType == CameraType.FRONT) {
            if (rotation != 0) {
                rotation = 360 - rotation;
            }
        }
        return rotation;
    }

    private void stopRecording() {
        mRecorder.stopRecording();
        recording = false;
        if (flashType == FlashType.TORCH) {
            changeLight();
        }
    }

    private void switchCamera() {
        if (recording)
            return;
        int type = mRecorder.switchCamera();
        if (type == CameraType.BACK.getType()) {
            cameraType = CameraType.BACK;
        } else if (type == CameraType.FRONT.getType()) {
            cameraType = CameraType.FRONT;
            changeLight();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_light:
                changeLight();
                break;
            case R.id.button_camera:
                switchCamera();
                break;
            case R.id.button_record: {
                if (!recording)
                    startRecording();
                else
                    stopRecording();
                break;
            }
        }
    }
}

