// x、y、z、w 三维+特殊坐标
attribute vec4 a_Position;

void main(){
    gl_Position = a_Position;
    // 设置绘制点的大小
    gl_PointSize = 10.0;
}