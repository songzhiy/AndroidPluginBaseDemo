package com.szy.plugintestproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by songzhiyang on 2019/1/23.
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

            }
        });

        findViewById(R.id.btn_test_content_provider).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.btn_test_transform_content_provider).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
