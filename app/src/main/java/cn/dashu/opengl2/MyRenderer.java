package cn.dashu.opengl2;

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
 *          B.片段着色器（fragment shader）:为组成点、直线或者三角形的每个片段生成最终的颜色。
 */
public class MyRenderer implements GLSurfaceView.Renderer {

    private static final int BYTES_PER_FLOAT = 4;
    private FloatBuffer vertexData;

    float[] tableVertices = {
            0f, 0f,
            0f, 14f,
            9f, 14f,
            9f, 0f
    };

    float[] tableVerticesWithTriangles = {
            // Triangle 1
            0f, 0f,
            9f, 14f,
            0f, 14f,
            // Triangle 2
            0f, 0f,
            9f, 0f,
            9f, 14f,
            // Line 1
            0f, 7f,
            9f, 7f,
            // Mallets
            4.5f, 2f,
            4.5f, 12f
    };

    public MyRenderer() {

        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(tableVerticesWithTriangles);

    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        glClearColor(1f, 1f, 1f, 0f);

        // 打开深度检测
        glEnable(GL_DEPTH_TEST);

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {

        glClear(GL_COLOR_BUFFER_BIT);

    }

}
