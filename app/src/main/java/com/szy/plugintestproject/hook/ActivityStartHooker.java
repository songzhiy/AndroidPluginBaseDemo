package com.szy.plugintestproject.hook;

import android.app.ActivityManager;
import android.content.Context;

import com.szy.plugintestproject.hook.activity.IActivityManagerProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * Created by songzhiyang on 2019/1/11.
 *
 * @author songzhiyang
 */
public class ActivityStartHooker {

    public static void hookActivityStarter(Context context) {
        //先获取到ActivityManager.IActivityManagerSingleton(Singleton<IActivityManager>)
        Class activityManagerNativeClazz = null;
        try {
            activityManagerNativeClazz = Class.forName("android.app.ActivityManagerNative");
            Field iActivityManagerSingletonField = activityManagerNativeClazz.getDeclaredField("gDefault");
            iActivityManagerSingletonField.setAccessible(true);
            Object iActivityManagerSingleton = iActivityManagerSingletonField.get(null);
            Field iActivityManagerField = iActivityManagerSingletonField.getType().getDeclaredField("mInstance");
            iActivityManagerField.setAccessible(true);
            //准备一个动态代理类 IActivityManagerProxy 代理 IActivityManager
            Object iActivityManagerProxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), iActivityManagerField.get(iActivityManagerSingleton).getClass().getInterfaces(), new IActivityManagerProxy(iActivityManagerField.get(iActivityManagerSingleton)));
            iActivityManagerField.set(iActivityManagerSingleton, iActivityManagerProxy);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
