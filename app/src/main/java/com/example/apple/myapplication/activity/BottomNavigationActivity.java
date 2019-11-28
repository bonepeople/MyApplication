package com.example.apple.myapplication.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.apple.myapplication.R;
import com.example.apple.myapplication.fragment.TabPageFragment;

import java.util.ArrayList;

/**
 * 底部导航栏示例页面
 *
 * @author bonepeople
 */
public class BottomNavigationActivity extends AppCompatActivity {
    private BottomNavigationBar bottomNavigationBar;

    private ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        bottomNavigationBar = findViewById(R.id.bottomNavigationBar);

        bottomNavigationBar.setTabSelectedListener(
                new BottomNavigationBar.SimpleOnTabSelectedListener() {
                    @Override
                    public void onTabSelected(int position) {
                        showFragment(position);
                    }

                    @Override
                    public void onTabUnselected(int position) {
                        super.onTabUnselected(position);
                    }

                    @Override
                    public void onTabReselected(int position) {
                        super.onTabReselected(position);
                    }
                });

        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.ic_launcher, "首页").setActiveColor(0xFF669966));
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.ic_launcher, "旅行").setActiveColor(0xFF995522));
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.ic_launcher, "消息").setActiveColor(0xFF999922));
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.ic_launcher, "我的").setActiveColor(0xFF999999));
        bottomNavigationBar.setFirstSelectedPosition(0);
        bottomNavigationBar.initialise();

        initData();
    }

    private void initData() {
        fragments.add(TabPageFragment.create("1"));
        fragments.add(TabPageFragment.create("2"));
        fragments.add(TabPageFragment.create("3"));
        fragments.add(TabPageFragment.create("4"));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (Fragment fragment : fragments) {
            transaction.add(R.id.frameLayout_container, fragment);
        }
        transaction.commit();
        showFragment(0);
    }

    private void showFragment(int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (int temp_i = 0; temp_i < fragments.size(); temp_i++) {
            if (temp_i == index) {
                transaction.show(fragments.get(temp_i));
            } else {
                transaction.hide(fragments.get(temp_i));
            }
        }
        transaction.commit();
    }
}
