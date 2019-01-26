package com.szy.plugintestproject.that;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.szy.plugininterfacesmodule.Constants;
import com.szy.plugininterfacesmodule.that.IActivityLifeCycle;
import com.szy.plugintestproject.BaseActivity;

/**
 * Created by songzhiyang on 2019/1/26.
 *
 * @author songzhiyang
 */
public class ActivityProxy extends BaseActivity{

    private IActivityLifeCycle mIActivityLifeCycle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String pluginName = getIntent().getStringExtra(Constants.ThatConstants.THAT_INTENT_PLUGIN_NAME);
        String activityName = getIntent().getStringExtra(Constants.ThatConstants.THAT_INTENT_PLUGIN_ACTIVITY_CLASS);

        mergeDexInHostApp(pluginName);
        mergeResource(pluginName);

        try {
            mIActivityLifeCycle = (IActivityLifeCycle) getClassLoader().loadClass(activityName).newInstance();
            mIActivityLifeCycle.setProxy(this);
            mIActivityLifeCycle.onCreate(savedInstanceState);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mIActivityLifeCycle.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mIActivityLifeCycle.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIActivityLifeCycle.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIActivityLifeCycle.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIActivityLifeCycle.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIActivityLifeCycle.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mIActivityLifeCycle.onActivityResult(requestCode,resultCode,data);
    }
}
