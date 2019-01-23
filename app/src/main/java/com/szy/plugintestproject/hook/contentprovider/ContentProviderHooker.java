package com.szy.plugintestproject.hook.contentprovider;

import android.content.Context;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by songzhiyang on 2019/1/23.
 *
 * @author songzhiyang
 */
public class ContentProviderHooker {
    public static void loadPluginContentProvider(Context context, String pluginName) {
        //先加载插件中的contentprovider信息
        //然后通过ActivityThread.installProviders()注册到host app中
        File pluginAApkFile = new File(context.getFilesDir(),pluginName);
        try {
            Object packageParser = context.getClassLoader().loadClass("android.content.pm.PackageParser").newInstance();
            Class[] parsePackageParamClass = new Class[]{File.class,int.class};
            Object[] parsePackageParams = new Object[]{pluginAApkFile, PackageManager.GET_PROVIDERS};
            Method parsePackageMethod = packageParser.getClass().getMethod("parsePackage",parsePackageParamClass);
            Object packageObj = parsePackageMethod.invoke(packageParser,parsePackageParams);
            //反射获取出package对象中的provider
            Field providersField = packageObj.getClass().getDeclaredField("providers");
            providersField.setAccessible(true);
            List providersObj = (List) providersField.get(packageObj);
            //将providerList转换为providerInfoList 然后通过ActivityThread.installProviders()注册到host app中
            List<ProviderInfo> providerInfoList = new ArrayList<>();
            for (Object provider : providersObj) {
                Field providerInfoField = provider.getClass().getDeclaredField("info");
                providerInfoField.setAccessible(true);
                ProviderInfo providerInfo = (ProviderInfo) providerInfoField.get(provider);
                //替换掉ProviderInfo中的packageName
                Field packageNameField = PackageItemInfo.class.getDeclaredField("packageName");
                packageNameField.setAccessible(true);
                packageNameField.set(providerInfo,context.getPackageName());
                //替换掉applicationInfo中的packageName
                Field applicationInfoField = ComponentInfo.class.getDeclaredField("applicationInfo");
                applicationInfoField.setAccessible(true);
                Object applicationInfo = applicationInfoField.get(providerInfo);
                packageNameField.set(applicationInfo,context.getPackageName());
                providerInfoList.add(providerInfo);
            }
            //使用反射调用ActivityThread.installProviders()
            Field activityThreadField = context.getClass().getDeclaredField("mMainThread");
            activityThreadField.setAccessible(true);
            Object activityThreadObj = activityThreadField.get(context);
            Class[] installProvidersParamsClass = new Class[]{Context.class,List.class};
            Method installProvidersMethod = activityThreadObj.getClass().getDeclaredMethod("installContentProviders",installProvidersParamsClass);
            Object[] installProviderParam = new Object[]{context,providerInfoList};
            installProvidersMethod.setAccessible(true);
            installProvidersMethod.invoke(activityThreadObj,installProviderParam);
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
