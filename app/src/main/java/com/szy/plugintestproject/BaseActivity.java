package com.szy.plugintestproject;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import com.szy.plugininterfacesmodule.BasePluginActivity;
import com.szy.plugininterfacesmodule.ResouceHelper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;

/**
 * Created by songzhiyang on 2018/12/19.
 *
 * @author songzhiyang
 */
public class BaseActivity extends BasePluginActivity{

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

            ResouceHelper.sResources = newResource;

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

            //设置activity的resource
            Field contextThemeWrapperField = ContextThemeWrapper.class.getDeclaredField("mResources");
            contextThemeWrapperField.setAccessible(true);
            contextThemeWrapperField.set(this,newResource);

            //设置空theme
            Field themeField = getBaseContext().getClass().getDeclaredField("mTheme");
            themeField.setAccessible(true);
            themeField.set(getBaseContext(),null);

            Field contextThemeWrapperThemeField = ContextThemeWrapper.class.getDeclaredField("mTheme");
            contextThemeWrapperThemeField.setAccessible(true);
            contextThemeWrapperThemeField.set(this,null);

            Log.e("------","host resource -- " + getBaseContext().getResources().toString());
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

    protected void mergeDexInHostApp(String apkName) {
        File apkFile = new File(getFilesDir(),apkName);
        if (!apkFile.exists()) {
            return;
        }
        //将插件所需的dex合并到pathclassloader的pathList的elements中
        Class baseClassLoaderClazz = BaseDexClassLoader.class;
        try {
            Field dexPathListField = baseClassLoaderClazz.getDeclaredField("pathList");
            dexPathListField.setAccessible(true);
            Object dexPathList = dexPathListField.get(getClassLoader());
            Field elementsFiled = dexPathList.getClass().getDeclaredField("dexElements");
            elementsFiled.setAccessible(true);
            //host的elements 存放dex的Element数组
            Object elementsObj = elementsFiled.get(dexPathList);
            //public Element(File dir, boolean isDirectory, File zip, DexFile dexFile) {
            Class elementClazz = elementsObj.getClass().getComponentType();
            Class[] paramsClazz = new Class[]{File.class,boolean.class,File.class, DexFile.class};
            Constructor elementConstructor = elementClazz.getConstructor(paramsClazz);
            File pluginApkOutputFile = new File(getFilesDir(),"outputFile");
            DexFile dexFile = DexFile.loadDex(apkFile.getAbsolutePath(),pluginApkOutputFile.getAbsolutePath(),0);
            Object[] params = new Object[]{apkFile,false,apkFile,dexFile};
            Object newElementObj = elementConstructor.newInstance(params);
            Object[] newElements = new Object[]{newElementObj};

            //copy element to new array
            Object newArray = Array.newInstance(elementClazz,Array.getLength(elementsObj)+1);
            System.arraycopy(elementsObj,0,newArray,0,Array.getLength(elementsObj));
            System.arraycopy(newElements,0,newArray,Array.getLength(elementsObj),1);

            Log.e("------","length ---- " + Array.getLength(newArray));
            //set new elements
            elementsFiled.set(dexPathList,newArray);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
