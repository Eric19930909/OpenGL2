package cn.dashu.opengl2.objects;

import cn.dashu.opengl2.data.Constants;
import cn.dashu.opengl2.data.VertexArray;
import cn.dashu.opengl2.programs.TextureShaderProgram;

import static android.opengl.GLES20.*;

/**
 * @author lushujie
 * @date 2018/8/16
 */
public class Table {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT
            + TEXTURE_COORDINATES_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;

    private static final float[] VERTEX_DATA = {
            // Order of coordinates : X, Y, S, T

            // Triangle Fan
            0f, 0f, 0.5f, 0.5f,
            -0.5f, -0.8f, 0f, 0.9f,
            0.5f, -0.8f, 1f, 0.9f,
            0.5f, 0.8f, 1f, 0.1f,
            -0.5f, 0.8f, 0f, 0.1f,
            -0.5f, -0.8f, 0f, 0.9f
    };

    private final VertexArray mVertexArray;

    public Table() {
        mVertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(TextureShaderProgram program) {

        mVertexArray.setVertexAttribPointer(
                0,
                program.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE
        );

        mVertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                program.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE
        );

    }

    public void draw() {

        glDrawArrays(GL_TRIANGLE_FAN, 0, VERTEX_DATA.length / (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT));

    }

}
