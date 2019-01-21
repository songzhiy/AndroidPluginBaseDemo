package com.szy.plugintestproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.szy.plugininterfacesmodule.BasePluginActivity;

/**
 * Created by songzhiyang on 2019/1/21.
 *
 * @author songzhiyang
 */
public class PluginServiceActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_load_plugin_service);

        findViewById(R.id.btn_start_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PluginServiceActivity.this,"开启服务",Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_stop_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
