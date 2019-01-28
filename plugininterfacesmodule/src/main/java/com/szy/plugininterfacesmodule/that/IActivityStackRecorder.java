package com.szy.plugininterfacesmodule.that;

/**
 * Created by songzhiyang on 2019/1/28.
 *
 * @author songzhiyang
 */
public interface IActivityStackRecorder {
    String getClassName();
    void finish();
    String getLaunchMode();
}
