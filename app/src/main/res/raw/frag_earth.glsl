#version 300 es
precision mediump float;//给出浮点数的精度
in vec2 vTextureCood;//接受从顶点着色器传过来的参数
in vec4 vAmbeient;//接受从顶点着色器过来环境光最终强度
in vec4 vDiffuse;//接受从顶点着色器过来的环境光最终的强度
in vec4 vSpecular;//接受从顶点着色器过来镜面反射光最终的强度

out vec4 fragColor;//传递到渲染管线的片元的颜色
uniform sampler2D sTextureDay;//白天纹理的内容数据
uniform sampler2D sTextureNight;//黑夜纹理的内容数据




void main() {

    vec4 finalColorDay;//从白天纹理的采样值
    vec4 finalColorNight;//从夜晚纹理只不过的采样的颜色值

    finalColorDay=texture(sTextureDay,vTextureCood);//采样出白天纹理的颜色值
    finalColorDay=finalColorDay*vAmbeient+finalColorDay*vSpecular+finalColorDay*vDiffuse;
    finalColorNight=texture(sTextureNight,vTextureCood);//采样出夜晚纹理的颜色值
    finalColorNight=finalColorNight*vec4(0.5,0.5,0.5,1);//计算出的该片元夜晚的颜色值

    if(vDiffuse.x>0.21)
    {

    //当散射光分量大于0.21时
    fragColor=finalColorDay;//采用白天的纹理
    }
    else if(vDiffuse.x<0.05)
    {
    fragColor=finalColorNight;//采样夜间的纹理
    }
    else{

    float t=(vDiffuse.x-0.05)/0.6;//计算白天的纹理的过渡阶段的百分比
    fragColor=t*finalColorDay+(1.0-t)*finalColorNight;//计算白天黑夜的过渡阶段的颜色值
    }
}
