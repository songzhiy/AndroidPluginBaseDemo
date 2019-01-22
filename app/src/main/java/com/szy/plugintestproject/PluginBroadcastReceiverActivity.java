package com.szy.plugintestproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

/**
 * Created by songzhiyang on 2019/1/22.
 *
 * 这里包含三部分：
 * 1、动态注册广播的处理 由于动态广播是用代码写的 因此只要能加载类 调用代码注册即可
 * 2、静态注册广播的处理 扫描插件apk 获取插件manifest文件中receiver信息 进行动态注册即可
 *    但2方案有个问题 就是失去了静态广播的能力 因此可以使用方案3处理
 * 3、在host宿主中添加一个receiver，其action为所有插件的触发条件，由host receiver进行静态注册并中转分发
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
                Toast.makeText(PluginBroadcastReceiverActivity.this,"加载动态注册广播接收者方案",Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_manifest_register_receiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PluginBroadcastReceiverActivity.this,"加载静态注册广播接收者方案",Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_host_manifest_register_receiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PluginBroadcastReceiverActivity.this,"宿主注册manifest加载插件静态注册广播接收者方案",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
