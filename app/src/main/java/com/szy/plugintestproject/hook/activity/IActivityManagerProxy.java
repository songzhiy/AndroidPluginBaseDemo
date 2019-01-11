package com.szy.plugintestproject.hook.activity;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by songzhiyang on 2019/1/11.
 *
 * @author songzhiyang
 */
public class IActivityManagerProxy implements InvocationHandler{

    private Object mIActivityManager = null;

    public IActivityManagerProxy(Object iActivityManager) {
        mIActivityManager = iActivityManager;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("startActivity".equals(method.getName())) {
            Log.e("------","hook啦！！！");
            return method.invoke(mIActivityManager,args);
        }
        return method.invoke(mIActivityManager,args);
    }
}
