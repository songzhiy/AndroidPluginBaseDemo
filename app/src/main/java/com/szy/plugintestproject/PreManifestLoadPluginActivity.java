package com.szy.plugintestproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.szy.plugininterfacesmodule.IPluginSkinConfig;
import com.szy.plugintestproject.hook.ActivityStartHooker;

import dalvik.system.DexClassLoader;

/**
 * Created by songzhiyang on 2019/1/9.
 *
 * 实现启动插件activity，并可以点击button，弹出toast
 *
 * 1、占坑的manifest提供  已提供standard
 * 2、资源的合并操作
 * 3、hook activity跳转所需的地方
 * 4、hook 插件dex 方式有下面三种
 *
 * 1、实现使用Hook LoadedApk命中缓存的方式进行classloader的处理
 * 2、实现将插件dex合并到宿主dex中的方式进行
 * 3、实现继承pathclassloader的方式，持有dexclassloader 进行findclass操作
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
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PreManifestLoadPluginActivity.this,HostSkinActivity.class));
            }
        });
    }
}