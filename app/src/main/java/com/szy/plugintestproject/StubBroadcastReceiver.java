package com.szy.plugintestproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.szy.plugininterfacesmodule.Constants;
import com.szy.plugintestproject.hook.receiver.BroadcastDynamicRegisterHooker;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by songzhiyang on 2019/1/22.
 *
 * @author songzhiyang
 */
public class StubBroadcastReceiver extends BroadcastReceiver {

    public static final String HOST_RECEIVER_1 = "host_receiver_1";
    public static final String HOST_RECEIVER_2 = "host_receiver_2";

    //用来存储宿主的receiver的action和插件的receiver的action的对应关系
    private static final Map<String, String> receiverMapping = new HashMap<>();

    static {
        receiverMapping.put(HOST_RECEIVER_1, Constants.BroadcastReceiverConstants.MANIFEST_REGISTER_PLUGIN_BROADCAST_RECEIVER1);
        receiverMapping.put(HOST_RECEIVER_2, Constants.BroadcastReceiverConstants.MANIFEST_REGISTER_PLUGIN_BROADCAST_RECEIVER2);
    }

    public static void loadPluginReceivers(Context context) {
        BroadcastDynamicRegisterHooker.dynamicRegiterPluginBroadcastReceiver(context,"plugina.apk");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        context.sendBroadcast(new Intent(receiverMapping.get(action)));
    }
}
