package com.szy.plugintestproject.hook.activity;

import android.content.Context;
import android.content.Intent;
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
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        return false;
    }
}
