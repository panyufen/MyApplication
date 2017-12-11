package com.example.pan.mydemo.view.anim;

import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SeekBar;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;
import com.example.pan.mydemo.widget.InterpolatorView;
import com.example.pan.mydemo.widget.LoadingView;

public class PathAnimActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {
    private Toolbar mToolBar;
    private LoadingView mLoadingView;
    private InterpolatorView mInterpolatorView;
    private AppCompatSeekBar mSbExternalR, mSbInternalR, mSbRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_anim);
        mToolBar = setSupportActionBar(R.id.tool_bar);

        mLoadingView = (LoadingView) this.findViewById(R.id.loading_view);
        mSbExternalR = (AppCompatSeekBar) this.findViewById(R.id.sb_external_r);
        mSbInternalR = (AppCompatSeekBar) this.findViewById(R.id.sb_internal_r);
        mSbRate = (AppCompatSeekBar) this.findViewById(R.id.sb_rate);
        mSbExternalR.setOnSeekBarChangeListener(this);
        mSbInternalR.setOnSeekBarChangeListener(this);
        mSbRate.setOnSeekBarChangeListener(this);

        mLoadingView.start();

        mInterpolatorView = (InterpolatorView) findViewById(R.id.interpolator_view);
        mInterpolatorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterpolatorView.startAnim(5000);
            }
        });

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == mSbExternalR) {
            mLoadingView.setExternalRadius(progress);
        } else if (seekBar == mSbInternalR) {
            mLoadingView.setInternalRadius(progress);
        } else if (seekBar == mSbRate) {
            mLoadingView.setDuration(progress);
        }
    }

    /**
     * Notification that the user has started a touch gesture. Clients may want to use this
     * to disable advancing the seekbar.
     *
     * @param seekBar The SeekBar in which the touch gesture began
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    /**
     * Notification that the user has finished a touch gesture. Clients may want to use this
     * to re-enable advancing the seekbar.
     *
     * @param seekBar The SeekBar in which the touch gesture began
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoadingView.stop();
    }

}
