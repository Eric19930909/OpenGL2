uniform mat4 u_Matrix;

 // x、y、z、w 三维+特殊坐标
 attribute vec4 a_Position;
 attribute vec4 a_Color;

 varying vec4 v_Color;

 void main(){
     v_Color = a_Color;
     gl_Position = u_Matrix * a_Position;
     // 设置绘制点的大小
     gl_PointSize = 10.0;
 }