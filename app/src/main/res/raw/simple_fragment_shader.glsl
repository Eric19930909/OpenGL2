// 定义所有浮点数据类型的默认精度
// lowp、mediump、highp对应低、中、高精度
precision mediump float;

// r、g、b、a 红绿蓝+透明度
uniform vec4 u_Color;

void main(){
    gl_FragColor = u_Color;
}