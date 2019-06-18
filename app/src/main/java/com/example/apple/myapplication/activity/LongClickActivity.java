package com.example.apple.myapplication.activity;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.example.apple.myapplication.R;

public class LongClickActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_click);

        constraintLayout = findViewById(R.id.constraintLayout);
        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);
        checkBox4 = findViewById(R.id.checkBox4);
        checkBox5 = findViewById(R.id.checkBox5);
        checkBox6 = findViewById(R.id.checkBox6);

        constraintLayout.setOnClickListener(this);
        constraintLayout.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        checkBox1.setChecked(!checkBox1.isChecked());
    }

    @Override
    public boolean onLongClick(View v) {
        //在使用android:animateLayoutChanges="true"的情况下，直接设置显示属性会好于通过约束设置显示属性，
        // 通过约束调整显示属性后，显示控件是瞬间的行为，并且会在结束时有一个闪烁的效果。
        // 未使用android:animateLayoutChanges="true"的情况，两种方式效果一致，都没有过渡动画。
        ConstraintSet set = new ConstraintSet();
        set.clone(constraintLayout);
        set.setVisibility(R.id.checkBox1, ConstraintSet.VISIBLE);
        set.setVisibility(R.id.checkBox2, ConstraintSet.VISIBLE);
        set.setVisibility(R.id.checkBox3, ConstraintSet.VISIBLE);
        set.setVisibility(R.id.checkBox4, ConstraintSet.VISIBLE);
        set.setVisibility(R.id.checkBox5, ConstraintSet.VISIBLE);
        set.setVisibility(R.id.checkBox6, ConstraintSet.VISIBLE);
        set.applyTo(constraintLayout);


//        checkBox1.setVisibility(View.VISIBLE);
//        checkBox2.setVisibility(View.VISIBLE);
//        checkBox3.setVisibility(View.VISIBLE);
//        checkBox4.setVisibility(View.VISIBLE);
//        checkBox5.setVisibility(View.VISIBLE);
//        checkBox6.setVisibility(View.VISIBLE);
        return true;
    }
}
