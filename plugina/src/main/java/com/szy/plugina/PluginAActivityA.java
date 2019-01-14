package com.szy.plugina;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.szy.plugininterfacesmodule.BasePluginActivity;

/**
 * Created by songzhiyang on 2019/1/12.
 *
 * @author songzhiyang
 */
public class PluginAActivityA extends BasePluginActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = LayoutInflater.from(getBaseContext()).inflate(R.layout.activity_plugin_a_a,null,false);
        setContentView(rootView);
    }
}
