package cn.dashu.opengl2;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * @author lushujie
 * @date 2018/8/9
 */
public class MyGLSurfaceView extends GLSurfaceView {

    public MyGLSurfaceView(Context context) {
        super(context);
        init();
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        setEGLContextClientVersion(2);
        setRenderer(new FourthRenderer(getContext()));

    }

}
