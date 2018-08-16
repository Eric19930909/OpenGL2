package cn.dashu.opengl2;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;

import com.socks.library.KLog;

public class MainActivity extends Activity {

    private MyGLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        glSurfaceView = findViewById(R.id.gl_surface_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (glSurfaceView != null) {
//            glSurfaceView.onResume();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (glSurfaceView != null) {
//            glSurfaceView.onPause();
//        }
    }

}
