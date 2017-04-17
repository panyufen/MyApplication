package com.example.pan.mydemo.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by PAN on 2017/2/3.
 */

public class Cube {
    public Cube() {
        int one = 0x10000;
        int vertices[] = {
                -one, -one, -one,   //左下后
                one, -one, -one,    //右下后
                one, one, -one,     //右上后
                -one, one, -one,    //左上后
                -one, one, one,     //左上前
                -one, -one, one,    //左下前
                one, -one, one,     //右下前
                one, one, one,      //右上前
        };

        int colors[] = {
                0, 0, 0, one,           //黑
                one, 0, 0, one,         //红
                one, one, 0, one,       //黄
                0, one, 0, one,         //绿
                0, 0, one, one,         //蓝
                one, 0, one, one,       //紫
                one, one, one, one,     //白
                0, one, one, one,       //青
        };

        byte indices[] = {
                0, 4, 5, 0, 5, 1,
                1, 5, 6, 1, 6, 2,
                2, 6, 7, 2, 7, 3,
                3, 7, 4, 3, 4, 0,
                4, 7, 6, 4, 6, 5,
                3, 0, 1, 3, 1, 2
        };

        //画12条边
        byte indexOrder[] = {
                0, 1, 1, 2, 2, 3,
                3, 0, 0, 5, 5, 4,
                4, 3, 4, 7, 7, 2,
                7, 6, 6, 1, 6, 5
        };


        // Buffers to be passed to gl*Pointer() functions
        // must be direct, i.e., they must be placed on the
        // native heap where the garbage collector cannot
        // move them.
        //
        // Buffers with multi-byte datatypes (e.g., short, int, float)
        // must have their byte order set to native order

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asIntBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asIntBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);

        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);

        mIndexOrderBuffer = ByteBuffer.allocateDirect(indexOrder.length);
        mIndexOrderBuffer.put(indexOrder);
        mIndexOrderBuffer.position(0);

    }

    public void draw(GL10 gl) {
        gl.glFrontFace(GL10.GL_CW);
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
        gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLES, 36, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);

        gl.glClear(GL10.GL_DEPTH_BUFFER_BIT);
        gl.glPointSize(20f);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDrawArrays(GL10.GL_POINTS, 0, 8);

        gl.glDrawElements(GL10.GL_LINES, 24, GL10.GL_UNSIGNED_BYTE, mIndexOrderBuffer);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

    }


    private IntBuffer mVertexBuffer;
    private IntBuffer mColorBuffer;
    private ByteBuffer mIndexBuffer;
    private ByteBuffer mIndexOrderBuffer;
}
