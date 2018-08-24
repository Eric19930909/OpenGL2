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
 * Open GL的透视除法，使用w分量创造三维的幻象
 * 保留z分量作为深度缓冲区，增加w作为第四个分量，可以把投影的影响与实际的z坐标解耦，以便在正交投影和透视投影之间切换
 * 视椎体(frustum)：由透视投影矩阵和投影除法创建的。
 * 通用的投影矩阵：(允许调整视野以及屏幕的宽高比)【Matrix.perspectiveM】
 * {
 * a/aspect, 0, 0, 0,
 * 0, a, 0, 0,
 * 0, 0, -(f+n)/(f-n), -2fn/(f-n),
 * 0, 0, -1, 0
 * }
 * a：相机焦距。焦距是由1/tan(视野/2)【tan表示正切函数】计算得到，这个视野必须小于180度。
 * 例如：90度视野，焦距为1/tan(90^。/2)，也就是1/1。
 * aspect：屏幕的宽高比，宽度/高度。
 * f：到远处平面的距离，必须是正值且大于到近处平面的距离。（far）
 * n：到近处平面的距离，必须是正值。比如，如果此值被设为-1，那近处平面就位于一个z值为-1处。（near）
 * <p>
 * 到第六章
 */
public class FourthRenderer implements GLSurfaceView.Renderer {

    private static final int BYTES_PER_FLOAT = 4;
    private FloatBuffer vertexData;

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;

    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private float[] tableVerticesWithTriangles = {
            // Order of coordinates: X, Y, Z, W, R, G, B
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

    /**
     * 模型矩阵
     */
    private final float[] modelMatrix = new float[16];

    /**
     * 最终传递到着色器的矩阵
     */
    private final float[] finalMatrix = new float[16];

    public FourthRenderer(Context context) {
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

        // 视口
        glViewport(0, 0, width, height);

        // 投影矩阵
        perspectiveM(projectionMatrix, 0, 45, (float) width / (float) height, 1f, 10f);

        // 设置为单位矩阵
        setIdentityM(modelMatrix, 0);
        // 移动物体
        translateM(modelMatrix, 0, 0, 0, -2.5f);
        // 旋转物体
        rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);

        // 获取最终的矩阵
        multiplyMM(finalMatrix, 0, projectionMatrix, 0, modelMatrix, 0);

    }

    @Override
    public void onDrawFrame(GL10 gl10) {

        glClear(GL_COLOR_BUFFER_BIT);

        // 传递矩阵给着色器
        glUniformMatrix4fv(uMatrixLocation, 1, false, finalMatrix, 0);

        // 桌子
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

        // 分隔线
        glDrawArrays(GL_LINES, 6, 2);

        // 木槌
        glDrawArrays(GL_POINTS, 8, 1);
        glDrawArrays(GL_POINTS, 9, 1);

    }

}
