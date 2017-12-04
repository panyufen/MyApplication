package com.example.pan.mydemo.view;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

public class FrescoActivity extends BaseActivity {

    SimpleDraweeView mSimpleDraweeView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco);

        Uri uri = Uri.parse("http://img1.gtimg.com/news/pics/hv1/177/206/2158/140376657.jpg");
        mSimpleDraweeView = (SimpleDraweeView) findViewById(R.id.image_view);
//        mSimpleDraweeView.setImageURI(uri);

        ControllerListener listener = new BaseControllerListener() {
            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
            }
        };

        Postprocessor redMeshPostprocessor = new BasePostprocessor() {
            @Override
            public String getName() {
                return "redMeshPostprocessor";
            }

            @Override
            public void process(Bitmap bitmap) {
                for (int x = 0; x < bitmap.getWidth(); x += 4) {
                    for (int y = 0; y < bitmap.getHeight(); y += 4) {
                        bitmap.setPixel(x, y, Color.RED);
                    }
                }
            }
        };

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setPostprocessor(redMeshPostprocessor)
                .build();

        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());

        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(300)
                .setPlaceholderImage(R.mipmap.detecimg)
                .setProgressBarImage(new ProgressBarDrawable())
                .build();


        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setTapToRetryEnabled(true)
                .setImageRequest(request)
                .setOldController(mSimpleDraweeView.getController())
                .setControllerListener(listener)
                .build();

        mSimpleDraweeView.setController(controller);
//        mSimpleDraweeView.setHierarchy(hierarchy);

    }
}