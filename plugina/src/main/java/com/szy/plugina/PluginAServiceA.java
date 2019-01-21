package com.szy.plugina;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("------","start service");
        if (isFirst) {
            isFirst = false;
            mTimer.schedule(mTimerTask,0,10);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        Log.e("------","stop service");
    }
}
