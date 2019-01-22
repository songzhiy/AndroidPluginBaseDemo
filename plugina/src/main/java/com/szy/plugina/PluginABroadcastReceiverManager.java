package com.szy.plugina;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.szy.plugininterfacesmodule.IBroadcastReceiverRegister;

import static com.szy.plugininterfacesmodule.Constants.BroadcastReceiverConstants.DYNAMIC_REGISTER_PLUGIN_BROADCAST_RECIEVER;

/**
 * Created by songzhiyang on 2019/1/22.
 *
 * @author songzhiyang
 */
public class PluginABroadcastReceiverManager implements IBroadcastReceiverRegister{

    private BroadcastReceiver mDynamicBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("------","插件动态注册广播接收者成功！");
        }
    };

    @Override
    public void registerBroadcastReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DYNAMIC_REGISTER_PLUGIN_BROADCAST_RECIEVER);
        context.registerReceiver(mDynamicBroadcastReceiver,intentFilter);
    }
}
