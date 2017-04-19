package com.example.pan.mydemo.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.cus.pan.library.utils.LogUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by PAN on 2017/4/18.
 */

public class LightSurfaceView extends GLSurfaceView {

    public boolean openLightFlag = false;
    private SceneRenderer mSceneRenderer;

    private final float TOUCH_SCALE_FACTOR = 0.3f;
    private float previousX = 0, previousY = 0;

    public LightSurfaceView(Context context) {
        this(context, null);
    }

    public LightSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSceneRenderer = new SceneRenderer();   //设置场景渲染器
        setRenderer(mSceneRenderer);            //设置渲染器
        setRenderMode(RENDERMODE_CONTINUOUSLY); //设置主动渲染模式
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dy = y - previousY;//计算触控笔移动Y位移
                float dx = x - previousX; //计算触控笔移动X位移
                mSceneRenderer.ball.mAngleX += dx * TOUCH_SCALE_FACTOR;//设置沿x轴旋转角度
                mSceneRenderer.ball.mAngleY += dy * TOUCH_SCALE_FACTOR;
                requestRender(); //渲染画面
                break;
        }
        previousX = x; //前一次触控位置x坐标
        previousY = y;
        return true;
    }

    private class SceneRenderer implements GLSurfaceView.Renderer {

        Ball ball = new Ball(4);

        public SceneRenderer() {}

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
//            gl.glDisable(GL10.GL_DITHER);
            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
            gl.glClearColor(0, 0, 0, 0); //背景黑色
            gl.glShadeModel(GL10.GL_SMOOTH);//平滑着色
            gl.glEnable(GL10.GL_DEPTH_TEST);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            gl.glViewport(0, 0, width, height);
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            float radio = (float) width / height;
            gl.glFrustumf(-radio, radio, -1, 1, 1, 10);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            LogUtils.i("onDrawFrame " + openLightFlag);
            if (openLightFlag) {
                gl.glEnable(GL10.GL_LIGHTING);//开启光照效果
                //初始化灯光
                initLight(gl);
                float[] positionParamsGreen = {2, 1, 0, 1};
                gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, positionParamsGreen, 0);
            } else {
                gl.glDisable(GL10.GL_LIGHTING);//关闭光照
            }
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();
            gl.glTranslatef(0, 0f, -1.8f);
            ball.drawSelf(gl);
            gl.glLoadIdentity();
        }

        private void initLight(GL10 gl) {
            gl.glEnable(GL10.GL_LIGHT0);
            float[] ambientParams = {0.1f, 0.1f, 0.1f, 1f};
            gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambientParams, 0);
            float[] diffuseParams = {0.5f, 0.5f, 0.5f, 1.0f};
            gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffuseParams, 0);
            float[] specularParams = {1.0f, 1.0f, 1.0f, 1.0f};
            gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specularParams, 0);

            float ambientMaterial[] = {0.4f, 0.4f, 0.4f, 1.0f};
            gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, ambientMaterial, 0);
            float diffuseMaterial[] = {0.8f, 0.8f, 0.8f, 1.0f};
            gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, diffuseMaterial, 0);
            float specularMaterial[] = {1f, 1f, 1f, 1.0f};
            gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, specularMaterial, 0);
            float shinimessMateiral[] = {1.5f};
            gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, shinimessMateiral, 0);
        }

    }
}
