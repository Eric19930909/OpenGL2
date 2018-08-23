package cn.dashu.opengl2.objects;

import cn.dashu.opengl2.data.Constants;
import cn.dashu.opengl2.data.VertexArray;
import cn.dashu.opengl2.programs.ColorShaderProgram;

import static android.opengl.GLES20.*;

/**
 * @author lushujie
 * @date 2018/8/16
 */
public class Mallet {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT)
            * Constants.BYTES_PER_FLOAT;

    private static final float[] VERTEX_DATA = {
            // Order of coordinates: X, Y, R, G, B
            0f, -0.4f, 0f, 0f, 1f,
            0f, 0.4f, 1f, 0f, 0f
    };

    private final VertexArray mVertexArray;

    public Mallet() {
        mVertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(ColorShaderProgram program) {

        mVertexArray.setVertexAttribPointer(
                0,
                program.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE
        );

        mVertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                program.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT,
                STRIDE
        );

    }

    public void draw() {

        glDrawArrays(GL_POINTS, 0, VERTEX_DATA.length / (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT));

    }

}
