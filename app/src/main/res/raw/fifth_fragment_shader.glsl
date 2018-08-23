// 定义所有浮点数据类型的默认精度
// lowp、mediump、highp对应低、中、高精度
precision mediump float;

// 该变量类型指的是一个二维纹理数据的数组，该变量接收实际的纹理数据
uniform sampler2D u_TextureUnit;
// 纹理坐标
varying vec2 v_TextureCoordinates;

void main(){
    // 该函数读入纹理中特定坐标处的颜色值，并返回结果
    gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);
}
