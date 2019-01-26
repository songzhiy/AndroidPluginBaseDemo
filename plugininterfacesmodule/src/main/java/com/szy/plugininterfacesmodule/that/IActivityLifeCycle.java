package com.szy.plugininterfacesmodule.that;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by songzhiyang on 2019/1/26.
 *
 * @author songzhiyang
 */
public interface IActivityLifeCycle {
    public void onCreate(@Nullable Bundle savedInstanceState);

    public void onStart();

    public void onRestart();

    public void onResume();

    public void onPause();

    public void onStop();

    public void onDestroy();

    public void onActivityResult(int requestCode, int resultCode, Intent data);

    public void setProxy(Activity activity);
}
