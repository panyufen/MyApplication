package com.example.pan.mydemo.view;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileReaderActivity extends BaseActivity {

    private TextView textView;
    private String fileName = "fileread/test.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_reader);
        textView = (TextView) findViewById(R.id.text_view);
    }

    private InputStream getFileInputStream() {
        AssetManager assetManager = getAssets();
        try {
            return assetManager.open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void readAssetsFile(View v) {
        long startTime = System.nanoTime();
        InputStream is = null;
        String result = "";
        try {
            is = getFileInputStream();
            if (is != null) {

                switch (v.getId()) {
                    case R.id.read_by_inputstream:
                        result = readByInputStream(is);
                        break;

                    case R.id.read_by_bufferredinputstream:
                        result = readByBufferredInputStream(is);
                        break;

                    case R.id.read_by_inputstreamreader:
                        result = readByInputStreamReader(is);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.nanoTime();
        setTextView(startTime, endTime, result);
    }

    private void setTextView(long s, long e, String result) {
        String result0 = "读取时间：" + (e - s) + "\n";
        if (TextUtils.isEmpty(result)) {
            result0 += "read failed";
        } else {
            result0 += "读取了：" + result.length() + " 个文字\n" + result;
        }

        textView.setText(textView.getText().toString() + "\n" + result0);
    }

    private String readByInputStream(InputStream is) throws IOException {
        int len = is.available();
        byte[] buffer = new byte[len];
        is.read(buffer);
        return new String(buffer, "utf-8");
    }


    private String readByBufferredInputStream(InputStream is) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(is, 1024 * 1024);
        int lenght = bufferedInputStream.available();
        byte[] buffer = new byte[lenght];
        bufferedInputStream.read(buffer);
        return new String(buffer, "utf-8");
    }


    private String readByInputStreamReader(InputStream is) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int len = 0;
        char[] tempLine = new char[1024];
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "utf-8"), 1024 * 1024);
        while ((len = bufferedReader.read(tempLine)) != -1) {
            stringBuilder.append(tempLine, 0, len);
        }
        return stringBuilder.toString();
    }

}
