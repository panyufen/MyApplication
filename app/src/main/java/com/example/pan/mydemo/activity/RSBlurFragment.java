package com.example.pan.mydemo.activity;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pan.mydemo.R;

/**
 * Created by paveldudka on 3/4/14.
 */
public class RSBlurFragment extends Fragment {
    private ImageView image;
    private TextView text;
    private TextView statusText;
    private CheckBox downScale;

    float downX,downY;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        image = (ImageView) view.findViewById(R.id.picture);
        text = (TextView) view.findViewById(R.id.text);
        image.setImageResource(R.drawable.picture);
        statusText = addStatusText((ViewGroup) view.findViewById(R.id.controls));
        addCheckBoxes((ViewGroup) view.findViewById(R.id.controls));
        applyBlur();
        text.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float denX = 0,denY = 0;
                switch( event.getAction() ){
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getRawX();
                        downY = event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        denX = event.getRawX()-downX;
                        denY = event.getRawY()-downY;
                        downX = event.getRawX();
                        downY = event.getRawY();

                        break;
                }

                Log.i("drag ",event.getRawX()+" "+event.getRawY()+" "+event.getX()+" "+event.getY());
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) v.getLayoutParams();
                layoutParams.leftMargin += (int)denX;
                layoutParams.topMargin += (int)denY;
                text.setLayoutParams(layoutParams);
                applyBlur();
                return true;
            }

        });
        return view;
    }

    private void applyBlur() {
        image.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                image.getViewTreeObserver().removeOnPreDrawListener(this);
                image.buildDrawingCache();

                Bitmap bmp = image.getDrawingCache();
                blur(bmp, text);
                return true;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();


        float scaleFactor = 1;
        float radius = 20;

        if (downScale.isChecked()) {
            scaleFactor = 8;
            radius = 2;
        }

        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth() / scaleFactor),
                (int) (view.getMeasuredHeight() / scaleFactor), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(overlay);

        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop() / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        RenderScript rs = RenderScript.create(getActivity());

        Allocation overlayAlloc = Allocation.createFromBitmap(
                rs, overlay);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(
                rs, overlayAlloc.getElement());

        blur.setInput(overlayAlloc);

        blur.setRadius(radius);

        blur.forEach(overlayAlloc);

        overlayAlloc.copyTo(overlay);

        view.setBackground(new BitmapDrawable(
                getResources(), overlay));

        rs.destroy();
        statusText.setText(System.currentTimeMillis() - startMs + "ms");
    }

    @Override
    public String toString() {
        return "RenderScript";
    }

    private TextView addStatusText(ViewGroup container) {
        TextView result = new TextView(getActivity());
        result.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        result.setTextColor(0xFFFFFFFF);
        container.addView(result);
        return result;
    }

    private void addCheckBoxes(ViewGroup container) {
        downScale = new CheckBox(getActivity());
        ViewGroup.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        downScale.setLayoutParams(lp);
        downScale.setText("高模糊");
        downScale.setVisibility(View.VISIBLE);
        downScale.setTextColor(0xFFFFFFFF);
        downScale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                applyBlur();
            }
        });
        container.addView(downScale);
    }
}
