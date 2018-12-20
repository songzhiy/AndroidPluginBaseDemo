package com.szy.plugintestproject;

import android.app.Application;
import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by songzhiyang on 2018/12/19.
 *
 * @author songzhiyang
 */
public class HostApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        //将assets目录下的包替换到data/data/files目录下 assets目录下的文件只能open出一个inputstream
        copyPluginApk("plugina-release-unsigned.apk","plugina.apk");
        copyPluginApk("plugina-release-unsignedB.apk","pluginb.apk");
    }

    private void copyPluginApk(String srcPluginApkName,String destPluginApkName) {
        InputStream pluginInputStream = null;
        FileOutputStream copyOutputStream = null;
        try {
            pluginInputStream = getAssets().open(srcPluginApkName);
            File pluginAFile = new File(getFilesDir(), destPluginApkName);
            copyOutputStream = new FileOutputStream(pluginAFile);

            byte[] readArray = new byte[1024];
            int totalRead = 0;
            while ((totalRead = pluginInputStream.read(readArray)) != -1) {
                copyOutputStream.write(readArray, 0, totalRead);
                copyOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pluginInputStream != null) {
                try {
                    pluginInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (copyOutputStream != null) {
                try {
                    copyOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
