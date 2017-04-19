package com.example.pan.mydemo.opengl;

import com.cus.pan.library.utils.LogUtils;
import com.google.gson.Gson;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by PAN on 2017/4/18.
 */
public class Ball {

    private IntBuffer mVertexBuffer;
    private IntBuffer mNormalBuffer;
    private IntBuffer mColorBuffer;
    private ByteBuffer mIndexBuffer;
    private final int angleSpan = 18;
    private int iCount = 0;
    private ArrayList<Integer> alVertix = new ArrayList<>();

    public int mAngleX = 0, mAngleY = 0, mAngleZ = 0;

    public Ball(int scale) {

        final int UNIT_SIZE = 13000;
        //创建球体
        for (int vAngle = -90; vAngle <= 90; vAngle = vAngle + angleSpan) {
            for (int hAngle = 0; hAngle < 360; hAngle = hAngle + angleSpan) {
                //横向纵向 到一个角度后计算坐标
                double xozLength = scale * UNIT_SIZE * Math.cos(Math.toRadians(vAngle));
                int x = (int) (xozLength * Math.cos(Math.toRadians(hAngle)));
                int y = (int) (xozLength * Math.sin(Math.toRadians(hAngle)));
                int z = (int) (scale * UNIT_SIZE * Math.sin(Math.toRadians(vAngle)));
                //将计算出的坐标放出顶点坐标List
                alVertix.add(x);
                alVertix.add(y);
                alVertix.add(z);
            }
        }

        int vertices[] = new int[alVertix.size()];
        for (int i = 0; i < alVertix.size(); i++) {
            vertices[i] = alVertix.get(i);
        }
        LogUtils.i("Ball vertices " + vertices.length + " " + new Gson().toJson(vertices));
        //创建顶点坐标缓冲
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asIntBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
        //创建顶点法向量缓冲
        ByteBuffer nbb = ByteBuffer.allocateDirect(vertices.length * 4);
        nbb.order(ByteOrder.nativeOrder());
        mNormalBuffer = nbb.asIntBuffer();
        mNormalBuffer.put(vertices);
        mNormalBuffer.position(0);

        ArrayList<Integer> alIndex = new ArrayList<>();
        int row = (180 / angleSpan) + 1;
        int col = 360 / angleSpan;
        //球体顶点索引
        for (int i = 0; i < row; i++) {
            if (i > 0 && i < row - 1) {
                //中间行
                for (int j = -1; j < col; j++) {
                    int k = i * col + j;
                    alIndex.add(k + col);
                    alIndex.add(k + 1);
                    alIndex.add(k);
                }
                for (int j = 0; j < col + 1; j++) {
                    int k = i * col + j;
                    alIndex.add(k - col);
                    alIndex.add(k - 1);
                    alIndex.add(k);
                }
            }
        }
        iCount = alIndex.size();
        byte indices[] = new byte[alIndex.size()];
        for (int i = 0; i < iCount; i++) {
            indices[i] = alIndex.get(i).byteValue();
        }
        LogUtils.i("Ball index " + indices.length + " " + new Gson().toJson(indices));
        //创建三角形构造数据缓冲
        mIndexBuffer = ByteBuffer.allocateDirect(iCount);
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);

//        int one = 0x10000;
//        int colorsSample[] = {
//                0, 0, 0, one,           //黑
//                one, 0, 0, one,         //红
//                one, one, 0, one,       //黄
//                0, one, 0, one,         //绿
//                0, 0, one, one,         //蓝
//                one, 0, one, one,       //紫
//                one, one, one, one,     //白
//                0, one, one, one,       //青
//        };
//        int colors[] = new int[vertices.length];
//        for (int i = 0; i < vertices.length; i++) {
//            colors[i] = colorsSample[i % colorsSample.length];
//        }
//        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
//        cbb.order(ByteOrder.nativeOrder());
//        mColorBuffer = cbb.asIntBuffer();
//        mColorBuffer.put(colors);
//        mColorBuffer.position(0);
    }

    public void drawSelf(GL10 gl) {
        gl.glRotatef(mAngleX, 0, 1, 0);
        gl.glRotatef(mAngleY, 1, 0, 0);
//        gl.glRotatef(mAngleZ, 0, 0, 1);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
//        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        //为画笔制定顶点坐标
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);

//        gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);

        //为画笔制定顶点法向量
        gl.glNormalPointer(GL10.GL_FIXED, 0, mNormalBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLES, iCount, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);

//        gl.glClear(GL10.GL_DEPTH_BUFFER_BIT);
//        gl.glPointSize(10f);
//        gl.glDrawArrays(GL10.GL_POINTS, 0, alVertix.size());
    }
}
