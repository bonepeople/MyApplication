package com.example.apple.myapplication.utils;

import android.content.Context;
import android.view.OrientationEventListener;

public class OrientationDetector extends OrientationEventListener {
    private int orientation;

    public OrientationDetector(Context context) {
        super(context);
    }

    @Override
    public void onOrientationChanged(int orientation) {
        this.orientation = orientation;
    }

    public int getOrientation() {
        return orientation;
    }
}
