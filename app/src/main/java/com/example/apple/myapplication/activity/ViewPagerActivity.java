package com.example.apple.myapplication.activity;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apple.myapplication.R;

import java.util.ArrayList;

public class ViewPagerActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private static final String TAG = "viewpagerTest";
    private ViewPager viewPager;
    private MyAdapter adapter;
    private ArrayList<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);

        data.add("1");
        data.add("2");
        data.add("3");
        data.add("4");
        data.add("5");
        data.add("6");
        data.add("7");

        viewPager = findViewById(R.id.viewPager);
        adapter = new MyAdapter();
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

        findViewById(R.id.button_switch_hard).setOnClickListener(this);
        findViewById(R.id.button_switch_smooth).setOnClickListener(this);
        findViewById(R.id.button_refresh).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_switch_hard:
                viewPager.setCurrentItem(1, false);
                break;
            case R.id.button_switch_smooth:
                viewPager.setCurrentItem(1, true);
                break;
            case R.id.button_refresh:
                data.clear();
                for (int i = 0; i < 7; i++) {
                    data.add(String.valueOf(i + 110));
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }

    /**
     * 当页面偏移量变化的时候进行回调更新，每一次页面转换的时候都会回调，包括界面初始化后
     *
     * @param position             当前位置 [0,+]
     * @param positionOffset       偏移量百分比 [0,1)
     * @param positionOffsetPixels 偏移量像素值 [0,+]
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.i(TAG, "onPageScrolled: position = " + position + " offset = " + positionOffset + " px = " + positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        Log.i(TAG, "onPageSelected: position = " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.i(TAG, "onPageScrollStateChanged: state = " + state);
    }


    private class MyAdapter extends PagerAdapter {

        /**
         * 获取视图总数
         */
        @Override
        public int getCount() {
            return data.size();
        }

        /**
         * 判断当前视图是否与数据对应，该函数会被调用数次，依次使用view与object对比，每次页面切换至少比对两轮
         *
         * @param view 加入到container中的View
         * @param o    instantiateItem返回的Object
         */
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            Log.v(TAG, "isViewFromObject view = " + view.getTag() + " str = " + o);
            return view.getTag().equals(o);
        }

        /**
         * 将对应位置所需的视图添加到容器中
         * 在需要初始化视图的时候调用
         * 用户手动滚动视图后，在当前视图停止滚动(onPageScrollStateChanged = 0)后统一初始化新视图
         * 通过代码设置当前页面的时候，先初始化所需页面(包括左右页面)再滚动到目标页面
         *
         * @param container 视图容器
         * @param position  视图位置
         * @return 将标志信息返回
         */
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            Log.d(TAG, "instantiateItem @ " + position);
            TextView textView = new TextView(container.getContext());
            textView.setGravity(Gravity.CENTER);
            textView.setText(data.get(position));
            textView.setTag(data.get(position));
            container.addView(textView);
            return data.get(position);
        }

        /**
         * 将对应位置的的视图从容器中移除
         * 在视图不再使用的时候调用
         *
         * @param container 视图容器
         * @param position  视图位置
         * @param object    标志信息
         */
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            TextView textView = container.findViewWithTag(object);
            container.removeView(textView);
            Log.d(TAG, "destroyItem @ " + position);
        }

        /**
         * 获取目标页面的标题
         *
         * @param position 视图位置
         * @return 标题内容
         */
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            Log.d(TAG, "getPageTitle @ " + position);
            return super.getPageTitle(position);
        }

        /**
         * 设置当前显示的主页面，会调用多次
         *
         * @param container ViewPager
         * @param position  主页面位置 [0,+]
         * @param object    页面所带标志
         */
        @Override
        public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            Log.d(TAG, "setPrimaryItem position = " + position + " object = " + object);
            super.setPrimaryItem(container, position, object);
        }

        /**
         * 开始更新视图，会调用多次，增加视图、移除视图、设置主视图的时候都会调用
         *
         * @param container ViewPager
         */
        @Override
        public void startUpdate(@NonNull ViewGroup container) {
            Log.v(TAG, "startUpdate: super");
            super.startUpdate(container);
        }

        /**
         * 结束更新视图，会调用多次，增加视图、移除视图、设置主视图的时候都会调用
         *
         * @param container ViewPager
         */
        @Override
        public void finishUpdate(@NonNull ViewGroup container) {
            Log.v(TAG, "finishUpdate: super");
            super.finishUpdate(container);
        }

        /**
         * 保存状态
         * 程序退到后台的时候调用
         */
        @Nullable
        @Override
        public Parcelable saveState() {
            Log.d(TAG, "saveState: super");
            return super.saveState();
        }

        /**
         * 恢复状态
         * 调用时机未知
         */
        @Override
        public void restoreState(@Nullable Parcelable state, @Nullable ClassLoader loader) {
            Log.d(TAG, "restoreState: super");
            super.restoreState(state, loader);
        }

        /**
         * 获取页面相对宽度
         *
         * @param position 页面位置
         * @return 页面宽度百分比，该数值为内容宽度占ViewPager宽度的比值，默认为1
         */
        @Override
        public float getPageWidth(int position) {
            return super.getPageWidth(position);
        }

        /**
         * 获取该标志位所处的新位置
         *
         * @return 位置坐标 [-2,+] POSITION_UNCHANGED-位置未变化，不需要刷新、POSITION_NONE-不存在这个视图，会更新视图、其他位置推测会直接更改ViewPager的页码到对应的位置
         */
        @Override
        public int getItemPosition(@NonNull Object object) {
            Log.e(TAG, "getItemPosition: str = " + object);
            return super.getItemPosition(object);
        }
    }
}
