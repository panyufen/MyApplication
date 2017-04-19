package com.example.pan.mydemo.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by PAN on 2017/4/17.
 */

public class ProjectionSurfaceView extends GLSurfaceView {

    private final float TOUCH_SCALE_FACTOR = 180f / 320;   //角度缩放比例

    private SceneRenderer mSceneRenderer;

    public boolean isPerspecttive = false;

    private float mPreviousY = 0;

    private float xAngle = 0;

    public ProjectionSurfaceView(Context context) {
        this(context, null);
    }

    public ProjectionSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSceneRenderer = new SceneRenderer();   //设置场景渲染器
        setRenderer(mSceneRenderer);            //设置渲染器
        setRenderMode(RENDERMODE_CONTINUOUSLY); //设置主动渲染模式
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dy = y - mPreviousY;
                xAngle += dy * TOUCH_SCALE_FACTOR;
                requestRender();
                break;

        }
        mPreviousY = y;
        return true;
    }

    private class SceneRenderer implements GLSurfaceView.Renderer {

        Hexagon[] ha = new Hexagon[]{
                new Hexagon(0),
                new Hexagon(-2),
                new Hexagon(-4),
                new Hexagon(-6),
                new Hexagon(-8),
                new Hexagon(-10),
                new Hexagon(-12)
        };

        public SceneRenderer() {}

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST); //设置hint
            gl.glClearColor(0, 0, 0, 0);                                       //设置背景色为黑色
            gl.glEnable(GL10.GL_DEPTH_TEST);                                //启用深度测试
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            gl.glViewport(0, 0, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            float radio = 320f / 480;
            if (isPerspecttive) {
                gl.glFrustumf(-radio, radio, -1, 1, 1f, 10);
            } else {
                gl.glOrthof(-radio, radio, -1, 1, 1f, 10);
            }
//            gl.glEnable(GL10.GL_CULL_FACE);
            gl.glShadeModel(GL10.GL_SMOOTH);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();
            gl.glTranslatef(0, 0f, -1.4f);
            gl.glRotatef(xAngle, 1, 0, 0);
            for (Hexagon th : ha) {
                th.drawSelf(gl);
            }

        }
    }


}
