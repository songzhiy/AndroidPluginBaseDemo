package com.szy.plugintestproject.hook.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageItemInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.szy.plugintestproject.hook.constants.HookConstants;

import java.lang.reflect.Field;

/**
 * Created by songzhiyang on 2019/1/11.
 *
 * @author songzhiyang
 */
public class ActivityThreadHandlerHooker {

    public static void hookActivityThreadHooker(Context context) {
        try {
            Class activityThreadClazz = Class.forName("android.app.ActivityThread");
            Field handlerField = activityThreadClazz.getDeclaredField("mH");
            handlerField.setAccessible(true);
            Field activityThreadField = context.getClass().getDeclaredField("mMainThread");
            activityThreadField.setAccessible(true);
            Object activityThreadObj = activityThreadField.get(context);
            Object handlerObj = handlerField.get(activityThreadObj);
            Field callbackField = Handler.class.getDeclaredField("mCallback");
            callbackField.setAccessible(true);
            callbackField.set(handlerObj, new HookCallback());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}

class HookCallback implements Handler.Callback {

    @Override
    public boolean handleMessage(Message msg) {
        Log.e("------", "HookCallback");
        switch (msg.what) {
            case 100://lauch activity
                //替换回原有的intent
                Field intentField = null;
                try {
                    intentField = msg.obj.getClass().getDeclaredField("intent");
                    intentField.setAccessible(true);
                    Intent intent = (Intent) intentField.get(msg.obj);
                    Intent realIntent = (Intent) intent.getParcelableExtra(HookConstants.INTENT_EXTRA_REAL_INTENT);
                    intent.setComponent(realIntent.getComponent());
                    // TODO: 2019/1/17 只有在命中缓存的情况下才应该走这里 替换packageName
                    Field activityInfoField = msg.obj.getClass().getDeclaredField("activityInfo");
                    activityInfoField.setAccessible(true);
                    ActivityInfo activityInfoObj = (ActivityInfo) activityInfoField.get(msg.obj);
                    ApplicationInfo applicationInfoObj = activityInfoObj.applicationInfo;
                    applicationInfoObj.packageName = "com.szy.plugina";
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                break;
            case 114://create service
                //替换回原有的service intent
                try {
                    Object createServiceDataObj = msg.obj;
                    Field createServiceDataServiceInfoField = createServiceDataObj.getClass().getDeclaredField("info");
                    createServiceDataServiceInfoField.setAccessible(true);
                    Object serviceInfoObj = createServiceDataServiceInfoField.get(createServiceDataObj);
                    Field serviceInfoNameField = PackageItemInfo.class.getDeclaredField("name");
                    serviceInfoNameField.setAccessible(true);
                    //这里应该从缓存里获取对应的service 这里因为demo的原因 直接写死了
                    serviceInfoNameField.set(serviceInfoObj,"com.szy.plugina.PluginAServiceA");
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            case 116://stop service
                //这里不需要hook 因为通过查看源码可以知道
                //在启动的时候进行了替换成stub service后 AMS会找到其对应的token
                //然后将这个token发送给ActivityThread 然后从mServices中查找对应的service
                //而在service创建的过程中 是当service创建完 才会将token和service的对应关系放到mService中
                //这样mServices中的service存放的已经是替换过的插件里的service了
                //所以在停止的时候 根据token找到的也是插件中的service 因此不需要在这里进行hook
                break;
            case 121://bind service
                //具体参考stop service的原因 均是从mServices这个缓存中获取出来的，因此无需hook
                break;
            case 122://unbind service
                //具体参考stop service的原因 均是从mServices这个缓存中获取出来的，因此无需hook
                break;
            default:
                break;
        }
        return false;
    }
}
