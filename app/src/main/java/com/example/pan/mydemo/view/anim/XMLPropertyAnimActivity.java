package com.example.pan.mydemo.view.anim;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;


public class XMLPropertyAnimActivity extends BaseActivity {
    private Toolbar mToolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmlproperty_anim);
        mToolBar = setSupportActionBar(R.id.tool_bar);
    }

    public void rotateAnimRun(View view) {
        Animator animator = AnimatorInflater.loadAnimator(this,R.animator.rotate_view_anim);
        animator.setTarget(view);
        animator.start();
    }

    public void rotateAnimRunListener(View view){
        Animator animator;
        if (view.getTag() != "1") {
            animator = AnimatorInflater.loadAnimator(this,R.animator.rotate_anim_listener);
            view.setTag("1");
        } else {
            animator = AnimatorInflater.loadAnimator(this,R.animator.rotate_anim_listener_reverse);
            view.setTag("0");
        }
        animator.setTarget(view);
        animator.start();
    }
}
