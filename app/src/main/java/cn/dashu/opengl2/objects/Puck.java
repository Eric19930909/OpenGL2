package cn.dashu.opengl2.objects;

import java.util.List;

import cn.dashu.opengl2.data.VertexArray;
import cn.dashu.opengl2.programs.SixthColorShaderProgram;
import cn.dashu.opengl2.util.Geometry;

/**
 * @author lushujie
 * @date 2018/8/24
 */
public class Puck {

    private static final int POSITION_COMPONENT_COUNT = 3;

    public final float radius, height;

    private final VertexArray mVertexArray;
    private final List<ObjectBuilder.DrawCommand> mDrawList;

    public Puck(float radius, float height, int numPointsAroundPuck) {

        ObjectBuilder.GeneratedData data = ObjectBuilder.createPuck(
                new Geometry.Cylinder(new Geometry.Point(0f, 0f, 0f), radius, height),
                numPointsAroundPuck);

        this.radius = radius;
        this.height = height;

        mVertexArray = new VertexArray(data.vertexData);
        mDrawList = data.drawList;

    }

    public void bindData(SixthColorShaderProgram program) {

        mVertexArray.setVertexAttribPointer(
                0,
                program.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                0
        );

    }

    public void draw() {
        for (ObjectBuilder.DrawCommand drawCommand : mDrawList) {
            drawCommand.draw();
        }
    }

}
