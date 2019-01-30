package com.szy.plugintestproject.that.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
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
        return new TransformBinder();
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
                targetService.setProxy(this);
                targetService.onCreate();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return targetService.onStartCommand(realIntent,flags,startId);
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

    public class TransformBinder extends Binder implements ITransformBinder {

        @Override
        public IBinder bindService(Intent service, ServiceConnection conn, int flags) {
            Log.e("------","ServiceProxy --- onBind!!!");
            Intent realIntent = service;
            String realIntentClassStr = realIntent.getComponent().getClassName();
            IServiceIifeCycle targetService = ThatPluginServiceManager.mPluginServiceObjCache.get(realIntentClassStr);
            if (targetService == null) {
                try {
                    targetService = (IServiceIifeCycle) getClassLoader().loadClass(realIntentClassStr).newInstance();
                    ThatPluginServiceManager.mPluginServiceObjCache.put(realIntentClassStr,targetService);
                    targetService.setProxy(ServiceProxy.this);
                    targetService.onCreate();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
            IBinder targetBinder = targetService.onBind(realIntent);
            conn.onServiceConnected(realIntent.getComponent(),targetBinder);
            return targetBinder;
        }

        @Override
        public void unbindService(ServiceConnection conn) {
            Log.e("------","ServiceProxy --- unBind!!!");
            conn.onServiceDisconnected(new ComponentName("",""));
        }
    }

    public interface ITransformBinder {
        IBinder bindService(Intent service, ServiceConnection conn, int flags);
        void unbindService(ServiceConnection conn);
    }
}
