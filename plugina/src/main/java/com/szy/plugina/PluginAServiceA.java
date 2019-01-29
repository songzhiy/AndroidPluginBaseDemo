package com.szy.plugina;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.szy.plugininterfacesmodule.IServiceLogBinder;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by songzhiyang on 2019/1/21.
 *
 * @author songzhiyang
 */
public class PluginAServiceA extends Service{
    Timer mTimer = new Timer();
    TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            Log.e("------","count -- " + i);
        }
    };
    int i = 0;
    boolean isFirst = true;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new PluginAServiceBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("------","start service");
        if (isFirst) {
            isFirst = false;
            mTimer.schedule(mTimerTask,0,1000);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        Log.e("------","stop service");
    }

    public class PluginAServiceBinder extends Binder implements IServiceLogBinder {

        @Override
        public void logServiceBinder() {
            Log.e("------","logPluginServiceBinder");
        }
    }
}
