package com.example.apple.myapplication.activity.transition;

import android.support.constraint.ConstraintLayout;
import android.support.transition.AutoTransition;
import android.support.transition.ChangeBounds;
import android.support.transition.Explode;
import android.support.transition.Fade;
import android.support.transition.Scene;
import android.support.transition.Slide;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.apple.myapplication.R;

public class SceneTransitionActivity extends AppCompatActivity {
    private ConstraintLayout constraintLayout;
    private Scene scene1, scene2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_transition);

        initView();
    }

    private void initView() {
        constraintLayout = findViewById(R.id.constraintLayout);

        scene1 = Scene.getSceneForLayout(constraintLayout, R.layout.scene_1, this);
        View view = LayoutInflater.from(this).inflate(R.layout.scene_2, constraintLayout, false);
        scene2 = new Scene(constraintLayout, view);
    }

    public void scene1(View view) {
        AutoTransition autoTransition = new AutoTransition();
        autoTransition.addTransition(new Slide())
                .addTransition(new ChangeBounds())
                .addTransition(new Explode());
        Transition transition = new Fade();
        transition.setDuration(1000);
        TransitionManager.go(scene1, autoTransition);
    }

    public void scene2(View view) {
        AutoTransition autoTransition = new AutoTransition();
        autoTransition.addTransition(new Fade(Fade.OUT))
                .addTransition(new ChangeBounds())
                .addTransition(new Fade(Fade.IN));
        Transition transition = new Fade();
        transition.setDuration(1000);
        TransitionManager.go(scene2, transition);
    }
}
