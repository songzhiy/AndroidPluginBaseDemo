package com.szy.plugininterfacesmodule.that;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by songzhiyang on 2019/1/28.
 *
 * @author songzhiyang
 */
public interface IServiceIifeCycle {

    public void onCreate();

    public IBinder onBind(Intent intent);

    public int onStartCommand(Intent intent, int flags, int startId);

    public boolean onUnbind(Intent intent);

    public void onDestroy();

    public void setProxy(Service proxy);
}
