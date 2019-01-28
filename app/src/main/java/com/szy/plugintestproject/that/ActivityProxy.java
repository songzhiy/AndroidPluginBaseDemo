package com.szy.plugintestproject.that;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.szy.plugininterfacesmodule.Constants;
import com.szy.plugininterfacesmodule.that.IActivityLifeCycle;
import com.szy.plugininterfacesmodule.that.IActivityStackRecorder;
import com.szy.plugintestproject.BaseActivity;

/**
 * Created by songzhiyang on 2019/1/26.
 *
 * 通过使用that框架的ActivityProxy 使得插件的activity彻底沦落为一个普通的类
 * 通过真正的activity -- ActivityProxy的生命周期来控制插件activity的回调方法
 * 但是存在一个问题 即activity的启动模式失效
 * 为了解决这个问题 可以手动做一个activity栈 当activity启动起来之后 可以把其放置到某个栈中
 * 但是这个方法无法解决插件重新调用host app中activity的情况
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
        String launchMode = getIntent().getStringExtra(Constants.ThatConstants.THAT_INTENT_ACTIVITY_LAUNCH_MODE);
        if (TextUtils.isEmpty(launchMode)) {
            launchMode = Constants.ThatConstants.THAT_INTENT_ACTIVITY_LAUNCH_MODE_STANDARD;
        }

        mergeDexInHostApp(pluginName);
        mergeResource(pluginName);

        try {
            mIActivityLifeCycle = (IActivityLifeCycle) getClassLoader().loadClass(activityName).newInstance();
            mIActivityLifeCycle.setProxy(this);
            mIActivityLifeCycle.setLaunchMode(launchMode);
            mIActivityLifeCycle.onCreate(savedInstanceState);

            ActivityStackRecorder.dealActivityStack((IActivityStackRecorder) mIActivityLifeCycle);
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
