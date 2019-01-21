package com.szy.plugintestproject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.szy.plugininterfacesmodule.BasePluginActivity;
import com.szy.plugintestproject.hook.ActivityStartHooker;
import com.szy.plugintestproject.hook.activity.ActivityThreadHandlerHooker;
import com.szy.plugintestproject.hook.activity.IActivityManagerProxy;

/**
 * Created by songzhiyang on 2019/1/21.
 *
 * @author songzhiyang
 */
public class PluginServiceActivity extends BaseActivity{

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        mergeDexInHostApp("plugina.apk");
        ActivityThreadHandlerHooker.hookActivityThreadHooker(newBase);
        ActivityStartHooker.hookActivityStarter(newBase);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_load_plugin_service);

        findViewById(R.id.btn_start_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(PluginServiceActivity.this,"com.szy.plugina.PluginAServiceA"));
                getBaseContext().startService(intent);
                Toast.makeText(PluginServiceActivity.this,"开启服务",Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_stop_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(PluginServiceActivity.this,"com.szy.plugina.PluginAServiceA"));
                getBaseContext().stopService(intent);
                Toast.makeText(PluginServiceActivity.this,"停止服务",Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_bind_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PluginServiceActivity.this,"绑定服务",Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_unbind_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PluginServiceActivity.this,"解绑服务",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
