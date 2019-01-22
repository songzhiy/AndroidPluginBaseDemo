package com.szy.plugintestproject.hook.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by songzhiyang on 2019/1/22.
 *
 * @author songzhiyang
 */
public class BroadcastDynamicRegisterHooker {

    public static void dynamicRegiterPluginBroadcastReceiver(Context context,String pluginName) {
        File pluginApk = new File(context.getFilesDir(),pluginName);

        try {
            //通过PackageParser拿到解析插件manifest文件，获取receivers集合
            Class packageParserClazz = context.getClassLoader().loadClass("android.content.pm.PackageParser");
            Object packageParserObj = packageParserClazz.newInstance();
            //通过PackageParser的parsePackage()获取apk的package对象，然后反射获取receivers集合
            Class[] parsePackageParamClass = new Class[]{File.class,int.class};
            Object[] parsePackageParams = new Object[]{pluginApk, PackageManager.GET_RECEIVERS};
            Method parsePackageMethod = packageParserClazz.getMethod("parsePackage",parsePackageParamClass);
            parsePackageMethod.setAccessible(true);
            Object packageObj = parsePackageMethod.invoke(packageParserObj,parsePackageParams);
            //反射获取Package对象中的receivers集合
            Field receiversField = packageObj.getClass().getDeclaredField("receivers");
            receiversField.setAccessible(true);
            Object receiversObj = receiversField.get(packageObj);
            List receiverList = (List) receiversObj;
            //将获取到的receiver list进行遍历动态注册
            for (Object receiver : receiverList) {
                //获取要注册的receiver的classname
                Field classNameField = context.getClassLoader().loadClass("android.content.pm.PackageParser$Component").getDeclaredField("className");
                classNameField.setAccessible(true);
                String className = (String) classNameField.get(receiver);
                //获取要注册的intent filter信息
                Field intentsFiled = context.getClassLoader().loadClass("android.content.pm.PackageParser$Component").getDeclaredField("intents");
                intentsFiled.setAccessible(true);
                List intentList = (List) intentsFiled.get(receiver);
                for (Object intent : intentList) {
                    Field actionsFiled = IntentFilter.class.getDeclaredField("mActions");
                    actionsFiled.setAccessible(true);
                    List<String> actionList = (List<String>) actionsFiled.get(intent);
                    IntentFilter intentFilter = new IntentFilter();
                    for (String action : actionList) {
                        intentFilter.addAction(action);
                    }
                    BroadcastReceiver broadcastReceiver = (BroadcastReceiver) context.getClassLoader().loadClass(className).newInstance();
                    context.registerReceiver(broadcastReceiver,intentFilter);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
