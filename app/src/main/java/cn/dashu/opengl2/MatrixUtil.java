package cn.dashu.opengl2;

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

}
