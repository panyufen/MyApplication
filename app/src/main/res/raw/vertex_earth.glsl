#version 300 es
uniform mat4 uMVPMatrix;//总的变换矩阵
uniform mat4 uMMatrix;  //变换矩阵
uniform  vec3 uCamera;//摄像机的位置
uniform vec3 uLightLocationSun;//太阳光源的位置
in vec3 aPosition;//顶点位置
in vec2 aTexCoor;//顶点纹理坐标
in vec3 aNormal;//法向量
out vec2 vTextureCood; //用于传递给片元着色器的变量
out vec4 vAmbient;//环境光
out vec4 vDiffuse;//发射光
out vec4 vSpecular;//镜面光

//定位光光照计算的方法
void pointLight( //定位光光照计算的方法
in vec3 normal,  //法向量
inout vec4 ambient,//环境光的最终强度
inout vec4 diffuse,//散射光最终强度
inout vec4 specular,//镜面光的最终强度
in  vec3 lightLocation,//光源位置
in vec4 lightAmbient,//环境光强度
in vec4 lightDiffuse,//散射光强度
in vec4 lightSpecular//镜面光强度

){
 ambient=lightAmbient;//直接得出环境光的最终强度
  vec3 normalTarget=aPosition+normal;//计算变化后的法向量
  vec3 newNormal=(uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*vec4(aPosition,1)).xyz;
  newNormal=normalize(newNormal);//对法向量规格化

  //计算从表面点到摄像机的向量
  vec3 eye=normalize(uCamera-(uMMatrix*vec4(aPosition,1)).xyz);
  //计算从表面点到光源位置的向量vp
  vec3 vp=normalize(lightLocation-(uMMatrix*vec4(aPosition,1)).xyz);
  vp=normalize(vp);//格式化

   vec3 halfVector=normalize(vp+eye);//求视线与光线的半向量
   float shiniess=50.0;//粗糙度，越小越光滑
   float nDotViewPosition=max(0.0,dot(newNormal,vp));//求法向量与vp的点积与0的最大值
   diffuse=lightDiffuse*nDotViewPosition;//计算散射光的最终的强度

  float nDotViewHalfVector=dot(newNormal,halfVector);//法线和半向量 的点积
   float powerFacetor=max(0.0,pow(nDotViewHalfVector,shiniess));//镜面反射光强度因子

   specular=lightSpecular*powerFacetor;//计算镜面光的最终强度
}

void main() {
 //地球着色器的main方法
gl_Position=uMVPMatrix*vec4(aPosition,1);//根据总的变换矩阵计算此次绘制顶点位置

vec4 ambientTemp=vec4(0.0,0.0,0.0,0.0);
vec4 diffuseTemp=vec4(0.0,0.0,0.0,0.0);
vec4 specularTemp=vec4(0.0,0.0,0.0,0.0);

//pointLight(normalize(aNormal),ambientTemp,diffuseTemp,specularTemp,uLightLocation,vec4(005,0.05,0.05,1.0)),vec4(1.0,1.0,1.0,1.0),vec4(0.3,0.3,0.3,1.0);
  pointLight(normalize(aNormal),ambientTemp,diffuseTemp,specularTemp,uLightLocationSun,
   vec4(0.05,0.05,0.05,1.0),vec4(1.0,1.0,1.0,1.0),vec4(0.3,0.3,0.3,1.0));
 vAmbient=ambientTemp;
 vDiffuse=diffuseTemp;
 vSpecular=specularTemp;

 //将顶点的纹理坐标传给片元着色器
 vTextureCood=aTexCoor;
}