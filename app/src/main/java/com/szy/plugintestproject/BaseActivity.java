package com.szy.plugintestproject;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

    protected void mergeResource(String apkName) {

        File apkFile = new File(getFilesDir(),apkName);
        if (!apkFile.exists()) {
            Toast.makeText(this,"插件apk不存在，资源合并失败",Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPathMethond = AssetManager.class.getDeclaredMethod("addAssetPath",String.class);
            //添加宿主的资源文件
            addAssetPathMethond.invoke(assetManager,getPackageResourcePath());
            //添加插件的资源文件
            addAssetPathMethond.invoke(assetManager,apkFile.getAbsolutePath());
            Resources newResource = new Resources(assetManager,getResources().getDisplayMetrics(),getResources().getConfiguration());

            //将newResource放置到ContextImpl中
            Field contextImplResourceField = getBaseContext().getClass().getDeclaredField("mResources");
            contextImplResourceField.setAccessible(true);
            contextImplResourceField.set(getBaseContext(),newResource);
            //将newResource放置到PackageInfo中
            Field contextImplLoadedApkField = getBaseContext().getClass().getDeclaredField("mPackageInfo");
            contextImplLoadedApkField.setAccessible(true);
            Object loadedApkObj = contextImplLoadedApkField.get(getBaseContext());
            Field loadedApkResourceField = loadedApkObj.getClass().getDeclaredField("mResources");
            loadedApkResourceField.setAccessible(true);
            loadedApkResourceField.set(loadedApkObj,newResource);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
