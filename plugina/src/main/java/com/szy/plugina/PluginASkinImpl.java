package com.szy.plugina;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.szy.plugininterfacesmodule.IPluginSkinConfig;

/**
 * Created by songzhiyang on 2018/12/19.
 *
 * @author songzhiyang
 */
public class PluginASkinImpl implements IPluginSkinConfig{
    @Override
    public String getPluginName(Context context) {
        if (context == null) {
            return "";
        }
        return context.getResources().getString(R.string.plugin_name);
    }

    @Override
    public Drawable getPluginIcon(Context context) {
        if (context == null) {
            return null;
        }
        return context.getResources().getDrawable(R.drawable.ic_plugin_logo);
    }
}
