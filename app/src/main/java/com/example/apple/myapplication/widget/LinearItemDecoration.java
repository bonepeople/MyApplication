package com.example.apple.myapplication.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 线性布局RecyclerView的分割线
 * 适用于LinearLayoutManager
 *
 * @author bonepeople
 */
public class LinearItemDecoration extends RecyclerView.ItemDecoration {
    private int spacing;
    private Paint paint;
    private Rect bounds = new Rect();

    /**
     * 创建线性布局RecyclerView的分割线
     *
     * @param space 项目之间的距离 dp
     */
    public LinearItemDecoration(float space) {
        spacing = (int) (space * 2);
    }

    public LinearItemDecoration setColor(@ColorInt int color) {
        if (paint == null)
            paint = new Paint();
        paint.setColor(color);
        return this;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            switch (layoutManager.getOrientation()) {
                case LinearLayoutManager.HORIZONTAL:
                    if (parent.getChildLayoutPosition(view) != 0)
                        outRect.set(spacing, 0, 0, 0);
                    break;
                case LinearLayoutManager.VERTICAL:
                    if (parent.getChildLayoutPosition(view) != 0)
                        outRect.set(0, spacing, 0, 0);
                    break;
            }
        }
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (paint == null)
            return;
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            switch (layoutManager.getOrientation()) {
                case LinearLayoutManager.HORIZONTAL:
                    drawHorizontal(canvas, parent);
                    break;
                case LinearLayoutManager.VERTICAL:
                    drawVertical(canvas, parent);
                    break;
            }
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        int left;
        int right;
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right, parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; ++i) {
            View child = parent.getChildAt(i);
            if (parent.getChildLayoutPosition(child) == 0)
                continue;
            parent.getDecoratedBoundsWithMargins(child, bounds);
            int top = bounds.top + Math.round(child.getTranslationY());
            int bottom = top + spacing;
            canvas.drawRect(left, top, right, bottom, paint);
        }
        canvas.restore();
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
        int top;
        int bottom;
        if (parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(parent.getPaddingLeft(), top, parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = 0;
            bottom = parent.getHeight();
        }

        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; ++i) {
            View child = parent.getChildAt(i);
            if (parent.getChildLayoutPosition(child) == 0)
                continue;
            parent.getDecoratedBoundsWithMargins(child, bounds);
            int left = bounds.left + Math.round(child.getTranslationX());
            int right = left + spacing;
            canvas.drawRect(left, top, right, bottom, paint);
        }
        canvas.restore();
    }
}
