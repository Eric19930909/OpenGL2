package cn.dashu.opengl2.objects;

import static android.opengl.GLES20.*;

import java.util.ArrayList;
import java.util.List;

import cn.dashu.opengl2.util.Geometry;

/**
 * @author lushujie
 * @date 2018/8/23
 * 物体构建器
 */
public class ObjectBuilder {

    /**
     * 一个顶点需要3个浮点数
     */
    private static final int FLOATS_PER_VERTEX = 3;
    private final float[] vertextData;
    private int offset = 0;

    private final List<DrawCommand> drawList = new ArrayList<>();

    private ObjectBuilder(int sizeInVertices) {
        vertextData = new float[sizeInVertices * FLOATS_PER_VERTEX];
    }

    /**
     * 计算圆柱体顶部顶点数量
     * 一个圆柱体的顶部是一个用三角形扇构造的圆；它有一个顶点在圆心，围着圆的每个点都有一个顶点，并且围着圆的第一个顶点要重复两次才能使圆闭合。
     */
    private static int sizeOfCircleInVertices(int numPoints) {
        return 1 + (numPoints + 1);
    }

    /**
     * 计算圆柱体侧面顶点的数量
     * 一个圆柱体的侧面是一个卷起来的长方形，由一个三角形带构造，围着顶部圆的每个点都需要两个顶点，并且前两个顶点要重复两次才能使这个管闭合。
     */
    private static int sizeOfOpenCylinderInVertices(int numPoints) {
        return (numPoints + 1) * 2;
    }

    /**
     * 生成冰球
     */
    static GeneratedData createPuck(Geometry.Cylinder puck, int numPoints) {
        int size = sizeOfCircleInVertices(numPoints)
                + sizeOfOpenCylinderInVertices(numPoints);

        ObjectBuilder builder = new ObjectBuilder(size);

        Geometry.Circle puckTop = new Geometry.Circle(
                puck.center.translateY(puck.height / 2f),
                puck.radius);

        builder.appendCircle(puckTop, numPoints);
        builder.appendOpenCylinder(puck, numPoints);

        return builder.build();
    }

    /**
     * 生成木槌
     */
    static GeneratedData createMallet(Geometry.Point center, float radius, float height, int numPoints) {

        int size = sizeOfCircleInVertices(numPoints) * 2
                + sizeOfOpenCylinderInVertices(numPoints) * 2;

        ObjectBuilder builder = new ObjectBuilder(size);

        // First, generate the mallet base.
        float baseHeight = height * 0.25f;

        Geometry.Circle baseCircle = new Geometry.Circle(
                center.translateY(-baseHeight),
                radius);
        Geometry.Cylinder baseCylinder = new Geometry.Cylinder(
                baseCircle.center.translateY(-baseHeight / 2f),
                radius, baseHeight);

        builder.appendCircle(baseCircle, numPoints);
        builder.appendOpenCylinder(baseCylinder, numPoints);

        // Now generate the mallet handle.
        float handleHeight = height * 0.75f;
        float handleRadius = radius / 3f;

        Geometry.Circle handleCircle = new Geometry.Circle(
                center.translateY(height / 2f),
                handleRadius);
        Geometry.Cylinder handleCylinder = new Geometry.Cylinder(
                handleCircle.center.translateY(-handleHeight / 2f),
                handleRadius, handleHeight);

        builder.appendCircle(handleCircle, numPoints);
        builder.appendOpenCylinder(handleCylinder, numPoints);

        return builder.build();
    }

    /**
     * 用三角形扇构造圆
     */
    private void appendCircle(Geometry.Circle circle, int numPoints) {

        final int startVertex = offset / FLOATS_PER_VERTEX;
        final int numVertices = sizeOfCircleInVertices(numPoints);

        // Center point of fan
        vertextData[offset++] = circle.center.x;
        vertextData[offset++] = circle.center.y;
        vertextData[offset++] = circle.center.z;

        // Fan around center point. <= is used because we want to generate
        // the point at the starting angle twice to complete the fan.
        for (int i = 0; i <= numPoints; i++) {
            float angleInRadians = ((float) i / (float) numPoints) * ((float) Math.PI * 2f);
            vertextData[offset++] = circle.center.x + circle.radius * (float) Math.cos((double) angleInRadians);
            vertextData[offset++] = circle.center.y;
            vertextData[offset++] = circle.center.z + circle.radius * (float) Math.sin((double) angleInRadians);
        }

        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_FAN, startVertex, numVertices);
            }
        });

    }

    /**
     * 用三角形带构造圆柱体侧面
     */
    private void appendOpenCylinder(Geometry.Cylinder cylinder, int numPoints) {

        final int startVertex = offset / FLOATS_PER_VERTEX;
        final int numVertices = sizeOfOpenCylinderInVertices(numPoints);
        final float yStart = cylinder.center.y - (cylinder.height / 2f);
        final float yEnd = cylinder.center.y + (cylinder.height / 2f);

        // 生成三角形带
        for (int i = 0; i <= numPoints; i++) {

            float angleInRadians = ((float) i / (float) numPoints) * ((float) Math.PI * 2f);
            float xPosition = cylinder.center.x + cylinder.radius * (float) Math.cos((double) angleInRadians);
            float zPosition = cylinder.center.z + cylinder.radius * (float) Math.sin((double) angleInRadians);

            vertextData[offset++] = xPosition;
            vertextData[offset++] = yStart;
            vertextData[offset++] = zPosition;

            vertextData[offset++] = xPosition;
            vertextData[offset++] = yEnd;
            vertextData[offset++] = zPosition;

        }

        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices);
            }
        });

    }

    private GeneratedData build() {
        return new GeneratedData(vertextData, drawList);
    }

    static class GeneratedData {

        final float[] vertexData;
        final List<DrawCommand> drawList;

        GeneratedData(float[] vertexData, List<DrawCommand> drawList) {
            this.vertexData = vertexData;
            this.drawList = drawList;
        }

    }

    static interface DrawCommand {
        /**
         * 绘制对象
         */
        void draw();
    }

}
