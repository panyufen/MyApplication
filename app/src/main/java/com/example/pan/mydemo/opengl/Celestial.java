package com.example.pan.mydemo.opengl;

import android.content.Context;
import android.opengl.GLES30;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.utils.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Pan.
 */
//表示星空球的类
public class Celestial {

    Context context;
    final  float UNIT_SIZE=10.0f;

    private FloatBuffer mVertexBuffer;
    int vCount=0;

    float yAngle;
    float scale;

    String mVertexShader;
    String mFragmentShader;

    int mProgram;
    int muMVPMatrixHandle;
    int maPositionHandle;
    int uPointSizeHandle;

    public  Celestial(float scale,float yAngle,int vCount,SkySurfaceView mv,Context context){
        this.context=context;
   this.yAngle=yAngle;
        this.scale=scale;
        this.vCount=vCount;
        initVertexData();
        initShader(mv);

    }
    public  void initVertexData(){

        //顶点坐标数据的初始化
        float vertices[]=new float[vCount*3];
        for(int i=0;i<vCount;i++){
            //随机产生每个星星的xyz坐标
            double angleTempJD=Math.PI*2*Math.random();
            double angleTempWD=Math.PI*(Math.random()-0.5f);

            vertices[i*3]=(float)(UNIT_SIZE*Math.cos(angleTempWD)*Math.sin(angleTempJD));
            vertices[i*3+1]=(float)(UNIT_SIZE*Math.sin(angleTempWD));
            vertices[i*3+2]=(float)(UNIT_SIZE*Math.cos(angleTempWD)*Math.cos(angleTempJD));
        }
        //创建顶点坐标数据缓冲
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为float型缓冲
        mVertexBuffer.put(vertices);//向缓冲区中放入顶点数据
        mVertexBuffer.position(0);//设置缓冲区起始位置
    }
    void initShader(SkySurfaceView mv)
    {

        mVertexShader=ShaderUtil.loadFromRawFile(context, R.raw.vertex_xk);
        ShaderUtil.checkGlError("==ss==");
        mFragmentShader=ShaderUtil.loadFromRawFile(context,R.raw.frag_xk);
        ShaderUtil.checkGlError("==ss==");

        //基于顶点着色器与片元着色器创建程序
        ShaderUtil.checkGlError("==ss==");
        mProgram =ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中总变换矩阵引用
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
        //获取顶点尺寸参数引用
        uPointSizeHandle = GLES30.glGetUniformLocation(mProgram, "uPointSize");

    }
    public  void drawSelf()
    {
        GLES30.glUseProgram(mProgram); //指定使用某套着色器程序
        //将最终变换矩阵传入渲染管线
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        GLES30.glUniform1f(uPointSizeHandle, scale);  //将顶点尺寸传入渲染管线
        GLES30.glVertexAttribPointer( //将顶点位置数据送入渲染管线
                maPositionHandle,
                3,
                GLES30.GL_FLOAT,
                false,
                3*4,
                mVertexBuffer
        );
        //启用顶点位置数据数组
        GLES30.glEnableVertexAttribArray(maPositionHandle);
        GLES30.glDrawArrays(GLES30.GL_POINTS, 0, vCount); //绘制星星点
    }
}
