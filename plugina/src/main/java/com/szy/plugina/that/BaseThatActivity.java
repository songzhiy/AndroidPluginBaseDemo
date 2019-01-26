package com.szy.plugina.that;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.szy.plugininterfacesmodule.that.IActivityLifeCycle;

/**
 * Created by songzhiyang on 2019/1/26.
 *
 * @author songzhiyang
 */
public class BaseThatActivity implements IActivityLifeCycle{

    protected Activity that;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("------","onCreate");
    }

    @Override
    public void onStart() {
        Log.e("------","onStart");
    }

    @Override
    public void onRestart() {
        Log.e("------","onRestart");
    }

    @Override
    public void onResume() {
        Log.e("------","onResume");
    }

    @Override
    public void onPause() {
        Log.e("------","onPause");
    }

    @Override
    public void onStop() {
        Log.e("------","onStop");
    }

    @Override
    public void onDestroy() {
        Log.e("------","onDestroy");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("------","onActivityResult");
    }

    @Override
    public void setProxy(Activity activity) {
        that = activity;
    }
}
