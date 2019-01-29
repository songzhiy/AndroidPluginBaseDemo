package com.szy.plugina.that;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.szy.plugininterfacesmodule.that.IServiceIifeCycle;

/**
 * Created by songzhiyang on 2019/1/28.
 *
 * @author songzhiyang
 */
public class ThatPluginAService2 extends Service implements IServiceIifeCycle{

    private Service that = null;

    @Override
    public void onCreate() {
        Log.e("------","ThatPluginAService2 - onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("------","ThatPluginAService2 - onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("------","ThatPluginAService2 - onStartCommand");
        return 0;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("------","ThatPluginAService2 - onUnbind");
        return false;
    }

    @Override
    public void onDestroy() {
        Log.e("------","ThatPluginAService2 - onDestroy");
    }

    @Override
    public void setProxy(Service proxy) {
        that = proxy;
    }
}
