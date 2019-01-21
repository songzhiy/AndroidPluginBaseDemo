package com.szy.plugintestproject.hook.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

import com.szy.plugintestproject.StubService;
import com.szy.plugintestproject.StubStandardActivity;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static com.szy.plugintestproject.hook.constants.HookConstants.INTENT_EXTRA_REAL_INTENT;

/**
 * Created by songzhiyang on 2019/1/11.
 *
 * @author songzhiyang
 */
public class IActivityManagerProxy implements InvocationHandler{

    private Object mIActivityManager = null;

    public IActivityManagerProxy(Object iActivityManager) {
        mIActivityManager = iActivityManager;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("startActivity".equals(method.getName())) {
            //hook activity
            Log.e("------","hook啦！！！");
            int intentIndex = 0;
            for (int i=0;i<args.length;i++) {
                if (args[i] instanceof Intent) {
                    intentIndex = i;
                    break;
                }
            }
            if (args[intentIndex] instanceof Intent) {
                Intent intent = (Intent) args[intentIndex];
                ComponentName componentName = intent.getComponent();
                if ("com.szy.plugina.PluginAActivityA".equals(componentName.getClassName())) {
                    Log.e("------","hook到要找的插件activity了！！");
                    //替换activity的intent
                    Intent stubIntent = new Intent();
                    stubIntent.setComponent(new ComponentName("com.szy.plugintestproject", StubStandardActivity.class.getName()));
                    stubIntent.putExtra(INTENT_EXTRA_REAL_INTENT,intent);
                    args[intentIndex] = stubIntent;
                }
            }
            Log.e("------","替换完的 --- " + ((Intent)args[intentIndex]).getComponent().getClassName());
            return method.invoke(mIActivityManager,args);
        }
        if ("startService".equals(method.getName())) {
            //hook service
            Log.e("------","start service -- hook!");
            int intentIndex = 0;
            for (int i=0;i<args.length;i++) {
                if (args[i] instanceof Intent) {
                    intentIndex = i;
                    break;
                }
            }
            Intent intent = (Intent) args[intentIndex];
            if ("com.szy.plugina.PluginAServiceA".equals(intent.getComponent().getClassName())) {
                Intent stubIntent = new Intent();
                ComponentName componentName = new ComponentName(intent.getComponent().getPackageName(), StubService.class.getName());
                stubIntent.setComponent(componentName);
                stubIntent.putExtra(INTENT_EXTRA_REAL_INTENT,intent);
                args[intentIndex] = stubIntent;
                return method.invoke(mIActivityManager,args);
            }
        }
        if ("stopService".equals(method.getName())) {
            //hook service
            Log.e("------","stop service -- hook!");
            int intentIndex = 0;
            for (int i=0;i<args.length;i++) {
                if (args[i] instanceof Intent) {
                    intentIndex = i;
                    break;
                }
            }
            Intent intent = (Intent) args[intentIndex];
            if ("com.szy.plugina.PluginAServiceA".equals(intent.getComponent().getClassName())) {
                Intent stubIntent = new Intent();
                ComponentName componentName = new ComponentName(intent.getComponent().getPackageName(), StubService.class.getName());
                stubIntent.setComponent(componentName);
                stubIntent.putExtra(INTENT_EXTRA_REAL_INTENT,intent);
                args[intentIndex] = stubIntent;
                return method.invoke(mIActivityManager,args);
            }
        }
        return method.invoke(mIActivityManager,args);
    }
}
