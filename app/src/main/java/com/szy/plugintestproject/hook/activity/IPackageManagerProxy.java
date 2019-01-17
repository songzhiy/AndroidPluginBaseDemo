package com.szy.plugintestproject.hook.activity;

import android.content.pm.PackageInfo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by songzhiyang on 2019/1/17.
 *
 * @author songzhiyang
 */
public class IPackageManagerProxy implements InvocationHandler {

    private Object mIActivityManagerProxy = null;

    public IPackageManagerProxy(Object iActivityManagerProxy) {
        mIActivityManagerProxy = iActivityManagerProxy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("getPackageInfo".equals(method.getName()) && "com.szy.plugina".equals(args[0].toString())) {
            return new PackageInfo();
        }
        return method.invoke(mIActivityManagerProxy,args);
    }
}
