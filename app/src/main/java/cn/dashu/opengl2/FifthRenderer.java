package cn.dashu.opengl2;

import android.content.Context;
import android.opengl.GLSurfaceView;

import static android.opengl.GLES20.*;
import static android.opengl.Matrix.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cn.dashu.opengl2.objects.Mallet;
import cn.dashu.opengl2.objects.Table;
import cn.dashu.opengl2.programs.ColorShaderProgram;
import cn.dashu.opengl2.programs.TextureShaderProgram;
import cn.dashu.opengl2.util.TextureUtil;

/**
 * @author lushujie
 * @date 2018/8/9
 * 纹理不必是正方形，但是每个维度都应该是2的幂
 * 当纹理大小被扩大或者缩小时，还需要使用纹理过滤。
 * 2个基本的过滤模式：
 * 最近邻过滤（nearest-neighbor filtering）和双线性插值（bilinear interpolation）
 * <p>
 * 到第七章
 */
public class FifthRenderer implements GLSurfaceView.Renderer {

//    private static final int BYTES_PER_FLOAT = 4;
//    private FloatBuffer vertexData;

//    private static final int POSITION_COMPONENT_COUNT = 2;
//    private static final int COLOR_COMPONENT_COUNT = 3;

//    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
//
//    private float[] tableVerticesWithTriangles = {
//            // Order of coordinates: X, Y, Z, W, R, G, B
//            // Triangle Fan
//            0f, 0f, 1f, 1f, 1f,
//            -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
//            0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
//            0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
//            -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
//            -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
//            // Line 1
//            -0.5f, 0f, 1f, 0f, 0f,
//            0.5f, 0f, 1f, 0f, 0f,
//            // Mallets
//            0f, -0.4f, 0f, 0f, 1f,
//            0f, 0.4f, 1f, 0f, 0f
//    };

    private Context mContext;

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
    private float[] finalMatrix = new float[16];

    private Table mTable;
    private Mallet mMallet;

    private TextureShaderProgram mTextureProgram;
    private ColorShaderProgram mColorProgram;

    private int texture;

    public FifthRenderer(Context context) {
        mContext = context;
//        floatToBuffer();
    }

//    private void floatToBuffer() {
//
//        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
//                .order(ByteOrder.nativeOrder())
//                .asFloatBuffer();
//        vertexData.put(tableVerticesWithTriangles);
//        vertexData.position(0);
//
//    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        glClearColor(0f, 0f, 0f, 0f);

        mTable = new Table();
        mMallet = new Mallet();

        mTextureProgram = new TextureShaderProgram(mContext);
        mColorProgram = new ColorShaderProgram(mContext);

        texture = TextureUtil.loadTexture(mContext, R.drawable.air_hockey_surface);

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

        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT);

        // Draw the table.
        mTextureProgram.useProgram();
        mTextureProgram.setUniforms(finalMatrix, texture);
        mTable.bindData(mTextureProgram);
        mTable.draw();

        // Draw the mallets.
        mColorProgram.useProgram();
        mColorProgram.setUniforms(finalMatrix);
        mMallet.bindData(mColorProgram);
        mMallet.draw();

    }

}
