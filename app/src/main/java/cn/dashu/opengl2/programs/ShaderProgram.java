package cn.dashu.opengl2.programs;

import android.content.Context;

import cn.dashu.opengl2.FileUtil;
import cn.dashu.opengl2.ShaderUtil;

import static android.opengl.GLES20.*;

/**
 * @author lushujie
 * @date 2018/8/22
 */
public class ShaderProgram {

    /**
     * Uniform constants
     */
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";

    /**
     * Attribute constants
     */
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    /**
     * Shader program
     */
    protected final int program;

    protected ShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId) {
        // Compile the shader and link the program.
        program = ShaderUtil.buildProgram(
                FileUtil.readTextFileFromResource(context, vertexShaderResourceId),
                FileUtil.readTextFileFromResource(context, fragmentShaderResourceId)
        );
    }

    public void useProgram() {
        // Set the current OpenGL shader program to this program;
        glUseProgram(program);
    }

}
