package com.szy.plugintestproject;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;

import dalvik.system.DexClassLoader;

/**
 * Created by songzhiyang on 2018/12/19.
 *
 * @author songzhiyang
 */
public class BaseActivity extends AppCompatActivity{

    protected DexClassLoader loadPluginApk(String apkName) {
        File file = new File(BaseActivity.this.getFilesDir(),apkName);
        if (file.exists()) {
            File pluginAFile = BaseActivity.this.getFileStreamPath(apkName);
            String dexPath = pluginAFile.getPath();
            File dexReleaseFile = BaseActivity.this.getDir("dex", Context.MODE_PRIVATE);
            DexClassLoader dexClassLoader = new DexClassLoader(dexPath,dexReleaseFile.getAbsolutePath(),null,getClassLoader());
            Log.e("------" ,"插件加载成功 ---- " + file.length());
            return dexClassLoader;
        } else {
            Log.e("------","插件加载失败，没有找到插件");
            return null;
        }
    }

    protected String getDexPath(String apkName) {
        return this.getFilesDir() + "/" + apkName;
    }
}
