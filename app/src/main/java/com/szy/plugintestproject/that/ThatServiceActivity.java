package com.szy.plugintestproject.that;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.szy.plugintestproject.BaseActivity;
import com.szy.plugintestproject.R;

/**
 * Created by songzhiyang on 2019/1/28.
 *
 * 这个页面主要展示一下两种功能：
 * 1、使用that代理的思想 进行service的start/stop bind/unbind处理
 * 2、使用that代理加动态获取service的hook方法，使用一个占坑service来负责回调所有的插件service工作
 *
 * @author songzhiyang
 */
public class ThatServiceActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_that_service);

        findViewById(R.id.btn_start_plugin_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.btn_stop_plugin_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.btn_start_dynamic_plugin_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
