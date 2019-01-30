package com.szy.plugintestproject.that;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.szy.plugininterfacesmodule.Constants;
import com.szy.plugininterfacesmodule.that.IThatServiceBinder;
import com.szy.plugintestproject.BaseActivity;
import com.szy.plugintestproject.R;
import com.szy.plugintestproject.that.service.ThatAMNHooker;
import com.szy.plugintestproject.that.service.ThatPluginServiceManager;

/**
 * Created by songzhiyang on 2019/1/28.
 *
 * 这个页面主要展示一下两种功能：
 * 1、使用that代理的思想 进行service的start/stop bind/unbind处理
 * 2、使用that代理加动态获取service的hook方法，使用一个占坑service来负责回调所有的插件service工作
 *
 *    1、先读取插件manifest中关于service的所有信息，保存在一个ServiceManager中
 *    2、Hook掉start service的方法 替换成ServiceProxy
 *    3、在onStartCommand中去使用that回调插件service onCreate/onStartCommand
 *    4、Hook Stop方法 判断ServiceManager中是否还存在运行的插件service 有则停止掉目标service 没有停止掉ProxyService
 *
 *    bind/unbind 类似
 *
 * @author songzhiyang
 */
public class ThatServiceActivity extends BaseActivity{

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        //预先加载插件service和hook start/stop service相关代码
        mergeDexInHostApp("plugina.apk");
        ThatPluginServiceManager.loadPluginService(newBase,"plugina.apk");
        ThatAMNHooker.thatHookAMN(newBase);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_that_service);

        findViewById(R.id.btn_start_dynamic_plugin_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ComponentName componentName = new ComponentName(ThatServiceActivity.this,"com.szy.plugina.that.ThatPluginAService");
                intent.setComponent(componentName);
                startService(intent);
            }
        });

        findViewById(R.id.btn_stop_dynamic_plugin_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ComponentName componentName = new ComponentName(ThatServiceActivity.this,"com.szy.plugina.that.ThatPluginAService");
                intent.setComponent(componentName);
                stopService(intent);
            }
        });

        findViewById(R.id.btn_start_dynamic_plugin_service2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ComponentName componentName = new ComponentName(ThatServiceActivity.this,"com.szy.plugina.that.ThatPluginAService2");
                intent.setComponent(componentName);
                startService(intent);
            }
        });

        findViewById(R.id.btn_stop_dynamic_plugin_service2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ComponentName componentName = new ComponentName(ThatServiceActivity.this,"com.szy.plugina.that.ThatPluginAService2");
                intent.setComponent(componentName);
                stopService(intent);
            }
        });

        findViewById(R.id.btn_bind_dynamic_plugin_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ComponentName componentName = new ComponentName(ThatServiceActivity.this,"com.szy.plugina.that.ThatPluginAService");
                intent.setComponent(componentName);
                bindService(intent,service1, Service.BIND_AUTO_CREATE);
            }
        });

        findViewById(R.id.btn_unbind_dynamic_plugin_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(service1);
            }
        });

        findViewById(R.id.btn_bind_dynamic_plugin_service2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ComponentName componentName = new ComponentName(ThatServiceActivity.this,"com.szy.plugina.that.ThatPluginAService2");
                intent.setComponent(componentName);
                bindService(intent,service2, Service.BIND_AUTO_CREATE);
            }
        });

        findViewById(R.id.btn_unbind_dynamic_plugin_service2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(service2);
            }
        });
    }

    private ServiceConnection service1 = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IThatServiceBinder iThatServiceBinder = (IThatServiceBinder) service;
            Log.e("------","onServiceConnected --- " + iThatServiceBinder.getThatServiceName());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("------","service1 --- onServiceDisconnected");
        }
    };

    private ServiceConnection service2 = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IThatServiceBinder iThatServiceBinder = (IThatServiceBinder) service;
            Log.e("------","onServiceConnected --- " + iThatServiceBinder.getThatServiceName());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("------","service2 --- onServiceDisconnected");
        }
    };

}
