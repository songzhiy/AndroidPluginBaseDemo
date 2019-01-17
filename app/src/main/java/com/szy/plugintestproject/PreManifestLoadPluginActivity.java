package com.szy.plugintestproject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.szy.plugininterfacesmodule.IPluginConfig;
import com.szy.plugininterfacesmodule.IPluginSkinConfig;
import com.szy.plugintestproject.dex.DexPathClassLoader;
import com.szy.plugintestproject.hook.ActivityStartHooker;
import com.szy.plugintestproject.hook.activity.ActivityLoadedApkCacheHook;
import com.szy.plugintestproject.hook.activity.ActivityThreadHandlerHooker;

import java.io.File;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;

/**
 * Created by songzhiyang on 2019/1/9.
 *
 * 实现启动插件activity，并可以点击button，弹出toast
 *
 * 1、占坑的manifest提供  已提供standard
 * 2、资源的合并操作  已完成
 * 3、hook activity跳转所需的地方 已完成
 * 4、hook 插件dex 方式有下面三种
 *
 * 1、实现使用Hook LoadedApk命中缓存的方式进行classloader的处理
 * 2、实现将插件dex合并到宿主dex中的方式进行   已完成
 * 3、实现继承pathclassloader的方式，持有dexclassloader 进行findclass操作  已完成
 *
 * @author songzhiyang
 */
public class PreManifestLoadPluginActivity extends BaseActivity{

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        mergeResource("plugina.apk");
        ActivityStartHooker.hookActivityStarter(newBase);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pre_manifest_load_plugin_activity);

        findViewById(R.id.btn_load_activity_use_cache).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                //准备开始实现hook LoadedApk的方式 替换classloader的办法
                ActivityLoadedApkCacheHook.hookActivityStarterLoadedApkCache(getBaseContext(),"plugina.apk","com.szy.plugina");

                ActivityLoadedApkCacheHook.hookPackageManagerGetPackageInfo(getBaseContext());

                ActivityThreadHandlerHooker.hookActivityThreadHooker(getBaseContext());
                Intent intent = new Intent();
                ComponentName componentName = null;
                componentName = new ComponentName("com.szy.plugina","com.szy.plugina.PluginAActivityA");
                intent.setComponent(componentName);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_load_activity_use_merge_class_loader).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    IPluginConfig iPluginConfig = (IPluginConfig) getClassLoader().loadClass("com.szy.plugina.PluginAManager").newInstance();
                    Log.e("------",iPluginConfig.getPluginName());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                // TODO: 2019/1/17 要调试 需要注释掉ActivityThreadHandlerCallback中的部分代码
                mergeDexInHostApp("plugina.apk");
                startPluginActivity();
            }
        });

        findViewById(R.id.btn_load_activity_use_path_classloader).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这个方法应该放置到 application的attachContext中
                DexClassLoader pluginAClassLoader = loadPluginApk("plugina.apk");
                DexPathClassLoader dexPathClassLoader = new DexPathClassLoader(getPackageCodePath(),getClassLoader());
                dexPathClassLoader.setDexClassLoader(pluginAClassLoader);
                //将新的classloader设置到mPackageInfo中 & 替换当前线程的ClassLoader
                try {
                    Field contextPackageInfoField = getBaseContext().getClass().getDeclaredField("mPackageInfo");
                    contextPackageInfoField.setAccessible(true);
                    Object contextPackageInfoObj = contextPackageInfoField.get(getBaseContext());
                    Field packageInfoClassLoaderField = contextPackageInfoObj.getClass().getDeclaredField("mBaseClassLoader");
                    packageInfoClassLoaderField.setAccessible(true);
                    packageInfoClassLoaderField.set(contextPackageInfoObj,dexPathClassLoader);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                Thread.currentThread().setContextClassLoader(dexPathClassLoader);

                //由于demo原因 在点击事件里hook已经晚了 因此需要再hook下ContextImpl中的classLoader
                try {
                    //6.0系统对应的classloader
                    Field contextPackageInfoField = getBaseContext().getClass().getDeclaredField("mPackageInfo");
                    contextPackageInfoField.setAccessible(true);
                    Object contextPackageInfoObj = contextPackageInfoField.get(getBaseContext());
                    Field packageInfoClassLoaderField = contextPackageInfoObj.getClass().getDeclaredField("mClassLoader");
                    packageInfoClassLoaderField.setAccessible(true);
                    packageInfoClassLoaderField.set(contextPackageInfoObj,dexPathClassLoader);

                    Field contextImplClassLoaderField = getBaseContext().getClass().getDeclaredField("mClassLoader");
                    contextImplClassLoaderField.setAccessible(true);
                    contextImplClassLoaderField.set(getBaseContext(),dexPathClassLoader);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                // TODO: 2019/1/17 要调试 需要注释掉ActivityThreadHandlerCallback中的部分代码
                startPluginActivity();
            }
        });
    }

    private void startPluginActivity() {
        ActivityThreadHandlerHooker.hookActivityThreadHooker(getBaseContext());
        Intent intent = new Intent();
        ComponentName componentName = null;
        try {
            componentName = new ComponentName(getBaseContext(),getClassLoader().loadClass("com.szy.plugina.PluginAActivityA"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//                ComponentName componentName = new ComponentName(PreManifestLoadPluginActivity.this,StubStandardActivity.class);
        intent.setComponent(componentName);
        startActivity(intent);
    }
}
