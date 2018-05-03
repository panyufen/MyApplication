package com.example.pan.mydemo.view.ffmpeg;

import android.os.Bundle;

import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

public class FFmpegActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ffmpeg);
        init();
    }

    private void init() {
        FFmpeg ffmpeg = FFmpeg.getInstance(this);
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onStart() {}

                @Override
                public void onFailure() {
                    LogUtils.i("ffmpeg load lib failure");
                }

                @Override
                public void onSuccess() {
                    LogUtils.i("ffmpeg load lib success");
                    getVersion();
                }

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegNotSupportedException e) {
            // Handle if FFmpeg is not supported by device
        }
    }

    private void getVersion() {
        final String[] cmd = {"-version"};
        FFmpeg ffmpeg = FFmpeg.getInstance(this);
        try {
            // to execute "ffmpeg -version" command you just need to pass "-version"
            ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onProgress(String message) {}

                @Override
                public void onFailure(String message) {}

                @Override
                public void onSuccess(String message) {
                    LogUtils.i("ffmpeg message " + message);

                }

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // Handle if FFmpeg is already running
        }
    }
}
