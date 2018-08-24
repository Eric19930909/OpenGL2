package cn.dashu.opengl2;

import android.content.Context;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.*;
import static android.opengl.Matrix.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cn.dashu.opengl2.util.FileUtil;
import cn.dashu.opengl2.util.ShaderUtil;

/**
 * @author lushujie
 * @date 2018/8/9
 * <p>
 * 到第五章
 */
public class ThirdRenderer implements GLSurfaceView.Renderer {

    private static final int BYTES_PER_FLOAT = 4;
    private FloatBuffer vertexData;

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;

    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private float[] tableVerticesWithTriangles = {
            // Order of coordinates: X, Y, R, G, B
            // Triangle Fan
            0f, 0f, 1f, 1f, 1f,
            -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
            0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
            0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
            -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
            -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
            // Line 1
            -0.5f, 0f, 1f, 0f, 0f,
            0.5f, 0f, 1f, 0f, 0f,
            // Mallets
            0f, -0.4f, 0f, 0f, 1f,
            0f, 0.4f, 1f, 0f, 0f
    };

    private Context mContext;

    private String vertexShaderSource;
    private String fragmentShaderSource;

    private int vertexShader;
    private int fragmentShader;

    private int program;

    private static final String A_COLOR = "a_Color";
    private int aColorLocation;

    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;

    private static final String U_MATRIX = "u_Matrix";
    private int uMatrixLocation;

    /**
     * 投影矩阵
     */
    private final float[] projectionMatrix = new float[16];

    public ThirdRenderer(Context context) {
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

        vertexShaderSource = FileUtil.readTextFileFromResource(mContext, R.raw.third_vertex_shader);
        fragmentShaderSource = FileUtil.readTextFileFromResource(mContext, R.raw.third_fragment_shader);

        vertexShader = ShaderUtil.compileVertexShader(vertexShaderSource);
        fragmentShader = ShaderUtil.compileFragmentShader(fragmentShaderSource);

        program = ShaderUtil.linkProgram(vertexShader, fragmentShader);

        if (ShaderUtil.validateProgram(program)) {
            glUseProgram(program);

            // 获取属性位置
            aColorLocation = glGetAttribLocation(program, A_COLOR);
            aPositionLocation = glGetAttribLocation(program, A_POSITION);
            uMatrixLocation = glGetUniformLocation(program, U_MATRIX);

            // 开启
            glEnableVertexAttribArray(aPositionLocation);
            // 关联属性与顶点数据的数组
            glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);

            vertexData.position(POSITION_COMPONENT_COUNT);
            // 开启
            glEnableVertexAttribArray(aColorLocation);
            // 关联颜色
            glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);

        }

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {

        glViewport(0, 0, width, height);

        final float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;

        if (width > height) {
            // Landscape
            orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        } else {
            // Portrait or square
            orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }

    }

    @Override
    public void onDrawFrame(GL10 gl10) {

        glClear(GL_COLOR_BUFFER_BIT);

        // 传递矩阵给着色器
        glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);

        // 桌子
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

        // 分隔线
        glDrawArrays(GL_LINES, 6, 2);

        // 木槌
        glDrawArrays(GL_POINTS, 8, 1);
        glDrawArrays(GL_POINTS, 9, 1);

    }

}
