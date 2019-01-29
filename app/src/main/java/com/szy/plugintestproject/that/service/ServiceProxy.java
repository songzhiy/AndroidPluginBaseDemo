package com.szy.plugintestproject.that.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.szy.plugininterfacesmodule.Constants;
import com.szy.plugininterfacesmodule.that.IServiceIifeCycle;

/**
 * Created by songzhiyang on 2019/1/28.
 *
 * @author songzhiyang
 */
public class ServiceProxy extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("------","ServiceProxy --- onBind!!!");
        return null;
    }

    @Override
    public void onCreate() {
        Log.e("------","ServiceProxy --- onCreate!!!");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent realIntent = intent.getParcelableExtra(Constants.ThatConstants.THAT_INTENT_PLUGIN_SERVICE_REAL_INTENT);
        String realIntentClassStr = realIntent.getComponent().getClassName();
        IServiceIifeCycle targetService = ThatPluginServiceManager.mPluginServiceObjCache.get(realIntentClassStr);
        if (targetService == null) {
            try {
                targetService = (IServiceIifeCycle) getClassLoader().loadClass(realIntentClassStr).newInstance();
                ThatPluginServiceManager.mPluginServiceObjCache.put(realIntentClassStr,targetService);
                targetService.onCreate();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return targetService.onStartCommand(intent,flags,startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("------","ServiceProxy --- onUnbind!!!");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("------","ServiceProxy --- onDestroy!!!");
    }
}
