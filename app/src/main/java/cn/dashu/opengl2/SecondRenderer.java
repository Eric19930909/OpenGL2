package cn.dashu.opengl2;

import android.content.Context;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.*;
import static android.opengl.GLUtils.*;
import static android.opengl.Matrix.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author lushujie
 * @date 2018/8/9
 * 1.三角形的卷曲顺序 -> 逆时针排列顶点，可优化性能。可以指出一个三角形属于任何给定物体的前面/后面。
 * 2.着色器：A.顶点着色器（vertex shader）：顶点位置。
 * B.片段着色器（fragment shader）:为组成点、直线或者三角形的每个片段生成最终的颜色。
 */
public class SecondRenderer implements GLSurfaceView.Renderer {

    private static final int BYTES_PER_FLOAT = 4;
    private FloatBuffer vertexData;

    private static final int POSITION_COMPONENT_COUNT = 2;

    float[] tableVertices = {
            0f, 0f,
            0f, 14f,
            9f, 14f,
            9f, 0f
    };

    private float[] tableVerticesWithTriangles = {
            // Triangle 1
            -0.5f, -0.5f,
            0.5f, 0.5f,
            -0.5f, 0.5f,
            // Triangle 2
            -0.5f, -0.5f,
            0.5f, -0.5f,
            0.5f, 0.5f,
            // Line 1
            -0.5f, 0f,
            0.5f, 0f,
            // Mallets
            0f, -0.25f,
            0f, 0.25f
    };

    private Context mContext;

    private String vertexShaderSource;
    private String fragmentShaderSource;

    private int vertexShader;
    private int fragmentShader;

    private int program;

    private static final String U_COLOR = "u_Color";
    private int uColorLocation;

    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;

    public SecondRenderer(Context context) {
        mContext = context;
        floatToBuffer();
    }

    private void floatToBuffer() {

        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(tableVerticesWithTriangles);
        vertexData.position(0);

    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        glClearColor(0f, 0f, 0f, 0f);

        // 打开深度检测
//        glEnable(GL_DEPTH_TEST);

        vertexShaderSource = FileUtil.readTextFileFromResource(mContext, R.raw.simple_vertex_shader);
        fragmentShaderSource = FileUtil.readTextFileFromResource(mContext, R.raw.simple_fragment_shader);

        vertexShader = ShaderUtil.compileVertexShader(vertexShaderSource);
        fragmentShader = ShaderUtil.compileFragmentShader(fragmentShaderSource);

        program = ShaderUtil.linkProgram(vertexShader, fragmentShader);

        if (ShaderUtil.validateProgram(program)) {
            glUseProgram(program);

            // 获取uniform位置
            uColorLocation = glGetUniformLocation(program, U_COLOR);
            // 获取属性位置
            aPositionLocation = glGetAttribLocation(program, A_POSITION);

        }

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {

        glClear(GL_COLOR_BUFFER_BIT);

        // 开启
        glEnableVertexAttribArray(aPositionLocation);
        // 关联属性与顶点数据的数组
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, vertexData);

        // 桌子
        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
        glDrawArrays(GL_TRIANGLES, 0, 6);

        // 分隔线
        glUniform4f(uColorLocation, 1f, 0f, 0f, 1f);
        glDrawArrays(GL_LINES, 6, 2);

        // 木槌
        glUniform4f(uColorLocation, 0f, 0f, 1f, 1f);
        glDrawArrays(GL_POINTS, 8, 1);

        glUniform4f(uColorLocation, 1f, 0f, 0f, 1f);
        glDrawArrays(GL_POINTS, 9, 1);

    }

}
