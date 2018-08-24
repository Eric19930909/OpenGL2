package cn.dashu.opengl2;

import android.content.Context;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cn.dashu.opengl2.objects.Puck;
import cn.dashu.opengl2.objects.SixthMallet;
import cn.dashu.opengl2.objects.Table;
import cn.dashu.opengl2.programs.SixthColorShaderProgram;
import cn.dashu.opengl2.programs.TextureShaderProgram;
import cn.dashu.opengl2.util.TextureUtil;

import static android.opengl.GLES20.*;
import static android.opengl.Matrix.*;

/**
 * @author lushujie
 * @date 2018/8/24
 */
public class SixthRenderer implements GLSurfaceView.Renderer {

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
     * 视图矩阵及其矩阵乘积结果
     */
    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];

    /**
     * 最终传递到着色器的矩阵
     */
    private float[] finalMatrix = new float[16];

    private Puck mPuck;

    private Table mTable;
    private SixthMallet mMallet;

    private TextureShaderProgram mTextureProgram;
    private SixthColorShaderProgram mColorProgram;

    private int texture;

    public SixthRenderer(Context context) {
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        glClearColor(0f, 0f, 0f, 0f);

        mTable = new Table();
        mMallet = new SixthMallet(0.08f, 0.15f, 32);
        mPuck = new Puck(0.06f, 0.02f, 32);

        mTextureProgram = new TextureShaderProgram(mContext);
        mColorProgram = new SixthColorShaderProgram(mContext);

        texture = TextureUtil.loadTexture(mContext, R.drawable.air_hockey_surface);

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {

        // 视口
        glViewport(0, 0, width, height);

        // 投影矩阵
        perspectiveM(projectionMatrix, 0, 45, (float) width / (float) height, 1f, 10f);

        setLookAtM(viewMatrix, 0, 0f, 1.2f, 2.2f, 0f, 0f, 0f, 0f, 1f, 0f);

        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

    }

    @Override
    public void onDrawFrame(GL10 gl10) {

        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT);

        // Draw the table.
        positionTableInScene();
        mTextureProgram.useProgram();
        mTextureProgram.setUniforms(modelViewProjectionMatrix, texture);
        mTable.bindData(mTextureProgram);
        mTable.draw();

        // Draw the mallets.
        positionObjectInScene(0f, mMallet.height / 2f, -0.4f);
        mColorProgram.useProgram();
        mColorProgram.setUniforms(modelViewProjectionMatrix, 1f, 0f, 0f);
        mMallet.bindData(mColorProgram);
        mMallet.draw();

        positionObjectInScene(0f, mMallet.height / 2f, 0.4f);
        mColorProgram.setUniforms(modelViewProjectionMatrix, 0f, 0f, 1f);
        mMallet.draw();

        // Draw the puck.
        positionObjectInScene(0f, mPuck.height / 2f, 0f);
        mColorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f);
        mPuck.bindData(mColorProgram);
        mPuck.draw();

    }

    private void positionTableInScene() {

        setIdentityM(modelMatrix, 0);
        rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);

    }

    private void positionObjectInScene(float x, float y, float z) {

        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, x, y, z);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);

    }

}
