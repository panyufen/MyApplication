package com.example.pan.mydemo.opengl;

import com.cus.pan.library.utils.LogUtils;
import com.google.gson.Gson;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by PAN on 2017/4/17.
 */

public class Hexagon {
    private IntBuffer mVertexBuffer;
    private IntBuffer mColorBuffer;
    private ByteBuffer mIndexBuffer;
    int vCount = 0;
    int iCount = 0;

    public Hexagon(int zOffset) {
        //顶点坐标数据初始化
        vCount = 7;
        final int UNIT_SIZE = 10000;
        int vertices[] = new int[]{
                0 * UNIT_SIZE, 0 * UNIT_SIZE, zOffset * UNIT_SIZE,
                2 * UNIT_SIZE, 3 * UNIT_SIZE, zOffset * UNIT_SIZE,
                4 * UNIT_SIZE, 0 * UNIT_SIZE, zOffset * UNIT_SIZE,
                2 * UNIT_SIZE, -3 * UNIT_SIZE, zOffset * UNIT_SIZE,
                -2 * UNIT_SIZE, -3 * UNIT_SIZE, zOffset * UNIT_SIZE,
                -4 * UNIT_SIZE, 0 * UNIT_SIZE, zOffset * UNIT_SIZE,
                -2 * UNIT_SIZE, 3 * UNIT_SIZE, zOffset * UNIT_SIZE
        };
        LogUtils.i("Hexagon vertices "+new Gson().toJson(vertices));
        //创建顶点坐标数据缓冲
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4); //分配新内存
        vbb.order(ByteOrder.nativeOrder());                              //设置字节顺序
        mVertexBuffer = vbb.asIntBuffer();                               //转换为int型
        mVertexBuffer.put(vertices);                                     //向缓冲区放入顶点数据
        mVertexBuffer.position(0);                                       //设置起点位置
        //顶点着色数据初始化
        final int one = 65535;
        int colors[] = new int[]{
                0, 0, one, 0,
                0, one, 0, 0,
                0, one, one, 0,
                one, 0, 0, 0,
                one, 0, one, 0,
                one, one, 0, 0,
                one, one, one, 0
        };
        //创建顶点着色数据缓冲
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asIntBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);
        //三角形构造索引数据初始化
        iCount = 18;
        byte indices[] = new byte[]{
                0, 2, 1,
                0, 3, 2,
                0, 4, 3,
                0, 5, 4,
                0, 6, 5,
                0, 1, 6
        };
        //三角形数据缓冲
        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);
    }

    public void drawSelf(GL10 gl){
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glVertexPointer(3,GL10.GL_FIXED,0,mVertexBuffer);
        gl.glColorPointer(4,GL10.GL_FIXED,0,mColorBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLES,iCount,GL10.GL_UNSIGNED_BYTE,mIndexBuffer);
    }
}
