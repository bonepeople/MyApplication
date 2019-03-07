package com.example.apple.myapplication;

import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DrawableActivity extends AppCompatActivity {
    private static final int tintColor = 0xFF669966;
    private TextView textView_normal, textView_round_5, textView_round_10, textView_border, textView_circle, textView_rectangle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable);

        textView_normal = findViewById(R.id.textView_normal);
        textView_round_5 = findViewById(R.id.textView_round_5);
        textView_round_10 = findViewById(R.id.textView_round_10);
        textView_border = findViewById(R.id.textView_border);
        textView_circle = findViewById(R.id.textView_circle);
        textView_rectangle = findViewById(R.id.textView_rectangle);

        textView_normal.setBackground(new NormalShape().getDrawable());
        textView_round_5.setBackground(new RoundShape().getDrawable(5));
        textView_round_10.setBackground(new RoundShape().getDrawable(10));
        textView_border.setBackground(new RingShape().getDrawable(10, 5));
        textView_circle.setBackground(new CircleShape().getDrawable());
        textView_rectangle.setBackground(new RectangleShape().getDrawable());
    }

    private class NormalShape {
        private Drawable getDrawable() {
            ShapeDrawable drawable = new ShapeDrawable();
            drawable.setTint(tintColor);
            return drawable;
        }
    }

    private class RoundShape {
        private Drawable getDrawable(int radius) {
            float[] outerRadii = {radius, radius, radius, radius, radius, radius, radius, radius};//左上x2,右上x2,右下x2,左下x2，注意顺序（顺时针依次设置）
            RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
            ShapeDrawable drawable = new ShapeDrawable(roundRectShape);
            drawable.setTint(tintColor);
            return drawable;
        }
    }

    private class RingShape {
        private Drawable getDrawable(int radius, int width) {
            float[] outerRadii = {radius, radius, radius, radius, radius, radius, radius, radius};//左上x2,右上x2,右下x2,左下x2，注意顺序（顺时针依次设置）
            RectF inset = new RectF(width, width, width, width);
            float[] innerRadii = {radius, radius, radius, radius, radius, radius, radius, radius};//内矩形 圆角半径
            RoundRectShape roundRectShape = new RoundRectShape(outerRadii, inset, innerRadii);
            ShapeDrawable drawable = new ShapeDrawable(roundRectShape);
            drawable.setTint(tintColor);
            return drawable;
        }
    }

    private class CircleShape extends RectShape {
        private float[] mOuterRadii;
        private RectF mInset;
        private float[] mInnerRadii;

        private RectF mInnerRect;
        private Path mPath; // this is what we actually draw

        //从RoundRectShape类借鉴
        public CircleShape() {
            mPath = new Path();
        }

        @Override
        public void draw(Canvas canvas, Paint paint) {
            canvas.drawPath(mPath, paint);
        }

        @Override
        public void getOutline(Outline outline) {
            if (mInnerRect != null) return; // have a hole, can't produce valid outline

            float radius = 0;
            if (mOuterRadii != null) {
                radius = mOuterRadii[0];
                for (int i = 1; i < 8; i++) {
                    if (mOuterRadii[i] != radius) {
                        // can't call simple constructors, use path
                        outline.setConvexPath(mPath);
                        return;
                    }
                }
            }

            final RectF rect = rect();
            outline.setRoundRect((int) Math.ceil(rect.left), (int) Math.ceil(rect.top),
                    (int) Math.floor(rect.right), (int) Math.floor(rect.bottom), radius);
        }

        @Override
        protected void onResize(float w, float h) {
            super.onResize(w, h);
            float outRadius = rect().height() / 2;
            mOuterRadii = new float[]{outRadius, outRadius, outRadius, outRadius, outRadius, outRadius, outRadius, outRadius};

            RectF r = rect();
            mPath.reset();

            if (mOuterRadii != null) {
                mPath.addRoundRect(r, mOuterRadii, Path.Direction.CW);
            } else {
                mPath.addRect(r, Path.Direction.CW);
            }
            if (mInnerRect != null) {
                mInnerRect.set(r.left + mInset.left, r.top + mInset.top,
                        r.right - mInset.right, r.bottom - mInset.bottom);
                if (mInnerRect.width() < w && mInnerRect.height() < h) {
                    if (mInnerRadii != null) {
                        mPath.addRoundRect(mInnerRect, mInnerRadii, Path.Direction.CCW);
                    } else {
                        mPath.addRect(mInnerRect, Path.Direction.CCW);
                    }
                }
            }
        }

        @Override
        public CircleShape clone() throws CloneNotSupportedException {
            final CircleShape shape = (CircleShape) super.clone();
            shape.mOuterRadii = mOuterRadii != null ? mOuterRadii.clone() : null;
            shape.mInnerRadii = mInnerRadii != null ? mInnerRadii.clone() : null;
            shape.mInset = new RectF(mInset);
            shape.mInnerRect = new RectF(mInnerRect);
            shape.mPath = new Path(mPath);
            return shape;
        }

        private Drawable getDrawable() {
            ShapeDrawable drawable = new ShapeDrawable(this);
            drawable.setTint(tintColor);
            return drawable;
        }
    }

    private class RectangleShape {
        private Drawable getDrawable() {
            float[] outerRadii = {50, 30, 30, 50, 50, 30, 30, 50};//左上x2,右上x2,右下x2,左下x2，注意顺序（顺时针依次设置）
            RectF inset = new RectF(30, 30, 30, 30);
            float[] innerRadii = {20, 10, 20, 10, 20, 10, 20, 10};//内矩形 圆角半径
            RoundRectShape roundRectShape = new RoundRectShape(outerRadii, inset, innerRadii);
            ShapeDrawable drawable = new ShapeDrawable(roundRectShape);
            drawable.setTint(tintColor);
            return drawable;
        }
    }
}
