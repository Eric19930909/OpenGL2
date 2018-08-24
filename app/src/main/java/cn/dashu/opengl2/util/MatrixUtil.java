package cn.dashu.opengl2.util;

import android.opengl.Matrix;

/**
 * @author lushujie
 * @date 2018/8/14
 * 矩阵
 * {
 * 1, 0, 0, TranslationX,
 * 0, 1, 0, TranslationY,
 * 0, 0, 1, TranslationZ,
 * 0, 0, 0, 1
 * }
 */
public class MatrixUtil {

    /**
     * 获取一个单位矩阵
     */
    public static float[] getIdentityMatrix() {
        return new float[]{
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        };
    }

    /**
     * 产生以下的正交投影矩阵
     * {
     * 2/(right - left), 0, 0, -(right + left)/(right - left),
     * 0, 2/(top - bottom), 0, -(top + bottom)/(top - bottom),
     * 0, 0, -2/(far - near), -(far + near)/(far - near),
     * 0, 0, 0, 1
     * }
     *
     * @param m       目标矩阵，这个数组的长度至少有16个元素，这样才能存储正交投影矩阵
     * @param mOffset 结果矩阵起始的偏移值
     * @param left    x轴的最小范围
     * @param right   x轴的最大范围
     * @param bottom  y轴的最小范围
     * @param top     y轴的最大范围
     * @param near    z轴的最小范围
     * @param far     z轴的最大范围
     */
    public static void orthoM(float[] m, int mOffset, float left, float right, float bottom, float top, float near, float far) {
        Matrix.orthoM(m, mOffset, left, right, bottom, top, near, far);
    }

    /**
     *
     * */
    public static void perspectiveM(float[] m, float yFovInDegrees, float aspect, float near, float far) {
        Matrix.perspectiveM(m, 0, yFovInDegrees, aspect, near, far);
    }

    /**
     * 视图矩阵
     *
     * @param rm                      目标矩阵
     * @param rmOffset                偏移值
     * @param eyeX,eyeY,eyeZ          眼睛所在位置
     * @param centerX,centerY,centerZ 眼睛正在看的位置（场景中心）
     * @param upX,upY,upZ             头所指向位置
     */
    public static void setLookAtM(float[] rm, int rmOffset,
                                  float eyeX, float eyeY, float eyeZ,
                                  float centerX, float centerY, float centerZ,
                                  float upX, float upY, float upZ) {
        Matrix.setLookAtM(rm, rmOffset, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

}
