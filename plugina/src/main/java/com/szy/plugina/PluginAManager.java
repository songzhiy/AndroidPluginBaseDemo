package com.szy.plugina;

import com.szy.plugininterfacesmodule.IPluginConfig;

/**
 * Created by songzhiyang on 2018/12/6.
 *
 * @author songzhiyang
 */
public class PluginAManager implements IPluginConfig {

    private static String PLUGIN_NAME = "pluginA";

    @Override
    public String getPluginName() {
        return PLUGIN_NAME;
    }

    @Override
    public void updatePluginName(String pluginName) {
        PLUGIN_NAME = pluginName;
    }
}
