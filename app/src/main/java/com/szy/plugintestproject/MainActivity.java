package com.szy.plugintestproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.szy.plugininterfacesmodule.IPluginConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import dalvik.system.DexClassLoader;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.load_plugin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DexClassLoader dexClassLoader = loadPluginApk("plugina.apk");
                try {
                    Class pluginAManagerClazz = dexClassLoader.loadClass("com.szy.plugina.PluginAManager");
                    IPluginConfig pluginAConfig = (IPluginConfig) pluginAManagerClazz.newInstance();
                    Toast.makeText(MainActivity.this,pluginAConfig.getPluginName(),Toast.LENGTH_SHORT).show();
                    pluginAConfig.updatePluginName("MainActivity");
                    Toast.makeText(MainActivity.this,pluginAConfig.getPluginName(),Toast.LENGTH_SHORT).show();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn_jump_2_skin_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this,HostSkinActivity.class));
            }
        });

        findViewById(R.id.btn_merge_dex_and_resource).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this,MergeDexAndResourceActivity.class));
            }
        });

        findViewById(R.id.btn_pre_load_plugin_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this,PreManifestLoadPluginActivity.class));
            }
        });

        findViewById(R.id.btn_load_plugin_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this,PluginServiceActivity.class));
            }
        });

        findViewById(R.id.btn_load_plugin_broadcast_receiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,PluginBroadcastReceiverActivity.class));
            }
        });

        findViewById(R.id.btn_load_plugin_content_provider).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,PluginContentProviderActivity.class));
            }
        });
    }
}
