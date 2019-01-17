package com.szy.plugintestproject.hook.activity;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.ArrayMap;
import android.util.Log;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.WeakHashMap;

import dalvik.system.DexClassLoader;

/**
 * Created by songzhiyang on 2019/1/15.
 *
 * @author songzhiyang
 */
public class ActivityLoadedApkCacheHook {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void hookActivityStarterLoadedApkCache(Context context, String apkName, String packageName) {
        File file = new File(context.getFilesDir(),apkName);
        try {
            //构建ApplicationInfo
            Class packageParserClazz = Class.forName("android.content.pm.PackageParser");
            Class[] paramsClass = new Class[]{Class.forName("android.content.pm.PackageParser$Package"),int.class,Class.forName("android.content.pm.PackageUserState")};
            Object packageParserObj = packageParserClazz.newInstance();
            Method parsePackageMethod = packageParserClazz.getDeclaredMethod("parsePackage",new Class[]{File.class,int.class});
            Object packageObj = parsePackageMethod.invoke(packageParserObj,new Object[]{file,0});
            Object[] params = new Object[]{packageObj,0,Class.forName("android.content.pm.PackageUserState").newInstance()};
            Method generateApplicationInfoMethod = packageParserClazz.getMethod("generateApplicationInfo",paramsClass);
            ApplicationInfo pluginApplicationInfo = (ApplicationInfo) generateApplicationInfoMethod.invoke(null,params);
            Field contextImplActivityThreadField = context.getClass().getDeclaredField("mMainThread");
            contextImplActivityThreadField.setAccessible(true);
            Object activityThreadObj = contextImplActivityThreadField.get(context);
            Class compatibilityInfoClazz = Class.forName("android.content.res.CompatibilityInfo");
            Class[] paramClass = new Class[]{ApplicationInfo.class,compatibilityInfoClazz};
            Method getPackageInfoNoCheckMethod = activityThreadObj.getClass().getMethod("getPackageInfoNoCheck",paramClass);
            Field compatibilityInfoField = compatibilityInfoClazz.getDeclaredField("DEFAULT_COMPATIBILITY_INFO");
            compatibilityInfoField.setAccessible(true);
            Object compatibilityInfoObj = compatibilityInfoField.get(null);
            Object[] getPackageInfoParams = new Object[]{pluginApplicationInfo,compatibilityInfoObj};
            Object loadedApkObj = getPackageInfoNoCheckMethod.invoke(activityThreadObj,getPackageInfoParams);
            //反射替换该loadedApkObj内的classloader
            Class loadedApkClazz = Class.forName("android.app.LoadedApk");
            Field loadedApkClassLoader = loadedApkClazz.getDeclaredField("mClassLoader");
            loadedApkClassLoader.setAccessible(true);
            DexClassLoader dexClassLoader = loadPluginApk(context,"plugina.apk");
            loadedApkClassLoader.set(loadedApkObj,dexClassLoader);
            //将该LoadedApk对象放置到ActivityThread中的packages<String,WeakReferences<LoadedApk>>缓存中
            Field activityThreadPackagesField = activityThreadObj.getClass().getDeclaredField("mPackages");
            activityThreadPackagesField.setAccessible(true);
            ArrayMap<String,WeakReference> packagesObj = (ArrayMap<String, WeakReference>) activityThreadPackagesField.get(activityThreadObj);
            packagesObj.put(packageName,new WeakReference(loadedApkObj));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private static DexClassLoader loadPluginApk(Context context,String apkName) {
        File file = new File(context.getFilesDir(),apkName);
        if (file.exists()) {
            File pluginAFile = context.getFileStreamPath(apkName);
            String dexPath = pluginAFile.getPath();
            File dexReleaseFile = context.getDir("dex", Context.MODE_PRIVATE);
            DexClassLoader dexClassLoader = new DexClassLoader(dexPath,dexReleaseFile.getAbsolutePath(),null,context.getClassLoader());
            Log.e("------" ,"插件加载成功 ---- " + file.length());
            return dexClassLoader;
        } else {
            Log.e("------","插件加载失败，没有找到插件");
            return null;
        }
    }

    public static void hookPackageManagerGetPackageInfo(Context context) {
        try {
            Field contextImplActivityThreadField = null;
            contextImplActivityThreadField = context.getClass().getDeclaredField("mMainThread");
            contextImplActivityThreadField.setAccessible(true);
            Object activityThreadObj = contextImplActivityThreadField.get(context);

            Field iPackageManagerField = activityThreadObj.getClass().getDeclaredField("sPackageManager");
            iPackageManagerField.setAccessible(true);
            Object iPackageManagerObj = iPackageManagerField.get(null);

            Object iPackageManagerProxy = Proxy.newProxyInstance(context.getClassLoader(),iPackageManagerObj.getClass().getInterfaces(),new IPackageManagerProxy(iPackageManagerObj));
            iPackageManagerField.set(activityThreadObj,iPackageManagerProxy);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
