package com.szy.plugininterfacesmodule;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by songzhiyang on 2018/12/19.
 *
 * @author songzhiyang
 */
public interface IPluginSkinConfig {

    String getPluginName(Context context);

    Drawable getPluginIcon(Context context);
}
