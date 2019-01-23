package com.szy.plugintestproject;

import android.content.ContentProvider;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.szy.plugininterfacesmodule.Constants;
import com.szy.plugintestproject.hook.contentprovider.ContentProviderHooker;

import static com.szy.plugininterfacesmodule.Constants.ContentProviderConstants.CONTENT_PROVIDER_HOST_APP_SCHEMA;

/**
 * Created by songzhiyang on 2019/1/23.
 *
 * 这个activity主要提供2个功能
 * 1、加载插件中的content provider 然后直接调用
 * 2、使用host app作为中转 进行content provider的中转处理 中转给插件
 *
 * 步骤如下：
 * 1、合并dex
 * 2、通过packageparser加载apk 并解析出里面的contentproviders
 * 3、然后通过ActivityThread的installProviders()注册到host app中
 *
 * @author songzhiyang
 */
public class PluginContentProviderActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_load_plugin_content_provider);

        findViewById(R.id.btn_load_plugin_content_provider).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mergeDexInHostApp("plugina.apk");
                ContentProviderHooker.loadPluginContentProvider(getBaseContext(),"plugina.apk");
            }
        });

        findViewById(R.id.btn_test_content_provider).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://"+ Constants.ContentProviderConstants.CONTENT_PROVIDER_PLUGIN_A_SCHEMA + "/");
                int count = getContentResolver().delete(uri,"",new String[]{});
                Log.e("------","count -- " + count);
            }
        });

        findViewById(R.id.btn_test_transform_content_provider).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://"+ CONTENT_PROVIDER_HOST_APP_SCHEMA + "/");
                int count = getContentResolver().delete(uri,"",new String[]{});
                Log.e("------","count -- " + count);
            }
        });
    }
}
