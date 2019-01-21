package com.szy.plugintestproject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.szy.plugininterfacesmodule.BasePluginActivity;
import com.szy.plugininterfacesmodule.IServiceLogBinder;
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

        final ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.e("------","service connected");
                IServiceLogBinder iServiceLogBinder = (IServiceLogBinder) service;
                iServiceLogBinder.logServiceBinder();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.e("------","service disconnected");
            }
        };

        findViewById(R.id.btn_bind_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(PluginServiceActivity.this,"com.szy.plugina.PluginAServiceA"));
                getBaseContext().bindService(intent, serviceConnection,Context.BIND_AUTO_CREATE);
                Toast.makeText(PluginServiceActivity.this,"绑定服务",Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_unbind_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBaseContext().unbindService(serviceConnection);
                Toast.makeText(PluginServiceActivity.this,"解绑服务",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
