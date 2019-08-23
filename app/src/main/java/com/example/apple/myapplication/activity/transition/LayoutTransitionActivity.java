package com.example.apple.myapplication.activity.transition;

import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.myapplication.R;

/**
 * 布局动画的测试界面
 */
public class LayoutTransitionActivity extends AppCompatActivity {
    private ConstraintLayout constraintLayout_root;
    private ImageView imageView_transition_1, imageView_transition_2;
    private TextView textView_transition_1, textView_transition_2;

    private LayoutTransition layoutTransition_default, layoutTransition_custom;
    private boolean defaultTransition = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_transition);

        constraintLayout_root = findViewById(R.id.constraintLayout_root);
        imageView_transition_1 = findViewById(R.id.imageView_transition_1);
        imageView_transition_2 = findViewById(R.id.imageView_transition_2);
        textView_transition_1 = findViewById(R.id.textView_transition_1);
        textView_transition_2 = findViewById(R.id.textView_transition_2);

        initLayoutTransition();
    }

    public void hideTextView1(View view) {
        textView_transition_1.setVisibility(View.GONE);
    }

    public void showTextView1(View view) {
        textView_transition_1.setVisibility(View.VISIBLE);
    }

    public void changeLayoutTransition(View view) {
        Button button = (Button) view;
        if (defaultTransition) {
            constraintLayout_root.setLayoutTransition(layoutTransition_custom);
            button.setText("切换至默认动画");
        } else {
            constraintLayout_root.setLayoutTransition(layoutTransition_default);
            button.setText("切换至自定义动画");
        }
        defaultTransition = !defaultTransition;
    }

    /**
     * 初始化两套布局动画
     */
    private void initLayoutTransition() {
        layoutTransition_default = constraintLayout_root.getLayoutTransition();
        layoutTransition_custom = new LayoutTransition();
        /*自定义动画可以设置为单一属性动画，也可以设置为多属性的属性动画，也可以将多个属性动画放到AnimatorSet中使用
         * 动画的target可以随意指定，执行动画的时候会被替换为当时的控件
         * 动画的StartDelay需要使用LayoutTransition的方法设置
         * 直接设置动画的时长是无效的，需要使用LayoutTransition的方法设置时长*/

        //设置添加动画
        PropertyValuesHolder property_translation = PropertyValuesHolder.ofFloat("translationY", -20, 0);
        PropertyValuesHolder property_scaleX = PropertyValuesHolder.ofFloat("scaleX", 1);
        PropertyValuesHolder property_scaleY = PropertyValuesHolder.ofFloat("scaleY", 1);
        ObjectAnimator addAnimator = ObjectAnimator.ofPropertyValuesHolder(new View(this), property_translation, property_scaleX, property_scaleY);
        layoutTransition_custom.setAnimator(LayoutTransition.APPEARING, addAnimator);
        layoutTransition_custom.setDuration(LayoutTransition.APPEARING, 1000);
        layoutTransition_custom.setStartDelay(LayoutTransition.APPEARING, 3000);

        //设置移除动画
        ObjectAnimator deleteAnimator_translation = ObjectAnimator.ofFloat(null, "translationY", 0, 100);
        ObjectAnimator deleteAnimator_scaleX = ObjectAnimator.ofFloat(null, "scaleX", 1f, 0.5f);
        ObjectAnimator deleteAnimator_scaleY = ObjectAnimator.ofFloat(null, "scaleY", 1f, 0.5f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(deleteAnimator_translation, deleteAnimator_scaleX, deleteAnimator_scaleY);
        layoutTransition_custom.setAnimator(LayoutTransition.DISAPPEARING, animatorSet);
        layoutTransition_custom.setDuration(LayoutTransition.DISAPPEARING, 3000);

        //设置添加影响动画
        /*必须要有left、top、right、bottom，这样才能正常移动过去，如果设置了动画的值，该值是相对于父容器的坐标，如果不需要设置位置的情况就将值设置为1，0
         * 动画的第一个值和最后一个值必须相同，同时不能所有的属性值都相同*/
        PropertyValuesHolder changeLeft = PropertyValuesHolder.ofInt("left", 0, 1);
        PropertyValuesHolder changeTop = PropertyValuesHolder.ofInt("top", 0, 1);
        PropertyValuesHolder changeRight = PropertyValuesHolder.ofInt("right", 0, 40, 0);
        PropertyValuesHolder changeBottom = PropertyValuesHolder.ofInt("bottom", 0, 70, 0);
        PropertyValuesHolder changeRotation = PropertyValuesHolder.ofFloat("rotation", 0, 50, 0);
        PropertyValuesHolder changeTranslation = PropertyValuesHolder.ofFloat("translationY", 0, 350, 0);
        ObjectAnimator addMove = ObjectAnimator.ofPropertyValuesHolder(new View(this), changeLeft, changeTop, changeRight, changeBottom, changeRotation, changeTranslation);
        layoutTransition_custom.setAnimator(LayoutTransition.CHANGE_APPEARING, addMove);
        layoutTransition_custom.setDuration(LayoutTransition.CHANGE_APPEARING, 5000);

        //设置移除影响动画
        PropertyValuesHolder removeLeft = PropertyValuesHolder.ofInt("left", 0, 1);
        PropertyValuesHolder removeTop = PropertyValuesHolder.ofInt("top", 0, 1);
        PropertyValuesHolder removeRight = PropertyValuesHolder.ofInt("right", 0, 1);
        PropertyValuesHolder removeBottom = PropertyValuesHolder.ofInt("bottom", 0, 1);
        PropertyValuesHolder removeAlpha = PropertyValuesHolder.ofFloat("alpha", 1, 0.3f, 1);
        ObjectAnimator deleteMove = ObjectAnimator.ofPropertyValuesHolder(new View(this), removeLeft, removeTop, removeRight, removeBottom, removeAlpha);
        layoutTransition_custom.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, deleteMove);
        layoutTransition_custom.setDuration(LayoutTransition.CHANGE_DISAPPEARING, 5000);
    }
}
