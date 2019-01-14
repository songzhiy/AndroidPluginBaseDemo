package com.szy.plugininterfacesmodule;

import android.app.Activity;
import android.content.res.Resources;

/**
 * Created by songzhiyang on 2019/1/12.
 *
 * @author songzhiyang
 */
public class BasePluginActivity extends Activity{

    @Override
    public Resources getResources() {
        if (ResouceHelper.sResources != null) {
            return ResouceHelper.sResources;
        }
        return super.getResources();
    }
}
