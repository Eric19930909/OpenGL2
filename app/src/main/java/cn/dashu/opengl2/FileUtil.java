package cn.dashu.opengl2;

import android.content.Context;

import com.socks.library.KLog;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author lushujie
 * @date 2018/8/10
 */
public class FileUtil {

    public static String readTextFileFromResource(Context context, int resourceId) {

        StringBuilder body = new StringBuilder();

        try {


            InputStream is = context.getResources().openRawResource(resourceId);
            InputStreamReader localInputStreamReader = new InputStreamReader(is);
            BufferedReader localBufferedReader = new BufferedReader(localInputStreamReader);

            String nextLine;
            while ((nextLine = localBufferedReader.readLine()) != null) {
                body.append(nextLine);
                body.append("\n");
            }

        } catch (Exception e) {
            KLog.e(e.getMessage());
        }

        return body.toString();
    }

}
