package cn.dashu.opengl2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.socks.library.KLog;

import static android.opengl.GLES20.*;
import static android.opengl.GLUtils.*;

/**
 * @author lushujie
 * @date 2018/8/16
 */
public class TextureUtil {

    public static int loadTexture(Context context, int resId) {
        final int[] textureObjectIds = new int[1];
        glGenTextures(1, textureObjectIds, 0);

        if (textureObjectIds[0] == 0) {
            KLog.e("纹理加载失败！");
            return 0;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);

        if (bitmap == null) {
            glDeleteTextures(1, textureObjectIds, 0);
            KLog.e("图片加载失败！");
            return 0;
        }

        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);

        // 缩小情况下，使用三线性过滤
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        // 放大情况下，使用双线性过滤
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // 读入bitmap定义的位图数据，并复制到当前绑定的纹理对象
        texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

        // 释放位图资源
        bitmap.recycle();

        // 生成MIP贴图
        glGenerateMipmap(GL_TEXTURE_2D);

        // 解除纹理绑定
        glBindTexture(GL_TEXTURE_2D, 0);

        return textureObjectIds[0];
    }

}
