package cn.dashu.opengl2.util;

import com.socks.library.KLog;

import static android.opengl.GLES20.*;

/**
 * @author lushujie
 * @date 2018/8/10
 */
public class ShaderUtil {

    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    public static int compileShader(int type, String shaderCode) {
        int shaderObjectId = glCreateShader(type);
        if (shaderObjectId != 0) {
            // 上传源代码
            glShaderSource(shaderObjectId, shaderCode);
            // 编译
            glCompileShader(shaderObjectId);
            // 检查编译状态
            int[] compileStatus = new int[1];
            glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);

            if (compileStatus[0] == 0) {
                glDeleteShader(shaderObjectId);
                KLog.e("Shader Log ：" + glGetShaderInfoLog(shaderObjectId));
                shaderObjectId = 0;
            }


        } else {
            KLog.e("着色器对象创建失败" + glGetError());
        }

        return shaderObjectId;
    }

    /**
     * 链接程序
     *
     * @param vertexShaderId   顶点着色器
     * @param fragmentShaderId 片段着色器
     */
    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        int programId = glCreateProgram();

        if (programId != 0) {
            // 将着色器附加到程序上
            glAttachShader(programId, vertexShaderId);
            glAttachShader(programId, fragmentShaderId);

            // 链接
            glLinkProgram(programId);
            int[] linkStatus = new int[1];
            glGetProgramiv(programId, GL_LINK_STATUS, linkStatus, 0);
            KLog.e("程序日志 : " + glGetProgramInfoLog(programId));

            if (linkStatus[0] == 0) {
                glDeleteProgram(programId);
                KLog.e("链接失败！");
                return 0;
            }

        }

        return programId;
    }

    /**
     * 检查程序有效性
     *
     * @param programId 程序ID
     */
    public static boolean validateProgram(int programId) {
        glValidateProgram(programId);

        int[] validateStatus = new int[1];
        glGetProgramiv(programId, GL_VALIDATE_STATUS, validateStatus, 0);
        KLog.e("Result: " + validateStatus[0] + " \nLog :" + glGetProgramInfoLog(programId));

        return validateStatus[0] != 0;
    }

    public static int buildProgram(String vertexShaderSource, String fragmentShaderSource) {

        int program;

        // Compile the shader.
        int vertexShader = compileVertexShader(vertexShaderSource);
        int fragmentShader = compileFragmentShader(fragmentShaderSource);

        // Link them into a shader program.
        program = linkProgram(vertexShader, fragmentShader);

        return program;
    }

}
