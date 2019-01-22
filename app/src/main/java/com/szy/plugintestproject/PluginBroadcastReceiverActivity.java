package com.szy.plugintestproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.szy.plugininterfacesmodule.Constants;
import com.szy.plugininterfacesmodule.IBroadcastReceiverRegister;
import com.szy.plugintestproject.hook.receiver.BroadcastDynamicRegisterHooker;

/**
 * Created by songzhiyang on 2019/1/22.
 *
 * 这里包含三部分：
 * 1、动态注册广播的处理 由于动态广播是用代码写的 因此只要能加载类 调用代码注册即可
 * 2、静态注册广播的处理 扫描插件apk 获取插件manifest文件中receiver信息 进行动态注册即可
 *    但2方案有个问题 就是失去了静态广播的能力 因此可以使用方案3处理
 * 3、在host宿主中添加一个receiver，其action为所有插件的触发条件，由host receiver进行静态注册并中转分发
 *    3主要是为了解决2丧失静态注册广播的优势 因此需要在宿主中注册所有的插件中需要的广播 作为action
 *    然后将action进行解析转发 转发给对应的插件广播接收者
 *
 * @author songzhiyang
 */
public class PluginBroadcastReceiverActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_load_plugin_broadcast_receiver);

        findViewById(R.id.btn_dynamic_register_receiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mergeDexInHostApp("plugina.apk");
                try {
                    //调用插件的动态注册广播接收者
                    IBroadcastReceiverRegister pluginARegister = (IBroadcastReceiverRegister) getClassLoader().loadClass("com.szy.plugina.PluginABroadcastReceiverManager").newInstance();
                    pluginARegister.registerBroadcastReceiver(getBaseContext());
                    //测试代码
                    Intent testBroadcastIntent = new Intent();
                    testBroadcastIntent.setAction(Constants.BroadcastReceiverConstants.DYNAMIC_REGISTER_PLUGIN_BROADCAST_RECIEVER);
                    getBaseContext().sendBroadcast(testBroadcastIntent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn_manifest_register_receiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mergeDexInHostApp("plugina.apk");
                //手动解析插件apk的manifest文件 然后进行动态注册
                BroadcastDynamicRegisterHooker.dynamicRegiterPluginBroadcastReceiver(getBaseContext(),"plugina.apk");
            }
        });

        findViewById(R.id.btn_host_manifest_register_receiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mergeDexInHostApp("plugina.apk");
                //这里理论上应该放置到 application的attachBaseContext回调中，因为要启动receiver需要先启动application
                StubBroadcastReceiver.loadPluginReceivers(getBaseContext());
            }
        });

        findViewById(R.id.btn_test_plugina_broadcast1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Constants.BroadcastReceiverConstants.MANIFEST_REGISTER_PLUGIN_BROADCAST_RECEIVER1);
                sendBroadcast(intent);
            }
        });

        findViewById(R.id.btn_test_plugina_broadcast2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Constants.BroadcastReceiverConstants.MANIFEST_REGISTER_PLUGIN_BROADCAST_RECEIVER2);
                sendBroadcast(intent);
            }
        });

        findViewById(R.id.btn_test_host_broadcast1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StubBroadcastReceiver.HOST_RECEIVER_1);
                sendBroadcast(intent);
            }
        });

        findViewById(R.id.btn_test_host_broadcast2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StubBroadcastReceiver.HOST_RECEIVER_2);
                sendBroadcast(intent);
            }
        });
    }
}
