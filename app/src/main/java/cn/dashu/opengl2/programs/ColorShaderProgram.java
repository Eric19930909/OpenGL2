package cn.dashu.opengl2.programs;

import android.content.Context;

import cn.dashu.opengl2.R;

import static android.opengl.GLES20.*;

/**
 * @author lushujie
 * @date 2018/8/22
 */
public class ColorShaderProgram extends ShaderProgram {

    /**
     * Uniform locations
     */
    private final int uMatrixLocation;

    /**
     * Attribute locations
     */
    private final int aPositionLocation;
    private final int aColorLocation;

    public ColorShaderProgram(Context context) {
        super(context, R.raw.fourth_vertex_shader, R.raw.fourth_fragment_shader);
        // Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);

        // Retrieve attribute locations for the shader program.
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aColorLocation = glGetAttribLocation(program, A_COLOR);
    }

    public void setUniforms(float[] matrix){
        // Pass the matrix into the shader program.
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getColorAttributeLocation() {
        return aColorLocation;
    }

}
