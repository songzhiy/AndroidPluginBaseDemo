package com.szy.plugina.that;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.szy.plugininterfacesmodule.that.IServiceIifeCycle;
import com.szy.plugininterfacesmodule.that.IThatServiceBinder;

/**
 * Created by songzhiyang on 2019/1/28.
 *
 * @author songzhiyang
 */
public class ThatPluginAService extends Service implements IServiceIifeCycle{

    private Service that = null;

    @Override
    public void onCreate() {
        Log.e("------","onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("------","onBind");
        return new ThatPluginAServiceBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("------","onStartCommand");
        return 0;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("------","onUnbind");
        return false;
    }

    @Override
    public void onDestroy() {
        Log.e("------","onDestroy");
    }

    @Override
    public void setProxy(Service proxy) {
        that = proxy;
    }

    public class ThatPluginAServiceBinder extends Binder implements IThatServiceBinder {

        @Override
        public String getThatServiceName() {
            return "That pluginA Service";
        }
    }
}
