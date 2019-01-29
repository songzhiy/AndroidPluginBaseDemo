package com.szy.plugintestproject.that.service;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by songzhiyang on 2019/1/29.
 *
 * @author songzhiyang
 */
public class ThatPluginServiceManager {

    private static final Map<String,ServiceInfo> mPluginServiceInfoCache = new HashMap<>();

    public static void loadPluginService(Context context, String pluginName) {
        try {
            File apkFile = new File(context.getFilesDir(), pluginName);
            //加载apk file 然后获取Package对象 缓存里面的相关信息
            Class packageParserClazz = context.getClassLoader().loadClass("android.content.pm.PackageParser");
            Object packageParserObj = packageParserClazz.newInstance();
            Class[] parsePackageParamsClazz = new Class[]{File.class, int.class};
            Method parsePackageMethod = packageParserClazz.getMethod("parsePackage", parsePackageParamsClazz);
            Object[] parsePackageParams = new Object[]{apkFile, PackageManager.GET_SERVICES};
            Object packageObj = parsePackageMethod.invoke(packageParserObj,parsePackageParams);
            //反射Package对象中的Services集合
            Field servicesListField = packageObj.getClass().getDeclaredField("services");
            servicesListField.setAccessible(true);
            ArrayList servicesListObj = (ArrayList) servicesListField.get(packageObj);
            for (Object serviceObj : servicesListObj) {
                ServiceInfo serviceInfo = (ServiceInfo) serviceObj.getClass().getField("info").get(serviceObj);
                mPluginServiceInfoCache.put(serviceInfo.name,serviceInfo);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
