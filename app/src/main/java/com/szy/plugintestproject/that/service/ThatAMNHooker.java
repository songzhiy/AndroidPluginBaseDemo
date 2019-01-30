package com.szy.plugintestproject.that.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;

import com.szy.plugininterfacesmodule.Constants;
import com.szy.plugininterfacesmodule.that.IActivityLifeCycle;
import com.szy.plugininterfacesmodule.that.IServiceIifeCycle;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by songzhiyang on 2019/1/29.
 *
 * @author songzhiyang
 */
public class ThatAMNHooker {

    public static void thatHookAMN(Context context) {
        try {
            Class amnClazz = context.getClassLoader().loadClass("android.app.ActivityManagerNative");
            Field defaultField = amnClazz.getDeclaredField("gDefault");
            defaultField.setAccessible(true);
            Object singletonObj = defaultField.get(null);
            Field singletonInstanceField = singletonObj.getClass().getSuperclass().getDeclaredField("mInstance");
            singletonInstanceField.setAccessible(true);
            Object iActivityManager = singletonInstanceField.get(singletonObj);

            //构造IActivityManager接口的动态代理类
            Object proxy = Proxy.newProxyInstance(context.getClassLoader(), iActivityManager.getClass().getInterfaces(), new IActivityManagerInvocationHandler(iActivityManager));
            singletonInstanceField.set(singletonObj, proxy);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static class IActivityManagerInvocationHandler implements InvocationHandler {

        private Object iActivityManager = null;

        public IActivityManagerInvocationHandler(Object realObj) {
            iActivityManager = realObj;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("startService".equals(method.getName())) {
                //替换intent
                int intentIndex = -1;
                for (int i = 0; i < args.length; i++) {
                    if (args[i] instanceof Intent) {
                        intentIndex = i;
                    }
                }
                if (intentIndex == -1) {
                    return method.invoke(iActivityManager, args);
                }
                Intent realIntent = (Intent) args[intentIndex];
                String pluginClassStr = realIntent.getComponent().getClassName();
                if (ThatPluginServiceManager.mPluginServiceInfoCache.get(pluginClassStr) != null) {
                    Intent proxyIntent = new Intent();
                    ComponentName componentName = new ComponentName(realIntent.getComponent().getPackageName(), "com.szy.plugintestproject.that.service.ServiceProxy");
                    proxyIntent.setComponent(componentName);
                    proxyIntent.putExtra(Constants.ThatConstants.THAT_INTENT_PLUGIN_SERVICE_REAL_INTENT, realIntent);
                    args[intentIndex] = proxyIntent;
                    return method.invoke(iActivityManager, args);
                }
            }
            if ("stopService".equals(method.getName())) {
                //替换intent
                int intentIndex = -1;
                for (int i = 0; i < args.length; i++) {
                    if (args[i] instanceof Intent) {
                        intentIndex = i;
                    }
                }
                if (intentIndex == -1) {
                    return method.invoke(iActivityManager, args);
                }
                Intent realIntent = (Intent) args[intentIndex];
                String pluginClassStr = realIntent.getComponent().getClassName();
                if (ThatPluginServiceManager.mPluginServiceObjCache.get(pluginClassStr) != null) {
                    IServiceIifeCycle iServiceIifeCycle = (IServiceIifeCycle) ThatPluginServiceManager.mPluginServiceObjCache.remove(pluginClassStr);
                    iServiceIifeCycle.onDestroy();
                    if (ThatPluginServiceManager.mPluginServiceObjCache.size() == 0) {
                        Intent proxyIntent = new Intent();
                        ComponentName componentName = new ComponentName(realIntent.getComponent().getPackageName(), "com.szy.plugintestproject.that.service.ServiceProxy");
                        proxyIntent.setComponent(componentName);
                        proxyIntent.putExtra(Constants.ThatConstants.THAT_INTENT_PLUGIN_SERVICE_REAL_INTENT, realIntent);
                        args[intentIndex] = proxyIntent;
                        return method.invoke(iActivityManager, args);
                    }
                    return 1;
                }
            }
            if ("bindService".equals(method.getName())) {
                //替换intent
                int intentIndex = -1;
                int serviceConnectionIndex = 4;
                for (int i = 0; i < args.length; i++) {
                    if (args[i] instanceof Intent) {
                        intentIndex = i;
                    }
                }
                if (intentIndex != -1) {
                    //缓存ServiceConnection 与 RealIntent之间的关系
                    Object serviceConnection = (Object) args[serviceConnectionIndex];
                    ThatPluginServiceManager.mPluginServiceConn2RealIntentCache.put(serviceConnection, (Intent) args[intentIndex]);
                    //替换intent
                    Intent realIntent = (Intent) args[intentIndex];
                    String pluginClassStr = realIntent.getComponent().getClassName();
                    if (ThatPluginServiceManager.mPluginServiceInfoCache.get(pluginClassStr) != null) {
                        Intent proxyIntent = new Intent();
                        ComponentName componentName = new ComponentName(realIntent.getComponent().getPackageName(), "com.szy.plugintestproject.that.service.ServiceProxy");
                        proxyIntent.setComponent(componentName);
                        proxyIntent.putExtra(Constants.ThatConstants.THAT_INTENT_PLUGIN_SERVICE_REAL_INTENT, realIntent);
                        args[intentIndex] = proxyIntent;
                        return method.invoke(iActivityManager, args);
                    }
                }
            }
            if ("unbindService".equals(method.getName())) {
                int serviceConnectionIndex = 0;
                Object serviceConnection = (Object) args[serviceConnectionIndex];
                Intent realIntent = ThatPluginServiceManager.mPluginServiceConn2RealIntentCache.get(serviceConnection);
                String pluginServiceName = realIntent.getComponent().getClassName();
                IServiceIifeCycle iServiceIifeCycle = ThatPluginServiceManager.mPluginServiceObjCache.remove(pluginServiceName);
                if (iServiceIifeCycle != null) {
                    iServiceIifeCycle.onUnbind(realIntent);
                }
                if (ThatPluginServiceManager.mPluginServiceObjCache.size() == 0) {
                    return method.invoke(iActivityManager,args);
                } else {
                    return 1;
                }
            }

            return method.invoke(iActivityManager, args);
        }
    }


}
