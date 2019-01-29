package com.szy.plugintestproject.that.service;

import android.content.Context;
import android.util.Log;

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
            Object proxy = Proxy.newProxyInstance(context.getClassLoader(),iActivityManager.getClass().getInterfaces(),new IActivityManagerInvocationHandler(iActivityManager));
            singletonInstanceField.set(singletonObj,proxy);
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
            Log.e("------","走了没");
            return method.invoke(iActivityManager,args);
        }
    }
}
