package com.szy.plugintestproject;

import android.os.Bundle;
import android.support.annotation.Nullable;

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
    }
}
