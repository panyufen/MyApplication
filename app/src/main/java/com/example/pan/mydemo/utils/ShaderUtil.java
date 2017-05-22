package com.example.pan.mydemo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Pan.
 */
//加载顶点shader与片元shader的工具类
public class ShaderUtil {
    //加载定制shader的方法
    public  static int loadShader(
            int shaderType,//shader的类型 GLES30.GL_VERTEX_SHADER(顶点) GLES30.GL_FRAGMENT_SHADER(片元)
            String source //shader的脚本字符串

    ){
        //创建一个新的Shader
        int shader = GLES30.glCreateShader(shaderType);
        //创建成功则加载shader
        if(shader!=0)
        {
            //加载shader的源代码
            GLES30.glShaderSource(shader,source);
            //编译shader
            GLES30.glCompileShader(shader);
            //存放编译成功shader数量的数组
            int [] compile=new int[1];
            //获取Shader的编译情况
            GLES30.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS,compile,0);
            if(compile[0]==0){
                //若编译失败则显示错误的日志并删除shader
                Log.e("ES20_ERROR","不能编译着色器"+shaderType+":");
                Log.e("ES20_ERROR",GLES30.glGetShaderInfoLog(shader));
                GLES30.glDeleteShader(shader);
                shader=0;
            }
        }
        return shader;
    }
    //创建着色器程序的方法
    public  static  int createProgram(String vertexSource,String fragmentSource) {
        //加载顶点着色器
        int vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0) {
            return 0;
        }

        //加载片元着色器
        int pixelShader=loadShader(GLES30.GL_FRAGMENT_SHADER,fragmentSource);
        if(pixelShader==0)
        {
            return 0;
        }
        //创建程序
        int program=GLES30.glCreateProgram();
        //若程序创建成功则向程序中加入顶点着色器与片元着色器
        if(program!=0){
            //向程序中加入顶点着色器
            GLES30.glAttachShader(program,vertexShader);
            //向程序中加入片元着色器
            GLES30.glAttachShader(program,pixelShader);
            checkGlError("glAttachShader");
            //连接程序
            GLES30.glLinkProgram(program);
            //存放连接成功program数量的数组
            int [] linkStatus=new int[1];
            //获取连接的情况
            GLES30.glGetProgramiv(program,GLES30.GL_LINK_STATUS,linkStatus,0);
            //若链接失败则报错并删除程序
            if (linkStatus[0] != GLES30.GL_TRUE)
            {
                Log.e("ES20_ERROR", "Could not link program: ");
                Log.e("ES20_ERROR", GLES30.glGetProgramInfoLog(program));
                GLES30.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
        }
    //检查每一步操作是否有错误的方法
    @SuppressLint("NewApi")
    public static void checkGlError(String op)
    {
        int error;
        while ((error = GLES30.glGetError()) != GLES30.GL_NO_ERROR)
        {
            Log.e("ES20_ERROR", op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }
    //从sh脚本中加载shader内容的方法
    public static String loadFromAssetsFile(String fname,Resources r)
    {
        String result=null;
        try
        {
            InputStream in=r.getAssets().open(fname);
            int ch=0;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while((ch=in.read())!=-1)
            {
                baos.write(ch);
            }
            byte[] buff=baos.toByteArray();
            baos.close();
            in.close();
            result=new String(buff,"UTF-8");
            result=result.replaceAll("\\r\\n","\n");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
    public  static   String loadFromRawFile(Context context ,int resourceId){

            StringBuilder body = new StringBuilder();

            try {
                InputStream inputStream =
                        context.getResources().openRawResource(resourceId);
                InputStreamReader inputStreamReader =
                        new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String nextLine;

                while ((nextLine = bufferedReader.readLine()) != null) {
                    body.append(nextLine);
                    body.append('\n');
                }
            } catch (IOException e) {
                throw new RuntimeException(
                        "不能加载资源: " + resourceId, e);
            } catch (Resources.NotFoundException nfe) {
                throw new RuntimeException("资源没有发现: " + resourceId, nfe);
            }

            return body.toString();
        }
    }

